package org.webappos.classloader;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

import org.webappos.properties.AppProperties;
import org.webappos.properties.EngineProperties;
import org.webappos.properties.SomeProperties;
import org.webappos.server.API;

public class PropertiesClassLoader extends URLClassLoader  {
	
	public PropertiesClassLoader() {
		super(new URL[] {});
	}
	
	public PropertiesClassLoader(URL[] arg0) {
		super(arg0);
	}
	

	/*public void addURL(URL url) {
		super.addURL(url);
	}*/
	
	private static Set<String> loadedIds = new HashSet<String>();
	
	public Class<?> findClassByName(String fullName) {
		Class<?> c = null;
		try {
			c = this.loadClass(fullName);
		} catch (ClassNotFoundException e1) {
		}
		return c;
	}
	
	public void addClasspathsForPropertiesId(String id) {
		if (id==null)
			return;
		SomeProperties props = API.propertiesManager.getPropertiesById(id);
		if (props == null)
			return;

		if (loadedIds.contains(props.id))
			return; // already loaded
		
		
		// adding classpaths...
		for (String cp : props.classpaths)
			try {
				super.addURL(new File(cp).toURI().toURL());
			} catch (MalformedURLException e) {
			}
		loadedIds.add(props.id);
		
		// if app, adding also classpaths for required engines
		
		if (props instanceof AppProperties) {
			
			for (String engineName : ((AppProperties)props).requires_engines) {
				if (!loadedIds.contains(engineName+".engine")) {
					EngineProperties eprops = API.propertiesManager.getEnginePropertiesByEngineName(engineName);
					if (eprops!=null) {
						for (String cp : eprops.classpaths)
							try {
								super.addURL(new File(cp).toURI().toURL());
							} catch (MalformedURLException e) {
							}
					}
					loadedIds.add(engineName+".engine");
				}
			}
			
		}		
		
	}

}
