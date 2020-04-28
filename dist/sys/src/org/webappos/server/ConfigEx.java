package org.webappos.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.fs.IFileSystem;


/**
 * Stores webAppOS configuration loaded from webappos/etc/webappos.properties.
 * ConfigEx extends ConfigStatic by adding dynamic configuration available for internal server-side usage.
 * @author Sergejs Kozlovics
 *
 */
public class ConfigEx extends ConfigStatic implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger logger =  LoggerFactory.getLogger(ConfigEx.class);
	
	public int free_port = 4572;
	public Map<String, String> mimes = new ConcurrentHashMap<String, String>(); // extension -> mime	
	
	public ConfigEx() { // constructor
		try {
			try {
				properties.load(new FileInputStream(ETC_DIR + File.separator + CONFIG_FILE_NAME));
			}
			catch (Throwable t) {
				logger.error("Could not load webAppOS properties from file "
							+ ETC_DIR + File.separator + CONFIG_FILE_NAME, t);
			}		

			
			
			for (Entry<Object, Object> entry : properties.entrySet()) {
				if (entry.getValue().toString().indexOf("$WEBAPPOS_ROOT")>=0) {
					properties.setProperty(entry.getKey().toString(), entry.getValue().toString().replace("$WEBAPPOS_ROOT", ROOT_DIR));
				}
			}
			
			domain_or_ip = properties.getProperty("domain_or_ip", domain_or_ip);
			simple_domain_or_ip = domain_or_ip;
			if (simple_domain_or_ip.startsWith("http://")) {
				simple_domain_or_ip = simple_domain_or_ip.substring(7); // remove protocol
			}
			if (simple_domain_or_ip.startsWith("https://")) {
				simple_domain_or_ip = simple_domain_or_ip.substring(8); // remove protocol
			}
			if (simple_domain_or_ip.indexOf('@')>=0) {
				simple_domain_or_ip = simple_domain_or_ip.substring(simple_domain_or_ip.indexOf('@')+1); // remove user@
			}
			if (simple_domain_or_ip.indexOf(':')>=0) {
				simple_domain_or_ip = simple_domain_or_ip.substring(0,  simple_domain_or_ip.indexOf(':')); // remove port
			}
			if (simple_domain_or_ip.indexOf('/')>=0) {
				simple_domain_or_ip = simple_domain_or_ip.substring(0,  simple_domain_or_ip.indexOf('/')); // remove path
			}
			simple_domain_or_ip = simple_domain_or_ip.trim();
			if (simple_domain_or_ip.isEmpty()) {
				simple_domain_or_ip = "localhost";
			}
			else {
				if (!Character.isAlphabetic(simple_domain_or_ip.charAt(0))) { // IP specified, setting hasOnlyIP to true
					hasOnlyIP = true;
				}
			}
			
			try {
				port = Integer.parseInt(properties.getProperty("port", port+""));
				if (port<0)
					port = 0;
				if (port>65535)
					port=65535;
			}
			catch(Throwable t) {				
			}
			try {
				secure_port = Integer.parseInt(properties.getProperty("secure_port", secure_port+""));
				if (secure_port<0)
					secure_port = 0;
				if (secure_port>65535)
					secure_port=65535;
			}
			catch(Throwable t) {				
			}
			try {
				free_port = Integer.parseInt(properties.getProperty("free_port", free_port+""));
				if (free_port<0)
					free_port = 0;
				if (free_port>65535)
					free_port=65535;
			}
			catch(Throwable t) {				
			}
			try {
				web_processor_bus_service_port = Integer.parseInt(properties.getProperty("web_processor_bus_service_port", web_processor_bus_service_port+""));
				if (web_processor_bus_service_port<0)
					web_processor_bus_service_port = 0;
				if (web_processor_bus_service_port>65535)
					web_processor_bus_service_port=65535;
			}
			catch(Throwable t) {				
			}
			try {
				secure = Boolean.parseBoolean(properties.getProperty("secure", secure+""));
			}
			catch(Throwable t) {				
			}
			acme_url = properties.getProperty("acme_url", acme_url);
			try {
				acme_renew_interval = Integer.parseInt(properties.getProperty("acme_renew_interval", acme_renew_interval+""));
				if (acme_renew_interval<1)
					acme_renew_interval = 1;
				if (acme_renew_interval>365*2)
					acme_renew_interval=365*2; // ??
			}
			catch(Throwable t) {				
			}
			registry_url = properties.getProperty("registry_url", registry_url);
			
			smtp_server = properties.getProperty("smtp_server", smtp_server);
			smtp_auth = properties.getProperty("smtp_auth", smtp_auth);
			smtp_from = properties.getProperty("smtp_from", "noreply@"+simple_domain_or_ip);
			smtp_from_name = properties.getProperty("smtp_from_name", smtp_from_name);
									
			standalone_token = properties.getProperty("standalone_token", null);
			
			this.addMimes(properties.getProperty("mimes"));

			try {
				max_users_per_project = Integer.parseInt(properties.getProperty("max_users_per_project", max_users_per_project+""));
				if (max_users_per_project<2)
					max_users_per_project = 2;
				project_user_predefined_bits_count = (32-Integer.numberOfLeadingZeros(max_users_per_project-1));
			}
			catch(Throwable t) {				
			}
			
			try {
				inline_webcalls = Boolean.parseBoolean(properties.getProperty("inline_webcalls", inline_webcalls+""));
			}
			catch(Throwable t) {				
			}
						
			try {
				allow_undeclared_webcalls = Boolean.parseBoolean(properties.getProperty("allow_undeclared_webcalls", allow_undeclared_webcalls+""));
			}
			catch(Throwable t) {				
			}
			
		} catch (Throwable t) {
			logger.error("Could not initialize webAppOS properties - "+t.getMessage());
		}		
		
		
		try {
			new File(HOME_DIR).mkdirs();
			new File(PROJECTS_CACHE_DIR).mkdirs();
		}
		catch(Throwable t) {			
		}
	}
	

	
	
	/**
	 * Registers mime types.
	 * @param mimes the semicolon(";")-delimited list of strings in the form of name:value, where name
	 * is an extension (including the dot, e.g., ".lua") or a directory name relative to the home folder (including the
	 * starting "/", e.g., "/Desktop"), while value is the mime-type, e.g., "source/lua" or "directory/desktop";
	 * the value is used when searching for the corresponding icon
	 */
	public void addMimes(String mimes) { // each app in app.properties can have a setting "mimes"		
		if (mimes==null)
			return;
		String[] arr = mimes.split(";");
		for (String s : arr) {
			int i = s.indexOf(':');
			if (i>=0) {
				String[] exts = s.substring(0, i).split(",");
				String mime = s.substring(i+1);
				for (String ext : exts) {
					if (ext.startsWith("."))
						ext = ext.substring(1);
					this.mimes.put(ext, mime);
				}
			}
		}
	}

	
	private class FSDriver {
		String className = null;
		Constructor<?> constr = null;
	}
	private Map<String, FSDriver > fs_drivers = new ConcurrentHashMap<String, FSDriver >(); // driver protocol name -> driver

	/**
	 * Registers a a file system driver, i.e., a Java class implementing the IFileSystem interface and
	 * having a string constructor.
	 * @param prefix the protocol (prefix) part to be used in location URIs to reference remote file systems, e.g., "gdrive" in "gdrive:my-location" 
	 * @param className the Java class name implementing the driver
	 */
	public void registerFileSystemDriver(String prefix, String className) {
		if ((prefix==null) || prefix.isEmpty())
			return;
		
		while (prefix.endsWith(":")) {
			prefix = prefix.substring(0, prefix.length()-1);
		}
		
		FSDriver drv = new FSDriver();
		drv.className = className;
		
		logger.info("Registering FS driver for protocol \""+prefix+"\" implemented in class "+className);
					
		this.fs_drivers.put(prefix, drv); // do not initialize the constructor, since the classpaths might not have been initialized yet
	}
	
	/**
	 * Initializes and returns a file system driver for the given prefix and remote location combined in the URI.
	 * When being initialized, the driver should take the credentials from the registry key xusers/[login]/[driver_name],
	 * which is initialized when requesting scopes. 
	 * @param uri - a remote location with the prefix corresponding to the driver protocol, e.g., "gdrive:my-remote-folder"
	 * @return an initialized file system driver or null on error
	 */
	public IFileSystem getFileSystemDriver(String uri) {
		if (uri==null)
			return null;
		
		int i = uri.indexOf(':');
		if (i<0)
			return null;
		
		String prefix = uri.substring(0, i);
		String remoteLocation = uri.substring(i+1);
		
		// TODO: cache
				
		FSDriver drv = this.fs_drivers.get(prefix);
		if (drv==null)
			return null;
		
		if (drv.constr==null) {
			Class<?> cls = API.classLoader.findClassByName(drv.className);
			if (cls==null) {
				this.fs_drivers.remove(prefix);
				return null;
			}

			try {
				drv.constr = cls.getConstructor(String.class);
			}
			catch(Throwable t) {
				
			}
		}
		if (drv.constr==null) {
			this.fs_drivers.remove(prefix);
			return null;
		}
		
		try {
			Object obj = drv.constr.newInstance(remoteLocation);
			if (obj instanceof IFileSystem)
				return (IFileSystem)obj;
		} catch (Throwable t) {
		}
		return null;
	}

}
