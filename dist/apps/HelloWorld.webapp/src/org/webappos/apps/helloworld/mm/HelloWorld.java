// automatically generated
package org.webappos.apps.helloworld.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class HelloWorld
  	implements RAAPIReferenceWrapper
{
	protected HelloWorldMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type HelloWorld since the RAAPI wrapper does not take care of this reference.");
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
	HelloWorld(HelloWorldMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.HELLOWORLD);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public HelloWorld(HelloWorldMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends HelloWorld> allObjects()
	{
		return allObjects(HelloWorldMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends HelloWorld> allObjects(HelloWorldMetamodelFactory factory)
	{
		ArrayList<HelloWorld> retVal = new ArrayList<HelloWorld>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.HELLOWORLD);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			HelloWorld o = (HelloWorld)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (HelloWorld)factory.findOrCreateRAAPIReferenceWrapper(HelloWorld.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(HelloWorldMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(HelloWorldMetamodelFactory factory)
	{
		for (HelloWorld o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static HelloWorld firstObject()
	{
		return firstObject(HelloWorldMetamodelFactory.eINSTANCE);
	} 

	public static HelloWorld firstObject(HelloWorldMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.HELLOWORLD);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			HelloWorld  retVal = (HelloWorld)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (HelloWorld)factory.findOrCreateRAAPIReferenceWrapper(HelloWorld.class, r, true);
			return retVal;
		}
	} 
 
	public String getMessage()
	{
		return factory.raapi.getAttributeValue(rObject, factory.HELLOWORLD_MESSAGE);
	}
	public boolean setMessage(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.HELLOWORLD_MESSAGE);
		return factory.raapi.setAttributeValue(rObject, factory.HELLOWORLD_MESSAGE, value.toString());
	}
}
