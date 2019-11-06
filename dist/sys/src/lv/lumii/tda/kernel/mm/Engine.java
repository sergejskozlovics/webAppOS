// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Engine
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::Engine since the RAAPI wrapper does not take care of this reference.");
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
	Engine(TDAKernelMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.ENGINE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Engine(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Engine> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Engine> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<Engine> retVal = new ArrayList<Engine>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ENGINE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Engine o = (Engine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Engine)factory.findOrCreateRAAPIReferenceWrapper(Engine.class, r, true);
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
		for (Engine o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Engine firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Engine firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ENGINE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Engine  retVal = (Engine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Engine)factory.findOrCreateRAAPIReferenceWrapper(Engine.class, r, true);
			return retVal;
		}
	} 
 
	public TDAKernel getKernel()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ENGINE_KERNEL);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			TDAKernel retVal = (TDAKernel)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (TDAKernel)factory.findOrCreateRAAPIReferenceWrapper(TDAKernel.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setKernel(TDAKernel value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ENGINE_KERNEL);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ENGINE_KERNEL))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ENGINE_KERNEL))
				ok = false;
		return ok;
	}
}
