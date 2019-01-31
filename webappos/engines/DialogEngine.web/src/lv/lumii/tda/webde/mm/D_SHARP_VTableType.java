// automatically generated
package lv.lumii.tda.webde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_VTableType
	extends D_SHARP_Component
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_VTableType since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_VTableType(DialogEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.D_SHARP_VTABLETYPE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_VTABLETYPE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public D_SHARP_VTableType(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends D_SHARP_VTableType> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_VTableType> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_VTableType> retVal = new ArrayList<D_SHARP_VTableType>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_VTABLETYPE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_VTableType o = (D_SHARP_VTableType)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_VTableType)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableType.class, r, true);
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
		for (D_SHARP_VTableType o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_VTableType firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_VTableType firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_VTABLETYPE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_VTableType  retVal = (D_SHARP_VTableType)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_VTableType)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableType.class, r, true);
			return retVal;
		}
	} 
 
	public Boolean getEditable()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_EDITABLE);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_EDITABLE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_EDITABLE, value.toString());
	}
	public Boolean getMovableRows()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_MOVABLEROWS);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMovableRows(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_MOVABLEROWS);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_MOVABLEROWS, value.toString());
	}
	public Boolean getMovableColumns()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_MOVABLECOLUMNS);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMovableColumns(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_MOVABLECOLUMNS);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_MOVABLECOLUMNS, value.toString());
	}
	public Integer getVerticalAlignment()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_VERTICALALIGNMENT);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_VERTICALALIGNMENT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_VERTICALALIGNMENT, value.toString());
	}
	public String getInsertButtonCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION);
	}
	public boolean setInsertButtonCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION, value.toString());
	}
	public String getDeleteButtonCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION);
	}
	public boolean setDeleteButtonCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION, value.toString());
	}
	public Integer getRowHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_ROWHEIGHT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setRowHeight(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_ROWHEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_ROWHEIGHT, value.toString());
	}
	public Boolean getAutoOrderRows()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_AUTOORDERROWS);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setAutoOrderRows(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_AUTOORDERROWS);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_AUTOORDERROWS, value.toString());
	}
	public Boolean getAutoOrderColumns()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setAutoOrderColumns(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS, value.toString());
	}
	public List<D_SHARP_VTable> getVTable()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTable>(factory, rObject, factory.D_SHARP_VTABLETYPE_VTABLE); 
	}
	public boolean setVTable(D_SHARP_VTable value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLETYPE_VTABLE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLETYPE_VTABLE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLETYPE_VTABLE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableColumnType> getColumnType()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableColumnType>(factory, rObject, factory.D_SHARP_VTABLETYPE_COLUMNTYPE); 
	}
	public boolean setColumnType(D_SHARP_VTableColumnType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLETYPE_COLUMNTYPE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLETYPE_COLUMNTYPE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLETYPE_COLUMNTYPE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableColumnType> getColumn()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableColumnType>(factory, rObject, factory.D_SHARP_VTABLETYPE_COLUMN); 
	}
	public boolean setColumn(D_SHARP_VTableColumnType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLETYPE_COLUMN);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLETYPE_COLUMN))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLETYPE_COLUMN))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Button> getButton()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Button>(factory, rObject, factory.D_SHARP_VTABLETYPE_BUTTON); 
	}
	public boolean setButton(D_SHARP_Button value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLETYPE_BUTTON);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLETYPE_BUTTON))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLETYPE_BUTTON))
				ok = false;
		return ok;
	}
	public List<D_SHARP_ColumnMovedEvent> getColumnMovedEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_ColumnMovedEvent>(factory, rObject, factory.D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT); 
	}
	public boolean setColumnMovedEvent(D_SHARP_ColumnMovedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_ColumnMovedEvent> getBeforeColumnMovedEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_ColumnMovedEvent>(factory, rObject, factory.D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT); 
	}
	public boolean setBeforeColumnMovedEvent(D_SHARP_ColumnMovedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_ColumnMovedEvent> getAfterColumnMovedEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_ColumnMovedEvent>(factory, rObject, factory.D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT); 
	}
	public boolean setAfterColumnMovedEvent(D_SHARP_ColumnMovedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT))
				ok = false;
		return ok;
	}
}
