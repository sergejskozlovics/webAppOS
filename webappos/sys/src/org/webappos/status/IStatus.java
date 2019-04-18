package org.webappos.status;

/**
 * An interface for specifying status values. Status info is referenced by a string key (usually, a fully qualified name of some property).
 * The value of each key can be updated times at runtime to reflect the updated status.
 * @author Sergejs Kozlovics
 *
 */
public interface IStatus {
	public void setStatus(String key, String value);		
	public void setStatus(String key, String value, long expireSeconds);
}
