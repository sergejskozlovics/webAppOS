package lv.lumii.tda.raapi;

/**
 * RAAPI extended to support writable references, e.g., a class with a specified reference can be created
 *
 */
public interface RAAPI_WR extends RAAPI {
	long getMaxReference();
	void switchToEvenReferences();
	
	boolean createClass(String name, long r);
	boolean createObject(long rClass, long r);
	boolean createAttribute(long rClass, String name, long rType, long r);
	boolean createAssociation(long rSourceClass, long rTargetClass,
			String sourceRole, String targetRole, boolean isComposition, long r, long rInv);
	boolean createDirectedAssociation(long rSourceClass, long rTargetClass,
			String targetRole, boolean isComposition, long r);
	boolean createAdvancedAssociation(String name, boolean nAry,
			boolean associationClass, long r);
}
