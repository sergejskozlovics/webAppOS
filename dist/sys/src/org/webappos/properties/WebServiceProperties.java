package org.webappos.properties;

import java.io.File;
import java.util.Map.Entry;

import org.webappos.server.API;
import org.webappos.server.ConfigEx;
import org.webappos.server.ConfigStatic;

public class WebServiceProperties extends SomeProperties {
	private static final long serialVersionUID = 1L;
	public String service_full_name;
	public String service_displayed_name;
	public String service_url_name;
	public String service_dir;
	
	//public boolean hidden = false;
	
	public String service_type = "javaservlet";	
	
	public boolean requires_root_url_paths = false;
	
	public int httpPort = -1;  // will be assigned either by the service adapter (from requires_additional_ports), or by webAppOS Gate 
	public int httpsPort = -1; // will be assigned either by the service adapter (from requires_additional_ports), or by webAppOS Gate
	
	public int[] requires_additional_ports = new int[0];
	
//	public boolean suid = false;
	
	public WebServiceProperties(String serviceFullName, String serviceDir) {
		super(serviceFullName, serviceDir+File.separator+"webservice.properties");		
		
		this.service_full_name = serviceFullName;	
		this.service_dir = serviceDir;
		
		try {			
			for (Entry<Object, Object> entry : properties.entrySet()) {
				if (entry.getValue().toString().indexOf("$WEBAPPOS_ROOT")>=0) {
					properties.setProperty(entry.getKey().toString(), entry.getValue().toString().replace("$WEBAPPOS_ROOT", ConfigStatic.ROOT_DIR));
				}
			}
			
			String simpleName = serviceFullName;
			/*if (simpleName.startsWith(".")) {
				hidden = true;
				simpleName = simpleName.substring(1);
			}*/
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
			
			
			service_type = properties.getProperty("service_type", service_type);
			
			if (properties.getProperty("service_type","").trim().equals("")) {
				File f = new File(this.service_dir+File.separator+"web-root");
				if (f.exists() && f.isDirectory())
					service_type = "webroot";
			}
			
			try {
				requires_root_url_paths = Boolean.parseBoolean(properties.getProperty("requires_root_url_paths", requires_root_url_paths+""));
			}
			catch(Throwable t) {				
			}
			
			
			String additional_ports = properties.getProperty("requires_additional_ports", "").trim();
			if (!additional_ports.isEmpty()) {
				String[] arr = additional_ports.split(",");
				this.requires_additional_ports = new int[arr.length];
				for (int k=0; k<arr.length; k++) {
					try {
						this.requires_additional_ports[k] = Integer.parseInt(arr[k]);
						if ((this.requires_additional_ports[k]<0) || (this.requires_additional_ports[k]>65535))
							this.requires_additional_ports[k] = -1;
					}
					catch(Throwable t) {
						this.requires_additional_ports[k] = -1;
					}
				}
			}
			

			if (API.config instanceof ConfigEx)
				((ConfigEx)API.config).addMimes(properties.getProperty("mimes"));
			
		} catch (Throwable t) {
		}		
	}

}
