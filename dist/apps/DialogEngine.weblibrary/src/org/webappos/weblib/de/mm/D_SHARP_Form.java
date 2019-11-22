// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_Form
	extends D_SHARP_VerticalBox
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_Form since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_Form(DialogEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.D_SHARP_FORM), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_FORM);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public D_SHARP_Form(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends D_SHARP_Form> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_Form> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_Form> retVal = new ArrayList<D_SHARP_Form>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_FORM);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_Form o = (D_SHARP_Form)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_Form)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Form.class, r, true);
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
		for (D_SHARP_Form o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_Form firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_Form firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_FORM);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_Form  retVal = (D_SHARP_Form)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Form)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Form.class, r, true);
			return retVal;
		}
	} 
 
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_CAPTION, value.toString());
	}
	public Boolean getButtonClickOnClose()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_BUTTONCLICKONCLOSE);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setButtonClickOnClose(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_BUTTONCLICKONCLOSE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_BUTTONCLICKONCLOSE, value.toString());
	}
	public Boolean getHasMinimizeButton()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_HASMINIMIZEBUTTON);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setHasMinimizeButton(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_HASMINIMIZEBUTTON);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_HASMINIMIZEBUTTON, value.toString());
	}
	public Boolean getHasMaximizeButton()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_HASMAXIMIZEBUTTON);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setHasMaximizeButton(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_HASMAXIMIZEBUTTON);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_HASMAXIMIZEBUTTON, value.toString());
	}
	public Boolean getIsCloseButtonEnabled()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_ISCLOSEBUTTONENABLED);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setIsCloseButtonEnabled(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_ISCLOSEBUTTONENABLED);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_ISCLOSEBUTTONENABLED, value.toString());
	}
	public Integer getLeft()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_LEFT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLeft(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_LEFT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_LEFT, value.toString());
	}
	public Integer getTop()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_TOP);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setTop(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_TOP);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_TOP, value.toString());
	}
	public Integer getWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_WIDTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setWidth(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_WIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_WIDTH, value.toString());
	}
	public Integer getHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_HEIGHT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setHeight(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_HEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_HEIGHT, value.toString());
	}
	public Boolean getEditable()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_FORM_EDITABLE);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setEditable(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_FORM_EDITABLE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_FORM_EDITABLE, value.toString());
	}
	public List<D_SHARP_Button> getDefaultButton()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Button>(factory, rObject, factory.D_SHARP_FORM_DEFAULTBUTTON); 
	}
	public boolean setDefaultButton(D_SHARP_Button value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_FORM_DEFAULTBUTTON);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_FORM_DEFAULTBUTTON))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_FORM_DEFAULTBUTTON))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Button> getCancelButton()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Button>(factory, rObject, factory.D_SHARP_FORM_CANCELBUTTON); 
	}
	public boolean setCancelButton(D_SHARP_Button value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_FORM_CANCELBUTTON);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_FORM_CANCELBUTTON))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_FORM_CANCELBUTTON))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Event> getOwnedEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Event>(factory, rObject, factory.D_SHARP_FORM_OWNEDEVENT); 
	}
	public boolean setOwnedEvent(D_SHARP_Event value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_FORM_OWNEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_FORM_OWNEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_FORM_OWNEDEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Component> getFocused()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Component>(factory, rObject, factory.D_SHARP_FORM_FOCUSED); 
	}
	public boolean setFocused(D_SHARP_Component value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_FORM_FOCUSED);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_FORM_FOCUSED))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_FORM_FOCUSED))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Button> getConstantFormCallingButton()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Button>(factory, rObject, factory.D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON); 
	}
	public boolean setConstantFormCallingButton(D_SHARP_Button value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Component> getFocusOrder()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Component>(factory, rObject, factory.D_SHARP_FORM_FOCUSORDER); 
	}
	public boolean setFocusOrder(D_SHARP_Component value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_FORM_FOCUSORDER);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_FORM_FOCUSORDER))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_FORM_FOCUSORDER))
				ok = false;
		return ok;
	}
	public D_SHARP_Button getCallingButton()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_FORM_CALLINGBUTTON);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_Button retVal = (D_SHARP_Button)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Button)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Button.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setCallingButton(D_SHARP_Button value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_FORM_CALLINGBUTTON);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_FORM_CALLINGBUTTON))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_FORM_CALLINGBUTTON))
				ok = false;
		return ok;
	}
	public List<Frame> getFrame()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<Frame>(factory, rObject, factory.D_SHARP_FORM_FRAME); 
	}
	public boolean setFrame(Frame value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_FORM_FRAME);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_FORM_FRAME))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_FORM_FRAME))
				ok = false;
		return ok;
	}
}
