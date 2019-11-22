// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class MoveLineEndPointEvent
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
				System.err.println("Unable to delete the object "+rObject+" of type MoveLineEndPointEvent since the RAAPI wrapper does not take care of this reference.");
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
	MoveLineEndPointEvent(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.MOVELINEENDPOINTEVENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.MOVELINEENDPOINTEVENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public MoveLineEndPointEvent(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends MoveLineEndPointEvent> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends MoveLineEndPointEvent> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<MoveLineEndPointEvent> retVal = new ArrayList<MoveLineEndPointEvent>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.MOVELINEENDPOINTEVENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			MoveLineEndPointEvent o = (MoveLineEndPointEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (MoveLineEndPointEvent)factory.findOrCreateRAAPIReferenceWrapper(MoveLineEndPointEvent.class, r, true);
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
		for (MoveLineEndPointEvent o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static MoveLineEndPointEvent firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static MoveLineEndPointEvent firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.MOVELINEENDPOINTEVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			MoveLineEndPointEvent  retVal = (MoveLineEndPointEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (MoveLineEndPointEvent)factory.findOrCreateRAAPIReferenceWrapper(MoveLineEndPointEvent.class, r, true);
			return retVal;
		}
	} 
 
	public List<Element> getTarget()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Element>(factory, rObject, factory.MOVELINEENDPOINTEVENT_TARGET); 
	}
	public boolean setTarget(Element value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.MOVELINEENDPOINTEVENT_TARGET);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.MOVELINEENDPOINTEVENT_TARGET))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.MOVELINEENDPOINTEVENT_TARGET))
				ok = false;
		return ok;
	}
	public List<Edge> getEdge()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Edge>(factory, rObject, factory.MOVELINEENDPOINTEVENT_EDGE); 
	}
	public boolean setEdge(Edge value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.MOVELINEENDPOINTEVENT_EDGE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.MOVELINEENDPOINTEVENT_EDGE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.MOVELINEENDPOINTEVENT_EDGE))
				ok = false;
		return ok;
	}
}
