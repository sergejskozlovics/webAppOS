package org.webappos.properties;

import java.util.List;

/**
 * A class for accessing properties of installed webAppOS apps, engines, and services.
 * Used by the server-side bridge and by server-side web processors. 
 * @author Sergejs Kozlovics
 *
 */
public interface IPropertiesManager {
	public String getAppDirectory(String appName);
	public AppProperties[] getAllInstalledApps();
	public AppProperties[] getAvailableApps(String login);
	public AppProperties getAppPropertiesByFullName(String appName);
	public ServiceProperties getServicePropertiesByFullName(String serviceName);
	public EngineProperties getEnginePropertiesByEngineName(String engineName);
	public SomeProperties getPropertiesById(String id);	
	public List<AppProperties> getAppPropertiesByExtension(String extension);
	public AppProperties getAppPropertiesByUrlName(String urlName);
}
