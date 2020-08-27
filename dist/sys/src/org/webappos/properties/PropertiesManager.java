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
	
	private Map<String, WebAppProperties> appPropsMap = new HashMap<String, WebAppProperties>();
	private Map<String, WebServiceProperties> svcPropsMap = new HashMap<String, WebServiceProperties>();
	private Map<String, WebLibraryProperties> libPropsMap = new HashMap<String, WebLibraryProperties>();
	private Map<String, ArrayList<WebAppProperties> > extensionsMap = new HashMap<String, ArrayList<WebAppProperties> >();
	
	public synchronized WebAppProperties[] getAllInstalledWebApps() {
		return appPropsMap.values().toArray(new WebAppProperties[]{});
	}
	
	public synchronized WebServiceProperties[] getAllInstalledWebServices() {
		return svcPropsMap.values().toArray(new WebServiceProperties[]{});
	}

	public synchronized WebAppProperties[] getAvailableWebApps(String login) {
		return getAllInstalledWebApps(); // TODO: for some users not all apps can be available
	}
	
	private synchronized WebAppProperties loadWebAppPropertiesByFullName(String fullAppName, String appDir) {
		if (fullAppName==null)
			return null;
		
		WebAppProperties appProps = appPropsMap.get(fullAppName);		
		
		if (appProps == null) {
			appProps = new WebAppProperties(fullAppName, appDir);
			appPropsMap.put(fullAppName, appProps);
			for (String extension : appProps.supported_extensions) {
				ArrayList<WebAppProperties> arr = extensionsMap.get(extension);
				if (arr==null) {
					arr = new ArrayList<WebAppProperties>();
					extensionsMap.put(extension, arr);
				}
				arr.add(appProps);
			}
			
			if ((appProps.project_extension!=null)&&(!appProps.project_extension.isEmpty())) {
				ArrayList<WebAppProperties> arr = extensionsMap.get(appProps.project_extension);
				if (arr==null) {
					arr = new ArrayList<WebAppProperties>();
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
				logger.error("App "+fullAppName+" already loaded from "+appProps.app_dir);
				return null;
			}
		}
	}
	
	public synchronized WebAppProperties getWebAppPropertiesByFullName(String appFullName) {
		if (appFullName==null)
			return null;
		
		WebAppProperties appProps = appPropsMap.get(appFullName);
		if (appProps!=null)
			return appProps;
		else {
			File f = new File(ConfigStatic.APPS_DIR+File.separator+appFullName);
			if (!f.isDirectory())
				return null;
			return loadWebAppPropertiesByFullName(appFullName, f.getAbsolutePath());
		}
	}
	
	private synchronized WebServiceProperties loadWebServicePropertiesByFullName(String serviceFullName, String serviceDir) {
		if (serviceFullName==null)
			return null;
				
		WebServiceProperties svcProps = svcPropsMap.get(serviceFullName);
		
		if (svcProps == null) {
			svcProps = new WebServiceProperties(serviceFullName, serviceDir);
			svcPropsMap.put(serviceFullName, svcProps);
			return svcProps;
		}
		else {
			if (svcProps.service_dir.equals(serviceDir))
				return svcProps;
			else {
				logger.error("Service "+serviceFullName+" already loaded from "+svcProps.service_dir);
				return null;
			}
			
		}
	}
	
	public synchronized WebServiceProperties getWebServicePropertiesByFullName(String serviceFullName) {
		
		if (serviceFullName==null)
			return null;
		
		WebServiceProperties svcProps = svcPropsMap.get(serviceFullName);
		if (svcProps!=null)
			return svcProps;
		else {
			File f = new File(ConfigStatic.APPS_DIR+File.separator+serviceFullName);
			if (!f.isDirectory())
				return null;
			
			return loadWebServicePropertiesByFullName(serviceFullName, ConfigStatic.APPS_DIR+File.separator+serviceFullName);
		}
	}
		
	
	private synchronized WebLibraryProperties loadWebLibraryPropertiesByFullName(String libFullName, String dir) {
		
		if (libFullName==null)
			return null;
				
		WebLibraryProperties props = libPropsMap.get(libFullName);
		
		if (props == null) {			
			props = new WebLibraryProperties(libFullName, dir);
			libPropsMap.put(libFullName, props);
			return props;
		}
		else {
			return props;
		}
	}
	
	public synchronized WebLibraryProperties getWebLibraryPropertiesByFullName(String libraryFullName) {
		
		if (libraryFullName==null)
			return null;
		
		WebLibraryProperties props = libPropsMap.get(libraryFullName);
		if (props!=null)
			return props;
		else {
			File f = new File(ConfigStatic.APPS_DIR+File.separator+libraryFullName);
			if (!f.isDirectory())
				return null;
			return loadWebLibraryPropertiesByFullName(libraryFullName, ConfigStatic.APPS_DIR+File.separator+libraryFullName);
		}
	}
	//TODO:
	// installAppOrService	
	// upgradeAppOrService
	// uninstallAppOrService	
	// load/remove properties; attach/detach to Gate
	// informs active users
	
	// TODO: getInstalledApps
	
	// TODO: findAssociatedApps (returns the list of apps; the first is the most preferred)
	
	public synchronized List<WebAppProperties> getWebAppPropertiesByExtension(String ext) {
		if (ext == null)
			return null;
		
		ext = ext.trim();
		while (!ext.isEmpty() && ext.charAt(0)=='.')
			ext = ext.substring(1);
		ext = ext.trim();

		if (ext.isEmpty())
			return null;
		
		return extensionsMap.get(ext); // TODO: return read-only?
	}

	public synchronized WebAppProperties getWebAppPropertiesByUrlName(String urlName) {
		if (urlName == null)
			return null;
		
		urlName = urlName.trim();
		if (urlName.isEmpty())
			return null;
		
		for (String key : appPropsMap.keySet()) {
			WebAppProperties props = appPropsMap.get(key);
			if (urlName.equals(props.app_url_name))
				return props;
		}
		
		return null;
	}
	
	
	public synchronized SomeProperties getPropertiesByFullName(String id) {
		if (id==null)
			return null;
		
		if (id.endsWith(".webapp"))
			return this.getWebAppPropertiesByFullName(id);
		if (id.endsWith(".webservice"))
			return this.getWebServicePropertiesByFullName(id);
		if (id.endsWith(".weblibrary"))
			return this.getWebLibraryPropertiesByFullName(id);
		
		return null;
	}

	@Override
	public WebAppProperties[] getAllInstalledWebApps_R() throws RemoteException {
		return this.getAllInstalledWebApps();
	}

	@Override
	public WebServiceProperties[] getAllInstalledWebServices_R() throws RemoteException {
		return this.getAllInstalledWebServices();
	}
	
	@Override
	public WebAppProperties[] getAvailableWebApps_R(String login) throws RemoteException {
		return this.getAvailableWebApps(login);
	}

	@Override
	public WebAppProperties getWebAppPropertiesByFullName_R(String appName) throws RemoteException {
		return this.getWebAppPropertiesByFullName(appName);
	}

	@Override
	public WebServiceProperties getWebServicePropertiesByFullName_R(String serviceName) throws RemoteException {
		return this.getWebServicePropertiesByFullName(serviceName);
	}

	@Override
	public WebLibraryProperties getWebLibraryPropertiesByFullName_R(String engineName) throws RemoteException {
		return this.getWebLibraryPropertiesByFullName(engineName);
	}

	@Override
	public SomeProperties getPropertiesByFullName_R(String id) throws RemoteException {
		return this.getPropertiesByFullName(id);
	}

	@Override
	public List<WebAppProperties> getWebAppPropertiesByExtension_R(String extension) throws RemoteException {
		return this.getWebAppPropertiesByExtension(extension);
	}

	@Override
	public WebAppProperties getWebAppPropertiesByUrlName_R(String urlName) throws RemoteException {
		return this.getWebAppPropertiesByUrlName(urlName);
	}
	
	
}
