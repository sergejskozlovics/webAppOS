// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Event
  	implements RAAPIReferenceWrapper
{
	protected DialogEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type Event since the RAAPI wrapper does not take care of this reference.");
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

	public boolean submit()
	{
		// return setSubmitter(org.webappos.weblib.de.mm.Submitter.firstObject(factory));
		long SUBMITTER_ROLE = factory.raapi.findAssociationEnd(factory.EVENT, "submitter");  
		boolean retVal = factory.raapi.createLink(rObject, org.webappos.weblib.de.mm.Submitter.firstObject(factory).rObject, SUBMITTER_ROLE);
		factory.raapi.freeReference(SUBMITTER_ROLE);
		return retVal;
	}

	// package-visibility:
	Event(DialogEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.EVENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Event(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Event> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Event> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<Event> retVal = new ArrayList<Event>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.EVENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Event o = (Event)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Event)factory.findOrCreateRAAPIReferenceWrapper(Event.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(DialogEngineMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(DialogEngineMetamodelFactory factory)
	{
		for (Event o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Event firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Event firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.EVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Event  retVal = (Event)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Event)factory.findOrCreateRAAPIReferenceWrapper(Event.class, r, true);
			return retVal;
		}
	} 
 
}
