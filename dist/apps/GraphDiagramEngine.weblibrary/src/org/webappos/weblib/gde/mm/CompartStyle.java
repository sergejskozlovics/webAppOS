// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class CompartStyle
	extends Style
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
				System.err.println("Unable to delete the object "+rObject+" of type CompartStyle since the RAAPI wrapper does not take care of this reference.");
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
	CompartStyle(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.COMPARTSTYLE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.COMPARTSTYLE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public CompartStyle(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends CompartStyle> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends CompartStyle> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<CompartStyle> retVal = new ArrayList<CompartStyle>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.COMPARTSTYLE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			CompartStyle o = (CompartStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (CompartStyle)factory.findOrCreateRAAPIReferenceWrapper(CompartStyle.class, r, true);
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
		for (CompartStyle o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static CompartStyle firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static CompartStyle firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.COMPARTSTYLE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			CompartStyle  retVal = (CompartStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (CompartStyle)factory.findOrCreateRAAPIReferenceWrapper(CompartStyle.class, r, true);
			return retVal;
		}
	} 
 
	public String getId()
	{
		return factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_ID);
	}
	public boolean setId(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_ID);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_ID, value.toString());
	}
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_CAPTION, value.toString());
	}
	public Integer getNr()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_NR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setNr(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_NR);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_NR, value.toString());
	}
	public Integer getAlignment()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_ALIGNMENT);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_ALIGNMENT);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_ALIGNMENT, value.toString());
	}
	public Integer getAdjustment()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_ADJUSTMENT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setAdjustment(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_ADJUSTMENT);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_ADJUSTMENT, value.toString());
	}
	public String getPicture()
	{
		return factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_PICTURE);
	}
	public boolean setPicture(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_PICTURE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_PICTURE, value.toString());
	}
	public Integer getPicWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_PICWIDTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPicWidth(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_PICWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_PICWIDTH, value.toString());
	}
	public Integer getPicHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_PICHEIGHT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPicHeight(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_PICHEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_PICHEIGHT, value.toString());
	}
	public Integer getPicPos()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_PICPOS);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_PICPOS);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_PICPOS, value.toString());
	}
	public Integer getPicStyle()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_PICSTYLE);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_PICSTYLE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_PICSTYLE, value.toString());
	}
	public Integer getAdornment()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_ADORNMENT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setAdornment(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_ADORNMENT);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_ADORNMENT, value.toString());
	}
	public Integer getLineWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_LINEWIDTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLineWidth(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_LINEWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_LINEWIDTH, value.toString());
	}
	public Integer getLineColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_LINECOLOR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLineColor(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_LINECOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_LINECOLOR, value.toString());
	}
	public String getFontTypeFace()
	{
		return factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_FONTTYPEFACE);
	}
	public boolean setFontTypeFace(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_FONTTYPEFACE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_FONTTYPEFACE, value.toString());
	}
	public Integer getFontCharSet()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_FONTCHARSET);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFontCharSet(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_FONTCHARSET);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_FONTCHARSET, value.toString());
	}
	public Integer getFontColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_FONTCOLOR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFontColor(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_FONTCOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_FONTCOLOR, value.toString());
	}
	public Integer getFontSize()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_FONTSIZE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFontSize(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_FONTSIZE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_FONTSIZE, value.toString());
	}
	public Integer getFontPitch()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_FONTPITCH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFontPitch(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_FONTPITCH);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_FONTPITCH, value.toString());
	}
	public Integer getFontStyle()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_FONTSTYLE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFontStyle(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_FONTSTYLE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_FONTSTYLE, value.toString());
	}
	public Integer getIsVisible()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_ISVISIBLE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setIsVisible(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_ISVISIBLE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_ISVISIBLE, value.toString());
	}
	public Boolean getLineStartDirection()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_LINESTARTDIRECTION);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLineStartDirection(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_LINESTARTDIRECTION);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_LINESTARTDIRECTION, value.toString());
	}
	public Boolean getLineEndDirection()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_LINEENDDIRECTION);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLineEndDirection(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_LINEENDDIRECTION);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_LINEENDDIRECTION, value.toString());
	}
	public Integer getBreakAtSpace()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_BREAKATSPACE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setBreakAtSpace(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_BREAKATSPACE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_BREAKATSPACE, value.toString());
	}
	public Integer getCompactVisible()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_COMPACTVISIBLE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setCompactVisible(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_COMPACTVISIBLE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_COMPACTVISIBLE, value.toString());
	}
	public Integer getDynamicTooltip()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COMPARTSTYLE_DYNAMICTOOLTIP);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setDynamicTooltip(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTSTYLE_DYNAMICTOOLTIP);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTSTYLE_DYNAMICTOOLTIP, value.toString());
	}
	public List<Compartment> getCompartment()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Compartment>(factory, rObject, factory.COMPARTSTYLE_COMPARTMENT); 
	}
	public boolean setCompartment(Compartment value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.COMPARTSTYLE_COMPARTMENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.COMPARTSTYLE_COMPARTMENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.COMPARTSTYLE_COMPARTMENT))
				ok = false;
		return ok;
	}
}
