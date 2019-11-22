// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_Item
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_Item since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_Item(DialogEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_ITEM);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public D_SHARP_Item(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends D_SHARP_Item> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_Item> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_Item> retVal = new ArrayList<D_SHARP_Item>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_ITEM);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_Item o = (D_SHARP_Item)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_Item)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Item.class, r, true);
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
		for (D_SHARP_Item o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_Item firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_Item firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_ITEM);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_Item  retVal = (D_SHARP_Item)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Item)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Item.class, r, true);
			return retVal;
		}
	} 
 
	public String getValue()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_ITEM_VALUE);
	}
	public boolean setValue(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_ITEM_VALUE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_ITEM_VALUE, value.toString());
	}
	public D_SHARP_ListBox getListBox()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_ITEM_LISTBOX);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_ListBox retVal = (D_SHARP_ListBox)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_ListBox)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_ListBox.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setListBox(D_SHARP_ListBox value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_ITEM_LISTBOX);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_ITEM_LISTBOX))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_ITEM_LISTBOX))
				ok = false;
		return ok;
	}
	public D_SHARP_ComboBox getComboBox()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_ITEM_COMBOBOX);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_ComboBox retVal = (D_SHARP_ComboBox)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_ComboBox)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_ComboBox.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setComboBox(D_SHARP_ComboBox value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_ITEM_COMBOBOX);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_ITEM_COMBOBOX))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_ITEM_COMBOBOX))
				ok = false;
		return ok;
	}
	public List<D_SHARP_ComboBox> getParentComboBox()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_ComboBox>(factory, rObject, factory.D_SHARP_ITEM_PARENTCOMBOBOX); 
	}
	public boolean setParentComboBox(D_SHARP_ComboBox value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_ITEM_PARENTCOMBOBOX);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_ITEM_PARENTCOMBOBOX))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_ITEM_PARENTCOMBOBOX))
				ok = false;
		return ok;
	}
	public List<D_SHARP_ListBox> getParentListBox()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_ListBox>(factory, rObject, factory.D_SHARP_ITEM_PARENTLISTBOX); 
	}
	public boolean setParentListBox(D_SHARP_ListBox value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_ITEM_PARENTLISTBOX);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_ITEM_PARENTLISTBOX))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_ITEM_PARENTLISTBOX))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableCell> getVTableCell()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableCell>(factory, rObject, factory.D_SHARP_ITEM_VTABLECELL); 
	}
	public boolean setVTableCell(D_SHARP_VTableCell value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_ITEM_VTABLECELL);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_ITEM_VTABLECELL))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_ITEM_VTABLECELL))
				ok = false;
		return ok;
	}
	public List<D_SHARP_ListBoxChangeEvent> getSListBoxChangeEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_ListBoxChangeEvent>(factory, rObject, factory.D_SHARP_ITEM_SLISTBOXCHANGEEVENT); 
	}
	public boolean setSListBoxChangeEvent(D_SHARP_ListBoxChangeEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_ITEM_SLISTBOXCHANGEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_ITEM_SLISTBOXCHANGEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_ITEM_SLISTBOXCHANGEEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_ListBoxChangeEvent> getDListBoxChangeEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_ListBoxChangeEvent>(factory, rObject, factory.D_SHARP_ITEM_DLISTBOXCHANGEEVENT); 
	}
	public boolean setDListBoxChangeEvent(D_SHARP_ListBoxChangeEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_ITEM_DLISTBOXCHANGEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_ITEM_DLISTBOXCHANGEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_ITEM_DLISTBOXCHANGEEVENT))
				ok = false;
		return ok;
	}
}
