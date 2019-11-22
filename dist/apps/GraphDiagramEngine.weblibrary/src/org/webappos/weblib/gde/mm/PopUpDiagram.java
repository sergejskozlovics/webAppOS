// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class PopUpDiagram
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
				System.err.println("Unable to delete the object "+rObject+" of type PopUpDiagram since the RAAPI wrapper does not take care of this reference.");
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
	PopUpDiagram(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.POPUPDIAGRAM), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.POPUPDIAGRAM);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public PopUpDiagram(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends PopUpDiagram> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends PopUpDiagram> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<PopUpDiagram> retVal = new ArrayList<PopUpDiagram>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.POPUPDIAGRAM);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			PopUpDiagram o = (PopUpDiagram)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (PopUpDiagram)factory.findOrCreateRAAPIReferenceWrapper(PopUpDiagram.class, r, true);
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
		for (PopUpDiagram o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static PopUpDiagram firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static PopUpDiagram firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.POPUPDIAGRAM);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			PopUpDiagram  retVal = (PopUpDiagram)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (PopUpDiagram)factory.findOrCreateRAAPIReferenceWrapper(PopUpDiagram.class, r, true);
			return retVal;
		}
	} 
 
	public List<PopUpCmd> getPopUpCmd()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<PopUpCmd>(factory, rObject, factory.POPUPDIAGRAM_POPUPCMD); 
	}
	public boolean setPopUpCmd(PopUpCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.POPUPDIAGRAM_POPUPCMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.POPUPDIAGRAM_POPUPCMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.POPUPDIAGRAM_POPUPCMD))
				ok = false;
		return ok;
	}
	public List<PopUpElement> getPopUpElement()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<PopUpElement>(factory, rObject, factory.POPUPDIAGRAM_POPUPELEMENT); 
	}
	public boolean setPopUpElement(PopUpElement value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.POPUPDIAGRAM_POPUPELEMENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.POPUPDIAGRAM_POPUPELEMENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.POPUPDIAGRAM_POPUPELEMENT))
				ok = false;
		return ok;
	}
}
