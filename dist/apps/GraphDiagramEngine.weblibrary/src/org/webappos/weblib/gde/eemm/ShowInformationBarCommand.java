// automatically generated
package org.webappos.weblib.gde.eemm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class ShowInformationBarCommand
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
				System.err.println("Unable to delete the object "+rObject+" of type ShowInformationBarCommand since the RAAPI wrapper does not take care of this reference.");
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
	ShowInformationBarCommand(EnvironmentEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.SHOWINFORMATIONBARCOMMAND), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.SHOWINFORMATIONBARCOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public ShowInformationBarCommand(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends ShowInformationBarCommand> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends ShowInformationBarCommand> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<ShowInformationBarCommand> retVal = new ArrayList<ShowInformationBarCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.SHOWINFORMATIONBARCOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			ShowInformationBarCommand o = (ShowInformationBarCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (ShowInformationBarCommand)factory.findOrCreateRAAPIReferenceWrapper(ShowInformationBarCommand.class, r, true);
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
		for (ShowInformationBarCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static ShowInformationBarCommand firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static ShowInformationBarCommand firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.SHOWINFORMATIONBARCOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			ShowInformationBarCommand  retVal = (ShowInformationBarCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ShowInformationBarCommand)factory.findOrCreateRAAPIReferenceWrapper(ShowInformationBarCommand.class, r, true);
			return retVal;
		}
	} 
 
	public String getMessage()
	{
		return factory.raapi.getAttributeValue(rObject, factory.SHOWINFORMATIONBARCOMMAND_MESSAGE);
	}
	public boolean setMessage(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.SHOWINFORMATIONBARCOMMAND_MESSAGE);
		return factory.raapi.setAttributeValue(rObject, factory.SHOWINFORMATIONBARCOMMAND_MESSAGE, value.toString());
	}
}
