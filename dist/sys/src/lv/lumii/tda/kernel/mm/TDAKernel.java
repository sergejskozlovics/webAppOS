// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class TDAKernel
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::TDAKernel since the RAAPI wrapper does not take care of this reference.");
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
	TDAKernel(TDAKernelMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.TDAKERNEL);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public TDAKernel(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends TDAKernel> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends TDAKernel> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<TDAKernel> retVal = new ArrayList<TDAKernel>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.TDAKERNEL);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			TDAKernel o = (TDAKernel)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (TDAKernel)factory.findOrCreateRAAPIReferenceWrapper(TDAKernel.class, r, true);
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
		for (TDAKernel o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static TDAKernel firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static TDAKernel firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.TDAKERNEL);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			TDAKernel  retVal = (TDAKernel)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (TDAKernel)factory.findOrCreateRAAPIReferenceWrapper(TDAKernel.class, r, true);
			return retVal;
		}
	} 
 
	public List<Engine> getAttachedEngine()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<Engine>(factory, rObject, factory.TDAKERNEL_ATTACHEDENGINE); 
	}
	public boolean setAttachedEngine(Engine value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TDAKERNEL_ATTACHEDENGINE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.TDAKERNEL_ATTACHEDENGINE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.TDAKERNEL_ATTACHEDENGINE))
				ok = false;
		return ok;
	}
	public Package getRootPackage()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TDAKERNEL_ROOTPACKAGE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Package retVal = (Package)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Package)factory.findOrCreateRAAPIReferenceWrapper(Package.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setRootPackage(Package value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TDAKERNEL_ROOTPACKAGE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.TDAKERNEL_ROOTPACKAGE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.TDAKERNEL_ROOTPACKAGE))
				ok = false;
		return ok;
	}
	public String getOnSaveStartedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.TDAKERNEL_ONSAVESTARTEDEVENT);
	}
	public boolean setOnSaveStartedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.TDAKERNEL_ONSAVESTARTEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.TDAKERNEL_ONSAVESTARTEDEVENT, value.toString());
	}
	public String getOnSaveFinishedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.TDAKERNEL_ONSAVEFINISHEDEVENT);
	}
	public boolean setOnSaveFinishedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.TDAKERNEL_ONSAVEFINISHEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.TDAKERNEL_ONSAVEFINISHEDEVENT, value.toString());
	}
	public String getOnSaveFailedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.TDAKERNEL_ONSAVEFAILEDEVENT);
	}
	public boolean setOnSaveFailedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.TDAKERNEL_ONSAVEFAILEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.TDAKERNEL_ONSAVEFAILEDEVENT, value.toString());
	}
}
