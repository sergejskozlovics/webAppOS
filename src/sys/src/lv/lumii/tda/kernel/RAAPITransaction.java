package lv.lumii.tda.kernel;

import java.util.ArrayList;

import lv.lumii.tda.raapi.IRepository;
import lv.lumii.tda.raapi.IRepositoryManagement;
import lv.lumii.tda.raapi.RAAPI;

// external action (stored in the repository)
// for the undo mechanism

 
/* A class that is able to store the history of RAAPI calls as
 * well as to undo and to redo these calls.
 * RAAPI calls MUST ALREADY BE de-cascaded (e.g., by means of the Delegator3WithCascadeDelete).
 * 
 * Notice! We assume that, when deleting links, the cascade delete layer
 * temporary deletes further links, then deletes the given link, then restores further links.
 * 
 * Notice! We assume that cascade delete layer on moveObject() excludes the objects from all its classes
 * except from the first one.
 * 
 * checked assert: temporary unstable references that are neither passed as arguments, nor returned, have to be freed)
 * checked assert: stable references must be redirected to 0, when the corresponding element is deleted
 */
public class RAAPITransaction extends DelegatorBase<RAAPI> implements IRepository {

	RAAPI delegate = null;
	IRepositoryManagement delegate2 = null;
	
	IReferenceMapper mapper;
	
	public RAAPITransaction(RAAPI _delegate, IRepositoryManagement _delegate2, IReferenceMapper _mapper)
	{
		delegate = _delegate;
		delegate2 = _delegate2;
		mapper = _mapper;
	}
	
	public RAAPITransaction(IRepository _delegate, IReferenceMapper _mapper)
	{
		delegate = _delegate;
		delegate2 = _delegate;
		mapper = _mapper;
	}

	public RAAPITransaction(RAAPI _delegate, IReferenceMapper _mapper)
	{
		delegate = _delegate;
		delegate2 = null;
		mapper = _mapper;
	}
	
	public IReferenceMapper getReferenceMapper()
	{
		return mapper;
	}

	@Override
	public RAAPI getDelegate()
	{
		return delegate;
	}
	
	private abstract class RepositoryAction {
		RAAPI raapi;
		IReferenceMapper mapper;
		boolean created;
		RepositoryAction(RAAPI _raapi, IReferenceMapper _mapper, boolean _created)
		{
			raapi = _raapi;
			mapper = _mapper;
			created = _created;
		}
		boolean undo()
		{
			if (created)
				return redelete();
			else
				return recreate();			
		}
		
		boolean redo()
		{
			if (created)
				return recreate();
			else
				return redelete();			
		}
		
		abstract boolean recreate();		
		abstract boolean redelete();
		abstract void free();
	}
	
	boolean undone = false;
	ArrayList<RepositoryAction> actions = new ArrayList<RepositoryAction>();

	private class ClassAction extends RepositoryAction {
		IReferenceMapper mapper;
		long rsClass;
		String name;
		ClassAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsClass, String _name, boolean _created) {
			super(_raapi, _mapper, _created);
			mapper = _mapper;
			rsClass = _rsClass;
			name = _name;
		}
		@Override
		boolean recreate() {
			long newUnstableReference = raapi.createClass(name);
			if (newUnstableReference != 0) {
				mapper.redirectStableReference(rsClass, newUnstableReference);
				return true;
			}
			else
				return false;
		}
		@Override
		boolean redelete() {
			boolean retVal = raapi.deleteClass(mapper.getUnstableReference(rsClass));
			if (retVal)
				mapper.redirectStableReference(rsClass, 0);
			return retVal;
		}
		
		@Override
		void free() {
			mapper.releaseStableReference(rsClass);
		}
	}	

	@Override
	public long createClass (String name)
	{
		if (undone) {
			clear();
		}
		
		long rCls = delegate.createClass(name);
		if (rCls == 0)
			return 0;
		
		long rsCls = mapper.getStableReference(rCls);
		
		actions.add(new ClassAction(delegate, mapper, rsCls, name, true));
		
		return rCls;
		
	}
	
	@Override
	public boolean deleteClass (long rClass)
	{
		if (undone) {
			clear();
		}
		
		String name = delegate.getClassName(rClass);
		if (name == null)
			return false;
		
		long rsCls = mapper.getStableReference(rClass);
		
		if (!delegate.deleteClass(rClass)) {
			mapper.redirectStableReference(rsCls, 0);
			mapper.releaseStableReference(rsCls);
			return false;
		}
		
		actions.add(new ClassAction(delegate, mapper, rsCls, name, false));
		
		return true;
		
	}

	private class GeneralizationAction extends RepositoryAction {
		IReferenceMapper mapper;
		long rsSubClass;
		long rsSuperClass;
		GeneralizationAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsSubClass, long _rsSuperClass, boolean _created) {
			super(_raapi, _mapper, _created);
			mapper = _mapper;
			rsSubClass = _rsSubClass;
			rsSuperClass = _rsSuperClass;
		}
		@Override
		boolean recreate() {			
			return raapi.createGeneralization(mapper.getUnstableReference(rsSubClass), mapper.getUnstableReference(rsSuperClass));
		}
		@Override
		boolean redelete() {
			return raapi.deleteGeneralization(mapper.getUnstableReference(rsSubClass), mapper.getUnstableReference(rsSuperClass));
		}
		
		@Override
		void free() {
			mapper.releaseStableReference(rsSubClass);
			mapper.releaseStableReference(rsSuperClass);
		}
	}	

	@Override
	public boolean createGeneralization (long rSubClass, long rSuperClass)
	{
		if (undone) {
			clear();
		}
		
		if (!delegate.createGeneralization(rSubClass, rSuperClass))
				return false;
		
		long rsSubCls = mapper.getStableReference(rSubClass);
		long rsSuperCls = mapper.getStableReference(rSuperClass);
		
		actions.add(new GeneralizationAction(delegate, mapper, rsSubCls, rsSuperCls, true));
		
		return true;
	}

	@Override
	public boolean deleteGeneralization (long rSubClass, long rSuperClass)
	{
		if (undone) {
			clear();
		}
		
		if (!delegate.deleteGeneralization(rSubClass, rSuperClass))
				return false;
		
		long rsSubCls = mapper.getStableReference(rSubClass);
		long rsSuperCls = mapper.getStableReference(rSuperClass);
		
		actions.add(new GeneralizationAction(delegate, mapper, rsSubCls, rsSuperCls, false));
		
		return true;
	}
	
	private class ObjectAction extends RepositoryAction {
		IReferenceMapper mapper;
		long rsObject;
		long rsClass;
		ObjectAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsObject, long _rsClass, boolean _created) {
			super(_raapi, _mapper, _created);
			mapper = _mapper;
			rsObject = _rsObject;
			rsClass = _rsClass;
		}
		@Override
		boolean recreate() {
			long newUnstableReference = raapi.createObject(mapper.getUnstableReference(rsClass));
			if (newUnstableReference != 0) {
				mapper.redirectStableReference(rsObject, newUnstableReference);
				return true;
			}
			else
				return false;
		}
		@Override
		boolean redelete() {
			boolean retVal = raapi.deleteObject(mapper.getUnstableReference(rsObject));
			if (retVal)
				mapper.redirectStableReference(rsObject, 0);
			return retVal;
		}
		
		@Override
		void free() {
			mapper.releaseStableReference(rsObject);
			mapper.releaseStableReference(rsClass);
		}
	}	
		
	@Override
	public long createObject (long rClass)
	{
		if (undone) {
			clear();
		}
		
		long rObject = delegate.createObject(rClass);
		if (rObject == 0)
			return 0;
				
		long rsClass = mapper.getStableReference(rClass);
		long rsObject = mapper.getStableReference(rObject);		
		
		actions.add(new ObjectAction(delegate, mapper, rsObject, rsClass, true));
		
		return rObject;
	}
	
	@Override
	public boolean deleteObject(long rObject)
	{
		if (undone) {
			clear();
		}
		long it = delegate.getIteratorForDirectObjectClasses(rObject);
		if (it == 0)
			return false;
		long rCls = delegate.resolveIteratorFirst(it);
		delegate.freeIterator(it);
		if (rCls == 0)
			return false;
		
		long rsObject = mapper.getStableReference(rObject);		
		
		if (!delegate.deleteObject(rObject)) {
			mapper.releaseStableReference(rsObject);
			delegate.freeReference(rCls);
			return false;
		}
		
		mapper.redirectStableReference(rsObject, 0);
		
		long rsCls = mapper.getStableReference(rCls);
		delegate.freeReference(rCls);
		
		actions.add(new ObjectAction(delegate, mapper, rsObject, rsCls, false));
		
		return true;
	}
	
	private class ObjectClassAction extends RepositoryAction {
		long rsObject;
		long rsClass;
		ObjectClassAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsObjectReference, long _rsClassReference, boolean _created) {
			super(_raapi, _mapper, _created);
			rsObject = _rsObjectReference;
			rsClass = _rsClassReference;
		}
		@Override
		boolean recreate() {
			return raapi.includeObjectInClass(mapper.getUnstableReference(rsObject), mapper.getUnstableReference(rsClass)); 
		}
		@Override
		boolean redelete() {
			return raapi.excludeObjectFromClass(mapper.getUnstableReference(rsObject), mapper.getUnstableReference(rsClass)); 
		}		
		@Override
		void free() {
			mapper.releaseStableReference(rsObject);
			mapper.releaseStableReference(rsClass);
		}
	}	

	@Override
	public boolean includeObjectInClass(long rObject, long rClass)
	{		
		if (undone) {
			clear();
		}
		
		if (!delegate.includeObjectInClass(rObject, rClass))
			return false;

		long rsObject = mapper.getStableReference(rObject);
		long rsClass = mapper.getStableReference(rClass);
		
		actions.add(new ObjectClassAction(delegate, mapper, rsObject, rsClass, true));
		
		return true;
	}
	
	@Override
	public boolean excludeObjectFromClass(long rObject, long rClass)
	{		
		if (undone) {
			clear();
		}
		
		if (!delegate.excludeObjectFromClass(rObject, rClass))
			return false;

		long rsObject = mapper.getStableReference(rObject);
		long rsClass = mapper.getStableReference(rClass);
		
		actions.add(new ObjectClassAction(delegate, mapper, rsObject, rsClass, false));
		
		return true;
	}
	
	// Notice! We assume that cascade delete layer on moveObject() excludes the objects from all its classes
	// except from the first one.
	private class MoveObjectAction extends RepositoryAction {
		long rsObject;
		long rsOldClass;
		long rsNewClass;
		MoveObjectAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsObjectReference, long _rsOldClass, long _rsNewClass) {
			super(_raapi, _mapper, true /*this action does not have the inverse*/);
			rsObject = _rsObjectReference;
			rsOldClass = _rsOldClass;
			rsNewClass = _rsNewClass;
		}
		@Override
		boolean recreate() {
			return raapi.moveObject(mapper.getUnstableReference(rsObject), mapper.getUnstableReference(rsNewClass)); 
		}
		@Override
		boolean redelete() {
			return raapi.moveObject(mapper.getUnstableReference(rsObject), mapper.getUnstableReference(rsOldClass));
		}		
		@Override
		void free() {
			mapper.releaseStableReference(rsObject);
			mapper.releaseStableReference(rsOldClass);
			mapper.releaseStableReference(rsNewClass);
		}
	}	
	
	@Override
	public boolean moveObject (long rObject, long rToClass)
	{		
		if (undone) {
			clear();
		}

		long it = delegate.getIteratorForDirectObjectClasses(rObject);
		if (it == 0)
			return false;
		long rOldCls = delegate.resolveIteratorFirst(it);
		delegate.freeIterator(it);
		if (rOldCls == 0)
			return false;
		
		if (!delegate.moveObject(rObject, rToClass)) {
			delegate.freeReference(rOldCls);
			return false;
		}
		
		long rsObject = mapper.getStableReference(rObject);
		long rsOldClass = mapper.getStableReference(rOldCls);
		delegate.freeReference(rOldCls);
		long rsNewClass = mapper.getStableReference(rToClass);
		
		actions.add(new MoveObjectAction(delegate, mapper, rsObject, rsOldClass, rsNewClass));
		
		return true;
	}
	
	private class AttributeAction extends RepositoryAction {
		long rsClass;
		long rsAttribute;		
		String name;
		long rsType;
		AttributeAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsClass, long _rsAttribute, String _name, long _rsType, boolean _created) {
			super(_raapi, _mapper, _created);
			rsClass = _rsClass;
			rsAttribute = _rsAttribute;
			name = _name;
			rsType = _rsType;
		}
		@Override
		boolean recreate() {
			long newUnstableReference = raapi.createAttribute(mapper.getUnstableReference(rsClass), name, mapper.getUnstableReference(rsType));
			if (newUnstableReference != 0) {
				mapper.redirectStableReference(rsAttribute, newUnstableReference);
				return true;
			}
			else
				return false;
		}
		@Override
		boolean redelete() {
			boolean retVal = raapi.deleteAttribute(mapper.getUnstableReference(rsAttribute));
			if (retVal)
				mapper.redirectStableReference(rsAttribute, 0);
			return retVal;
		}
		@Override
		void free() {
			mapper.releaseStableReference(rsClass);
			mapper.releaseStableReference(rsAttribute);
			mapper.releaseStableReference(rsType);
		}
	}	
	
	@Override
	public long createAttribute (long rClass, String name, long rPrimitiveType)
	{
		if (undone) {
			clear();
		}
		
		long rAttr = delegate.createAttribute(rClass, name, rPrimitiveType);
		if (rAttr == 0)
			return 0;
				
		long rsClass = mapper.getStableReference(rClass);
		long rsAttr = mapper.getStableReference(rAttr);
		long rsType = mapper.getStableReference(rPrimitiveType);
		
		actions.add(new AttributeAction(delegate, mapper, rsClass, rsAttr, name, rsType, true));
		
		return rAttr;
		
	}
	
	@Override
	public boolean deleteAttribute (long rAttribute)
	{
		if (undone) {
			clear();
		}
		
		if (rAttribute == 0)
			return false;
		
		long rType = delegate.getAttributeType(rAttribute);
		if (rType == 0)
			return false;
		long rClass = delegate.getAttributeDomain(rAttribute);
		if (rClass == 0) {
			delegate.freeReference(rType);
			return false;
		}
		
		String name = delegate.getAttributeName(rAttribute);

		long rsClass = mapper.getStableReference(rClass);
		delegate.freeReference(rClass);
		long rsAttr = mapper.getStableReference(rAttribute);
		long rsType = mapper.getStableReference(rType);
		delegate.freeReference(rType);
		
		
		if ((name==null) || !delegate.deleteAttribute(rAttribute)) {
			mapper.releaseStableReference(rsClass);
			mapper.releaseStableReference(rsAttr);
			mapper.releaseStableReference(rsType);
			return false;
		}
		
		mapper.redirectStableReference(rsAttr, 0);
		
		actions.add(new AttributeAction(delegate, mapper, rsClass, rsAttr, name, rsType, false));
		
		return true;
		
	}
	
	private class AttributeValueAction extends RepositoryAction {
		long rsObject;
		long rsAttribute;
		String oldValue;
		String newValue;
		AttributeValueAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsObjectReference, long _rsAttributeReference, String _oldValue, String _newValue) {
			super(_raapi, _mapper, true);
			rsObject = _rsObjectReference;
			rsAttribute = _rsAttributeReference;
			oldValue = _oldValue;
			newValue = _newValue;
		}
		@Override
		boolean recreate() {
			if (newValue == null)
				return raapi.deleteAttributeValue(mapper.getUnstableReference(rsObject), mapper.getUnstableReference(rsAttribute));
			else
				return raapi.setAttributeValue(mapper.getUnstableReference(rsObject), mapper.getUnstableReference(rsAttribute), newValue);
		}
		@Override
		boolean redelete() {
			if (oldValue == null)
				return raapi.deleteAttributeValue(mapper.getUnstableReference(rsObject), mapper.getUnstableReference(rsAttribute));
			else
				return raapi.setAttributeValue(mapper.getUnstableReference(rsObject), mapper.getUnstableReference(rsAttribute), oldValue);
		}
		@Override
		void free() {
			mapper.releaseStableReference(rsObject);
			mapper.releaseStableReference(rsAttribute);
		}
	}	
	
	@Override
	public boolean setAttributeValue (long rObject, long rAttribute, String value)
	{
		if (undone) {
			clear();
		}
		
		String oldValue = delegate.getAttributeValue(rObject, rAttribute);
		
		if (!delegate.setAttributeValue(rObject, rAttribute, value))
			return false;

		long rsObject = mapper.getStableReference(rObject);
		long rsAttribute = mapper.getStableReference(rAttribute);
		
		actions.add(new AttributeValueAction(delegate, mapper, rsObject, rsAttribute, oldValue, value));
		
		return true;		
	}
	
	@Override
	public boolean deleteAttributeValue(long rObject, long rAttribute)
	{
		if (undone) {
			clear();
		}
		
		String value = delegate.getAttributeValue(rObject, rAttribute);
		
		if (!delegate.deleteAttributeValue(rObject, rAttribute))
			return false;

		long rsObject = mapper.getStableReference(rObject);
		long rsAttribute = mapper.getStableReference(rAttribute);
		
		actions.add(new AttributeValueAction(delegate, mapper, rsObject, rsAttribute, value, null));
		
		return true;		
	}
	
	private class AssociationAction extends RepositoryAction {
		long rsSourceClassReference;
		long rsTargetClassReference;
		long rsAssociationReference;
		long rsInverseAssociationReference;
		String sourceRole = null; // if null, we assume the association is a directed association
		String targetRole;
		boolean isComposition;
		
		String name = null; // if not null, then the association is an advanced association
		boolean nAry;
		boolean associationClass;
		
		// constructor for bi-directional and directed associations
		AssociationAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsAssociationReference, long _rsInverseAssociationReference, long _rsSourceClassReference, long _rsTargetClassReference, String _sourceRole, String _targetRole, boolean _isComposition, boolean _created) {
			super(_raapi, _mapper, _created);
			rsAssociationReference = _rsAssociationReference;
			rsInverseAssociationReference = _rsInverseAssociationReference;
			rsSourceClassReference = _rsSourceClassReference;
			rsTargetClassReference = _rsTargetClassReference;
			sourceRole = _sourceRole;
			targetRole = _targetRole;
			isComposition = _isComposition;
		}
		
		// constructor for peculiar associations
		AssociationAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsAssociationReference, String _name, boolean _nAry, boolean _associationClass, boolean _created)
		{
			super(_raapi, _mapper, _created);
			rsAssociationReference = _rsAssociationReference;
			name = _name;
			nAry = _nAry;
			associationClass = _associationClass;
		}
		
		boolean isAdvancedAssociation()
		{
			return (name!=null);
		}
		
		boolean isDirectedAssociation()
		{
			return sourceRole!=null;
		}
		
		@Override
		boolean recreate() {
			if (isAdvancedAssociation()) {
				// advanced association
				long r = raapi.createAdvancedAssociation(name, nAry, associationClass);
				if (r!=0)
					mapper.redirectStableReference(rsAssociationReference, r);
				return (r!=0);
			}
			else {
				// ordinary association
				
				if (isDirectedAssociation()) {
					// directed association
					long r = raapi.createDirectedAssociation(mapper.getUnstableReference(rsSourceClassReference), mapper.getUnstableReference(rsTargetClassReference), targetRole, isComposition);
					if (r!=0)
						mapper.redirectStableReference(rsAssociationReference, r);
					return (r!=0);
				}
				else {
					// bi-directional association
					long r = raapi.createAssociation(mapper.getUnstableReference(rsSourceClassReference), mapper.getUnstableReference(rsTargetClassReference), sourceRole, targetRole, isComposition);
					if (r!=0) {
						mapper.redirectStableReference(rsAssociationReference, r);
						r = raapi.getInverseAssociationEnd(r);
						if (r!=0) {
							mapper.redirectStableReference(rsInverseAssociationReference, r);
							raapi.freeReference(r);
						}
					}
					return (r!=0);
				} 
			}
		}
		
		@Override
		boolean redelete() {
			boolean retVal = raapi.deleteAssociation(mapper.getUnstableReference(rsAssociationReference));
			if (retVal) {
				mapper.redirectStableReference(rsAssociationReference, 0);
				if (!isAdvancedAssociation() && isDirectedAssociation())
					mapper.redirectStableReference(rsInverseAssociationReference, 0);
			} 
			return retVal;
		}
		
		@Override
		void free() {
			if (!isAdvancedAssociation()) {
				// ordinary association
				mapper.releaseStableReference(rsSourceClassReference);
				mapper.releaseStableReference(rsTargetClassReference);
				if (!isDirectedAssociation()) // bi-directional association
					mapper.releaseStableReference(rsInverseAssociationReference);
			}
			
			mapper.releaseStableReference(rsAssociationReference);
			
		}		
	}	
	
	@Override
	public long createAssociation (long rSourceClass, long rTargetClass, String sourceRoleName, String targetRoleName, boolean isComposition)
	{
		if (undone) {
			clear();
		}
		
		long rAssoc = delegate.createAssociation(rSourceClass, rTargetClass, sourceRoleName, targetRoleName, isComposition);
		if (rAssoc == 0)
			return 0;
		
		long rsAssoc = mapper.getStableReference(rAssoc);
		
		long rInv = delegate.getInverseAssociationEnd(rAssoc);
		long rsInv = mapper.getStableReference(rInv);
		delegate.freeReference(rInv);
		
		long rsSourceClass = mapper.getStableReference(rSourceClass);
		long rsTargetClass = mapper.getStableReference(rTargetClass);
		
		actions.add(new AssociationAction(delegate, mapper, rsAssoc, rsInv, rsSourceClass, rsTargetClass, sourceRoleName, targetRoleName, isComposition, true));
		
		return rAssoc;
	}
	
	@Override
	public long createDirectedAssociation (long rSourceClass, long rTargetClass, String targetRoleName, boolean isComposition)
	{
		if (undone) {
			clear();
		}
		
		long rAssoc = delegate.createDirectedAssociation(rSourceClass, rTargetClass, targetRoleName, isComposition);
		if (rAssoc == 0)
			return 0;
		
		long rsAssoc = mapper.getStableReference(rAssoc);
		
		long rsSourceClass = mapper.getStableReference(rSourceClass);
		long rsTargetClass = mapper.getStableReference(rTargetClass);
		
		actions.add(new AssociationAction(delegate, mapper, rsAssoc, 0, rsSourceClass, rsTargetClass,null, targetRoleName, isComposition, true));
		
		return rAssoc;
	}
	
	@Override
	public long createAdvancedAssociation (String name, boolean nAry, boolean associationClass)
	{
		if (undone) {
			clear();
		}
		
		long rAssoc = delegate.createAdvancedAssociation(name, nAry, associationClass);
		if (rAssoc == 0)
			return 0;
		
		long rsAssoc = mapper.getStableReference(rAssoc);
		
		actions.add(new AssociationAction(delegate, mapper, rsAssoc, name, nAry, associationClass, true));
		
		return rAssoc;
	}
	
	@Override
	public boolean deleteAssociation (long rAssociationEndOrAdvancedAssociation)
	{
		if (undone) {
			clear();
		}
		
		if (delegate.isAdvancedAssociation(rAssociationEndOrAdvancedAssociation)) {
			// advanced association
			
			String name = delegate.getClassName(rAssociationEndOrAdvancedAssociation);
			long rs = mapper.getStableReference(rAssociationEndOrAdvancedAssociation);
			
			boolean retVal = delegate.deleteAssociation(rAssociationEndOrAdvancedAssociation);
			if (retVal) {
				mapper.redirectStableReference(rs, 0);
				actions.add(new AssociationAction(delegate, mapper, rs, name, true/*TODO:nAry*/, true/*TODO:associationClass*/, false));
			}
			else
				mapper.releaseStableReference(rs);
			
			return retVal;
		}
		else {
			// ordinary association
			
			long rInv = delegate.getInverseAssociationEnd(rAssociationEndOrAdvancedAssociation);
			if (rInv!=0) {				
				// bi-directional
				
				long rSrcCls = delegate.getSourceClass(rAssociationEndOrAdvancedAssociation);
				long rTgtCls = delegate.getTargetClass(rAssociationEndOrAdvancedAssociation);
				
				long rsAssoc = mapper.getStableReference(rAssociationEndOrAdvancedAssociation);
				long rsInv = mapper.getStableReference(rInv);
				delegate.freeReference(rInv);
				long rsSourceClass = mapper.getStableReference(rSrcCls);
				delegate.freeReference(rSrcCls);
				long rsTargetClass = mapper.getStableReference(rTgtCls);
				delegate.freeReference(rTgtCls);
				
				String targetRoleName = delegate.getRoleName(rAssociationEndOrAdvancedAssociation);
				String sourceRoleName = delegate.getRoleName(rInv);
				
				boolean isComposition = delegate.isComposition(rAssociationEndOrAdvancedAssociation);
				
				if (delegate.deleteAssociation(rAssociationEndOrAdvancedAssociation)) {
					mapper.redirectStableReference(rsAssoc, 0);
					mapper.redirectStableReference(rsInv, 0);
					actions.add(new AssociationAction(delegate, mapper, rsAssoc, rsInv, rsSourceClass, rsTargetClass, sourceRoleName, targetRoleName, isComposition, false));
					return true;
				}
				else {
					mapper.releaseStableReference(rsAssoc);
					mapper.releaseStableReference(rsInv);
					mapper.releaseStableReference(rsSourceClass);
					mapper.releaseStableReference(rsTargetClass);
					return false;
				}
				
			}
			else {
				// directed
				
				long rSrcCls = delegate.getSourceClass(rAssociationEndOrAdvancedAssociation);
				long rTgtCls = delegate.getTargetClass(rAssociationEndOrAdvancedAssociation);
				
				long rsAssoc = mapper.getStableReference(rAssociationEndOrAdvancedAssociation);
				long rsSourceClass = mapper.getStableReference(rSrcCls);
				delegate.freeReference(rSrcCls);
				long rsTargetClass = mapper.getStableReference(rTgtCls);
				delegate.freeReference(rTgtCls);
				
				String targetRoleName = delegate.getRoleName(rAssociationEndOrAdvancedAssociation);
				
				boolean isComposition = delegate.isComposition(rAssociationEndOrAdvancedAssociation);
				
				if (delegate.deleteAssociation(rAssociationEndOrAdvancedAssociation)) {
					mapper.redirectStableReference(rsAssoc, 0);
					actions.add(new AssociationAction(delegate, mapper, rsAssoc, 0, rsSourceClass, rsTargetClass, null, targetRoleName, isComposition, false));
					return true;
				}
				else {
					mapper.releaseStableReference(rsAssoc);
					mapper.releaseStableReference(rsSourceClass);
					mapper.releaseStableReference(rsTargetClass);
					return false;
				}
			}
		}
	}
	
	
	private class LinkAction extends RepositoryAction {
		long rsSourceObjectReference;
		long rsTargetObjectReference;
		long rsAssociationReference;
		int position; // if -1, then the end of the linked object list
		LinkAction(RAAPI _raapi, IReferenceMapper _mapper, long _rsSourceObjectReference, long _rsTargetObjectReference, long _rsAssociationReference, int _position, boolean _created) {
			super(_raapi, _mapper, _created);
			rsSourceObjectReference = _rsSourceObjectReference;
			rsTargetObjectReference = _rsTargetObjectReference;
			rsAssociationReference = _rsAssociationReference;
			position = _position;
		}
		@Override
		boolean recreate() {
			if (position == -1) {
				// info: we do not take into a consideration the target position, since
				// we assume that the cascade delete layers temporary deletes further links, then 
				// deletes this link, then restores further links
				boolean retVal = raapi.createLink(mapper.getUnstableReference(rsSourceObjectReference), mapper.getUnstableReference(rsTargetObjectReference), mapper.getUnstableReference(rsAssociationReference));			
				return retVal;
			}
			else
				return raapi.createOrderedLink(mapper.getUnstableReference(rsSourceObjectReference), mapper.getUnstableReference(rsTargetObjectReference), mapper.getUnstableReference(rsAssociationReference), position);
		}
		@Override
		boolean redelete() {
			boolean retVal = raapi.deleteLink(mapper.getUnstableReference(rsSourceObjectReference), mapper.getUnstableReference(rsTargetObjectReference), mapper.getUnstableReference(rsAssociationReference));			
			return retVal;
		}
		@Override
		void free() {
			mapper.releaseStableReference(rsSourceObjectReference);
			mapper.releaseStableReference(rsTargetObjectReference);
			mapper.releaseStableReference(rsAssociationReference);
		}		
	}	
	

	@Override
	public boolean createLink(long rSourceObject, long rTargetObject, long rAssociationEnd)
	{
		if (undone) {
			clear();
		}
		
		
		if (!delegate.createLink(rSourceObject, rTargetObject, rAssociationEnd))
			return false;
		
		// storing the link creation...		
		long rsSourceObject = mapper.getStableReference(rSourceObject);
		long rsAssociationEnd = mapper.getStableReference(rAssociationEnd);
		long rsTargetObject = mapper.getStableReference(rTargetObject);
		
		
		actions.add(new LinkAction(delegate, mapper, rsSourceObject, rsTargetObject, rsAssociationEnd, -1, true));
		
		return true;		
	}
	
	@Override
	public boolean createOrderedLink (long rSourceObject, long rTargetObject, long rAssociationEnd, int targetPosition)
	{
		if (undone) {
			clear();
		}
		
		
		if (!delegate.createOrderedLink(rSourceObject, rTargetObject, rAssociationEnd, targetPosition))
			return false;
		
		// storing the link creation...		
		long rsSourceObject = mapper.getStableReference(rSourceObject);
		long rsAssociationEnd = mapper.getStableReference(rAssociationEnd);
		long rsTargetObject = mapper.getStableReference(rTargetObject);
		
		
		actions.add(new LinkAction(delegate, mapper, rsSourceObject, rsTargetObject, rsAssociationEnd, targetPosition, true));
		
		return true;		
	}
	
	@Override
	public boolean deleteLink(long rSourceObject, long rTargetObject, long rAssociationEnd)
	{
		if (undone) {
			clear();
		}
		
		if (!delegate.deleteLink(rSourceObject, rTargetObject, rAssociationEnd))
			return false;
				
		assert (!linkExists(rSourceObject, rTargetObject, rAssociationEnd));
		
		// storing the actual link deletion...		
		long rsSourceObject = mapper.getStableReference(rSourceObject);
		long rsAssociationEnd = mapper.getStableReference(rAssociationEnd);
		long rsTargetObject = mapper.getStableReference(rTargetObject);
		actions.add(new LinkAction(delegate, mapper, rsSourceObject, rsTargetObject, rsAssociationEnd, -1, false));
		
		return true;		
	}

	public interface IExternalAction {
		boolean undo();		
		boolean redo();
		void free();
	}
	
	private class ExternalActionWrapper extends RepositoryAction {
		IExternalAction action;
		
		ExternalActionWrapper(IExternalAction _a)
		{
			super(null, null, true);
			action = _a;
		}

		@Override
		boolean recreate() {
			return action.redo();
		}

		@Override
		boolean redelete() {
			return action.undo();
		}

		@Override
		void free() {
			action.free();
		}		
	}
	
	public void registerExternalAction(IExternalAction a)
	{
		if (undone) {
			clear();
		}
		
		actions.add(new ExternalActionWrapper(a));
	}
/*	
	public boolean registerUndoStreamState(long rStreamState)
	{
		return false;
	}
	
	public boolean registerUndoableExternalAction(long rModificatingAction)
	{
		return false;
	}*/
	
	public boolean isUndone()
	{
		return undone;
	}
	
	public boolean undo()
	{
		if (undone)
			return actions.isEmpty();
		
		undone = true; // for reentrant or recursive calls
		
		boolean ok = true;
		for (int i=actions.size()-1; i>=0; i--) {
			if (!actions.get(i).undo()) {
				ok = false;
			}
		}
		return ok;
	}	
	
	public boolean redo()
	{
		if (!undone)
			return actions.isEmpty();
		
		undone = false; // for reentrant or recursive calls
		
		boolean ok = true;
		for (int i=0; i<actions.size(); i++) {
			if (!actions.get(i).redo()) {
				ok = false;
			}
		}
		return ok;		
	}
	
	public void clear()
	{
		for (RepositoryAction a : actions) {
			a.free();		
		}
		actions.clear();
		undone = false;
	}

	@Override
	public boolean exists(String location) {
		if (delegate2!=null)
			return delegate2.exists(location);
		else
			return false;
	}

	@Override
	public boolean open(String location) {
		if (delegate2!=null)
			return delegate2.open(location);
		else
			return false;
	}

	@Override
	public void close() {
		if (delegate2!=null)
			delegate2.close();
	}

	@Override
	public boolean startSave() {
		if (delegate2!=null)
			return delegate2.startSave();
		else
			return false;
	}

	@Override
	public boolean finishSave() {
		if (delegate2!=null)
			return delegate2.finishSave();
		else
			return false;
	}

	@Override
	public boolean cancelSave() {
		if (delegate2!=null)
			return delegate2.cancelSave();
		else
			return false;
	}

	@Override
	public boolean drop(String location) {
		if (delegate2!=null)
			return delegate2.drop(location);
		else
			return false;
	}

}
