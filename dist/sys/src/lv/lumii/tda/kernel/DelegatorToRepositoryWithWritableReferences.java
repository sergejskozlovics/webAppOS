package lv.lumii.tda.kernel;

import java.io.*;
import java.util.*;

import lv.lumii.tda.raapi.IRepository;
import lv.lumii.tda.raapi.RAAPI_WR;
import lv.lumii.tda.util.ObjectsIndexer;
import lv.lumii.tda.util.ReverseObjectsIndexer;

/**
 * A repository wrapper, which allocates references incrementally and allows to set reference values for new elements (=implements RAAPI_WR).
 * For clarity, we use the word "reference" for inner references and the word "index" for outer references.
 */
public class DelegatorToRepositoryWithWritableReferences extends DelegatorToRepositoryBase implements RAAPI_WR {
	private ObjectsIndexer<PeculiarIterator> iteratorIndexer = new ObjectsIndexer<PeculiarIterator>(32); // max 32-bits for iterators
	IRepository repo;

	DelegatorToRepositoryWithWritableReferences(IRepository _repo)
	{
		super();
		super.setDelegate(_repo);
		repo = _repo;
	}
		
	
	class PeculiarIterator // stores proxy references
	{
		private ArrayList<Long> list = new ArrayList<Long>();
		private int i = 0;
		PeculiarIterator()
		{
		}
		void add(long r)
		{
			list.add(r);
		}
		
		void addToFront(long r)
		{
			list.add(0, r);
		}
		
		void addAfter(long rNew, long rExisting)
		{
			int i = list.indexOf(rExisting);
			if (i==-1)
				list.add(rNew);
			else
				list.add(i+1, rNew);
		}
		
		boolean alreadyAdded(long r)
		{
			return list.contains(r);
		}
		
		long resolveFirst()
		{
			i = 0;
			return resolveNext();
		}
		long resolveNext()
		{
			if (i < list.size()) {
				long r = list.get(i++);
				return r;
			}
			else
				return 0;
		}		
		long resolve(int _i)
		{
			if ((_i>=0) && (_i < list.size())) {
				Long retVal = list.get(_i);
				this.i = _i+1;
				return retVal==null?0:retVal;
			}
			else
				return 0;
		}
		int getLength()
		{
			return list.size();
		}
		void free()
		{
		}
	}
	
	synchronized private long i2r(long index) { // index to reference
		if (index == 0)
			return 0;
		Long l = i2r_map.get(index);
		if (l==null)
			return 0;
		else
			return l;
	}
	
	synchronized private long r2i(long reference) { // reference to index
		if (reference == 0)
			return 0;
		Long l = r2i_map.get(reference);
		if (l==null)
			return 0;
		else
			return l;		
	}

	synchronized private long r2n(long reference) { // reference to a new index
		//System.out.println("r2n "+reference);
		if (reference == 0)
			return 0;
		
		long i = newIndex();
		//System.out.println("->r2n "+i);
		r2i_map.put(reference, i);
		i2r_map.put(i, reference);
		return i;
	}

	private int predefinedBitsCount = 0;
	private long predefinedBitsValues = 0;
	
	synchronized private long newIndex() {
		maxIndex = (((maxIndex>>predefinedBitsCount)+1) << predefinedBitsCount) | predefinedBitsValues;
		return maxIndex;
	}

	public synchronized long findPrimitiveDataType (String name)
	{		
		return r2i(getDelegate().findPrimitiveDataType(name)); 
	}
	
	public synchronized String getPrimitiveDataTypeName (long iDataType)
	{
		return getDelegate().getPrimitiveDataTypeName(i2r(iDataType));
	}
	
	public synchronized boolean isPrimitiveDataType (long index)
	{
		if (index == 0)
			return false;
		
		return getDelegate().isPrimitiveDataType(i2r(index));		
	}
	
		
	public synchronized long createClass(String name) {
		return r2n(getDelegate().createClass(name));
	}
	
	public synchronized long findClass(String name) {
		return r2i(getDelegate().findClass(name));
	}
	
	public synchronized String getClassName(long iClass) {
		return getDelegate().getClassName(i2r(iClass));
	}
	
	public synchronized boolean deleteClass(long iClass) {
		Long r2 = i2r_map.get(iClass);
		if (r2!=null) {
			getDelegate().deleteClass(r2);
			i2r_map.remove(iClass);
			r2i_map.remove(r2);
			return true;
		}
		else
			return false;
	}
	
	@Override
	public synchronized boolean isClass(long i)
	{
		return getDelegate().isClass(i2r(i));
	}
	
	public synchronized long createObject(long iClass) {
		return r2n(getDelegate().createObject(i2r(iClass)));
	}
	
	public synchronized boolean moveObject(long iObject, long iToClass) {
		return getDelegate().moveObject(i2r(iObject), i2r(iToClass));
	}
	
	public synchronized boolean deleteObject(long iObject) {
		Long r2 = i2r_map.get(iObject);
		if (r2!=null) {
			getDelegate().deleteObject(r2);
			i2r_map.remove(iObject);
			r2i_map.remove(r2);
			return true;
		}
		else
			return false;
	}
	
	public synchronized boolean includeObjectInClass(long iObject, long iClass) {
		return getDelegate().includeObjectInClass(i2r(iObject), i2r(iClass));
	}
	
	public synchronized boolean excludeObjectFromClass(long iObject, long iClass) {
		return getDelegate().excludeObjectFromClass(i2r(iObject), i2r(iClass));
	}
	
	public synchronized long createAttribute(long iClass, String name, long iType) {
		return r2n(getDelegate().createAttribute(i2r(iClass), name, i2r(iType)));
	}
	
	public synchronized long findAttribute(long iClass, String name) {
		return r2i(getDelegate().findAttribute(i2r(iClass), name));
	}
	
	public synchronized String getAttributeName(long iAttribute) {
		return getDelegate().getAttributeName(i2r(iAttribute));
	}
	
	public synchronized long getAttributeDomain(long iAttribute)
	{
		return r2i(getDelegate().getAttributeDomain(i2r(iAttribute)));
	}
	
	public synchronized long getAttributeType(long iAttribute) {
		return r2i(getDelegate().getAttributeType(i2r(iAttribute)));
	}
	
	public synchronized boolean deleteAttribute(long iAttribute) {		
		Long r2 = i2r_map.get(iAttribute);
		if (r2!=null) {
			getDelegate().deleteAttribute(r2);			
			i2r_map.remove(iAttribute);
			r2i_map.remove(r2);
			return true;
		}
		else
			return false;
	}
	
	@Override
	public synchronized boolean isAttribute(long i)
	{
		return getDelegate().isAttribute(i2r(i));
	}
	
	public synchronized boolean setAttributeValue(long iObject, long iAttribute, String value) {
		return getDelegate().setAttributeValue(i2r(iObject), i2r(iAttribute), value);
	}
	
	public synchronized String getAttributeValue(long iObject, long iAttribute) {				
		return getDelegate().getAttributeValue(i2r(iObject), i2r(iAttribute));
	}
	
	public synchronized boolean deleteAttributeValue(long iObject, long iAttribute) {
		return getDelegate().deleteAttributeValue(i2r(iObject), i2r(iAttribute));
	}
	
	public synchronized long createAssociation(long iSourceClass, long iTargetClass,
			String sourceRole, String targetRole, boolean isComposition) {
		long r = getDelegate().createAssociation(
				i2r(iSourceClass),
				i2r(iTargetClass),
				sourceRole,
				targetRole,
				isComposition);
		long rInv = getDelegate().getInverseAssociationEnd(r);
		r2n(rInv); // create mapping for inverse
		return r2n(r);
	}
	
	public synchronized long createDirectedAssociation(long iSourceClass, long iTargetClass,
			String targetRole, boolean isComposition) {
		long r = getDelegate().createDirectedAssociation(
				i2r(iSourceClass),
				i2r(iTargetClass),
				targetRole,
				isComposition);
		long rInv = getDelegate().getInverseAssociationEnd(r);
		r2n(rInv); // create mapping for inverse
		return r2n(r);
	}

	@Override
	public synchronized long createAdvancedAssociation(String name, boolean nAry,
			boolean associationClass) {
		long r = getDelegate().createAdvancedAssociation(name, nAry, associationClass);
		long rInv = getDelegate().getInverseAssociationEnd(r);
		r2n(rInv); // create mapping for inverse
		return r2n(r);
	}

	@Override
	public synchronized boolean isAdvancedAssociation(long i) {
		return getDelegate().isAdvancedAssociation(i2r(i));
	}
	
	public synchronized long findAssociationEnd(long iSourceClass, String targetRoleName) {
		return r2i(getDelegate().findAssociationEnd(i2r(iSourceClass), targetRoleName));
	}
	
	public synchronized long getInverseAssociationEnd(long iAssociation) {
		return r2i(getDelegate().getInverseAssociationEnd(i2r(iAssociation)));
	}
	
	public synchronized long getSourceClass(long iAssociation) {
		return r2i(getDelegate().getSourceClass(i2r(iAssociation)));
	}
	
	public synchronized long getTargetClass(long iAssociation) {
		return r2i(getDelegate().getTargetClass(i2r(iAssociation)));
	}
	
	public synchronized String getRoleName(long iAssociation) {
		return getDelegate().getRoleName(i2r(iAssociation));
	}
	
	public synchronized boolean isComposition(long iAssociation) {
		return getDelegate().isComposition(i2r(iAssociation));
	}
	
	public synchronized boolean deleteAssociation(long iAssociation) {
		Long r2 = i2r_map.get(iAssociation);
		if (r2!=null) {
			getDelegate().deleteAssociation(r2);			
			i2r_map.remove(iAssociation);
			r2i_map.remove(r2);
			return true;
		}
		else
			return false;
	}
	
	@Override
	public synchronized boolean isAssociationEnd(long i)
	{
		return getDelegate().isAssociationEnd(i2r(i));
	}	
	
		
	public synchronized boolean createLink(long iSourceObject, long iTargetObject,
			long iAssociationEnd) {
		return getDelegate().createLink(i2r(iSourceObject), i2r(iTargetObject), i2r(iAssociationEnd));
	}
	
	public synchronized boolean linkExists(long iSourceObject, long iTargetObject,
			long iAssociationEnd) {
		return getDelegate().linkExists(i2r(iSourceObject), i2r(iTargetObject), i2r(iAssociationEnd));
	}
	
	public synchronized boolean createOrderedLink(long iSourceObject, long iTargetObject,
			long iAssociationEnd, int targetPosition) {
		return getDelegate().createOrderedLink(i2r(iSourceObject), i2r(iTargetObject), i2r(iAssociationEnd), targetPosition);
	}
	
	public synchronized int getLinkedObjectPosition(long iSourceObject, long iTargetObject,
			long iAssociationEnd) {
		return getDelegate().getLinkedObjectPosition(i2r(iSourceObject), i2r(iTargetObject), i2r(iAssociationEnd));
	}
	
	public synchronized boolean deleteLink(long iSourceObject, long iTargetObject,
			long iAssociationEnd) {
		return getDelegate().deleteLink(i2r(iSourceObject), i2r(iTargetObject), i2r(iAssociationEnd));
	}
	
	
	public synchronized boolean createGeneralization(long iSubClass, long iSuperClass) {
		return getDelegate().createGeneralization(i2r(iSubClass), i2r(iSuperClass));
	}
	
	public synchronized boolean isDirectSubClass(long iSubClass, long iSuperClass) {
		return getDelegate().isDirectSubClass(i2r(iSubClass), i2r(iSuperClass));
	}
	
	
	public synchronized boolean isDerivedClass(long iDirectlyOrIndirectlyDerivedClass,
			long iSuperClass) {
		return getDelegate().isDerivedClass(i2r(iDirectlyOrIndirectlyDerivedClass), i2r(iSuperClass));
	}
	
	public synchronized boolean deleteGeneralization(long iSubClass, long iSuperClass) {
		return getDelegate().deleteGeneralization(i2r(iSubClass), i2r(iSuperClass));
	}
	
	public synchronized boolean isTypeOf(long iObject, long iClass) {
		return getDelegate().isTypeOf(i2r(iObject), i2r(iClass));
	}
	
	public synchronized boolean isKindOf(long iObject, long iClass) {
		return getDelegate().isKindOf(i2r(iObject), i2r(iClass));
	}
	
			
	public synchronized long getIteratorForClasses() {
		PeculiarIterator pit = new PeculiarIterator();
		
		long itCls = getDelegate().getIteratorForClasses();
		if (itCls != 0) {
			long dCls = getDelegate().resolveIteratorFirst(itCls);
			while (dCls != 0) {
				
				pit.add(r2i(dCls));
				
				dCls = getDelegate().resolveIteratorNext(itCls);
			}
			getDelegate().freeIterator(itCls);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForAllClassObjects(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForAllClassObjects(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForDirectClassObjects(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForDirectClassObjects(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForDirectObjectClasses(long iObject) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForDirectObjectClasses(i2r(iObject));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForAllAttributes(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForAllAttributes(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForDirectAttributes(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForDirectAttributes(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForAllOutgoingAssociationEnds(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForAllOutgoingAssociationEnds(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForDirectOutgoingAssociationEnds(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForDirectOutgoingAssociationEnds(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}

	public synchronized long getIteratorForAllIngoingAssociationEnds(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForAllIngoingAssociationEnds(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForDirectIngoingAssociationEnds(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForDirectIngoingAssociationEnds(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForLinkedObjects(long iObject, long iAssociation) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForLinkedObjects(i2r(iObject), i2r(iAssociation));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForDirectSuperClasses(long iSubClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForDirectSuperClasses(i2r(iSubClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForDirectSubClasses(long iSuperClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForDirectSubClasses(i2r(iSuperClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	public synchronized long getIteratorForObjectsByAttributeValue(long iAttribute,
			String value) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForObjectsByAttributeValue(i2r(iAttribute), value);
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}
	
	@Override
	public synchronized long resolveIteratorFirst(long it) {
		if (it!=0) {
			PeculiarIterator pi = iteratorIndexer.get(it);
			if (pi != null)
				return pi.resolveFirst();
			else
				return 0;
		}
		else
			return 0;
	}

	@Override
	public synchronized long resolveIteratorNext(long it) {
		if (it!=0) {
			PeculiarIterator pi = iteratorIndexer.get(it);
			if (pi != null)
				return pi.resolveNext();
			else
				return 0;
		}
		else
			return 0;
	}
	
	@Override
	public synchronized int getIteratorLength (long it)
	{
		if (it!=0) {
			PeculiarIterator pi = iteratorIndexer.get(it);
			if (pi != null)
				return pi.getLength();
			else
				return 0;
		}
		else
			return 0;		
	}

	@Override
	public synchronized long resolveIterator (long it, int position)
	{
		if (it!=0) {
			PeculiarIterator pi = iteratorIndexer.get(it);
			if (pi != null) {
				return pi.resolve(position);
			}
			else
				return 0;
		}
		else
			return 0;		
	}	
	
	public synchronized String serializeReference(long r) {
		// TODO
		throw new RuntimeException("TDA Kernel: serializeReference() not yet implemented in DelegatorToRepositoryWithWritableReferences.");
	}
	public synchronized long deserializeReference(String s) {
		// TODO
		throw new RuntimeException("TDA Kernel: deserializeReference() not yet implemented in DelegatorToRepositoryWithWritableReferences.");
	}
	
	public synchronized void freeReference(long r) {
	}
	
	public synchronized void freeIterator(long it) {
		if ((it!=0)) {
			PeculiarIterator pi = iteratorIndexer.get(it);
			if (pi != null) {
				pi.free();
				iteratorIndexer.freeIndex(it);
			}
		}
	}
	
	
	

	//***** OPENING/CLOSING THE REPOSITORIES *****//
	
	private String mapLocation = null;
	private boolean even = false;
	private long maxIndex = 0;
	private Map<Long, Long> r2i_map = new HashMap<Long, Long>();
	private Map<Long, Long> i2r_map = new HashMap<Long, Long>();
	
	synchronized private boolean readMap()
	{
		r2i_map.clear();
		i2r_map.clear();
		if (mapLocation == null)
			return false;
		maxIndex = 0;
		File f = new File(mapLocation);
		if (f.exists()) {
			DataInputStream in = null;
			try {
				in = new DataInputStream(new FileInputStream(f));
				long index, r;
			    while (true) { 
			         index = in.readLong();
			         r = in.readLong();
			         r2i_map.put(r, index);
			         i2r_map.put(index, r);
			         if (index > maxIndex)
			        	 maxIndex = index;
			    }
			} catch (FileNotFoundException e) {
				return false;
			} catch (EOFException ignored) {
				try {
					if (in!=null)
						in.close();
				}
				catch(Throwable t) {					
				}
				return true;
			} catch (IOException e) {
				try {
					if (in!=null)
						in.close();
				}
				catch(Throwable t) {					
				}
				return false;
			}
		}
		else {
			long r = getDelegate().findPrimitiveDataType("String");
			if (r != 0) {
				++maxIndex;
				r2i_map.put(r, maxIndex);
				i2r_map.put(maxIndex, r);
				//System.err.println("!!! STRING TYPE IS "+r+" "+maxIndex);
			}
			r = getDelegate().findPrimitiveDataType("Integer");
			if (r != 0) {
				++maxIndex;
				r2i_map.put(r, maxIndex);
				i2r_map.put(maxIndex, r);
			}
			r = getDelegate().findPrimitiveDataType("Real");
			if (r != 0) {
				++maxIndex;
				r2i_map.put(r, maxIndex);
				i2r_map.put(maxIndex, r);
			}
			r = getDelegate().findPrimitiveDataType("Boolean");
			if (r != 0) {
				++maxIndex;
				r2i_map.put(r, maxIndex);
				i2r_map.put(maxIndex, r);
			}
			
			long itC = getDelegate().getIteratorForClasses();
			if (itC!=0) {
				long rC = getDelegate().resolveIteratorFirst(itC);
				while (rC != 0) {
					++maxIndex;
					r2i_map.put(rC, maxIndex);
					i2r_map.put(maxIndex, rC);

					long itO = getDelegate().getIteratorForDirectClassObjects(rC);
					if (itO!=0) {
						long rO = getDelegate().resolveIteratorFirst(itO);
						while (rO != 0) {
							++maxIndex;
							r2i_map.put(rO, maxIndex);
							i2r_map.put(maxIndex, rO);
							rO = getDelegate().resolveIteratorNext(itO);
						}
						getDelegate().freeIterator(itO);
					}				
					
					long itAt = getDelegate().getIteratorForDirectAttributes(rC);
					if (itAt!=0) {
						long rAt = getDelegate().resolveIteratorFirst(itAt);
						while (rAt != 0) {
							++maxIndex;
							r2i_map.put(rAt, maxIndex);
							i2r_map.put(maxIndex, rAt);
							rAt = getDelegate().resolveIteratorNext(itAt);
						}
						getDelegate().freeIterator(itAt);
					}

					long itAO = getDelegate().getIteratorForDirectOutgoingAssociationEnds(rC);
					if (itAO!=0) {
						long rAO = getDelegate().resolveIteratorFirst(itAO);
						while (rAO != 0) {
							++maxIndex;
							r2i_map.put(rAO, maxIndex);
							i2r_map.put(maxIndex, rAO);
							rAO = getDelegate().resolveIteratorNext(itAO);
						}
						getDelegate().freeIterator(itAO);
					}

					long itAI = getDelegate().getIteratorForDirectIngoingAssociationEnds(rC);
					if (itAI!=0) {
						long rAI = getDelegate().resolveIteratorFirst(itAI);
						while (rAI != 0) {
							++maxIndex;
							r2i_map.put(rAI, maxIndex);
							i2r_map.put(maxIndex, rAI);
							rAI = getDelegate().resolveIteratorNext(itAI);
						}
						getDelegate().freeIterator(itAI);
					}
					
					rC = getDelegate().resolveIteratorNext(itC);
				}
				getDelegate().freeIterator(itC);
			}
			return true;
		}
	}
	
	synchronized private boolean saveMap()
	{
		if (mapLocation == null)
			return false;
		File f = new File(mapLocation);
		File fbak = new File(mapLocation+"bak");
		if (fbak.exists() && fbak.isFile())
			fbak.delete();
		if (f.exists() && f.isFile())
			f.renameTo(fbak);
		
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new FileOutputStream(f));
			Map<Long, Long> m = i2r_map;
			for (Long index : m.keySet()) {
				out.writeLong(index);
				out.writeLong(m.get(index));
			}
			out.close();
			return true;
		} catch (IOException e) {
			try {
				if (out!=null)
					out.close();
			}
			catch(Throwable t) {					
			}
			
			if (f.exists() && f.isFile())
				f.delete();			
			if (fbak.exists() && fbak.isFile())
				fbak.renameTo(f);
			
			return false;
		}
	}
	
	@Override
	public synchronized boolean exists(String location) {		
		return getDelegate().exists(location);		
	}

	@Override
	public synchronized boolean open(String location) {
		boolean retVal = getDelegate().open(location);
		
		if (retVal) {
			
			int i = location.indexOf("file:///");
			if (i>=0)
				location = location.substring(8);
			else {
				i = location.indexOf("file:/");
				if (i>=0)
					location = location.substring(6);
			}
			
			File f = new File(location);
			if (f.isDirectory()) {				
				mapLocation = f.getParent()+File.separator+f.getName()+File.separator+"writable_references.map";
			}
			else /* if(f.isFile())*/ {
				mapLocation = f.getParent()+File.separator+f.getName()+".map";
			}
			/*else {
				getDelegate().close();
				return false;
			}*/
				
			if (readMap()) {
				return true;
			}
			else {
				mapLocation = null;
				getDelegate().close();
				return false;
			}
		}
		
		return retVal;
	}

	@Override
	public synchronized void close() {
		getDelegate().close();
		mapLocation = null;
		i2r_map.clear();
		r2i_map.clear();
	}

	@Override
	public synchronized boolean startSave() {		
		if (!getDelegate().startSave())
			return false;
		
		if (!saveMap()) {
			getDelegate().cancelSave();
			return false;
		}
		
		return true;		
	}

	@Override
	public synchronized boolean finishSave() {
		if (getDelegate().finishSave()) {
			File fbak = new File(mapLocation+"bak");
			fbak.delete();
			return true;
		}
		else
			return false;
	}

	@Override
	public synchronized boolean cancelSave() {		
		File f = new File(mapLocation);
		File fbak = new File(mapLocation+"bak");
		if (f.exists()&&f.isFile())
			f.delete();
		if (fbak.exists()&&fbak.isFile())
			fbak.renameTo(f);
		return getDelegate().cancelSave();
	}

	@Override
	public synchronized boolean drop(String location) {

		File f1 = new File(location);
		File f = null;
		if (f1.isDirectory() || f1.isFile()) {
			f = new File(f1.getParent()+File.separator+f1.getName()+".map");
		}
		
		if (getDelegate().drop(location)) {
			if (f.exists()&&f.isFile())
				f.delete();
			return true;
		}
		else
			return false;
	}
	

	@Override
	public synchronized long getIteratorForLinguisticClasses() {

		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForLinguisticClasses();
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}

	@Override
	public synchronized long getIteratorForDirectLinguisticInstances(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForDirectLinguisticInstances(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}

	@Override
	public synchronized long getIteratorForAllLinguisticInstances(long iClass) {
		PeculiarIterator pit = new PeculiarIterator();
		
		long it = getDelegate().getIteratorForAllLinguisticInstances(i2r(iClass));
		if (it != 0) {
			long r = getDelegate().resolveIteratorFirst(it);
			while (r != 0) {
				
				pit.add(r2i(r));
				
				r = getDelegate().resolveIteratorNext(it);
			}
			getDelegate().freeIterator(it);
		}
		
		return iteratorIndexer.acquireIndex(pit);
	}

	@Override
	public synchronized long getLinguisticClassFor(long i) {
		return r2i(getDelegate().getLinguisticClassFor(i2r(i)));
	}

	@Override
	public synchronized boolean isLinguistic(long i) {
		return getDelegate().isLinguistic(i2r(i));
	}

	@Override
	public synchronized String callSpecificOperation(String operationName, String arguments) {
		return getDelegate().callSpecificOperation(operationName, arguments);
	}
	
	//***** WRITABLE INDEXES *****//
	@Override
	public synchronized long getMaxReference() {
		return maxIndex;
	}
	
	@Override
	public boolean setPredefinedBits(int bitsCount, long bitsValues) {
		if (bitsCount>50)
			return false;
		
		predefinedBitsCount = bitsCount;
		predefinedBitsValues = (bitsValues & ((1<<bitsCount)-1)); // keeping only lowest bitsCount bits
		return true;
	}

	@Override
	public int getPredefinedBitsCount() {
		return predefinedBitsCount;
	}

	@Override
	public long getPredefinedBitsValues() {
		return predefinedBitsValues;
	}

	
	@Override
	public synchronized boolean createClass(String name, long i) {
		long r = getDelegate().createClass(name);
		if (r == 0)
			return false;
		r2i_map.put(r, i);
		i2r_map.put(i, r);
		if (i > maxIndex)
			maxIndex = i;
		return true;
	}

	@Override
	public synchronized boolean createObject(long iClass, long i) {
		long r = getDelegate().createObject(i2r(iClass));
		if (r == 0)
			return false;
		r2i_map.put(r, i);
		i2r_map.put(i, r);
		if (i > maxIndex)
			maxIndex = i;
		return true;
	}

	@Override
	public synchronized boolean createAttribute(long iClass, String name, long iType, long i) {
		long r = getDelegate().createAttribute(i2r(iClass), name, i2r(iType));
		if (r == 0)
			return false;
		r2i_map.put(r, i);
		i2r_map.put(i, r);
		if (i > maxIndex)
			maxIndex = i;
		return true;
	}
	
	@Override
	public synchronized boolean createAssociation(long iSourceClass, long iTargetClass,
			String sourceRole, String targetRole, boolean isComposition, long i, long iInv) {
		long r = getDelegate().createAssociation(
						i2r(iSourceClass),
						i2r(iTargetClass),
						sourceRole,
						targetRole,
						isComposition);
		if (r == 0)
			return false;
		long rInv = getDelegate().getInverseAssociationEnd(r);
		r2i_map.put(r, i);
		i2r_map.put(i, r);
		if (i > maxIndex)
			maxIndex = i;
		r2i_map.put(rInv, iInv);
		i2r_map.put(iInv, rInv);
		if (iInv > maxIndex)
			maxIndex = iInv;
		return true;
	}
	
	@Override
	public synchronized boolean createDirectedAssociation(long iSourceClass, long iTargetClass,
			String targetRole, boolean isComposition, long i) {
		long r = getDelegate().createDirectedAssociation(
						i2r(iSourceClass),
						i2r(iTargetClass),
						targetRole,
						isComposition);
		if (r == 0)
			return false;
		r2i_map.put(r, i);
		i2r_map.put(i, r);
		if (i > maxIndex)
			maxIndex = i;
		return true;
	}

	@Override
	public synchronized boolean createAdvancedAssociation(String name, boolean nAry,
			boolean associationClass, long i) {		
		long r = getDelegate().createAdvancedAssociation(name, nAry, associationClass);
		if (r == 0)
			return false;
		r2i_map.put(r, i);
		i2r_map.put(i, r);
		if (i > maxIndex)
			maxIndex = i;
		return true;
	}

}

