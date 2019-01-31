// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Package
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::Package since the RAAPI wrapper does not take care of this reference.");
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
	Package(TDAKernelMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.PACKAGE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Package(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Package> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Package> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<Package> retVal = new ArrayList<Package>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PACKAGE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Package o = (Package)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Package)factory.findOrCreateRAAPIReferenceWrapper(Package.class, r, true);
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
		for (Package o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Package firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Package firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PACKAGE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Package  retVal = (Package)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Package)factory.findOrCreateRAAPIReferenceWrapper(Package.class, r, true);
			return retVal;
		}
	} 
 
	public Repository getMountedRepository()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_MOUNTEDREPOSITORY);
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
	public boolean setMountedRepository(Repository value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_MOUNTEDREPOSITORY);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PACKAGE_MOUNTEDREPOSITORY))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PACKAGE_MOUNTEDREPOSITORY))
				ok = false;
		return ok;
	}
	public Package getParent()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_PARENT);
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
	public boolean setParent(Package value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_PARENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PACKAGE_PARENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PACKAGE_PARENT))
				ok = false;
		return ok;
	}
	public List<Package> getChild()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<Package>(factory, rObject, factory.PACKAGE_CHILD); 
	}
	public boolean setChild(Package value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_CHILD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PACKAGE_CHILD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PACKAGE_CHILD))
				ok = false;
		return ok;
	}
	public Repository getAssociatedRepository()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_ASSOCIATEDREPOSITORY);
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
	public boolean setAssociatedRepository(Repository value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_ASSOCIATEDREPOSITORY);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PACKAGE_ASSOCIATEDREPOSITORY))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PACKAGE_ASSOCIATEDREPOSITORY))
				ok = false;
		return ok;
	}
	public TDAKernel getKernel()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_KERNEL);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			TDAKernel retVal = (TDAKernel)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (TDAKernel)factory.findOrCreateRAAPIReferenceWrapper(TDAKernel.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setKernel(TDAKernel value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_KERNEL);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PACKAGE_KERNEL))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PACKAGE_KERNEL))
				ok = false;
		return ok;
	}
	public List<ProxyReference> getProxyReference()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<ProxyReference>(factory, rObject, factory.PACKAGE_PROXYREFERENCE); 
	}
	public boolean setProxyReference(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_PROXYREFERENCE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PACKAGE_PROXYREFERENCE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PACKAGE_PROXYREFERENCE))
				ok = false;
		return ok;
	}
	public String getSimpleName()
	{
		return factory.raapi.getAttributeValue(rObject, factory.PACKAGE_SIMPLENAME);
	}
	public boolean setSimpleName(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.PACKAGE_SIMPLENAME);
		return factory.raapi.setAttributeValue(rObject, factory.PACKAGE_SIMPLENAME, value.toString());
	}
	public List<Repository> getStackedRepository()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<Repository>(factory, rObject, factory.PACKAGE_STACKEDREPOSITORY); 
	}
	public boolean setStackedRepository(Repository value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PACKAGE_STACKEDREPOSITORY);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PACKAGE_STACKEDREPOSITORY))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PACKAGE_STACKEDREPOSITORY))
				ok = false;
		return ok;
	}
}
