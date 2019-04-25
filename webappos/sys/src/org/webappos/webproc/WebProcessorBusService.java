package org.webappos.webproc;

import java.io.File;
import java.lang.reflect.Method;
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
import org.webappos.server.API;
import org.webappos.server.APIForServerBridge;
import org.webappos.server.ConfigStatic;
import org.webappos.server.IShutDownListener;
import org.webappos.status.IRStatus;
import org.webappos.webcaller.IJsonWebCallsAdapter;
import org.webappos.webcaller.IRWebCaller;
import org.webappos.webcaller.ITdaWebCallsAdapter;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.IWebCaller.CallingConventions;

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
				adapter.connect(webproctabRow.location, id, webproctabRow.options);
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
						adapter.connect(webproctabRow.location, id, webproctabRow.options);
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
				info.adapter.connect(wpRow.location, info.id, info.webproctabRow.options);
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
	public IRMRAM getDataMemory() throws RemoteException {
		return APIForServerBridge.dataMemoryForServerBridge;
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
		String cached_wp = null;
		if (seed.project_id!=null)
			cached_wp = APIForServerBridge.dataMemoryForServerBridge.getCachedInstructionSet(seed.project_id, action.resolvedInstructionSet);
		
		
		if (API.config.inline_webcalls || action.isInline) {
			// executing webcall here, without web processors
			
			if (!API.config.inline_webcalls) {
				if ((cached_wp!=null) && !"inline".equals(cached_wp)) {
					// clearing cache for the web processor who created it earlier...
					this.clearCachedInstructionSet(seed.project_id, action.resolvedInstructionSet, cached_wp);
					APIForServerBridge.dataMemoryForServerBridge.setCachedInstructionSet(seed.project_id, action.resolvedInstructionSet, null);
					cached_wp = null;
				}
				
			}
			
			TDAKernel kernel = API.dataMemory.getTDAKernel(seed.project_id);
			boolean newSingleSynchronizer = (kernel!=null) && (seed instanceof IWebCaller.SyncedWebCallSeed);
			if (newSingleSynchronizer) {
				APIForServerBridge.dataMemoryForServerBridge.setSingleSynchronizer(seed.project_id, ((IWebCaller.SyncedWebCallSeed)seed).singleSynchronizer);
			}
			
			Class<?> adapterClass = null;
			Object adapter = null;
			Method m = null;
					
			try {
				adapterClass = Class.forName("org.webappos.adapters.webcalls."+action.resolvedInstructionSet+".WebCallsAdapter");					
				adapter = adapterClass.getConstructor().newInstance();
			}
			catch(Throwable t) {					
			}
			try {
				m = adapterClass.getMethod("clearCache", String.class);
			}
			catch(Throwable t) {					
			}
			
			if (adapter == null)
				return true; // the caller will not try to re-enqueue later
			
			if (cached_wp == null) {
				if ((seed.project_id!=null) && (m!=null)) {
					// associating instruction set of the given project with the "inline" web processor...
					APIForServerBridge.dataMemoryForServerBridge.setCachedInstructionSet(seed.project_id, action.resolvedInstructionSet, "inline");
					cached_wp = "inline";
				}
			}
			
											
			if ((seed.callingConventions == CallingConventions.JSONCALL) && (adapter instanceof IJsonWebCallsAdapter)) {
				boolean newOwner = false;
				if (kernel!=null && kernel.getOwner()==null) {
					newOwner = true;
					TDAKernel.Owner owner = new TDAKernel.Owner();
					owner.login = seed.login;
					owner.project_id = seed.project_id;
					kernel.setOwner(owner);
				}
				try {
					String res = ((IJsonWebCallsAdapter)adapter).jsoncall(action.resolvedLocation, seed.jsonArgument, seed.project_id, API.dataMemory.getProjectFullAppName(seed.project_id), seed.login);
					if (seed.jsonResult!=null)
						seed.jsonResult.complete(res);
				}
				catch(Throwable t) {
					if (seed.jsonResult!=null)
						seed.jsonResult.completeExceptionally(t);
				}
				if (newOwner)
					kernel.setOwner(null);
			}
			else
			if ((seed.callingConventions == CallingConventions.TDACALL) && (adapter instanceof ITdaWebCallsAdapter)) {
				boolean newOwner = false;
				if (kernel!=null && kernel.getOwner()==null) {
					newOwner = true;
					TDAKernel.Owner owner = new TDAKernel.Owner();
					owner.login = seed.login;
					owner.project_id = seed.project_id;
					kernel.setOwner(owner);
				}
				try {
					((ITdaWebCallsAdapter)adapter).tdacall(action.resolvedLocation, seed.tdaArgument, kernel, seed.project_id, API.dataMemory.getProjectFullAppName(seed.project_id), seed.login);
					if (seed.jsonResult!=null)
						seed.jsonResult.complete(null);
				}
				catch(Throwable t) {
					if (seed.jsonResult!=null)
						seed.jsonResult.completeExceptionally(t);
				}
				if (newOwner)
					kernel.setOwner(null);
			}
			else {
				logger.error("Count not peform server-side web call "+seed.actionName+" since calling conventions do not match. ");
			}
			
			// on web proc finish:
			if (newSingleSynchronizer) {
				APIForServerBridge.dataMemoryForServerBridge.setSingleSynchronizer(seed.project_id, null);
			}

			return true;
		} // inline
		
		for (String wpId : wpMap.keySet()) {
			if (cached_wp != null) {
				if (!wpId.equals(cached_wp))
					continue; // TODO: more efficient check for cached_wp, i.e., w/o the for cycle
			}
			
			// TODO: more efficient web processor chooser; w/o the for cycle?
			
			WebProcessorHandle h = wpMap.get(wpId);
			if ((h==null) || (h.api == null) || (!h.available)/* || (!h.idle)*/) { // TODO: IDLE management
				
				if (cached_wp!=null) {
					// cleanup cache, since the web processor is not available...
					APIForServerBridge.dataMemoryForServerBridge.setCachedInstructionSet(seed.project_id, action.resolvedInstructionSet, null);
					cached_wp = null;
				}
				
				continue;
			}
			
			try {
				if (h.api.hasInstructionSet(action.resolvedInstructionSet)) {
					
					if (!h.idle)
						continue; // TODO: IDLE management
					
					if (cached_wp == null) {
						if ((seed.project_id!=null) && h.api.perProjectCachedInstructionSet(action.resolvedInstructionSet)) {
							// associating instruction set of the given project with the current web processor...
							APIForServerBridge.dataMemoryForServerBridge.setCachedInstructionSet(seed.project_id, action.resolvedInstructionSet, wpId);
							cached_wp = wpId;
						}
					}
					
					if (!APIForServerBridge.dataMemoryForServerBridge.lock(seed.project_id))
						continue;
					
					TDAKernel kernel = API.dataMemory.getTDAKernel(seed.project_id);
					boolean newSingleSynchronizer = (kernel!=null) && (seed instanceof IWebCaller.SyncedWebCallSeed) && (APIForServerBridge.dataMemoryForServerBridge.getSingleSynchronizer(seed.project_id)==null);
					if (newSingleSynchronizer) {
						APIForServerBridge.dataMemoryForServerBridge.setSingleSynchronizer(seed.project_id, ((IWebCaller.SyncedWebCallSeed)seed).singleSynchronizer);
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
								APIForServerBridge.dataMemoryForServerBridge.setSingleSynchronizer(seed.project_id, null);
								APIForServerBridge.dataMemoryForServerBridge.unlock(seed.project_id);
								WebProcessorHandle h = wpMap.get(wpId);
								if (h!=null && h.faultMRAM) {
									APIForServerBridge.dataMemoryForServerBridge.faultMRAM(seed.project_id);
								}								
							}
						};
					}
					else {
						h.onceWebCallFinished = new Runnable() {
							@Override
							public void run() {
								APIForServerBridge.dataMemoryForServerBridge.unlock(seed.project_id);
								WebProcessorHandle h = wpMap.get(wpId);
								if (h!=null && h.faultMRAM) {
									APIForServerBridge.dataMemoryForServerBridge.faultMRAM(seed.project_id);
								}								
							}
						};						
					}
					
					System.out.println("!!!Running ["+action.resolvedInstructionSet+"] web call for "+seed.project_id+" at "+wpId+" for "+seed.actionName+"/"+action.resolvedLocation);
					h.api.startWebCall(seed, action);
					return true; // call submitted, all ok
				}
			} catch (RemoteException e) {
				logger.error("Exception during web processor call in `"+h.id+"', faulting ("+e.toString()+"; "+e.getMessage()+")");
				h.fault();
			}
		}
		
		// no desired web processor found
		return false; // try this webcall later
		
	}

	public synchronized void clearCachedInstructionSet(String project_id, String instructionSet, String wpName) {
		if (API.config.inline_webcalls || "inline".equals(wpName)) {
			Class<?> adapterClass = null;
			try {
				adapterClass = Class.forName("org.webappos.adapters.webcalls."+instructionSet+".WebCallsAdapter");					
				Method m = adapterClass.getMethod("clearCache", String.class);
				if (m!=null)
					m.invoke(null, project_id);
			}
			catch(Throwable t) {
				t.printStackTrace();
			}			
			return;
		}
		
		WebProcessorHandle h = wpMap.get(wpName);
		if ((h==null) || (h.api == null) || (!h.available)/* || (!h.idle)*/) { // TODO: IDLE management
			return;
		}
		
		try {
			h.api.clearCachedInstructionSet(project_id, instructionSet);
		} catch (RemoteException e) {
			logger.error("Exception during web processor clear cache in `"+h.id+"', faulting ("+e.toString()+"; "+e.getMessage()+")");
			h.fault();
		}
	}

}
