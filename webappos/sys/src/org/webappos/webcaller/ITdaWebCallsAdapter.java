package org.webappos.webcaller;

import lv.lumii.tda.raapi.RAAPI;

/**
 * Interface for webAppOS server-side web calls adapters implementing the "tdacall" calling conventions.
 * Each adapter must be implemented as Java class with the full name
 * org.webappos.adapters.webcalls.[adapter_name].WebCallsAdapter.
 *
 * The same adapter can implemented {@link IJsonWebCallsAdapter} as well.
 * 
 * @author Sergejs Kozlovics
 *
 */
public interface ITdaWebCallsAdapter {
	

	/**
	 * Performs a web call according to the "tdacall" calling conventions.
	 * 
	 * @param resolvedLocation adapter-specific location of code to execute
	 * @param rObject a repository object (usually, an instance of TDAKernel::Command or TDAKernel::Event)
	 * @param raapi a pointer to the underlying model repository implementing RAAPI
	 * @param project_id the current project_id
	 * @param appFullName the current webAppOS app
	 * @param login the current user login
	 */
	public void tdacall(String resolvedLocation, long rObject, RAAPI raapi, String project_id, String appFullName, String login);

}
