// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class GraphDiagramStyle
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
				System.err.println("Unable to delete the object "+rObject+" of type GraphDiagramStyle since the RAAPI wrapper does not take care of this reference.");
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
	GraphDiagramStyle(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.GRAPHDIAGRAMSTYLE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.GRAPHDIAGRAMSTYLE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public GraphDiagramStyle(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends GraphDiagramStyle> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends GraphDiagramStyle> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<GraphDiagramStyle> retVal = new ArrayList<GraphDiagramStyle>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.GRAPHDIAGRAMSTYLE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			GraphDiagramStyle o = (GraphDiagramStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (GraphDiagramStyle)factory.findOrCreateRAAPIReferenceWrapper(GraphDiagramStyle.class, r, true);
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
		for (GraphDiagramStyle o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static GraphDiagramStyle firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static GraphDiagramStyle firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.GRAPHDIAGRAMSTYLE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			GraphDiagramStyle  retVal = (GraphDiagramStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (GraphDiagramStyle)factory.findOrCreateRAAPIReferenceWrapper(GraphDiagramStyle.class, r, true);
			return retVal;
		}
	} 
 
	public String getId()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_ID);
	}
	public boolean setId(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_ID);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_ID, value.toString());
	}
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_CAPTION, value.toString());
	}
	public Integer getBkgColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_BKGCOLOR);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_BKGCOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_BKGCOLOR, value.toString());
	}
	public Integer getPrintZoom()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_PRINTZOOM);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPrintZoom(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_PRINTZOOM);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_PRINTZOOM, value.toString());
	}
	public Integer getScreenZoom()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_SCREENZOOM);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setScreenZoom(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_SCREENZOOM);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_SCREENZOOM, value.toString());
	}
	public Integer getLayoutMode()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_LAYOUTMODE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLayoutMode(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_LAYOUTMODE);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_LAYOUTMODE, value.toString());
	}
	public Integer getLayoutAlgorithm()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLayoutAlgorithm(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM, value.toString());
	}
	public List<GraphDiagram> getGraphDiagram()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<GraphDiagram>(factory, rObject, factory.GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM); 
	}
	public boolean setGraphDiagram(GraphDiagram value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM))
				ok = false;
		return ok;
	}
}
