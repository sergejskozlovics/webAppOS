// automatically generated
package lv.lumii.tda.webde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_VTableRow
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_VTableRow since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_VTableRow(DialogEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_VTABLEROW);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public D_SHARP_VTableRow(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends D_SHARP_VTableRow> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_VTableRow> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_VTableRow> retVal = new ArrayList<D_SHARP_VTableRow>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_VTABLEROW);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_VTableRow o = (D_SHARP_VTableRow)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_VTableRow)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableRow.class, r, true);
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
		for (D_SHARP_VTableRow o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_VTableRow firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_VTableRow firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_VTABLEROW);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_VTableRow  retVal = (D_SHARP_VTableRow)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_VTableRow)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableRow.class, r, true);
			return retVal;
		}
	} 
 
	public Boolean getEdited()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLEROW_EDITED);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setEdited(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLEROW_EDITED);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLEROW_EDITED, value.toString());
	}
	public Boolean getInserted()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLEROW_INSERTED);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setInserted(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLEROW_INSERTED);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLEROW_INSERTED, value.toString());
	}
	public Boolean getDeleted()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLEROW_DELETED);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setDeleted(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLEROW_DELETED);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLEROW_DELETED, value.toString());
	}
	public Integer getVerticalAlignment()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLEROW_VERTICALALIGNMENT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setVerticalAlignment(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLEROW_VERTICALALIGNMENT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLEROW_VERTICALALIGNMENT, value.toString());
	}
	public D_SHARP_VTable getVTable()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLEROW_VTABLE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_VTable retVal = (D_SHARP_VTable)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_VTable)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTable.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setVTable(D_SHARP_VTable value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLEROW_VTABLE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLEROW_VTABLE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLEROW_VTABLE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableCell> getVTableCell()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableCell>(factory, rObject, factory.D_SHARP_VTABLEROW_VTABLECELL); 
	}
	public boolean setVTableCell(D_SHARP_VTableCell value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLEROW_VTABLECELL);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLEROW_VTABLECELL))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLEROW_VTABLECELL))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableCell> getActiveCell()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableCell>(factory, rObject, factory.D_SHARP_VTABLEROW_ACTIVECELL); 
	}
	public boolean setActiveCell(D_SHARP_VTableCell value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLEROW_ACTIVECELL);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLEROW_ACTIVECELL))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLEROW_ACTIVECELL))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTable> getParentTable()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTable>(factory, rObject, factory.D_SHARP_VTABLEROW_PARENTTABLE); 
	}
	public boolean setParentTable(D_SHARP_VTable value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLEROW_PARENTTABLE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLEROW_PARENTTABLE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLEROW_PARENTTABLE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_RowMovedEvent> getRowMovedEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_RowMovedEvent>(factory, rObject, factory.D_SHARP_VTABLEROW_ROWMOVEDEVENT); 
	}
	public boolean setRowMovedEvent(D_SHARP_RowMovedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLEROW_ROWMOVEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLEROW_ROWMOVEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLEROW_ROWMOVEDEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_RowMovedEvent> getBeforeRowMovedEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_RowMovedEvent>(factory, rObject, factory.D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT); 
	}
	public boolean setBeforeRowMovedEvent(D_SHARP_RowMovedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_RowMovedEvent> getAfterRowMovedEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_RowMovedEvent>(factory, rObject, factory.D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT); 
	}
	public boolean setAfterRowMovedEvent(D_SHARP_RowMovedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT))
				ok = false;
		return ok;
	}
}
