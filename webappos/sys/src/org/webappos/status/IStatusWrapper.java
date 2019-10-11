package org.webappos.status;

import org.webappos.registry.IRegistryWrapper;

public class IStatusWrapper extends IRegistryWrapper implements IStatus {
	
	public IStatusWrapper(IRStatus _delegate) {
		super(_delegate);
	}

}
