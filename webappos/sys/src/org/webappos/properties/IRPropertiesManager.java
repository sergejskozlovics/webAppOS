package org.webappos.properties;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRPropertiesManager extends Remote {
	public String getAppDirectory_R(String appName) throws RemoteException;
	public AppProperties[] getAllInstalledApps_R() throws RemoteException;
	public AppProperties[] getAvailableApps_R(String login) throws RemoteException;
	public AppProperties getAppPropertiesByFullName_R(String appName) throws RemoteException;
	public ServiceProperties getServicePropertiesByFullName_R(String serviceName) throws RemoteException;
	public EngineProperties getEnginePropertiesByEngineName_R(String engineName) throws RemoteException;
	public SomeProperties getPropertiesById_R(String id) throws RemoteException;
	public List<AppProperties> getAppPropertiesByExtension_R(String extension) throws RemoteException;
	public AppProperties getAppPropertiesByUrlName_R(String urlName) throws RemoteException;
}
