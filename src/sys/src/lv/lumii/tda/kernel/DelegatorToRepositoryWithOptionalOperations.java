package lv.lumii.tda.kernel;

import java.util.*;

import javax.swing.JOptionPane;

import lv.lumii.tda.raapi.*;

// DelegatorToRepositoryActions performs the following functions:
//	 * implements unimplemented optional RAAPI functions (including All/Direct-functions);
//   * hides classes and association ends, whose names start with "." (UNIX-style hiding);

public class DelegatorToRepositoryWithOptionalOperations extends DelegatorToRepositoryBase implements IRepository {

	class PeculiarIterator
	{
		private ArrayList<Long> list = new ArrayList<Long>();
		private int i = 0;
		private long it;
		PeculiarIterator(long _it)
		{
			this.it = _it;
		}
		void add(long r)
		{
			/*if (DelegatorToRepositoryWithOptionalOperations.this.isClass(r)) {
				String name = delegate.getClassName(r);
				if ((name != null) && ( name.startsWith(".") || name.contains("::.") )) {
					delegate.freeReference(r);
					return;
				}
			}
			if (DelegatorToRepositoryWithOptionalOperations.this.isAssociationEnd(r)) {
				String name = delegate.getRoleName(r);
				if ((name != null) && name.startsWith(".")) {
					delegate.freeReference(r);
					return;
				}
			}
			if (DelegatorToRepositoryWithOptionalOperations.this.isAttribute(r)) {
				String name = delegate.getAttributeName(r);
				if ((name != null) && name.startsWith(".")) {
					delegate.freeReference(r);
					return;
				}
			}*/
			
			list.add(r);
		}
		long resolveFirst()
		{
			i = 0;
			return resolveNext();
		}
		long resolveNext()
		{
			if (i < list.size())
				return list.get(i++);
			else
				return 0;
		}		
		long resolve(int _i)
		{
			if (_i >= list.size()) {
				return 0;
			}
			Long retVal = list.get(_i);
			this.i = _i+1;
			return retVal==null?0:retVal;
		}
		int getLength()
		{
			return list.size();
		}
		void free()
		{
			for (Long l : list) {
				delegate.freeReference(l);		
			}
			delegate.freeIterator(it);
		}
	}

	Map<Long, PeculiarIterator> peculiarIterators =
		new HashMap<Long, PeculiarIterator>();
			// When an iterator reference is passed to us,
			// we check whether this iterator is a key in peculiarIterators.
			// If yes, we use the corresponding peculiar iterator.
			// Otherwise, we pass the call to the delegator delegate
			// to handle the iterator as an ordinary TDA iterator.
	

	private long peculiarize(long it)
	// creates a new PeculiarIterator and ADDS elements to it
	{
		if (it==0)
			return 0;
		PeculiarIterator pi = new PeculiarIterator(it);
		long r = delegate.resolveIteratorFirst(it);
		while (r != 0) {
			pi.add(r);
			r = delegate.resolveIteratorNext(it);
		}
		peculiarIterators.put(it, pi);
		return it;
	}
	
	private long peculiarize(long it, PeculiarIterator pi)
	// uses an existing PeculiarIterator; DOES NOT ADD elements to it
	{
		peculiarIterators.put(it, pi);
		return it;
	}

	private long getPeculiarIteratorForIngoingAssociationEnds(long rTargetClass, boolean all)
	{
		long itCls = this.getIteratorForClasses();
		if (itCls == 0)
			return 0;
		
		PeculiarIterator pi = new PeculiarIterator(itCls);
		
		long rCls = this.resolveIteratorFirst(itCls);
		while (rCls != 0) {
			
			
			long itAssoc;
			if (all)
				itAssoc = this.getIteratorForAllOutgoingAssociationEnds(rCls);
			else
				itAssoc = this.getIteratorForDirectOutgoingAssociationEnds(rCls);
			if (itAssoc != 0) {
				long rAssoc = this.resolveIteratorFirst(itAssoc);
				while (rAssoc != 0) {
					if (this.getTargetClass(rAssoc)==rTargetClass)
						pi.add(rAssoc);
					else
						this.freeReference(rAssoc);
					rAssoc = this.resolveIteratorNext(itAssoc);
				}
				this.freeIterator(itAssoc);
			}
			
			this.freeReference(rCls);
			rCls = this.resolveIteratorNext(itCls);
		}
		
		peculiarIterators.put(itCls, pi);
		return itCls;
	}

	public DelegatorToRepositoryWithOptionalOperations(IRepository _delegate) {
		super();
		setDelegate(_delegate);
	}

	// nullify() should be called, when the delegate is closed;
	// then other modules using can continue to use this RepositoryActionsWrapper,
	// but they will get the false/null/0 value for delegate actions;	
	public void nullify() 
	{
		delegate = null;
	}
	
	private boolean strOK(String name)
	{
		return (name!=null) && (name.length()>0);
	}

	///// Operations on primitive data types /////
	@Override
	public long findPrimitiveDataType (String name)
	{
		if ((delegate != null) && strOK(name))
			return delegate.findPrimitiveDataType(name);
		else
			return 0;
	}	
	@Override
	public String getPrimitiveDataTypeName(long r)
	{
		if ((delegate != null) && (r != 0))
			return delegate.getPrimitiveDataTypeName(r);
		else
			return null;
	}
	@Override
	public boolean isPrimitiveDataType(long r)
	{
		if ((delegate != null) && (r != 0)) 
			return delegate.isPrimitiveDataType(r);
		else
			return false;
	}

	//
	  ///// Operations on classes /////
	@Override
	public long createClass(String name) {
		if (delegate != null) {
			long r = name==null?0:this.findClass(name);
			if (r != 0) {
				this.freeReference(r);
				return 0; // a class with the given name already exists
			}
			return delegate.createClass(name);
		}
		else
			return 0;
	}
	@Override
	public long findClass(String name) {
		if (delegate == null)
			return 0;
		try {
			return delegate.findClass(name);
		}
		catch (UnsupportedOperationException e) {
			long it = this.getIteratorForClasses();
			if (it != 0) {
				long r = this.resolveIteratorFirst(it);
				while (r != 0) {
					if (this.getClassName(r).equals(name)) {
						this.freeIterator(it); // we do not need the iterator anymore
						return r; // do not free this reference, but return it
					}

					this.freeReference(r);
					r = this.resolveIteratorNext(it);
				}
				this.freeIterator(it);
			}
			return 0;
		}
	}
	@Override
	public String getClassName(long r)
	{
		if ((delegate != null) && (r != 0))
			return delegate.getClassName(r);
		else
			return null;
	}
	@Override
	public boolean deleteClass(long r)
	{
		if ((delegate != null) && (r!=0))
			return delegate.deleteClass(r);
		else
			return false;
	}
	@Override
	public long getIteratorForClasses() {
		if (delegate != null) {
			return peculiarize(delegate.getIteratorForClasses());
		}
		else
			return 0;
	}
	@Override
	public boolean createGeneralization(long rSubClass, long rSuperClass)
	{
		if ((delegate != null) && (rSubClass!=0) && (rSuperClass!=0)) {
			return delegate.createGeneralization(rSubClass, rSuperClass);
		}
		else
			return false;
	}
	@Override
	public boolean deleteGeneralization(long rSubClass, long rSuperClass)
	{
		if ((delegate != null) && (rSubClass!=0) && (rSuperClass!=0)) {
			return delegate.deleteGeneralization(rSubClass, rSuperClass);
		}
		else
			return false;
	}
	@Override
	public long getIteratorForDirectSuperClasses(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.getIteratorForDirectSuperClasses(param0);
		else
			return 0;
	}
	@Override
	public long getIteratorForDirectSubClasses(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.getIteratorForDirectSubClasses(param0);
		else
			return 0;
	}
	@Override
	public boolean isClass(long param0)
	{
		if ((delegate != null) && (param0!=0))
			try {
				return delegate.isClass(param0);
			}
		catch(UnsupportedOperationException e) {
			if (param0 == 0)
				return false;
			String name = delegate.getClassName(param0);
			if (name == null)
				return false;
			long r = delegate.findClass(name);
			if (r == 0)
				return false;
			delegate.freeReference(r);
			return (r == param0);
		}
		else
			return false;
	}
	@Override
	public boolean isDirectSubClass (long rSubClass, long rSuperClass)
	{
		if ((delegate != null)&&(rSubClass != 0)&&(rSuperClass != 0)) {
			try {
				return delegate.isDirectSubClass(rSubClass, rSuperClass);
			}
			catch(UnsupportedOperationException e)
			{
				long it = this.getIteratorForDirectSubClasses(rSuperClass);
				if (it == 0)
					return false;
				long rCls = this.resolveIteratorFirst(it);
				while (rCls != 0) {
					if (rCls == rSubClass) {
						this.freeReference(rCls);
						this.freeIterator(it);
						return true;
					}
					
					this.freeReference(rCls);
					this.resolveIteratorNext(it);
				}
				this.freeIterator(it);
				return false;
			}
		}
		else
			return false;
	}
	private boolean searchForSuperClassRecursively(long rStartClass, long rWantedClass, Set<Long> superClassesSoFar)
	{
		// referenced added to superClassesSoFar must then be freed;
		// we use superClassesSoFar in order not to process the same superclass twice;
		
		long it = this.getIteratorForDirectSuperClasses(rStartClass);
		if (it == 0)
			return false;
		long rSuperCls = this.resolveIteratorFirst(it);
		while (rSuperCls != 0) {
			if (rSuperCls == rWantedClass) {
				this.freeIterator(it);
				this.freeReference(rSuperCls);
				return true;
			}
			
			if (!superClassesSoFar.contains(rSuperCls)) {
				superClassesSoFar.add(rSuperCls);
				// recursive depth-first search...
				if (searchForSuperClassRecursively(rSuperCls, rWantedClass, superClassesSoFar)) {
					this.freeIterator(it);
					return true;
				}
			}
			else {
				// this rSuperCls has already been processed
				this.freeReference(rSuperCls);
			}
			
			rSuperCls = this.resolveIteratorNext(it);
		}
		
		this.freeIterator(it);
		return false;
	}	
	@Override
	public boolean isDerivedClass (long rDirectlyOrIndirectlyDerivedClass, long rSuperClass)
	{
		if ((delegate != null)&&(rDirectlyOrIndirectlyDerivedClass != 0)&&(rSuperClass != 0)&&(rDirectlyOrIndirectlyDerivedClass!=rSuperClass)) {
			try {
				return delegate.isDerivedClass(rDirectlyOrIndirectlyDerivedClass, rSuperClass);
			}
			catch(UnsupportedOperationException e)
			{
				Set<Long> referencesSoFar = new HashSet<Long>();
				
				boolean result = searchForSuperClassRecursively(rDirectlyOrIndirectlyDerivedClass, rSuperClass, referencesSoFar);
				
				// freeing referencesSoFar...
				for (Long r: referencesSoFar)
					this.freeReference(r.longValue());
				
				return result;			
			}
		}
		else
			return false;
	}

	  ///// Operations on objects /////
	@Override
	public long createObject(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.createObject(param0);
		else
			return 0;
	}
	@Override
	public boolean deleteObject(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.deleteObject(param0);
		else
			return false;
	}
	@Override
	public boolean includeObjectInClass(long rObject, long rClass) {
		if ((delegate == null)&&(rObject!=0)&&(rClass!=0))
			try {
				return delegate.includeObjectInClass(rObject, rClass);
			}
			catch (UnsupportedOperationException e) {
				return false; // TODO: implement in the kernel proxy references layer
			}
		else
			return false; 
	}
	@Override
	public boolean excludeObjectFromClass(long rObject, long rClass) {
		if ((delegate == null)&&(rObject!=0)&&(rClass!=0))
			try {
				return delegate.excludeObjectFromClass(rObject, rClass);
			}
			catch (UnsupportedOperationException e) {
				return false; // TODO: implement in the kernel proxy references layer
			}
		else
			return false; 
	}	
	@Override
	public boolean moveObject (long rObject, long rToClass)
	{
		// all the links and attributes have already been cascade deleted
		if ((delegate == null)&&(rObject!=0)&&(rToClass!=0))
			try {
				return delegate.moveObject(rObject, rToClass);
			}
			catch (UnsupportedOperationException e) {
				//return false; // TODO: implement in the kernel proxy references layer
				throw e;
			}
		else
			return false; 
	}
	@Override
	public long getIteratorForAllClassObjects(long rClass) {
		if ((delegate != null)&&(rClass!=0)) {			
			try {
				return delegate.getIteratorForAllClassObjects(rClass);
			}
			catch (UnsupportedOperationException e) {
				long it = delegate.getIteratorForDirectClassObjects(rClass);
				if (it == 0)
					return 0;

				// we will implement getIteratorForClassObjects via getIteratorForDirectClassObjects

				java.util.LinkedHashSet<Long> objs = new java.util.LinkedHashSet<Long>();

				// getting direct objects...
				long rObj = delegate.resolveIteratorFirst(it);
				while (rObj != 0) {
					if (objs.contains(rObj))
						delegate.freeReference(rObj);
					else
						objs.add(rObj);
					rObj = delegate.resolveIteratorNext(it);
				}

				// getting subclasses and their objects
				long subClsIt = this.getIteratorForDirectSubClasses(rClass); // recursive
				if (subClsIt == 0) {
					for (Long _rObj : objs) {
						delegate.freeReference(_rObj);
					}
					delegate.freeIterator(it);
					return 0;
				}

				long subCls = this.resolveIteratorFirst(subClsIt);
				while (subCls != 0) {
					// processing the next subclass...
					long subObjIt = this.getIteratorForAllClassObjects(subCls); // recursive
					if (subObjIt == 0) {
						for (Long _rObj : objs)
							delegate.freeReference(_rObj); // freeing already found references
						this.freeReference(subCls);
						this.freeIterator(subClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long subObj = this.resolveIteratorFirst(subObjIt);
					while (subObj != 0) {
						// processing the next super attribute...
						if (objs.contains(subObj))
							this.freeReference(subObj);
						else
							objs.add(subObj);
						subObj = this.resolveIteratorNext(subObjIt);
					}
					this.freeIterator(subObjIt);

					this.freeReference(subCls);
					subCls = this.resolveIteratorNext(subClsIt);
				}
				this.freeIterator(subClsIt);				
				
				PeculiarIterator pi = new PeculiarIterator(it);
				for (Long l : objs)
					pi.add(l);
				return peculiarize(it, pi);
			}
		}
		else
			return 0;
	}
	@Override
	public long getIteratorForDirectClassObjects(long rClass) {
		if ((delegate != null)&&(rClass!=0)) {			
			try {
				return delegate.getIteratorForDirectClassObjects(rClass);
			}
			catch (UnsupportedOperationException e) {
				long it = delegate.getIteratorForAllClassObjects(rClass);
				if (it == 0)
					return 0;

				// we will implement getIteratorForDirectClassObject via getIteratorForClassObjects
				java.util.LinkedHashSet<Long> objs = new java.util.LinkedHashSet<Long>();

				// getting all objects...
				long rObj = delegate.resolveIteratorFirst(it);
				while (rObj != 0) {
					objs.add(rObj);
					rObj = delegate.resolveIteratorNext(it);
				}

				// excluding objects of sub classes...
				long subClsIt = delegate.getIteratorForDirectSubClasses(rClass);
				if (subClsIt == 0) {
					for (Long _rObj : objs)
						delegate.freeReference(_rObj); // freeing found references
					delegate.freeIterator(it);
					return 0;
				}

				long subCls = delegate.resolveIteratorFirst(subClsIt);
				while (subCls != 0) {
					// processing the next subclass...
					long subObjIt = this.getIteratorForAllClassObjects(subCls); // recursive
					if (subObjIt == 0) {
						for (Long _rObj : objs)
							delegate.freeReference(_rObj); // freeing already found references
						delegate.freeReference(subCls);
						delegate.freeIterator(subClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long subObj = delegate.resolveIteratorFirst(subObjIt);
					while (subObj != 0) {
						// processing the next super attribute...
						if (objs.contains(subObj)) {
							delegate.freeReference(subObj);
							objs.remove(subObj);
						}
						delegate.freeReference(subObj);
						subObj = delegate.resolveIteratorNext(subObjIt);
					}
					delegate.freeIterator(subObjIt);

					delegate.freeReference(subCls);
					subCls = delegate.resolveIteratorNext(subClsIt);
				}
				delegate.freeIterator(subClsIt);
				
				PeculiarIterator pi = new PeculiarIterator(it);
				for (Long l : objs)
					pi.add(l);
				return peculiarize(it, pi);
			}
		}
		else
			return 0;
	}
	@Override
	public long getIteratorForDirectObjectClasses(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.getIteratorForDirectObjectClasses(param0);
		else
			return 0;
	}
	@Override
	public boolean isTypeOf(long rObject, long rClass)
	{		
		if (delegate != null) {
			try {
				return delegate.isTypeOf(rObject, rClass);
			}
			catch (UnsupportedOperationException e) {
				long it = this.getIteratorForDirectObjectClasses(rObject);
				if (it == 0)
					return false;
				
				long cls2 = this.resolveIteratorFirst(it);
				while (cls2 != 0) {
					this.freeReference(cls2);
					
					if (cls2 == rClass) {
						this.freeIterator(it);
						return true;
					}
					
					cls2 = this.resolveIteratorNext(it);
				}
				this.freeIterator(it);
				
				return false;				
			}
		}
		else
			return false;
	}
	@Override
	public boolean isKindOf(long rObject, long rClass)
	{
		if (delegate != null) {
			try {
				return delegate.isKindOf(rObject, rClass);
			}
			catch (UnsupportedOperationException e) {
				long it = this.getIteratorForDirectObjectClasses(rObject);
				if (it == 0)
					return false;
				long rObjectType = this.resolveIteratorFirst(it);
				while (rObjectType != 0) {
					if ((rObjectType == rClass) || (isDerivedClass(rObjectType, rClass))) {
						this.freeReference(rObjectType);
						this.freeIterator(it);
						return true;
					}
					
					this.freeReference(rObjectType);
					rObjectType = this.resolveIteratorNext(it);
				}
				this.freeIterator(it);
				
				return false;
			}
		}
		else
			return false;
	}

	  ///// Operations on attributes /////
	@Override
	public long createAttribute(long rClass, String name, long type) {
		if (delegate != null) {
			long r = this.findAttribute(rClass, name);
			if (r != 0) {
				this.freeReference(r);
				return 0; // an attribute with the given name already exists
			}
			
			return delegate.createAttribute(rClass, name, type);
		}
		else
			return 0;
	}
	@Override
	public long findAttribute(long rClass, String name) {
		if ((delegate != null)&&(rClass != 0))
			try {
				return delegate.findAttribute(rClass, name);
			}
			catch (UnsupportedOperationException e) {		
				long it = this.getIteratorForAllAttributes(rClass);
				if (it != 0) {
					long r = this.resolveIteratorFirst(it);
					while (r != 0) {
						if (this.getAttributeName(r).equals(name)) {
							this.freeIterator(it); // we do not need the iterator anymore
							return r; // do not free this reference, but return it
						}
	
						this.freeReference(r);
						r = this.resolveIteratorNext(it);
					}
					this.freeIterator(it);
				}
				return 0;
			}
		else
			return 0;
	}
	@Override
	public boolean deleteAttribute(long rAttr)
	{
		if ((delegate != null)&&(rAttr!=0)) {
			return delegate.deleteAttribute(rAttr);
		}
		else
			return false;
	}
	@Override
	public long getIteratorForAllAttributes(long rClass) {
		if ((delegate != null)&&(rClass!=0)) {
			try {
				return delegate.getIteratorForAllAttributes(rClass);
			}
			catch (UnsupportedOperationException e) {		
				long it = delegate.getIteratorForDirectAttributes(rClass);
				if (it == 0)
					return 0;

				// we will implement getIteratorForAttributes via getIteratorForDirectAttributes

				// getting superclasses and their attributes
				java.util.LinkedHashSet<Long> attrs = new java.util.LinkedHashSet<Long>();
				long superClsIt = this.getIteratorForDirectSuperClasses(rClass); // recursive
				if (superClsIt == 0) {
					delegate.freeIterator(it);
					return 0;
				}

				long superCls = this.resolveIteratorFirst(superClsIt);
				while (superCls != 0) {
					// processing the next super class...
					long superAttrIt = this.getIteratorForAllAttributes(superCls); // recursive
					if (superAttrIt == 0) {
						for (Long attr : attrs)
							this.freeReference(attr); // freeing already found references
						this.freeReference(superCls);
						this.freeIterator(superClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long superAttr = this.resolveIteratorFirst(superAttrIt);
					while (superAttr != 0) {
						// processing the next super attribute...
						if (attrs.contains(superAttr))
							this.freeReference(superAttr);
						else
							attrs.add(superAttr);
						superAttr = this.resolveIteratorNext(superAttrIt);
					}
					this.freeIterator(superAttrIt);

					this.freeReference(superCls);
					superCls = this.resolveIteratorNext(superClsIt);
				}
				this.freeIterator(superClsIt);

				// now, getting direct attributes...
				long rAttr = delegate.resolveIteratorFirst(it);
				while (rAttr != 0) {
					if (attrs.contains(rAttr))
						delegate.freeReference(rAttr);
					else
						attrs.add(rAttr);
					rAttr = delegate.resolveIteratorNext(it);
				}

				PeculiarIterator pi = new PeculiarIterator(it);
				for (Long a : attrs)
					pi.add(a);
				return peculiarize(it, pi);
			}
		}
		else
			return 0;
	}
	@Override
	public long getIteratorForDirectAttributes(long rClass) {
		if ((delegate != null) && (rClass!=0)) {
			try {
				return delegate.getIteratorForDirectAttributes(rClass);
			}
			catch (UnsupportedOperationException e) {		
				long it = delegate.getIteratorForAllAttributes(rClass);
				if (it == 0)
					return 0;

				// we will implement getIteratorForDirectAttributes via getIteratorForAttributes
				java.util.LinkedHashSet<Long> attrs = new java.util.LinkedHashSet<Long>();

				// getting all attributes...
				long rAttr = delegate.resolveIteratorFirst(it);
				while (rAttr != 0) {
					attrs.add(rAttr);
					rAttr = delegate.resolveIteratorNext(it);
				}

				// excluding attributes of super classes...
				long superClsIt = delegate.getIteratorForDirectSuperClasses(rClass);
				if (superClsIt == 0) {
					for (Long attr : attrs)
						delegate.freeReference(attr); // freeing found references
					delegate.freeIterator(it);
					return 0;
				}

				long superCls = delegate.resolveIteratorFirst(superClsIt);
				while (superCls != 0) {
					// processing the next super class...
					long superAttrIt = this.getIteratorForAllAttributes(superCls); // recursive
					if (superAttrIt == 0) {
						for (Long attr : attrs)
							delegate.freeReference(attr); // freeing already found references
						delegate.freeReference(superCls);
						delegate.freeIterator(superClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long superAttr = this.resolveIteratorFirst(superAttrIt);
					while (superAttr != 0) {
						// processing the next super attribute...
						if (attrs.contains(superAttr)) {
							this.freeReference(superAttr);
							attrs.remove(superAttr);
						}
						this.freeReference(superAttr);
						superAttr = this.resolveIteratorNext(superAttrIt);
					}
					this.freeIterator(superAttrIt);

					delegate.freeReference(superCls);
					superCls = delegate.resolveIteratorNext(superClsIt);
				}
				delegate.freeIterator(superClsIt);
				
				PeculiarIterator pi = new PeculiarIterator(it);
				for (Long a : attrs)
					pi.add(a);
				return peculiarize(it, pi);
			}			
		}
		else
			return 0;
	}
	@Override
	public String getAttributeName(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.getAttributeName(param0);
		else
			return null;
	}
	@Override
	public long getAttributeDomain(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.getAttributeDomain(param0);
		else
			return 0;
	}
	@Override
	public long getAttributeType(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.getAttributeType(param0);
		else
			return 0;
	}
	@Override
	public boolean isAttribute(long param0)
	{
		if (delegate != null) {
			try {
				return delegate.isAttribute(param0);
			}
			catch (UnsupportedOperationException e) {
				if (param0 == 0)
					return false;
				long rCls = delegate.getAttributeDomain(param0);
				if (rCls == 0)
					return false;
				String name = delegate.getAttributeName(param0);
				if (name == null) {
					delegate.freeReference(rCls);
					return false;
				}
				long rAttr = delegate.findAttribute(rCls, name);
				if (rAttr != 0)
					delegate.freeReference(rAttr);
				delegate.freeReference(rCls);
				return (rAttr == param0);
			}
		}
		else
			return false;
	}

	///// Operations on attribute values /////
	@Override
	public boolean setAttributeValue(long rObject, long rAttribute, String value) {		
		if ((delegate != null)&&(rObject!=0)&&(rAttribute!=0)) {
			if (value == null)
				return delegate.deleteAttributeValue(rObject, rAttribute);
			else
				return delegate.setAttributeValue(rObject, rAttribute, value);
		}
		else
			return false;
	}
	@Override
	public String getAttributeValue(long param0, long param1)
	{
		if ((delegate != null)&&(param0!=0)&&(param1!=0))
			return delegate.getAttributeValue(param0, param1);
		else
			return null;
	}
	@Override
	public boolean deleteAttributeValue(long param0, long param1)
	{
		if ((delegate != null)&&(param0!=0)&&(param1!=0))
			return delegate.deleteAttributeValue(param0, param1);
		else
			return false;
	}
	@Override
	public long getIteratorForObjectsByAttributeValue(long param0, String param1) // TODO
	{
		if (delegate != null)
			return delegate.getIteratorForObjectsByAttributeValue(param0, param1);
		else
			return 0;
	}

	///// Operations on associations /////
	@Override
	public long createAssociation (long rSourceClass, long rTargetClass, String sourceRoleName, String targetRoleName, boolean isComposition)
	{
		if ((delegate != null) && (rSourceClass!=0) && (rTargetClass!=0)) {
			long r = this.findAssociationEnd(rSourceClass, targetRoleName);
			if (r != 0) {
				this.freeReference(r);
				return 0; // the target role already exists
			}			
			r = this.findAssociationEnd(rTargetClass, sourceRoleName);
			if (r != 0) {
				this.freeReference(r);
				return 0; // the source role already exists
			}			

			return delegate.createAssociation(rSourceClass, rTargetClass, sourceRoleName,
					targetRoleName, isComposition);
		}
		else
			return 0;
	}	
	@Override
	public long createDirectedAssociation (long rSourceClass, long rTargetClass, String targetRoleName, boolean isComposition)
	{
		if (delegate != null) {
			long r = delegate.findAssociationEnd(rSourceClass, targetRoleName);
			if (r != 0) {
				delegate.freeReference(r);
				return 0; // the target role already exists
			}
			try {
				return delegate.createDirectedAssociation(rSourceClass, rTargetClass,
					targetRoleName, isComposition);
			}
			catch(UnsupportedOperationException e)
			{
				if (targetRoleName == null)
					throw new TDAKernelInconsistencyException("createDirectedAssociation: Could not simulate directed association since the target role name is null.");
				String generatedSourceRole = this.getClassName(rSourceClass);
				if (generatedSourceRole == null)
					throw new TDAKernelInconsistencyException("createDirectedAssociation: Could not get class name for reference "+rSourceClass);
				generatedSourceRole = ".inverseFor"+generatedSourceRole+"."+targetRoleName;
				return this.createAssociation(rSourceClass, rTargetClass,
					generatedSourceRole, targetRoleName, isComposition);
			}
		}
		else
			return 0;
	}
	@Override
	public long createAdvancedAssociation(String param0, boolean param1, boolean param2)
	{
		if (delegate != null)
			try {
				return delegate.createAdvancedAssociation(param0, param1, param2);
			}
			catch(UnsupportedOperationException e)
			{
				return 0;
			}
		else
			return 0;
	}
	@Override
	public long findAssociationEnd(long rSourceClass, String targetRoleName) {
		if ((delegate != null) && (rSourceClass!=0) && (targetRoleName != null))
			try {
				return delegate.findAssociationEnd(rSourceClass, targetRoleName);
			}
			catch(UnsupportedOperationException e)
			{
				long it = this.getIteratorForAllOutgoingAssociationEnds(rSourceClass);
				if (it != 0) {
					long r = this.resolveIteratorFirst(it);
					while (r != 0) {
						if (targetRoleName.equals(this.getRoleName(r))) {
							this.freeIterator(it); // we do not need the iterator anymore
							return r; // do not free this reference, but return it
						}
		
						this.freeReference(r);
						r = this.resolveIteratorNext(it);
					}
					this.freeIterator(it);
				}
				return 0;
			}
		else
			return 0;
	}
	@Override
	public boolean deleteAssociation(long rAssoc)
	{
		if ((delegate != null)&&(rAssoc!=0)) {
			return delegate.deleteAssociation(rAssoc);
		}
		else
			return false;
	}

	@Override
	public long getIteratorForAllOutgoingAssociationEnds (long rClass)
	{
		if ((delegate != null)&&(rClass!=0)) {
			try {	
				long it = delegate.getIteratorForAllOutgoingAssociationEnds(rClass);
				PeculiarIterator pi = new PeculiarIterator(it);
				long r = delegate.resolveIteratorFirst(it);
				while (r != 0) {
					String role = delegate.getRoleName(r);
					if ((role==null) || role.startsWith("."))
						delegate.freeReference(r);
					else
						pi.add(r);
					r = delegate.resolveIteratorNext(it);
				}
				
				return peculiarize(it, pi);
				//return peculiarize(delegate.getIteratorForAllOutgoingAssociationEnds(rClass));
			}
			catch(UnsupportedOperationException e)
			{
				long it = delegate.getIteratorForDirectOutgoingAssociationEnds(rClass);
				if (it == 0)
					return 0;
				
				// we will implement getIteratorForAssociations via getIteratorForDirectAssociations
								
				LinkedHashSet<Long> assocs = new LinkedHashSet<Long>();
				
				// getting superclasses and their associations
				long superClsIt = this.getIteratorForDirectSuperClasses(rClass); // recursive
				if (superClsIt == 0) {
					delegate.freeIterator(it);
					return 0;
				}
				
				long superCls = this.resolveIteratorFirst(superClsIt);
				while (superCls != 0) {
					// processing the next super class...
					long superAssocIt = this.getIteratorForAllOutgoingAssociationEnds(superCls); // recursive
					if (superAssocIt == 0) {
						for (Long assoc : assocs)
							this.freeReference(assoc); // freeing already found references
						this.freeReference(superCls);
						this.freeIterator(superClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long superAssoc = this.resolveIteratorFirst(superAssocIt);
					while (superAssoc != 0) {
						// processing the next superclass association...													
						if (assocs.contains(superAssoc))
							this.freeReference(superAssoc);
						else
							assocs.add(superAssoc);
						superAssoc = this.resolveIteratorNext(superAssocIt);
					}
					this.freeIterator(superAssocIt);
					
					this.freeReference(superCls);
					superCls = this.resolveIteratorNext(superClsIt);
				}
				this.freeIterator(superClsIt);
				
				// now, getting direct associations...
				long rAssoc = delegate.resolveIteratorFirst(it);
				while (rAssoc != 0) {													
					if (assocs.contains(rAssoc))
						delegate.freeReference(rAssoc);
					else
						assocs.add(rAssoc);
					rAssoc = delegate.resolveIteratorNext(it);
				}
				
				PeculiarIterator pi = new PeculiarIterator(it);
				for (Long a : assocs) {
					String role = delegate.getRoleName(a);
					if ((role==null) || role.startsWith("."))
						delegate.freeReference(a);
					else
						pi.add(a);
				}
				return peculiarize(it, pi);
			}
		}
		else
			return 0;
	}

	@Override
	public long getIteratorForDirectOutgoingAssociationEnds (long rClass)
	{
		if ((delegate != null)&&(rClass!=0)) {
			try {	
				long it = delegate.getIteratorForDirectOutgoingAssociationEnds(rClass);
				PeculiarIterator pi = new PeculiarIterator(it);
				long r = delegate.resolveIteratorFirst(it);
				while (r != 0) {
					String role = delegate.getRoleName(r);
					if ((role==null) || role.startsWith("."))
						delegate.freeReference(r);
					else
						pi.add(r);
					r = delegate.resolveIteratorNext(it);
				}
				
				return peculiarize(it, pi);
				//return peculiarize(delegate.getIteratorForDirectOutgoingAssociationEnds(rClass));
			}
			catch(UnsupportedOperationException e)
			{
				long it = delegate.getIteratorForAllOutgoingAssociationEnds(rClass);
				if (it == 0)
					return 0;
				
				// we will implement getIteratorForDirectAssociations via getIteratorForAllAssociations
				LinkedHashSet<Long> assocs = new LinkedHashSet<Long>();
				
				// getting all associations...
				long rAssoc = delegate.resolveIteratorFirst(it);
				while (rAssoc != 0) {
					if (assocs.contains(rAssoc))
						delegate.freeReference(rAssoc);
					else
						assocs.add(rAssoc);
					rAssoc = delegate.resolveIteratorNext(it);
				}
				
				// excluding associations of super classes...
				long superClsIt = delegate.getIteratorForDirectSuperClasses(rClass);
				if (superClsIt == 0) {
					for (Long assoc : assocs)
						delegate.freeReference(assoc); // freeing found references
					delegate.freeIterator(it);
					return 0;
				}
				
				long superCls = delegate.resolveIteratorFirst(superClsIt);
				while (superCls != 0) {
					// processing the next super class...
					long superAssocIt = this.getIteratorForAllOutgoingAssociationEnds(superCls); // recursive
					if (superAssocIt == 0) {
						for (Long assoc : assocs)
							delegate.freeReference(assoc); // freeing already found references
						delegate.freeReference(superCls);
						delegate.freeIterator(superClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long superAssoc = this.resolveIteratorFirst(superAssocIt);
					while (superAssoc != 0) {
						// processing the next super attribute...
						if (assocs.contains(superAssoc)) {
							this.freeReference(superAssoc);
							assocs.remove(superAssoc);
						}
						this.freeReference(superAssoc);
						superAssoc = this.resolveIteratorNext(superAssocIt);
					}
					this.freeIterator(superAssocIt);
					
					delegate.freeReference(superCls);
					superCls = delegate.resolveIteratorNext(superClsIt);
				}
				delegate.freeIterator(superClsIt);
				
				PeculiarIterator pi = new PeculiarIterator(it);
				for (Long a : assocs) {
					String role = delegate.getRoleName(a);
					if ((role==null) || role.startsWith("."))
						delegate.freeReference(a);
					else
						pi.add(a);
				}
				return peculiarize(it, pi);
			}
		}
		else
			return 0;
	}
	
	@Override
	public long getIteratorForAllIngoingAssociationEnds (long rClass)
	{
		if ((delegate != null)&&(rClass!=0)) {
			try {	
				return peculiarize(delegate.getIteratorForAllIngoingAssociationEnds(rClass));
			}
			catch(UnsupportedOperationException e)
			{
				long it = 0;
				try {
					it = delegate.getIteratorForDirectIngoingAssociationEnds(rClass);
				}
				catch(UnsupportedOperationException e2) {
					return getPeculiarIteratorForIngoingAssociationEnds(rClass, true);
				}
				
				if (it == 0)
					return 0;
				
				
				// we will implement getIteratorForAssociations via getIteratorForDirectAssociations
								
				LinkedHashSet<Long> assocs = new LinkedHashSet<Long>();
				
				// getting superclasses and their associations
				long superClsIt = this.getIteratorForDirectSuperClasses(rClass); // recursive
				if (superClsIt == 0) {
					delegate.freeIterator(it);
					return 0;
				}
				
				long superCls = this.resolveIteratorFirst(superClsIt);
				while (superCls != 0) {
					// processing the next super class...
					long superAssocIt = this.getIteratorForAllIngoingAssociationEnds(superCls); // recursive
					if (superAssocIt == 0) {
						for (Long assoc : assocs)
							this.freeReference(assoc); // freeing already found references
						this.freeReference(superCls);
						this.freeIterator(superClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long superAssoc = this.resolveIteratorFirst(superAssocIt);
					while (superAssoc != 0) {
						// processing the next superclass association...													
						if (assocs.contains(superAssoc))
							this.freeReference(superAssoc);
						else
							assocs.add(superAssoc);
						superAssoc = this.resolveIteratorNext(superAssocIt);
					}
					this.freeIterator(superAssocIt);
					
					this.freeReference(superCls);
					superCls = this.resolveIteratorNext(superClsIt);
				}
				this.freeIterator(superClsIt);
				
				// now, getting direct associations...
				long rAssoc = delegate.resolveIteratorFirst(it);
				while (rAssoc != 0) {													
					if (assocs.contains(rAssoc))
						delegate.freeReference(rAssoc);
					else
						assocs.add(rAssoc);
					rAssoc = delegate.resolveIteratorNext(it);
				}
				
				PeculiarIterator pi = new PeculiarIterator(it);
				for (Long a : assocs)
					pi.add(a);
				return peculiarize(it, pi);
			}
		}
		else
			return 0;
	}

	@Override
	public long getIteratorForDirectIngoingAssociationEnds (long rClass)
	{
		if ((delegate != null)&&(rClass!=0)) {
			try {	
				return peculiarize(delegate.getIteratorForDirectIngoingAssociationEnds(rClass));
			}
			catch(UnsupportedOperationException e)
			{
				long it = 0;
				try {
					it = delegate.getIteratorForAllIngoingAssociationEnds(rClass);
				}
				catch(UnsupportedOperationException e2) {
					return getPeculiarIteratorForIngoingAssociationEnds(rClass, false);
				}
				if (it == 0)
					return 0;
				
				// we will implement getIteratorForDirectAssociations via getIteratorForAllAssociations
				LinkedHashSet<Long> assocs = new LinkedHashSet<Long>();
				
				// getting all associations...
				long rAssoc = delegate.resolveIteratorFirst(it);
				while (rAssoc != 0) {
					if (assocs.contains(rAssoc))
						delegate.freeReference(rAssoc);
					else
						assocs.add(rAssoc);
					rAssoc = delegate.resolveIteratorNext(it);
				}
				
				// excluding associations of super classes...
				long superClsIt = delegate.getIteratorForDirectSuperClasses(rClass);
				if (superClsIt == 0) {
					for (Long assoc : assocs)
						delegate.freeReference(assoc); // freeing found references
					delegate.freeIterator(it);
					return 0;
				}
				
				long superCls = delegate.resolveIteratorFirst(superClsIt);
				while (superCls != 0) {
					// processing the next super class...
					long superAssocIt = this.getIteratorForAllIngoingAssociationEnds(superCls); // recursive
					if (superAssocIt == 0) {
						for (Long assoc : assocs)
							delegate.freeReference(assoc); // freeing already found references
						delegate.freeReference(superCls);
						delegate.freeIterator(superClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long superAssoc = this.resolveIteratorFirst(superAssocIt);
					while (superAssoc != 0) {
						// processing the next super attribute...
						if (assocs.contains(superAssoc)) {
							this.freeReference(superAssoc);
							assocs.remove(superAssoc);
						}
						this.freeReference(superAssoc);
						superAssoc = this.resolveIteratorNext(superAssocIt);
					}
					this.freeIterator(superAssocIt);
					
					delegate.freeReference(superCls);
					superCls = delegate.resolveIteratorNext(superClsIt);
				}
				delegate.freeIterator(superClsIt);
				
				PeculiarIterator pi = new PeculiarIterator(it);
				for (Long a : assocs)
					pi.add(a);
				return peculiarize(it, pi);
			}
		}
		else
			return 0;
	} 
	
	@Override
	public long getInverseAssociationEnd(long rAssociationEnd) {
		if ((delegate != null) && (rAssociationEnd != 0)) {
			long rInvAssoc = delegate.getInverseAssociationEnd(rAssociationEnd);
			String targetRole = delegate.getRoleName(rInvAssoc);
			if ((rInvAssoc != 0) && (targetRole != null) && (targetRole.startsWith("."))) {
				// simulating unidirectional associations --- do not return the inverse association,
				// if the association was created as bi-directional with a generated source role
				// (=inverse target role)
					delegate.freeReference(rInvAssoc);
					return 0;
				}
			return rInvAssoc;
		}
		else
			return 0;
	}
	@Override
	public long getSourceClass(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.getSourceClass(param0);
		else
			return 0;
	}
	@Override
	public long getTargetClass(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.getTargetClass(param0);
		else
			return 0;
	}
	@Override
	public String getRoleName(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.getRoleName(param0);
		else
			return null;
	}
	@Override
	public boolean isComposition(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			return delegate.isComposition(param0);
		else
			return false;
	}
	@Override
	public boolean isAdvancedAssociation(long param0)
	{
		if ((delegate != null)&&(param0!=0))
			try {
				return delegate.isAdvancedAssociation(param0);
			}
			catch(UnsupportedOperationException e)
			{
				return false;
			}
		else
			return false;
	}	
	@Override
	public boolean isAssociationEnd(long param0)
	{
		if (delegate != null) {
			try {
				return delegate.isAssociationEnd(param0);
			}
			catch (UnsupportedOperationException e) {
				
				if (param0 == 0)
					return false;
				long rCls = delegate.getSourceClass(param0);
				if (rCls == 0)
					return false;
				String roleName = delegate.getRoleName(param0);
				if (roleName == null) {
					delegate.freeReference(rCls);
					return false;
				}
				long rAssocEnd = delegate.findAssociationEnd(rCls, roleName);
				if (rAssocEnd != 0)
					delegate.freeReference(rAssocEnd);
				delegate.freeReference(rCls);
				return (rAssocEnd == param0);
			}
		}
		else
			return false;
	}

	///// Operations on links /////
	@Override
	public boolean createLink(long param0, long param1, long param2)
	{
		if ((delegate != null)&&(param0!=0)&&(param1!=0)&&(param2!=0))
			return delegate.createLink(param0, param1, param2);
		else
			return false;
	}
	@Override
	public boolean createOrderedLink(long rSourceObject, long rTargetObject,
			long rAssociation, int targetPosition) {
		if ((delegate == null) || (rSourceObject == 0) || (rTargetObject == 0) || (rAssociation == 0))
			return false;

		try {
			return delegate.createOrderedLink(rSourceObject, rTargetObject, rAssociation, targetPosition);			
		}
		catch(UnsupportedOperationException e)
		{

			long rInvAssoc = this.getInverseAssociationEnd(rAssociation);
			if (rInvAssoc == 0) { // unidirectional association; do not worry about order of inverse links
				LinkedList<Long> savedLateLinkedObjects = new LinkedList<Long>();

				boolean result = true;

				long it = this.getIteratorForLinkedObjects(rSourceObject, rAssociation);
				if (it != 0) {
					int index = 0;
					long r = this.resolveIteratorFirst(it);
					// skipping objects at positions from 0 to targetPosition-1 (including) or while not all objects traversed...
					while ( ((targetPosition<0/*means "infinity"*/) || (index < targetPosition))&&(r != 0)) {					
						index++;
						this.freeReference(r);
						r = this.resolveIteratorNext(it);
					}

					// saving objects at positions from the current object at targetPosition to the last object...
					while (r != 0) {
						savedLateLinkedObjects.add(r); // do not free the reference now
						r = this.resolveIteratorNext(it);
					}

					this.freeIterator(it);

					// deleting saved "late" links...							
					for (Long savedObj : savedLateLinkedObjects) {
						result = result || this.deleteLink(rSourceObject, savedObj.longValue(), rAssociation);
					}
				}

				if (result)
					result = this.createLink(rSourceObject, rTargetObject, rAssociation);

				// restoring saved links and freing references...
				for (Long savedObj : savedLateLinkedObjects) {
					boolean ok = this.createLink(rSourceObject, savedObj.longValue(), rAssociation);
					this.freeReference(savedObj.longValue()); // now we can free the reference
					if (result && !ok) {
						// error when restoring a link 
						this.deleteLink(rSourceObject, rTargetObject, rAssociation); 
						result = false;
					}
				}

				return result;			
			}
			else { // bidirectional associations; saved late links will be restored recursively
				class SavedLinkedObject {
					long rLinkedObject;
					int inversePosition;
				}
				LinkedList<SavedLinkedObject> savedLateLinkedObjects = new LinkedList<SavedLinkedObject>();

				boolean result = true; 

				long it = this.getIteratorForLinkedObjects(rSourceObject, rAssociation);
				if (it != 0) {
					int index = 0;
					long r = this.resolveIteratorFirst(it);
					// skipping objects at positions from 0 to targetPosition-1 (including) or while not all objects traversed...
					while ( ((targetPosition<0/*means "infinity"*/) || (index < targetPosition))&&(r != 0)) {					
						index++;
						this.freeReference(r);
						r = this.resolveIteratorNext(it);
					}

					// saving objects at positions from the current object at targetPosition to the last object...
					while (r != 0) {					
						SavedLinkedObject linkedObj = new SavedLinkedObject();
						linkedObj.rLinkedObject = r; // do not free the reference now
						linkedObj.inversePosition = this.getLinkedObjectPosition(r, rSourceObject, rInvAssoc);					
						savedLateLinkedObjects.add(linkedObj);

						r = this.resolveIteratorNext(it);
					}				

					this.freeIterator(it);

					// deleting saved "late" links...							
					for (SavedLinkedObject savedObj : savedLateLinkedObjects) {
						result = result || this.deleteLink(rSourceObject, savedObj.rLinkedObject, rAssociation);
					}				
				}

				result = result || this.createLink(rSourceObject, rTargetObject, rAssociation);

				// restoring saved links...
				for (SavedLinkedObject savedObj : savedLateLinkedObjects) {
					boolean ok = this.createOrderedLink(savedObj.rLinkedObject, rSourceObject, rInvAssoc, savedObj.inversePosition);
					this.freeReference(savedObj.rLinkedObject); // now we can free the reference
					if (result && !ok) {
						// error when restoring a link
						this.deleteLink(rSourceObject, rTargetObject, rAssociation); 
						result = false;
					}
				}
				this.freeReference(rInvAssoc);

				return result;						
			}			
		}
	}
	@Override
	public boolean deleteLink(long param0, long param1, long param2)
	{
		if ((delegate != null)&&(param0!=0)&&(param1!=0)&&(param2!=0))
			return delegate.deleteLink(param0, param1, param2);
		else
			return false;
	}
	@Override
	public boolean linkExists(long rSourceObject, long rTargetObject, long rAssociationEnd) {
		if ((rSourceObject == 0) || (rTargetObject == 0) || (rAssociationEnd == 0))
			return false;
		try {
			return delegate.linkExists(rSourceObject, rTargetObject, rAssociationEnd);
		}
		catch(UnsupportedOperationException e)
		{
			long it = this.getIteratorForLinkedObjects(rSourceObject, rAssociationEnd);
			if (it != 0) {
				long r = this.resolveIteratorFirst(it);
				while (r != 0) {
					if (r == rTargetObject) {
						this.freeReference(r); // freeing the reference returned by the iterator
						this.freeIterator(it); // we do not need the iterator anymore
						return true; 
					}

					this.freeReference(r);
					r = this.resolveIteratorNext(it);
				}
				this.freeIterator(it);
			}
			return false;
		}
	}
	@Override
	public long getIteratorForLinkedObjects(long param0, long param1)
	{
		if (delegate != null)
			return delegate.getIteratorForLinkedObjects(param0, param1);
		else
			return 0;
	}
	@Override
	public int getLinkedObjectPosition(long rSourceObject, long rTargetObject, long rAssociation) {
		if ((delegate == null) || (rSourceObject == 0) || (rTargetObject == 0) || (rAssociation == 0))
			return -1;
		try {
			return delegate.getLinkedObjectPosition(rSourceObject, rTargetObject, rAssociation);
		}
		catch(UnsupportedOperationException e)
		{
			long it = this.getIteratorForLinkedObjects(rSourceObject, rAssociation);
			if (it != 0) {
				int index = 0;
				long r = this.resolveIteratorFirst(it);
				while (r != 0) {
					if (r == rTargetObject) {
						this.freeReference(r); // freeing the reference returned by the iterator
						this.freeIterator(it); // we do not need the iterator anymore
						return index; 
					}

					index++;
					this.freeReference(r);
					r = this.resolveIteratorNext(it);
				}
				this.freeIterator(it);
			}
			return -1;
		}
	}

	  ///// Operations on enumerations /////
/*
 * 	@Override
	public long createEnumeration(String name) {
		if (delegate != null) {
			long r = delegate.findEnumeration(name);
			if (r != 0) {
				delegate.freeReference(r);
				return 0; // the given enumeration name already exists
			}			
			return delegate.createEnumeration(name);
		}
		else
			return 0;
	}

	@Override
	public long findEnumeration(String param0)
	{
		if (delegate != null)
			return delegate.findEnumeration(param0);
		else
			return 0;
	}

	@Override
	public String getEnumerationName(long param0)
	{
		if (delegate != null)
			return delegate.getEnumerationName(param0);
		else
			return null;
	}

	@Override
	public boolean deleteEnumeration(long param0)
	{
		if (delegate != null)
			return delegate.deleteEnumeration(param0);
		else
			return false;
	}

	@Override
	public long getIteratorForEnumerations()
	{
		if (delegate != null)
			return delegate.getIteratorForEnumerations();
		else
			return 0;
	}

	@Override
	public boolean isEnumeration(long param0)
	{
		if (delegate != null)
			return delegate.isEnumeration(param0);
		else
			return false;
	}


 */
	  ///// Operations on enumeration literals /////
/*
 * 	@Override
	public boolean addEnumerationLiteral(long param0, String param1)
	{
		if (delegate != null)
			return delegate.addEnumerationLiteral(param0, param1);
		else
			return false;
	}

	@Override
	public boolean enumerationContainsLiteral(long param0, String param1)
	{
		if (delegate != null)
			return delegate.enumerationContainsLiteral(param0, param1);
		else
			return false;
	}

	@Override
	public String getEnumerationLiterals(long param0)
	{
		if (delegate != null)
			return delegate.getEnumerationLiterals(param0);
		else
			return null;
	}

	@Override
	public boolean deleteEnumerationLiteral(long param0, String param1)
	{
		if (delegate != null)
			return delegate.deleteEnumerationLiteral(param0, param1);
		else
			return false;
	}

 */
	  ///// Operations on methods /////
/*	  long createMethod (long rClass, String name, long returnType, boolean isStatic);
	  long findMethod (long rClass, String name);
	  long deleteMethod (long rMethod);
	  long getMethodReturnType (long rMethod);
	  boolean isMethod (long r);
	  boolean isStaticMethod (long rMethod);
	  long attachExceptionToMethod (long rMethod, long rExceptionType);
	  long detachExceptionFromMethod (long rMethod, long rExceptionType);
	  long createMethodParameter (long rMethod, String name, long type);
	  long findMethodParameter (long rMethod, String name);
	  boolean deleteMethodParameter (long rParameter);
	  long getMethodParameterType (long rParameter);
	  boolean isMethodParameter (long r);
	  long getIteratorForAllMethods (long rClass);
	  long getIteratorForDirectMethods (long rClass);
	  long getIteratorForMethodParameters (long rMethod);
	  long getIteratorForMethodExceptions (long rMethod);
	  String callStaticMethod (long rClass, long rStaticMethod, String arguments);
	  String callMethod (long rThisObject, long rMethod, String arguments);

	@Override
	public boolean isMethod(long param0)
	{
		if (delegate != null)
			return delegate.isMethod(param0);
		else
			return false;
	}

	@Override
	public String callMethod(long param0, long param1, String param2)
	{
		if (delegate != null)
			return delegate.callMethod(param0, param1, param2);
		else
			return null;
	}

	@Override
	public long getIteratorForMethodParameters(long param0)
	{
		if (delegate != null)
			return delegate.getIteratorForMethodParameters(param0);
		else
			return 0;
	}

	@Override
	public long getIteratorForMethodExceptions(long param0)
	{
		if (delegate != null)
			return delegate.getIteratorForMethodExceptions(param0);
		else
			return 0;
	}

	@Override
	public long findMethod(long param0, String param1)
	{
		if (delegate != null)
			return delegate.findMethod(param0, param1);
		else
			return 0;
	}

	@Override
	public long getMethodReturnType(long param0)
	{
		if (delegate != null)
			return delegate.getMethodReturnType(param0);
		else
			return 0;
	}

	@Override
	public boolean isStaticMethod(long param0)
	{
		if (delegate != null)
			return delegate.isStaticMethod(param0);
		else
			return false;
	}

	@Override
	public long attachExceptionToMethod(long param0, long param1)
	{
		if (delegate != null)
			return delegate.attachExceptionToMethod(param0, param1);
		else
			return 0;
	}

	@Override
	public long detachExceptionFromMethod(long param0, long param1)
	{
		if (delegate != null)
			return delegate.detachExceptionFromMethod(param0, param1);
		else
			return 0;
	}

	@Override
	public long createMethodParameter(long param0, String param1, long param2)
	{
		if (delegate != null)
			return delegate.createMethodParameter(param0, param1, param2);
		else
			return 0;
	}

	@Override
	public long findMethodParameter(long param0, String param1)
	{
		if (delegate != null)
			return delegate.findMethodParameter(param0, param1);
		else
			return 0;
	}

	@Override
	public boolean deleteMethodParameter(long param0)
	{
		if (delegate != null)
			return delegate.deleteMethodParameter(param0);
		else
			return false;
	}

	@Override
	public long getMethodParameterType(long param0)
	{
		if (delegate != null)
			return delegate.getMethodParameterType(param0);
		else
			return 0;
	}

	@Override
	public boolean isMethodParameter(long param0)
	{
		if (delegate != null)
			return delegate.isMethodParameter(param0);
		else
			return false;
	}
	
	@Override
	public long createMethod(long param0, String param1, long param2, boolean param3)
	{
		if (delegate != null)
			return delegate.createMethod(param0, param1, param2, param3);
		else
			return 0;
	}

	@Override
	public long deleteMethod(long param0)
	{
		if (delegate != null)
			return delegate.deleteMethod(param0);
		else
			return 0;
	}

	

	@Override
	public long getIteratorForAllMethods(long rClass) {
		if (delegate != null) {
			long it = delegate.getIteratorForAllMethods(rClass);
			if (it == 0) {
				it = delegate.getIteratorForDirectMethods(rClass);
				if (it == 0)
					return 0;

				// we will implement getIteratorForAllMethods via getIteratorForDirectMethods

				// getting superclasses and their methods
				LinkedHashSet<Long> methods = new LinkedHashSet<Long>();
				long superClsIt = this.getIteratorForDirectSuperClasses(rClass); // recursive
				if (superClsIt == 0) {
					delegate.freeIterator(it);
					return 0;
				}

				long superCls = this.resolveIteratorFirst(superClsIt);
				while (superCls != 0) {
					// processing the next super class...
					long superMethodIt = this.getIteratorForAllMethods(superCls); // recursive
					if (superMethodIt == 0) {
						for (Long attr : methods)
							delegate.freeReference(attr); // freeing already found references
						this.freeReference(superCls);
						this.freeIterator(superClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long superMethod = this.resolveIteratorFirst(superMethodIt);
					while (superMethod != 0) {
						// processing the next superclass method...
						if (methods.contains(superMethod))
							this.freeReference(superMethod);
						else
							methods.add(superMethod);
						superMethod = this.resolveIteratorNext(superMethodIt);
					}
					this.freeIterator(superMethodIt);

					this.freeReference(superCls);
					superCls = this.resolveIteratorNext(superClsIt);
				}
				this.freeIterator(superClsIt);

				// now, getting direct methods...
				long rMethod = delegate.resolveIteratorFirst(it);
				while (rMethod != 0) {
					if (methods.contains(rMethod))
						delegate.freeReference(rMethod);
					else
						methods.add(rMethod);
					rMethod = delegate.resolveIteratorNext(it);
				}

				peculiarIterators.put(it, methods.iterator());
			}

			return it;
		}
		else
			return 0;
	}

	@Override
	public long getIteratorForDirectMethods(long rClass) {
		if (delegate != null) {
			long it = delegate.getIteratorForDirectMethods(rClass);
			if (it == 0) {
				it = delegate.getIteratorForAllMethods(rClass);
				if (it == 0)
					return 0;

				// we will implement getIteratorForDirectMethods via getIteratorForAllMethods
				LinkedHashSet<Long> methods = new LinkedHashSet<Long>();

				// getting all methods...
				long rMethod = delegate.resolveIteratorFirst(it);
				while (rMethod != 0) {
					methods.add(rMethod);
					rMethod = delegate.resolveIteratorNext(it);
				}

				// excluding methods of super classes...
				long superClsIt = delegate.getIteratorForDirectSuperClasses(rClass);
				if (superClsIt == 0) {
					for (Long mthd : methods)
						delegate.freeReference(mthd); // freeing found references
					delegate.freeIterator(it);
					return 0;
				}

				long superCls = delegate.resolveIteratorFirst(superClsIt);
				while (superCls != 0) {
					// processing the next super class...
					long superMethodIt = this.getIteratorForAllMethods(superCls); // recursive
					if (superMethodIt == 0) {
						for (Long attr : methods)
							delegate.freeReference(attr); // freeing already found references
						delegate.freeReference(superCls);
						delegate.freeIterator(superClsIt);
						delegate.freeIterator(it);
						return 0;
					}
					long superMethod = delegate.resolveIteratorFirst(superMethodIt);
					while (superMethod != 0) {
						// processing the next superclass method...
						if (methods.contains(superMethod)) {
							delegate.freeReference(superMethod);
							methods.remove(superMethod);
						}
						delegate.freeReference(superMethod);
						superMethod = delegate.resolveIteratorNext(superMethodIt);
					}
					delegate.freeIterator(superMethodIt);

					delegate.freeReference(superCls);
					superCls = delegate.resolveIteratorNext(superClsIt);
				}
				delegate.freeIterator(superClsIt);

				peculiarIterators.put(it, methods.iterator());
			}
			return it;
		}
		else
			return 0;
	}

	@Override
	public String callStaticMethod(long param0, long param1, String param2)
	{
		if (delegate != null)
			return delegate.callStaticMethod(param0, param1, param2);
		else
			return null;
	}
	
*/
	  ///// Generics /////
/*	  
	  long createTemplateParameter (long rClass, String parameterName);
	  long findTemplateParameter (long rClass, String name);
	  boolean deleteTemplateParameter (long rTemplateParameter);
	  String getTemplateParameterName (long rTemplateParameter);
	  long getIteratorForTemplateParameters (long rClass);
	  boolean isTemplateParameter (long r);
	  long createBoundClass (long rGenericClass);
	  boolean deleteBoundClass (long rBoundClass);
	  long getIteratorForBoundClasses (long rGenericClass);
	  boolean isBoundClass (long r);
	  long getGenericClass (long rBoundClass);
	  boolean setTemplateArgument (long rBoundClass, long rTemplateParameter, long rTemplateArgument);
	  long getTemplateArgument (long rBoundClass, long rTemplateParameter);

	@Override
	public long getIteratorForTemplateParameters(long param0)
	{
		if (delegate != null)
			return delegate.getIteratorForTemplateParameters(param0);
		else
			return 0;
	}


	@Override
	public long createTemplateParameter(long param0, String param1)
	{
		if (delegate != null)
			return delegate.createTemplateParameter(param0, param1);
		else
			return 0;
	}

	@Override
	public long findTemplateParameter(long param0, String param1)
	{
		if (delegate != null)
			return delegate.findTemplateParameter(param0, param1);
		else
			return 0;
	}

	@Override
	public boolean deleteTemplateParameter(long param0)
	{
		if (delegate != null)
			return delegate.deleteTemplateParameter(param0);
		else
			return false;
	}

	@Override
	public String getTemplateParameterName(long param0)
	{
		if (delegate != null)
			return delegate.getTemplateParameterName(param0);
		else
			return null;
	}

	@Override
	public boolean isTemplateParameter(long param0)
	{
		if (delegate != null)
			return delegate.isTemplateParameter(param0);
		else
			return false;
	}

	@Override
	public long createBoundClass(long param0)
	{
		if (delegate != null)
			return delegate.createBoundClass(param0);
		else
			return 0;
	}

	@Override
	public boolean deleteBoundClass(long param0)
	{
		if (delegate != null)
			return delegate.deleteBoundClass(param0);
		else
			return false;
	}

	@Override
	public long getIteratorForBoundClasses(long param0)
	{
		if (delegate != null)
			return delegate.getIteratorForBoundClasses(param0);
		else
			return 0;
	}

	@Override
	public long getGenericClass(long param0)
	{
		if (delegate != null)
			return delegate.getGenericClass(param0);
		else
			return 0;
	}

	@Override
	public boolean isBoundClass(long param0)
	{
		if (delegate != null)
			return delegate.isBoundClass(param0);
		else
			return false;
	}


	@Override
	public boolean setTemplateArgument(long param0, long param1, long param2)
	{
		if (delegate != null)
			return delegate.setTemplateArgument(param0, param1, param2);
		else
			return false;
	}

	@Override
	public long getTemplateArgument(long param0, long param1)
	{
		if (delegate != null)
			return delegate.getTemplateArgument(param0, param1);
		else
			return 0;
	}
*/
	  ///// Operations with iterators /////
	
	@Override
	public long resolveIteratorFirst(long it) {
		if ((delegate != null)&&(it!=0)) {
			PeculiarIterator pi = peculiarIterators.get(it);
			if (pi == null) {
				return delegate.resolveIteratorFirst(it);
			}
			else {
				return pi.resolveFirst();
			}
		}
		else
			return 0;
	}

	@Override
	public long resolveIteratorNext(long it) {
		if ((delegate != null)&&(it!=0)) {
			PeculiarIterator pi = peculiarIterators.get(it);
			if (pi == null)
				return delegate.resolveIteratorNext(it);
			else {
				return pi.resolveNext();
			}
		}
		else
			return 0;
	}
	
	@Override
	public int getIteratorLength (long it)
	{
		if ((delegate != null) && (it!=0)) {
			PeculiarIterator pi = peculiarIterators.get(it);
			if (pi == null) {
				try {
					return delegate.getIteratorLength(it);
				}
				catch(UnsupportedOperationException e) {
					peculiarize(it);
					pi = peculiarIterators.get(it);
					if (pi == null)
						return 0;
					else
						return pi.getLength();
				}
			}
			else {
				return pi.getLength();
			}
		}
		else
			return 0;		
	}

	@Override
	public long resolveIterator (long it, int position)
	{
		if ((delegate != null) && (it!=0)) {
			PeculiarIterator pi = peculiarIterators.get(it);
			if (pi == null) {
				try {
					return delegate.resolveIterator(it, position);
				}
				catch(UnsupportedOperationException e) {
					peculiarize(it);
					pi = peculiarIterators.get(it);
					if (pi == null)
						return 0;
					else
						return pi.resolve(position);
				}
			}
			else {
				return pi.resolve(position);
			}
		}
		else
			return 0;		
	}
	
	@Override
	public void freeReference(long r)
	{
		if ((delegate != null) && (r!=0))
			delegate.freeReference(r);
		else
			return;
	}
	
	@Override
	public void freeIterator (long it)
	{
		if ((delegate != null) && (it!=0)) {
			PeculiarIterator pi = peculiarIterators.remove(it);
			if (pi == null)
				delegate.freeIterator(it);
			else {
				pi.free();
			}
		}
		else
			return;		
	}

	@Override
	public String serializeReference(long r)
	{
		if ((delegate != null) && (r!=0))
			try {
				return delegate.serializeReference(r);
			}
			catch(UnsupportedOperationException e) {
				if (this.isClass(r)) {
					String name = this.getClassName(r);
					if (name == null)
						return null;
					return "class:"+name;
				}
				if (this.isAssociationEnd(r)) {
					long rCls = this.getSourceClass(r);
					if (rCls == 0)
						return null;
					String className = this.getClassName(rCls);
					String roleName = this.getRoleName(r);
					this.freeReference(rCls);
					if ((className == null) || (roleName == null))
						return null;
					return "association_end:"+className+"->"+roleName;
				}
				if (this.isAttribute(r)) {
					long rCls = this.getAttributeDomain(r);
					if (rCls == 0)
						return null;
					String className = this.getClassName(rCls);
					String attrName = this.getAttributeName(r);
					this.freeReference(rCls);
					if ((className == null) || (attrName == null))
						return null;
					return "attribute:"+className+"->"+attrName;
				}
				// else: assume object
				long it = this.getIteratorForDirectObjectClasses(r);
				if (it == 0)
					return null;
				long rCls = this.resolveIteratorFirst(it);
				this.freeIterator(it);
				if (rCls == 0)
					return null;
				String className = this.getClassName(rCls);
				if (className == null) {
					this.freeReference(rCls);
					return null;
				}
				it = this.getIteratorForDirectClassObjects(rCls);
				int i = 0;
				long rObj = this.resolveIteratorFirst(it);
				while (rObj != 0) {
					this.freeReference(rObj);
					if (r == rObj) {
						this.freeReference(rCls);
						this.freeIterator(it);
						return "object:"+className+"["+i+"]";
					}
					rObj = this.resolveIteratorNext(it);
					i++;
				}
				this.freeReference(rCls);
				this.freeIterator(it);
				
				return null; // no serialization created
			}
		else
			return null;
	}

	@Override
	public long deserializeReference(String s)
	{
		if (delegate != null)
			try {
				return delegate.deserializeReference(s);
			}
			catch(UnsupportedOperationException e) {
				if (s == null)
					return 0;
				if (s.startsWith("class:")) {
					return this.findClass(s.substring(6));
				}
				if (s.startsWith("association_end:")) {
					s = s.substring(16);
					int i = s.indexOf("->");
					if (i < 0)
						return 0;
					long rCls = this.findClass(s.substring(0, i));
					if (rCls == 0)
						return 0;
					long rAssoc = this.findAssociationEnd(rCls, s.substring(i+2));
					this.freeReference(rCls);
					return rAssoc;
				}
				if (s.startsWith("attribute:")) {
					s = s.substring(10);
					int i = s.indexOf("->");
					if (i < 0)
						return 0;
					long rCls = this.findClass(s.substring(0, i));
					if (rCls == 0)
						return 0;
					long rAttr = this.findAttribute(rCls, s.substring(i+2));
					this.freeReference(rCls);
					return rAttr;					
				}
				if (s.startsWith("object:")) {
					s = s.substring(7);
					int i = s.indexOf("[");
					if (i < 0)
						return 0;
					long rCls = this.findClass(s.substring(0, i));
					if (rCls == 0)
						return 0;
					s = s.substring(i+1, s.length()-1); // without "[" and "]"
					try {
						i = Integer.parseInt(s);
					}
					catch (Throwable t) {
						this.freeReference(rCls);
						return 0;
					}
					long it = this.getIteratorForDirectClassObjects(rCls);
					this.freeReference(rCls);
					if (it == 0)
						return 0;
					long retVal = this.resolveIterator(it, i);
					this.freeIterator(it);
					return retVal;
				}
				return 0; // could not deserialize
			}
		else
			return 0;
	}

	@Override
	public long getIteratorForLinguisticClasses() {
		if (delegate != null)
			return delegate.getIteratorForLinguisticClasses();
		else
			return 0;
	}

	@Override
	public long getIteratorForDirectLinguisticInstances(long r) {
		if ((delegate != null)&&(r!=0))
			return delegate.getIteratorForDirectLinguisticInstances(r);
		else
			return 0;
	}

	@Override
	public long getIteratorForAllLinguisticInstances(long r) {
		if ((delegate != null)&&(r!=0))
			return delegate.getIteratorForAllLinguisticInstances(r);
		else
			return 0;
	}

	@Override
	public long getLinguisticClassFor(long r) {
		if ((delegate != null)&&(r!=0))
			return delegate.getLinguisticClassFor(r);
		else
			return 0;
	}

	@Override
	public boolean isLinguistic(long r) {
		if ((delegate != null)&&(r!=0))
			return delegate.isLinguistic(r);
		else
			return false;
	}

	@Override
	public String callSpecificOperation(String operationName, String arguments) {
		if ((delegate != null)&&(operationName!=null)) {
			try {
				return delegate.callSpecificOperation(operationName, arguments);
			}
			catch (Throwable t) {
				return null;
			}
		}
		else
			return null;
	}
	
	@Override
	public boolean exists(String location) {
		if (delegate instanceof IRepository)
			return ((IRepository)delegate).exists(location);
		else
			return false;
	}

	@Override
	public boolean open(String location) {
		if (delegate instanceof IRepository) {
			boolean retVal = ((IRepository)delegate).open(location);
			return retVal;
		}
		else
			return false;
	}

	@Override
	public void close() {
		if (delegate instanceof IRepository)
			((IRepository)delegate).close();
	}

	@Override
	public boolean startSave() {
		if (delegate instanceof IRepository)
			return ((IRepository)delegate).startSave();
		else
			return false;
	}

	@Override
	public boolean finishSave() {
		if (delegate instanceof IRepository)
			return ((IRepository)delegate).finishSave();
		else
			return false;
	}

	@Override
	public boolean cancelSave() {
		if (delegate instanceof IRepository)
			return ((IRepository)delegate).cancelSave();
		else
			return false;
	}

	@Override
	public boolean drop(String location) {
		if (delegate instanceof IRepository)
			return ((IRepository)delegate).drop(location);
		else
			return false;
	}
	
}
