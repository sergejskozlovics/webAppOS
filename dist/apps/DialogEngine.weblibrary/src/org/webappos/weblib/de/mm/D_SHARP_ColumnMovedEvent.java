// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_ColumnMovedEvent
	extends D_SHARP_Event
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected DialogEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_ColumnMovedEvent since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_ColumnMovedEvent(DialogEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.D_SHARP_COLUMNMOVEDEVENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_COLUMNMOVEDEVENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public D_SHARP_ColumnMovedEvent(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends D_SHARP_ColumnMovedEvent> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_ColumnMovedEvent> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_ColumnMovedEvent> retVal = new ArrayList<D_SHARP_ColumnMovedEvent>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_COLUMNMOVEDEVENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_ColumnMovedEvent o = (D_SHARP_ColumnMovedEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_ColumnMovedEvent)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_ColumnMovedEvent.class, r, true);
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
		for (D_SHARP_ColumnMovedEvent o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_ColumnMovedEvent firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_ColumnMovedEvent firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_COLUMNMOVEDEVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_ColumnMovedEvent  retVal = (D_SHARP_ColumnMovedEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_ColumnMovedEvent)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_ColumnMovedEvent.class, r, true);
			return retVal;
		}
	} 
 
	public List<D_SHARP_VTableType> getColumn()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableType>(factory, rObject, factory.D_SHARP_COLUMNMOVEDEVENT_COLUMN); 
	}
	public boolean setColumn(D_SHARP_VTableType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COLUMNMOVEDEVENT_COLUMN);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COLUMNMOVEDEVENT_COLUMN))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COLUMNMOVEDEVENT_COLUMN))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableType> getBefore()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableType>(factory, rObject, factory.D_SHARP_COLUMNMOVEDEVENT_BEFORE); 
	}
	public boolean setBefore(D_SHARP_VTableType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COLUMNMOVEDEVENT_BEFORE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COLUMNMOVEDEVENT_BEFORE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COLUMNMOVEDEVENT_BEFORE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableType> getAfter()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableType>(factory, rObject, factory.D_SHARP_COLUMNMOVEDEVENT_AFTER); 
	}
	public boolean setAfter(D_SHARP_VTableType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COLUMNMOVEDEVENT_AFTER);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COLUMNMOVEDEVENT_AFTER))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COLUMNMOVEDEVENT_AFTER))
				ok = false;
		return ok;
	}
}
