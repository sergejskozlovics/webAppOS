package org.webappos.status;

import com.google.gson.JsonElement;

/**
 * An interface for specifying status values. Status info is referenced by a string key (usually, a fully qualified name of some property).
 * The value of each key can be updated several times at runtime to reflect the updated status.
 * @author Sergejs Kozlovics
 *
 */
public interface IStatus {
	/**
	 * Gets the value of the given status key.
	 * @param key a key specified as the path (e.g., "server/pid")
	 * @return a JSON element (the final element or a branch); null, if the key not found or an error occurred
	 */
	public JsonElement getValue(String key);	
	/**
	 * Sets the given status value. The old value if overwritten. If the key is pointing to a subtree,
	 * the whole subtree is overwritten with the new value (which can be a final value or a subtree).
	 * @param key a registry key specified as the path (e.g., "server/pid") 
	 * @param value a final value or a JSON object
	 * @return whether the operation succeeded
	 */
	public boolean setValue(String key, Object value);
}
