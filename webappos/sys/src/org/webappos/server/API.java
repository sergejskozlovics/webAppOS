package org.webappos.server;

import org.webappos.classloader.PropertiesClassLoader;
import org.webappos.fs.HomeFS;
import org.webappos.fs.IFileSystem;
import org.webappos.memory.IMRAM;
import org.webappos.memory.IMRAMWrapper;
import org.webappos.memory.MRAM;
import org.webappos.properties.IPropertiesManager;
import org.webappos.properties.IPropertiesManagerWrapper;
import org.webappos.properties.PropertiesManager;
import org.webappos.registry.CouchDBRegistry;
import org.webappos.registry.IRRegistry;
import org.webappos.registry.IRegistry;
import org.webappos.registry.IRegistryWrapper;
import org.webappos.registry.IStatus;
import org.webappos.registry.IStatusWrapper;
import org.webappos.registry.InFileStatus;
import org.webappos.registry.JsonFilesRegistry;
import org.webappos.util.PID;
import org.webappos.util.Ports;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.IWebCallerWrapper;
import org.webappos.webcaller.WebCaller;
import org.webappos.webproc.IRWebProcessor;
import org.webappos.webproc.IRWebProcessorBusService;
import org.webappos.webproc.WebProcessorBusService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * The main entry point to access webAppOS Server-Side API. The API class
 * encapsulates the most important server-side APIs accessible via static fields.<br>
 * These fields can be used to access certain
 * server-side resources (such as MRAM, Registry, etc.), which are shared between
 * the server-side bridge and web processors (which are different processes).
 * This API class, after initialized, ensures synchronized access to the resources.<br>
 * 
 * @author Sergejs Kozlovics
 *
 */
public class API {
	
	
	/**
	 * Initializes webAppOS Server-Side API for use inside of webAppOS Gate and Bridge.<br>
	 * As part of the initialization process, starts Web Processor Bus Service to be used 
	 * by web processors.<br>
	 * Besides initializing the API class, this function also initializes the internal
	 * APIForServerBridge class, which provides extended API available to Gate and Bridge,
	 * but not available to web processors.
	 */
	public synchronized static void initAPI() { 
		APIForServerBridge.configForServerBridge = new ConfigEx();
		config = APIForServerBridge.configForServerBridge; 
		
		try {
			APIForServerBridge.propertiesManagerForServerBridge = new PropertiesManager();
		} catch (RemoteException e1) {
		}
		propertiesManager = APIForServerBridge.propertiesManagerForServerBridge;
		
		try {
			APIForServerBridge.memoryForServerBridge = new MRAM();
		} catch (RemoteException e) {
		}
		memory = APIForServerBridge.memoryForServerBridge;
		
		try {
			if ((config.registry_url!=null) && (!config.registry_url.trim().isEmpty())) {
				APIForServerBridge.registryForServerBridge = new CouchDBRegistry(config.registry_url);
				registry = (IRegistry)APIForServerBridge.registryForServerBridge;
			}
			else {
				APIForServerBridge.registryForServerBridge = new JsonFilesRegistry();
				registry = (IRegistry)APIForServerBridge.registryForServerBridge;
			}
		} catch (RemoteException e) {
		}
		
		try {
			APIForServerBridge.statusForServerBridge = new InFileStatus();
		} catch (RemoteException e) {
		} 
		status = APIForServerBridge.statusForServerBridge;
		
		status.setStatus("server/internal/PID", PID.getPID()+"");
		
		try {
			APIForServerBridge.webCallerForServerBridge = new WebCaller();
		} catch (RemoteException e) {
		}
		webCaller = APIForServerBridge.webCallerForServerBridge;
		
		homeFSRoot = HomeFS.ROOT_INSTANCE;
		emailSender = new TLS_SMTP_EmailSender(API.config.smtp_server, API.config.smtp_auth, API.config.smtp_from, API.config.smtp_from_name);
		
		// Creating RMI service
		if (wpbService == null) {
			try {
				APIForServerBridge.wpbServiceForServerBridge = new WebProcessorBusService();			
				
				int i=0;
				while ((i<10) && (Ports.portTaken(API.config.web_processor_bus_service_port+i))) {
					i++;
				}				
				API.config.web_processor_bus_service_port+=i;
				

				Registry registry = LocateRegistry.createRegistry(API.config.web_processor_bus_service_port);
		        registry.rebind(ConfigStatic.WEB_PROCESSOR_BUS_SERVICE_NAME, APIForServerBridge.wpbServiceForServerBridge);
		        APIForServerBridge.wpbServiceForServerBridge.webproctabInit();
		        wpbService = APIForServerBridge.wpbServiceForServerBridge;
		        status.setStatus("server/internal/web_processor_bus_service_port", API.config.web_processor_bus_service_port+"");
			}
			catch(Throwable t) {				
				APIForServerBridge.wpbServiceForServerBridge = null;
				status.setStatus("server/internal/web_processor_bus_service_port", "ERROR:"+t.getMessage());
				return;
			}
		}
		
	}
	
	/**
	 * Initializes webAppOS Server-Side API for use in a web processors.<br>
	 * Each web processor must call initAPI with the appropriate arguments.
	 * @param webProcessorID the web processor ID passed to it as a command-line argument
	 * @param webProcBusURL the URL of the Web Processor Bus Service for handling RMI requests
	 * @param wpAPI web processor RMI API (IRWebProcessor) for this web processor
	 */
	public synchronized static void initAPI(String webProcessorID, String webProcBusURL, IRWebProcessor wpAPI) { // initialize from a web processor
		try {
			wpbService = (IRWebProcessorBusService) Naming.lookup(webProcBusURL);

			wpbService.registerWebProcessor(webProcessorID, wpAPI);
		
			config = wpbService.getConfig();
			
			propertiesManager = new IPropertiesManagerWrapper(wpbService.getPropertiesManager());
			
		    memory = new IMRAMWrapper(wpbService.getMemory());
		    registry = new IRegistryWrapper(wpbService.getRegistry());
		    status = new IStatusWrapper(wpbService.getStatus());

		    status.setStatus("webproctab/"+webProcessorID+"/PID", PID.getPID()+"");
		    
		    webCaller = new IWebCallerWrapper(wpbService.getWebCaller());
		    
		}
		catch(Throwable t) {
			return;
		}
		
		homeFSRoot = HomeFS.ROOT_INSTANCE;
		emailSender = new TLS_SMTP_EmailSender(API.config.smtp_server, API.config.smtp_auth, API.config.smtp_from, API.config.smtp_from_name);
	}
	
	private static Set<IShutDownListener> listeners = ConcurrentHashMap.newKeySet();

	
	/**
	 * Registers a shutdown listener, which will be called when the current Java Virtual Machine
	 * is terminated.<br>
	 * Can be used by web processor adapters and Web Processor Bus Service to terminate
	 * running local web processors, when the server (Gate) is being terminated.
	 * @param l
	 */
	public synchronized static void addShutDownListener(IShutDownListener l) {
		listeners.add(l);
	}
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() 
	    { 
	      public void run() 
	      {
    		  for (IShutDownListener l : listeners) {
    			  l.onServerShutdown();
    		  }
	      } 
	    }); 		
	}
	
	/**
	 * Stores loaded webAppOS configuration.
	 */
	public static ConfigStatic config = null;	
	
	/**
	 * An entry point to access properties of installed webAppOS apps, engines, and services.
	 */
	public static IPropertiesManager propertiesManager = null;
	
	/**
	 * An entry point to access 
	 */
	public static IMRAM memory = null;
	public static IRegistry registry = null;
	public static IStatus status = null;
	public static IWebCaller webCaller = null;
	
	public static PropertiesClassLoader classLoader = new PropertiesClassLoader();
	public static IFileSystem homeFSRoot = null;
	public static IEmailSender emailSender = null;
	
	public static IRWebProcessorBusService wpbService = null;	
}
