// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class PortStyle
	extends ElemStyle
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected GraphDiagramEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type PortStyle since the RAAPI wrapper does not take care of this reference.");
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
	PortStyle(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.PORTSTYLE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.PORTSTYLE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public PortStyle(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends PortStyle> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends PortStyle> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<PortStyle> retVal = new ArrayList<PortStyle>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PORTSTYLE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			PortStyle o = (PortStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (PortStyle)factory.findOrCreateRAAPIReferenceWrapper(PortStyle.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		for (PortStyle o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static PortStyle firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static PortStyle firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PORTSTYLE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			PortStyle  retVal = (PortStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (PortStyle)factory.findOrCreateRAAPIReferenceWrapper(PortStyle.class, r, true);
			return retVal;
		}
	} 
 
	public String getPicture()
	{
		return factory.raapi.getAttributeValue(rObject, factory.PORTSTYLE_PICTURE);
	}
	public boolean setPicture(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.PORTSTYLE_PICTURE);
		return factory.raapi.setAttributeValue(rObject, factory.PORTSTYLE_PICTURE, value.toString());
	}
	public Integer getWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.PORTSTYLE_WIDTH);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.PORTSTYLE_WIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.PORTSTYLE_WIDTH, value.toString());
	}
	public Integer getHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.PORTSTYLE_HEIGHT);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.PORTSTYLE_HEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.PORTSTYLE_HEIGHT, value.toString());
	}
	public Integer getAlignment()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.PORTSTYLE_ALIGNMENT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setAlignment(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.PORTSTYLE_ALIGNMENT);
		return factory.raapi.setAttributeValue(rObject, factory.PORTSTYLE_ALIGNMENT, value.toString());
	}
	public Integer getPicPos()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.PORTSTYLE_PICPOS);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPicPos(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.PORTSTYLE_PICPOS);
		return factory.raapi.setAttributeValue(rObject, factory.PORTSTYLE_PICPOS, value.toString());
	}
	public Integer getPicStyle()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.PORTSTYLE_PICSTYLE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPicStyle(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.PORTSTYLE_PICSTYLE);
		return factory.raapi.setAttributeValue(rObject, factory.PORTSTYLE_PICSTYLE, value.toString());
	}
}
