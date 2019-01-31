package lv.lumii.tda.kernel;

import java.util.*;

import javax.swing.JOptionPane;

import lv.lumii.tda.raapi.*;
import lv.lumii.tda.util.ReverseObjectsIndexer;

public class DelegatorToRepositoryWithObjectRecreation extends DelegatorToRepositoryBase implements IRepository {

	class PeculiarIterator
	{
		private ArrayList<Long> list = new ArrayList<Long>();
		private int i = 0;
		private long it;
		private boolean forObjects;
		PeculiarIterator(long _it, boolean _forObjects)
		{
			this.it = _it;
			this.forObjects = _forObjects;
		}
		void add(long r)
		{
			if (DelegatorToRepositoryWithObjectRecreation.this.isClass(r)) {
				String name = delegate.getClassName(r);
				if ((name != null) && ( name.startsWith(".") || name.contains("::.") )) {
					delegate.freeReference(r);
					return;
				}
			}
			if (DelegatorToRepositoryWithObjectRecreation.this.isAssociationEnd(r)) {
				String name = delegate.getRoleName(r);
				if ((name != null) && name.startsWith(".")) {
					delegate.freeReference(r);
					return;
				}
			}
			if (DelegatorToRepositoryWithObjectRecreation.this.isAttribute(r)) {
				String name = delegate.getAttributeName(r);
				if ((name != null) && name.startsWith(".")) {
					delegate.freeReference(r);
					return;
				}
			}
			
			if (forObjects)
				list.add(initialReference(r));
			else
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
			if (_i >= list.size())
				return 0;
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
	
	ReverseObjectsIndexer<Long> objectsIndexer = null;
	
	void registerRecreatedObject(long oldRef, long newRef)
	{
		if (oldRef == newRef)
			objectsIndexer.freeIndex(oldRef);
		else
			objectsIndexer.set(oldRef, newRef);
	}
	
	long recreatedObject(long oldRef)
	{
		Long l = objectsIndexer.getObject(oldRef);
		if (l == null)
			return oldRef; // do not using the mapping
		else
			return l;
	}
	
	long initialReference(long currentObjectReference)
	{
		if ((objectsIndexer.findIndex(currentObjectReference)==0)
			&&(objectsIndexer.get(currentObjectReference) == null)) {
			// index is free
			return currentObjectReference; // do not adding the mapping
		}
		else {
			return objectsIndexer.getIndex(currentObjectReference);
		}
	}

	private long peculiarize(long it, boolean forObjects)
	// creates a new PeculiarIterator and ADDS elements to it
	{
		if (it==0)
			return 0;
		PeculiarIterator pi = new PeculiarIterator(it, forObjects);
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
		
		PeculiarIterator pi = new PeculiarIterator(itCls, false);
		
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


	public DelegatorToRepositoryWithObjectRecreation(IRepository _delegate) {
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
	public boolean createGeneralization(long rSubClass, long rSuperClass)
	{
		if ((delegate != null) && (rSubClass!=0) && (rSuperClass!=0)) {
			
			if (!classHasObjects(rSubClass)) {
				return delegate.createGeneralization(rSubClass, rSuperClass);
			}
			else {
				// need to re-create objects:				
				startUpdateObjects(rSubClass);
				boolean retVal = delegate.createGeneralization(rSubClass, rSuperClass);
				finishUpdateObjects();
				return retVal;
			}			
		}
		else
			return false;
	}
	@Override
	public boolean deleteGeneralization(long rSubClass, long rSuperClass)
	{
		if ((delegate != null) && (rSubClass!=0) && (rSuperClass!=0)) {
			if (!classHasObjects(rSubClass)) {
				return delegate.deleteGeneralization(rSubClass, rSuperClass);
			}
			else {
				// need to re-create objects:				
				startUpdateObjects(rSubClass);
				boolean retVal = delegate.deleteGeneralization(rSubClass, rSuperClass);
				finishUpdateObjects();
				return retVal;
			}			
		}
		else
			return false;
	}
	public boolean isClass(long param0)
	{
		param0 = recreatedObject(param0);
		return delegate.isClass(param0);
	}

	  ///// Operations on objects /////
	@Override
	public long createObject(long rClass)
	{
		if ((delegate != null)&&(rClass!=0))
			return initialReference(delegate.createObject(rClass));
		else
			return 0;
	}
	@Override
	public boolean deleteObject(long rClass)
	{
		rClass = recreatedObject(rClass);
		if ((delegate != null)&&(rClass!=0)) {
			boolean retVal = delegate.deleteObject(rClass);
			return retVal;
		}
		else
			return false;
	}
	@Override
	public boolean includeObjectInClass(long rObject, long rClass) {
		rObject = recreatedObject(rObject);
		if ((delegate == null)&&(rObject!=0)&&(rClass!=0))
			return delegate.includeObjectInClass(rObject, rClass);
		else
			return false;
	}
	@Override
	public boolean excludeObjectFromClass(long rObject, long rClass) {
		rObject = recreatedObject(rObject);
		if ((delegate == null)&&(rObject!=0)&&(rClass!=0))
			return delegate.excludeObjectFromClass(rObject, rClass);
		else
			return false; 
	}	
	@Override
	public boolean moveObject (long rObject, long rToClass)
	{
		rObject = recreatedObject(rObject);
		if ((delegate == null)&&(rObject!=0)&&(rToClass!=0))
			return delegate.moveObject(rObject, rToClass);
		else
			return false; 
	}
	@Override
	public long getIteratorForAllClassObjects(long rClass) {
		return peculiarize(delegate.getIteratorForAllClassObjects(rClass), true);
	}
	@Override
	public long getIteratorForDirectClassObjects(long rClass) {
		return peculiarize(delegate.getIteratorForDirectClassObjects(rClass), true);
	}
	@Override
	public boolean isTypeOf(long rObject, long rClass)
	{
		rObject = recreatedObject(rObject);
		return delegate.isTypeOf(rObject, rClass);
	}
	@Override
	public boolean isKindOf(long rObject, long rClass)
	{
		rObject = recreatedObject(rObject);
		return delegate.isKindOf(rObject, rClass);
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
			
			if (!classHasObjects(rClass)) {
				return delegate.createAttribute(rClass, name, type);
			}
			else {
				// need to re-create objects:				
				startUpdateObjects(rClass);
				long retVal = delegate.createAttribute(rClass, name, type);
				finishUpdateObjects();
				return retVal;
			}
		}
		else
			return 0;
	}
	@Override
	public boolean deleteAttribute(long rAttr)
	{
		if ((delegate != null)&&(rAttr!=0)) {
			long rClass = delegate.getAttributeDomain(rAttr);
			if (!classHasObjects(rClass)) {
				delegate.freeReference(rClass);
				return delegate.deleteAttribute(rAttr);
			}
			else {
				// need to re-create objects:				
				startUpdateObjects(rClass);
				boolean retVal = delegate.deleteAttribute(rAttr);
				finishUpdateObjects();
				delegate.freeReference(rClass);
				return retVal;
			}
		}
		else
			return false;
	}
	@Override
	public boolean isAttribute(long param0)
	{
		param0 = recreatedObject(param0);
		return delegate.isAttribute(param0);
	}

	///// Operations on attribute values /////
	@Override
	public boolean setAttributeValue(long rObject, long rAttribute, String value) {
		rObject = recreatedObject(rObject);
		if ((delegate != null)&&(rObject!=0)&&(rAttribute!=0)) {
			if (value == null)
				return delegate.deleteAttributeValue(rObject, rAttribute);
			else {
				boolean retVal  = delegate.setAttributeValue(rObject, rAttribute, value);
				return retVal;
			}
		}
		else
			return false;
	}
	@Override
	public String getAttributeValue(long rObject, long rAttribute)
	{
		rObject = recreatedObject(rObject);
		if ((delegate != null)&&(rObject!=0)&&(rAttribute!=0))
			return delegate.getAttributeValue(rObject, rAttribute);
		else
			return null;
	}
	@Override
	public boolean deleteAttributeValue(long rObject, long rAttribute)
	{
		rObject = recreatedObject(rObject);
		if ((delegate != null)&&(rObject!=0)&&(rAttribute!=0))
			return delegate.deleteAttributeValue(rObject, rAttribute);
		else
			return false;
	}
	@Override
	public long getIteratorForObjectsByAttributeValue(long param0, String param1) // TODO
	{
		if (delegate != null)
			return peculiarize(delegate.getIteratorForObjectsByAttributeValue(param0, param1), true);
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

			if (!classHasObjects(rSourceClass) && !classHasObjects(rTargetClass)) {
				return delegate.createAssociation(rSourceClass, rTargetClass, sourceRoleName,
						targetRoleName, isComposition);
			}
			else {
				// need to re-create objects:				
				startUpdateObjects(rSourceClass);
				startUpdateObjects(rTargetClass);
				long retVal = delegate.createAssociation(rSourceClass, rTargetClass, sourceRoleName,
						targetRoleName, isComposition);
				finishUpdateObjects();
				return retVal;
			}			
		}
		else
			return 0;
	}	
	
	@Override
	public long createDirectedAssociation (long rSourceClass, long rTargetClass, String targetRoleName, boolean isComposition)
	{
		if ((delegate != null) && (rSourceClass!=0) && (rTargetClass!=0)) {
			long r = this.findAssociationEnd(rSourceClass, targetRoleName);
			if (r != 0) {
				this.freeReference(r);
				return 0; // the target role already exists
			}			

			if (!classHasObjects(rSourceClass) && !classHasObjects(rTargetClass)) {
				return delegate.createDirectedAssociation(rSourceClass, rTargetClass,
						targetRoleName, isComposition);
			}
			else {
				// need to re-create objects:				
				startUpdateObjects(rSourceClass);
				startUpdateObjects(rTargetClass);
				long retVal = delegate.createDirectedAssociation(rSourceClass, rTargetClass,
						targetRoleName, isComposition);
				finishUpdateObjects();
				return retVal;
			}			
		}
		else
			return 0;
	}/*
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
	}*/
	@Override
	public boolean deleteAssociation(long rAssoc)
	{
		if ((delegate != null)&&(rAssoc!=0)) {
			long rSrcCls = delegate.getSourceClass(rAssoc);
			long rTgtCls = delegate.getTargetClass(rAssoc);
			if (!classHasObjects(rSrcCls)&&!classHasObjects(rTgtCls)) {
				delegate.freeReference(rSrcCls);
				delegate.freeReference(rTgtCls);
				return delegate.deleteAssociation(rAssoc);
			}
			else {
				// need to re-create objects:				
				startUpdateObjects(rSrcCls);
				startUpdateObjects(rTgtCls);
				boolean retVal = delegate.deleteAssociation(rAssoc);
				finishUpdateObjects();
				delegate.freeReference(rSrcCls);
				delegate.freeReference(rTgtCls);
				return retVal;
			}			
				
		}
		else
			return false;
	}


	///// Operations on links /////
	@Override
	public boolean createLink(long rSourceObject, long rTargetObject, long rAssociation)
	{
		rSourceObject = recreatedObject(rSourceObject);
		rTargetObject = recreatedObject(rTargetObject);
		if ((delegate != null)&&(rSourceObject!=0)&&(rTargetObject!=0)&&(rAssociation!=0))
			return delegate.createLink(rSourceObject, rTargetObject, rAssociation);
		else
			return false;
	}
	@Override
	public boolean createOrderedLink(long rSourceObject, long rTargetObject,
			long rAssociation, int targetPosition) {
		if ((delegate == null) || (rSourceObject == 0) || (rTargetObject == 0) || (rAssociation == 0))
			return false;

		rSourceObject = recreatedObject(rSourceObject);
		rTargetObject = recreatedObject(rTargetObject);
		return delegate.createOrderedLink(rSourceObject, rTargetObject, rAssociation, targetPosition);			
	}
	@Override
	public boolean deleteLink(long rSourceObject, long rTargetObject, long rAssociation)
	{
		rSourceObject = recreatedObject(rSourceObject);
		rTargetObject = recreatedObject(rTargetObject);
		if ((delegate != null)&&(rSourceObject!=0)&&(rTargetObject!=0)&&(rAssociation!=0))
			return delegate.deleteLink(rSourceObject, rTargetObject, rAssociation);
		else
			return false;
	}
	@Override
	public boolean linkExists(long rSourceObject, long rTargetObject, long rAssociationEnd) {
		rSourceObject = recreatedObject(rSourceObject);
		rTargetObject = recreatedObject(rTargetObject);
		if ((rSourceObject == 0) || (rTargetObject == 0) || (rAssociationEnd == 0))
			return false;
		return delegate.linkExists(rSourceObject, rTargetObject, rAssociationEnd);
	}
	@Override
	public long getIteratorForLinkedObjects(long rObject, long rAssociationEnd)
	{
		rObject = recreatedObject(rObject);
		if (delegate != null)
			return peculiarize(delegate.getIteratorForLinkedObjects(rObject, rAssociationEnd), true);
		else
			return 0;
	}
	@Override
	public int getLinkedObjectPosition(long rSourceObject, long rTargetObject, long rAssociation) {
		if ((delegate == null) || (rSourceObject == 0) || (rTargetObject == 0) || (rAssociation == 0))
			return -1;
		rSourceObject = recreatedObject(rSourceObject);
		rTargetObject = recreatedObject(rTargetObject);
		return delegate.getLinkedObjectPosition(rSourceObject, rTargetObject, rAssociation);
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
					peculiarize(it, false);
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
					peculiarize(it, false);
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
				return new Long(r).toString();
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
				try {
					return Long.parseLong(s);
				}
				catch(Throwable t)
				{
					return 0;
				}
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
		if ((delegate != null)&&(operationName!=null))
			return delegate.callSpecificOperation(operationName, arguments);
		else
			return null;	
	}
	
	private boolean classHasObjects(long rClass)
	{
		boolean hasObjects = false;
		long it = this.getIteratorForAllClassObjects(rClass);
		if (it != 0) {
			long r = this.resolveIteratorFirst(it);
			if (r!=0) {
				hasObjects = true;
				this.freeReference(r);
			}
			this.freeIterator(it);
		}
		return hasObjects;		
	}
	
	RAAPITransaction t = null;
	private boolean startUpdateObjects(long rClass)
	{
		if (t== null)
			t = new RAAPITransaction(this, new IReferenceMapper() {

				@Override
				public long getStableReference(long r) {
					return r;
				}

				@Override
				public long getUnstableReference(long rStableReference) {
					return rStableReference;
				}

				@Override
				public boolean redirectStableReference(long rOldStableReference, long rNewUnstableReference) {
					registerRecreatedObject(rOldStableReference, rNewUnstableReference);
					return true;
				}

				@Override
				public void releaseStableReference(long rStableReference) {
				}
				
			});
		return startUpdateObjects(t, rClass);
	}

	private boolean startUpdateObjects(RAAPITransaction raapi, long rClass)
	{
		boolean ok = true;
		
		long it = raapi.getIteratorForDirectSubClasses(rClass);
		long rSubCls = raapi.resolveIteratorFirst(it);
		while (rSubCls != 0) {
			if (!startUpdateObjects(raapi, rSubCls))
				ok = false;
			raapi.freeReference(rSubCls);
			rSubCls = raapi.resolveIteratorNext(it);
		}
		this.freeIterator(it);

		it = raapi.getIteratorForDirectClassObjects(rClass);
		long rObj = raapi.resolveIteratorFirst(it);
		while (rObj != 0) {
			if (!DelegatorToRepositoryWithCascadeDelete.cascadeDeleteObject(rObj, raapi, false, true))
				ok = false;
			rObj = raapi.resolveIteratorNext(it);
		}
		raapi.freeIterator(it);
		return ok;
	}
	
	private void finishUpdateObjects()
	{
		t.undo();
		t.clear();
		t = null;
	}

	@Override
	public boolean open(String location) {
		if (delegate instanceof IRepository) {
			if (objectsIndexer == null) {
				objectsIndexer = new ReverseObjectsIndexer<Long>();
				objectsIndexer.set(0L, 0L);
			}
			return ((IRepository)delegate).open(location);
		}
		else
			return false;
	}

	@Override
	public void close() {
		if (delegate instanceof IRepository)
			((IRepository)delegate).close();
		
		objectsIndexer = null;
	}

}
