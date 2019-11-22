// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_ComboBox
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_ComboBox since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_ComboBox(DialogEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.D_SHARP_COMBOBOX), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_COMBOBOX);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public D_SHARP_ComboBox(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends D_SHARP_ComboBox> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_ComboBox> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_ComboBox> retVal = new ArrayList<D_SHARP_ComboBox>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_COMBOBOX);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_ComboBox o = (D_SHARP_ComboBox)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_ComboBox)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_ComboBox.class, r, true);
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
		for (D_SHARP_ComboBox o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_ComboBox firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_ComboBox firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_COMBOBOX);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_ComboBox  retVal = (D_SHARP_ComboBox)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_ComboBox)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_ComboBox.class, r, true);
			return retVal;
		}
	} 
 
	public String getText()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMBOBOX_TEXT);
	}
	public boolean setText(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMBOBOX_TEXT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMBOBOX_TEXT, value.toString());
	}
	public Boolean getEditable()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMBOBOX_EDITABLE);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMBOBOX_EDITABLE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMBOBOX_EDITABLE, value.toString());
	}
	public List<D_SHARP_Item> getItem()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Item>(factory, rObject, factory.D_SHARP_COMBOBOX_ITEM); 
	}
	public boolean setItem(D_SHARP_Item value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMBOBOX_ITEM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMBOBOX_ITEM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMBOBOX_ITEM))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Item> getSelected()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Item>(factory, rObject, factory.D_SHARP_COMBOBOX_SELECTED); 
	}
	public boolean setSelected(D_SHARP_Item value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMBOBOX_SELECTED);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMBOBOX_SELECTED))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMBOBOX_SELECTED))
				ok = false;
		return ok;
	}
}
