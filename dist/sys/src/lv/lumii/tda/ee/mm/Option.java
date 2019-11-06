// automatically generated
package lv.lumii.tda.ee.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Option
	extends UndoIgnoringClass
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
				System.err.println("Unable to delete the object "+rObject+" of type Option since the RAAPI wrapper does not take care of this reference.");
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
	Option(EnvironmentEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.OPTION), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.OPTION);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public Option(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends Option> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Option> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<Option> retVal = new ArrayList<Option>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.OPTION);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Option o = (Option)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Option)factory.findOrCreateRAAPIReferenceWrapper(Option.class, r, true);
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
		for (Option o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Option firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Option firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.OPTION);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Option  retVal = (Option)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Option)factory.findOrCreateRAAPIReferenceWrapper(Option.class, r, true);
			return retVal;
		}
	} 
 
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.OPTION_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.OPTION_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.OPTION_CAPTION, value.toString());
	}
	public String getImage()
	{
		return factory.raapi.getAttributeValue(rObject, factory.OPTION_IMAGE);
	}
	public boolean setImage(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.OPTION_IMAGE);
		return factory.raapi.setAttributeValue(rObject, factory.OPTION_IMAGE, value.toString());
	}
	public String getId()
	{
		return factory.raapi.getAttributeValue(rObject, factory.OPTION_ID);
	}
	public boolean setId(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.OPTION_ID);
		return factory.raapi.setAttributeValue(rObject, factory.OPTION_ID, value.toString());
	}
	public String getLocation()
	{
		return factory.raapi.getAttributeValue(rObject, factory.OPTION_LOCATION);
	}
	public boolean setLocation(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.OPTION_LOCATION);
		return factory.raapi.setAttributeValue(rObject, factory.OPTION_LOCATION, value.toString());
	}
	public String getOnOptionSelectedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.OPTION_ONOPTIONSELECTEDEVENT);
	}
	public boolean setOnOptionSelectedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.OPTION_ONOPTIONSELECTEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.OPTION_ONOPTIONSELECTEDEVENT, value.toString());
	}
	public EnvironmentEngine getEnvironmentEngine()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.OPTION_ENVIRONMENTENGINE);
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
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.OPTION_ENVIRONMENTENGINE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.OPTION_ENVIRONMENTENGINE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.OPTION_ENVIRONMENTENGINE))
				ok = false;
		return ok;
	}
	public List<OptionSelectedEvent> getOptionSelectedEvent()
	{
		return new EnvironmentEngineMetamodel_RAAPILinkedObjectsList<OptionSelectedEvent>(factory, rObject, factory.OPTION_OPTIONSELECTEDEVENT); 
	}
	public boolean setOptionSelectedEvent(OptionSelectedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.OPTION_OPTIONSELECTEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.OPTION_OPTIONSELECTEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.OPTION_OPTIONSELECTEDEVENT))
				ok = false;
		return ok;
	}
	public Option getParent()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.OPTION_PARENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Option retVal = (Option)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Option)factory.findOrCreateRAAPIReferenceWrapper(Option.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setParent(Option value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.OPTION_PARENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.OPTION_PARENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.OPTION_PARENT))
				ok = false;
		return ok;
	}
	public List<Option> getChild()
	{
		return new EnvironmentEngineMetamodel_RAAPILinkedObjectsList<Option>(factory, rObject, factory.OPTION_CHILD); 
	}
	public boolean setChild(Option value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.OPTION_CHILD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.OPTION_CHILD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.OPTION_CHILD))
				ok = false;
		return ok;
	}
	public List<Frame> getFrame()
	{
		return new EnvironmentEngineMetamodel_RAAPILinkedObjectsList<Frame>(factory, rObject, factory.OPTION_FRAME); 
	}
	public boolean setFrame(Frame value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.OPTION_FRAME);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.OPTION_FRAME))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.OPTION_FRAME))
				ok = false;
		return ok;
	}
	public Boolean getIsEnabled()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.OPTION_ISENABLED);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setIsEnabled(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.OPTION_ISENABLED);
		return factory.raapi.setAttributeValue(rObject, factory.OPTION_ISENABLED, value.toString());
	}
}
