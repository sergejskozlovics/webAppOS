package org.webappos.properties;

import java.rmi.RemoteException;
import java.util.List;

public class IPropertiesManagerWrapper implements IPropertiesManager {
	
	private IRPropertiesManager delegate;
	
	public IPropertiesManagerWrapper(IRPropertiesManager _delegate) {
		delegate = _delegate;
	}

	public String getAppDirectory(String appName) {
		try {
			return delegate.getAppDirectory_R(appName);
		} catch (RemoteException e) {
			return null;
		}
	}

	public AppProperties[] getAllInstalledApps() {
		try {
			return delegate.getAllInstalledApps_R();
		} catch (RemoteException e) {
			return new AppProperties[] {};
		}
	}

	public AppProperties[] getAvailableApps(String login) {
		try {
			return delegate.getAvailableApps_R(login);
		} catch (RemoteException e) {
			return new AppProperties[] {};
		}
	}

	public AppProperties getAppPropertiesByFullName(String appName) {
		try {
			return delegate.getAppPropertiesByFullName_R(appName);
		} catch (RemoteException e) {
			return null;
		}
	}

	public ServiceProperties getServicePropertiesByFullName(String serviceName) {
		try {
			return delegate.getServicePropertiesByFullName_R(serviceName);
		} catch (RemoteException e) {
			return null;
		}
	}

	public EngineProperties getEnginePropertiesByEngineName(String engineName) {
		try {
			return delegate.getEnginePropertiesByEngineName_R(engineName);
		} catch (RemoteException e) {
			return null;
		}
	}

	public SomeProperties getPropertiesById(String id) {
		try {
			return delegate.getPropertiesById_R(id);
		} catch (RemoteException e) {
			return null;
		}
	}

	@Override
	public List<AppProperties> getAppPropertiesByExtension(String extension) {
		try {
			return delegate.getAppPropertiesByExtension_R(extension);
		} catch (RemoteException e) {
			return null;
		}
	}

	@Override
	public AppProperties getAppPropertiesByUrlName(String urlName) {
		try {
			return delegate.getAppPropertiesByUrlName_R(urlName);
		} catch (RemoteException e) {
			return null;
		}
	}

}
