// automatically generated
package lv.lumii.tda.webde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_VTableColumnType
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_VTableColumnType since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_VTableColumnType(DialogEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_VTABLECOLUMNTYPE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public D_SHARP_VTableColumnType(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends D_SHARP_VTableColumnType> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_VTableColumnType> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_VTableColumnType> retVal = new ArrayList<D_SHARP_VTableColumnType>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_VTABLECOLUMNTYPE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_VTableColumnType o = (D_SHARP_VTableColumnType)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_VTableColumnType)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableColumnType.class, r, true);
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
		for (D_SHARP_VTableColumnType o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_VTableColumnType firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_VTableColumnType firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_VTABLECOLUMNTYPE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_VTableColumnType  retVal = (D_SHARP_VTableColumnType)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_VTableColumnType)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_VTableColumnType.class, r, true);
			return retVal;
		}
	} 
 
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_CAPTION, value.toString());
	}
	public String getDefaultValue()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE);
	}
	public boolean setDefaultValue(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE, value.toString());
	}
	public Boolean getEditable()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_EDITABLE);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_EDITABLE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_EDITABLE, value.toString());
	}
	public Integer getWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_WIDTH);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_WIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_WIDTH, value.toString());
	}
	public Double getPreferredRelativeWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPreferredRelativeWidth(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH, value.toString());
	}
	public Integer getHorizontalAlignment()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setHorizontalAlignment(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT, value.toString());
	}
	public List<D_SHARP_VTableType> getVTableType()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableType>(factory, rObject, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE); 
	}
	public boolean setVTableType(D_SHARP_VTableType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE))
				ok = false;
		return ok;
	}
	public D_SHARP_VTableType getVTableTypeOwner()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER);
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
	public boolean setVTableTypeOwner(D_SHARP_VTableType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER))
				ok = false;
		return ok;
	}
	public List<D_SHARP_VTableCell> getVTableCell()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_VTableCell>(factory, rObject, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLECELL); 
	}
	public boolean setVTableCell(D_SHARP_VTableCell value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLECELL);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLECELL))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECOLUMNTYPE_VTABLECELL))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TableComponent> getDefaultComponentType()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TableComponent>(factory, rObject, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE); 
	}
	public boolean setDefaultComponentType(D_SHARP_TableComponent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TableComponent> getDefaultComponent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TableComponent>(factory, rObject, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT); 
	}
	public boolean setDefaultComponent(D_SHARP_TableComponent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT))
				ok = false;
		return ok;
	}
}
