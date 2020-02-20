package org.webappos.properties;

import java.io.File;

public class WebLibraryProperties extends SomeProperties {
	private static final long serialVersionUID = 1L;

	public String on_load = "";	

	public WebLibraryProperties(String fullLibName, String dir) {
		super(fullLibName, dir+File.separator+"weblibrary.properties");
		
		on_load = properties.getProperty("on_load", on_load);
		
	}
}
