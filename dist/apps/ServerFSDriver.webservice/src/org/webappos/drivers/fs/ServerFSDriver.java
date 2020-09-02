package org.webappos.drivers.fs;

import org.webappos.fs.HomeFS;
import org.webappos.server.ConfigStatic;

public class ServerFSDriver extends HomeFS {
	
	public ServerFSDriver(String login, String dir) {
		super(dir.replace("readonly:","").replace("$WEBAPPOS_ROOT", ConfigStatic.ROOT_DIR), true, false, false); // show hidden, do not use any drivers internally

		// TODO: check for allowed dirs in Registry key apps/ServerFSDriver.webservice 
		if (dir.indexOf("readonly:")>=0)
			this.setReadOnly(true);
	}

}
