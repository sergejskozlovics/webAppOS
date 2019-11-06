// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class LaunchTransformationCommand
	extends Command
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::LaunchTransformationCommand since the RAAPI wrapper does not take care of this reference.");
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
	LaunchTransformationCommand(TDAKernelMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.LAUNCHTRANSFORMATIONCOMMAND), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.LAUNCHTRANSFORMATIONCOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public LaunchTransformationCommand(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends LaunchTransformationCommand> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends LaunchTransformationCommand> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<LaunchTransformationCommand> retVal = new ArrayList<LaunchTransformationCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.LAUNCHTRANSFORMATIONCOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			LaunchTransformationCommand o = (LaunchTransformationCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (LaunchTransformationCommand)factory.findOrCreateRAAPIReferenceWrapper(LaunchTransformationCommand.class, r, true);
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
		for (LaunchTransformationCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static LaunchTransformationCommand firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static LaunchTransformationCommand firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.LAUNCHTRANSFORMATIONCOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			LaunchTransformationCommand  retVal = (LaunchTransformationCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (LaunchTransformationCommand)factory.findOrCreateRAAPIReferenceWrapper(LaunchTransformationCommand.class, r, true);
			return retVal;
		}
	} 
 
	public String getUri()
	{
		return factory.raapi.getAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONCOMMAND_URI);
	}
	public boolean setUri(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONCOMMAND_URI);
		return factory.raapi.setAttributeValue(rObject, factory.LAUNCHTRANSFORMATIONCOMMAND_URI, value.toString());
	}
}
