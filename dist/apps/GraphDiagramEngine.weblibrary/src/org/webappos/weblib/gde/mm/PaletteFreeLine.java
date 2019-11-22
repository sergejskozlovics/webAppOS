// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class PaletteFreeLine
	extends PaletteElement
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
				System.err.println("Unable to delete the object "+rObject+" of type PaletteFreeLine since the RAAPI wrapper does not take care of this reference.");
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
	PaletteFreeLine(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.PALETTEFREELINE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.PALETTEFREELINE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public PaletteFreeLine(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends PaletteFreeLine> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends PaletteFreeLine> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<PaletteFreeLine> retVal = new ArrayList<PaletteFreeLine>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PALETTEFREELINE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			PaletteFreeLine o = (PaletteFreeLine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (PaletteFreeLine)factory.findOrCreateRAAPIReferenceWrapper(PaletteFreeLine.class, r, true);
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
		for (PaletteFreeLine o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static PaletteFreeLine firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static PaletteFreeLine firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PALETTEFREELINE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			PaletteFreeLine  retVal = (PaletteFreeLine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (PaletteFreeLine)factory.findOrCreateRAAPIReferenceWrapper(PaletteFreeLine.class, r, true);
			return retVal;
		}
	} 
 
	public List<NewFreeLineEvent> getNewFreeLineEvent()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<NewFreeLineEvent>(factory, rObject, factory.PALETTEFREELINE_NEWFREELINEEVENT); 
	}
	public boolean setNewFreeLineEvent(NewFreeLineEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PALETTEFREELINE_NEWFREELINEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PALETTEFREELINE_NEWFREELINEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PALETTEFREELINE_NEWFREELINEEVENT))
				ok = false;
		return ok;
	}
}
