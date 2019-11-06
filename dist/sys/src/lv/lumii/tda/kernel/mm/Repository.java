// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Repository
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::Repository since the RAAPI wrapper does not take care of this reference.");
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
	Repository(TDAKernelMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.REPOSITORY);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Repository(TDAKernelMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Repository> allObjects()
	{
		return allObjects(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Repository> allObjects(TDAKernelMetamodelFactory factory)
	{
		ArrayList<Repository> retVal = new ArrayList<Repository>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.REPOSITORY);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Repository o = (Repository)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Repository)factory.findOrCreateRAAPIReferenceWrapper(Repository.class, r, true);
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
		for (Repository o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Repository firstObject()
	{
		return firstObject(TDAKernelMetamodelFactory.eINSTANCE);
	} 

	public static Repository firstObject(TDAKernelMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.REPOSITORY);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Repository  retVal = (Repository)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Repository)factory.findOrCreateRAAPIReferenceWrapper(Repository.class, r, true);
			return retVal;
		}
	} 
 
	public List<Package> getMountPoint()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<Package>(factory, rObject, factory.REPOSITORY_MOUNTPOINT); 
	}
	public boolean setMountPoint(Package value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.REPOSITORY_MOUNTPOINT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.REPOSITORY_MOUNTPOINT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.REPOSITORY_MOUNTPOINT))
				ok = false;
		return ok;
	}
	public List<Package> getPackage()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<Package>(factory, rObject, factory.REPOSITORY_PACKAGE); 
	}
	public boolean setPackage(Package value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.REPOSITORY_PACKAGE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.REPOSITORY_PACKAGE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.REPOSITORY_PACKAGE))
				ok = false;
		return ok;
	}
	public String getUri()
	{
		return factory.raapi.getAttributeValue(rObject, factory.REPOSITORY_URI);
	}
	public boolean setUri(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.REPOSITORY_URI);
		return factory.raapi.setAttributeValue(rObject, factory.REPOSITORY_URI, value.toString());
	}
	public List<ProxyReference> getProxyReference()
	{
		return new TDAKernelMetamodel_RAAPILinkedObjectsList<ProxyReference>(factory, rObject, factory.REPOSITORY_PROXYREFERENCE); 
	}
	public boolean setProxyReference(ProxyReference value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.REPOSITORY_PROXYREFERENCE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.REPOSITORY_PROXYREFERENCE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.REPOSITORY_PROXYREFERENCE))
				ok = false;
		return ok;
	}
	public Package getCoveredPackage()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.REPOSITORY_COVEREDPACKAGE);
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
	public boolean setCoveredPackage(Package value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.REPOSITORY_COVEREDPACKAGE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.REPOSITORY_COVEREDPACKAGE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.REPOSITORY_COVEREDPACKAGE))
				ok = false;
		return ok;
	}
}
