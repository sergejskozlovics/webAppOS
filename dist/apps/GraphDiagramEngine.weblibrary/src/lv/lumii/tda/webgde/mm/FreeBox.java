// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class FreeBox
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
				System.err.println("Unable to delete the object "+rObject+" of type FreeBox since the RAAPI wrapper does not take care of this reference.");
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
	FreeBox(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.FREEBOX), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.FREEBOX);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public FreeBox(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends FreeBox> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends FreeBox> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<FreeBox> retVal = new ArrayList<FreeBox>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FREEBOX);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			FreeBox o = (FreeBox)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (FreeBox)factory.findOrCreateRAAPIReferenceWrapper(FreeBox.class, r, true);
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
		for (FreeBox o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static FreeBox firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static FreeBox firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.FREEBOX);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			FreeBox  retVal = (FreeBox)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (FreeBox)factory.findOrCreateRAAPIReferenceWrapper(FreeBox.class, r, true);
			return retVal;
		}
	} 
 
	public Integer getFreeBox_x()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREEBOX_FREEBOX_X);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeBox_x(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREEBOX_FREEBOX_X);
		return factory.raapi.setAttributeValue(rObject, factory.FREEBOX_FREEBOX_X, value.toString());
	}
	public Integer getFreeBox_y()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREEBOX_FREEBOX_Y);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeBox_y(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREEBOX_FREEBOX_Y);
		return factory.raapi.setAttributeValue(rObject, factory.FREEBOX_FREEBOX_Y, value.toString());
	}
	public Integer getFreeBox_w()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREEBOX_FREEBOX_W);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeBox_w(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREEBOX_FREEBOX_W);
		return factory.raapi.setAttributeValue(rObject, factory.FREEBOX_FREEBOX_W, value.toString());
	}
	public Integer getFreeBox_h()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREEBOX_FREEBOX_H);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeBox_h(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREEBOX_FREEBOX_H);
		return factory.raapi.setAttributeValue(rObject, factory.FREEBOX_FREEBOX_H, value.toString());
	}
	public Integer getFreeBox_z()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.FREEBOX_FREEBOX_Z);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setFreeBox_z(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.FREEBOX_FREEBOX_Z);
		return factory.raapi.setAttributeValue(rObject, factory.FREEBOX_FREEBOX_Z, value.toString());
	}
}
