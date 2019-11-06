// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class InterDirectedLink
  	implements RAAPIReferenceWrapper
{
	protected TDAKernelMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::InterDirectedLink since the RAAPI wrapper does not take care of this reference.");
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
	InterDirectedLink(TDAKernelMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.INTERDIRECTEDLINK);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public InterDirectedLink(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends InterDirectedLink> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends InterDirectedLink> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<InterDirectedLink> retVal = new ArrayList<InterDirectedLink>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.INTERDIRECTEDLINK);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			InterDirectedLink o = (InterDirectedLink)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (InterDirectedLink)factory.findOrCreateRAAPIReferenceWrapper(InterDirectedLink.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(TDAKernelMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(TDAKernelMetamodelFactory factory)
	{
		for (InterDirectedLink o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static InterDirectedLink firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static InterDirectedLink firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.INTERDIRECTEDLINK);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			InterDirectedLink  retVal = (InterDirectedLink)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (InterDirectedLink)factory.findOrCreateRAAPIReferenceWrapper(InterDirectedLink.class, r, true);
			return retVal;
		}
	} 
 
	public ProxyReference getSourceObject()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_SOURCEOBJECT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			ProxyReference retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(ProxyReference.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setSourceObject(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_SOURCEOBJECT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERDIRECTEDLINK_SOURCEOBJECT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERDIRECTEDLINK_SOURCEOBJECT))
				ok = false;
		return ok;
	}
	public ProxyReference getTargetObject()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_TARGETOBJECT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			ProxyReference retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(ProxyReference.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setTargetObject(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_TARGETOBJECT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERDIRECTEDLINK_TARGETOBJECT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERDIRECTEDLINK_TARGETOBJECT))
				ok = false;
		return ok;
	}
	public InterDirectedLink getInverse()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_INVERSE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			InterDirectedLink retVal = (InterDirectedLink)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (InterDirectedLink)factory.findOrCreateRAAPIReferenceWrapper(InterDirectedLink.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setInverse(InterDirectedLink value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_INVERSE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERDIRECTEDLINK_INVERSE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERDIRECTEDLINK_INVERSE))
				ok = false;
		return ok;
	}
	public InterDirectedLink getInterDirectedLink()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_INTERDIRECTEDLINK);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			InterDirectedLink retVal = (InterDirectedLink)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (InterDirectedLink)factory.findOrCreateRAAPIReferenceWrapper(InterDirectedLink.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setInterDirectedLink(InterDirectedLink value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_INTERDIRECTEDLINK);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERDIRECTEDLINK_INTERDIRECTEDLINK))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERDIRECTEDLINK_INTERDIRECTEDLINK))
				ok = false;
		return ok;
	}
	public ProxyReference getAssociation()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_ASSOCIATION);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			ProxyReference retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(ProxyReference.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setAssociation(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_ASSOCIATION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERDIRECTEDLINK_ASSOCIATION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERDIRECTEDLINK_ASSOCIATION))
				ok = false;
		return ok;
	}
	public ProxyReference getPreviousTargetObject()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			ProxyReference retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(ProxyReference.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setPreviousTargetObject(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT))
				ok = false;
		return ok;
	}
}
