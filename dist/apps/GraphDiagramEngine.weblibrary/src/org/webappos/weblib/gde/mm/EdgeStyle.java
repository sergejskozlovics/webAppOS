// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class EdgeStyle
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
				System.err.println("Unable to delete the object "+rObject+" of type EdgeStyle since the RAAPI wrapper does not take care of this reference.");
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
	EdgeStyle(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.EDGESTYLE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.EDGESTYLE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public EdgeStyle(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends EdgeStyle> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends EdgeStyle> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<EdgeStyle> retVal = new ArrayList<EdgeStyle>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.EDGESTYLE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			EdgeStyle o = (EdgeStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (EdgeStyle)factory.findOrCreateRAAPIReferenceWrapper(EdgeStyle.class, r, true);
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
		for (EdgeStyle o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static EdgeStyle firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static EdgeStyle firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.EDGESTYLE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			EdgeStyle  retVal = (EdgeStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (EdgeStyle)factory.findOrCreateRAAPIReferenceWrapper(EdgeStyle.class, r, true);
			return retVal;
		}
	} 
 
	public Integer getStartShapeCode()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_STARTSHAPECODE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setStartShapeCode(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_STARTSHAPECODE);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_STARTSHAPECODE, value.toString());
	}
	public Integer getStartLineWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_STARTLINEWIDTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setStartLineWidth(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_STARTLINEWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_STARTLINEWIDTH, value.toString());
	}
	public Integer getStartBkgColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_STARTBKGCOLOR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setStartBkgColor(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_STARTBKGCOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_STARTBKGCOLOR, value.toString());
	}
	public Integer getStartLineColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_STARTLINECOLOR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setStartLineColor(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_STARTLINECOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_STARTLINECOLOR, value.toString());
	}
	public Integer getLineType()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_LINETYPE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLineType(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_LINETYPE);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_LINETYPE, value.toString());
	}
	public Integer getEndShapeCode()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_ENDSHAPECODE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setEndShapeCode(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_ENDSHAPECODE);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_ENDSHAPECODE, value.toString());
	}
	public Integer getEndLineWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_ENDLINEWIDTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setEndLineWidth(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_ENDLINEWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_ENDLINEWIDTH, value.toString());
	}
	public Integer getEndBkgColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_ENDBKGCOLOR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setEndBkgColor(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_ENDBKGCOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_ENDBKGCOLOR, value.toString());
	}
	public Integer getEndLineColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_ENDLINECOLOR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setEndLineColor(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_ENDLINECOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_ENDLINECOLOR, value.toString());
	}
	public Integer getMiddleShapeCode()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_MIDDLESHAPECODE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMiddleShapeCode(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_MIDDLESHAPECODE);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_MIDDLESHAPECODE, value.toString());
	}
	public Integer getMiddleLineWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_MIDDLELINEWIDTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMiddleLineWidth(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_MIDDLELINEWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_MIDDLELINEWIDTH, value.toString());
	}
	public Integer getMiddleDashLength()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_MIDDLEDASHLENGTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMiddleDashLength(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_MIDDLEDASHLENGTH);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_MIDDLEDASHLENGTH, value.toString());
	}
	public Integer getMiddleBreakLength()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_MIDDLEBREAKLENGTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMiddleBreakLength(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_MIDDLEBREAKLENGTH);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_MIDDLEBREAKLENGTH, value.toString());
	}
	public Integer getMiddleBkgColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_MIDDLEBKGCOLOR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMiddleBkgColor(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_MIDDLEBKGCOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_MIDDLEBKGCOLOR, value.toString());
	}
	public Integer getMiddleLineColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.EDGESTYLE_MIDDLELINECOLOR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMiddleLineColor(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.EDGESTYLE_MIDDLELINECOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.EDGESTYLE_MIDDLELINECOLOR, value.toString());
	}
}
