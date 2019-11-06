// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class InterAttributeValue
  	implements RAAPIReferenceWrapper
{
	protected TDAKernelMetamodelFactory factory;
	protected long rObject = 0;
	protected boolean takeReference;

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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::InterAttributeValue since the RAAPI wrapper does not take care of this reference.");
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
	InterAttributeValue(TDAKernelMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.INTERATTRIBUTEVALUE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public InterAttributeValue(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends InterAttributeValue> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends InterAttributeValue> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<InterAttributeValue> retVal = new ArrayList<InterAttributeValue>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.INTERATTRIBUTEVALUE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			InterAttributeValue o = (InterAttributeValue)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (InterAttributeValue)factory.findOrCreateRAAPIReferenceWrapper(InterAttributeValue.class, r, true);
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
		for (InterAttributeValue o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static InterAttributeValue firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static InterAttributeValue firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.INTERATTRIBUTEVALUE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			InterAttributeValue  retVal = (InterAttributeValue)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (InterAttributeValue)factory.findOrCreateRAAPIReferenceWrapper(InterAttributeValue.class, r, true);
			return retVal;
		}
	} 
 
	public ProxyReference getObject()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERATTRIBUTEVALUE_OBJECT);
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
	public boolean setObject(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERATTRIBUTEVALUE_OBJECT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERATTRIBUTEVALUE_OBJECT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERATTRIBUTEVALUE_OBJECT))
				ok = false;
		return ok;
	}
	public ProxyReference getAttribute()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERATTRIBUTEVALUE_ATTRIBUTE);
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
	public boolean setAttribute(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERATTRIBUTEVALUE_ATTRIBUTE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERATTRIBUTEVALUE_ATTRIBUTE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERATTRIBUTEVALUE_ATTRIBUTE))
				ok = false;
		return ok;
	}
	public String getValue()
	{
		return factory.raapi.getAttributeValue(rObject, factory.INTERATTRIBUTEVALUE_VALUE);
	}
	public boolean setValue(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.INTERATTRIBUTEVALUE_VALUE);
		return factory.raapi.setAttributeValue(rObject, factory.INTERATTRIBUTEVALUE_VALUE, value.toString());
	}
}
