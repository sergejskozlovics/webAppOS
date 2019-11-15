package org.webappos.properties;

public class WebLibraryProperties extends SomeProperties {
	private static final long serialVersionUID = 1L;

	public String on_load = "";	

	public WebLibraryProperties(String fullLibName, String fname) {
		super(fullLibName, fname);
		
		on_load = properties.getProperty("on_load", on_load);
		
	}
}
