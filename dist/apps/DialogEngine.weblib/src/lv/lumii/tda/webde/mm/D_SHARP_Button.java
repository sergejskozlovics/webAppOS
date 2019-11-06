// automatically generated
package lv.lumii.tda.webde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_Button
	extends D_SHARP_TableComponent
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_Button since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_Button(DialogEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.D_SHARP_BUTTON), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_BUTTON);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public D_SHARP_Button(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends D_SHARP_Button> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_Button> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_Button> retVal = new ArrayList<D_SHARP_Button>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_BUTTON);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_Button o = (D_SHARP_Button)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_Button)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Button.class, r, true);
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
		for (D_SHARP_Button o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_Button firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_Button firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_BUTTON);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_Button  retVal = (D_SHARP_Button)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Button)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Button.class, r, true);
			return retVal;
		}
	} 
 
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_BUTTON_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_BUTTON_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_BUTTON_CAPTION, value.toString());
	}
	public Boolean getCloseOnClick()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_BUTTON_CLOSEONCLICK);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setCloseOnClick(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_BUTTON_CLOSEONCLICK);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_BUTTON_CLOSEONCLICK, value.toString());
	}
	public Boolean getDeleteOnClick()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_BUTTON_DELETEONCLICK);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setDeleteOnClick(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_BUTTON_DELETEONCLICK);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_BUTTON_DELETEONCLICK, value.toString());
	}
	public List<D_SHARP_Form> getDefaultButtonForm()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Form>(factory, rObject, factory.D_SHARP_BUTTON_DEFAULTBUTTONFORM); 
	}
	public boolean setDefaultButtonForm(D_SHARP_Form value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_BUTTON_DEFAULTBUTTONFORM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_BUTTON_DEFAULTBUTTONFORM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_BUTTON_DEFAULTBUTTONFORM))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Form> getCancelButtonForm()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Form>(factory, rObject, factory.D_SHARP_BUTTON_CANCELBUTTONFORM); 
	}
	public boolean setCancelButtonForm(D_SHARP_Form value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_BUTTON_CANCELBUTTONFORM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_BUTTON_CANCELBUTTONFORM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_BUTTON_CANCELBUTTONFORM))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Form> getConstantFormOnClick()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Form>(factory, rObject, factory.D_SHARP_BUTTON_CONSTANTFORMONCLICK); 
	}
	public boolean setConstantFormOnClick(D_SHARP_Form value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_BUTTON_CONSTANTFORMONCLICK);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_BUTTON_CONSTANTFORMONCLICK))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_BUTTON_CONSTANTFORMONCLICK))
				ok = false;
		return ok;
	}
	public D_SHARP_VTableType getVTableType()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_BUTTON_VTABLETYPE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_VTableType retVal = (D_SHARP_VTableType)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_VTableType)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableType.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setVTableType(D_SHARP_VTableType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_BUTTON_VTABLETYPE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_BUTTON_VTABLETYPE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_BUTTON_VTABLETYPE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Form> getFormOnClick()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Form>(factory, rObject, factory.D_SHARP_BUTTON_FORMONCLICK); 
	}
	public boolean setFormOnClick(D_SHARP_Form value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_BUTTON_FORMONCLICK);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_BUTTON_FORMONCLICK))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_BUTTON_FORMONCLICK))
				ok = false;
		return ok;
	}
}
