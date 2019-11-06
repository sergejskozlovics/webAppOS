// automatically generated
package lv.lumii.tda.webde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_TextLine
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_TextLine since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_TextLine(DialogEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_TEXTLINE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public D_SHARP_TextLine(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends D_SHARP_TextLine> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_TextLine> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_TextLine> retVal = new ArrayList<D_SHARP_TextLine>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_TEXTLINE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_TextLine o = (D_SHARP_TextLine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_TextLine)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_TextLine.class, r, true);
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
		for (D_SHARP_TextLine o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_TextLine firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_TextLine firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_TEXTLINE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_TextLine  retVal = (D_SHARP_TextLine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_TextLine)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_TextLine.class, r, true);
			return retVal;
		}
	} 
 
	public String getText()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_TEXTLINE_TEXT);
	}
	public boolean setText(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_TEXTLINE_TEXT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_TEXTLINE_TEXT, value.toString());
	}
	public Boolean getInserted()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_TEXTLINE_INSERTED);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_TEXTLINE_INSERTED);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_TEXTLINE_INSERTED, value.toString());
	}
	public Boolean getDeleted()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_TEXTLINE_DELETED);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_TEXTLINE_DELETED);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_TEXTLINE_DELETED, value.toString());
	}
	public Boolean getEdited()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_TEXTLINE_EDITED);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_TEXTLINE_EDITED);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_TEXTLINE_EDITED, value.toString());
	}
	public D_SHARP_MultiLineTextBox getMultiLineTextBox()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TEXTLINE_MULTILINETEXTBOX);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_MultiLineTextBox retVal = (D_SHARP_MultiLineTextBox)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_MultiLineTextBox)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_MultiLineTextBox.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setMultiLineTextBox(D_SHARP_MultiLineTextBox value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TEXTLINE_MULTILINETEXTBOX);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TEXTLINE_MULTILINETEXTBOX))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TEXTLINE_MULTILINETEXTBOX))
				ok = false;
		return ok;
	}
	public List<D_SHARP_MultiLineTextBox> getParentMultiLineTextBox()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_MultiLineTextBox>(factory, rObject, factory.D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX); 
	}
	public boolean setParentMultiLineTextBox(D_SHARP_MultiLineTextBox value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX))
				ok = false;
		return ok;
	}
	public List<D_SHARP_MultiLineTextBoxChangeEvent> getIMultiLineTextBoxChangeEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_MultiLineTextBoxChangeEvent>(factory, rObject, factory.D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT); 
	}
	public boolean setIMultiLineTextBoxChangeEvent(D_SHARP_MultiLineTextBoxChangeEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_MultiLineTextBoxChangeEvent> getDMultiLineTextBoxChangeEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_MultiLineTextBoxChangeEvent>(factory, rObject, factory.D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT); 
	}
	public boolean setDMultiLineTextBoxChangeEvent(D_SHARP_MultiLineTextBoxChangeEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_MultiLineTextBoxChangeEvent> getEMultiLineTextBoxChangeEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_MultiLineTextBoxChangeEvent>(factory, rObject, factory.D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT); 
	}
	public boolean setEMultiLineTextBoxChangeEvent(D_SHARP_MultiLineTextBoxChangeEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT))
				ok = false;
		return ok;
	}
}
