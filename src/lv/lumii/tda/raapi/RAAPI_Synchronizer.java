package lv.lumii.tda.raapi;

import java.nio.ByteBuffer;

public interface RAAPI_Synchronizer { // may be used without prior cascade delete
	void syncCreateClass(String name, long rClass);
	void syncDeleteClass(long rClass);
	
	void syncCreateGeneralization(long rSub, long rSuper);
	void syncDeleteGeneralization(long rSub, long rSuper);
	
	void syncCreateObject(long rClass, long rObject);
	void syncDeleteObject(long rObject);
	
	void syncIncludeObjectInClass(long rObject, long rClass);
	void syncExcludeObjectFromClass(long rObject, long rClass);	
	void syncMoveObject(long rObject, long rClass);
	
	void syncCreateAttribute(long rClass, String name, long rPrimitiveType, long rAttr);
	void syncDeleteAttribute(long rAttr);
	
	void syncSetAttributeValue(long rObject, long rAttr, String value);
	void syncDeleteAttributeValue(long rObject, long rAttr);
	
	void syncCreateAssociation(long rSourceClass, long rTargetClass, String sourceRoleName, String targetRoleName, boolean isComposition, long rAssoc, long rInvAssoc);
	void syncCreateDirectedAssociation(long rSourceClass, long rTargetClass, String targetRoleName, boolean isComposition, long rAssoc);
	void syncCreateAdvancedAssociation(String name, boolean nAry, boolean associationClass, long rAssoc);
	void syncDeleteAssociation(long rAssoc);
	
	void syncCreateLink(long rSourceObject, long rTargetObject, long rAssociationEnd);
	void syncCreateOrderedLink(long rSourceObject, long rTargetObject, long rAssociationEnd, long targetPosition);
	void syncDeleteLink(long rSourceObject, long rTargetObject, long rAssociationEnd);

	void syncRawAction(double[] arr, String str);
	
	void syncBulk(double[] actions, String[] strings);
	void syncBulk(int nActions, double[] actions, int nStrings, String[] strings);
	void syncBulk(ByteBuffer actions, String delimitedStrings);
	
	void syncMaxReference(long r);
	void sendString(String s);		
}
