// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Style
  	implements RAAPIReferenceWrapper
{
	protected GraphDiagramEngineMetamodelFactory factory;
	protected long rObject = 0;
	protected boolean takeReference;

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
				System.err.println("Unable to delete the object "+rObject+" of type Style since the RAAPI wrapper does not take care of this reference.");
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
	Style(GraphDiagramEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.STYLE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Style(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Style> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Style> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<Style> retVal = new ArrayList<Style>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.STYLE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Style o = (Style)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Style)factory.findOrCreateRAAPIReferenceWrapper(Style.class, r, true);
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
		for (Style o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Style firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Style firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.STYLE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Style  retVal = (Style)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Style)factory.findOrCreateRAAPIReferenceWrapper(Style.class, r, true);
			return retVal;
		}
	} 
 
}
