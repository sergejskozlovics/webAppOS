// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class ToolbarElement
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
				System.err.println("Unable to delete the object "+rObject+" of type ToolbarElement since the RAAPI wrapper does not take care of this reference.");
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
	ToolbarElement(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.TOOLBARELEMENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.TOOLBARELEMENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public ToolbarElement(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends ToolbarElement> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends ToolbarElement> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<ToolbarElement> retVal = new ArrayList<ToolbarElement>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.TOOLBARELEMENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			ToolbarElement o = (ToolbarElement)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (ToolbarElement)factory.findOrCreateRAAPIReferenceWrapper(ToolbarElement.class, r, true);
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
		for (ToolbarElement o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static ToolbarElement firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static ToolbarElement firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.TOOLBARELEMENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			ToolbarElement  retVal = (ToolbarElement)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ToolbarElement)factory.findOrCreateRAAPIReferenceWrapper(ToolbarElement.class, r, true);
			return retVal;
		}
	} 
 
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.TOOLBARELEMENT_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.TOOLBARELEMENT_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.TOOLBARELEMENT_CAPTION, value.toString());
	}
	public String getPicture()
	{
		return factory.raapi.getAttributeValue(rObject, factory.TOOLBARELEMENT_PICTURE);
	}
	public boolean setPicture(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.TOOLBARELEMENT_PICTURE);
		return factory.raapi.setAttributeValue(rObject, factory.TOOLBARELEMENT_PICTURE, value.toString());
	}
	public String getProcedureName()
	{
		return factory.raapi.getAttributeValue(rObject, factory.TOOLBARELEMENT_PROCEDURENAME);
	}
	public boolean setProcedureName(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.TOOLBARELEMENT_PROCEDURENAME);
		return factory.raapi.setAttributeValue(rObject, factory.TOOLBARELEMENT_PROCEDURENAME, value.toString());
	}
	public Toolbar getToolbar()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TOOLBARELEMENT_TOOLBAR);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Toolbar retVal = (Toolbar)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Toolbar)factory.findOrCreateRAAPIReferenceWrapper(Toolbar.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setToolbar(Toolbar value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TOOLBARELEMENT_TOOLBAR);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.TOOLBARELEMENT_TOOLBAR))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.TOOLBARELEMENT_TOOLBAR))
				ok = false;
		return ok;
	}
	public List<ToolbarElementSelectEvent> getToolbarElementSelectEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<ToolbarElementSelectEvent>(factory, rObject, factory.TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT); 
	}
	public boolean setToolbarElementSelectEvent(ToolbarElementSelectEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT))
				ok = false;
		return ok;
	}
}
