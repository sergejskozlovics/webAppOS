// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class ElemStyle
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
				System.err.println("Unable to delete the object "+rObject+" of type ElemStyle since the RAAPI wrapper does not take care of this reference.");
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
	ElemStyle(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.ELEMSTYLE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.ELEMSTYLE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public ElemStyle(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends ElemStyle> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends ElemStyle> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<ElemStyle> retVal = new ArrayList<ElemStyle>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ELEMSTYLE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			ElemStyle o = (ElemStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (ElemStyle)factory.findOrCreateRAAPIReferenceWrapper(ElemStyle.class, r, true);
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
		for (ElemStyle o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static ElemStyle firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static ElemStyle firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ELEMSTYLE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			ElemStyle  retVal = (ElemStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ElemStyle)factory.findOrCreateRAAPIReferenceWrapper(ElemStyle.class, r, true);
			return retVal;
		}
	} 
 
	public String getId()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_ID);
	}
	public boolean setId(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_ID);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_ID, value.toString());
	}
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_CAPTION, value.toString());
	}
	public Integer getShapeCode()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_SHAPECODE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setShapeCode(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_SHAPECODE);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_SHAPECODE, value.toString());
	}
	public Integer getShapeStyle()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_SHAPESTYLE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setShapeStyle(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_SHAPESTYLE);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_SHAPESTYLE, value.toString());
	}
	public Integer getLineWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_LINEWIDTH);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_LINEWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_LINEWIDTH, value.toString());
	}
	public Integer getDashLength()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_DASHLENGTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setDashLength(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_DASHLENGTH);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_DASHLENGTH, value.toString());
	}
	public Integer getBreakLength()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_BREAKLENGTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setBreakLength(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_BREAKLENGTH);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_BREAKLENGTH, value.toString());
	}
	public Integer getBkgColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_BKGCOLOR);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setBkgColor(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_BKGCOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_BKGCOLOR, value.toString());
	}
	public Integer getLineColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_LINECOLOR);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_LINECOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_LINECOLOR, value.toString());
	}
	public Integer getDynamicTooltip()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ELEMSTYLE_DYNAMICTOOLTIP);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMSTYLE_DYNAMICTOOLTIP);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMSTYLE_DYNAMICTOOLTIP, value.toString());
	}
	public List<Element> getElement()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Element>(factory, rObject, factory.ELEMSTYLE_ELEMENT); 
	}
	public boolean setElement(Element value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMSTYLE_ELEMENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMSTYLE_ELEMENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMSTYLE_ELEMENT))
				ok = false;
		return ok;
	}
	public List<UpdateStyleCmd> getUpdateStyleCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<UpdateStyleCmd>(factory, rObject, factory.ELEMSTYLE_UPDATESTYLECMD); 
	}
	public boolean setUpdateStyleCmd(UpdateStyleCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMSTYLE_UPDATESTYLECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMSTYLE_UPDATESTYLECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMSTYLE_UPDATESTYLECMD))
				ok = false;
		return ok;
	}
}
