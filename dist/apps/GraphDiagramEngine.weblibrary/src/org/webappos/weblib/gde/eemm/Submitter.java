// automatically generated
package org.webappos.weblib.gde.eemm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Submitter
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
				System.err.println("Unable to delete the object "+rObject+" of type Submitter since the RAAPI wrapper does not take care of this reference.");
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
	Submitter(EnvironmentEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.SUBMITTER);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Submitter(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Submitter> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Submitter> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<Submitter> retVal = new ArrayList<Submitter>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.SUBMITTER);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Submitter o = (Submitter)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Submitter)factory.findOrCreateRAAPIReferenceWrapper(Submitter.class, r, true);
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
		for (Submitter o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Submitter firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Submitter firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.SUBMITTER);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Submitter  retVal = (Submitter)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Submitter)factory.findOrCreateRAAPIReferenceWrapper(Submitter.class, r, true);
			return retVal;
		}
	} 
 
	public Event getEvent()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.SUBMITTER_EVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Event retVal = (Event)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Event)factory.findOrCreateRAAPIReferenceWrapper(Event.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setEvent(Event value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.SUBMITTER_EVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.SUBMITTER_EVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.SUBMITTER_EVENT))
				ok = false;
		return ok;
	}
	public Command getCommand()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.SUBMITTER_COMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Command retVal = (Command)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Command)factory.findOrCreateRAAPIReferenceWrapper(Command.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setCommand(Command value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.SUBMITTER_COMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.SUBMITTER_COMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.SUBMITTER_COMMAND))
				ok = false;
		return ok;
	}
}
