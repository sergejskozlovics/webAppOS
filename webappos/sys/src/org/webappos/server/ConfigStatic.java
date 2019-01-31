package org.webappos.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Stores webAppOS configuration loaded from webappos/etc/webappos.properties.
 * @author Sergejs Kozlovics
 *
 */
public abstract class ConfigStatic {

	public static final String CONFIG_FILE_NAME = "webappos.properties";
	
	public static final String SYS_DIR = getSysDir();
	public static final String ROOT_DIR = SYS_DIR.substring(0, SYS_DIR.lastIndexOf(File.separatorChar));
	public static final String BIN_DIR = ROOT_DIR+File.separator+"bin";
	public static final String ETC_DIR = ROOT_DIR+File.separator+"etc";
	public static final String WEB_ROOT_DIR = ROOT_DIR+File.separator+"web-root";
	public static final String HOME_DIR = ROOT_DIR+File.separator+"home";	
	public static final String APPS_DIR = ROOT_DIR+File.separator+"apps";
	public static final String ENGINES_DIR = ROOT_DIR+File.separator+"engines";
	public static final String PROJECTS_CACHE_DIR = ROOT_DIR+File.separator+"cache"+File.separator+"projects";
	public static final String WEB_ROOT_CACHE_DIR = ROOT_DIR+File.separator+"cache"+File.separator+"web-root";
	public static final String VAR_DIR = ROOT_DIR+File.separator+"var";
	public static final String LOG_DIR = ROOT_DIR+File.separator+"var"+File.separator+"log";
	
	public static final String WEB_PROCESSOR_BUS_SERVICE_NAME = "web_processor_bus_service";

	public Properties properties = new Properties();
	
	public boolean hasOnlyIP = false;
	public String simple_domain_or_ip = "localhost";
	public String domain_or_ip = "localhost:4570";
	public int port = 4570;
	public int secure_port = 4571;
	
	public int web_processor_bus_service_port=1200;
	
	public boolean secure = true;
	public String acme_url = "acme://letsencrypt.org/staging";
	public int acme_renew_interval = 60;
	public String registry_url = ""; // empty string for JSON files; use "http://webappos:webappos@127.0.0.1:5984" for CouchDB
	
	public String preferred_engine_adapter = "web";
	
	public String smtp_server = ""; // e.g., "smtp.gmail.com:587";
	public String smtp_auth = ""; // user:password
	public String smtp_from = ""; // e.g., noreply@domain.org
	public String smtp_from_name = "webAppOS";	
	
	public String standalone_token = null;

	
	public int max_users_per_project = 2; // minimum 2 (the server and at least one client)
	public int project_user_predefined_bits_count = 1; // depends on the previous value
	
	public boolean inline_webcalls = false;
	
	public boolean inWebMode = true; // TODO
	
	

	private static String getSysDir() {
		 String dir = getClassDirectory(ConfigStatic.class);
		 System.out.println("DIRR="+dir);
		 File f = new File(dir);
		 while (!f.getName().equals("sys")) {
			 f = f.getParentFile();
			 if (f==null) {
				 throw new RuntimeException("Could not find webAppOS sys directory.");
			 }
		 }
		 
		 //if (f.getAbsolutePath().endsWith("build"+File.separator+"eclipse-workspace"+File.separator+"sys")) {
			// return f.getParentFile().getParentFile().getParentFile().getAbsolutePath()+File.separator+"sys";
		 //}
		 //else
		 
		System.out.println(f.getAbsolutePath());
		return f.getAbsolutePath();
	}
	
	public static String getClassDirectory(Class<?> cls)
	{
		String path = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.replace("%20", " ");
		int i = path.lastIndexOf('/');
		if (i==-1)
			return ".";
		path = path.substring(0, i);
		
		try {
			File f = new File(path);
			return f.getAbsolutePath();
		}
		catch (Throwable t) {
			return ".";
		}
	}
	
	
	/**
	 * Finds the directory for the engine with the given name.
	 * @param engineName the name of the engine
	 * @return the directory path of the given engine or null if not found
	 */
	public String getEngineDirectory(String engineName) {
		String s;
		if (this.inWebMode)
			s = ConfigStatic.ENGINES_DIR+File.separator+engineName+".web";
		else
			s = ConfigStatic.ENGINES_DIR+File.separator+engineName+".staticjava";
		if (new File(s).exists())
			return s;
		else
			return null;
	}
	

}
