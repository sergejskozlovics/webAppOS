package org.webappos.properties;

import java.util.List;

/**
 * A class for accessing properties of installed webAppOS apps, libraries, and services.
 * Used by the server-side bridge and by server-side web processors. 
 * @author Sergejs Kozlovics
 *
 */
public interface IPropertiesManager {
	public WebAppProperties[] getAllInstalledWebApps();
	public WebAppProperties[] getAvailableWebApps(String login);
	public WebAppProperties getWebAppPropertiesByFullName(String appName);
	public WebServiceProperties getWebServicePropertiesByFullName(String serviceName);
	public WebLibraryProperties getWebLibraryPropertiesByFullName(String libraryName);
	public SomeProperties getPropertiesByFullName(String fullName);	
	public List<WebAppProperties> getWebAppPropertiesByExtension(String extension);
	public WebAppProperties getWebAppPropertiesByUrlName(String urlName);
	
}
