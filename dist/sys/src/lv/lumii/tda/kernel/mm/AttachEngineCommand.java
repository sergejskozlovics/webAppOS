// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class AttachEngineCommand
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::AttachEngineCommand since the RAAPI wrapper does not take care of this reference.");
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
	AttachEngineCommand(TDAKernelMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.ATTACHENGINECOMMAND), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.ATTACHENGINECOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public AttachEngineCommand(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends AttachEngineCommand> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends AttachEngineCommand> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<AttachEngineCommand> retVal = new ArrayList<AttachEngineCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ATTACHENGINECOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			AttachEngineCommand o = (AttachEngineCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (AttachEngineCommand)factory.findOrCreateRAAPIReferenceWrapper(AttachEngineCommand.class, r, true);
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
		for (AttachEngineCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static AttachEngineCommand firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static AttachEngineCommand firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ATTACHENGINECOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			AttachEngineCommand  retVal = (AttachEngineCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (AttachEngineCommand)factory.findOrCreateRAAPIReferenceWrapper(AttachEngineCommand.class, r, true);
			return retVal;
		}
	} 
 
	public String getName()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ATTACHENGINECOMMAND_NAME);
	}
	public boolean setName(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ATTACHENGINECOMMAND_NAME);
		return factory.raapi.setAttributeValue(rObject, factory.ATTACHENGINECOMMAND_NAME, value.toString());
	}
}
