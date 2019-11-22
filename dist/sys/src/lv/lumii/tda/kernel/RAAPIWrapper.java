package lv.lumii.tda.kernel;

import java.util.HashSet;

import lv.lumii.tda.raapi.RAAPI;

/* Usage example:
 		RAAPIWrapper raapiw = new RAAPIWrapper(raapi);
		try { ... }
		finally { raapiw.free(); }
 * 
 */

public class RAAPIWrapper extends DelegatorBase {
	
	RAAPI delegate;
	
	@Override
	public RAAPI getDelegate() {
		return delegate;
	}
	
	private HashSet<Long> referencesToFree = new HashSet<Long>();	
	private HashSet<Long> iteratorsToFree = new HashSet<Long>();
	
	private long regRef(long r)
	{
		if (r==0)
			return 0;
		if (referencesToFree.contains(r))
			delegate.freeReference(r);
		else
			referencesToFree.add(r);
		return r;
	}

	private long regIt(long it)
	{
		if (it==0)
			return 0;
		if (iteratorsToFree.contains(it))
			delegate.freeIterator(it);
		else
			iteratorsToFree.add(it);
		return it;
	}
	
	public RAAPIWrapper(RAAPI _delegate)
	{
		delegate = _delegate;
	}
	
	
	public long findClass(String arg0)
	{
		return regRef(getDelegate().findClass(arg0));		
	}
	
	public long createObject(long arg0)
	{
		return regRef(getDelegate().createObject(arg0));
	}
	
	public long getAttributeType(long arg0)
	{
		return regRef(getDelegate().getAttributeType(arg0));
	}
	
	public long getIteratorForClasses()
	{
		return regIt(getDelegate().getIteratorForClasses());
	}
	
	public long findPrimitiveDataType(String arg0)
	{
		return regRef(getDelegate().findPrimitiveDataType(arg0));
	}
	
	public long createAttribute(long arg0, String arg1, long arg2)
	{
		return regRef(getDelegate().createAttribute(arg0, arg1, arg2));
	}
	
	public long findAttribute(long arg0, String arg1)
	{
		return regRef(getDelegate().findAttribute(arg0, arg1));
	}
	
	public long getIteratorForAllAttributes(long arg0)
	{
		return regIt(getDelegate().getIteratorForAllAttributes(arg0));
	}
	public long getAttributeDomain(long arg0)
	{
		return regRef(getDelegate().getAttributeDomain(arg0));
	}
	
	public long createAssociation(long arg0, long arg1, String arg2, String arg3, boolean arg4)
	{
		return regRef(getDelegate().createAssociation(arg0, arg1, arg2, arg3, arg4));
	}
	public long createDirectedAssociation(long arg0, long arg1, String arg2, boolean arg3)
	{
		return regRef(getDelegate().createDirectedAssociation(arg0, arg1, arg2, arg3));
	}
	public long createAdvancedAssociation(String arg0, boolean arg1, boolean arg2)
	{
		return regRef(getDelegate().createAdvancedAssociation(arg0, arg1, arg2));
	}
	public long findAssociationEnd(long arg0, String arg1)
	{
		return regRef(getDelegate().findAssociationEnd(arg0, arg1));
	}
	
	public long getInverseAssociationEnd(long arg0)
	{
		return regRef(getDelegate().getInverseAssociationEnd(arg0));
	}
	public long getSourceClass(long arg0)
	{
		return regRef(getDelegate().getSourceClass(arg0));
	}
	public long getTargetClass(long arg0)
	{
		return regRef(getDelegate().getTargetClass(arg0));
	}
	public long getIteratorForLinkedObjects(long arg0, long arg1)
	{
		return regIt(getDelegate().getIteratorForLinkedObjects(arg0, arg1));
	}
	
	public long resolveIteratorFirst(long arg0)
	{
		return regRef(getDelegate().resolveIteratorFirst(arg0));
	}
	public long resolveIteratorNext(long arg0)
	{
		return regRef(getDelegate().resolveIteratorNext(arg0));
	}	
	
	public long resolveIterator(long arg0, int arg1)
	{
		return regRef(getDelegate().resolveIterator(arg0, arg1));
	}
	
	public void freeReference(long arg0)
	{
		// do nothing; we will free it on free()
	}
	
	public long deserializeReference(String arg0)
	{
		return regRef(getDelegate().deserializeReference(arg0));
	}
	public long getLinguisticClassFor(long arg0)
	{
		return regRef(getDelegate().getLinguisticClassFor(arg0));
	}
	
	public long getIteratorForDirectSubClasses(long arg0)
	{
		return regIt(getDelegate().getIteratorForDirectSubClasses(arg0));
	}
	public long getIteratorForDirectClassObjects(long arg0)
	{
		return regIt(getDelegate().getIteratorForDirectClassObjects(arg0));
	}
	public long getIteratorForDirectObjectClasses(long arg0)
	{
		return regIt(getDelegate().getIteratorForDirectObjectClasses(arg0));
	}
	public long getIteratorForDirectSuperClasses(long arg0)
	{
		return regIt(getDelegate().getIteratorForDirectSuperClasses(arg0));
	}
	public long getIteratorForAllClassObjects(long arg0)
	{
		return regIt(getDelegate().getIteratorForAllClassObjects(arg0));
	}
	public long getIteratorForDirectAttributes(long arg0)
	{
		return regIt(getDelegate().getIteratorForDirectAttributes(arg0));
	}
	public long getIteratorForObjectsByAttributeValue(long arg0, String arg1)
	{
		return regIt(getDelegate().getIteratorForObjectsByAttributeValue(arg0, arg1));
	}
	public long getIteratorForAllOutgoingAssociationEnds(long arg0)
	{
		return regIt(getDelegate().getIteratorForAllOutgoingAssociationEnds(arg0));
	}
	public long getIteratorForDirectOutgoingAssociationEnds(long arg0)
	{
		return regIt(getDelegate().getIteratorForDirectOutgoingAssociationEnds(arg0));
	}
	public long getIteratorForAllIngoingAssociationEnds(long arg0)
	{
		return regIt(getDelegate().getIteratorForAllIngoingAssociationEnds(arg0));
	}
	public long getIteratorForDirectIngoingAssociationEnds(long arg0)
	{
		return regIt(getDelegate().getIteratorForDirectIngoingAssociationEnds(arg0));
	}
	public long getIteratorForLinguisticClasses()
	{
		return regIt(getDelegate().getIteratorForLinguisticClasses());
	}
	public long getIteratorForDirectLinguisticInstances(long arg0)
	{
		return regIt(getDelegate().getIteratorForDirectLinguisticInstances(arg0));
	}
	public long getIteratorForAllLinguisticInstances(long arg0)
	{
		return regIt(getDelegate().getIteratorForAllLinguisticInstances(arg0));
	}
	
	public long createClass(String arg0)
	{
		return regRef(getDelegate().createClass(arg0));
	}

	public boolean deleteAttribute(long arg0)
	{
		if (getDelegate().deleteAttribute(arg0)) {
			referencesToFree.remove(arg0);
			return true;
		}
		else
			return false;			
	}
	
	public boolean deleteAssociation(long arg0)
	{
		if (getDelegate().deleteAssociation(arg0)) {
			referencesToFree.remove(arg0);
			return true;
		}
		else
			return false;			
	}
	
	public boolean deleteObject(long arg0)
	{
		if (getDelegate().deleteObject(arg0)) {
			referencesToFree.remove(arg0);
			return true;			
		}
		else
			return false;
	}
	
	public boolean deleteClass(long arg0)
	{
		if (getDelegate().deleteClass(arg0)) {
			referencesToFree.remove(arg0);
			return true;			
		}
		else
			return false;
	}
	
	public void freeIterator(long arg0)
	{
		// do nothing; we will free it on free()
	}
	
	public void free()	
	{
		for (Long r : referencesToFree)
			delegate.freeReference(r);
		referencesToFree.clear();
		
		for (Long it : iteratorsToFree)
			delegate.freeIterator(it);
		iteratorsToFree.clear();
	}
	
	public void finalize()
	{
		free();
	}
	
	
	// additional useful functions
	public long getFirstClassObject(long rClass)
	{
		long it = this.getIteratorForAllClassObjects(rClass);
		if (it == 0)
			return 0;
		long r = this.resolveIteratorFirst(it);
		this.freeIterator(it);
		return r;
	}
	
	public long getFirstObjectByClassName(String name)
	{
		long rCls = this.findClass(name);
		if (rCls == 0)
			return 0;
		else {
			long retVal = this.getFirstClassObject(rCls);
			this.freeReference(rCls);
			return retVal;
		}
	}
	
	public long getAssociationEndByNames(String className, String roleName)
	{
		long rCls = this.findClass(className);
		if (rCls == 0)
			return 0;
		long retVal = this.findAssociationEnd(rCls, roleName);
		this.freeReference(rCls);
		return retVal;
	}

	public long getFirstObjectClass(long rObject)
	{
		long it = this.getIteratorForDirectObjectClasses(rObject);
		if (it == 0)
			return 0;
		long r = this.resolveIteratorFirst(it);
		this.freeIterator(it);
		return r;
	}	

	public long getFirstLinkedObject(long rObject, String roleName)
	{
		return resolveIteratorFirst(
				getIteratorForLinkedObjects(rObject, findAssociationEnd(getFirstObjectClass(rObject), roleName)));
	}	
	
	public String getObjectClassName(long rObject)
	{
		long rCls = this.getFirstObjectClass(rObject);
		if (rCls == 0)
			return null;
		else {
			String retVal = this.getClassName(rCls);
			this.freeReference(rObject);
			return retVal;
		}
	}
	
	public String getAttributeValueByName(long rObject, String name)
	{
		return getAttributeValue(rObject, findAttribute(getFirstObjectClass(rObject), name));
	}
	
	public boolean createLinkByRoleName(long rSrc, long rTgt, String roleName)
	{
		return this.createLink(rSrc, rTgt, findAssociationEnd(getFirstObjectClass(rSrc), roleName));
	}
	
}

