// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class InterDirectedAssociation
	extends ProxyReference
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected TDAKernelMetamodelFactory factory;
	protected long rObject = 0;
	protected boolean takeReference;
	*/

	public RAAPI getRAAPI()
	{
		return factory.raapi;
	}
	public long getRAAPIReference()
	{
		return rObject;
	}

	public boolean delete()
	{
		if (rObject != 0) {
			if (!takeReference) {
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::InterDirectedAssociation since the RAAPI wrapper does not take care of this reference.");
				return false;
			}
			factory.wrappers.remove(rObject);
			boolean retVal = factory.raapi.deleteObject(rObject);
			if (retVal) {
				rObject = 0;
			}
			else
				factory.wrappers.put(rObject, this); // putting back
			return retVal;
		}
		else
			return false;
	}

	public void finalize()
	{
		if (rObject != 0) {
			if (takeReference) {
				factory.wrappers.remove(rObject);
				factory.raapi.freeReference(rObject);
			}
			rObject = 0;
		}
	}


	// package-visibility:
	InterDirectedAssociation(TDAKernelMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.INTERDIRECTEDASSOCIATION), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.INTERDIRECTEDASSOCIATION);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public InterDirectedAssociation(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		super(_factory, _rObject, _takeReference);
		/*
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
		*/
	}

	// iterator for instances...
	public static Iterable<? extends InterDirectedAssociation> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends InterDirectedAssociation> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<InterDirectedAssociation> retVal = new ArrayList<InterDirectedAssociation>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.INTERDIRECTEDASSOCIATION);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			InterDirectedAssociation o = (InterDirectedAssociation)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (InterDirectedAssociation)factory.findOrCreateRAAPIReferenceWrapper(InterDirectedAssociation.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(TDAKernelMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(TDAKernelMetamodelFactory factory)
	{
		for (InterDirectedAssociation o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static InterDirectedAssociation firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static InterDirectedAssociation firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.INTERDIRECTEDASSOCIATION);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			InterDirectedAssociation  retVal = (InterDirectedAssociation)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (InterDirectedAssociation)factory.findOrCreateRAAPIReferenceWrapper(InterDirectedAssociation.class, r, true);
			return retVal;
		}
	} 
 
	public String getTargetRole()
	{
		return factory.raapi.getAttributeValue(rObject, factory.INTERDIRECTEDASSOCIATION_TARGETROLE);
	}
	public boolean setTargetRole(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.INTERDIRECTEDASSOCIATION_TARGETROLE);
		return factory.raapi.setAttributeValue(rObject, factory.INTERDIRECTEDASSOCIATION_TARGETROLE, value.toString());
	}
	public Boolean getIsComposition()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.INTERDIRECTEDASSOCIATION_ISCOMPOSITION);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setIsComposition(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.INTERDIRECTEDASSOCIATION_ISCOMPOSITION);
		return factory.raapi.setAttributeValue(rObject, factory.INTERDIRECTEDASSOCIATION_ISCOMPOSITION, value.toString());
	}
	public ProxyReference getSourceClass()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDASSOCIATION_SOURCECLASS);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			ProxyReference retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(ProxyReference.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setSourceClass(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDASSOCIATION_SOURCECLASS);
		// deleting previous links...
		if (it != 0) {
			ArrayList<Long> list = new ArrayList<Long>();
			long r = factory.raapi.resolveIteratorFirst(it);
			while (r != 0) {
				list.add(r);
				r = factory.raapi.resolveIteratorNext(it);
			}
			factory.raapi.freeIterator(it);
			for (Long rLinked : list)
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERDIRECTEDASSOCIATION_SOURCECLASS))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERDIRECTEDASSOCIATION_SOURCECLASS))
				ok = false;
		return ok;
	}
	public ProxyReference getTargetClass()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDASSOCIATION_TARGETCLASS);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			ProxyReference retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(ProxyReference.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setTargetClass(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDASSOCIATION_TARGETCLASS);
		// deleting previous links...
		if (it != 0) {
			ArrayList<Long> list = new ArrayList<Long>();
			long r = factory.raapi.resolveIteratorFirst(it);
			while (r != 0) {
				list.add(r);
				r = factory.raapi.resolveIteratorNext(it);
			}
			factory.raapi.freeIterator(it);
			for (Long rLinked : list)
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERDIRECTEDASSOCIATION_TARGETCLASS))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERDIRECTEDASSOCIATION_TARGETCLASS))
				ok = false;
		return ok;
	}
	public InterDirectedAssociation getInverse()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDASSOCIATION_INVERSE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			InterDirectedAssociation retVal = (InterDirectedAssociation)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (InterDirectedAssociation)factory.findOrCreateRAAPIReferenceWrapper(InterDirectedAssociation.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setInverse(InterDirectedAssociation value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDASSOCIATION_INVERSE);
		// deleting previous links...
		if (it != 0) {
			ArrayList<Long> list = new ArrayList<Long>();
			long r = factory.raapi.resolveIteratorFirst(it);
			while (r != 0) {
				list.add(r);
				r = factory.raapi.resolveIteratorNext(it);
			}
			factory.raapi.freeIterator(it);
			for (Long rLinked : list)
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERDIRECTEDASSOCIATION_INVERSE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERDIRECTEDASSOCIATION_INVERSE))
				ok = false;
		return ok;
	}
}
