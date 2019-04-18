package org.webappos.server;

import org.webappos.classloader.PropertiesClassLoader;
import org.webappos.email.IEmailSender;
import org.webappos.email.TLS_SMTP_EmailSender;
import org.webappos.fs.HomeFS;
import org.webappos.fs.IFileSystem;
import org.webappos.memory.IMRAM;
import org.webappos.memory.IMRAMWrapper;
import org.webappos.memory.MRAM;
import org.webappos.properties.IPropertiesManager;
import org.webappos.properties.IPropertiesManagerWrapper;
import org.webappos.properties.PropertiesManager;
import org.webappos.registry.CouchDBRegistry;
import org.webappos.registry.IRegistry;
import org.webappos.registry.IRegistryWrapper;
import org.webappos.registry.JsonFilesRegistry;
import org.webappos.status.IStatus;
import org.webappos.status.IStatusWrapper;
import org.webappos.status.InFileStatus;
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
 * Provides access to webAppOS Server-side API for accessing certain
 * server-side resources (such as MRAM, Registry, etc.), which are shared between
 * the server-side bridge and web processors (which are different processes).
 * The API class ensures synchronized access to the resources.
 * 
 * @author Sergejs Kozlovics
 *
 */
public class API {
	
	
	/**
	 * Initializes webAppOS Server-Side API within webAppOS Gate.
	 * In addition, initializes APIForServerBridge and Java RMI service for web processors.
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
			APIForServerBridge.dataMemoryForServerBridge = new MRAM();
		} catch (RemoteException e) {
		}
		dataMemory = APIForServerBridge.dataMemoryForServerBridge;
		
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
	 * Initializes webAppOS Server-Side API within a web processor.
	 * @param webProcessorID the web processor ID passed to it as a command-line argument
	 * @param webProcBusURL the URL of the Web Processor Bus Service for handling RMI requests
	 * @param wpAPI web processor RMI API (IRWebProcessor) for this web processor
	 * @param register whether to register the Web Processor within Web Processor Bus Service;
	 *                 if false, the web processor must be registered later by calling API.wpbService.registerWebProcessor
	 */
	public synchronized static void initAPI(String webProcessorID, String webProcBusURL, IRWebProcessor wpAPI, boolean register) { // initialize from a web processor
		try {
			wpbService = (IRWebProcessorBusService) Naming.lookup(webProcBusURL);

			wpbService.registerWebProcessor(webProcessorID, wpAPI);
		
			config = wpbService.getConfig();
			
			propertiesManager = new IPropertiesManagerWrapper(wpbService.getPropertiesManager());
			
		    dataMemory = new IMRAMWrapper(wpbService.getDataMemory());
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
	
	public static ConfigStatic config = null;
	
	// DATA
	public static IMRAM dataMemory = null;
	
	// CODE
	public static IWebCaller webCaller = null;	
	public static IPropertiesManager propertiesManager = null;
	public static PropertiesClassLoader classLoader = new PropertiesClassLoader();
	
	// DEVICES
	public static IRegistry registry = null;
	public static IStatus status = null;
	public static IFileSystem homeFSRoot = null;
	public static IEmailSender emailSender = null;
	
	// BUS
	public static IRWebProcessorBusService wpbService = null;	
}
