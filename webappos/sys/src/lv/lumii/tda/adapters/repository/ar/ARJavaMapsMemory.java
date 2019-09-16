package lv.lumii.tda.adapters.repository.ar;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import lv.lumii.tda.raapi.RAAPI_Synchronizer;


public class ARJavaMapsMemory implements IARMemory {
	
	private static boolean DEBUG = false;

	// TODO: allow configure (max ops cnt, max mem size..)

	
	private static final int INITIAL_N = 10000;
	private static final int INITIAL_S = 100;
	
	private int curmaxN, curmaxS;
	private int deletedN, deletedS; 
	/*package*/ double[] actions;
	/*package*/ String[] strings;
	
	private Set<Long> allClasses = new HashSet<Long>();

	private Map<String, ArrayList<Integer> > whereStringMap = new HashMap<String, ArrayList<Integer> >(); // string -> array of action indices
	private Map<Double, ArrayList<Integer> > whereReferenceMap = new HashMap<Double, ArrayList<Integer> >(); // reference -> array of action indices
	private Map<Integer, Integer> a2sMap = new TreeMap<Integer, Integer>(); // action index -> string index (for string actions)
	
	// TODO:
	// s2a  string index -> action index
	// r2a  reference -> action index
	// a2s  action index-> string index
		
	public ARJavaMapsMemory() {		
	}
	
	@Override
	synchronized public boolean reallocateFromCache() {
		return false;
	}
	
	synchronized public boolean reallocate(int nActions, int nStrings) {
		
		int nSize = INITIAL_N;
		while (nActions > nSize) {
			nSize*=2;
		}
		
		int sSize = INITIAL_S;
		while (nStrings > sSize) {
			sSize*=2;
		}
		
		actions = new double[nSize];
		actions[0] = 0xEE; // bulk actions
		strings = new String[sSize];
		curmaxN = 1; 
		curmaxS = 0;
		deletedN = 0;
		deletedS = 0;
		
		multiClasses = false;
		
		allClasses.clear();
		
		whereStringMap.clear();
		whereReferenceMap.clear();
		a2sMap.clear();
		
		return true;
	}
	
	synchronized public void free(boolean clearCache) {
		reallocate(0, 0);
	}
	
//	private long lastId=0;
	
	synchronized public ActionsIterator s2a_get(String s, boolean isRoleName) {
		/*try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}*/		
		
		ArrayList<Integer> list = whereStringMap.get(s);
		
	/*	if (lastId!=Thread.currentThread().getId()) {
			System.out.println("THREADGET "+lastId+"->"+Thread.currentThread().getId()+" "+(list==null?"null":list.size()));
			lastId = Thread.currentThread().getId();
		}*/
		if (list==null)
			return null;
		
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		for (Integer ii : list) {
			if (actions[ii]!=0x00) {// not deleted
				
				Integer si = a2sMap.get(ii);
				if (isRoleName) {
					if ((si!=null) && (si>=0) && (si<curmaxS) && (strings[si]!=null))
						if (strings[si].equals(s) || strings[si].startsWith(s+"/") || strings[si].endsWith("/"+s))
							list2.add(ii);
				}
				else {
					if ((si!=null) && (si>=0) && (si<curmaxS) && (strings[si]!=null) && (strings[si].equals(s)))
						list2.add(ii);
				}
			}
		}
		return new ActionsIteratorOverArrayList(list2);		
	}
	
	synchronized public int a2s_get(int index) {
		Integer si = a2sMap.get(index);
		if ((si==null) || (si<0) || (si>=curmaxS))
			return -1;
		else
			return si;
	}
	
	
	synchronized public ActionsIterator r2a_get_iterator(double r) {
		ArrayList<Integer> list = whereReferenceMap.get((double)r);
		if (list==null)
			return null;
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		for (Integer i : list) {
			if (actions[i]!=0x00) {// not deleted
				list2.add(i);
			}
		}
		return new ActionsIteratorOverArrayList(list2);		
	}
	
	synchronized public ArrayList<Integer> r2a_get(double r) {
		return whereReferenceMap.get((double)r);
	}
	
	synchronized public int r2a_get_first(double r) {
		ArrayList<Integer> a = whereReferenceMap.get((double)r);
		if ((a==null) || a.isEmpty())
			return -1;
		for (Integer i : a)
			if (actions[i]!=0x00)
				return i;
		
		return -1;
		//return a.get(0);
	}
	
	synchronized public ArrayList<Integer> r2a_get_and_remove(double r) {
		//ArrayList<Integer> list = whereReferenceMap.remove((double)r);
		//return list;
		return null;
/*		ArrayList<Integer> list2 = new ArrayList<Integer>();
		for (Integer i : list) {
			if (actions[i]!=0x00) {// not deleted
				list2.add(i);
			}
		}
		return list2;*/
	}
	
	private void putWhereString(String s, int i) {		
		ArrayList<Integer> arr = whereStringMap.get(s);
		if (arr == null) {
			arr = new ArrayList<Integer>();
			whereStringMap.put(s, arr);
		}			
		arr.add(i);
	}

	
	private void addWhereString(String s, int i, boolean isRoleString) { // TODO: index instead of string
		if (s==null)
			return;
		
		putWhereString(s, i);
		
		
		if (isRoleString) {
			int k = s.indexOf('/'); // for association role name/inverse role name
			if (k>=0) {
				String sub1 = s.substring(0, k);
				String sub2 = s.substring(k+1);
				
				if (!sub1.isEmpty()) {
					putWhereString(sub1, i);
				}
				if (!sub2.isEmpty()) {
					putWhereString(sub2, i);
				}
			}
		}
	}

	synchronized public void r2a_add(double d, int i) {		
		ArrayList<Integer> arr = whereReferenceMap.get(d);
		if (arr == null) {
			arr = new ArrayList<Integer>();
			whereReferenceMap.put(d, arr);
		}			
		arr.add(i);
	}
	
	
	
	
	/**
	 * Re-arranges the actions array (increases/decreases array size, if needed).
	 * @param additionalN How many elements in the actions array are to be added. The number of elements depends on the length+1 of actions.
	 * If <=0, then the array is re-arranged for save purposes (e.g., elements of deleted actions are excluded).
	 */
	private void rearrangeActions(int additionalN) {
		double[] old = actions;
		
		if (additionalN > 0) {
			// check memory
			
			if (curmaxN+additionalN < actions.length)
				return; // room ok			
			
			if ((deletedN>actions.length/4) && (curmaxN-deletedN+additionalN < actions.length)) {			
				if (curmaxN-deletedN+additionalN <= actions.length/4) {
					// decrease memory before rearrange...
					actions = new double[actions.length/2];
				}
			}
			else {
				// increase memory before rearrange
				actions = new double[actions.length*2];
			}
		}
		
		// rearrange
		
		int i=0; int j=0;
		whereStringMap.clear();
		whereReferenceMap.clear();
		
		while (j<curmaxN) {
			if (old[j] == 0x00) { // deleted action
				j++; // skip this action id
				if (j<curmaxN) {
					j += old[j]; // increasing j by action size stored there...
				}
				continue;
			}
			
			// non-deleted action
			int actionsDelta = RepositoryAdapter.getOpSize(old[j]);
			
			if (RepositoryAdapter.isStringOp(old[j])) {
				if (DEBUG) {
					if (a2sMap.get(j)==null) {
						System.err.println("!!!! wrong map value for "+j+"->"+i+"; action = "+old[j]+" "+curmaxN+" arr:"+old.length+"->"+actions.length);
						int opsize = RepositoryAdapter.getOpSize(old[j]);
						for (int k=1; k<=opsize; k++)
							System.err.print(old[j+k]+" ");
						System.err.println();
						
						//System.err.println("className "+this.getClassName((long)old[j+1]));
					}
				}
				int si = a2sMap.remove(j);
				assert !a2sMap.containsKey(j);
				assert !a2sMap.keySet().contains(j);

				addWhereString(strings[si], i, old[j]==0x05 || old[j]==0x15 || old[j]==0x25);
				a2sMap.put(i, si);
				
//				if (old[j]==0x01)
//					System.err.println("rearranging action 0x01  "+j+"->"+i+" "+strings[si]+" "+whereStringMap.get(strings[si])+" "+a2sMap.get(i)+"=?="+si);
				
			}
			
			actions[i] = old[j]; // move action id...
			
			int ii = i;
			
			while (actionsDelta>0) { // move action
				i++; j++;
				actions[i] = old[j];
				actionsDelta--;
				if (actions[i]>RepositoryAdapter.MAX_PRIMITIVE_REFERENCE) { // this is not some primitive type reference; neither boolean 0 or 1 value
					r2a_add(actions[i], ii);
				}
			}
			
			i++;
			j++;
		}
		curmaxN = i;
		deletedN = 0;		
	}
	
	
	/**
	 * Re-arranges the strings array (increases/decreases array size, if needed).
	 * @param additionalS How many strings are to be added to the strings array. If <=0, then the array is re-arranged for save purposes (e.g., deleted strings are excluded).
	 */
	private void rearrangeStrings(int additionalS) {
		String[] old = strings;
		
		if (additionalS > 0) {		
			// check memory...
			if (curmaxS+additionalS < strings.length)
				return; // room ok
			
			if (curmaxS-deletedS+additionalS < strings.length) {
				if (deletedS>strings.length/4) {			
					if (curmaxS-deletedS+additionalS <= strings.length/4) {
						// decrease memory before rearrange...
						strings = new String[strings.length/2];
					}
				}
			}
			else {
				// increase memory before rearrange
				strings = new String[strings.length*2];
			}
			/*
			if ((deletedS>strings.length/4) && (curmaxS-deletedS+additionalS < strings.length)) {			
				if (curmaxS-deletedS+additionalS <= strings.length/4) {
					// decrease memory before rearrange...
					strings = new String[strings.length/2];
				}
			}
			else {
				// increase memory before rearrange
				strings = new String[strings.length*2];
			}*/
			
			
		}
		
		
		
		Map<Integer, Integer> inv = new HashMap<Integer, Integer>();
		for (Integer k : a2sMap.keySet())
			inv.put(a2sMap.get(k), k);		
				
		// rearrange
		int i=0; int j=0;
		//System.out.println("rearrange strings");
		while (j<curmaxS) {
			if (old[j] == null) { // deleted string
				j++; // skip this string
				continue;
			}
			
			// non-deleted string
			strings[i] = old[j]; // move string...
			//if (i!=j)
				//System.out.print(" "+i+"<-"+j+"["+strings[i]+"]");

			// remove ai->j, put ai->i
			if (i!=j)
				a2sMap.put(inv.get(j), i);

			
			i++;
			j++;
		}
		//System.out.println();
		curmaxS = i;		
		deletedS = 0;		
	}
	
	/*	private int firstIndexWhere(double r) {
	ArrayList<Integer> arr = whereReferenceMap.get(r);
	if ((arr == null) || (arr.isEmpty()))
		return -1;
	return arr.get(0); // the 0th must be createClass		
}

private double firstActionWhere(double r) {
	ArrayList<Integer> arr = whereReferenceMap.get(r);
	if ((arr == null) || (arr.isEmpty()))
		return 0;
	int i = arr.get(0);
	return actions[i];
}*/
	

	
	synchronized public int addAction(double... arr) {
		rearrangeActions(arr.length);
		int retVal = curmaxN;		
		actions[retVal] = arr[0];
		if ((arr[0]==0x12) || (arr[0]==0xE2))
			multiClasses = true;
		for (int i=1; i<arr.length; i++) {
			actions[retVal+i] = arr[i];
			if (arr[i]>RepositoryAdapter.MAX_PRIMITIVE_REFERENCE) {
				r2a_add(arr[i], retVal);
			}
		}
		curmaxN+=arr.length;
		return retVal;
	}
	
	synchronized public int addStringAction(String s, double... arr) {
		if (s==null)
			s="";
		rearrangeActions(arr.length);
		int retVal = curmaxN;
		actions[retVal] = arr[0];
		for (int i=1; i<arr.length; i++) {
			actions[retVal+i] = arr[i];
			if (arr[i]>RepositoryAdapter.MAX_PRIMITIVE_REFERENCE)
				r2a_add(arr[i], retVal);
		}
		curmaxN+=arr.length;
		
		rearrangeStrings(1);
		a2sMap.put(retVal, curmaxS);
		addWhereString(s, retVal, arr[0]==0x05 || arr[0]==0x15 || arr[0]==0x25);
		strings[curmaxS++] = s;

		
		if (arr[0]==0x01) { // createClass
			allClasses.add((long)arr[1]);			
		}
		if (arr[0]==0x25) { // createAdvancedAssociation
			boolean associationClass = arr[2]==1.0;
			if (associationClass)
				allClasses.add((long)arr[3]);			
		}
		
		return retVal;
	}

	synchronized public int addRoleStringAction(String s, double... arr) {
		if (s==null)
			s="";
		rearrangeActions(arr.length);
		int retVal = curmaxN;
		actions[retVal] = arr[0];
		for (int i=1; i<arr.length; i++) {
			actions[retVal+i] = arr[i];
			if (arr[i]>RepositoryAdapter.MAX_PRIMITIVE_REFERENCE)
				r2a_add(arr[i], retVal);
		}
		curmaxN+=arr.length;
		
		rearrangeStrings(1);
		a2sMap.put(retVal, curmaxS);
		addWhereString(s, retVal, arr[0]==0x05 || arr[0]==0x15 || arr[0]==0x25);
		strings[curmaxS++] = s;
		
		if (arr[0]==0x01) { // createClass
			allClasses.add((long)arr[1]);			
		}
		if (arr[0]==0x25) { // createAdvancedAssociation
			boolean associationClass = arr[2]==1.0;
			if (associationClass)
				allClasses.add((long)arr[3]);			
		}
		
		return retVal;
	}

	synchronized public void deleteSimpleAction(int index) {
	
		if (actions[index]==0x00)
			return; // already deleted
		
		if (actions[index]==0x01) { // addClass
			allClasses.remove((long)actions[index+1]);			
		}
		if (actions[index]==0x25) { // createAdvancedAssociation
			boolean associationClass = actions[index+2]==1.0;
			if (associationClass)
				allClasses.remove((long)actions[index+3]);			
		}
		
		int size = RepositoryAdapter.getOpSize(actions[index]);
		actions[index] = 0;
		actions[index+1] = size;		
		// other actions[index+i] may remain the same
		deletedN++;
		
		Integer si = a2sMap.remove(index);
		assert !a2sMap.containsKey(index);
		assert !a2sMap.keySet().contains(index);
		if ((si != null) && (si>=0) && (si<curmaxS) && (strings[si]!=null)) {
			// whereReferenceMap, whereStringMap - do not modify, it will be modified during re-arranging actions
			strings[si] = null;
			deletedS++;
		}
	}
	
	synchronized public long[] getAllClasses() {
		long[] listCopy = new long[allClasses.size()];
		int i = 0;
		for (Long l : allClasses) {
			listCopy[i++] = l;
		}
		return listCopy;
	}

	synchronized public void storeActions(DataOutputStream fw1) throws IOException {
	      rearrangeActions(0);
	      for (int i=0; i<curmaxN; i++)
	    	  fw1.writeDouble(actions[i]);	      
	}
	
	synchronized public void storeStrings(BufferedWriter fw2) throws IOException {
		rearrangeStrings(0);
	      boolean first = true;
	      for (int i=0; i<curmaxS; i++) {
	    	  String s = strings[i];
	    	  if (first)
	    		  first = false;
	    	  else
	    		  fw2.write('`');
	    	  fw2.write(RAAPI_Synchronizer.sharpenString(s));	    	  
	      }		
	}

	@Override
	synchronized public void syncAllEx(RAAPI_Synchronizer synchronizer, int bitsCount, long bitsValues) {
		if (synchronizer == null)
			return;
	    rearrangeActions(0);
	    rearrangeStrings(0);
	    
	    synchronizer.syncBulk(curmaxN, actions, curmaxS, strings);
	    synchronizer.syncMaxReference(this.getMaxReference(), bitsCount, bitsValues);
	}

	@Override
	synchronized public double actions_get(int i) {
		return actions[i];
	}

	@Override
	synchronized public String strings_get(int i) {
		return strings[i];
	}
	
	@Override
	synchronized public boolean tryLock() {
		return true;
	}
	
	@Override
	synchronized public void lock() {
		// do nothing
	}

	@Override
	synchronized public void unlock() {
		// do nothing
	}

	private boolean multiClasses = false;
	@Override
	synchronized public boolean hasMultiTypedObjects() {
		return multiClasses;
	}
	
	private long maxReference = -1;

	@Override
	synchronized public long getMaxReference() {
		if (maxReference < 0) {
			long rMax = RepositoryAdapter.MAX_PRIMITIVE_REFERENCE;
	
			for (int i = 0; i < curmaxN; i++) {
				double d = actions[i];
				if (d > rMax)
					rMax = (long)d;
			}
			maxReference = rMax;
		}
		return maxReference;
	}
	
	@Override
	synchronized public boolean setMaxReference(long ref) {
		maxReference = ref;
		return true;
	}
	
	private int predefinedBitsCount = 0;
	private long predefinedBitsValues = 0;

	@Override
	synchronized public boolean setPredefinedBits(int bitsCount, long bitsValues) {
		predefinedBitsCount = bitsCount;
		predefinedBitsValues = (bitsValues & ((1<<bitsCount)-1)); // keeping only lowest bitsCount bits
		return true;
	}

	@Override
	synchronized public int getPredefinedBitsCount() {
		return predefinedBitsCount;
	}

	@Override
	synchronized public long getPredefinedBitsValues() {
		return predefinedBitsValues;
	}

	@Override
	synchronized public long newReference() {
		maxReference = (((maxReference>>predefinedBitsCount)+1) << predefinedBitsCount) | predefinedBitsValues;
		return maxReference;
	}

	@Override
	synchronized public double getMemoryUsageFactor() {
		return 0.0;
	}

	@Override
	synchronized public boolean memoryFault() {
		return false;
	}

	@Override
	synchronized public void rearrange() {
		//this.rearrangeActions(0);
		this.rearrangeStrings(0);
	}
	

}
