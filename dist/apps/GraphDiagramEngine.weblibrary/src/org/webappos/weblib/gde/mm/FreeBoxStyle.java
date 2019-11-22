// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class FreeBoxStyle
	extends NodeStyle
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
				System.err.println("Unable to delete the object "+rObject+" of type FreeBoxStyle since the RAAPI wrapper does not take care of this reference.");
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
	FreeBoxStyle(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.FREEBOXSTYLE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.FREEBOXSTYLE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public FreeBoxStyle(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends FreeBoxStyle> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends FreeBoxStyle> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<FreeBoxStyle> retVal = new ArrayList<FreeBoxStyle>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FREEBOXSTYLE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			FreeBoxStyle o = (FreeBoxStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (FreeBoxStyle)factory.findOrCreateRAAPIReferenceWrapper(FreeBoxStyle.class, r, true);
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
		for (FreeBoxStyle o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static FreeBoxStyle firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static FreeBoxStyle firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FREEBOXSTYLE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			FreeBoxStyle  retVal = (FreeBoxStyle)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (FreeBoxStyle)factory.findOrCreateRAAPIReferenceWrapper(FreeBoxStyle.class, r, true);
			return retVal;
		}
	} 
 
}
