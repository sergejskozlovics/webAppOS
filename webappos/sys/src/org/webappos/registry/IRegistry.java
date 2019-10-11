package org.webappos.registry;

import com.google.gson.JsonElement;

/**
 * webAppOS Registry API.
 * Used by webAppOS apps to access Registry. In addition, Registry drivers implement this API to provide
 * access to a remote registry that can be mounted into the "users/[login]" branch.
 * 
 * @author Sergejs Kozlovics
 *
 */
public interface IRegistry {
	/**
	 * Gets the value of the given registry key. If the key is pointing to a final node, 
	 * a value of some primitive type is returned. Otherwise, a JSON object representing the registry subtree is returned.
	 * @param key a registry key specified as the path starting with one of the predefined
	 * registry root keys ("users", "xusers", "apps", etc.) and delimited by "/";
	 * as a special key, one can use #xusers to obtain the number of documents in the xusers registry branch 
	 * @return a JSON element (the final element or a branch); null, if the registry key not found or an error occurred
	 */
	public JsonElement getValue(String key);	
	/**
	 * Sets the given registry value. The value if overwritten. If the key is pointing to a subtree,
	 * the whole subtree is overwritten with the new value (which can be a final value or a subtree).
	 * @param key a registry key specified as the path starting with one of the predefined
	 * registry root keys ("users", "xusers", "apps", etc.) and delimited by "/" 
	 * @param value a final value or a JSON object
	 * @return whether the operation succeeded
	 */
	public boolean setValue(String key, Object value);
}
