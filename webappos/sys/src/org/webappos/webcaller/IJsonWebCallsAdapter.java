package org.webappos.webcaller;


/**
 * Interface for webAppOS server-side web calls adapters implementing the "jsoncall" calling conventions.
 * Each adapter must be implemented as Java class with the full name
 * org.webappos.adapters.webcalls.[adapter_name].WebCallsAdapter.
 *
 * The same adapter can implemented {@link ITdaWebCallsAdapter} as well.
 * 
 * @author Sergejs Kozlovics
 *
 */
public interface IJsonWebCallsAdapter {
	
	/**
	 * Performs a web call according to the "jsoncall" calling conventions.
	 * 
	 * @param resolvedLocation adapter-specific location of code to execute
	 * @param arg a string argument or a stringified JSON
	 * @param project_id current webAppOS project ID (null for static calls)
	 * @param appFullName the current webAppOS app name
	 * @param login the current webAppOS user login (null for public calls)
	 * @return the stringified JSON object contatining the call result; in case of error,
	 * either the JSON "error" attribute has to be specified, or null returned
	 */
	public String jsoncall(String resolvedLocation, String arg, String project_id, String appFullName, String login);

}
