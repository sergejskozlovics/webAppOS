// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class PasteGraphClipboardEvent
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
				System.err.println("Unable to delete the object "+rObject+" of type PasteGraphClipboardEvent since the RAAPI wrapper does not take care of this reference.");
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
	PasteGraphClipboardEvent(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.PASTEGRAPHCLIPBOARDEVENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.PASTEGRAPHCLIPBOARDEVENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public PasteGraphClipboardEvent(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends PasteGraphClipboardEvent> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends PasteGraphClipboardEvent> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<PasteGraphClipboardEvent> retVal = new ArrayList<PasteGraphClipboardEvent>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PASTEGRAPHCLIPBOARDEVENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			PasteGraphClipboardEvent o = (PasteGraphClipboardEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (PasteGraphClipboardEvent)factory.findOrCreateRAAPIReferenceWrapper(PasteGraphClipboardEvent.class, r, true);
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
		for (PasteGraphClipboardEvent o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static PasteGraphClipboardEvent firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static PasteGraphClipboardEvent firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PASTEGRAPHCLIPBOARDEVENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			PasteGraphClipboardEvent  retVal = (PasteGraphClipboardEvent)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (PasteGraphClipboardEvent)factory.findOrCreateRAAPIReferenceWrapper(PasteGraphClipboardEvent.class, r, true);
			return retVal;
		}
	} 
 
}
