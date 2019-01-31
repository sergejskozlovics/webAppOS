package org.webappos.registry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.webappos.server.ConfigStatic;
import org.webappos.util.UTCDate;

public class InFileStatus extends UnicastRemoteObject implements IStatus, IRStatus {

	private static final long serialVersionUID = 1L;
	private static PrintWriter w = null;
	
	public InFileStatus() throws RemoteException {
		super();
		new File(ConfigStatic.VAR_DIR).mkdirs();
		//File f = new File(Config.VAR_DIR+File.separator+"status-"+(new java.util.Date()).toString().replaceAll(":", "_")+".txt");
		File f = new File(ConfigStatic.VAR_DIR+File.separator+"status.txt");
		try {
			w = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setStatus(String key, String value) {		
		if (w!=null) {
			if (value==null)
				w.println(key+"=");
			else
				w.println(key+"="+value);
			w.flush();
		}
	}
	
	@Override
	public void setStatus(String key, String value, long expireSeconds) {
		if (w!=null) {
			if (value==null)
				w.println(key+"=");
			else
				w.println(key+"="+value+" // expires@"+UTCDate.stringify(new java.util.Date()));
			w.flush();
		}
	}

	@Override
	public void setStatus_R(String key, String value) throws RemoteException {
		setStatus(key, value);		
	}

	@Override
	public void setStatus_R(String key, String value, long expireSeconds) throws RemoteException {
		setStatus(key, value, expireSeconds);		
	}

}
