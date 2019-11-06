package org.webappos.properties;

public class EngineProperties extends SomeProperties {
	private static final long serialVersionUID = 1L;

	public EngineProperties(String engineName, String fname) {
		super(engineName, fname);
		if (!engineName.endsWith(".engine")) {
			engineName = engineName+".engine";
			id = engineName;
		}
	}
}
