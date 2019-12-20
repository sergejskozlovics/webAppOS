package lv.lumii.tda.adapters.repository.ar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.lumii.tda.adapters.repository.ar.IARMemory.ActionsIterator;
//
import lv.lumii.tda.raapi.*;

public class RepositoryAdapter extends RepositoryAdapterBase implements IRepository, RAAPI_WR, RAAPI_Synchronizable  {
	
	private static Logger logger =  LoggerFactory.getLogger(RepositoryAdapter.class);
	
	
	
	private static boolean DEBUG_LINKS = false;
	
	/*	
	private String collectClassNames(long rObject) {
		LinkedList<Long> classes = new LinkedList<Long>();
		collectDirectObjectClasses(rObject, classes);
		String retVal = "";
		for (Long rCls : classes)
			retVal+=getClassName(rCls)+" ";
		return retVal;
	}
	private void debugAction(int i) {
		double op=mem.actions_get(i);
		int n = getOpSize(op);
		logger.debug("DEBUG ACTION: op="+op+" args=");
		// general case...
		for (int k=1;k<=n;k++)
			logger.debug(mem.actions_get(i+k)+" ");
		// specific info...
		if (op==0x04) {
			logger.debug(" attrName="+getAttributeName((long)mem.actions_get(i+2)));
			logger.debug(" type="+collectClassNames((long)mem.actions_get(i+1))+" ");
		}
		int si = mem.a2s_get(i);
		if ((si>=0) && (mem.strings_get(si)!=null)) {
			logger.debug(" string="+mem.strings_get(si));
		}
		else
			logger.debug(" no string");
		
	}
*/	
	public static IRepository create()
	{
		return new RepositoryAdapter();
	}
	
	private String openRepositoryPath = null;
	
	private static long STRING_REFERENCE = 1;
	private static long INTEGER_REFERENCE = 2;
	private static long REAL_REFERENCE = 3;
	private static long BOOLEAN_REFERENCE = 4;
	/*package*/static final long MAX_PRIMITIVE_REFERENCE = 4;
	
	
	
	
	private IARMemory mem = null;

	private long lastIt = 0;
	private Map<Long, MyIterator > activeIteratorsMap= new HashMap<Long, MyIterator>();
	
	private String readIntoMemory(String uri) { // vmesto reallocate...
		// uri may be:
		// * just dir name (for ARMemory/ARMemory arrays)
		// * shmserver:dir
		// * shmclient:dir
		
		
		lastIt = 0;
		activeIteratorsMap.clear();
		
		String retVal;
		
		if (uri.startsWith("shmclient:")) {
			// shm client
			retVal = uri.substring(10);
			mem = new ARSharedMemoryArrays(retVal, false);
			if (!mem.reallocateFromCache()) {
				logger.debug("AR SHM CLIENT LOAD FROM CACHE FAILED.");
				mem = null;
				return null;
			}
			logger.debug("AR SHM CLIENT LOADED FROM CACHE "+retVal);
			
			return retVal;
		}
		else
		if (uri.startsWith("shmserver:")) {
			// shm server
			retVal = uri.substring(10);
			mem = new ARSharedMemoryArrays(retVal, true);
			if (mem.reallocateFromCache()) {
				logger.debug("AR SHM SERVER LOADED FROM CACHE "+retVal);
				return retVal;
			}
			logger.debug("AR SHM SERVER TO BE LOADED FROM FILES "+retVal);
			
			// otherwise continue with reading the ar.actions and ar.strings files into memory
		}
		else
		if (uri.startsWith("javamaps:")){
			// just a directory...
			retVal = uri.substring(9);
			mem = new ARJavaMapsMemory();//new ARJavaMapsMemory();//ARMemoryArrays();
			logger.debug("AR (JAVA MAPS) TO BE LOADED FROM FILES "+retVal);
		}
		else {
			// just a directory...
			retVal = uri;
			mem = new ARMemoryArrays();//new ARJavaMapsMemory();//ARMemoryArrays();
			logger.debug("AR (ORDINARY) TO BE LOADED FROM FILES "+retVal);
		}

			
		// reading ar.actions and ar.strings into memory...
		File cur1 = new File(retVal+File.separator+"ar.actions");
		File cur2 = new File(retVal+File.separator+"ar.strings");
		
		if (cur1.exists()) {
			if (!cur1.isFile() || !cur1.canRead()) {
				logger.error("AR open: can't read repository in "+retVal);
				return null;
			}
		}
		else {
			mem.reallocate(0, 0);
			return retVal; // the repository does not exist; creating...
		}
		
		
		DataInputStream fr1 = null;
		BufferedReader fr2 = null;
		try {
		    try {
			  fr2 = new BufferedReader
			    		    (new InputStreamReader(new FileInputStream(cur2), StandardCharsets.UTF_8));
			  
			  CharBuffer buf = CharBuffer.allocate((int)cur2.length());
			  fr2.read(buf);
			  buf.rewind();
			  String[] strArr = buf.toString().split("`");
			  int strIndex = 0;
			  logger.debug("strArr.length="+strArr.length);
			  
		      fr1 = new DataInputStream(new BufferedInputStream(new FileInputStream(cur1)));		      		      
		      
		      if (fr1.readDouble() != 0xEE) {
		    	  logger.error("AR open: the first action is not 0xEE");
		    	  free(false);
		    	  return null;
		      }
		      
		      mem.reallocate((int)cur1.length()/8, strArr.length);
		      
		      try {
		    	  double op;
		    	  int len, i;
		    	  for (;;) {
		    		  op = fr1.readDouble();
		    		  len = getOpSize(op);
		    		  if (len<=0) { // error, wrong op
		    			  free(false);
		    			  return null;
		    		  }
	    			  double[] arr = new double[len+1];
	    			  arr[0] = op;
		    		  for (i=1; i<=len; i++) {
		    			  arr[i] = fr1.readDouble();
		    			  
		    			  if (arr[i] > mem.getMaxReference()) {
		    				mem.setMaxReference((long)arr[i]);
		    			  }
		    		  }
		    		  
		    		  if (isStringOp(op)) {
		    			  if (strIndex>=strArr.length) {
			    			  free(false);
			    			  return null;		    				  
		    			  }
		    				  
		    			  if ((op == 0x05) || (op == 0x15))
		    				mem.addRoleStringAction(RAAPI_Synchronizer.unsharpenString( strArr[strIndex++] ), arr);
		    			  else
		    				mem.addStringAction(RAAPI_Synchronizer.unsharpenString( strArr[strIndex++] ), arr);
		    		  }
		    		  else 
		    			  mem.addAction(arr);		 
		    		  //logger.debug("action #"+i+" "+arr[0]);
		    	  }
		      }
		      catch(Throwable t) {
		    	  // EOF
		      }
		      
		      logger.debug("AR LOADED OK.");
			  return retVal;
		    } catch (Throwable t) {
			    logger.debug("AR NOT LOADED.");
		    	free(false);
		    	return null;
		    }
		}
		finally {
			if (fr1!=null)
				try {
					fr1.close();
				} catch (IOException e) {
				}
			if (fr2!=null)
				try {
					fr2.close();
				} catch (IOException e) {
				}
		}
		
					
	}
	
	private void free(boolean clearCache) {
		
		
		lastIt = 0;
		activeIteratorsMap.clear();
		
		mem.free(clearCache);
		mem = null;		
	}
	
	
	/*package*/static boolean isStringOp(double op) {
		return (op==0x01) || (op==0x03) || (op==0x04) || (op==0x05) || (op==0x15) || (op==0x25);
	}
	
	private static int opsize[] = new int[255];
	
	static {
		opsize[0x01] = 1;
		opsize[0xF1] = 1;
		opsize[0x11] = 2;
		opsize[0xE1] = 2;
		opsize[0x02] = 2;
		opsize[0xF2] = 1;
		opsize[0x12] = 2;
		opsize[0xE2] = 2;
		opsize[0x22] = 2;
		opsize[0x03] = 3;
		opsize[0xF3] = 1;
		opsize[0x04] = 2;
		opsize[0xF4] = 2;
		opsize[0x05] = 5;
		opsize[0x15] = 4;
		opsize[0x25] = 3;
		opsize[0xF5] = 1;
		opsize[0x06] = 3;
		opsize[0x16] = 4;
		opsize[0xF6] = 3;
	}
	
	/*package*/static int getOpSize(double op) {
/*		if (op == 0x01)
			return 1;
		if (op == 0xF1)
			return 1;
		if (op == 0x11)
			return 2;
		if (op == 0xE1)
			return 2;
		if (op == 0x02)
			return 2;
		if (op == 0xF2)
			return 1;
		if (op == 0x12)
			return 2;
		if (op == 0xE2)
			return 2;
		if (op == 0x22)
			return 2;
		if (op == 0x03)
			return 3;
		if (op == 0xF3)
			return 1;
		if (op == 0x04)
			return 2;
		if (op == 0xF4)
			return 2;
		if (op == 0x05)
			return 5;
		if (op == 0x15)
			return 4;
		if (op == 0x25)
			return 3;
		if (op == 0xF5)
			return 1;
		if (op == 0x06)
			return 3;
		if (op == 0x16)
			return 4;
		if (op == 0xF6)
			return 3;
		return 0;*/
		return opsize[(int)op];
	}
	
	
	/**
	 * Searches for the action, where the given op has been performed on the given reference arguments (e.g., createLink on association reference, obj1 and obj2 references)
	 * @param op the operation (e.g., 0x01)
	 * @param arr references that were arguments to the given action (arguments may be in any position); may not be empty!
	 * @return the action index, or -1 if not found
	 */

	private int findActionWhere(double op, double... arr) {
		
		if (arr.length==2) {
			IARMemory.ActionsIterator it = mem.r2a_get_iterator(arr[0]);
			if (it==null)
				return -1;
			int ai = it.next();
			while (ai!=-1) {
				if (mem.actions_get(ai)==op) {
					int n = opsize[(int)op];
					boolean ok0=false, ok1=false;
					for (int i=1; i<=n; i++) {
						if (mem.actions_get(ai+i)==arr[0])
							ok0=true;
						if (mem.actions_get(ai+i)==arr[1])
							ok1=true;
					}
					if (ok0 && ok1)
						return ai;
				}
				ai = it.next();
			}
			return -1;
		}
		
		IARMemory.ActionsIterator[] itarr = new IARMemory.ActionsIterator[arr.length];
		int[] valarr = new int[arr.length];
		int i;
		int curmax=-1;
		for (i=0; i<arr.length; i++) {
			itarr[i] = mem.r2a_get_iterator(arr[i]);
			if (itarr[i]==null)
				return -1;
			valarr[i] = itarr[i].next();
						
			if (valarr[i] > curmax)
				curmax = valarr[i];
		}
	
		if (curmax<0)
			return -1;
		
		for (;;) {
			
			// moving iterators until all arr elements are found within the same index
			
			for (i=0; i<arr.length; i++) {
				
				if (valarr[i]<curmax)
					valarr[i] = itarr[i].nextGreaterOrEqual(curmax);
				
				if ((valarr[i]!=-1) && (valarr[i]<curmax))
					valarr[i] = itarr[i].next();
				
				if (valarr[i]==-1)
					return -1;
			}
			
			boolean ok = true;
			for (i=0; i<arr.length; i++) {
				if (valarr[i]!=curmax) {
					ok = false;
					curmax=valarr[i];
					break;
				}
			}
			
			if (ok) {
				// the final step: checking whether it is the correct action
				if (mem.actions_get(curmax)==op)
					return curmax; // found!
				
				// update curmax and move all iterators...
				for (i=0; i<arr.length; i++) {					
					valarr[i] = itarr[i].next();
					if (valarr[i]==-1)
						return -1;
					if (valarr[i] > curmax)
						curmax=valarr[i];
				}
			}
			else {
				// just update curmax...
				for (i=0; i<arr.length; i++) {
					if (valarr[i]==-1)
						return -1;
					if (valarr[i] > curmax)
						curmax=valarr[i];
				}
			}
		}
	}	

/*	private int findActionWhere(double op, double... arr) {
		
		IARMemory.ActionsIterator[] itarr = new IARMemory.ActionsIterator[arr.length];
		int[] valarr = new int[arr.length];
		int i;
		int curmax=-1;
		for (i=0; i<arr.length; i++) {
			itarr[i] = mem.r2a_get_iterator(arr[i]);
			if (itarr[i]==null)
				return -1;
			valarr[i] = itarr[i].next();
						
			if (valarr[i] > curmax)
				curmax = valarr[i];
		}
	
		if (curmax<0)
			return -1;
		
		for (;;) {
			
			// moving iterators until all arr elements are found within the same index
			
			for (i=0; i<arr.length; i++) {
				
				//if (valarr[i]<curmax)
					//valarr[i] = itarr[i].nextGreaterOrEqual(curmax);
				
				if ((valarr[i]!=-1) && (valarr[i]<curmax))
					valarr[i] = itarr[i].next();
				
				if (valarr[i]==-1)
					return -1;
			}
			
			boolean ok = true;
			for (i=0; i<arr.length; i++) {
				if (valarr[i]!=curmax) {
					ok = false;
					curmax=valarr[i];
					break;
				}
			}
			
			if (ok) {
				// the final step: checking whether it is the correct action
				if (mem.actions_get(curmax)==op)
					return curmax; // found!
				
				// update curmax and move all iterators...
				for (i=0; i<arr.length; i++) {					
					valarr[i] = itarr[i].next();
					if (valarr[i]==-1)
						return -1;
					if (valarr[i] > curmax)
						curmax=valarr[i];
				}
			}
			else {
				// just update curmax...
				for (i=0; i<arr.length; i++) {
					if (valarr[i]==-1)
						return -1;
					if (valarr[i] > curmax)
						curmax=valarr[i];
				}
			}
		}
	}*/	
	
	/**
	 * Searches for the action, where the given op is performed on the given string (e.g., createClass on the given name)
	 * @param op the operation (e.g., 0x01)
	 * @param s the string associated with the action (for createAssociation: either "role1/role2", or "role1", or "role2")
	 * @param isRoleName whether the string to be found is a role name
	 * @return the action index, or -1 if not found
	 */
	private int findActionWhere(double op, String s, boolean isRoleName) {
		ActionsIterator it = mem.s2a_get(s, isRoleName);
		if (it == null)
			return -1;
		int i;
		while ((i=it.next())!=-1) {
			if (mem.actions_get(i)==op)
				return i;		
		}
		return -1;				
	}

		
	private int findWhere(double r) {
		return mem.r2a_get_first(r);
	}
			
	
	
	/**
	 * Searches for the action, where the given op is performed on the given reference and string (e.g., createAssociation on the given class reference and role name)
	 * @param op the operation (e.g., 0x05)
	 * @param s the string associated with the action (for createAssociation: either "role1/role2", or "role1", or "role2")
	 * @param r the reference that was any of arguments of the action
	 * @return the action index, or -1 if not found
	 */
	private int findActionWhere(double op, String s, double r, boolean isRoleName) {
		ActionsIterator it  = mem.s2a_get(s, isRoleName);
		if (it == null)
			return -1;
		int n = RepositoryAdapter.getOpSize(op);
		int i;
		while ((i=it.next())!=-1) {
			if (mem.actions_get(i)==op) {
				for (int k=1; k<=n; k++)
					if (mem.actions_get(i+k)==r)
						return i;
			}
		}
		return -1;				
	}
	
	// used from: excludeObjectFromClass, moveObject (via excludeObjectFromClass call)
	private void detachObjectFromClass(Collection<Long> rObjects, long rClass) { 
		Set<Long> classes = this.collectSuperClasses(rClass);
		Set<Long> attrs = new HashSet<Long>();
		Set<Long> assocEnds = new HashSet<Long>();
		for (Long l : classes) {
			this.collectDirectAttributes(l, attrs);
			this.collectDirectAndInverseAssociationEnds(l, assocEnds);
		}
		
		for (Long rObject : rObjects) {
			IARMemory.ActionsIterator rait = mem.r2a_get_iterator(rObject);
			if (rait != null) { 
				List<Integer> toDelete = new LinkedList<Integer>();
				int k;
				while ((k=rait.next())!=-1) { 
					if (mem.actions_get(k)==0x04) { // attribute value
						if (attrs.contains((long)mem.actions_get(k+1)))
							toDelete.add(k);
					}
					if (mem.actions_get(k)==0x06) { // link
						if (assocEnds.contains((long)mem.actions_get(k+3)))
							toDelete.add(k);
					}
				}
				for (Integer kk: toDelete)
					mem.deleteSimpleAction(kk);
			}
		}
	}
	
	private void detachObjectFromClass(long rObject, long rClass) {
		LinkedList<Long> a = new LinkedList<Long>();
		a.add(rObject);
		detachObjectFromClass(a, rClass);
	}
	
/*	private List<Integer> r2a_get(double r) {
		ActionsIterator it = mem.r2a_get_iterator(r);
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		int i=it.next();
		while (i!=-1) {
			if (mem.actions_get(i)!=0x00)
				retVal.add(i);
			i=it.next();
		}
		return retVal;
	}*/
	
	// used from: deleteClass, deleteObject
	private boolean deleteObjectRecursively(double rObject) {
		List<Integer> list = mem.r2a_get_and_remove(rObject);
			// the mem.actions array element will be removed during rearrangemem.actions;
			// here we exclude from the whereReferenceMap to avoid infinite recursion
		if (list == null)
			return false;
		
		List<Integer> actionsToDelete = new LinkedList<Integer>();
		List<Double> linkedToDelete = new LinkedList<Double>();
		for (Integer i : list) {
			if (mem.actions_get(i)==0x06) { // createLink; check for objects linked via composition
				long rAssoc = (long)mem.actions_get(i+3);
				if (isComposition(rAssoc)) {
					linkedToDelete.add(mem.actions_get(i+2));
				}
				else {
					long rInv = this.getInverseAssociationEnd(rAssoc);
					if (isComposition(rInv))
						linkedToDelete.add(mem.actions_get(i+1));
				}
			}
			
			actionsToDelete.add(i); // delete this action anyway				
		}
		
		for (Integer i : actionsToDelete) {
			mem.deleteSimpleAction(i);
		}
		
		for (Double d : linkedToDelete) {
			deleteObjectRecursively(d);
		}
		
		
		return true;
	}
	
	public RepositoryAdapter()
	{
		//reinitialize(0, 0);
	}

	@Override
	synchronized public long findPrimitiveDataType (String name)
	{
		if (name.toLowerCase().equals("string"))
			return STRING_REFERENCE;
		if (name.toLowerCase().equals("int") || name.toLowerCase().equals("integer"))
			return INTEGER_REFERENCE;
		if (name.toLowerCase().equals("float") || name.toLowerCase().equals("real"))
			return REAL_REFERENCE;
		if (name.toLowerCase().equals("bool") || name.toLowerCase().equals("boolean")) 
			return BOOLEAN_REFERENCE;
		return 0;
	}
	
	@Override
	synchronized public String getPrimitiveDataTypeName (long rDataType)
	{
		if (rDataType == INTEGER_REFERENCE)
			return "Integer";
		if (rDataType == REAL_REFERENCE)
			return "Real";
		if (rDataType == BOOLEAN_REFERENCE)
			return "Boolean";
		if (rDataType == STRING_REFERENCE)
			return "String";
		return null;		
	}
	
	@Override
	synchronized public boolean isPrimitiveDataType (long r)
	{
		return
			(r == INTEGER_REFERENCE)
		 || (r == REAL_REFERENCE)
		 || (r == BOOLEAN_REFERENCE)
		 || (r == STRING_REFERENCE);
	}
	

	@Override
	synchronized public long createClass(String name) {
		if (mem==null)
			return 0;
		mem.lock();
		try {
			if (name == null)
				return 0;
			if (findClass(name)!=0)
				return 0;
			
			long r = mem.newReference();
			mem.addStringAction(name, 0x01, r);	
					
			
			return r;
		}
		finally {
			mem.unlock();
		}
	}

	@Override // RAAPI_WR
	synchronized public boolean createClass(String name, long r) {
		if (mem==null)
			return false;
		mem.lock();
		try {
			if (name == null)
				return false;
			if (findClass(name)!=0)
				return false;
			
			mem.addStringAction(name, 0x01, r);
			
			return true;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long findClass(String name) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			int i = findActionWhere(0x01, name, false);
			if (i<0)
				return 0;
			else
				return (long)mem.actions_get(i+1);
		}
		finally {
			mem.unlock();
		}
	}
	@Override
	synchronized public String getClassName(long rClass) {
		if (mem==null)
			return null;
		 mem.lock();
		try {
			int i = findWhere(rClass);
			if ((i<0) || (mem.actions_get(i)!=0x01)) {
				return null;
			}
			int ii = mem.a2s_get(i);
			
			return (ii<0)?null:mem.strings_get(ii);
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean deleteClass(long rClass) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findWhere(rClass);
			if ((i<0) || (mem.actions_get(i)!=0x01))
				return false;
			
			List<Long> arr = new LinkedList<Long>();
			for (Long rCls : collectSubClasses(rClass)) {
				collectDirectClassObjects(rCls, arr);
			}
			
			for (Long rObj : arr) {
				deleteObjectRecursively(rObj);
			}
			
			
			List<Integer> list = mem.r2a_get_and_remove((double)rClass);
			if (list==null)
				return false;
			for (Integer k : list) {
				mem.deleteSimpleAction(k);
			}
			
					
			return true;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public boolean isClass (long r)
	{
		if (mem==null)
			return false;
		 mem.lock();
		try {
			if (r==0)
				return false;
			int i = findWhere(r);
			if ((i<0)||(mem.actions_get(i)!=0x01))
				return false; // not createClass action
			return true;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long createObject(long rClass) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			if (!isClass(rClass))
				return 0;
			
			long r = mem.newReference();
			mem.addAction(0x02, rClass, r);	
			return r;
		}
		finally {
			mem.unlock();
		}
	}

	@Override // RAAPI_WR
	synchronized public boolean createObject(long rClass, long r) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			if (!isClass(rClass))
				return false;
			mem.addAction(0x02, rClass, r);		
			return true;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public boolean isTypeOf(long rObject, long rClass) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			Set<Long> arr = new HashSet<Long>();
			collectDirectObjectClasses(rObject, arr);
			return arr.contains(rClass);
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean isKindOf(long rObject, long rClass) {
		if (mem==null)
			return false;
		mem.lock();
		try {
			ArrayList<Long> directSet = new ArrayList<Long>();
				
			collectDirectObjectClasses(rObject, directSet);
			
			if (directSet.contains(rClass))
				return true;
			
			for (Long rCls : directSet) {
				if (collectSuperClasses(rCls).contains(rClass))
					return true;
			}
			
			return false;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public boolean deleteObject(long rObject) {		
		if (mem==null)
			return false;
		 mem.lock();
		try {
			boolean retVal = deleteObjectRecursively(rObject);
			return retVal;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean includeObjectInClass(long rObject, long rClass) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findActionWhere(0x02, rObject, rClass);
			if (i>=0) {
				// need to include in the first class; searching for the exclude-action...
				int j = mem.hasMultiTypedObjects()?findActionWhere(0xE2, rObject, rClass):-1;
				if (j>=0)
					mem.deleteSimpleAction(j); // just delete this exclude-action
				return true;
			}
			else {		
				// check if not already included
				int i2 = findActionWhere(0x12, rObject, rClass);
				if (i2>=0)
					return false;
				mem.addAction(0x12, rObject, rClass);
				return true;
			}
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean excludeObjectFromClass(long rObject, long rClass) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findActionWhere(0x02, rObject, rClass);
			if (i>=0) {
				// need to exclude from the first class...
				int j = mem.hasMultiTypedObjects()?findActionWhere(0xE2, rObject, rClass):-1;
				if (j>=0)
					return true; // already excluded
	
				detachObjectFromClass(rObject, rClass);
				
				mem.addAction(0xE2, rObject, rClass); // adding the exclude-action
				return true;
			}
			else {		
				int i2 = findActionWhere(0x12, rObject, rClass);
				if (i2<0)
					return false;
				
				detachObjectFromClass(rObject, rClass);
				mem.deleteSimpleAction(i2);
			}
			
			return true;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean moveObject(long rObject, long rToClass)
	{
		if (mem==null)
			return false;
		 mem.lock();
		try {
			IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rObject);
			if (rait == null)
				return false;
	
			List<Long> otherClasses = new LinkedList<Long>();
			long rFirstClass = 0;
			int i;
			while ((i=rait.next())!=-1) {
				if (mem.actions_get(i)==0x02) {
					if (mem.actions_get(i+2)==rObject)
						rFirstClass = (long)mem.actions_get(i+1);
				}
				if (mem.actions_get(i)==0x12) {
					if (mem.actions_get(i+1)==rObject)
						otherClasses.add((long)mem.actions_get(i+2));
				}
			}
			
			if (rFirstClass != 0)
				excludeObjectFromClass(rObject, rFirstClass);
			
			boolean ok = true;
			for (Long l : otherClasses)
				if (!excludeObjectFromClass(rObject, l)) 
					ok = false;
			
			if (!includeObjectInClass(rObject, rToClass))
				ok = false;
			return ok;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long createAttribute(long rClass, String name, long type) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			if (type == 0)
				return 0;
			
			for (Long rCls : collectSuperClasses(rClass)) {
				if ( findActionWhere(0x03, name, rCls, false)>= 0 )
					return 0; // already exists in this class or some super class
			}
			
			long r = mem.newReference();
			mem.addStringAction(name, 0x03, rClass, type, r);
			return r;
		}
		finally {
			mem.unlock();
		}
	}

	@Override // RAAPI_WR
	synchronized public boolean createAttribute(long rClass, String name, long type, long r) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			if (type == 0)
				return false;
					
			for (Long rCls : collectSuperClasses(rClass)) {
				if ( findActionWhere(0x03, name, rCls, false)>= 0 )
					return false; // already exists in this class or some super class
			}
			
			mem.addStringAction(name, 0x03, rClass, type, r);
			return true;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long findAttribute(long rClass, String name) {
		if (mem==null)
			return 0;
		mem.lock();
		try {
			int i = findActionWhere(0x03, name, rClass, false);
			if (i>=0)
				return (long)mem.actions_get(i+3);			
			
			
			for (Long rCls : collectSuperClasses(rClass)) {
				i = findActionWhere(0x03, name, rCls, false);
				if (i>=0)
					return (long)mem.actions_get(i+3);			
			}
					
			return 0;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public String getAttributeName(long rAttribute) {		
		if (mem==null)
			return null;
		 mem.lock();
		try {
			int i = findWhere(rAttribute);
			if ((i<0) || (mem.actions_get(i)!=0x03))
				return null;
			int si = mem.a2s_get(i);
			return (si<0)?null:mem.strings_get(si);
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getAttributeDomain (long rAttribute)
	{
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			int i = findWhere(rAttribute);
			if ((i<0) || (mem.actions_get(i)!=0x03)) // not createAttribute
				return 0;
			
			return (long)mem.actions_get(i+1);
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long getAttributeType(long rAttribute) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			int i = findWhere(rAttribute);
			if ((i<0) || (mem.actions_get(i)!=0x03)) // not createAttribute
				return 0;
			
			return (long)mem.actions_get(i+2);
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public boolean isAttribute (long r)
	{		
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findWhere(r);
			if ((i<0) || (mem.actions_get(i)!=0x03)) // not createAttribute
				return false;
			return true;
		}
		finally {
			mem.unlock();
		}
	}
	

	@Override
	synchronized public boolean deleteAttribute(long rAttribute) {	
		if (mem==null)
			return false;
		 mem.lock();
		try {
			List<Integer> list = mem.r2a_get_and_remove(rAttribute);
			if (list==null)
				return false;
			for (Integer k : list)
				mem.deleteSimpleAction(k);
			
			return true;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean setAttributeValue(long rObject, long rAttribute, String value) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			// Deleting previous values...
			deleteAttributeValue(rObject, rAttribute);
			
			if (value==null)
				return true;
			
			mem.addStringAction(value, 0x04, rObject, rAttribute);
			return true;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public String getAttributeValue(long rObject, long rAttribute) {
		if (mem==null)
			return null;
		mem.lock();
		try {
			int i = findActionWhere(0x04, rObject, rAttribute);
			if (i<0)
				return null;
			int si = mem.a2s_get(i);
			return si<0?null:mem.strings_get(si);
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean deleteAttributeValue(long rObject, long rAttribute) {
		if (mem==null)
			return false; 	
		mem.lock();
		try {
			int i = findActionWhere(0x04, rObject, rAttribute);
			if (i<0)
				return false;
			mem.deleteSimpleAction(i);
			return true;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long createAssociation(long rSourceClass, long rTargetClass,
			String sourceRole, String targetRole, boolean isComposition) {
		if (mem==null)
			return 0;
		mem.lock();
		try {				
			if (findAssociationEnd(rSourceClass, targetRole)!=0)
				return 0;
			if (findAssociationEnd(rTargetClass, sourceRole)!=0)
				return 0;
			
			long r = mem.newReference();
			long rInv = mem.newReference();
			
			mem.addRoleStringAction(sourceRole+"/"+targetRole, 0x05, rSourceClass, rTargetClass, isComposition?1:0, r, rInv);
			return r;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override // RAAPI_WR
	synchronized public boolean createAssociation(long rSourceClass, long rTargetClass,
			String sourceRole, String targetRole, boolean isComposition, long r, long rInv) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			if (findAssociationEnd(rSourceClass, targetRole)!=0)
				return false;
			if (findAssociationEnd(rTargetClass, sourceRole)!=0)
				return false;
		
			mem.addRoleStringAction(sourceRole+"/"+targetRole, 0x05, rSourceClass, rTargetClass, isComposition?1:0, r, rInv);
			return true;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long createDirectedAssociation(long rSourceClass, long rTargetClass,
			String targetRole, boolean isComposition) {
				
		if (mem==null)
			return 0;
		mem.lock();
		try {
			if (findAssociationEnd(rSourceClass, targetRole)!=0)
				return 0;
							
			long r = mem.newReference();
			
			mem.addRoleStringAction("/"+targetRole, 0x15, rSourceClass, rTargetClass, isComposition?1:0, r);
			return r;
		}
		finally {
			mem.unlock();
		}
	}

	@Override // RAAPI_WR
	synchronized public boolean createDirectedAssociation(long rSourceClass, long rTargetClass,
			String targetRole, boolean isComposition, long r) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			if (findAssociationEnd(rSourceClass, targetRole)!=0)
				return false;
					
			mem.addRoleStringAction("/"+targetRole, 0x15, rSourceClass, rTargetClass, isComposition?1:0, r);
			return true;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long createAdvancedAssociation(String name, boolean nAry, boolean associationClass) {
		
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			int i = findActionWhere(0x25, name, false);
			if (i>=0)
				return 0;
					
			long r = mem.newReference();
			
			mem.addStringAction(name, 0x25, nAry?1:0, associationClass?1:0, r);
			return r;
		}
		finally {
			mem.unlock();
		}
	}

	@Override // RAAPI_WR
	synchronized public boolean createAdvancedAssociation(String name, boolean nAry, boolean associationClass, long r) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findActionWhere(0x25, name, false);
			if (i>=0)
				return false;
			
			mem.addStringAction(name, 0x25, nAry?1:0, associationClass?1:0, r);
			return true;
		}
		finally {
			mem.unlock();
		}
	}
	
	private long findDirectAssociationEnd(long rCls, String targetRole) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ActionsIterator it = mem.s2a_get(targetRole, true);
			if (it == null) {
				logger.debug("AR findAssociationEnd: it==null");
				return 0;
			}
			
			int i;
			while ((i=it.next())!=-1) {
				int si = mem.a2s_get(i);
				if (si<0)
					continue;
				if (mem.actions_get(i)==0x05) {
					if ((mem.actions_get(i+1)==rCls) && (mem.strings_get(si).endsWith("/"+targetRole))) // direct association?
						return (long)mem.actions_get(i+4);
					if ((mem.actions_get(i+2)==rCls) && (mem.strings_get(si).startsWith(targetRole+"/"))) // inverse association?
						return (long)mem.actions_get(i+5);
				}
				else
				if (mem.actions_get(i)==0x15) {
					if ((mem.actions_get(i+1)==rCls) && (mem.strings_get(si).endsWith("/"+targetRole))) // direct association?
						return (long)mem.actions_get(i+4);
					if ((mem.actions_get(i+2)==rCls) && (mem.strings_get(si).startsWith(targetRole+"/"))) // inverse association?
						return (long)mem.actions_get(i+5);
				}
			}
			return 0;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long findAssociationEnd(long rSourceClass, String targetRole) {
		if ((targetRole == null) || (rSourceClass==0))
			return 0;
		
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			
			long retVal = findDirectAssociationEnd(rSourceClass, targetRole);
			if (retVal!=0)
				return retVal;
			
			for (Long rCls : collectSuperClasses(rSourceClass)) {
				retVal = findDirectAssociationEnd(rCls, targetRole);
				if (retVal!=0)
					return retVal;
			}
			
			return 0;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getInverseAssociationEnd(long rAssociation) {
		if (rAssociation==0)
			return 0;
		
		if (mem==null)
			return 0;
		 mem.lock();
		try {
	
			int i = findWhere(rAssociation);
			if (i<0)
				return 0;
			
			if (mem.actions_get(i)==0x15)
				return 0; // directed association
			
			if (mem.actions_get(i+4)==rAssociation)
				return (long)mem.actions_get(i+5);
			if (mem.actions_get(i+5)==rAssociation)
				return (long)mem.actions_get(i+4);
			return 0;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getSourceClass(long rAssociation) {
		if (rAssociation==0)
			return 0;

		if (mem==null)
			return 0;
		 mem.lock();
		try {
			int i = findWhere(rAssociation);
			if (i<0)
				return 0;
			
			if (mem.actions_get(i+4)==rAssociation)
				return (long)mem.actions_get(i+1);
			if (mem.actions_get(i+5)==rAssociation)
				return (long)mem.actions_get(i+2);
			return 0;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getTargetClass(long rAssociation) {
		if (rAssociation==0)
			return 0;

		if (mem==null)
			return 0;
		 mem.lock();
		try {
			int i = findWhere(rAssociation);
			if (i<0)
				return 0;
			
			if (mem.actions_get(i+4)==rAssociation)
				return (long)mem.actions_get(i+2);
			if (mem.actions_get(i+5)==rAssociation)
				return (long)mem.actions_get(i+1);
			
			return 0;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public String getRoleName(long rAssociation) {
		if (rAssociation==0)
			return null;

		if (mem==null)
			return null;
		 mem.lock();
		try {
			int i = findWhere(rAssociation);
			if (i<0)
				return null;
			
			int si = mem.a2s_get(i);
			if ((si<0) || (mem.strings_get(si)==null))
				return null;
			
			int k = mem.strings_get(si).indexOf('/');
			if (k<0)
				return null;
			
			if (mem.actions_get(i+4)==rAssociation)
				return mem.strings_get(si).substring(k+1);
			if (mem.actions_get(i+5)==rAssociation)
				return mem.strings_get(si).substring(0, k);
			return null;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean isComposition(long rAssociation) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findWhere(rAssociation);
			if (i<0)
				return false;
			
			if (mem.actions_get(i)==0x05) // bi-directional association
				return (mem.actions_get(i+4)==rAssociation) && (mem.actions_get(i+3)==1.0);
			
			if (mem.actions_get(i)==0x15) // directed association
				return mem.actions_get(i+3)==1.0;
			return false;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public boolean isAdvancedAssociation (long r)
	{
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findWhere(r);
			if (i<0)
				return false;
			
			return (mem.actions_get(i)==0x25); // advanced association
		}
		finally {
			mem.unlock();
		}
	}
	 
	@Override
	synchronized public boolean isAssociationEnd (long r)
	{
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findWhere(r);
			if (i<0)
				return false;
			
			if (mem.actions_get(i)==0x05)
				return (mem.actions_get(i+4)==r) || (mem.actions_get(i+5)==r);
			if (mem.actions_get(i)==0x15)
				return (mem.actions_get(i+4)==r);
			
			return false;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean deleteAssociation(long r) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			long rInv = this.getInverseAssociationEnd(r);
			List<Integer> list = mem.r2a_get_and_remove(r);
			List<Integer> list2 = mem.r2a_get_and_remove(rInv);
			if ((list == null) && (list2==null))
				return false;
			
			if (list!=null) {
				for (Integer k : list)
					mem.deleteSimpleAction(k);
			}
			if (list2!=null) {
				for (Integer k : list2)
					mem.deleteSimpleAction(k);
			}
			
			return true;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean createLink(long rSourceObject, long rTargetObject,
			long rAssociation) {
				
		if (mem==null)
			return false;
		 mem.lock();
		try {
			if (linkExists(rSourceObject, rTargetObject, rAssociation))
				return false;
	 			
			mem.addAction(0x06, rSourceObject, rTargetObject, rAssociation);
			return true;
		}
		finally {
			mem.unlock();
		}
	}

	
	@Override
	synchronized public boolean createOrderedLink(long rSourceObject, long rTargetObject,
			long rAssociation, int position) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectLinkedObjects(rSourceObject, rAssociation, arr);
			
			if (arr.size()<=position)
				return createLink(rSourceObject, rTargetObject, rAssociation);
			
			if (position<0)
				position = arr.size()+position;
			if (position<0)
				position = 0;
			
			for (int i=position; i<arr.size(); i++)
				deleteLink(rSourceObject, rTargetObject, rAssociation);
			
			boolean retVal = createLink(rSourceObject, rTargetObject, rAssociation);
			
			for (int i=position; i<arr.size(); i++)
				createLink(rSourceObject, rTargetObject, rAssociation);
			
			return retVal;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public boolean linkExists(long rSourceObject, long rTargetObject,
			long rAssociation) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findActionWhere(0x06, rSourceObject, rTargetObject, rAssociation);
			if (i>=0)
				return true; 
			long rInv = getInverseAssociationEnd(rAssociation);
			if (rInv!=0) {
				int j = findActionWhere(0x06, rTargetObject, rSourceObject, rInv);
				if (j>=0)
					return true;
			}
			return false;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public int getLinkedObjectPosition(long rSourceObject, long rTargetObject,
			long rAssociation) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectLinkedObjects(rSourceObject, rAssociation, arr);
			for (int i=0; i<arr.size(); i++)
				if (arr.get(i)==rTargetObject)
					return i;
			
			return -1;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public boolean deleteLink(long rSourceObject, long rTargetObject,
			long rAssociation) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			int i = findActionWhere(0x06, rSourceObject, rTargetObject, rAssociation);
			if (i>=0) {
				mem.deleteSimpleAction(i);
				return true;
			}
			long rInv = getInverseAssociationEnd(rAssociation);
			if (rInv!=0) {
				int j = findActionWhere(0x06, rTargetObject, rSourceObject, rInv);
				if (j>=0) {
					mem.deleteSimpleAction(j);
					return true;
				}
			}
			return false;
		}
		finally {
			mem.unlock();
		}
	}
	
	
	@Override
	synchronized public boolean createGeneralization(long rSubClass, long rSuperClass) {		
		if (mem==null)
			return false;
		if (rSubClass==0 || rSuperClass==0)
			return false;
		
		mem.lock();
		try {
			int i = findActionWhere(0x11, rSubClass, rSuperClass);
			if (i>=0)
				return false; // already exists
			
			mem.addAction(0x11, rSubClass, rSuperClass);
			return true;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean isDirectSubClass(long rSubClass, long rSuperClass) {
		if (mem==null)
			return false;
		if ((rSubClass == 0) || (rSuperClass == 0))
			return false;
		
		mem.lock();
		try {
			
			int i = findActionWhere(0x11, rSubClass, rSuperClass);
			return i>=0;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean isDerivedClass(long rDirectlyOrIndirectlyDerivedClass,
			long rSuperClass) {
		if (mem==null)
			return false;
		 mem.lock();
		try {
			if ((rDirectlyOrIndirectlyDerivedClass == 0) || (rSuperClass == 0))
				return false;
	
			long it = this.getIteratorForDirectSubClasses(rSuperClass);
			if (it == 0)
				return false;
			
			long r = this.resolveIteratorFirst(it);
			while (r != 0) {
				if (r == rDirectlyOrIndirectlyDerivedClass) {
					break;
				}
				if (isDerivedClass(rDirectlyOrIndirectlyDerivedClass, r)) {
					break;
				}
				r = this.resolveIteratorNext(it);
			}
			
			this.freeIterator(it);
			
			return (r != 0);
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public boolean deleteGeneralization(long rSubClass, long rSuperClass) {
		if ((rSubClass == 0) || (rSuperClass == 0))
			return false;
		
		if (mem==null)
			return false;
		
		mem.lock();
		try {
			int i = findActionWhere(0x11, rSubClass, rSuperClass);
			if (i<0)
				return false;
			
			ArrayList<Long> arr = new ArrayList<Long>();
			for (Long rCls : collectSubClasses(rSubClass)) {
				collectDirectClassObjects(rCls, arr);
			}
			
			for (Long rObj : arr) {
				detachObjectFromClass(rObj, rSuperClass);
			}
			mem.deleteSimpleAction(i);
			
			return true;
		}
		finally {
			mem.unlock();
		}
	}

	private interface MyIterator extends Iterator<Long> {
		public int getSize();			
		public void free();
		public void copyOnWrite(); // currently, not used; may be used in the future for better performance (thus, a copied ArrayList will be used only when necessary,
								   // e.g., some write operation that affects the iterator is called; in all other cases, we just go through the mem.actions array)
		public long getAt(int i);
 	}
	
	private class ClassIterator implements MyIterator {
		private long it;
		private int pos=0;
		private long[] listCopy = null;
		
		public ClassIterator() {
			it = ++lastIt;
			pos = 0;
			activeIteratorsMap.put(it, this);
			
			listCopy = mem.getAllClasses();
		}
		
		public int getSize() {
			return listCopy.length;
		}
		
		public void free() {
			activeIteratorsMap.remove(it);
		}

		@Override
		public boolean hasNext() {
			return pos<listCopy.length;
		}

		@Override
		public Long next() {
			if (pos<listCopy.length)
				return listCopy[pos++];
			else
				return 0L;
		}
		
		@Override
		public void copyOnWrite() {
			// do nothing, since class iterator already works on a copied array
		}

		@Override
		public long getAt(int i) {
			pos = i;
			if (hasNext())
				return next();
			else
				return 0;
		}
	}
	
	private class ArrayListIterator implements MyIterator {
		ArrayList<Long> arr;
		int pos;
		long it;

		public ArrayListIterator(ArrayList<Long> _arr) {
			arr = _arr;
			pos = 0;
			it = ++lastIt;
			activeIteratorsMap.put(it, this);
		}		
		
 		@Override
		public boolean hasNext() {
 			return (pos<arr.size());
		}

		@Override
		public Long next() {
			if (pos<arr.size())
				return arr.get(pos++);
			else
				return null;
		}

		@Override
		public int getSize() {
			return arr.size();
		}

		@Override
		public void free() {
			activeIteratorsMap.remove(it);
		}

		@Override
		public void copyOnWrite() {
		}
		
		@Override
		public long getAt(int i) {
			if (i<0) {
				pos = arr.size()+i;
				if (pos<0)
					pos = 0;
			}
			pos = i;
			if (hasNext())
				return next();
			else
				return 0;
		}
	}
	
	private void collectSuperClasses(long rClass, Set<Long> s) {
		if (s.contains(rClass))
			return;
		s.add(rClass);
		
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rClass);
		if (rait == null)
			return;
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x11) {
				if (mem.actions_get(i+1)==rClass)
					collectSuperClasses((long)mem.actions_get(i+2), s);
			}
		}
	}
	
	private Set<Long> collectSuperClasses(long rClass) {
		Set<Long> s = new HashSet<Long>();
		collectSuperClasses(rClass, s);
		return s;
	}
	
	private void collectSubClasses(long rClass, Set<Long> s) {
		if (s.contains(rClass))
			return;
		s.add(rClass);
		
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rClass);
		if (rait == null)
			return;
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x11) {
				if (mem.actions_get(i+2)==rClass)
					collectSubClasses((long)mem.actions_get(i+1), s);
			}
		}
	}
	
	private Set<Long> collectSubClasses(long rClass) {
		Set<Long> s = new HashSet<Long>();
		collectSubClasses(rClass, s);
		return s;
	}
	
	private void collectDirectClassObjects(long rClass, List<Long> result) {
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rClass);
		if (rait == null)
			return;
		
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x02) {
				if (mem.actions_get(i+1)==rClass) {
					int j = mem.hasMultiTypedObjects()?findActionWhere(0xE2, mem.actions_get(i+2), rClass):-1;
					if (j<0) { // not excluded 
						result.add((long)mem.actions_get(i+2));
					}
				}
			}
			if (mem.actions_get(i)==0x12) {
				if (mem.actions_get(i+2)==rClass) {
					result.add((long)mem.actions_get(i+1));
				}
			}
		}
	}

	private void collectDirectObjectClasses(long rObject, Collection<Long> result) {
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rObject);
		if (rait == null)
			return;
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x02) {
				if (mem.actions_get(i+2)==rObject) {
					int j = mem.hasMultiTypedObjects()?findActionWhere(0xE2, rObject, (long)mem.actions_get(i+1)):-1;
					if (j<0) { // this is the first class, and the object has not been excluded from this class 
						double el = mem.actions_get(i+1);
						result.add((long)el);
					}
				}
			}
			if (mem.actions_get(i)==0x12) {
				if (mem.actions_get(i+1)==rObject) {
					result.add((long)mem.actions_get(i+2));
				}
			}
		}
	}

	private void collectDirectAttributes(long rClass, Collection<Long> result) {
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rClass);
		if (rait == null)
			return;
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x03) {
				if (mem.actions_get(i+1)==rClass)
					result.add((long)mem.actions_get(i+3));
			}
		}
	}

	private void collectDirectAssociationEnds(long rClass, Collection<Long> result) {
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rClass);
		if (rait == null)
			return;
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x05) {
				if (mem.actions_get(i+1)==rClass)
					result.add((long)mem.actions_get(i+4));
				else 
				if (mem.actions_get(i+2)==rClass)
					result.add((long)mem.actions_get(i+5));				
				// "else" ensures that we will return just one association end for a loop
			}
			if (mem.actions_get(i)==0x15) {
				if (mem.actions_get(i+1)==rClass)
					result.add((long)mem.actions_get(i+4));
			}
		}
	}

	private void collectDirectIngoingAssociationEnds(long rClass, Collection<Long> result) {
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rClass);
		if (rait == null)
			return;
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x05) {
				if (mem.actions_get(i+1)==rClass)
					result.add((long)mem.actions_get(i+5));
				else 
				if (mem.actions_get(i+2)==rClass)
					result.add((long)mem.actions_get(i+4));				
				// "else" ensures that we will return just one association end for a loop
			}
			if (mem.actions_get(i)==0x15) {
				if (mem.actions_get(i+2)==rClass)
					result.add((long)mem.actions_get(i+4));
			}
		}
	}
	private void collectDirectAndInverseAssociationEnds(long rClass, Collection<Long> result) {
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rClass);
		if (rait == null)
			return;
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x05) {
				if ((mem.actions_get(i+1)==rClass) || (mem.actions_get(i+2)==rClass)) {
					result.add((long)mem.actions_get(i+4));
					result.add((long)mem.actions_get(i+5));
				}
			}
			if (mem.actions_get(i)==0x15) {
				if (mem.actions_get(i+1)==rClass)
					result.add((long)mem.actions_get(i+4));
			}
		}
	}
	
	private void collectDirectSuperClasses(double rClass, Collection<Long> result) {
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rClass);
		if (rait == null)
			return;
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x11) {
				if (mem.actions_get(i+1)==rClass) {
					result.add((long)mem.actions_get(i+2));
				}
			}
		}
	}

	private void collectDirectSubClasses(double rClass, Collection<Long> result) {
		IARMemory.ActionsIterator rait = mem.r2a_get_iterator((double)rClass);		
		if (rait == null)
			return;
		int i;
		while ((i=rait.next())!=-1) {
			if (mem.actions_get(i)==0x11) {
				if (mem.actions_get(i+2)==rClass) {
					result.add((long)mem.actions_get(i+1));
				}
			}
		}
	}
	
	private void collectLinkedObjects(long rObject, long rAssocEnd, List<Long> result) {
		long rInv = this.getInverseAssociationEnd(rAssocEnd);
		
		if (DEBUG_LINKS) logger.debug("collectLinkedObjects "+rObject+" "+rAssocEnd+" ("+getRoleName(rAssocEnd)+")");
		if (rInv == 0) {
			if (DEBUG_LINKS) logger.debug("unidirectional association");
			// links in one direction only
			
			
			IARMemory.ActionsIterator it1 = mem.r2a_get_iterator((double)rObject);
			IARMemory.ActionsIterator it2 = mem.r2a_get_iterator((double)rAssocEnd);
			if ((it1 == null) || (it2 == null))
				return;
			
			int val1 = it1.next();
			int val2 = it2.next();
			
			while ((val1!=-1) && (val2!=-1)) {
				if (val1 < val2) 
					val1 = it1.nextGreaterOrEqual(val2);
				else
				if (val1 > val2)
					val2 = it2.nextGreaterOrEqual(val1);
				else {
					int i = val1;
					if (mem.actions_get(i)==0x06) {
						if (mem.actions_get(i+3)==rAssocEnd) {
							if (mem.actions_get(i+1)==rObject) 
								result.add((long)mem.actions_get(i+2));
						}
					}
					val1 = it1.next();
					val2 = it2.next();
				}
			}
		}
		else {
			if (DEBUG_LINKS) logger.debug("bidirectional association");
			// bi-directional links possible

			IARMemory.ActionsIterator it1 = mem.r2a_get_iterator((double)rObject);
			IARMemory.ActionsIterator it2 = mem.r2a_get_iterator((double)rAssocEnd);
			IARMemory.ActionsIterator it3 = mem.r2a_get_iterator((double)rInv);
			if ((it1 == null) || (it2 == null) || (it3 == null))
				return;

			int val1 = it1.next();
			int val2 = it2.next();
			int val3 = it3.next();
			
/* SLOW LINKS, SIMPLE AND CORRECT SOLUTION*/
/*			int i = it1.next();
			while (i!=-1) {
				if (mem.actions_get(i)==0x06) {
					if (mem.actions_get(i+3)==rAssocEnd) {
						if (mem.actions_get(i+1)==rObject) 
							result.add((long)mem.actions_get(i+2));
							
					}
					if (mem.actions_get(i+3)==rInv) {
						if (mem.actions_get(i+2)==rObject) 
							result.add((long)mem.actions_get(i+1));							
					}
				}
				i=it1.next();
			}*/
			
//*/			

			
// fast links (should work, but may need more testing)
//			int i1=0, i2=0, i3=0;
			while ( (val1!=-1) && ((val2!=-1)||(val3!=-1)) ) {
				if (val3==-1) {
					if (DEBUG_LINKS) logger.debug("list3 ended");
					// list3 ended
					if (val1 < val2) 
						val1 = it1.nextGreaterOrEqual(val2);
					else
					if (val1 > val2)
						val2 = it2.nextGreaterOrEqual(val1);
					else {
						int i = val1;
						if (mem.actions_get(i)==0x06) {
							if (mem.actions_get(i+3)==rAssocEnd) {
								if (mem.actions_get(i+1)==rObject) 
									result.add((long)mem.actions_get(i+2));
									
							}
							if (mem.actions_get(i+3)==rInv) {
								if (mem.actions_get(i+2)==rObject) 
									result.add((long)mem.actions_get(i+1));
									
							}
						}
						val1 = it1.next();
						val2 = it2.next();
					}
				}
				else
				if (val2==-1) {
					if (DEBUG_LINKS) logger.debug("list2 ended");
					// list2 ended
					if (val1 < val3) 
						val1 = it1.nextGreaterOrEqual(val3);
					else
					if (val1 > val3)
						val3 = it3.nextGreaterOrEqual(val1);
					else {
						int i = val1;
						if (mem.actions_get(i)==0x06) {
							if (mem.actions_get(i+3)==rAssocEnd) {
								if (mem.actions_get(i+1)==rObject) 
									result.add((long)mem.actions_get(i+2));									
							}
							if (mem.actions_get(i+3)==rInv) {
								if (mem.actions_get(i+2)==rObject) 
									result.add((long)mem.actions_get(i+1));
							}
						}
						val1 = it1.next();
						val3 = it3.next();
					}
				}
				else {
					if (DEBUG_LINKS) logger.debug("list2 and list3 available");
					// both lists available
					if (val2 <= val3) {
						// move i1 or i2
						if (val1 < val2) 
							val1 = it1.nextGreaterOrEqual(val2);
						else
						if (val1 > val2)
							val2 = it2.nextGreaterOrEqual(val1);
						else {
							int i = val1;
							if (mem.actions_get(i)==0x06) {
								if (mem.actions_get(i+3)==rAssocEnd) {
									if (mem.actions_get(i+1)==rObject) 
										result.add((long)mem.actions_get(i+2));
								}
								if (mem.actions_get(i+3)==rInv) {
									if (mem.actions_get(i+2)==rObject) 
										result.add((long)mem.actions_get(i+1));
								}
							}
							val1 = it1.next();
							val2 = it2.next();
						}
					}
					else { // (val2 > val3)
						// move i1 or i3
						if (val1 < val3) 
							val1 = it1.nextGreaterOrEqual(val3);
						else
						if (val1 > val3)
							val3 = it3.nextGreaterOrEqual(val1);
						else {
							int i = val1;
							if (mem.actions_get(i)==0x06) {
								if (mem.actions_get(i+3)==rAssocEnd) {
									if (mem.actions_get(i+1)==rObject) 
										result.add((long)mem.actions_get(i+2));
								}
								if (mem.actions_get(i+3)==rInv) {
									if (mem.actions_get(i+2)==rObject) 
										result.add((long)mem.actions_get(i+1));
								}
							}
							val1 = it1.next();
							val3 = it3.next();
						}							
					}
				} // list2 and list3 available
				
			}  // fast links
		
		}
	}
	
	@Override
	synchronized public long getIteratorForClasses() {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			return new ClassIterator().it;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getIteratorForAllClassObjects(long _rClass) {
		if (mem==null)
			return 0;
		mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			for (Long rClass : collectSubClasses(_rClass)) {
				collectDirectClassObjects(rClass, arr);
			}
			return new ArrayListIterator(arr).it;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getIteratorForDirectClassObjects(long rClass) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectDirectClassObjects(rClass, arr);
			return new ArrayListIterator(arr).it;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long getIteratorForDirectObjectClasses(long rObject) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectDirectObjectClasses(rObject, arr);
			long retVal = new ArrayListIterator(arr).it;
			return retVal;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getIteratorForAllAttributes(long _rClass) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			for (Long rClass : collectSuperClasses(_rClass)) {
				collectDirectAttributes(rClass, arr);
			}
			return new ArrayListIterator(arr).it;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getIteratorForDirectAttributes(long rClass) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectDirectAttributes(rClass, arr);
			return new ArrayListIterator(arr).it;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getIteratorForAllOutgoingAssociationEnds(long _rClass) {		
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			for (Long rClass : collectSuperClasses(_rClass)) {
				collectDirectAssociationEnds(rClass, arr);
			}
			return new ArrayListIterator(arr).it;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long  getIteratorForDirectOutgoingAssociationEnds(long rClass) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectDirectAssociationEnds(rClass, arr);
			return new ArrayListIterator(arr).it;
		}
		finally {
			mem.unlock();
		}
	}

	
	@Override
	synchronized public long getIteratorForDirectIngoingAssociationEnds(long _rClass) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			for (Long rClass : collectSuperClasses(_rClass)) {
				collectDirectIngoingAssociationEnds(rClass, arr);
			}
			return new ArrayListIterator(arr).it;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long getIteratorForAllIngoingAssociationEnds(long rClass) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectDirectIngoingAssociationEnds(rClass, arr);
			return new ArrayListIterator(arr).it;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getIteratorForLinkedObjects(long rObject, long rAssociation) {		
		if (mem==null)
			return 0;
		 mem.lock();
			//mem.rearrange();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectLinkedObjects(rObject, rAssociation, arr);
			ArrayListIterator alit = new ArrayListIterator(arr); 
			long retVal = alit.it;
			return retVal;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getIteratorForDirectSuperClasses(long rSubClass) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectDirectSuperClasses(rSubClass, arr);
			long retVal = new ArrayListIterator(arr).it;
			return retVal;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getIteratorForDirectSubClasses(long rSuperClass) {
		if (mem==null)
			return 0;
		mem.lock();
		try {
			ArrayList<Long> arr = new ArrayList<Long>();
			collectDirectSubClasses(rSuperClass, arr);
			return new ArrayListIterator(arr).it;
		}
		finally {
			mem.unlock();
		}
	}

	@Override
	synchronized public long getIteratorForObjectsByAttributeValue(long rAttribute, String value) {
		if (mem==null)
			return 0;
		 mem.lock();
		try {
			ActionsIterator it = mem.s2a_get(value, false);
			
			ArrayList<Long> arr2 = new ArrayList<Long>();
			
			int i;
			if (it!=null) while ((i=it.next())!=-1) // TODO: check for rAttribute
				if (mem.actions_get(i)==0x04)
					arr2.add((long)mem.actions_get(i+1));
			
			return new ArrayListIterator(arr2).it;
		}
		finally {
			mem.unlock();
		}
	}
	
	@Override
	synchronized public long resolveIteratorFirst(long it) {
		MyIterator mit = activeIteratorsMap.get(it);
		return mit==null?0:mit.getAt(0);
	}

	@Override
	synchronized public long resolveIteratorNext(long it) {
		MyIterator mit = activeIteratorsMap.get(it);
		if (mit == null)
			return 0;
		if (!mit.hasNext())
			return 0;
		return mit.next();
	}

	@Override
	synchronized public long resolveIterator(long it, int pos) {
		MyIterator mit = activeIteratorsMap.get(it);
		return mit==null?0:mit.getAt(pos);
	}

	@Override
	synchronized public int getIteratorLength(long it) {
		MyIterator mit = activeIteratorsMap.get(it);		
		return mit==null?0:mit.getSize();
	}
	
	@Override
	synchronized public String serializeReference(long r)
	{
		return Long.valueOf(r).toString();
	}
	
	@Override
	synchronized public long deserializeReference(String s)
	{
		try {
			return Long.parseLong(s);
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	synchronized public void freeReference(long r) {
	}

	@Override
	synchronized public void freeIterator(long it) {
		MyIterator mit = activeIteratorsMap.get(it);
		if (mit!=null)
			mit.free();
	}

	@Override
	synchronized public boolean open(String uri)
	{
		if (uri == null) 
			return false; 
		
		//uri = "shmserver:"+uri;
		if (openRepositoryPath != null)
			return false; // already open
		
		try {
			openRepositoryPath = this.readIntoMemory(uri);
		}
		catch(Throwable t) {
			free(true);
			return false;
		}
			
		
		return openRepositoryPath!=null;						
	}

	@Override
	synchronized public void close()
	{
		if (openRepositoryPath == null)
			return; // not open

		free(false);
		openRepositoryPath = null;
	}
	
	
	@Override
	synchronized public boolean exists (String path)
	{
		String path1 = path+File.separator+"ar.actions";
		String path2 = path+File.separator+"ar.strings";
		return (new File(path1).exists() && new File(path2).exists());
	}		

	@Override
	synchronized public boolean startSave ()
	{
		if (openRepositoryPath == null)
			return false;
		
		if (mem==null)
			return false;
		
		File dir = new File(openRepositoryPath);
		if (!dir.exists())
			dir.mkdirs();
		File prev1 = new File(openRepositoryPath+File.separator+"ar.actions.prev");
		File prev2 = new File(openRepositoryPath+File.separator+"ar.strings.prev");
		File cur1 = new File(openRepositoryPath+File.separator+"ar.actions");
		File cur2 = new File(openRepositoryPath+File.separator+"ar.strings");
		

		if (prev1.exists())
			prev1.delete();
		if (prev2.exists())
			prev2.delete();
		
		long lastModified1 = cur1.lastModified();
		long lastModified2 = cur2.lastModified();
		cur1.renameTo(prev1);
		cur2.renameTo(prev2);
		prev1.setLastModified(lastModified1);
		prev2.setLastModified(lastModified2);

		
		DataOutputStream fw1 = null;
		BufferedWriter fw2 = null;
		mem.lock();
		try {
		    try {
		    			    
		      fw1 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(cur1)));
		      mem.storeActions(fw1);
		      
		      fw2 = new BufferedWriter
		    		    (new OutputStreamWriter(new FileOutputStream(cur2), StandardCharsets.UTF_8));
		      mem.storeStrings(fw2);
		      
		      fw1.close(); fw1=null;
			  fw2.close(); fw2=null;
			  return true;
		    } catch (Throwable t) {
		    	return false;
		    }
		}
		finally {
			mem.unlock();
			if (fw1!=null)
				try {
					fw1.close();
				} catch (IOException e) {
				}
			if (fw2!=null)
				try {
					fw2.close();
				} catch (IOException e) {
				}
		}
	}

	@Override
	synchronized public boolean finishSave ()
	{
		if (openRepositoryPath == null)
			return false;
		
		File prev1 = new File(openRepositoryPath+File.separator+"ar.actions.prev");
		File prev2 = new File(openRepositoryPath+File.separator+"ar.strings.prev");
		
		boolean ok = true; 
		if (prev1.exists()) {
			if (!prev1.delete())
				ok = false;
		}
		if (prev2.exists()) {
			if (!prev2.delete())
				ok = false;
		}

		return ok;
	}

	@Override
	synchronized public boolean cancelSave ()
	{
		if (openRepositoryPath==null)
			return false;

		File prev1 = new File(openRepositoryPath+File.separator+"ar.actions.prev");
		File prev2 = new File(openRepositoryPath+File.separator+"ar.strings.prev");
		File cur1 = new File(openRepositoryPath+File.separator+"ar.actions");
		File cur2 = new File(openRepositoryPath+File.separator+"ar.strings");

		boolean ok = true;
		
		if (prev1.exists() && prev2.exists()) {
			long lastModified1 = prev1.lastModified();
			long lastModified2 = prev2.lastModified();
			if (cur1.exists())
				cur1.delete();
			if (cur2.exists())
				cur2.delete();
			if (!prev1.renameTo(cur1))
				ok = false;
			if (!prev2.renameTo(cur2))
				ok = false;
			cur1.setLastModified(lastModified1);
			cur2.setLastModified(lastModified2);
		}
		else
			ok = false;

		return ok;
	}
	
	@Override
	synchronized public boolean drop(String location) {
		if (location == null)
			return false;
		
		File f1 = new File(location+File.separator+"ar.actions");
		File f2 = new File(location+File.separator+"ar.strings");
		
		boolean ok = true; 
		if (f1.exists()) {
			if (!f1.delete())
				ok = false;
		}
		if (f2.exists()) {
			if (!f2.delete())
				ok = false;
		}

		return ok;
	}

	@Override // RAAPI_WR
	synchronized public long getMaxReference() {
		return mem.getMaxReference();
	}

	@Override
	synchronized public String callSpecificOperation(String arg0, String arg1) {
		if ("shmsync".equalsIgnoreCase(arg0)) {
			logger.debug("AR SPECIFIC SHMSYNC");
			if ((mem!=null) && mem.tryLock())
				 mem.unlock();
		}		
		return null;
	}

	@Override
	synchronized public void syncAll(RAAPI_Synchronizer synchronizer, long bitsValues) {
		mem.syncAllEx(synchronizer, mem.getPredefinedBitsCount(), bitsValues);
	}

	@Override
	synchronized public boolean setPredefinedBits(int bitsCount, long bitsValues) {
		if (bitsCount>50)
			return false;
		
		return mem.setPredefinedBits(bitsCount, bitsValues);
	}

	@Override
	synchronized public int getPredefinedBitsCount() {
		return mem.getPredefinedBitsCount();
	}

	@Override
	synchronized public long getPredefinedBitsValues() {
		return mem.getPredefinedBitsValues();
	}


}

