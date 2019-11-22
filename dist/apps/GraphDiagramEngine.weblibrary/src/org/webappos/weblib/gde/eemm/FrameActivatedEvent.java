// automatically generated
package org.webappos.weblib.gde.eemm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class FrameActivatedEvent
	extends Event
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
				System.err.println("Unable to delete the object "+rObject+" of type FrameActivatedEvent since the RAAPI wrapper does not take care of this reference.");
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
	FrameActivatedEvent(EnvironmentEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.FRAMEACTIVATEDEVENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.FRAMEACTIVATEDEVENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public FrameActivatedEvent(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends FrameActivatedEvent> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends FrameActivatedEvent> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<FrameActivatedEvent> retVal = new ArrayList<FrameActivatedEvent>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FRAMEACTIVATEDEVENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			FrameActivatedEvent o = (FrameActivatedEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (FrameActivatedEvent)factory.findOrCreateRAAPIReferenceWrapper(FrameActivatedEvent.class, r, true);
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
		for (FrameActivatedEvent o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static FrameActivatedEvent firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static FrameActivatedEvent firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FRAMEACTIVATEDEVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			FrameActivatedEvent  retVal = (FrameActivatedEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (FrameActivatedEvent)factory.findOrCreateRAAPIReferenceWrapper(FrameActivatedEvent.class, r, true);
			return retVal;
		}
	} 
 
	public Frame getFrame()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAMEACTIVATEDEVENT_FRAME);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Frame retVal = (Frame)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Frame)factory.findOrCreateRAAPIReferenceWrapper(Frame.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setFrame(Frame value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAMEACTIVATEDEVENT_FRAME);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAMEACTIVATEDEVENT_FRAME))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAMEACTIVATEDEVENT_FRAME))
				ok = false;
		return ok;
	}
}
