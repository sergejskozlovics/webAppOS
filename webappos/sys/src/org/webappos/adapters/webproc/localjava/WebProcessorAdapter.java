package org.webappos.adapters.webproc.localjava;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.server.IShutDownListener;
import org.webappos.webproc.IRWebProcessor;
import org.webappos.webproc.IWebProcessorAdapter;

public class WebProcessorAdapter implements IWebProcessorAdapter, IShutDownListener {
	private Process p = null;
	private String id = null;
	
	private IRWebProcessor wpAPI = null;

	private static Logger logger =  LoggerFactory.getLogger(WebProcessorAdapter.class);
	@Override
	public void connect(String location, String id) {
		if (p!=null) {
			// destroy previously launched process
			logger.info("Destroying previous localjava web processor "+id+" because a fresh one was requested.");
			p.destroyForcibly();
			p = null;
		}
		this.id = id;
		
		// launch java class in a separate process; pass RMI service address and web proc id

		String separator = System.getProperty("file.separator");
		String classpath = System.getProperty("java.class.path");
		if (classpath == null)
			classpath = ConfigStatic.BIN_DIR+File.pathSeparator+ConfigStatic.SYS_DIR;
		String libraryPath = System.getProperty("java.library.path");
		if (libraryPath == null)
			libraryPath = ConfigStatic.BIN_DIR;
		
		String path = System.getProperty("java.home")
	                + separator + "bin" + separator + "java";
		
		ArrayList<String> args = new ArrayList<String>();
		args.add(path);
		
		args.add("-cp"); args.add(classpath);
		args.add("-Djava.library.path="+libraryPath);
		args.add("-Dlog4j.configurationFile="+ConfigStatic.ETC_DIR+File.separator+"log4j2.properties");
				
		args.add(location);
		args.add(id);
		args.add("rmi://localhost:"+API.config.web_processor_bus_service_port+"/"+ConfigStatic.WEB_PROCESSOR_BUS_SERVICE_NAME);
		
		try {
			ProcessBuilder pb = new ProcessBuilder(args.toArray(new String[]{}));
			pb.inheritIO();
			//pb.redirectOutput(Redirect.INHERIT);
			//pb.redirectError(Redirect.INHERIT);			
			p = pb.start();
		} catch (Throwable e) {
		}
		
		API.addShutDownListener(this);
	}

	@Override
	public void onServerShutdown() {
		terminate("Destroying localjava web processor "+id+" because of server shutdown.");
	}
	
	private void terminate(String msg) {
		if (p!=null) {
			// waiting for a process to finish by its own (we suppose, the server asked web processor to halt)
			int i=100;
			while ((i>0) && (p.isAlive())) {
				try {
					p.waitFor(10, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
				}
			}

			if (p.isAlive()) {
				logger.info(msg);
				p.destroyForcibly();
				p = null;
			}
		}
		
	}

	@Override
	public void disconnect(IRWebProcessor wpAPI) {
		try {
			if (wpAPI!=null)
				wpAPI.disconnect();
		} catch (Throwable e) {
		}
		if (p.isAlive()) {
			logger.info("Destroying (instantly) localjava web processor "+id+" because disconnect requested.");
			p.destroyForcibly();
			p = null;
		}
	}

}
