// automatically generated
package org.webappos.weblib.gde.eemm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Frame
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
				System.err.println("Unable to delete the object "+rObject+" of type Frame since the RAAPI wrapper does not take care of this reference.");
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
	Frame(EnvironmentEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.FRAME);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Frame(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Frame> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Frame> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<Frame> retVal = new ArrayList<Frame>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FRAME);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Frame o = (Frame)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Frame)factory.findOrCreateRAAPIReferenceWrapper(Frame.class, r, true);
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
		for (Frame o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Frame firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Frame firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FRAME);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Frame  retVal = (Frame)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Frame)factory.findOrCreateRAAPIReferenceWrapper(Frame.class, r, true);
			return retVal;
		}
	} 
 
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.FRAME_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FRAME_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.FRAME_CAPTION, value.toString());
	}
	public String getContentURI()
	{
		return factory.raapi.getAttributeValue(rObject, factory.FRAME_CONTENTURI);
	}
	public boolean setContentURI(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FRAME_CONTENTURI);
		return factory.raapi.setAttributeValue(rObject, factory.FRAME_CONTENTURI, value.toString());
	}
	public String getLocation()
	{
		return factory.raapi.getAttributeValue(rObject, factory.FRAME_LOCATION);
	}
	public boolean setLocation(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FRAME_LOCATION);
		return factory.raapi.setAttributeValue(rObject, factory.FRAME_LOCATION, value.toString());
	}
	public Boolean getIsResizeable()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FRAME_ISRESIZEABLE);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setIsResizeable(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FRAME_ISRESIZEABLE);
		return factory.raapi.setAttributeValue(rObject, factory.FRAME_ISRESIZEABLE, value.toString());
	}
	public Boolean getIsClosable()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FRAME_ISCLOSABLE);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setIsClosable(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FRAME_ISCLOSABLE);
		return factory.raapi.setAttributeValue(rObject, factory.FRAME_ISCLOSABLE, value.toString());
	}
	public String getOnFrameActivatedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.FRAME_ONFRAMEACTIVATEDEVENT);
	}
	public boolean setOnFrameActivatedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FRAME_ONFRAMEACTIVATEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.FRAME_ONFRAMEACTIVATEDEVENT, value.toString());
	}
	public String getOnFrameDeactivatingEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.FRAME_ONFRAMEDEACTIVATINGEVENT);
	}
	public boolean setOnFrameDeactivatingEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FRAME_ONFRAMEDEACTIVATINGEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.FRAME_ONFRAMEDEACTIVATINGEVENT, value.toString());
	}
	public String getOnFrameResizedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.FRAME_ONFRAMERESIZEDEVENT);
	}
	public boolean setOnFrameResizedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FRAME_ONFRAMERESIZEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.FRAME_ONFRAMERESIZEDEVENT, value.toString());
	}
	public String getOnCloseFrameRequestedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.FRAME_ONCLOSEFRAMEREQUESTEDEVENT);
	}
	public boolean setOnCloseFrameRequestedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FRAME_ONCLOSEFRAMEREQUESTEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.FRAME_ONCLOSEFRAMEREQUESTEDEVENT, value.toString());
	}
	public EnvironmentEngine getEnvironmentEngine()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_ENVIRONMENTENGINE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			EnvironmentEngine retVal = (EnvironmentEngine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (EnvironmentEngine)factory.findOrCreateRAAPIReferenceWrapper(EnvironmentEngine.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setEnvironmentEngine(EnvironmentEngine value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_ENVIRONMENTENGINE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_ENVIRONMENTENGINE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_ENVIRONMENTENGINE))
				ok = false;
		return ok;
	}
	public AttachFrameCommand getAttachFrameCommand()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_ATTACHFRAMECOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			AttachFrameCommand retVal = (AttachFrameCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (AttachFrameCommand)factory.findOrCreateRAAPIReferenceWrapper(AttachFrameCommand.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setAttachFrameCommand(AttachFrameCommand value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_ATTACHFRAMECOMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_ATTACHFRAMECOMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_ATTACHFRAMECOMMAND))
				ok = false;
		return ok;
	}
	public DetachFrameCommand getDetachFrameCommand()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_DETACHFRAMECOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			DetachFrameCommand retVal = (DetachFrameCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (DetachFrameCommand)factory.findOrCreateRAAPIReferenceWrapper(DetachFrameCommand.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setDetachFrameCommand(DetachFrameCommand value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_DETACHFRAMECOMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_DETACHFRAMECOMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_DETACHFRAMECOMMAND))
				ok = false;
		return ok;
	}
	public FrameActivatedEvent getFrameActivatedEvent()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_FRAMEACTIVATEDEVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			FrameActivatedEvent retVal = (FrameActivatedEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (FrameActivatedEvent)factory.findOrCreateRAAPIReferenceWrapper(FrameActivatedEvent.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setFrameActivatedEvent(FrameActivatedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_FRAMEACTIVATEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_FRAMEACTIVATEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_FRAMEACTIVATEDEVENT))
				ok = false;
		return ok;
	}
	public FrameDeactivatingEvent getFrameDeactivatingEvent()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_FRAMEDEACTIVATINGEVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			FrameDeactivatingEvent retVal = (FrameDeactivatingEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (FrameDeactivatingEvent)factory.findOrCreateRAAPIReferenceWrapper(FrameDeactivatingEvent.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setFrameDeactivatingEvent(FrameDeactivatingEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_FRAMEDEACTIVATINGEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_FRAMEDEACTIVATINGEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_FRAMEDEACTIVATINGEVENT))
				ok = false;
		return ok;
	}
	public CloseFrameRequestedEvent getCloseFrameRequestedEvent()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_CLOSEFRAMEREQUESTEDEVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			CloseFrameRequestedEvent retVal = (CloseFrameRequestedEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (CloseFrameRequestedEvent)factory.findOrCreateRAAPIReferenceWrapper(CloseFrameRequestedEvent.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setCloseFrameRequestedEvent(CloseFrameRequestedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_CLOSEFRAMEREQUESTEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_CLOSEFRAMEREQUESTEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_CLOSEFRAMEREQUESTEDEVENT))
				ok = false;
		return ok;
	}
	public FrameResizedEvent getFrameResizedEvent()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_FRAMERESIZEDEVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			FrameResizedEvent retVal = (FrameResizedEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (FrameResizedEvent)factory.findOrCreateRAAPIReferenceWrapper(FrameResizedEvent.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setFrameResizedEvent(FrameResizedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_FRAMERESIZEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_FRAMERESIZEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_FRAMERESIZEDEVENT))
				ok = false;
		return ok;
	}
	public List<Option> getOption()
	{
		return new EnvironmentEngineMetamodel_RAAPILinkedObjectsList<Option>(factory, rObject, factory.FRAME_OPTION); 
	}
	public boolean setOption(Option value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_OPTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_OPTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_OPTION))
				ok = false;
		return ok;
	}
	public PostMessageToFrameCommand getPostMessageToFrameCommand()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_POSTMESSAGETOFRAMECOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			PostMessageToFrameCommand retVal = (PostMessageToFrameCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (PostMessageToFrameCommand)factory.findOrCreateRAAPIReferenceWrapper(PostMessageToFrameCommand.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setPostMessageToFrameCommand(PostMessageToFrameCommand value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_POSTMESSAGETOFRAMECOMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_POSTMESSAGETOFRAMECOMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_POSTMESSAGETOFRAMECOMMAND))
				ok = false;
		return ok;
	}
	public ActivateFrameCommand getActivateFrameCommand()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_ACTIVATEFRAMECOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			ActivateFrameCommand retVal = (ActivateFrameCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ActivateFrameCommand)factory.findOrCreateRAAPIReferenceWrapper(ActivateFrameCommand.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setActivateFrameCommand(ActivateFrameCommand value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_ACTIVATEFRAMECOMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_ACTIVATEFRAMECOMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_ACTIVATEFRAMECOMMAND))
				ok = false;
		return ok;
	}
}
