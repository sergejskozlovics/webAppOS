// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_Group
  	implements RAAPIReferenceWrapper
{
	protected DialogEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_Group since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_Group(DialogEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_GROUP);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public D_SHARP_Group(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends D_SHARP_Group> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_Group> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_Group> retVal = new ArrayList<D_SHARP_Group>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_GROUP);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_Group o = (D_SHARP_Group)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_Group)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Group.class, r, true);
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
		for (D_SHARP_Group o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_Group firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_Group firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_GROUP);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_Group  retVal = (D_SHARP_Group)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Group)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Group.class, r, true);
			return retVal;
		}
	} 
 
	public List<D_SHARP_Component> getComponentWidths()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Component>(factory, rObject, factory.D_SHARP_GROUP_COMPONENTWIDTHS); 
	}
	public boolean setComponentWidths(D_SHARP_Component value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_GROUP_COMPONENTWIDTHS);
		// deleting previous links...
		if (it != 0) {
			ArrayList<Long> list = new ArrayList<Long>();
			long r = factory.raapi.resolveIteratorFirst(it);
			while (r != 0) {
				list.add(r);
				r = factory.raapi.resolveIteratorNext(it);
			}
			factory.raapi.freeIterator(it);
			for (Long rLinked : list)
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_GROUP_COMPONENTWIDTHS))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_GROUP_COMPONENTWIDTHS))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Component> getComponentHeights()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Component>(factory, rObject, factory.D_SHARP_GROUP_COMPONENTHEIGHTS); 
	}
	public boolean setComponentHeights(D_SHARP_Component value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_GROUP_COMPONENTHEIGHTS);
		// deleting previous links...
		if (it != 0) {
			ArrayList<Long> list = new ArrayList<Long>();
			long r = factory.raapi.resolveIteratorFirst(it);
			while (r != 0) {
				list.add(r);
				r = factory.raapi.resolveIteratorNext(it);
			}
			factory.raapi.freeIterator(it);
			for (Long rLinked : list)
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_GROUP_COMPONENTHEIGHTS))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_GROUP_COMPONENTHEIGHTS))
				ok = false;
		return ok;
	}
	public D_SHARP_Component getOwner()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_GROUP_OWNER);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_Component retVal = (D_SHARP_Component)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Component)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Component.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setOwner(D_SHARP_Component value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_GROUP_OWNER);
		// deleting previous links...
		if (it != 0) {
			ArrayList<Long> list = new ArrayList<Long>();
			long r = factory.raapi.resolveIteratorFirst(it);
			while (r != 0) {
				list.add(r);
				r = factory.raapi.resolveIteratorNext(it);
			}
			factory.raapi.freeIterator(it);
			for (Long rLinked : list)
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_GROUP_OWNER))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_GROUP_OWNER))
				ok = false;
		return ok;
	}
}
