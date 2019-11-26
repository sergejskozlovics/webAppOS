package org.webappos.webcalls;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.webappos.fs.HomeFS;
import org.webappos.fs.IFileSystem.PathInfo;
import org.webappos.properties.WebAppProperties;
import org.webappos.properties.WebLibraryProperties;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webmem.IWebMemory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lv.lumii.tda.kernel.mmdparser.MetamodelInserter;

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
		boolean b = (props==null) || (props.main==null) || (props.main.isEmpty());
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

	private static boolean insertSomeMetamodels(String someFullName, IWebMemory raapi)
	{
		File dir = new File(ConfigStatic.APPS_DIR+File.separator+someFullName);
		File[] files = dir.listFiles();
		if (files==null)
			return false;
		
		boolean ok = true;
		for (File f : files) {
			if (f.isFile()) {
				if (f.getName().endsWith(".mmd") || f.getName().endsWith(".ecore")) {										 
					// trying to insert this metamodel...
					try {
						if (!MetamodelInserter.insertMetamodel(f.toURI().toURL(), raapi))
							ok = false;
					} catch (MalformedURLException e) {
						ok = false;
					}
					
				}
			}
		}
		
		return ok;
	}
	
	public static String initializeProject(String project_id, String arg, String login, String fullAppName) {
		
		WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(fullAppName);
		if (props==null)
			return "{\"error\":\"Web app not found.\"}";
		
		IWebMemory webmem = API.dataMemory.getWebMemory(project_id);
		if (webmem==null)
			return "{\"error\":\"Server-side web memory not found.\"}";

		boolean bootstrapped = false;
		try {
			JsonElement jelement = new JsonParser().parse(arg);
			JsonObject o = jelement.getAsJsonObject();
			JsonElement el = o.get("bootstrapped");
			bootstrapped = el.getAsBoolean();
		}
		catch(Throwable t) {			
		}
						
		// loading required web libraries...
		for (String fullLibraryName : props.requires_web_libraries) {
			if (bootstrapped)
				insertSomeMetamodels(fullLibraryName, webmem);
			
			WebLibraryProperties wlprops = API.propertiesManager.getWebLibraryPropertiesByFullName(fullLibraryName);
			if ((wlprops != null) && (wlprops.on_load!=null) && (!wlprops.on_load.isEmpty())) {
				IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
				seed.callingConventions = IWebCaller.CallingConventions.WEBMEMCALL;
				seed.actionName = wlprops.on_load;
				seed.webmemArgument = 0;
				seed.login = login;
				seed.project_id = project_id;
				seed.jsonResult = null;
				
				API.webCaller.enqueue(seed);				
			}
		}
		
		if (bootstrapped)
			insertSomeMetamodels(fullAppName, webmem);
		
		if (props.main!=null && !props.main.isEmpty()) {
			IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
			seed.callingConventions = IWebCaller.CallingConventions.WEBMEMCALL;
			seed.actionName = props.main;
			seed.webmemArgument = 0;
			seed.login = login;
			seed.project_id = project_id;
			seed.jsonResult = null;
			
			API.webCaller.enqueue(seed);
		}

		return "{}";
	}
}
