package org.webappos.properties;

import java.rmi.RemoteException;
import java.util.List;

public class IPropertiesManagerWrapper implements IPropertiesManager {
	
	private IRPropertiesManager delegate;
	
	public IPropertiesManagerWrapper(IRPropertiesManager _delegate) {
		delegate = _delegate;
	}

	public WebAppProperties[] getAllInstalledWebApps() {
		try {
			return delegate.getAllInstalledWebApps_R();
		} catch (RemoteException e) {
			return new WebAppProperties[] {};
		}
	}
	
	public WebServiceProperties[] getAllInstalledWebServices() {
		try {
			return delegate.getAllInstalledWebServices_R();
		} catch (RemoteException e) {
			return new WebServiceProperties[] {};
		}
	}

	public WebAppProperties[] getAvailableWebApps(String login) {
		try {
			return delegate.getAvailableWebApps_R(login);
		} catch (RemoteException e) {
			return new WebAppProperties[] {};
		}
	}

	public WebAppProperties getWebAppPropertiesByFullName(String appName) {
		try {
			return delegate.getWebAppPropertiesByFullName_R(appName);
		} catch (RemoteException e) {
			return null;
		}
	}

	public WebServiceProperties getWebServicePropertiesByFullName(String serviceName) {
		try {
			return delegate.getWebServicePropertiesByFullName_R(serviceName);
		} catch (RemoteException e) {
			return null;
		}
	}

	public WebLibraryProperties getWebLibraryPropertiesByFullName(String engineName) {
		try {
			return delegate.getWebLibraryPropertiesByFullName_R(engineName);
		} catch (RemoteException e) {
			return null;
		}
	}

	public SomeProperties getPropertiesByFullName(String id) {
		try {
			return delegate.getPropertiesByFullName_R(id);
		} catch (RemoteException e) {
			return null;
		}
	}

	@Override
	public List<WebAppProperties> getWebAppPropertiesByExtension(String extension) {
		try {
			return delegate.getWebAppPropertiesByExtension_R(extension);
		} catch (RemoteException e) {
			return null;
		}
	}

	@Override
	public WebAppProperties getWebAppPropertiesByUrlName(String urlName) {
		try {
			return delegate.getWebAppPropertiesByUrlName_R(urlName);
		} catch (RemoteException e) {
			return null;
		}
	}

}
