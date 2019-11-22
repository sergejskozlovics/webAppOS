// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_ProgressBar
	extends D_SHARP_Component
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected DialogEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_ProgressBar since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_ProgressBar(DialogEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.D_SHARP_PROGRESSBAR), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_PROGRESSBAR);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public D_SHARP_ProgressBar(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends D_SHARP_ProgressBar> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_ProgressBar> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_ProgressBar> retVal = new ArrayList<D_SHARP_ProgressBar>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_PROGRESSBAR);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_ProgressBar o = (D_SHARP_ProgressBar)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_ProgressBar)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_ProgressBar.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(DialogEngineMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(DialogEngineMetamodelFactory factory)
	{
		for (D_SHARP_ProgressBar o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_ProgressBar firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_ProgressBar firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_PROGRESSBAR);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_ProgressBar  retVal = (D_SHARP_ProgressBar)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_ProgressBar)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_ProgressBar.class, r, true);
			return retVal;
		}
	} 
 
	public Integer getPosition()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_PROGRESSBAR_POSITION);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPosition(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_PROGRESSBAR_POSITION);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_PROGRESSBAR_POSITION, value.toString());
	}
	public Integer getMinimumValue()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_PROGRESSBAR_MINIMUMVALUE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMinimumValue(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_PROGRESSBAR_MINIMUMVALUE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_PROGRESSBAR_MINIMUMVALUE, value.toString());
	}
	public Integer getMaximumValue()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_PROGRESSBAR_MAXIMUMVALUE);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMaximumValue(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_PROGRESSBAR_MAXIMUMVALUE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_PROGRESSBAR_MAXIMUMVALUE, value.toString());
	}
}
