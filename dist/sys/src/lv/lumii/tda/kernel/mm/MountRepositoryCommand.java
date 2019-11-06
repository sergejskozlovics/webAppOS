// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class MountRepositoryCommand
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::MountRepositoryCommand since the RAAPI wrapper does not take care of this reference.");
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
	MountRepositoryCommand(TDAKernelMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.MOUNTREPOSITORYCOMMAND), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.MOUNTREPOSITORYCOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public MountRepositoryCommand(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends MountRepositoryCommand> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends MountRepositoryCommand> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<MountRepositoryCommand> retVal = new ArrayList<MountRepositoryCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.MOUNTREPOSITORYCOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			MountRepositoryCommand o = (MountRepositoryCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (MountRepositoryCommand)factory.findOrCreateRAAPIReferenceWrapper(MountRepositoryCommand.class, r, true);
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
		for (MountRepositoryCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static MountRepositoryCommand firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static MountRepositoryCommand firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.MOUNTREPOSITORYCOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			MountRepositoryCommand  retVal = (MountRepositoryCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (MountRepositoryCommand)factory.findOrCreateRAAPIReferenceWrapper(MountRepositoryCommand.class, r, true);
			return retVal;
		}
	} 
 
	public String getUri()
	{
		return factory.raapi.getAttributeValue(rObject, factory.MOUNTREPOSITORYCOMMAND_URI);
	}
	public boolean setUri(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.MOUNTREPOSITORYCOMMAND_URI);
		return factory.raapi.setAttributeValue(rObject, factory.MOUNTREPOSITORYCOMMAND_URI, value.toString());
	}
	public String getMountPoint()
	{
		return factory.raapi.getAttributeValue(rObject, factory.MOUNTREPOSITORYCOMMAND_MOUNTPOINT);
	}
	public boolean setMountPoint(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.MOUNTREPOSITORYCOMMAND_MOUNTPOINT);
		return factory.raapi.setAttributeValue(rObject, factory.MOUNTREPOSITORYCOMMAND_MOUNTPOINT, value.toString());
	}
}
