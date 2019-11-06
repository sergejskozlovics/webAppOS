package lv.lumii.tda.raapi;

/**
 * RAAPI extended to support writable references, e.g., a class with a specified reference can be created
 *
 */
public interface RAAPI_WR extends RAAPI {
	long getMaxReference();
	boolean setPredefinedBits(int bitsCount, long bitsValues);
	int getPredefinedBitsCount();
	long getPredefinedBitsValues();
	
	/**
	 * Splits the set of references into two parts: the bit number getPredefinedBitsCount() will be 0
	 * for our references and 1 for references of the second group.
	 * After that, the predefined bits count is increased.
	 * @return the bits values for the other part or -1 on error
	 */
	default long splitReferences() {
		int current = getPredefinedBitsCount();
		if (current >= 50)
			return -1;
		
		long values = getPredefinedBitsValues();
		setPredefinedBits(current+1, values);
			// do not set the bit number getPredefinedBitsCount() to zero, since it is already zero
		return values | (1<<current);
	}
	
	boolean createClass(String name, long r);
	boolean createObject(long rClass, long r);
	boolean createAttribute(long rClass, String name, long rType, long r);
	boolean createAssociation(long rSourceClass, long rTargetClass,
			String sourceRole, String targetRole, boolean isComposition, long r, long rInv);
	boolean createDirectedAssociation(long rSourceClass, long rTargetClass,
			String targetRole, boolean isComposition, long r);
	boolean createAdvancedAssociation(String name, boolean nAry,
			boolean associationClass, long r);
	
	default boolean setSynchronizedAttributeValue(long rObject, long rAttribute, String value, String oldValue) {
		synchronized (this) {
			// assigns min(current value, synced value)
			String curValue = getAttributeValue(rObject, rAttribute);
			if (curValue == null) {
				if (oldValue == null)
					return setAttributeValue(rObject, rAttribute, value);
				else
					return false; // keeping null, since null<value
			}
			else {
				if (curValue.equals(oldValue))
					return setAttributeValue(rObject, rAttribute, value);
				else
					if (value.compareTo(curValue)<0)
						return setAttributeValue(rObject, rAttribute, value);
					else
						return false; // keeping curValue
			}					
		}
	}
	
}
