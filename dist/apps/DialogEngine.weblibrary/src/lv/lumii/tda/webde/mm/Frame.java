// automatically generated
package lv.lumii.tda.webde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Frame
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
	Frame(DialogEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.FRAME);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Frame(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Frame> allObjects(DialogEngineMetamodelFactory factory)
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
		return deleteAllObjects(DialogEngineMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(DialogEngineMetamodelFactory factory)
	{
		for (Frame o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Frame firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Frame firstObject(DialogEngineMetamodelFactory factory)
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
	public List<D_SHARP_Form> getForm()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Form>(factory, rObject, factory.FRAME_FORM); 
	}
	public boolean setForm(D_SHARP_Form value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.FRAME_FORM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.FRAME_FORM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.FRAME_FORM))
				ok = false;
		return ok;
	}
}
