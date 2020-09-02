package org.webappos.properties;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRPropertiesManager extends Remote {
	public WebAppProperties[] getAllInstalledWebApps_R() throws RemoteException;
	public WebServiceProperties[] getAllInstalledWebServices_R() throws RemoteException;
	public WebAppProperties[] getAvailableWebApps_R(String login) throws RemoteException;
	public WebAppProperties getWebAppPropertiesByFullName_R(String appName) throws RemoteException;
	public WebServiceProperties getWebServicePropertiesByFullName_R(String serviceName) throws RemoteException;
	public WebLibraryProperties getWebLibraryPropertiesByFullName_R(String libraryName) throws RemoteException;
	public SomeProperties getPropertiesByFullName_R(String id) throws RemoteException;
	public List<WebAppProperties> getWebAppPropertiesByExtension_R(String extension) throws RemoteException;
	public WebAppProperties getWebAppPropertiesByUrlName_R(String urlName) throws RemoteException;
}
