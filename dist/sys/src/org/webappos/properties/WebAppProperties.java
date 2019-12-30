package org.webappos.properties;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.webappos.fs.HomeFS;
import org.webappos.fs.IFileSystem.PathInfo;
import org.webappos.server.API;
import org.webappos.server.ConfigEx;
import org.webappos.server.ConfigStatic;

public class WebAppProperties extends SomeProperties {
	private static final long serialVersionUID = 1L;
	public String app_full_name;
	public String app_displayed_name;
	public String app_url_name; 
	public String project_extension; // without a dot; default is app_url_name
	public String app_icon_url;
	
	public String app_dir;
	
	public String[] supported_extensions = {}; // additional extensions; without dots
	
	public boolean hidden = false;
	
	public boolean collaborative = false;
	public boolean singleton = false;
	public String app_type = "html";
	public boolean requires_root_url_paths = false;
	
	public String main = "";	
	
/*	public enum MemoryScope { NONE, USER, PROJECT, TEMP };
	public MemoryScope memory_scope = MemoryScope.PROJECT;
	
	public boolean suid = false;*/
	
	public String[] requires_web_libraries = {};
	
	public String[] app_templates_search_path = {};
	public String[] published_templates_search_path = {};
	public String[] user_templates_search_path = {};
	private String default_app_templates_search_path = "$WEBAPPOS_ROOT/apps/$APP_FULL_NAME/templates".replace("$WEBAPPOS_ROOT", ConfigStatic.ROOT_DIR);
	private String default_published_templates_search_path = "$WEBAPPOS_ROOT/apps/$APP_FULL_NAME/published_templates".replace("$WEBAPPOS_ROOT", ConfigStatic.ROOT_DIR);
	private String default_user_templates_search_path = "$WEBAPPOS_ROOT/home/$LOGIN/templates".replace("$WEBAPPOS_ROOT", ConfigStatic.ROOT_DIR);
			
	private String[] process_templates_path(String templates_path) {
		if (!System.getProperty("os.name").contains("Windows"))
			templates_path = templates_path.replace(':', ';').replace('/', File.separatorChar);
		else
			templates_path = templates_path.replace('\\', File.separatorChar);
		
		ArrayList<String> arr2 = new ArrayList<String>();
		
		for (String path : templates_path.split(";")) {
			if (!path.trim().isEmpty())
				arr2.add(path.trim().replace("$APP_FULL_NAME", app_full_name));
		}

		return arr2.toArray(new String[] {});		
	}
	
	public WebAppProperties(String fullAppName, String appDir) {
		super(fullAppName, appDir+File.separator+"webapp.properties");
		
		this.app_full_name = fullAppName;	
		this.app_dir = appDir;
					
		try {
			for (Entry<Object, Object> entry : properties.entrySet()) {
				if (entry.getValue().toString().indexOf("$WEBAPPOS_ROOT")>=0) {
					properties.setProperty(entry.getKey().toString(), entry.getValue().toString().replace("$WEBAPPOS_ROOT", ConfigStatic.ROOT_DIR));
				}
				
				if (entry.getValue().toString().indexOf("$APP_DIR")>=0) {
					properties.setProperty(entry.getKey().toString(), entry.getValue().toString().replace("$APP_DIR", appDir));
				}
				
			}

			String simpleName = fullAppName;			
			int i = simpleName.lastIndexOf(".");
			if (i>=0)
				simpleName = simpleName.substring(0, i);
			
			app_displayed_name = properties.getProperty("app_displayed_name", simpleName);

			StringBuffer _urlName = new StringBuffer();
			for (i=0; i<simpleName.length(); i++) {
				char c = simpleName.charAt(i); 
				if (!Character.isSpaceChar(c) && !Character.isWhitespace(c))
					_urlName.append(Character.toLowerCase(c));
			}
			app_url_name = properties.getProperty("app_url_name", _urlName.toString());
			
			// searching for app icon...
			File f1 = new File(ConfigStatic.APPS_DIR+File.separator+app_full_name+File.separator+"web-root"+File.separator+"icon.png");
			File f2 = new File(ConfigStatic.APPS_DIR+File.separator+app_full_name+File.separator+"web-root"+File.separator+"icon.svg");
			File f3 = new File(ConfigStatic.APPS_DIR+File.separator+app_full_name+File.separator+"web-root"+File.separator+"icon.jpg");
			
			app_icon_url = "/icons/unknown-app-icon.svg";
			if (f1.exists())
				app_icon_url = "/apps/"+this.app_url_name+"/icon.png";
			else
				if (f2.exists()) 
					app_icon_url = "/apps/"+this.app_url_name+"/icon.svg";
				else
					if (f3.exists())
						app_icon_url = "/apps/"+this.app_url_name+"/icon.jpg";
			
			project_extension = properties.getProperty("project_extension", app_url_name);
			
			String _supported_extensions = properties.getProperty("supported_extensions", "").trim();			
			ArrayList<String> arr1 = new ArrayList<String>();			
			for (String ext : _supported_extensions.split(",")) {
				ext = ext.trim();
				while (!ext.isEmpty() && ext.charAt(0)=='.')
					ext = ext.substring(1);
				ext = ext.trim();
				if (!ext.isEmpty()) {				
					arr1.add(ext);
				}
			}
			supported_extensions = arr1.toArray(new String[] {});
			
			try {
				collaborative = Boolean.parseBoolean(properties.getProperty("collaborative", collaborative+""));
			}
			catch(Throwable t) {				
			}

			try {
				singleton = Boolean.parseBoolean(properties.getProperty("singleton", singleton+""));
			}
			catch(Throwable t) {				
			}
			
			app_type = properties.getProperty("app_type", app_type);
			try {
				requires_root_url_paths = Boolean.parseBoolean(properties.getProperty("requires_root_url_paths", requires_root_url_paths+""));
			}
			catch(Throwable t) {				
			}
			
			try {
				hidden = Boolean.parseBoolean(properties.getProperty("hidden", hidden+""));
			}
			catch(Throwable t) {				
			}
			
			main = properties.getProperty("main", main);

			/*
			String s = properties.getProperty("memory_scope", "PROJECT").trim().toUpperCase();
			if (s.equals("NONE") || s.isEmpty())
				memory_scope = MemoryScope.NONE;
			else
				if (s.equals("USER"))
					memory_scope = MemoryScope.USER;
				else
					if (s.startsWith("TEMP"))
						memory_scope = MemoryScope.TEMP;
					// else remains MemoryScope.PROJECT
			
			try {
				suid = Boolean.parseBoolean(properties.getProperty("suid", suid+""));
			}
			catch(Throwable t) {				
			}
			*/
			
			String _requires_web_libraries = properties.getProperty("requires_web_libraries", "").trim();			
			ArrayList<String> arr = new ArrayList<String>();			
			for (String libName : _requires_web_libraries.split(",")) {
				libName = libName.trim();
				if (!libName.isEmpty()) {
					if (!libName.endsWith(".weblibrary"))
						libName += ".weblibrary";
					arr.add(libName);
				}
			}
			requires_web_libraries = arr.toArray(new String[] {});
						
			app_templates_search_path = process_templates_path( properties.getProperty("app_templates_search_path", default_app_templates_search_path).trim() );
			published_templates_search_path = process_templates_path( properties.getProperty("published_templates_search_path", default_published_templates_search_path).trim() );
			user_templates_search_path = process_templates_path( properties.getProperty("user_templates_search_path", default_user_templates_search_path).trim() );
			
			
			if (API.config instanceof ConfigEx)
				((ConfigEx)API.config).addMimes(properties.getProperty("mimes"));

			
		} catch (Throwable t) {
		}		
	}

	
	/**
	 * Finds all templates found in the app's templates search paths
	 * (search paths are defined in app.properties via the app_templates_search_path setting).
	 * @return the list of relative paths of found templates (or null on error); paths are relative to template search path
	 *   and delimited by "/" for further processing by webAppOS IProject
	 */
	public List<String> listAppTemplates() {		
		ArrayList<String> retVal = new ArrayList<String>();
		for (String path : this.app_templates_search_path) {
				File f = new File(path);			
				if (f.exists() && f.isDirectory()) {
					String[] arr = f.list();
					if (arr!=null)
						for (String s : arr) {
								if (s.endsWith("."+project_extension)) {
									retVal.add(path+"/"+s);
									break; // for
								}
						}
				}
		}
		return retVal;		
	}

	/**
	 * Finds all templates found in the published templates search paths
	 * (search paths are defined in app.properties via the published_templates_search_path setting).
	 * @return the list of relative paths of found templates (or null on error); paths are relative to template search path
	 *   and delimited by "/" for further processing by webAppOS IProject
	 */
	public List<String> listPublishedTemplates() {		
		ArrayList<String> retVal = new ArrayList<String>();
		for (String path : this.published_templates_search_path) {
				File f = new File(path);			
				if (f.exists() && f.isDirectory()) {
					String[] arr = f.list();
					if (arr!=null)
						for (String s : arr) {
								if (s.endsWith("."+project_extension)) {
									retVal.add(path+"/"+s);
									break; // for
								}
						}
				}
		}
		return retVal;		
	}
	
	/**
	 * Finds all templates found in the user's template search paths
	 * (search paths are defined in app.properties via the user_templates_search_path setting).
	 * @param login the name of the user (since certain templates are searched in the user's home folder); can be null
	 * @return the list of relative paths of found templates (or null on error); paths are relative to template search path
	 *   and delimited by "/" for further processing by webAppOS IProject
	 */
	public List<String> listUserTemplates(String login) {
		String homePrefix=ConfigStatic.HOME_DIR+File.separator;
		
		ArrayList<String> retVal = new ArrayList<String>();
		for (String path : this.user_templates_search_path) {
			if (path.indexOf("$LOGIN")>=0) {
				if (login==null)
					continue;
				else
					path = path.replace("$LOGIN", login);
			}
			
			// if the path points to a home directory...
			if (path.startsWith(homePrefix)) {
				// use HomeFS...
				path = path.substring(homePrefix.length()).replace('\\', '/');
				PathInfo pi = HomeFS.ROOT_INSTANCE.getPathInfo(path);
				if ((pi!=null) && (pi.isDirectory)) {
					List<PathInfo> list = HomeFS.ROOT_INSTANCE.listDirectory(path);
					if (list!=null)
						for (PathInfo info : list) {
								if (info.name.endsWith("."+project_extension)) {
									retVal.add(path+"/"+info.name);
									break; // for
								}
						}
				}
			}
			else {			
				File f = new File(path);			
				if (f.exists() && f.isDirectory()) {
					String[] arr = f.list();
					if (arr!=null)
						for (String s : arr) {
								if (s.endsWith("."+project_extension)) {
									retVal.add(path+"/"+s);
									break; // for
								}
						}
				}
			}
		}
		return retVal;		
	}

	/**
	 * Finds an app template with the given name (can include path) relative to one of the app templates search paths,
	 * and opens an input stream (search paths are defined in app.properties via the app_templates_search_path setting).
	 * @param templateName the validated name of the template (can include path) relative to app_template_search_path
	 * @return the template stream (or null, if not found or an error occurred); the stream must be closed by the caller
	 */
	public InputStream openAppTemplateStream(String relativeTemplateName) {
		for (String path : this.app_templates_search_path) {
		
			File f = new File(path+File.separator+relativeTemplateName);
			System.err.println(f.getAbsolutePath()+" "+f.exists()+" "+f.isFile());
			if (f.exists() && f.isFile()) {
				try {
					return new BufferedInputStream(new FileInputStream(f));
				}
				catch(Throwable t) {
					return null;
				}
			}
			
		}
		return null;
	}
	
	/**
	 * Finds a published template with the given name (can include path) relative to one of the published templates search paths,
	 * and opens an input stream (search paths are defined in app.properties via the published_templates_search_path setting).
	 * @param templateName the validated name of the template (can include path) relative to published_templates_search_path
	 * @return the template stream (or null, if not found or an error occurred); the stream must be closed by the caller
	 */
	public InputStream openPublishedTemplateStream(String relativeTemplateName) {
		for (String path : this.published_templates_search_path) {
		
			File f = new File(path+File.separator+relativeTemplateName);
			System.err.println(f.getAbsolutePath()+" "+f.exists()+" "+f.isFile());
			if (f.exists() && f.isFile()) {
				try {
					return new BufferedInputStream(new FileInputStream(f));
				}
				catch(Throwable t) {
					return null;
				}
			}
			
		}
		return null;
	}
	
	/**
	 * Finds a user template with the given name (can include path) relative to one of the user template search paths,
	 * and opens an input stream (search paths are defined in app.properties via the user_templates_search_path setting).
	 * @param login the validated name of the user (since certain templates are searched in the user's home folder); can be null
	 * @param templateName the validated name of the template (can include path) relative to user_template_search_path
	 * @return the template stream (or null, if not found or an error occurred); the stream must be closed by the caller
	 */
	public InputStream openUserTemplateStream(String login, String relativeTemplateName) {
		String homePrefix=ConfigStatic.HOME_DIR.replace('\\', '/')+"/";
		
		for (String path : this.user_templates_search_path) {
			if (path.indexOf("$LOGIN")>=0) {
				if (login==null)
					continue;
				else
					path = path.replace("$LOGIN", login);
			}
			

			// if the path points to a home directory...
			if (path.replace('\\', '/').startsWith(homePrefix)) {
				// use HomeFS...
				path = path.substring(homePrefix.length()).replace('\\', '/')+"/"+relativeTemplateName;
				if (!HomeFS.ROOT_INSTANCE.pathExists(path))
					continue;
				PathInfo pi = HomeFS.ROOT_INSTANCE.getPathInfo(path);
				if ((pi!=null) && (!pi.isDirectory)) {
					return HomeFS.ROOT_INSTANCE.downloadFile(path);
				}
			}
			else {			
				File f = new File(path+File.separator+relativeTemplateName);
				System.err.println(f.getAbsolutePath()+" "+f.exists()+" "+f.isFile());
				if (f.exists() && f.isFile()) {
					try {
						return new BufferedInputStream(new FileInputStream(f));
					}
					catch(Throwable t) {
						return null;
					}
				}
			}
			
		}
		return null;
	}
	
	public InputStream openTemplateStream(String login, String templateNameWithPrefix) {
		if (templateNameWithPrefix == null)
			return null;
		if (templateNameWithPrefix.startsWith("apptemplate:"))
			return this.openAppTemplateStream(templateNameWithPrefix.substring("apptemplate:".length()));
		if (templateNameWithPrefix.startsWith("publishedtemplate:"))
			return this.openPublishedTemplateStream(templateNameWithPrefix.substring("publishedtemplate:".length()));
		if (templateNameWithPrefix.startsWith("usertemplate:"))
			return this.openUserTemplateStream(login, templateNameWithPrefix.substring("usertemplate:".length()));
		
		return null;
	}

}
