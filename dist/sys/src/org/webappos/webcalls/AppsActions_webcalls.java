package org.webappos.webcalls;

import java.io.File;
import java.util.List;

import org.webappos.fs.HomeFS;
import org.webappos.fs.IFileSystem.PathInfo;
import org.webappos.properties.WebAppProperties;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webcaller.IWebCaller;

import lv.lumii.tda.kernel.TDAKernel;

public class AppsActions_webcalls {

	public static String getAppPropertiesByFullName(String appFullName) { // no raapi, no login
		WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(appFullName);
		if (props==null)
			return "{}";
		
		return "{\"fullName\":\""+props.app_full_name+"\",\"displayedName\":\""+props.app_displayed_name+"\",\"urlName\":\""+props.app_url_name+"\",\"iconURL\":\""+props.app_icon_url+"\",\"projectExtension\":\""+props.project_extension+"\",\"singleton\":"+props.singleton+"}";
	}
	
	public static String getAppPropertiesByUrlName(String appUrlName) { // no raapi, no login
		WebAppProperties props = API.propertiesManager.getWebAppPropertiesByUrlName(appUrlName);
		if (props==null)
			return "{}";
		
		return "{\"fullName\":\""+props.app_full_name+"\",\"displayedName\":\""+props.app_displayed_name+"\",\"urlName\":\""+props.app_url_name+"\",\"iconURL\":\""+props.app_icon_url+"\",\"projectExtension\":\""+props.project_extension+"\",\"singleton\":"+props.singleton+"}";
	}
	
	public static String getAssociatedAppsByExtension(String extension, String login) { // no raapi
		List<WebAppProperties> list = API.propertiesManager.getWebAppPropertiesByExtension(extension);
		
		if (list==null)
			return "[]";

		String retVal = "[";
		for (WebAppProperties props : list) {
			if (retVal.length()>1)
				retVal+=",";
			retVal += "{\"fullName\":\""+props.app_full_name+"\",\"displayedName\":\""+props.app_displayed_name+"\",\"urlName\":\""+props.app_url_name+"\",\"iconURL\":\""+props.app_icon_url+"\",\"projectExtension\":\""+props.project_extension+"\"}";
		}
		retVal+="]";
		
		return retVal;
	}

	public static String getAvailableApps(String anyArg, String login) { // no raapi
		WebAppProperties[] arr = API.propertiesManager.getAvailableWebApps(login);
		
		String retVal = "[";
		for (WebAppProperties props : arr) {
			if (props.hidden)
				continue;
			
			if (retVal.length()>1)
				retVal+=",";
			retVal += "{\"fullName\":\""+props.app_full_name+"\",\"displayedName\":\""+props.app_displayed_name+"\",\"urlName\":\""+props.app_url_name+"\",\"iconURL\":\""+props.app_icon_url+"\"}";
		}
		
		retVal += "]";
		return retVal;
	}	

	public static String appRequiresTemplate(String appFullName) { // no raapi, no login		
		WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(appFullName);		
		boolean b = (props==null) || (props.initial_webcall==null) || (props.initial_webcall.isEmpty());
		String s = "{\"result\":"+b+"}";
		return s;
	}
	
	public static String getAppTemplates(String appFullName) { // no raapi, no login
		WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(appFullName);
		
		if ((props==null) || (props.app_templates_search_path==null))
			return "[]";
		
		String ext = props.project_extension;
		if (ext==null)
			return "[]";
		
		ext = "."+ext;
		
		String retVal = "[";
		for (String path : props.app_templates_search_path) {

				File f = new File(path);
				String[] list = f.list();
				if (list!=null)
					for (String s : list) {
						if (s.endsWith(ext)) {
							if (retVal.length()>1)
								retVal+=",";
							retVal+="\""+s+"\"";
						}
					}
				
			
		}
		retVal += "]";
		return retVal;
	}

	public static String getPublishedTemplates(String appFullName) { // no raapi, no login
		WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(appFullName);
		if ((props==null) || (props.published_templates_search_path==null))
			return "[]";
		
		String ext = props.project_extension;
		if (ext==null)
			return "[]";
		
		ext = "."+ext;
		
		String retVal = "[";
		for (String path : props.published_templates_search_path) {

				File f = new File(path);
				String[] list = f.list();
				if (list!=null)
					for (String s : list) {
						if (s.endsWith(ext)) {
							if (retVal.length()>1)
								retVal+=",";
							retVal+="\""+s+"\"";
						}
					}
				
			
		}
		retVal += "]";
		System.err.println(retVal);
		return retVal;
	}

	public static String getUserTemplates(String appFullName, String login) { // no raapi
		WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(appFullName);
		if ((props==null) || (props.user_templates_search_path==null))
			return "[]";
		
		String ext = props.project_extension;
		if (ext==null)
			return "[]";
		
		ext = "."+ext;
				
		String homePrefix=ConfigStatic.HOME_DIR.replace('\\', '/')+"/";
		
		String retVal = "[";
		for (String path : props.user_templates_search_path) {
			if (path.indexOf("$LOGIN")>=0) {
				if (login==null)
					continue;
				else
					path = path.replace("$LOGIN", login);
			}
			

			// if the path points to a home directory...
			if (path.replace('\\', '/').startsWith(homePrefix)) {
				
				// use HomeFS...
				path = path.substring(homePrefix.length()).replace('\\', '/');
								
				if (!HomeFS.ROOT_INSTANCE.pathExists(path))
					continue;
				
				List<PathInfo> list = HomeFS.ROOT_INSTANCE.listDirectory(path);
				if (list==null)
					continue;
				
				for (PathInfo pi : list) {
					if (pi.name.endsWith(ext)) {
						if (retVal.length()>1)
							retVal+=",";
						retVal+="\""+pi.name+"\"";
					}					
				}
			}
			
		}
		retVal += "]";
		return retVal;
	}

	
	public static String initializeProject(String project_id, String arg, String login, String fullAppName) {
		
		WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(fullAppName);
		if (props==null)
			return "{}";
		
		TDAKernel kernel = API.dataMemory.getTDAKernel(project_id);
		if (kernel==null)
			return "{}";
		
		
		
		long it = kernel.getIteratorForAllClassObjects(kernel.KMM.SUBMITTER);
		long rSubmitter = kernel.resolveIteratorFirst(it);
		kernel.freeIterator(it);
		
		// loading engines...
		for (String engineName : props.requires_web_libraries) {
			long rCmd = kernel.createObject(kernel.KMM.ATTACHENGINECOMMAND);
			kernel.setAttributeValue(rCmd, kernel.KMM.ATTACHENGINECOMMAND_NAME, engineName);
			kernel.createLink(rCmd, rSubmitter, kernel.KMM.COMMAND_SUBMITTER);			
		}
		
		IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
		seed.callingConventions = IWebCaller.CallingConventions.TDACALL;
		seed.actionName = props.initial_webcall;
		seed.tdaArgument = 0;
		seed.login = login;
		seed.project_id = project_id;
		seed.jsonResult = null;
		
		API.webCaller.enqueue(seed);

		return "{}";
	}
}
