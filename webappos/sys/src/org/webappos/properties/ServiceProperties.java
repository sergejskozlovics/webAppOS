package org.webappos.properties;

import java.io.File;
import java.util.Map.Entry;

import org.webappos.server.API;
import org.webappos.server.APIForServerBridge;
import org.webappos.server.ConfigStatic;

public class ServiceProperties extends SomeProperties {
	private static final long serialVersionUID = 1L;
	public String service_full_name;
	public String service_displayed_name;
	public String service_url_name;
	public String service_dir;
	
	public boolean hidden = false;
	
	enum ServiceType { PERMANENT, ONETIME };
	public ServiceType service_type = ServiceType.ONETIME;
	public String service_adapter = "javaservlet";	
	
	public boolean requires_root_url_paths = false;
	public boolean requires_http_port = false;
	public boolean requires_https_port = false;
	
	public boolean suid = false;
	
	public ServiceProperties(String serviceName, String serviceDir) {
		super(serviceName, serviceDir+File.separator+"service.properties");
		if (!serviceName.endsWith(".service")) {
			serviceName = serviceName+".service";
			id = serviceName;
		}
		
		
		
		this.service_full_name = serviceName;	
		this.service_dir = serviceDir;
		
		try {			
			for (Entry<Object, Object> entry : properties.entrySet()) {
				if (entry.getValue().toString().indexOf("$WEBAPPOS_ROOT")>=0) {
					properties.setProperty(entry.getKey().toString(), entry.getValue().toString().replace("$WEBAPPOS_ROOT", ConfigStatic.ROOT_DIR));
				}
			}
			
			String simpleName = serviceName;
			if (simpleName.startsWith(".")) {
				hidden = true;
				simpleName = simpleName.substring(1);
			}
			int i = simpleName.lastIndexOf(".");
			if (i>=0)
				simpleName = simpleName.substring(0, i);
			
			service_displayed_name = properties.getProperty("service_displayed_name", simpleName);

			StringBuffer _urlName = new StringBuffer();
			for (i=0; i<simpleName.length(); i++) {
				char c = simpleName.charAt(i); 
				if (!Character.isSpaceChar(c) && !Character.isWhitespace(c))
					_urlName.append(Character.toLowerCase(c));
			}
			service_url_name = properties.getProperty("service_url_name", _urlName.toString());
			
			
			String s = properties.getProperty("service_type", "ONETIME").trim().toUpperCase();
			if (s.equals("PERMANENT"))
				service_type = ServiceType.PERMANENT;
			service_adapter = properties.getProperty("service_adapter", service_adapter);
			
			if (properties.getProperty("service_adapter","").trim().equals("")) {
				File f = new File(this.service_dir+File.separator+"web-root");
				if (f.exists() && f.isDirectory())
					service_adapter = "webroot";
			}
			
			try {
				requires_root_url_paths = Boolean.parseBoolean(properties.getProperty("requires_root_url_paths", requires_root_url_paths+""));
			}
			catch(Throwable t) {				
			}
			
			try {
				requires_http_port = Boolean.parseBoolean(properties.getProperty("requires_http_port", requires_http_port+""));
			}
			catch(Throwable t) {				
			}
			
			try {
				requires_https_port = Boolean.parseBoolean(properties.getProperty("requires_https_port", requires_https_port+""));
			}
			catch(Throwable t) {				
			}

			try {
				suid = Boolean.parseBoolean(properties.getProperty("suid", suid+""));
			}
			catch(Throwable t) {				
			}
			
			APIForServerBridge.configForServerBridge.addMimes(properties.getProperty("mimes"));
			
		} catch (Throwable t) {
		}		
	}

}
