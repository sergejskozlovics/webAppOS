// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class PopUpElemSelectEvent
	extends Event
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
				System.err.println("Unable to delete the object "+rObject+" of type PopUpElemSelectEvent since the RAAPI wrapper does not take care of this reference.");
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
	PopUpElemSelectEvent(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.POPUPELEMSELECTEVENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.POPUPELEMSELECTEVENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public PopUpElemSelectEvent(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends PopUpElemSelectEvent> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends PopUpElemSelectEvent> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<PopUpElemSelectEvent> retVal = new ArrayList<PopUpElemSelectEvent>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.POPUPELEMSELECTEVENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			PopUpElemSelectEvent o = (PopUpElemSelectEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (PopUpElemSelectEvent)factory.findOrCreateRAAPIReferenceWrapper(PopUpElemSelectEvent.class, r, true);
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
		for (PopUpElemSelectEvent o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static PopUpElemSelectEvent firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static PopUpElemSelectEvent firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.POPUPELEMSELECTEVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			PopUpElemSelectEvent  retVal = (PopUpElemSelectEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (PopUpElemSelectEvent)factory.findOrCreateRAAPIReferenceWrapper(PopUpElemSelectEvent.class, r, true);
			return retVal;
		}
	} 
 
	public List<PopUpElement> getPopUpElement()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<PopUpElement>(factory, rObject, factory.POPUPELEMSELECTEVENT_POPUPELEMENT); 
	}
	public boolean setPopUpElement(PopUpElement value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.POPUPELEMSELECTEVENT_POPUPELEMENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.POPUPELEMSELECTEVENT_POPUPELEMENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.POPUPELEMSELECTEVENT_POPUPELEMENT))
				ok = false;
		return ok;
	}
}
