package org.webappos.server;

import org.webappos.memory.MRAM;
import org.webappos.properties.PropertiesManager;
import org.webappos.registry.IRRegistry;
import org.webappos.status.InFileStatus;
import org.webappos.webcaller.WebCaller;
import org.webappos.webproc.WebProcessorBusService;

/**
 * Provides access to webAppOS Server-side extended API,
 * which is available to server-side bridge, but not for web processors.
 * 
 * @author Sergejs Kozlovics
 *
 */
public class APIForServerBridge {	
	public static ConfigEx configForServerBridge = null;
	
	// DATA
	public static MRAM dataMemoryForServerBridge = null;

	// CODE
	public static WebCaller webCallerForServerBridge = null;	
	public static PropertiesManager propertiesManagerForServerBridge = null;
	
	// DEVICES
	public static IRRegistry registryForServerBridge = null;
	public static InFileStatus statusForServerBridge = null;
	
	// BUS
	public static WebProcessorBusService wpbServiceForServerBridge = null;
}
