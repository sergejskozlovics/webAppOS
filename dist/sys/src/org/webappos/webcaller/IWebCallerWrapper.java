package org.webappos.webcaller;

import java.rmi.RemoteException;
import java.util.Map;

import org.webappos.server.API;
import org.webappos.webmem.IWebMemory;


public class IWebCallerWrapper implements IWebCaller {
	private IRWebCaller delegate;

	public IWebCallerWrapper(IRWebCaller _delegate) {
		delegate = _delegate;
	}
	
	public void enqueue(WebCallSeed seed) {
		if (seed.project_id!=null) {
			IWebMemory webmem = API.dataMemory.getWebMemory(seed.project_id);
			if (webmem!=null)
				webmem.flush();
		}
		try {
			delegate.enqueue_R(seed);
		} catch (RemoteException e) {
		}
	}

	@Override
	public boolean invokeNow(WebCallSeed seed) {
		if (seed.project_id!=null) {
			IWebMemory webmem = API.dataMemory.getWebMemory(seed.project_id);
			if (webmem!=null)
				webmem.flush();
		}
		try {
			return delegate.invokeNow_R(seed);
		} catch (RemoteException e) {
			return false;
		}
	}
	
	public boolean webCallExists(String actionName){
		try {
			return delegate.webCallExists_R(actionName);
		} catch (RemoteException e) {
			return false;
		}
	}

	@Override
	public WebCallDeclaration getWebCallDeclaration(String actionName) {
		try {
			return delegate.getWebCallDeclaration_R(actionName);
		} catch (RemoteException e) {
			return null;
		}
	}

	@Override
	public Map<String, WebCallDeclaration> getWebCalls(String fullAppName) {
		try {
			return delegate.getWebCalls_R(fullAppName);
		} catch (RemoteException e) {
			return null;
		}
	}

	@Override
	public int getQueueSize(String project_id) {
		if (project_id==null)
			return 0;
		
		IWebMemory webmem = API.dataMemory.getWebMemory(project_id);
		if (webmem!=null)
			webmem.flush();
		try {
			return delegate.getQueueSize_R(project_id);
		} catch (RemoteException e) {
			return 0;
		}
	}


}
