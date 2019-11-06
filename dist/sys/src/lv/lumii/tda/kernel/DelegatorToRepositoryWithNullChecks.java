package lv.lumii.tda.kernel;

import java.util.*;

import javax.swing.JOptionPane;

import lv.lumii.tda.raapi.*;
import lv.lumii.tda.util.ReverseObjectsIndexer;

// DelegatorToRepositoryActions performs the following functions:
//   * checks whether the delegate is not null;
//   * checks arguments for 0/null and does not forward calls with such values
//     (when appropriate);
//   * in setAttributeValue, if value is null, calls deleteAttributeValue
//   * does not create a class, an attribute, or an association, if
//     it already exists (with the same name)

public class DelegatorToRepositoryWithNullChecks extends DelegatorToRepositoryBase implements IRepository {

	public static IRepository STUB_DELEGATOR =
			new DelegatorToRepositoryWithNullChecks(null);
	
	public DelegatorToRepositoryWithNullChecks(IRepository _delegate) {
		super();
		setDelegate(_delegate);
	}
	

	// nullify() should be called, when the delegate is closed;
	// then other modules using can continue to use this RepositoryActionsWrapper,
	// but they will get the false/null/0 value for delegate actions;	
	public void nullify() 
	{
		setDelegate(null);
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
		if ((delegate != null) && (strOK(name))) {
			long r = this.findClass(name);
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
		if ((delegate != null) && (strOK(name)))
			return delegate.findClass(name);
		else
			return 0;
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
			return delegate.getIteratorForClasses();
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
			return delegate.isClass(param0);
		else
			return false;
	}
	@Override
	public boolean isDirectSubClass (long rSubClass, long rSuperClass)
	{
		if ((delegate != null)&&(rSubClass != 0)&&(rSuperClass != 0)) {
			return delegate.isDirectSubClass(rSubClass, rSuperClass);
		}
		else
			return false;
	}
	@Override
	public boolean isDerivedClass (long rDirectlyOrIndirectlyDerivedClass, long rSuperClass)
	{
		if ((delegate != null)&&(rDirectlyOrIndirectlyDerivedClass != 0)&&(rSuperClass != 0)&&(rDirectlyOrIndirectlyDerivedClass!=rSuperClass)) {
			return delegate.isDerivedClass(rDirectlyOrIndirectlyDerivedClass, rSuperClass);
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
			return delegate.includeObjectInClass(rObject, rClass);
		else
			return false; 
	}
	@Override
	public boolean excludeObjectFromClass(long rObject, long rClass) {
		if ((delegate == null)&&(rObject!=0)&&(rClass!=0))
			return delegate.excludeObjectFromClass(rObject, rClass);
		else
			return false; 
	}	
	@Override
	public boolean moveObject (long rObject, long rToClass)
	{
		if ((delegate == null)&&(rObject!=0)&&(rToClass!=0))
			return delegate.moveObject(rObject, rToClass);
		else
			return false; 
	}
	@Override
	public long getIteratorForAllClassObjects(long rClass) {
		if ((delegate != null)&&(rClass!=0)) {			
			return delegate.getIteratorForAllClassObjects(rClass);
		}
		else
			return 0;
	}
	@Override
	public long getIteratorForDirectClassObjects(long rClass) {
		if ((delegate != null)&&(rClass!=0)) {			
			return delegate.getIteratorForDirectClassObjects(rClass);
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
			return delegate.isTypeOf(rObject, rClass);
		}
		else
			return false;
	}
	@Override
	public boolean isKindOf(long rObject, long rClass)
	{
		if (delegate != null) {
			return delegate.isKindOf(rObject, rClass);
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
			return delegate.findAttribute(rClass, name);
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
			return delegate.getIteratorForAllAttributes(rClass);
		}
		else
			return 0;
	}
	@Override
	public long getIteratorForDirectAttributes(long rClass) {
		if ((delegate != null) && (rClass!=0)) {
			return delegate.getIteratorForDirectAttributes(rClass);
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
	public boolean isAttribute(long r)
	{
		if ((delegate != null) && (r!=0)) {
			return delegate.isAttribute(r);
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
	public long getIteratorForObjectsByAttributeValue(long param0, String param1)
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
			this.freeReference(r);
			if (r != 0)
				return 0; // the target role already exists
			
			r = this.findAssociationEnd(rTargetClass, sourceRoleName);
			this.freeReference(r);
			if (r != 0)
				return 0; // the source role already exists
			
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
			delegate.freeReference(r);
			if (r != 0)
				return 0; // the target role already exists
			
			return delegate.createDirectedAssociation(rSourceClass, rTargetClass,
				targetRoleName, isComposition);
		}
		else
			return 0;
	}
	@Override
	public long createAdvancedAssociation(String name, boolean nAry, boolean associationClass)
	{
		if ((delegate != null) && (name != null) && (name.length()>0))
			return delegate.createAdvancedAssociation(name, nAry, associationClass);
		else
			return 0;
	}
	@Override
	public long findAssociationEnd(long rSourceClass, String targetRoleName) {
		if ((delegate != null) && (rSourceClass!=0) && (targetRoleName != null))
			return delegate.findAssociationEnd(rSourceClass, targetRoleName);
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
			return delegate.getIteratorForAllOutgoingAssociationEnds(rClass);
		}
		else
			return 0;
	}

	@Override
	public long getIteratorForDirectOutgoingAssociationEnds (long rClass)
	{
		if ((delegate != null)&&(rClass!=0)) {
			return delegate.getIteratorForDirectOutgoingAssociationEnds(rClass);
		}
		else
			return 0;
	}
	
	@Override
	public long getIteratorForAllIngoingAssociationEnds (long rClass)
	{
		if ((delegate != null)&&(rClass!=0)) {
			return delegate.getIteratorForAllIngoingAssociationEnds(rClass);
		}
		else
			return 0;
	}

	@Override
	public long getIteratorForDirectIngoingAssociationEnds (long rClass)
	{
		if ((delegate != null)&&(rClass!=0)) {
			return delegate.getIteratorForDirectIngoingAssociationEnds(rClass);
		}
		else
			return 0;
	} 
	
	@Override
	public long getInverseAssociationEnd(long rAssociationEnd) {
		if ((delegate != null) && (rAssociationEnd != 0)) {
			return delegate.getInverseAssociationEnd(rAssociationEnd);
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
			return delegate.isAdvancedAssociation(param0);
		else
			return false;
	}	
	@Override
	public boolean isAssociationEnd(long param0)
	{
		if (delegate != null) {
			return delegate.isAssociationEnd(param0);
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

		return delegate.createOrderedLink(rSourceObject, rTargetObject, rAssociation, targetPosition);			
	}
	@Override
	public boolean deleteLink(long param0, long param1, long param2)
	{
		//JOptionPane.showMessageDialog(null, "deletelink(nulls) "+param0+" "+param1+" "+param2);
		if ((delegate != null)&&(param0!=0)&&(param1!=0)&&(param2!=0))
			return delegate.deleteLink(param0, param1, param2);
		else
			return false;
	}
	@Override
	public boolean linkExists(long rSourceObject, long rTargetObject, long rAssociationEnd) {
		if ((rSourceObject == 0) || (rTargetObject == 0) || (rAssociationEnd == 0))
			return false;
		return delegate.linkExists(rSourceObject, rTargetObject, rAssociationEnd);
	}
	@Override
	public long getIteratorForLinkedObjects(long rObject, long rAssociationEnd)
	{
		if ((delegate != null) && (rObject != 0) && (rAssociationEnd != 0))
			return delegate.getIteratorForLinkedObjects(rObject, rAssociationEnd);
		else
			return 0;
	}
	@Override
	public int getLinkedObjectPosition(long rSourceObject, long rTargetObject, long rAssociation) {
		if ((delegate == null) || (rSourceObject == 0) || (rTargetObject == 0) || (rAssociation == 0))
			return -1;
		return delegate.getLinkedObjectPosition(rSourceObject, rTargetObject, rAssociation);
	}
	  ///// Operations with iterators /////
	
	@Override
	public long resolveIteratorFirst(long it) {
		if ((delegate != null)&&(it!=0)) {
			return delegate.resolveIteratorFirst(it);
		}
		else
			return 0;
	}

	@Override
	public long resolveIteratorNext(long it) {
		if ((delegate != null)&&(it!=0)) {
			return delegate.resolveIteratorNext(it);
		}
		else
			return 0;
	}
	
	@Override
	public int getIteratorLength (long it)
	{
		if ((delegate != null) && (it!=0)) {
			return delegate.getIteratorLength(it);
		}
		else
			return 0;		
	}

	@Override
	public long resolveIterator (long it, int position)
	{
		if ((delegate != null) && (it!=0)) {
			return delegate.resolveIterator(it, position);
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
			delegate.freeIterator(it);
		}
		else
			return;		
	}

	@Override
	public String serializeReference(long r)
	{
		if ((delegate != null) && (r!=0))
			return delegate.serializeReference(r);
		else
			return null;
	}

	@Override
	public long deserializeReference(String s)
	{
		if ((delegate != null) && (strOK(s)))
			return delegate.deserializeReference(s);
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
	
	@Override
	public boolean exists(String location) {
		if (delegate instanceof IRepository)
			return ((IRepository)delegate).exists(location);
		else
			return false;
	}

	@Override
	public boolean open(String location) {
		JOptionPane.showMessageDialog(null, "null chks");
		if (delegate instanceof IRepository) {
			return ((IRepository)delegate).open(location);
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
