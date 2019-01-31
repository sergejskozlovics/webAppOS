// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class PopUpElement
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
				System.err.println("Unable to delete the object "+rObject+" of type PopUpElement since the RAAPI wrapper does not take care of this reference.");
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
	PopUpElement(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.POPUPELEMENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.POPUPELEMENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public PopUpElement(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends PopUpElement> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends PopUpElement> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<PopUpElement> retVal = new ArrayList<PopUpElement>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.POPUPELEMENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			PopUpElement o = (PopUpElement)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (PopUpElement)factory.findOrCreateRAAPIReferenceWrapper(PopUpElement.class, r, true);
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
		for (PopUpElement o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static PopUpElement firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static PopUpElement firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.POPUPELEMENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			PopUpElement  retVal = (PopUpElement)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (PopUpElement)factory.findOrCreateRAAPIReferenceWrapper(PopUpElement.class, r, true);
			return retVal;
		}
	} 
 
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.POPUPELEMENT_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.POPUPELEMENT_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.POPUPELEMENT_CAPTION, value.toString());
	}
	public String getProcedureName()
	{
		return factory.raapi.getAttributeValue(rObject, factory.POPUPELEMENT_PROCEDURENAME);
	}
	public boolean setProcedureName(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.POPUPELEMENT_PROCEDURENAME);
		return factory.raapi.setAttributeValue(rObject, factory.POPUPELEMENT_PROCEDURENAME, value.toString());
	}
	public PopUpDiagram getPopUpDiagram()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.POPUPELEMENT_POPUPDIAGRAM);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			PopUpDiagram retVal = (PopUpDiagram)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (PopUpDiagram)factory.findOrCreateRAAPIReferenceWrapper(PopUpDiagram.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setPopUpDiagram(PopUpDiagram value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.POPUPELEMENT_POPUPDIAGRAM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.POPUPELEMENT_POPUPDIAGRAM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.POPUPELEMENT_POPUPDIAGRAM))
				ok = false;
		return ok;
	}
	public List<PopUpElemSelectEvent> getPopUpElemSelectEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<PopUpElemSelectEvent>(factory, rObject, factory.POPUPELEMENT_POPUPELEMSELECTEVENT); 
	}
	public boolean setPopUpElemSelectEvent(PopUpElemSelectEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.POPUPELEMENT_POPUPELEMSELECTEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.POPUPELEMENT_POPUPELEMSELECTEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.POPUPELEMENT_POPUPELEMSELECTEVENT))
				ok = false;
		return ok;
	}
}
