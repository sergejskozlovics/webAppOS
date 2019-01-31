// automatically generated
package org.webappos.sample.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Counter
  	implements RAAPIReferenceWrapper
{
	protected SampleMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type Counter since the RAAPI wrapper does not take care of this reference.");
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
	Counter(SampleMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.COUNTER);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Counter(SampleMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Counter> allObjects()
	{
		return allObjects(SampleMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Counter> allObjects(SampleMetamodelFactory factory)
	{
		ArrayList<Counter> retVal = new ArrayList<Counter>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.COUNTER);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Counter o = (Counter)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Counter)factory.findOrCreateRAAPIReferenceWrapper(Counter.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(SampleMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(SampleMetamodelFactory factory)
	{
		for (Counter o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Counter firstObject()
	{
		return firstObject(SampleMetamodelFactory.eINSTANCE);
	} 

	public static Counter firstObject(SampleMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.COUNTER);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Counter  retVal = (Counter)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Counter)factory.findOrCreateRAAPIReferenceWrapper(Counter.class, r, true);
			return retVal;
		}
	} 
 
	public Integer getCount()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.COUNTER_COUNT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setCount(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COUNTER_COUNT);
		return factory.raapi.setAttributeValue(rObject, factory.COUNTER_COUNT, value.toString());
	}
}
