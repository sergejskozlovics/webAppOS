package org.webappos.adapters.webcalls.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import lv.lumii.tda.raapi.RAAPI;

public class Module_lua_raapi extends TwoArgFunction {
	
	public Module_lua_raapi LIB = null;
	private RAAPI raapi = null;
	
    public Module_lua_raapi(RAAPI _raapi) {
        LIB = this;
        raapi = _raapi;
    }

	@Override
	public LuaValue call(LuaValue modName, LuaValue env) {
		LuaTable module = new LuaTable(0,30); // I think "new LuaTable()" instead of "(0, 30)" is OK

		// RAAPI functions
		module.set("findClass", new raapi_findClass());
		module.set("getClassName", new raapi_getClassName());
		module.set("createObject", new raapi_createObject());
		module.set("deserializeReference", new raapi_deserializeReference());
		module.set("createLink", new raapi_createLink());
		module.set("deleteObject", new raapi_deleteObject());
		module.set("isTypeOf", new raapi_isTypeOf());
		module.set("findAttribute", new raapi_findAttribute());
		module.set("deleteClass", new raapi_deleteClass());
		module.set("isClass", new raapi_isClass());
		module.set("isDirectSubClass", new raapi_isDirectSubClass());
		module.set("isKindOf", new raapi_isKindOf());
		module.set("isDerivedClass", new raapi_isDerivedClass());
		module.set("deleteAttribute", new raapi_deleteAttribute());
		module.set("getAttributeType", new raapi_getAttributeType());
		module.set("isAttribute", new raapi_isAttribute());
		module.set("moveObject", new raapi_moveObject());
		module.set("getTargetClass", new raapi_getTargetClass());
		module.set("getRoleName", new raapi_getRoleName());
		module.set("createClass", new raapi_createClass());
		module.set("getSourceClass", new raapi_getSourceClass());
		module.set("createAttribute", new raapi_createAttribute());
		module.set("isComposition", new raapi_isComposition());
		module.set("getAttributeName", new raapi_getAttributeName());
		module.set("isAssociationEnd", new raapi_isAssociationEnd());
		module.set("deleteLink", new raapi_deleteLink());
		module.set("freeIterator", new raapi_freeIterator());
		module.set("resolveIterator", new raapi_resolveIterator());
		module.set("isLinguistic", new raapi_isLinguistic());
		module.set("linkExists", new raapi_linkExists());
		module.set("freeReference", new raapi_freeReference());
		module.set("callSpecificOperation", new raapi_callSpecificOperation());
		module.set("getLinguisticClassFor", new raapi_getLinguisticClassFor());
		module.set("getIteratorForLinguisticClasses", new raapi_getIteratorForLinguisticClasses());
		module.set("findPrimitiveDataType", new raapi_findPrimitiveDataType());
		module.set("includeObjectInClass", new raapi_includeObjectInClass());
		module.set("getIteratorForDirectSubClasses", new raapi_getIteratorForDirectSubClasses());
		module.set("isPrimitiveDataType", new raapi_isPrimitiveDataType());
		module.set("getIteratorForAllClassObjects", new raapi_getIteratorForAllClassObjects());
		module.set("createGeneralization", new raapi_createGeneralization());
		module.set("getIteratorForAllAttributes", new raapi_getIteratorForAllAttributes());
		module.set("getIteratorForClasses", new raapi_getIteratorForClasses());
		module.set("getPrimitiveDataTypeName", new raapi_getPrimitiveDataTypeName());
		module.set("getIteratorForDirectClassObjects", new raapi_getIteratorForDirectClassObjects());
		module.set("getIteratorForDirectSuperClasses", new raapi_getIteratorForDirectSuperClasses());
		module.set("excludeObjectFromClass", new raapi_excludeObjectFromClass());
		module.set("getIteratorForDirectAttributes", new raapi_getIteratorForDirectAttributes());
		module.set("deleteGeneralization", new raapi_deleteGeneralization());
		module.set("getAttributeDomain", new raapi_getAttributeDomain());
		module.set("setAttributeValue", new raapi_setAttributeValue());
		module.set("deleteAttributeValue", new raapi_deleteAttributeValue());
		module.set("createOrderedLink", new raapi_createOrderedLink());
		module.set("deleteAssociation", new raapi_deleteAssociation());
		module.set("getIteratorForLinkedObjects", new raapi_getIteratorForLinkedObjects());
		module.set("getLinkedObjectPosition", new raapi_getLinkedObjectPosition());
		module.set("resolveIteratorFirst", new raapi_resolveIteratorFirst());
		module.set("resolveIteratorNext", new raapi_resolveIteratorNext());
		module.set("getIteratorLength", new raapi_getIteratorLength());
		module.set("serializeReference", new raapi_serializeReference());
		module.set("getAttributeValue", new raapi_getAttributeValue());
		module.set("createAssociation", new raapi_createAssociation());
		module.set("getInverseAssociationEnd", new raapi_getInverseAssociationEnd());
		module.set("createDirectedAssociation", new raapi_createDirectedAssociation());
		module.set("isAdvancedAssociation", new raapi_isAdvancedAssociation());
		module.set("createAdvancedAssociation", new raapi_createAdvancedAssociation());
		module.set("findAssociationEnd", new raapi_findAssociationEnd());
		module.set("getIteratorForDirectOutgoingAssociationEnds", new raapi_getIteratorForDirectOutgoingAssociationEnds());
		module.set("getIteratorForAllIngoingAssociationEnds", new raapi_getIteratorForAllIngoingAssociationEnds());
		module.set("getIteratorForAllLinguisticInstances", new raapi_getIteratorForAllLinguisticInstances());
		module.set("getIteratorForObjectsByAttributeValue", new raapi_getIteratorForObjectsByAttributeValue());
		module.set("getIteratorForDirectLinguisticInstances", new raapi_getIteratorForDirectLinguisticInstances());
		module.set("getIteratorForDirectIngoingAssociationEnds", new raapi_getIteratorForDirectIngoingAssociationEnds());
		module.set("getIteratorForAllOutgoingAssociationEnds", new raapi_getIteratorForAllOutgoingAssociationEnds());
		module.set("getIteratorForDirectObjectClasses", new raapi_getIteratorForDirectObjectClasses());
		
		// MII_REP functions would be
		
        /*module.set("GetTypeName", new miirep_GetTypeName());

        module.set("GetObjectTypeIdByName", new miirep_GetObjectTypeIdByName());
        module.set("GetObjectTypeIdList", new miirep_GetObjectTypeIdList());
        module.set("GetPropertyTypeIdList", new miirep_GetPropertyTypeIdList());
        module.set("GetLinkTypeIdList", new miirep_GetLinkTypeIdList());
        module.set("GetExtendsId", new miirep_GetExtendsId());
        module.set("ExtendsExtends", new miirep_ExtendsExtends());
        module.set("GetExtensionIdList", new miirep_GetExtensionIdList());

        module.set("GetPropertyTypeIdByName", new miirep_GetPropertyTypeIdByName());

        module.set("GetLinkTypeIdByName", new miirep_GetLinkTypeIdByName());
        module.set("GetInverseLinkTypeId", new miirep_GetInverseLinkTypeId());
        module.set("GetLinkTypeAttributes", new miirep_GetLinkTypeAttributes());

        module.set("GetObjectNum", new miirep_GetObjectNum());
        module.set("GetObjectIdByIndex", new miirep_GetObjectIdByIndex());
        module.set("ObjectExists", new miirep_ObjectExists());
        module.set("GetObjectTypeId", new miirep_GetObjectTypeId());
        module.set("GetPropertyValue", new miirep_GetPropertyValue());
        module.set("GetLinkedObjectNum", new miirep_GetLinkedObjectNum());
        module.set("GetLinkedObjectIdByIndex", new miirep_GetLinkedObjectIdByIndex());
        module.set("AlreadyConnected", new miirep_AlreadyConnected());
        module.set("GetObjectIdByPropertyValue", new miirep_GetObjectIdByPropertyValue());

        module.set("CreateObject", new miirep_CreateObject());
        module.set("AddProperty", new miirep_AddProperty());
        module.set("DeleteProperty", new miirep_DeleteProperty());
        module.set("DeleteObjectHard", new miirep_DeleteObjectHard());
        module.set("CreateLink", new miirep_CreateLink());
        module.set("DeleteLink", new miirep_DeleteLink());

        module.set("EnableUndo", new miirep_EnableUndo());
        module.set("DisableUndo", new miirep_DisableUndo());
        module.set("ClearHistory", new miirep_ClearHistory());

        module.set("CreateObjectType", new miirep_CreateObjectType());
        module.set("AddPropertyType", new miirep_AddPropertyType());
        module.set("CreatePropertyType1", new miirep_CreatePropertyType1());
        module.set("GetObjectTypeAttributes1", new miirep_GetObjectTypeAttributes1());
        module.set("UpdateObjectType1",	new miirep_UpdateObjectType1());
        module.set("CreateLinkType", new miirep_CreateLinkType());

        module.set("DeleteObjectType", new miirep_DeleteObjectType());
        module.set("RemovePropertyType", new miirep_RemovePropertyType());
        module.set("DeleteLinkType", new miirep_DeleteLinkType());*/

        return module;		
	}

	class raapi_findClass extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.findClass(arg0.toString()));
		}

	}



	class raapi_getClassName extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getClassName(arg0.tolong()));
		}

	}



	class raapi_createObject extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.createObject(arg0.tolong()));
		}

	}



	class raapi_deserializeReference extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.deserializeReference(arg0.toString()));
		}

	}



	class raapi_resolveIterator extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.resolveIterator(arg0.tolong(), arg1.toint()));
		}

	}



	class raapi_linkExists extends ThreeArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2) {
			return LuaValue.valueOf(raapi.linkExists(arg0.tolong(), arg1.tolong(), arg2.tolong()));
		}

	}



	class raapi_freeIterator extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			raapi.freeIterator(Long.parseLong(arg0.toString()));
			return null;
		}

	}



	class raapi_deleteLink extends ThreeArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2) {
			return LuaValue.valueOf(raapi.deleteLink(arg0.tolong(), arg1.tolong(), arg2.tolong()));
		}

	}



	class raapi_isLinguistic extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.isLinguistic(arg0.tolong()));
		}

	}



	class raapi_freeReference extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			raapi.freeReference(arg0.tolong());
			return null;
		}

	}



	class raapi_getIteratorForLinguisticClasses extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForLinguisticClasses());
		}

	}



	class raapi_getLinguisticClassFor extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getLinguisticClassFor(arg0.tolong()));
		}

	}



	class raapi_callSpecificOperation extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.callSpecificOperation(arg0.toString(), arg1.toString()));
		}

	}



	class raapi_moveObject extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.moveObject(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_deleteAttribute extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.deleteAttribute(arg0.tolong()));
		}

	}



	class raapi_createLink extends ThreeArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2) {
			return LuaValue.valueOf(raapi.createLink(arg0.tolong(), arg1.tolong(), arg2.tolong()));
		}

	}



	class raapi_isTypeOf extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.isTypeOf(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_findAttribute extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.findAttribute(arg0.tolong(), arg1.toString()));
		}

	}



	class raapi_getRoleName extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getRoleName(arg0.tolong()));
		}

	}



	class raapi_isAttribute extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.isAttribute(arg0.tolong()));
		}

	}



	class raapi_createClass extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.createClass(arg0.toString()));
		}

	}



	class raapi_deleteClass extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.deleteClass(arg0.tolong()));
		}

	}



	class raapi_isClass extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.isClass(arg0.tolong()));
		}

	}



	class raapi_isDerivedClass extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.isDerivedClass(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_isKindOf extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.isKindOf(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_createAttribute extends ThreeArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2) {
			return LuaValue.valueOf(raapi.createAttribute(arg0.tolong(), arg1.toString(), arg2.tolong()));
		}

	}



	class raapi_getSourceClass extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getSourceClass(arg0.tolong()));
		}

	}



	class raapi_isDirectSubClass extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.isDirectSubClass(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_getTargetClass extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getTargetClass(arg0.tolong()));
		}

	}



	class raapi_getAttributeName extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			if (arg0.tolong()==0)
				return LuaValue.NIL;
			return LuaValue.valueOf(raapi.getAttributeName(arg0.tolong()));
		}

	}



	class raapi_getAttributeType extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getAttributeType(arg0.tolong()));
		}

	}



	class raapi_isComposition extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.isComposition(arg0.tolong()));
		}

	}



	class raapi_isAssociationEnd extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.isAssociationEnd(arg0.tolong()));
		}

	}



	class raapi_deleteObject extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.deleteObject(arg0.tolong()));
		}

	}



	class raapi_getPrimitiveDataTypeName extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getPrimitiveDataTypeName(arg0.tolong()));
		}

	}



	class raapi_deleteGeneralization extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.deleteGeneralization(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_excludeObjectFromClass extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.excludeObjectFromClass(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_getIteratorForDirectClassObjects extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForDirectClassObjects(arg0.tolong()));
		}

	}



	class raapi_getIteratorForAllAttributes extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForAllAttributes(arg0.tolong()));
		}

	}



	class raapi_getAttributeDomain extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getAttributeDomain(arg0.tolong()));
		}

	}



	class raapi_findPrimitiveDataType extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.findPrimitiveDataType(arg0.toString()));
		}

	}



	class raapi_isPrimitiveDataType extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.isPrimitiveDataType(arg0.tolong()));
		}

	}



	class raapi_getIteratorForClasses extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForClasses());
		}

	}



	class raapi_createGeneralization extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.createGeneralization(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_getIteratorForAllClassObjects extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForAllClassObjects(arg0.tolong()));
		}

	}



	class raapi_getIteratorForDirectAttributes extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForDirectAttributes(arg0.tolong()));
		}

	}



	class raapi_getIteratorForDirectSubClasses extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForDirectSubClasses(arg0.tolong()));
		}

	}



	class raapi_setAttributeValue extends ThreeArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2) {
			return LuaValue.valueOf(raapi.setAttributeValue(arg0.tolong(), arg1.tolong(), arg2.toString()));
		}

	}



	class raapi_getIteratorForDirectSuperClasses extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForDirectSuperClasses(arg0.tolong()));
		}

	}



	class raapi_includeObjectInClass extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.includeObjectInClass(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_findAssociationEnd extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.findAssociationEnd(arg0.tolong(), arg1.toString()));
		}

	}



	class raapi_deleteAssociation extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.deleteAssociation(arg0.tolong()));
		}

	}



	class raapi_getInverseAssociationEnd extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getInverseAssociationEnd(arg0.tolong()));
		}

	}



	class raapi_getLinkedObjectPosition extends ThreeArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2) {
			return LuaValue.valueOf(raapi.getLinkedObjectPosition(arg0.tolong(), arg1.tolong(), arg2.tolong()));
		}

	}



	class raapi_serializeReference extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.serializeReference(arg0.tolong()));
		}

	}



	class raapi_resolveIteratorNext extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.resolveIteratorNext(arg0.tolong()));
		}

	}



	class raapi_isAdvancedAssociation extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.isAdvancedAssociation(arg0.tolong()));
		}

	}



	class raapi_createDirectedAssociation extends FourArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2, LuaValue arg3) {
			return LuaValue.valueOf(raapi.createDirectedAssociation(arg0.tolong(), arg1.tolong(), arg2.toString(), arg3.toboolean()));
		}

	}



	class raapi_createOrderedLink extends FourArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2, LuaValue arg3) {
			return LuaValue.valueOf(raapi.createOrderedLink(arg0.tolong(), arg1.tolong(), arg2.tolong(), arg3.toint()));
		}

	}



	class raapi_createAssociation extends FiveArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
			return LuaValue.valueOf(raapi.createAssociation(arg0.tolong(), arg1.tolong(), arg2.toString(), arg3.toString(), arg4.toboolean()));
		}

	}



	class raapi_getIteratorForLinkedObjects extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.getIteratorForLinkedObjects(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_resolveIteratorFirst extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			long r = raapi.resolveIteratorFirst(arg0.tolong());
			return LuaValue.valueOf(r);
		}

	}



	class raapi_getIteratorLength extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorLength(arg0.tolong()));
		}

	}



	class raapi_createAdvancedAssociation extends ThreeArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2) {
			return LuaValue.valueOf(raapi.createAdvancedAssociation(arg0.toString(), arg1.toboolean(), arg2.toboolean()));
		}

	}



	class raapi_deleteAttributeValue extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.deleteAttributeValue(arg0.tolong(), arg1.tolong()));
		}

	}



	class raapi_getAttributeValue extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			String retVal = raapi.getAttributeValue(arg0.tolong(), arg1.tolong());
			if (retVal == null)
				return LuaValue.NIL;
			else
				return LuaValue.valueOf(retVal);
		}

	}



	class raapi_getIteratorForAllIngoingAssociationEnds extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForAllIngoingAssociationEnds(arg0.tolong()));
		}

	}



	class raapi_getIteratorForDirectIngoingAssociationEnds extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForDirectIngoingAssociationEnds(arg0.tolong()));
		}

	}



	class raapi_getIteratorForDirectObjectClasses extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			long it = raapi.getIteratorForDirectObjectClasses(arg0.tolong());
			return LuaValue.valueOf(it);
		}

	}



	class raapi_getIteratorForDirectLinguisticInstances extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForDirectLinguisticInstances(arg0.tolong()));
		}

	}



	class raapi_getIteratorForObjectsByAttributeValue extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			return LuaValue.valueOf(raapi.getIteratorForObjectsByAttributeValue(arg0.tolong(), arg1.toString()));
		}

	}



	class raapi_getIteratorForAllOutgoingAssociationEnds extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForAllOutgoingAssociationEnds(arg0.tolong()));
		}

	}



	class raapi_getIteratorForDirectOutgoingAssociationEnds extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForDirectOutgoingAssociationEnds(arg0.tolong()));
		}

	}



	class raapi_getIteratorForAllLinguisticInstances extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue arg0) {
			return LuaValue.valueOf(raapi.getIteratorForAllLinguisticInstances(arg0.tolong()));
		}

	}


	abstract public class FourArgFunction extends LibFunction {

		abstract public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4);

		public FourArgFunction() {
		}

		public FourArgFunction(LuaValue env) {
		this .env = env;
		}

		@Override
		public final LuaValue call() {
		return call(NIL, NIL, NIL, NIL);
		}

		@Override
		public final LuaValue call(LuaValue arg) {
		return call(arg, NIL, NIL, NIL);
		}

		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2) {
		return call(arg1, arg2, NIL, NIL);
		}

		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return call(arg1, arg2, arg3, NIL);
		}


		@Override
		public Varargs invoke(Varargs varargs) {
			return call(varargs.arg1(), varargs.arg(2), varargs.arg(3), varargs.arg(4));
		}

	}	
	
	abstract public class FiveArgFunction extends LibFunction {

		abstract public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4, LuaValue arg5);

		public FiveArgFunction() {
		}

		public FiveArgFunction(LuaValue env) {
			this .env = env;
		}

		@Override
		public final LuaValue call() {
		return call(NIL, NIL, NIL, NIL, NIL);
		}

		@Override
		public final LuaValue call(LuaValue arg) {
		return call(arg, NIL, NIL, NIL, NIL);
		}

		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2) {
		return call(arg1, arg2, NIL, NIL, NIL);
		}

		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return call(arg1, arg2, arg3, NIL, NIL);
		}

		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
		return call(arg1, arg2, arg3, arg4, NIL);
		}

		@Override
		public Varargs invoke(Varargs varargs) {
			return call(varargs.arg1(), varargs.arg(2), varargs.arg(3), varargs.arg(4), varargs.arg(5));
		}

	}	
	
	abstract public class SevenArgFunction extends LibFunction {

		abstract public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4, LuaValue arg5, LuaValue arg6, LuaValue arg7);

		public SevenArgFunction() {
		}

		public SevenArgFunction(LuaValue env) {
		this .env = env;
		}

		@Override
		public final LuaValue call() {
		return call(NIL, NIL, NIL, NIL, NIL, NIL, NIL);
		}

		@Override
		public final LuaValue call(LuaValue arg) {
		return call(arg, NIL, NIL, NIL, NIL, NIL, NIL);
		}

		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2) {
		return call(arg1, arg2, NIL, NIL, NIL, NIL, NIL);
		}

		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return call(arg1, arg2, arg3, NIL, NIL, NIL, NIL);
		}

		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
		return call(arg1, arg2, arg3, arg4, NIL, NIL, NIL);
		}

		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4, LuaValue arg5) {
		return call(arg1, arg2, arg3, arg4, arg5, NIL, NIL);
		}

		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4, LuaValue arg5, LuaValue arg6) {
		return call(arg1, arg2, arg3, arg4, arg5, arg6, NIL);
		}

		@Override
		public Varargs invoke(Varargs varargs) {
		return call(varargs.arg1(), varargs.arg(2), varargs.arg(3), varargs.arg(4), varargs.arg(5), varargs.arg(6), varargs.arg(7));
		}

	}	
}
