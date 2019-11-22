// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class SaveDgrCmd
	extends Command
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
				System.err.println("Unable to delete the object "+rObject+" of type SaveDgrCmd since the RAAPI wrapper does not take care of this reference.");
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
	SaveDgrCmd(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.SAVEDGRCMD), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.SAVEDGRCMD);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public SaveDgrCmd(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends SaveDgrCmd> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends SaveDgrCmd> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<SaveDgrCmd> retVal = new ArrayList<SaveDgrCmd>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.SAVEDGRCMD);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			SaveDgrCmd o = (SaveDgrCmd)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (SaveDgrCmd)factory.findOrCreateRAAPIReferenceWrapper(SaveDgrCmd.class, r, true);
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
		for (SaveDgrCmd o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static SaveDgrCmd firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static SaveDgrCmd firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.SAVEDGRCMD);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			SaveDgrCmd  retVal = (SaveDgrCmd)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (SaveDgrCmd)factory.findOrCreateRAAPIReferenceWrapper(SaveDgrCmd.class, r, true);
			return retVal;
		}
	} 
 
}
