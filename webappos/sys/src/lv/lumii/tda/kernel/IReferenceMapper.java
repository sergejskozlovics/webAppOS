package lv.lumii.tda.kernel;

/**
 * Defines a (callback) interface for redirecting RAAPI references. 
 * It is used by RAAPITransaction to redirect references to the re-created elements during undo/redo.
 * It is implemented by ProxyReferenceMapper (a part of Delegator6WithProxyReferences),
 * which will redirect proxy references to re-created domestic elements.  
 * 
 * @author Sergejs Kozlovics
 *
 */
public interface IReferenceMapper {

	/**
	 * Obtains a stable reference for the given unstable reference r, which can be later
	 * redirected to another unstable reference.
	 * The counter of the stable reference is incremented by 1. To release the stable reference,
	 * user releaseStableReference.
	 * @param r an unstable reference, for which to obtain a stable reference
	 * @return a stable reference, or 0 on error
	 */
	long getStableReference(long r);
	
	/**
	 * Returns the current unstable reference associated with the given stable reference.
	 * @param rStableReference a stable reference
	 * @return the current unstable reference (it may change during time)
	 */
	long getUnstableReference(long rStableReference);
	
	/**
	 * Redirects the given old stable reference to the given unstable reference.
	 * After that, the old stable reference will point to the element referenced by the new unstable reference.
	 * @param rOldStableReference the old stable reference
	 * @param rNewUnstableReference the new unstable reference, or 0, if the unstable reference needs to be detached
	 * @return whether the operation succeeded
	 */
	boolean redirectStableReference(long rOldStableReference, long rNewUnstableReference);

	/**
	 * Increments the counter of the given (previously obtained) stable reference.
	 * @param rStableReference a reference for which to increment the counter
	 */
	//void keepStableReference(long rStableReference);
	
	/**
	 * Decrements the counter of the given (previously obtained) stable reference.
	 * @param rStableReference a reference for which to decrement the counter
	 */
	void releaseStableReference(long rStableReference);
}
