package org.webappos.webcaller;

import java.rmi.RemoteException;
import java.util.Map;

import org.webappos.server.API;

import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public class IWebCallerWrapper implements IWebCaller {
	private IRWebCaller delegate;

	public IWebCallerWrapper(IRWebCaller _delegate) {
		delegate = _delegate;
	}
	
	public void enqueue(WebCallSeed seed) {
		if (seed.project_id!=null) {
			TDAKernel kernel = API.dataMemory.getTDAKernel(seed.project_id);
			if (kernel!=null) {
				RAAPI_Synchronizer s = kernel.getSynchronizer();
				if (s!=null)
					s.flush();
			}
		}
		try {
			delegate.enqueue_R(seed);
		} catch (RemoteException e) {
		}
	}

	@Override
	public boolean invokeNow(WebCallSeed seed) {
		if (seed.project_id!=null) {
			TDAKernel kernel = API.dataMemory.getTDAKernel(seed.project_id);
			if (kernel!=null) {
				RAAPI_Synchronizer s = kernel.getSynchronizer();
				if (s!=null)
					s.flush();
			}
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


}
