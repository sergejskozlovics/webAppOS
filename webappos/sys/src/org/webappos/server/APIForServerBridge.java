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
	public static MRAM memoryForServerBridge = null;
	public static PropertiesManager propertiesManagerForServerBridge = null;
	public static IRRegistry registryForServerBridge = null;
	public static InFileStatus statusForServerBridge = null;
	public static WebCaller webCallerForServerBridge = null;	
	public static WebProcessorBusService wpbServiceForServerBridge = null;
}
