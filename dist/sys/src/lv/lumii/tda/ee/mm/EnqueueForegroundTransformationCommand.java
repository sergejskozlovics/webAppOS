// automatically generated
package lv.lumii.tda.ee.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class EnqueueForegroundTransformationCommand
	extends LaunchTransformationInForegroundCommand
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected EnvironmentEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type EnqueueForegroundTransformationCommand since the RAAPI wrapper does not take care of this reference.");
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
	EnqueueForegroundTransformationCommand(EnvironmentEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public EnqueueForegroundTransformationCommand(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends EnqueueForegroundTransformationCommand> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends EnqueueForegroundTransformationCommand> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<EnqueueForegroundTransformationCommand> retVal = new ArrayList<EnqueueForegroundTransformationCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			EnqueueForegroundTransformationCommand o = (EnqueueForegroundTransformationCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (EnqueueForegroundTransformationCommand)factory.findOrCreateRAAPIReferenceWrapper(EnqueueForegroundTransformationCommand.class, r, true);
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
		for (EnqueueForegroundTransformationCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static EnqueueForegroundTransformationCommand firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static EnqueueForegroundTransformationCommand firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			EnqueueForegroundTransformationCommand  retVal = (EnqueueForegroundTransformationCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (EnqueueForegroundTransformationCommand)factory.findOrCreateRAAPIReferenceWrapper(EnqueueForegroundTransformationCommand.class, r, true);
			return retVal;
		}
	} 
 
}
