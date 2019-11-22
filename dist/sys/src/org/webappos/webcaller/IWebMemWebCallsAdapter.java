package org.webappos.webcaller;

import org.webappos.webmem.IWebMemory;

/**
 * Interface for webAppOS server-side web calls adapters implementing the "webmemcall" calling conventions.
 * Each adapter must be implemented as Java class with the full name
 * org.webappos.adapters.webcalls.[adapter_name].WebCallsAdapter.
 *
 * The same adapter can implement {@link IJsonWebCallsAdapter} as well.
 * 
 * @author Sergejs Kozlovics
 *
 */
public interface IWebMemWebCallsAdapter {
	

	/**
	 * Performs a web call according to the "webmemcall" calling conventions.
	 * 
	 * @param resolvedLocation adapter-specific location of code to execute
	 * @param pwd the directory, where the .webcalls file was located
	 * @param rObject a repository object (usually, a command or an event)
	 * @param webmem a pointer to web memory
	 * @param project_id the current project_id
	 * @param appFullName the name of the current app
	 * @param login the current user login
	 */
	public void webmemcall(String resolvedLocation, String pwd, long rObject, IWebMemory webmem, String project_id, String appFullName, String login);

}
