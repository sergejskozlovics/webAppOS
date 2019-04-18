package org.webappos.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import org.webappos.properties.AppProperties;
import org.webappos.properties.EngineProperties;
import org.webappos.properties.SomeProperties;
import org.webappos.server.API;

/**
 * A custom webAppOS class loader used by server-side web processors to find classes
 * of webAppOS apps, services, and engines.
 * Takes into a consideration classpaths configured in app.properties, service.properties, and engine.properties,
 * if those classpaths have been added using {@link addClasspathsForPropertiesId}.
 * 
 * @author Sergejs Kozlovics
 *
 */
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
	
	/**
	 * Loads the given class taking into a consideration classpaths added using {@link addClasspathsForPropertiesId}.
	 * @param fullName class full name, e.g., "org.example.MyClass"
	 * @return the loaded Java class, or null on error
	 */
	public Class<?> findClassByName(String fullName) {
		Class<?> c = findLoadedClass(fullName);
		if (c!=null)
			return c;
		try {
			c = this.loadClass(fullName);
		} catch (ClassNotFoundException e1) {
		}
		return c;
	}
	
	/**
	 * Adds Java classpaths configured for the given app, service or engine. 
	 * @param id [appName].app or [serviceName].service or [engineName].engine
	 */
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

	private static Set<String> luaDirsLoaded = new HashSet<String>();
	
	public void loadLuaClasses(String path) {
		File ff = new File(path);
		if (!ff.exists())
			return;
		String cp = path;
		try {
			cp = ff.getCanonicalPath();
		} catch (IOException e) {
		}
		if (luaDirsLoaded.contains(cp))
			return; // already loaded
		System.out.println("Loading Lua classes from "+cp);
		luaDirsLoaded.add(cp);
				
		File dir = new File(path);
		for (File f : dir.listFiles()) {
			if (f.isDirectory())
				loadLuaClasses(path+"/"+f.getName());
			else {
				String fname = f.getName();
				if (fname.endsWith(".class")) {
					fname = fname.substring(0, fname.length()-6);
					byte[] bytes;
					try {
						bytes = Files.readAllBytes(f.toPath());
						Class<?> c = null;
						c = findLoadedClass(fname);
						if (c==null)
							c = defineClass(fname, bytes, 0, bytes.length);
						resolveClass(c);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
	}
	
}
