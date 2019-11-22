// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_Container
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_Container since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_Container(DialogEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.D_SHARP_CONTAINER), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_CONTAINER);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public D_SHARP_Container(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends D_SHARP_Container> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_Container> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_Container> retVal = new ArrayList<D_SHARP_Container>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_CONTAINER);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_Container o = (D_SHARP_Container)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_Container)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Container.class, r, true);
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
		for (D_SHARP_Container o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_Container firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_Container firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_CONTAINER);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_Container  retVal = (D_SHARP_Container)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Container)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Container.class, r, true);
			return retVal;
		}
	} 
 
	public Integer getHorizontalAlignment()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_HORIZONTALALIGNMENT);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_HORIZONTALALIGNMENT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_HORIZONTALALIGNMENT, value.toString());
	}
	public Integer getVerticalAlignment()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_VERTICALALIGNMENT);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_VERTICALALIGNMENT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_VERTICALALIGNMENT, value.toString());
	}
	public Integer getHorizontalSpacing()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_HORIZONTALSPACING);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setHorizontalSpacing(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_HORIZONTALSPACING);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_HORIZONTALSPACING, value.toString());
	}
	public Integer getVerticalSpacing()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_VERTICALSPACING);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setVerticalSpacing(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_VERTICALSPACING);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_VERTICALSPACING, value.toString());
	}
	public Integer getLeftBorder()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_LEFTBORDER);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLeftBorder(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_LEFTBORDER);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_LEFTBORDER, value.toString());
	}
	public Integer getRightBorder()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_RIGHTBORDER);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setRightBorder(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_RIGHTBORDER);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_RIGHTBORDER, value.toString());
	}
	public Integer getTopBorder()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_TOPBORDER);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setTopBorder(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_TOPBORDER);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_TOPBORDER, value.toString());
	}
	public Integer getBottomBorder()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_BOTTOMBORDER);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setBottomBorder(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_BOTTOMBORDER);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_BOTTOMBORDER, value.toString());
	}
	public Integer getLeftPadding()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_LEFTPADDING);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLeftPadding(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_LEFTPADDING);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_LEFTPADDING, value.toString());
	}
	public Integer getRightPadding()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_RIGHTPADDING);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setRightPadding(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_RIGHTPADDING);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_RIGHTPADDING, value.toString());
	}
	public Integer getTopPadding()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_TOPPADDING);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setTopPadding(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_TOPPADDING);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_TOPPADDING, value.toString());
	}
	public Integer getBottomPadding()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_CONTAINER_BOTTOMPADDING);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setBottomPadding(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_CONTAINER_BOTTOMPADDING);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_CONTAINER_BOTTOMPADDING, value.toString());
	}
	public List<D_SHARP_Component> getFocus()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Component>(factory, rObject, factory.D_SHARP_CONTAINER_FOCUS); 
	}
	public boolean setFocus(D_SHARP_Component value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_CONTAINER_FOCUS);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_CONTAINER_FOCUS))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_CONTAINER_FOCUS))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Component> getComponent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Component>(factory, rObject, factory.D_SHARP_CONTAINER_COMPONENT); 
	}
	public boolean setComponent(D_SHARP_Component value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_CONTAINER_COMPONENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_CONTAINER_COMPONENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_CONTAINER_COMPONENT))
				ok = false;
		return ok;
	}
}
