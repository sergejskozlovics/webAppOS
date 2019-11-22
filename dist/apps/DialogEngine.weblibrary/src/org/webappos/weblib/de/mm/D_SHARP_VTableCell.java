// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_VTableCell
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_VTableCell since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_VTableCell(DialogEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_VTABLECELL);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public D_SHARP_VTableCell(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends D_SHARP_VTableCell> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_VTableCell> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_VTableCell> retVal = new ArrayList<D_SHARP_VTableCell>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_VTABLECELL);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_VTableCell o = (D_SHARP_VTableCell)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_VTableCell)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableCell.class, r, true);
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
		for (D_SHARP_VTableCell o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_VTableCell firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_VTableCell firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_VTABLECELL);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_VTableCell  retVal = (D_SHARP_VTableCell)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_VTableCell)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableCell.class, r, true);
			return retVal;
		}
	} 
 
	public String getValue()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLECELL_VALUE);
	}
	public boolean setValue(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLECELL_VALUE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLECELL_VALUE, value.toString());
	}
	public D_SHARP_VTableRow getVTableRow()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECELL_VTABLEROW);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_VTableRow retVal = (D_SHARP_VTableRow)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_VTableRow)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableRow.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setVTableRow(D_SHARP_VTableRow value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECELL_VTABLEROW);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECELL_VTABLEROW))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECELL_VTABLEROW))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableRow> getParentRow()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableRow>(factory, rObject, factory.D_SHARP_VTABLECELL_PARENTROW); 
	}
	public boolean setParentRow(D_SHARP_VTableRow value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECELL_PARENTROW);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECELL_PARENTROW))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECELL_PARENTROW))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Item> getSelectedItem()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Item>(factory, rObject, factory.D_SHARP_VTABLECELL_SELECTEDITEM); 
	}
	public boolean setSelectedItem(D_SHARP_Item value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECELL_SELECTEDITEM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECELL_SELECTEDITEM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECELL_SELECTEDITEM))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableColumnType> getVTableColumnType()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableColumnType>(factory, rObject, factory.D_SHARP_VTABLECELL_VTABLECOLUMNTYPE); 
	}
	public boolean setVTableColumnType(D_SHARP_VTableColumnType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECELL_VTABLECOLUMNTYPE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECELL_VTABLECOLUMNTYPE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECELL_VTABLECOLUMNTYPE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TableComponent> getComponentType()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TableComponent>(factory, rObject, factory.D_SHARP_VTABLECELL_COMPONENTTYPE); 
	}
	public boolean setComponentType(D_SHARP_TableComponent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECELL_COMPONENTTYPE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECELL_COMPONENTTYPE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECELL_COMPONENTTYPE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TableComponent> getComponent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TableComponent>(factory, rObject, factory.D_SHARP_VTABLECELL_COMPONENT); 
	}
	public boolean setComponent(D_SHARP_TableComponent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECELL_COMPONENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECELL_COMPONENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECELL_COMPONENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Event> getEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Event>(factory, rObject, factory.D_SHARP_VTABLECELL_EVENT); 
	}
	public boolean setEvent(D_SHARP_Event value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECELL_EVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECELL_EVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECELL_EVENT))
				ok = false;
		return ok;
	}
}
