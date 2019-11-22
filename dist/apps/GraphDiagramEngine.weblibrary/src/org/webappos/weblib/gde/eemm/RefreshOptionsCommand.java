// automatically generated
package org.webappos.weblib.gde.eemm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class RefreshOptionsCommand
	extends Command
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
				System.err.println("Unable to delete the object "+rObject+" of type RefreshOptionsCommand since the RAAPI wrapper does not take care of this reference.");
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
	RefreshOptionsCommand(EnvironmentEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.REFRESHOPTIONSCOMMAND), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.REFRESHOPTIONSCOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public RefreshOptionsCommand(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends RefreshOptionsCommand> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends RefreshOptionsCommand> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<RefreshOptionsCommand> retVal = new ArrayList<RefreshOptionsCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.REFRESHOPTIONSCOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			RefreshOptionsCommand o = (RefreshOptionsCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (RefreshOptionsCommand)factory.findOrCreateRAAPIReferenceWrapper(RefreshOptionsCommand.class, r, true);
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
		for (RefreshOptionsCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static RefreshOptionsCommand firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static RefreshOptionsCommand firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.REFRESHOPTIONSCOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			RefreshOptionsCommand  retVal = (RefreshOptionsCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (RefreshOptionsCommand)factory.findOrCreateRAAPIReferenceWrapper(RefreshOptionsCommand.class, r, true);
			return retVal;
		}
	} 
 
	public List<EnvironmentEngine> getEnvironmentEngine()
	{
		return new EnvironmentEngineMetamodel_RAAPILinkedObjectsList<EnvironmentEngine>(factory, rObject, factory.REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE); 
	}
	public boolean setEnvironmentEngine(EnvironmentEngine value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE))
				ok = false;
		return ok;
	}
}
