package org.webappos.webmem;

import java.rmi.RemoteException;

import org.webappos.bridge.BridgeEventsCommandsHook;
import org.webappos.project.IProject;
import org.webappos.server.API;

import lv.lumii.tda.kernel.TDAKernel;

public class IWebMemoryAreaWrapper implements IWebMemoryArea {
	
	private IRWebMemoryArea delegate; 
	
	private String cached_project_id = null;
	private TDAKernel cached_kernel = null;
	
	public IWebMemoryAreaWrapper(IRWebMemoryArea _delegate) {
		delegate = _delegate;
	}

	@Override
	public TDAKernel getWebMemory(String project_id) {
		if (project_id==null)
			return null;
		if (project_id.equals(cached_project_id))
			return cached_kernel;
		
		if (cached_kernel!=null)
			cached_kernel.close();
		
		cached_project_id = project_id;
		cached_kernel = null;
		
		String folder = API.dataMemory.getProjectFolder(project_id);
		if (folder == null)
			return null;
		
		cached_kernel = new TDAKernel();
		System.err.println("CLIENT KERNEL "+IProject.CLIENT_REPOSITORY+":"+folder);
		if (!cached_kernel.open(IProject.CLIENT_REPOSITORY+":"+folder)) {
			cached_kernel = null;
			System.err.println("CLIENT KERNEL OBLOM");
			return null;
		}
		
		cached_kernel.attachSynchronizer(new SynchronizerToIRWebMemoryArea(project_id, delegate), false, -1/*ignored*/);
		cached_kernel.setEventsCommandsHook(BridgeEventsCommandsHook.INSTANCE);
		
		System.err.println("CLIENT KERNEL OK");
		return cached_kernel;
	}
	
	@Override
	public String getProjectFolder(String project_id) {
		try {
			return delegate.getProjectFolder_R(project_id);
		} catch (RemoteException e) {
			return null;
		}
	}

	@Override
	public String getProjectFullAppName(String project_id) {
		try {
			return delegate.getProjectFullAppName_R(project_id);
		} catch (RemoteException e) {
			return null;
		}
	}

	@Override
	public boolean renameActiveProject(String project_id, String new_project_id) {
		try {
			return delegate.renameActiveProject_R(project_id, new_project_id);
		} catch (RemoteException e) {
			return false;
		}
	}
	
	@Override
	public void webMemoryFault(String project_id) {
		try {
			delegate.webMemoryFault_R(project_id);
		} catch (RemoteException e) {
		}
	}



}
