// automatically generated
package lv.lumii.tda.ee.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class UndoIgnoringClass
  	implements RAAPIReferenceWrapper
{
	protected EnvironmentEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::UndoIgnoringClass since the RAAPI wrapper does not take care of this reference.");
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
	UndoIgnoringClass(EnvironmentEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.UNDOIGNORINGCLASS);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public UndoIgnoringClass(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends UndoIgnoringClass> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends UndoIgnoringClass> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<UndoIgnoringClass> retVal = new ArrayList<UndoIgnoringClass>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.UNDOIGNORINGCLASS);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			UndoIgnoringClass o = (UndoIgnoringClass)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (UndoIgnoringClass)factory.findOrCreateRAAPIReferenceWrapper(UndoIgnoringClass.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(EnvironmentEngineMetamodelFactory factory)
	{
		for (UndoIgnoringClass o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static UndoIgnoringClass firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static UndoIgnoringClass firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.UNDOIGNORINGCLASS);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			UndoIgnoringClass  retVal = (UndoIgnoringClass)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (UndoIgnoringClass)factory.findOrCreateRAAPIReferenceWrapper(UndoIgnoringClass.class, r, true);
			return retVal;
		}
	} 
 
}
