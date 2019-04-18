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
	 * Returns the user's login by the given e-mail (or login alias).
	 * In webAppOS, the user can specify only login, only e-mail, or both.
	 * In the latter case, the e-mail becomes a login alias. If the user specifies this e-mail,
	 * it must be converted to login, thus, getUserLogin comes in handy.
	 * 
	 * @param emailOrLogin user's e-mail, true login, or alias 
	 * @return the true login
	 */
	//public String getUserLogin(String emailOrLogin);
	/**
	 * Gets the value of the given registry key. If the key is pointing to a final node, 
	 * a value of some primitive type is returned. Otherwise, a JSON object representing the registry subtree is returned.
	 * @param key a registry key specified as the path starting with one of the predefined
	 * registry root keys ("users", "xusers", "apps", etc.) and delimited by "/" 
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
