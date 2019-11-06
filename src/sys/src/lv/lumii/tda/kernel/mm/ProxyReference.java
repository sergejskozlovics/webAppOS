// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class ProxyReference
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::ProxyReference since the RAAPI wrapper does not take care of this reference.");
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
	ProxyReference(TDAKernelMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.PROXYREFERENCE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public ProxyReference(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends ProxyReference> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends ProxyReference> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<ProxyReference> retVal = new ArrayList<ProxyReference>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PROXYREFERENCE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			ProxyReference o = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(ProxyReference.class, r, true);
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
		for (ProxyReference o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static ProxyReference firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static ProxyReference firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PROXYREFERENCE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			ProxyReference  retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ProxyReference)factory.findOrCreateRAAPIReferenceWrapper(ProxyReference.class, r, true);
			return retVal;
		}
	} 
 
	public String getSerializedDomesticReference()
	{
		return factory.raapi.getAttributeValue(rObject, factory.PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE);
	}
	public boolean setSerializedDomesticReference(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE);
		return factory.raapi.setAttributeValue(rObject, factory.PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE, value.toString());
	}
	public List<InterDirectedLink> getOutgoingLink()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<InterDirectedLink>(factory, rObject, factory.PROXYREFERENCE_OUTGOINGLINK); 
	}
	public boolean setOutgoingLink(InterDirectedLink value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_OUTGOINGLINK);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_OUTGOINGLINK))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_OUTGOINGLINK))
				ok = false;
		return ok;
	}
	public List<InterDirectedLink> getIngoingLink()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<InterDirectedLink>(factory, rObject, factory.PROXYREFERENCE_INGOINGLINK); 
	}
	public boolean setIngoingLink(InterDirectedLink value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_INGOINGLINK);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_INGOINGLINK))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_INGOINGLINK))
				ok = false;
		return ok;
	}
	public List<InterDirectedLink> getInterDirectedLink()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<InterDirectedLink>(factory, rObject, factory.PROXYREFERENCE_INTERDIRECTEDLINK); 
	}
	public boolean setInterDirectedLink(InterDirectedLink value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_INTERDIRECTEDLINK);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_INTERDIRECTEDLINK))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_INTERDIRECTEDLINK))
				ok = false;
		return ok;
	}
	public List<InterAttributeValue> getInterAttributeValue()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<InterAttributeValue>(factory, rObject, factory.PROXYREFERENCE_INTERATTRIBUTEVALUE); 
	}
	public boolean setInterAttributeValue(InterAttributeValue value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_INTERATTRIBUTEVALUE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_INTERATTRIBUTEVALUE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_INTERATTRIBUTEVALUE))
				ok = false;
		return ok;
	}
	public List<InterAttributeValue> getValue()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<InterAttributeValue>(factory, rObject, factory.PROXYREFERENCE_VALUE); 
	}
	public boolean setValue(InterAttributeValue value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_VALUE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_VALUE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_VALUE))
				ok = false;
		return ok;
	}
	public List<ProxyReference> getInterSuperClass()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<ProxyReference>(factory, rObject, factory.PROXYREFERENCE_INTERSUPERCLASS); 
	}
	public boolean setInterSuperClass(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_INTERSUPERCLASS);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_INTERSUPERCLASS))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_INTERSUPERCLASS))
				ok = false;
		return ok;
	}
	public List<ProxyReference> getInterSubClass()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<ProxyReference>(factory, rObject, factory.PROXYREFERENCE_INTERSUBCLASS); 
	}
	public boolean setInterSubClass(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_INTERSUBCLASS);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_INTERSUBCLASS))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_INTERSUBCLASS))
				ok = false;
		return ok;
	}
	public List<InterDirectedAssociation> getOutgoingAssociation()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<InterDirectedAssociation>(factory, rObject, factory.PROXYREFERENCE_OUTGOINGASSOCIATION); 
	}
	public boolean setOutgoingAssociation(InterDirectedAssociation value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_OUTGOINGASSOCIATION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_OUTGOINGASSOCIATION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_OUTGOINGASSOCIATION))
				ok = false;
		return ok;
	}
	public List<InterDirectedAssociation> getIngoingAssociation()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<InterDirectedAssociation>(factory, rObject, factory.PROXYREFERENCE_INGOINGASSOCIATION); 
	}
	public boolean setIngoingAssociation(InterDirectedAssociation value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_INGOINGASSOCIATION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_INGOINGASSOCIATION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_INGOINGASSOCIATION))
				ok = false;
		return ok;
	}
	public Package getPackage()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_PACKAGE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Package retVal = (Package)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Package)factory.findOrCreateRAAPIReferenceWrapper(Package.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setPackage(Package value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_PACKAGE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_PACKAGE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_PACKAGE))
				ok = false;
		return ok;
	}
	public List<ProxyReference> getInterType()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<ProxyReference>(factory, rObject, factory.PROXYREFERENCE_INTERTYPE); 
	}
	public boolean setInterType(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_INTERTYPE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_INTERTYPE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_INTERTYPE))
				ok = false;
		return ok;
	}
	public List<ProxyReference> getInterInstance()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<ProxyReference>(factory, rObject, factory.PROXYREFERENCE_INTERINSTANCE); 
	}
	public boolean setInterInstance(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_INTERINSTANCE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_INTERINSTANCE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_INTERINSTANCE))
				ok = false;
		return ok;
	}
	public List<InterDirectedLink> getNextInterDirectedLink()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<InterDirectedLink>(factory, rObject, factory.PROXYREFERENCE_NEXTINTERDIRECTEDLINK); 
	}
	public boolean setNextInterDirectedLink(InterDirectedLink value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_NEXTINTERDIRECTEDLINK);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_NEXTINTERDIRECTEDLINK))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_NEXTINTERDIRECTEDLINK))
				ok = false;
		return ok;
	}
	public Repository getRepository()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_REPOSITORY);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Repository retVal = (Repository)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Repository)factory.findOrCreateRAAPIReferenceWrapper(Repository.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setRepository(Repository value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PROXYREFERENCE_REPOSITORY);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PROXYREFERENCE_REPOSITORY))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PROXYREFERENCE_REPOSITORY))
				ok = false;
		return ok;
	}
}
