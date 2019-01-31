// automatically generated
package org.webappos.sample.mm;
import lv.lumii.tda.raapi.RAAPI;
import java.util.*;

public class SampleMetamodelFactory
{
	// for compatibility with ECore
	public static SampleMetamodelFactory eINSTANCE = new SampleMetamodelFactory();

	HashMap<Long, RAAPIReferenceWrapper> wrappers =
		new HashMap<Long, RAAPIReferenceWrapper>();

	public RAAPIReferenceWrapper findOrCreateRAAPIReferenceWrapper(Class<? extends RAAPIReferenceWrapper> cls, long rObject, boolean takeReference)
	// if takeReference==true, takes care about freeing rObject
	{
		RAAPIReferenceWrapper w = wrappers.get(rObject);
		if (w != null) {
			if (takeReference)
				raapi.freeReference(rObject);
			return w;
		}

		Class<? extends RAAPIReferenceWrapper> cls1 = findClosestType(rObject);
				
		try {
			java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls1.getConstructor(SampleMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
			return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);
		} catch (Throwable t1) {
			try {
				java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls.getConstructor(SampleMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
				return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);				
			} catch (Throwable t) {
				return null;
			}
		}

	}

	public Class<? extends RAAPIReferenceWrapper> findClosestType(long rObject)
	{
		Class<? extends RAAPIReferenceWrapper> retVal = null;
		long rCurClass = 0;

		if (raapi.isKindOf(rObject, COUNTER)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(COUNTER,rCurClass))) {
				retVal = Counter.class;
				rCurClass = COUNTER;
			}
		}

		return retVal; 
	}

	public RAAPIReferenceWrapper findOrCreateRAAPIReferenceWrapper(long rObject, boolean takeReference)
		// if takeReference==true, takes care about freeing rObject
	{
		RAAPIReferenceWrapper w = wrappers.get(rObject);
		if (w != null) {
			if (takeReference)
				raapi.freeReference(rObject);
			return w;
		}
		long it = raapi.getIteratorForDirectObjectClasses(rObject);
		if (it == 0)
			return null;		
		long rClass = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		if (rClass == 0)
			return null;
		if (rClass == COUNTER)
			w = new Counter(this, rObject, takeReference);
		if (w==null) {
		}
		if ((w != null) && takeReference)
			wrappers.put(rObject, w);
		return w;
	}

	public boolean deleteModel()
	{
		boolean ok = true;
		if (!Counter.deleteAllObjects(this))
			ok = false;
		return ok; 
	}

	// RAAPI references:
	RAAPI raapi = null;
	public long COUNTER = 0;
	  public long COUNTER_COUNT = 0;

	public class ElementReferenceException extends Exception
	{
		private static final long serialVersionUID = 1L;
		public ElementReferenceException(String msg)
		{
			super(msg);
		}
	}

	public void unsetRAAPI()
	{
		try {
			setRAAPI(null, null, false);
		}
		catch (Throwable t)
		{
		}
	}

	public RAAPI getRAAPI()
	{
		return raapi;
	}

	public void setRAAPI(RAAPI _raapi, String prefix, boolean insertMetamodel) throws ElementReferenceException // set RAAPI to null to free references
	{
		if (raapi != null) {
			// freeing object-level references...
			for (Long r : wrappers.keySet())
				raapi.freeReference(r);
			wrappers.clear();
			// freeing class-level references...
			if (COUNTER != 0) {
				raapi.freeReference(COUNTER);
				COUNTER = 0;
			}
	  		if (COUNTER_COUNT != 0) {
				raapi.freeReference(COUNTER_COUNT);
				COUNTER_COUNT = 0;
			}
		}

		raapi = _raapi;

		if (raapi != null) {
			// initializing class references...
			COUNTER = raapi.findClass("Counter");
			if ((COUNTER == 0) && (prefix != null))
				COUNTER = raapi.findClass(prefix+"Counter");
			if ((COUNTER == 0) && insertMetamodel)
				COUNTER = raapi.createClass(prefix+"Counter");
			if (COUNTER == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Counter.");
			}

			// creating generalizations, if they do not exist...
			if (insertMetamodel) {
			}

			// initializing references for attributes and associations...
			COUNTER_COUNT = raapi.findAttribute(COUNTER, "count");
			if ((COUNTER_COUNT == 0) && insertMetamodel)
				COUNTER_COUNT = raapi.createAttribute(COUNTER, "count", raapi.findPrimitiveDataType("Integer"));
			if (COUNTER_COUNT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute count of the class Counter.");
			}
		}
	}

	public Counter createCounter()
	{
		Counter retVal = new Counter(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
}
