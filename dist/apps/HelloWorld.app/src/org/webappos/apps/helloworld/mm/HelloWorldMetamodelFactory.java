// automatically generated
package org.webappos.apps.helloworld.mm;
import lv.lumii.tda.raapi.RAAPI;
import java.util.*;

public class HelloWorldMetamodelFactory
{
	// for compatibility with ECore
	public static HelloWorldMetamodelFactory eINSTANCE = new HelloWorldMetamodelFactory();

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
			java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls1.getConstructor(HelloWorldMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
			return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);
		} catch (Throwable t1) {
			try {
				java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls.getConstructor(HelloWorldMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
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

		if (raapi.isKindOf(rObject, HELLOWORLD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(HELLOWORLD,rCurClass))) {
				retVal = HelloWorld.class;
				rCurClass = HELLOWORLD;
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
		if (rClass == HELLOWORLD)
			w = new HelloWorld(this, rObject, takeReference);
		if (w==null) {
		}
		if ((w != null) && takeReference)
			wrappers.put(rObject, w);
		return w;
	}

	public boolean deleteModel()
	{
		boolean ok = true;
		if (!HelloWorld.deleteAllObjects(this))
			ok = false;
		return ok; 
	}

	// RAAPI references:
	RAAPI raapi = null;
	public long HELLOWORLD = 0;
	  public long HELLOWORLD_MESSAGE = 0;

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
			if (HELLOWORLD != 0) {
				raapi.freeReference(HELLOWORLD);
				HELLOWORLD = 0;
			}
	  		if (HELLOWORLD_MESSAGE != 0) {
				raapi.freeReference(HELLOWORLD_MESSAGE);
				HELLOWORLD_MESSAGE = 0;
			}
		}

		raapi = _raapi;

		if (raapi != null) {
			// initializing class references...
			HELLOWORLD = raapi.findClass("HelloWorld");
			if ((HELLOWORLD == 0) && (prefix != null))
				HELLOWORLD = raapi.findClass(prefix+"HelloWorld");
			if ((HELLOWORLD == 0) && insertMetamodel)
				HELLOWORLD = raapi.createClass(prefix+"HelloWorld");
			if (HELLOWORLD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class HelloWorld.");
			}

			// creating generalizations, if they do not exist...
			if (insertMetamodel) {
			}

			// initializing references for attributes and associations...
			HELLOWORLD_MESSAGE = raapi.findAttribute(HELLOWORLD, "message");
			if ((HELLOWORLD_MESSAGE == 0) && insertMetamodel)
				HELLOWORLD_MESSAGE = raapi.createAttribute(HELLOWORLD, "message", raapi.findPrimitiveDataType("String"));
			if (HELLOWORLD_MESSAGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute message of the class HelloWorld.");
			}
		}
	}

	public HelloWorld createHelloWorld()
	{
		HelloWorld retVal = new HelloWorld(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
}
