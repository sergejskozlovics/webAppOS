// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class UnmountRepositoryCommand
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::UnmountRepositoryCommand since the RAAPI wrapper does not take care of this reference.");
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
	UnmountRepositoryCommand(TDAKernelMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.UNMOUNTREPOSITORYCOMMAND), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.UNMOUNTREPOSITORYCOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public UnmountRepositoryCommand(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends UnmountRepositoryCommand> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends UnmountRepositoryCommand> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<UnmountRepositoryCommand> retVal = new ArrayList<UnmountRepositoryCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.UNMOUNTREPOSITORYCOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			UnmountRepositoryCommand o = (UnmountRepositoryCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (UnmountRepositoryCommand)factory.findOrCreateRAAPIReferenceWrapper(UnmountRepositoryCommand.class, r, true);
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
		for (UnmountRepositoryCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static UnmountRepositoryCommand firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static UnmountRepositoryCommand firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.UNMOUNTREPOSITORYCOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			UnmountRepositoryCommand  retVal = (UnmountRepositoryCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (UnmountRepositoryCommand)factory.findOrCreateRAAPIReferenceWrapper(UnmountRepositoryCommand.class, r, true);
			return retVal;
		}
	} 
 
	public String getMountPoint()
	{
		return factory.raapi.getAttributeValue(rObject, factory.UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT);
	}
	public boolean setMountPoint(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT);
		return factory.raapi.setAttributeValue(rObject, factory.UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT, value.toString());
	}
}
