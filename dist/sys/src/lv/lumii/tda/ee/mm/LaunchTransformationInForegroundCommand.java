// automatically generated
package lv.lumii.tda.ee.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class LaunchTransformationInForegroundCommand
	extends AsyncCommand
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
				System.err.println("Unable to delete the object "+rObject+" of type LaunchTransformationInForegroundCommand since the RAAPI wrapper does not take care of this reference.");
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
	LaunchTransformationInForegroundCommand(EnvironmentEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public LaunchTransformationInForegroundCommand(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends LaunchTransformationInForegroundCommand> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends LaunchTransformationInForegroundCommand> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<LaunchTransformationInForegroundCommand> retVal = new ArrayList<LaunchTransformationInForegroundCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			LaunchTransformationInForegroundCommand o = (LaunchTransformationInForegroundCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (LaunchTransformationInForegroundCommand)factory.findOrCreateRAAPIReferenceWrapper(LaunchTransformationInForegroundCommand.class, r, true);
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
		for (LaunchTransformationInForegroundCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static LaunchTransformationInForegroundCommand firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static LaunchTransformationInForegroundCommand firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			LaunchTransformationInForegroundCommand  retVal = (LaunchTransformationInForegroundCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (LaunchTransformationInForegroundCommand)factory.findOrCreateRAAPIReferenceWrapper(LaunchTransformationInForegroundCommand.class, r, true);
			return retVal;
		}
	} 
 
	public String getUri()
	{
		return factory.raapi.getAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI);
	}
	public boolean setUri(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI);
		return factory.raapi.setAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI, value.toString());
	}
	public String getMessage()
	{
		return factory.raapi.getAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE);
	}
	public boolean setMessage(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE);
		return factory.raapi.setAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE, value.toString());
	}
	public Integer getProgress()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setProgress(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS);
		return factory.raapi.setAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS, value.toString());
	}
}
