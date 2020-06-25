package org.webappos.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.webcaller.WebCaller;

/**
 * @author Sergejs Kozlovics
 *
 * This class is a superclass for properties. It has 3 subclasses for
 * storing properties for apps, services, and web libraries.
 * 
 */
public class SomeProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger logger =  LoggerFactory.getLogger(SomeProperties.class);
	
	public long version = System.currentTimeMillis();
	
	/**
	 * The id of these properties. Equals to the full webapp/webservice/weblibrary name (with the corresponding extension).
	 */
	public String id;
	/**
	 * Java Properties object (loaded from the .properties file)
	 */
	public Properties properties = new Properties();
	
	/**
	 * Classpaths that have to be added by dependent apps, services, and libraries to perform
	 * Java webcalls.
	 */
	public List<String> classpaths = new ArrayList<String>();
	/**
	 * The list of found .webcalls files. These files are loaded by Gate.
	 */
	public List<String> webcallsFiles = new ArrayList<String>();
	
	/**
	 * The list of full names (with extensions) of directly required apps, services, and web libraries.  
	 */
	public List<String> requires = new ArrayList<String>();
	
	/**
	 * The list of ALL required apps, services, and web libraries (computed by traversing this.requires recursively).
	 */
	public List<String> all_required = new ArrayList<String>(); // from requires + recursive
	/**
	 * The list of ALL required web libraries (computed by traversing this.requires recursively, and keeping only web libraries).
	 */
	public List<String> all_required_web_libraries = new ArrayList<String>(); // extracted from requires + recursive
	
	public String[] auto_webcalls = {};
	
	private static void collectAllRequiredWebLibraries(SomeProperties props, List<String> list) {
		if (props == null || list == null)
			return;
		for (String dep : props.requires) {
			collectAllRequiredWebLibraries(API.propertiesManager.getPropertiesByFullName(dep), list);
			if (dep.endsWith(".weblibrary"))
				if (!list.contains(dep))
					list.add(dep);
		}		
	}

	private static void collectAllRequired(SomeProperties props, List<String> list) {
		if (props == null || list == null)
			return;
		for (String dep : props.requires) {
			collectAllRequired(API.propertiesManager.getPropertiesByFullName(dep), list);
			if (!list.contains(dep))
				list.add(dep);
		}		
	}

	public SomeProperties(String _id, String fileName) {
		id = _id;
		try {
			properties.load(new FileInputStream(fileName));
		}
		catch(Throwable t) {
			logger.warn("Could not load properties from file "+fileName);
		}
		
		File fDir;
		try {
			fDir = new File(fileName).getParentFile();
		}
		catch(Throwable t) {
			return;
		}
		if (!fDir.exists())
			return;
		
		String sDir = fDir.getAbsolutePath();
		
		String defVal = new File(sDir+File.separator+"bin").isDirectory()?"bin":"";
		String s = properties.getProperty("classpaths", defVal);
		String arr[] = s.split(";");
		for (String cp : arr)
			if (!cp.isEmpty()) {
				if (cp.endsWith("/*") || cp.endsWith("\\*") || cp.equals("*")) {
					while (cp.endsWith("*") || cp.endsWith("/") || cp.endsWith("\\")) {
						cp = cp.substring(0, cp.length()-1);
					}
					File d = new File(sDir+File.separator+cp);
					if (d.exists() && d.isDirectory()) 
						for (String fname : d.list()) {
							if (fname.toLowerCase().endsWith(".jar")) {
								classpaths.add(sDir+File.separator+cp+File.separator+fname);
							}
						}
				}
				else
					classpaths.add(sDir+File.separator+cp);
			}
								
		for (String fname : fDir.list()) {
			if (fname.endsWith(WebCaller.FILE_EXTENSION)) {
				webcallsFiles.add(sDir+File.separator+fname);
			}
		}

		String _requires = properties.getProperty("requires", "").trim();			
		for (String depName : _requires.split(",")) {
			depName = depName.trim();
			if (!depName.isEmpty()) {
				requires.add(depName);
			}
		}
		
		collectAllRequiredWebLibraries(this, this.all_required_web_libraries);
		collectAllRequired(this, this.all_required);
	}
	
}
