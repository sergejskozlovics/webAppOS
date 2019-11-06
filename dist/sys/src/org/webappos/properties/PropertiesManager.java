package org.webappos.properties;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;

public class PropertiesManager extends UnicastRemoteObject implements IPropertiesManager, IRPropertiesManager {
	public PropertiesManager() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;
	private static Logger logger =  LoggerFactory.getLogger(PropertiesManager.class);
	
	private Map<String, AppProperties> appPropsMap = new HashMap<String, AppProperties>();
	private Map<String, ServiceProperties> svcPropsMap = new HashMap<String, ServiceProperties>();
	private Map<String, EngineProperties> enginePropsMap = new HashMap<String, EngineProperties>();
	private Map<String, ArrayList<AppProperties> > extensionsMap = new HashMap<String, ArrayList<AppProperties> >();
	
	public synchronized String getAppDirectory(String appName) {
		if (appName==null)
			return null;
		if (!appName.endsWith(".app"))
			appName += ".app";

		return ConfigStatic.APPS_DIR+File.separator+appName;
	}
	
	/*
	public synchronized static String[] getAllInstalledApps() {
		ArrayList<String> arr = new ArrayList<String>();
		File f = new File(Config.APPS_DIR);
		for (String name : f.list()) {
			if (name.endsWith(".app")) {
				arr.add(name.substring(0, name.length()-4));
			}
		}
		return arr.toArray(new String[]{});
	}

	public synchronized static String[] getAvailableApps(String login) {
		return getAllInstalledApps(); // TODO: for some users not all apps can be available
	}*/
	
	public synchronized AppProperties[] getAllInstalledApps() {
		return appPropsMap.values().toArray(new AppProperties[]{});
	}

	public synchronized AppProperties[] getAvailableApps(String login) {
		return getAllInstalledApps(); // TODO: for some users not all apps can be available
	}
	
	public synchronized AppProperties loadAppProperties(String appName, String appDir) {
		if (appName==null)
			return null;
		if (!appName.endsWith(".app"))
			appName += ".app";
		
		AppProperties appProps = appPropsMap.get(appName);		
		
		if (appProps == null) {
			appProps = new AppProperties(appName, appDir);
			appPropsMap.put(appName, appProps);
			for (String extension : appProps.supported_extensions) {
				ArrayList<AppProperties> arr = extensionsMap.get(extension);
				if (arr==null) {
					arr = new ArrayList<AppProperties>();
					extensionsMap.put(extension, arr);
				}
				arr.add(appProps);
			}
			
			if ((appProps.project_extension!=null)&&(!appProps.project_extension.isEmpty())) {
				ArrayList<AppProperties> arr = extensionsMap.get(appProps.project_extension);
				if (arr==null) {
					arr = new ArrayList<AppProperties>();
					extensionsMap.put(appProps.project_extension, arr);
				}
				arr.add(appProps);
			}
			return appProps;
		}
		else {
			if (appProps.app_dir.equals(appDir))
				return appProps;
			else {
				logger.error("App "+appName+" already loaded from "+appProps.app_dir);
				return null;
			}
		}
	}
	
	public synchronized AppProperties getAppPropertiesByFullName(String appName) {
		if (appName==null)
			return null;
		if (!appName.endsWith(".app"))
			appName += ".app";
		
		
		AppProperties appProps = appPropsMap.get(appName);		
		return appProps;
	}
	
/*	public synchronized static String getAppByProjectId(String project_id) {
		if (project_id==null)
			return null;
		int i = project_id.lastIndexOf('.');
		if (i<0)
			return null;
		return AppsManager.getAppByExtension(project_id.substring(i+1));
	}*/
	
	public synchronized ServiceProperties loadServiceProperties(String serviceName, String serviceDir) {
		if (serviceName==null)
			return null;
		if (!serviceName.endsWith(".service"))
			serviceName += ".service";
				
		ServiceProperties svcProps = svcPropsMap.get(serviceName);
		
		if (svcProps == null) {
			svcProps = new ServiceProperties(serviceName, serviceDir);
			svcPropsMap.put(serviceName, svcProps);
			return svcProps;
		}
		else {
			if (svcProps.service_dir.equals(serviceDir))
				return svcProps;
			else {
				logger.error("Service "+serviceName+" already loaded from "+svcProps.service_dir);
				return null;
			}
			
		}
	}
	
	public synchronized ServiceProperties getServicePropertiesByFullName(String serviceName) {
		
		if (serviceName==null)
			return null;
		if (!serviceName.endsWith(".service"))
			serviceName += ".service";
		
		
		ServiceProperties svcProps = svcPropsMap.get(serviceName);
		return svcProps;
	}
		
	
	public synchronized EngineProperties loadEngineProperties(String _engineName) {
		String engineName = _engineName;
		
		if (engineName==null)
			return null;
		if (!engineName.endsWith(".engine"))
			engineName += ".engine";
				
		String engineDirS = API.config.getEngineDirectory(_engineName);
		
		EngineProperties engineProps = enginePropsMap.get(engineName);
		
		if (engineProps == null) {
			engineProps = new EngineProperties(engineName, engineDirS+File.separator+"engine.properties");
			enginePropsMap.put(engineName, engineProps);
			return engineProps;
		}
		else {
			return engineProps;
		}
	}
	
	public synchronized EngineProperties getEnginePropertiesByEngineName(String engineName) {
		
		if (engineName==null)
			return null;
		if (!engineName.endsWith(".engine"))
			engineName += ".engine";
		
		
		EngineProperties engineProps = enginePropsMap.get(engineName);
		return engineProps;
	}
	//TODO:
	// installAppOrService	
	// upgradeAppOrService
	// uninstallAppOrService	
	// load/remove properties; attach/detach to Gate
	// informs active users
	
	// TODO: getInstalledApps
	
	// TODO: findAssociatedApps (returns the list of apps; the first is the most preferred)
	
	public synchronized List<AppProperties> getAppPropertiesByExtension(String extension) {
		if (extension == null)
			return null;
		
		return extensionsMap.get(extension); // TODO: return read-only?
	}

	public synchronized AppProperties getAppPropertiesByUrlName(String urlName) {
		if (urlName == null)
			return null;
		
		urlName = urlName.trim();
		if (urlName.isEmpty())
			return null;
		
		for (String key : appPropsMap.keySet()) {
			AppProperties props = appPropsMap.get(key);
			if (urlName.equals(props.app_url_name))
				return props;
		}
		
		return null;
	}
	
	
	public synchronized SomeProperties getPropertiesById(String id) {
		SomeProperties p;
		p = appPropsMap.get(id);
		if (p!=null)
			return p;
		p = svcPropsMap.get(id);
		if (p!=null)
			return p;
		return enginePropsMap.get(id);
	}

	@Override
	public String getAppDirectory_R(String appName) throws RemoteException {
		return this.getAppDirectory(appName);
	}

	@Override
	public AppProperties[] getAllInstalledApps_R() throws RemoteException {
		return this.getAllInstalledApps();
	}

	@Override
	public AppProperties[] getAvailableApps_R(String login) throws RemoteException {
		return this.getAvailableApps(login);
	}

	@Override
	public AppProperties getAppPropertiesByFullName_R(String appName) throws RemoteException {
		return this.getAppPropertiesByFullName(appName);
	}

	@Override
	public ServiceProperties getServicePropertiesByFullName_R(String serviceName) throws RemoteException {
		return this.getServicePropertiesByFullName(serviceName);
	}

	@Override
	public EngineProperties getEnginePropertiesByEngineName_R(String engineName) throws RemoteException {
		return this.getEnginePropertiesByEngineName(engineName);
	}

	@Override
	public SomeProperties getPropertiesById_R(String id) throws RemoteException {
		return this.getPropertiesById(id);
	}

	@Override
	public List<AppProperties> getAppPropertiesByExtension_R(String extension) throws RemoteException {
		return this.getAppPropertiesByExtension(extension);
	}

	@Override
	public AppProperties getAppPropertiesByUrlName_R(String urlName) throws RemoteException {
		return this.getAppPropertiesByUrlName(urlName);
	}
	
	
}
