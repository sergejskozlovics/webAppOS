package org.webappos.webproc;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.memory.IRMRAM;
import org.webappos.properties.IRPropertiesManager;
import org.webappos.registry.IRRegistry;
import org.webappos.registry.IRStatus;
import org.webappos.server.API;
import org.webappos.server.APIForServerBridge;
import org.webappos.server.ConfigStatic;
import org.webappos.server.IShutDownListener;
import org.webappos.webcaller.IRWebCaller;
import org.webappos.webcaller.IWebCaller;

import lv.lumii.tda.kernel.TDAKernel;

public class WebProcessorBusService extends UnicastRemoteObject implements IRWebProcessorBusService {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger =  LoggerFactory.getLogger(WebProcessorBusService.class);
	
	private static class WebProcessorHandle {
		IRWebProcessor api = null;
		boolean available = false;
		boolean idle = true;
		WebProcTab.Row webproctabRow = null;
		IWebProcessorAdapter adapter = null;
		String id = null;
		Runnable onceWebCallFinished = null;
		CompletableFuture<String> onceResult = null;
		boolean faultMRAM = false;
		
		void fault() { // call on exception
			if (adapter!=null)
				adapter.disconnect(api);
			api = null;
			available = false;
			
			faultMRAM = true;
			
			if (onceWebCallFinished!=null) {
				onceWebCallFinished.run();
				onceWebCallFinished = null;
			}
			
			if (onceResult!=null) {
				onceResult.completeExceptionally(new Throwable("Web processor disconnected."));
				onceResult = null;
			}
			
						
			if (webproctabRow.reconnectMs<0) {
				API.status.setStatus("webproctab/"+id+"/status", "disconnected");
				API.status.setStatus("webproctab/"+id+"/message", "Disconnected unexpectedly.");
				// no need to reconnect..
				return;
			}
			
			if (webproctabRow.reconnectMs==0) {
				API.status.setStatus("webproctab/"+id+"/status", "connecting");
				API.status.setStatus("webproctab/"+id+"/message", "Disconnected unexpectedly. Reconnecting...");
				adapter.connect(webproctabRow.location, id);
			}
			else {
				API.status.setStatus("webproctab/"+id+"/status", "connecting");
				API.status.setStatus("webproctab/"+id+"/message", "Disconnected unexpectedly. Reconnecting in "+webproctabRow.reconnectMs+" ms...");
				
				new Thread() {
				    @Override
				    public void run() {
				        try {
				            Thread.sleep(webproctabRow.reconnectMs);
				        } catch (InterruptedException e) {
				        }
						API.status.setStatus("webproctab/"+id+"/status", "connecting");
						API.status.setStatus("webproctab/"+id+"/message", "Disconnected unexpectedly. Reconnecting...");
						adapter.connect(webproctabRow.location, id);
				    }
				}.start();
			}
		}
		
		
	}
	
	private Map<String, WebProcessorHandle> wpMap = new HashMap<String, WebProcessorHandle>();

	public WebProcessorBusService() throws RemoteException {
		super();
	}
	
	public void webproctabInit() {
		if (API.config.inline_webcalls) {
			logger.info("Ignoring webproctab, since all webcalls will be inline.");
			return;
		}
		WebProcTab webproctab = new WebProcTab(ConfigStatic.ETC_DIR+File.separator+"webproctab");
		
		for (WebProcTab.Row wpRow : webproctab.getRows()) {
			
			Class<?> c = null;
			try {
				c = Class.forName("org.webappos.adapters.webproc."+wpRow.type+".WebProcessorAdapter");
			}
			catch(Throwable t) {
				API.status.setStatus("webproctab/"+wpRow.name+"/status", "disconnected");
				API.status.setStatus("webproctab/"+wpRow.name+"/message", "Unsupported web processor type: "+wpRow.type+".");
				continue;
			}
					
			
			for (int i=0; i<wpRow.instances; i++) {
				// connecting...
				WebProcessorHandle info = new WebProcessorHandle();
				info.webproctabRow = wpRow;
				info.id = wpRow.name+" ("+i+")";
				try {
					info.adapter = (IWebProcessorAdapter) c.getConstructor().newInstance();
				}
				catch(Throwable t) {
					API.status.setStatus("webproctab/"+wpRow.name+"/status", "disconnected");
					API.status.setStatus("webproctab/"+wpRow.name+"/message", "Could not instantiate "+wpRow.type+" adapter."); 
					continue;
				}
				wpMap.put(info.id, info);
				API.status.setStatus("webproctab/"+info.id+"/status", "connecting");
				API.status.setStatus("webproctab/"+wpRow.name+"/message", "Initial connect request sent.");
				info.adapter.connect(wpRow.location, info.id);
			}
		}
		
		API.addShutDownListener(new IShutDownListener() {

			@Override
			public void onServerShutdown() {
				synchronized (WebProcessorBusService.class) {
					for (String wpId : wpMap.keySet()) {
						WebProcessorHandle h = wpMap.get(wpId);
						if (h==null)
							continue;
						logger.info("Disconnecting (on shutdown) web processor "+wpId); 
						if (h.adapter!=null)
							h.adapter.disconnect(h.api);
					}
					
				}
			}
			
		});
		
	}
	

	@Override
	public synchronized void registerWebProcessor(String id, IRWebProcessor wpAPI) throws RemoteException {
		WebProcessorHandle h = wpMap.get(id);
		if (h==null)
			return;
		h.available = true;
		h.idle = true;
		h.api = wpAPI;
		h.faultMRAM = false;
		API.status.setStatus("webproctab/"+id, "available");
/*		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		try {
		wpAPI.hasInstructionSet("test");
		wpAPI.disconnect(); // will throw exception always
		}
		catch(Throwable t) {
		//	t.printStackTrace();
		}
		*/
	}
	
	@Override
	public void webCallFinished(String webProcId, String result) throws RemoteException {
		WebProcessorHandle h = wpMap.get(webProcId);
		if (h==null)
			return;
		h.idle = true;
		
		// on web proc finish:
		if (h.onceWebCallFinished!=null) {
			h.onceWebCallFinished.run();
			h.onceWebCallFinished = null;
		}
		
		if (h.onceResult!=null) {
			if ((result!=null) && (result.startsWith("ERROR:")))
				h.onceResult.completeExceptionally(new Throwable(result.substring("ERROR:".length())));
			else
				h.onceResult.complete(result);
			h.onceResult = null;
		}
		
	}

	@Override
	public ConfigStatic getConfig() throws RemoteException {
		return API.config;
	} 

	@Override
	public IRPropertiesManager getPropertiesManager() throws RemoteException {
		return APIForServerBridge.propertiesManagerForServerBridge;
	}
	
	@Override
	public IRMRAM getMemory() throws RemoteException {
		return APIForServerBridge.memoryForServerBridge;
	}

	@Override
	public IRRegistry getRegistry() throws RemoteException {
		return APIForServerBridge.registryForServerBridge;
	}

	@Override
	public IRStatus getStatus() throws RemoteException {
		return APIForServerBridge.statusForServerBridge;
	}

	@Override
	public IRWebCaller getWebCaller() throws RemoteException {
		return APIForServerBridge.webCallerForServerBridge;
	}
	
	///// used only from the server bridge /////
	public synchronized boolean webCallToWebProcessor(IWebCaller.WebCallSeed seed, IWebCaller.WebCallDeclaration action) {
		for (String wpId : wpMap.keySet()) {
			// TODO: more efficient web processor chooser
			WebProcessorHandle h = wpMap.get(wpId);
			if ((h==null) || (h.api == null) || (!h.available)/* || (!h.idle)*/) // TODO: IDLE management
				continue;
			
			try {
				if (h.api.hasInstructionSet(action.resolvedInstructionSet)) {
					
					if (!h.idle)
						continue; // TODO: IDLE management
					
					if (!APIForServerBridge.memoryForServerBridge.lock(seed.project_id))
						continue;
					
					TDAKernel kernel = API.memory.getTDAKernel(seed.project_id);
					boolean newSingleSynchronizer = (kernel!=null) && (seed instanceof IWebCaller.SyncedWebCallSeed) && (APIForServerBridge.memoryForServerBridge.getSingleSynchronizer(seed.project_id)==null);
					if (newSingleSynchronizer) {
						APIForServerBridge.memoryForServerBridge.setSingleSynchronizer(seed.project_id, ((IWebCaller.SyncedWebCallSeed)seed).singleSynchronizer);
						((IWebCaller.SyncedWebCallSeed)seed).singleSynchronizer = null; // do not pass singleSyncrhonizer to the web processor
					}
					

					h.onceResult = seed.jsonResult;
					seed.jsonResult = null; // do not pass jsonResult to the web processor; we will use h.onceResult after the web processor finished the execution
					
					h.idle = false;
					// on web proc finish:
					if (newSingleSynchronizer) {
						h.onceWebCallFinished = new Runnable() {
							@Override
							public void run() {
								APIForServerBridge.memoryForServerBridge.setSingleSynchronizer(seed.project_id, null);
								APIForServerBridge.memoryForServerBridge.unlock(seed.project_id);
								WebProcessorHandle h = wpMap.get(wpId);
								if (h!=null && h.faultMRAM) {
									APIForServerBridge.memoryForServerBridge.faultMRAM(seed.project_id);
								}								
							}
						};
					}
					else {
						h.onceWebCallFinished = new Runnable() {
							@Override
							public void run() {
								APIForServerBridge.memoryForServerBridge.unlock(seed.project_id);
								WebProcessorHandle h = wpMap.get(wpId);
								if (h!=null && h.faultMRAM) {
									APIForServerBridge.memoryForServerBridge.faultMRAM(seed.project_id);
								}								
							}
						};						
					}
					
					h.api.startWebCall(seed, action);
					return true; // call submitted, all ok
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				h.fault();
			}
		}
		
		// no desired web processor found
		return false; // try this webcall later
		
	}


}
