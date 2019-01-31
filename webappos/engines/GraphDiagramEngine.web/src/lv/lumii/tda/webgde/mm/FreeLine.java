// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class FreeLine
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
				System.err.println("Unable to delete the object "+rObject+" of type FreeLine since the RAAPI wrapper does not take care of this reference.");
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
	FreeLine(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.FREELINE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.FREELINE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public FreeLine(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends FreeLine> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends FreeLine> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<FreeLine> retVal = new ArrayList<FreeLine>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FREELINE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			FreeLine o = (FreeLine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (FreeLine)factory.findOrCreateRAAPIReferenceWrapper(FreeLine.class, r, true);
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
		for (FreeLine o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static FreeLine firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static FreeLine firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FREELINE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			FreeLine  retVal = (FreeLine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (FreeLine)factory.findOrCreateRAAPIReferenceWrapper(FreeLine.class, r, true);
			return retVal;
		}
	} 
 
	public Integer getFreeLine_x1()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREELINE_FREELINE_X1);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeLine_x1(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREELINE_FREELINE_X1);
		return factory.raapi.setAttributeValue(rObject, factory.FREELINE_FREELINE_X1, value.toString());
	}
	public Integer getFreeLine_y1()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREELINE_FREELINE_Y1);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeLine_y1(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREELINE_FREELINE_Y1);
		return factory.raapi.setAttributeValue(rObject, factory.FREELINE_FREELINE_Y1, value.toString());
	}
	public Integer getFreeLine_xn()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREELINE_FREELINE_XN);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeLine_xn(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREELINE_FREELINE_XN);
		return factory.raapi.setAttributeValue(rObject, factory.FREELINE_FREELINE_XN, value.toString());
	}
	public Integer getFreeLine_yn()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREELINE_FREELINE_YN);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeLine_yn(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREELINE_FREELINE_YN);
		return factory.raapi.setAttributeValue(rObject, factory.FREELINE_FREELINE_YN, value.toString());
	}
	public Integer getFreeLine_z()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREELINE_FREELINE_Z);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeLine_z(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREELINE_FREELINE_Z);
		return factory.raapi.setAttributeValue(rObject, factory.FREELINE_FREELINE_Z, value.toString());
	}
}
