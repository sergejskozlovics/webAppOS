// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Node
	extends Element
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
				System.err.println("Unable to delete the object "+rObject+" of type Node since the RAAPI wrapper does not take care of this reference.");
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
	Node(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.NODE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.NODE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public Node(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends Node> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Node> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<Node> retVal = new ArrayList<Node>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.NODE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Node o = (Node)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Node)factory.findOrCreateRAAPIReferenceWrapper(Node.class, r, true);
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
		for (Node o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Node firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Node firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.NODE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Node  retVal = (Node)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Node)factory.findOrCreateRAAPIReferenceWrapper(Node.class, r, true);
			return retVal;
		}
	} 
 
	public List<Node> getComponent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Node>(factory, rObject, factory.NODE_COMPONENT); 
	}
	public boolean setComponent(Node value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.NODE_COMPONENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.NODE_COMPONENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.NODE_COMPONENT))
				ok = false;
		return ok;
	}
	public Node getContainer()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.NODE_CONTAINER);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Node retVal = (Node)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Node)factory.findOrCreateRAAPIReferenceWrapper(Node.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setContainer(Node value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.NODE_CONTAINER);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.NODE_CONTAINER))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.NODE_CONTAINER))
				ok = false;
		return ok;
	}
	public List<Port> getPort()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Port>(factory, rObject, factory.NODE_PORT); 
	}
	public boolean setPort(Port value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.NODE_PORT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.NODE_PORT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.NODE_PORT))
				ok = false;
		return ok;
	}
	public List<NewBoxEvent> getNewBoxEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<NewBoxEvent>(factory, rObject, factory.NODE_NEWBOXEVENT); 
	}
	public boolean setNewBoxEvent(NewBoxEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.NODE_NEWBOXEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.NODE_NEWBOXEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.NODE_NEWBOXEVENT))
				ok = false;
		return ok;
	}
	public List<ChangeParentEvent> getChangeParentEventN()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<ChangeParentEvent>(factory, rObject, factory.NODE_CHANGEPARENTEVENTN); 
	}
	public boolean setChangeParentEventN(ChangeParentEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.NODE_CHANGEPARENTEVENTN);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.NODE_CHANGEPARENTEVENTN))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.NODE_CHANGEPARENTEVENTN))
				ok = false;
		return ok;
	}
	public List<ChangeParentEvent> getChangeParentEventT()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<ChangeParentEvent>(factory, rObject, factory.NODE_CHANGEPARENTEVENTT); 
	}
	public boolean setChangeParentEventT(ChangeParentEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.NODE_CHANGEPARENTEVENTT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.NODE_CHANGEPARENTEVENTT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.NODE_CHANGEPARENTEVENTT))
				ok = false;
		return ok;
	}
	public List<NewPinEvent> getNewPinEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<NewPinEvent>(factory, rObject, factory.NODE_NEWPINEVENT); 
	}
	public boolean setNewPinEvent(NewPinEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.NODE_NEWPINEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.NODE_NEWPINEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.NODE_NEWPINEVENT))
				ok = false;
		return ok;
	}
}
