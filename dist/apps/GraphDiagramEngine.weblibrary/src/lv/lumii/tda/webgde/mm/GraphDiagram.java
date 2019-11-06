// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class GraphDiagram
	extends PresentationElement
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
				System.err.println("Unable to delete the object "+rObject+" of type GraphDiagram since the RAAPI wrapper does not take care of this reference.");
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
	GraphDiagram(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.GRAPHDIAGRAM), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.GRAPHDIAGRAM);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public GraphDiagram(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends GraphDiagram> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends GraphDiagram> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<GraphDiagram> retVal = new ArrayList<GraphDiagram>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.GRAPHDIAGRAM);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			GraphDiagram o = (GraphDiagram)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (GraphDiagram)factory.findOrCreateRAAPIReferenceWrapper(GraphDiagram.class, r, true);
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
		for (GraphDiagram o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static GraphDiagram firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static GraphDiagram firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.GRAPHDIAGRAM);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			GraphDiagram  retVal = (GraphDiagram)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (GraphDiagram)factory.findOrCreateRAAPIReferenceWrapper(GraphDiagram.class, r, true);
			return retVal;
		}
	} 
 
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_CAPTION, value.toString());
	}
	public String getStyle()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_STYLE);
	}
	public boolean setStyle(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_STYLE);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_STYLE, value.toString());
	}
	public String getGraphDgrType()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_GRAPHDGRTYPE);
	}
	public boolean setGraphDgrType(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_GRAPHDGRTYPE);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_GRAPHDGRTYPE, value.toString());
	}
	public Boolean getIsReadOnly()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_ISREADONLY);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setIsReadOnly(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_ISREADONLY);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_ISREADONLY, value.toString());
	}
	public String getRemoteId()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_REMOTEID);
	}
	public boolean setRemoteId(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_REMOTEID);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_REMOTEID, value.toString());
	}
	public String getTargetDiagramRemoteId()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID);
	}
	public boolean setTargetDiagramRemoteId(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID, value.toString());
	}
	public String getTreeErrorIcon()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_TREEERRORICON);
	}
	public boolean setTreeErrorIcon(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_TREEERRORICON);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_TREEERRORICON, value.toString());
	}
	public String getTreeMultiUserIcon()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_TREEMULTIUSERICON);
	}
	public boolean setTreeMultiUserIcon(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_TREEMULTIUSERICON);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_TREEMULTIUSERICON, value.toString());
	}
	public String getMultiComment()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_MULTICOMMENT);
	}
	public boolean setMultiComment(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_MULTICOMMENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_MULTICOMMENT, value.toString());
	}
	public Boolean getIsReadOnly2()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_ISREADONLY2);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setIsReadOnly2(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_ISREADONLY2);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_ISREADONLY2, value.toString());
	}
	public Integer getBkgColor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_BKGCOLOR);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_BKGCOLOR);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_BKGCOLOR, value.toString());
	}
	public Integer getPrintZoom()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_PRINTZOOM);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_PRINTZOOM);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_PRINTZOOM, value.toString());
	}
	public Integer getScreenZoom()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_SCREENZOOM);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_SCREENZOOM);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_SCREENZOOM, value.toString());
	}
	public Integer getLayoutMode()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_LAYOUTMODE);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_LAYOUTMODE);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_LAYOUTMODE, value.toString());
	}
	public Integer getLayoutAlgorithm()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAM_LAYOUTALGORITHM);
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
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAM_LAYOUTALGORITHM);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAM_LAYOUTALGORITHM, value.toString());
	}
	public List<Palette> getPalette()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Palette>(factory, rObject, factory.GRAPHDIAGRAM_PALETTE); 
	}
	public boolean setPalette(Palette value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_PALETTE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_PALETTE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_PALETTE))
				ok = false;
		return ok;
	}
	public List<Toolbar> getToolbar()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Toolbar>(factory, rObject, factory.GRAPHDIAGRAM_TOOLBAR); 
	}
	public boolean setToolbar(Toolbar value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_TOOLBAR);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_TOOLBAR))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_TOOLBAR))
				ok = false;
		return ok;
	}
	public List<GraphDiagramStyle> getGraphDiagramStyle()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<GraphDiagramStyle>(factory, rObject, factory.GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE); 
	}
	public boolean setGraphDiagramStyle(GraphDiagramStyle value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE))
				ok = false;
		return ok;
	}
	public List<GraphDiagramType> getGraphDiagramType()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<GraphDiagramType>(factory, rObject, factory.GRAPHDIAGRAM_GRAPHDIAGRAMTYPE); 
	}
	public boolean setGraphDiagramType(GraphDiagramType value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_GRAPHDIAGRAMTYPE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_GRAPHDIAGRAMTYPE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_GRAPHDIAGRAMTYPE))
				ok = false;
		return ok;
	}
	public List<CurrentDgrPointer> getCurrentDgrPointer()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<CurrentDgrPointer>(factory, rObject, factory.GRAPHDIAGRAM_CURRENTDGRPOINTER); 
	}
	public boolean setCurrentDgrPointer(CurrentDgrPointer value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_CURRENTDGRPOINTER);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_CURRENTDGRPOINTER))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_CURRENTDGRPOINTER))
				ok = false;
		return ok;
	}
	public List<Collection> getCollection()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Collection>(factory, rObject, factory.GRAPHDIAGRAM_COLLECTION); 
	}
	public boolean setCollection(Collection value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_COLLECTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_COLLECTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_COLLECTION))
				ok = false;
		return ok;
	}
	public List<Element> getElement()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Element>(factory, rObject, factory.GRAPHDIAGRAM_ELEMENT); 
	}
	public boolean setElement(Element value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_ELEMENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_ELEMENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_ELEMENT))
				ok = false;
		return ok;
	}
	public List<Command> getCommand()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Command>(factory, rObject, factory.GRAPHDIAGRAM_COMMAND); 
	}
	public boolean setCommand(Command value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_COMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_COMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_COMMAND))
				ok = false;
		return ok;
	}
	public List<ActivateDgrEvent> getActivateDgrEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<ActivateDgrEvent>(factory, rObject, factory.GRAPHDIAGRAM_ACTIVATEDGREVENT); 
	}
	public boolean setActivateDgrEvent(ActivateDgrEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_ACTIVATEDGREVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_ACTIVATEDGREVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_ACTIVATEDGREVENT))
				ok = false;
		return ok;
	}
	public List<Element> getSource()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Element>(factory, rObject, factory.GRAPHDIAGRAM_SOURCE); 
	}
	public boolean setSource(Element value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_SOURCE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_SOURCE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_SOURCE))
				ok = false;
		return ok;
	}
	public List<Element> getParent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Element>(factory, rObject, factory.GRAPHDIAGRAM_PARENT); 
	}
	public boolean setParent(Element value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_PARENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_PARENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_PARENT))
				ok = false;
		return ok;
	}
	public List<Frame> getFrame()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Frame>(factory, rObject, factory.GRAPHDIAGRAM_FRAME); 
	}
	public boolean setFrame(Frame value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.GRAPHDIAGRAM_FRAME);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.GRAPHDIAGRAM_FRAME))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.GRAPHDIAGRAM_FRAME))
				ok = false;
		return ok;
	}
}
