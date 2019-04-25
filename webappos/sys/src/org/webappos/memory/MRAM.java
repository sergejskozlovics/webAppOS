package org.webappos.memory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.bridge.BridgeEventsCommandsHook;
import org.webappos.bridge.MultiSynchronizer;
import org.webappos.project.CloudProject;
import org.webappos.project.IProject;
import org.webappos.properties.AppProperties;
import org.webappos.server.API;
import org.webappos.server.APIForServerBridge;

import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;
import lv.lumii.tda.raapi.RAAPI_WR;

public class MRAM extends UnicastRemoteObject implements IMRAM, IRMRAM {
	
	public MRAM() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;
	private static Logger logger =  LoggerFactory.getLogger(MRAM.class);
	
	/*
	private static Thread shmsyncThread = new Thread() {
		@Override
		public void run() {
			for(;;) {
				try {
					Thread.sleep(10); // TODO: configure this
				} catch (InterruptedException e) {
					logger.error("SHM SYNC THREAD INTERRUPTED");
					break;
				}
				// shmsync all active contexts
			}
        }				
	};
	
	static {
		shmsyncThread.start();
	}*/
	
	private static class Slot {
		public Vector<RAAPI_Synchronizer> allSynchronizers = new Vector<RAAPI_Synchronizer>();
		public RAAPI_Synchronizer multiSynchronizer = new MultiSynchronizer(allSynchronizers);		
		public RAAPI_Synchronizer singleSynchronizer = null; 

		public IProject project = null;
		public RAAPI_WR raapi_wr = null;
		public Map<String, Runnable> ws_tokens = new HashMap<String, Runnable>(); // connected clients and their onFault runnables
		public Set<MRAM_Handle> handles = new HashSet<MRAM_Handle>(); // handles of connected clients		
		public Set<Long> usedPredefinedBitsValues = new HashSet<Long>();
		private long lastUsedPredefinedBits = 0;
		public AppProperties appProps = null;
		public boolean locked = false;

		public Map<String, String> cachedInstructionSetToWebProcessor = new HashMap<String, String>();
		
		public long newPredefinedBits() { // check for usedPredefinedBitsValues.size() < Config.max_users_per_project BEFORE calling newPredefinedBits() 	 
			for (;;) {
				if (lastUsedPredefinedBits>=API.config.max_users_per_project) {
					lastUsedPredefinedBits = 0;
				}
				else
					lastUsedPredefinedBits++;
				
				if (!usedPredefinedBitsValues.contains(lastUsedPredefinedBits)) {
					return lastUsedPredefinedBits;
				}
			}
		}
		
		public boolean init(boolean bootstrap, String app_url_name, String template, String project_id, String login, RAAPI_Synchronizer sync) {
			logger.trace("INIT MRAM Slot for "+project_id+"; login="+login+"; app_url_name="+app_url_name);
			
			appProps = API.propertiesManager.getAppPropertiesByUrlName(app_url_name);
			if (appProps == null) {
				logger.trace("Could not get app properties for "+app_url_name);
				return false;
			}
			
			project = new CloudProject();
			
			if (bootstrap) {
				if (!project.boostrap(appProps, project_id, login, sync, BridgeEventsCommandsHook.INSTANCE)) {
					logger.trace("bootstrap failed");
					return false;
				}
			}
			else {
				if (template!=null) {
					if (!project.createFromTemplate(appProps, template, project_id, login, sync, BridgeEventsCommandsHook.INSTANCE)) {
						logger.trace("not opened from template "+template);
						return false;
					}
				}
				else {					
					if (!project.open(appProps, project_id, login, sync, BridgeEventsCommandsHook.INSTANCE)) {
						logger.trace("not opened");
						return false;
					}
				}
			}			
			
			usedPredefinedBitsValues.add(0L); // our server-side repository 
			usedPredefinedBitsValues.add(1L); // the first client-side repository 
			lastUsedPredefinedBits=1;
			
			logger.trace("opened");
			raapi_wr = project.getTDAKernel().attachSynchronizer(multiSynchronizer, false, // we've synced during bootstrap/create/open
					-1); // this value will be ignored
			logger.trace("multi synchronizer attached");			
						
			return true;
		}
		
		public void done(boolean fault) {
			for (String instructionSet : cachedInstructionSetToWebProcessor.keySet()) {
				String webProcName = cachedInstructionSetToWebProcessor.get(instructionSet);
				APIForServerBridge.wpbServiceForServerBridge.clearCachedInstructionSet(this.project.getName(), instructionSet, webProcName);
			}
			cachedInstructionSetToWebProcessor.clear();
			
			if (project!=null) {
				project.close();
			}
			
			if (fault) {
				for (String k : ws_tokens.keySet()) {
					Runnable r = ws_tokens.get(k);
					if (r!=null) {
						try {
							r.run();
						}
						catch(Throwable t){							
						}
					}
				}
			}
			
			allSynchronizers.clear();
			project = null;
			raapi_wr = null;
			ws_tokens.clear();			
			handles.clear();
		}
		
	}
	
	private Map<String, Slot> projectIdToSlotMap = new HashMap<String, Slot>();
	
	public class MRAM_Handle {
		public String project_id;
		public String login;
		private String ws_token;
		public RAAPI_Synchronizer currentSynchronizer;
		public TDAKernel kernel; // with all synchronizers
		public RAAPI_WR raapi_wr; // without synchronizers
		public RAAPI_Synchronizer otherSynchronizers; // other synchronizers (excluding currentSynchronizer)
		private long predefinedBitsValues;
		
		public String getFullAppName() {
			Slot slot = projectIdToSlotMap.get(this.project_id);
			if (slot==null)
				return null;
			else
				return slot.appProps.app_full_name;
		}
	}
	

	synchronized public boolean lock(String project_id) {
		if (project_id==null)
			return true;
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot == null)
			return false;
		if (slot.locked)
			return false;
		slot.locked = true;
		return true;
	}
	
	synchronized public void unlock(String project_id) {
		if (project_id==null)
			return;
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot == null)
			return;
		slot.locked  = false;
	}
	
	synchronized public void setCachedInstructionSet(String project_id, String instructionSet, String wpID) {
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot == null)
			return;
		if (wpID == null)
			slot.cachedInstructionSetToWebProcessor.remove(instructionSet);
		else
			slot.cachedInstructionSetToWebProcessor.put(instructionSet, wpID); 		
	}
	
	synchronized public String getCachedInstructionSet(String project_id, String instructionSet) {
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot == null)
			return null;
		return slot.cachedInstructionSetToWebProcessor.get(instructionSet); 		
	}
	
	synchronized public RAAPI_Synchronizer getSingleSynchronizer(String project_id) {
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot == null)
			return null;
		return slot.singleSynchronizer; 		
	}

	synchronized public void setSingleSynchronizer(String project_id, RAAPI_Synchronizer singleSynchronizer) {
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot == null)
			return;
		slot.singleSynchronizer = singleSynchronizer; 		
	}
	
	synchronized public TDAKernel getTDAKernel(String project_id) {
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot == null)
			return null;
		if (slot.project==null)
			return null;
		return slot.project.getTDAKernel();
	}
	
	synchronized public String getProjectFolder(String project_id) {
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot == null)
			return null;
		if (slot.project==null)
			return null;
		return slot.project.getFolderName();
	}
	
	synchronized public String getProjectFullAppName(String project_id) {
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot==null)
			return null;
		else
			return slot.appProps.app_full_name;
	}
	
	private static Runnable emptyRunnable = new Runnable() {

		@Override
		public void run() {
		}
		
	};
	
	synchronized public boolean renameActiveProject(String project_id, String new_project_id) {
		if ((project_id==null) || (new_project_id==null))
			return false;
		
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot==null)
			return false;
		boolean b = slot.project.setName(new_project_id);
		if (b) {
			projectIdToSlotMap.remove(project_id);
			projectIdToSlotMap.put(new_project_id, slot);
			for (MRAM_Handle h : slot.handles) {
				h.project_id = new_project_id;
			}
			
			slot.multiSynchronizer.syncBulk(new double[]{0xEE, 0xFC}, new String[] { new_project_id });
		}
		return b;
	}
	
	// returns null if ack sync failed (need to re-connect without ack);
	// or e.g. when this ws_token is already used to connect to this project
	// TODO: throw exceptions to inform about errors in detail?
	synchronized public MRAM_Handle connectToMRAM(boolean bootstrap, String app_url_name, String template, String project_id, String login, String ws_token, long available_action_for_ack, RAAPI_Synchronizer sync, Runnable onFault) {
		if ((project_id==null) || (login==null) || (ws_token==null) || (sync==null))
			return null;
		
		if (onFault==null)
			onFault = emptyRunnable; 
		
		// TODO: currently, acks not implemented; assume available_action_for_ack=INFINITE (-1)
		// when it is implemented, it will send last ack to the client (for possible re-connect)
		if (available_action_for_ack >= 0)
			return null; // ack failed (currently, not implemented)
		
		Slot slot = null;
		if (!bootstrap && (template==null)) {
			// check, whether the project has already been loaded into MRAM
			slot = projectIdToSlotMap.get(project_id);
		}
		
		boolean inited = false;
		if (slot==null) {
				slot = new Slot();
				
				if (slot.ws_tokens.containsKey(ws_token))
					return null; // this user/browser already connected to this project_id
				
				if (slot.init(bootstrap, app_url_name, template, project_id, login, sync)) {
					project_id = slot.project.getName(); // bootstrap or creating from template could modify desired project_id
					projectIdToSlotMap.put(project_id, slot);
					
					inited = true;
				}
				else
					return null;
		}

		long newPredefinedBitsValues;
		if (inited) {
			slot.ws_tokens.put(ws_token, onFault);				
			slot.allSynchronizers.add(sync);
			newPredefinedBitsValues = 1; // the first client
		}
		else {
			if (slot.usedPredefinedBitsValues.size() >= API.config.max_users_per_project) {
				return null; // too many users connected
			}

			slot.ws_tokens.put(ws_token, onFault);				
			slot.allSynchronizers.add(sync);
		
			final Slot slot1 = slot;
			
			newPredefinedBitsValues = slot.newPredefinedBits();
			
			/*ForegroundThread.runInForegroundThread(new Runnable() {

				@Override
				public void run() {*/
					slot1.project.getTDAKernel().sync(sync, newPredefinedBitsValues); // sync for this additional synchronizer (for the first synchronizer we sync during init)				
				/*}
			}, login);*/
		}
		

		MRAM_Handle retVal = new MRAM_Handle();
		retVal.project_id = project_id;
		retVal.login = login;
		retVal.ws_token = ws_token;
		retVal.currentSynchronizer = sync;		
		retVal.kernel = slot.project.getTDAKernel();
		retVal.raapi_wr = slot.raapi_wr;
		retVal.otherSynchronizers = new MultiSynchronizer(slot.allSynchronizers, sync);
		retVal.predefinedBitsValues = newPredefinedBitsValues;
		
		slot.handles.add(retVal);
		return retVal;
	}

	// synchronizers MUST be disconnected on bridge socket close; otherwise, MRAM slot will remain occupied;
	// also, the user won't be able to reconnect to this project with the same ws_token;
	// the handle must be the same as returned by connectToMRAM 
	synchronized public void disconnectFromMRAM(MRAM_Handle h) {
		if (h==null)
			return;
		
		Slot slot = projectIdToSlotMap.get(h.project_id);
		if (slot==null)
			return;
		
		slot.usedPredefinedBitsValues.remove(h.predefinedBitsValues);
		slot.ws_tokens.remove(h.ws_token);
		slot.allSynchronizers.remove(h.currentSynchronizer);
		slot.handles.remove(h);
		if (slot.ws_tokens.isEmpty()) {
			// clearing MRAM slot
			slot.done(false/*no fault*/);
			projectIdToSlotMap.remove(h.project_id);
		}		
	}
	
	/**
	 * Clears the MRAM slot for the given project and disconnects all users by calling onFault runnables (within slot.done()).
	 * @param project_id the project_id for which to perform "MRAM fault" action.
	 */
	synchronized public void faultMRAM(String project_id) {
		Slot slot = projectIdToSlotMap.remove(project_id);
		if (slot==null)
			return;
		slot.done(true/*MRAM fault*/);
	}

	@Override
	public String getProjectFolder_R(String project_id) throws RemoteException {
		return this.getProjectFolder(project_id);
	}

	@Override
	public String getProjectFullAppName_R(String project_id) throws RemoteException {
		return this.getProjectFullAppName(project_id);
	}
	
	@Override
	public boolean renameActiveProject_R(String project_id, String new_project_id) throws RemoteException {
		return this.renameActiveProject(project_id, new_project_id);
	}

	@Override
	public void faultMRAM_R(String project_id) throws RemoteException {
		this.faultMRAM(project_id);
	}

	@Override
	public void syncChanges_R(String project_id, int nActions, double[] actions, String delimitedStrings) throws RemoteException {
		Slot slot = projectIdToSlotMap.get(project_id);
		if (slot == null)
			return;
		if (slot.project==null)
			return;
		TDAKernel kernel = slot.project.getTDAKernel();
		if (kernel == null)
			return;
		RAAPI_Synchronizer s = kernel.getSynchronizer();
		if (s==null)
			return;
		s.syncBulk(nActions, actions, delimitedStrings);
	}

	
	/*
	// should be called on bridge socket disconnect (for any reason)
	synchronized public void setLastAck(String project_id, String ws_token, long last_ack) {
		// stores into the registry (?)
	}*/
}
