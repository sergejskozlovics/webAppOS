package org.webappos.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.webcaller.WebCaller;

public class SomeProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Logger logger =  LoggerFactory.getLogger(SomeProperties.class);
	
	public long version = System.currentTimeMillis();
	public String id;
	public Properties properties = new Properties(); 
	public List<String> classpaths = new ArrayList<String>();
	public List<String> webcallsFiles = new ArrayList<String>();
	
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
		
		String sDir = fDir.getAbsolutePath();
		
		String defVal = new File(sDir+File.separator+"bin").isDirectory()?"bin":"";
		String s = properties.getProperty("classpaths", defVal);
		String arr[] = s.split(";");
		for (String cp : arr)
			if (!cp.isEmpty())
				classpaths.add(sDir+File.separator+cp);
								
		for (String fname : fDir.list()) {
			if (fname.endsWith(WebCaller.FILE_EXTENSION)) {
				webcallsFiles.add(sDir+File.separator+fname);
			}
		}
		
	}
	
}
