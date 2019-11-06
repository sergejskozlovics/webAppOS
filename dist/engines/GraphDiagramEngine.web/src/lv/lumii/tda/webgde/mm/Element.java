// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Element
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
				System.err.println("Unable to delete the object "+rObject+" of type Element since the RAAPI wrapper does not take care of this reference.");
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
	Element(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.ELEMENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.ELEMENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public Element(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends Element> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Element> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<Element> retVal = new ArrayList<Element>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ELEMENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Element o = (Element)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Element)factory.findOrCreateRAAPIReferenceWrapper(Element.class, r, true);
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
		for (Element o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Element firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Element firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ELEMENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Element  retVal = (Element)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Element)factory.findOrCreateRAAPIReferenceWrapper(Element.class, r, true);
			return retVal;
		}
	} 
 
	public String getStyle()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ELEMENT_STYLE);
	}
	public boolean setStyle(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMENT_STYLE);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMENT_STYLE, value.toString());
	}
	public String getLocation()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ELEMENT_LOCATION);
	}
	public boolean setLocation(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ELEMENT_LOCATION);
		return factory.raapi.setAttributeValue(rObject, factory.ELEMENT_LOCATION, value.toString());
	}
	public GraphDiagram getGraphDiagram()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_GRAPHDIAGRAM);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			GraphDiagram retVal = (GraphDiagram)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (GraphDiagram)factory.findOrCreateRAAPIReferenceWrapper(GraphDiagram.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setGraphDiagram(GraphDiagram value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_GRAPHDIAGRAM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_GRAPHDIAGRAM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_GRAPHDIAGRAM))
				ok = false;
		return ok;
	}
	public Collection getCollection()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_COLLECTION);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Collection retVal = (Collection)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Collection)factory.findOrCreateRAAPIReferenceWrapper(Collection.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setCollection(Collection value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_COLLECTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_COLLECTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_COLLECTION))
				ok = false;
		return ok;
	}
	public List<ElemStyle> getElemStyle()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<ElemStyle>(factory, rObject, factory.ELEMENT_ELEMSTYLE); 
	}
	public boolean setElemStyle(ElemStyle value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_ELEMSTYLE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_ELEMSTYLE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_ELEMSTYLE))
				ok = false;
		return ok;
	}
	public List<OkCmd> getOkCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<OkCmd>(factory, rObject, factory.ELEMENT_OKCMD); 
	}
	public boolean setOkCmd(OkCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_OKCMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_OKCMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_OKCMD))
				ok = false;
		return ok;
	}
	public List<DefaultStyleCmd> getDefaultStyleCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<DefaultStyleCmd>(factory, rObject, factory.ELEMENT_DEFAULTSTYLECMD); 
	}
	public boolean setDefaultStyleCmd(DefaultStyleCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_DEFAULTSTYLECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_DEFAULTSTYLECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_DEFAULTSTYLECMD))
				ok = false;
		return ok;
	}
	public List<PasteCmd> getPasteCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<PasteCmd>(factory, rObject, factory.ELEMENT_PASTECMD); 
	}
	public boolean setPasteCmd(PasteCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_PASTECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_PASTECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_PASTECMD))
				ok = false;
		return ok;
	}
	public List<ActiveElementCmd> getActiveElementCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<ActiveElementCmd>(factory, rObject, factory.ELEMENT_ACTIVEELEMENTCMD); 
	}
	public boolean setActiveElementCmd(ActiveElementCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_ACTIVEELEMENTCMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_ACTIVEELEMENTCMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_ACTIVEELEMENTCMD))
				ok = false;
		return ok;
	}
	public List<StyleDialogCmd> getStyleDialogCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<StyleDialogCmd>(factory, rObject, factory.ELEMENT_STYLEDIALOGCMD); 
	}
	public boolean setStyleDialogCmd(StyleDialogCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_STYLEDIALOGCMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_STYLEDIALOGCMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_STYLEDIALOGCMD))
				ok = false;
		return ok;
	}
	public List<RerouteCmd> getRerouteCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<RerouteCmd>(factory, rObject, factory.ELEMENT_REROUTECMD); 
	}
	public boolean setRerouteCmd(RerouteCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_REROUTECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_REROUTECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_REROUTECMD))
				ok = false;
		return ok;
	}
	public List<AlignSizeCmd> getAlignSizeCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<AlignSizeCmd>(factory, rObject, factory.ELEMENT_ALIGNSIZECMD); 
	}
	public boolean setAlignSizeCmd(AlignSizeCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_ALIGNSIZECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_ALIGNSIZECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_ALIGNSIZECMD))
				ok = false;
		return ok;
	}
	public List<L2ClickEvent> getL2ClickEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<L2ClickEvent>(factory, rObject, factory.ELEMENT_L2CLICKEVENT); 
	}
	public boolean setL2ClickEvent(L2ClickEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_L2CLICKEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_L2CLICKEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_L2CLICKEVENT))
				ok = false;
		return ok;
	}
	public List<LClickEvent> getLClickEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<LClickEvent>(factory, rObject, factory.ELEMENT_LCLICKEVENT); 
	}
	public boolean setLClickEvent(LClickEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_LCLICKEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_LCLICKEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_LCLICKEVENT))
				ok = false;
		return ok;
	}
	public List<RClickEvent> getRClickEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<RClickEvent>(factory, rObject, factory.ELEMENT_RCLICKEVENT); 
	}
	public boolean setRClickEvent(RClickEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_RCLICKEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_RCLICKEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_RCLICKEVENT))
				ok = false;
		return ok;
	}
	public List<NewLineEvent> getNewLineEventS()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<NewLineEvent>(factory, rObject, factory.ELEMENT_NEWLINEEVENTS); 
	}
	public boolean setNewLineEventS(NewLineEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_NEWLINEEVENTS);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_NEWLINEEVENTS))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_NEWLINEEVENTS))
				ok = false;
		return ok;
	}
	public List<NewLineEvent> getNewLineEventE()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<NewLineEvent>(factory, rObject, factory.ELEMENT_NEWLINEEVENTE); 
	}
	public boolean setNewLineEventE(NewLineEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_NEWLINEEVENTE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_NEWLINEEVENTE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_NEWLINEEVENTE))
				ok = false;
		return ok;
	}
	public List<MoveLineStartPointEvent> getMoveLineStartPointEventT()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<MoveLineStartPointEvent>(factory, rObject, factory.ELEMENT_MOVELINESTARTPOINTEVENTT); 
	}
	public boolean setMoveLineStartPointEventT(MoveLineStartPointEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_MOVELINESTARTPOINTEVENTT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_MOVELINESTARTPOINTEVENTT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_MOVELINESTARTPOINTEVENTT))
				ok = false;
		return ok;
	}
	public List<MoveLineEndPointEvent> getMoveLineEndPointEventT()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<MoveLineEndPointEvent>(factory, rObject, factory.ELEMENT_MOVELINEENDPOINTEVENTT); 
	}
	public boolean setMoveLineEndPointEventT(MoveLineEndPointEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_MOVELINEENDPOINTEVENTT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_MOVELINEENDPOINTEVENTT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_MOVELINEENDPOINTEVENTT))
				ok = false;
		return ok;
	}
	public List<FreeBoxEditedEvent> getFreeBoxEditedEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<FreeBoxEditedEvent>(factory, rObject, factory.ELEMENT_FREEBOXEDITEDEVENT); 
	}
	public boolean setFreeBoxEditedEvent(FreeBoxEditedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_FREEBOXEDITEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_FREEBOXEDITEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_FREEBOXEDITEDEVENT))
				ok = false;
		return ok;
	}
	public List<FreeLineEditedEvent> getFreeLineEditedEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<FreeLineEditedEvent>(factory, rObject, factory.ELEMENT_FREELINEEDITEDEVENT); 
	}
	public boolean setFreeLineEditedEvent(FreeLineEditedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_FREELINEEDITEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_FREELINEEDITEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_FREELINEEDITEDEVENT))
				ok = false;
		return ok;
	}
	public List<Compartment> getCompartment()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Compartment>(factory, rObject, factory.ELEMENT_COMPARTMENT); 
	}
	public boolean setCompartment(Compartment value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_COMPARTMENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_COMPARTMENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_COMPARTMENT))
				ok = false;
		return ok;
	}
	public List<Edge> getEStart()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Edge>(factory, rObject, factory.ELEMENT_ESTART); 
	}
	public boolean setEStart(Edge value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_ESTART);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_ESTART))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_ESTART))
				ok = false;
		return ok;
	}
	public List<Edge> getEEnd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Edge>(factory, rObject, factory.ELEMENT_EEND); 
	}
	public boolean setEEnd(Edge value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_EEND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_EEND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_EEND))
				ok = false;
		return ok;
	}
	public List<GraphDiagram> getTarget()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<GraphDiagram>(factory, rObject, factory.ELEMENT_TARGET); 
	}
	public boolean setTarget(GraphDiagram value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_TARGET);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_TARGET))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_TARGET))
				ok = false;
		return ok;
	}
	public List<GraphDiagram> getChild()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<GraphDiagram>(factory, rObject, factory.ELEMENT_CHILD); 
	}
	public boolean setChild(GraphDiagram value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_CHILD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_CHILD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_CHILD))
				ok = false;
		return ok;
	}
	public List<UpdateStyleCmd> getUpdateStyleCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<UpdateStyleCmd>(factory, rObject, factory.ELEMENT_UPDATESTYLECMD); 
	}
	public boolean setUpdateStyleCmd(UpdateStyleCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ELEMENT_UPDATESTYLECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ELEMENT_UPDATESTYLECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ELEMENT_UPDATESTYLECMD))
				ok = false;
		return ok;
	}
}
