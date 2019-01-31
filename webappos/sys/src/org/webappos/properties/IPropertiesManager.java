package org.webappos.properties;

import java.util.List;

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
