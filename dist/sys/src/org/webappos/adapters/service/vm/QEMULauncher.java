package org.webappos.adapters.service.vm;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.properties.ServiceProperties;
import org.webappos.server.ConfigStatic;

public class QEMULauncher {
	
	private static Logger logger =  LoggerFactory.getLogger(QEMULauncher.class);
	
	private ArrayList<String> args = new ArrayList<String>();
	private Process javaProcess = null;
	
	public QEMULauncher(String qemu_path, ServiceProperties svcProps) {
			String system = svcProps.properties.getProperty("vm_bits", "i386");
   			args.add(qemu_path+File.separator+"qemu-system-"+system);

    		args.add("-boot");
    		args.add("d");
    		args.add("-m");
    		args.add(svcProps.properties.getProperty("vm_memory", "512"));
    		args.add("-hda");
    		args.add(ConfigStatic.APPS_DIR+File.separator+svcProps.service_full_name+File.separator+svcProps.properties.getProperty("vm_disk", "disk.vdi"));
    		args.add("-device");
    		args.add("e1000,netdev=enp0s3");
    		args.add("-monitor");
    		args.add("stdio");
    		args.add("-netdev");    		
    		String lastArg = "user,id=enp0s3";
    		
    		String vm_ports = svcProps.properties.getProperty("vm_ports","").trim();
    		if (!vm_ports.isEmpty()) {
	    		String[] arr = vm_ports.split(",");
	    		int n = Math.min(arr.length, svcProps.requires_additional_ports.length);
				for (int k=0; k<n; k++) {
					try {
						int vm_port = Integer.parseInt(arr[k]);
						int our_port = svcProps.requires_additional_ports[k];
						lastArg += ",hostfwd=tcp::"+our_port+"-:"+vm_port;
					}
					catch(Throwable t) {
					}
				}
	    		
    		}
    		args.add(lastArg);
    		
    		
    		String gr = svcProps.properties.getProperty("vm_graphics", "false");
    		if ("false".equalsIgnoreCase(gr) || "no".equalsIgnoreCase(gr))
    			args.add("-nographic");
    		
	}
	
	public boolean launch(Runnable onStopped, Runnable onHalted) {		
		logger.debug("Launching QEMULauncher via java: "+args);
		
		String javapath = System.getProperty("java.home")
                + File.separator + "bin" + File.separator + "java";
		
		ArrayList<String> javargs = new ArrayList<String>();
		javargs.add(javapath);
		javargs.add("-cp");
		javargs.add( System.getProperty("java.class.path") );
		javargs.add("org.webappos.adapters.service.vm.QEMULauncher");
		
		javargs.addAll(args);
		
		final ProcessBuilder pb = new ProcessBuilder(javargs.toArray(new String[]{}));
		
		pb.redirectError(Redirect.INHERIT);
		
		
		try {
    		javaProcess = pb.start();       		
		} catch (Throwable e) {
			javaProcess = null;
			return false;
		}

		
		BufferedReader r = new BufferedReader(new InputStreamReader(javaProcess.getInputStream()));
		try {
			String s = r.readLine();
			if (s.equals("OK")) {
				
				// reading input from the child (e.g. "HALTED" or "STOPPED")...
				new Thread() {
					public void run(){
						String s = null;
						for(;;) {
							try {
								s = r.readLine();
								if (s.equals("HALTED") || s.equals("STOPPED"))
									break;
							}
							catch(Throwable t) {
								s = null;
								break;
							}					
						}
						// now either the child died, or it sent us the TERMINATED signal...
						
						if (s==null)
							s = "HALTED";
						logger.debug("Child terminated, s="+s);
						if ((onHalted!=null) && "HALTED".equals(s)) {
							onHalted.run();
						}
						else
							if (onStopped!=null)
								onStopped.run();
						
					}
				}.start();
				
				return true;
			}
			else
				throw new RuntimeException("close");
		}
		catch(Throwable t) {
			try {
				javaProcess.getInputStream().close();
			} catch (IOException e) {
			}
			try {
				javaProcess.getOutputStream().close();
			} catch (IOException e) {
			}
			try {
				javaProcess.getErrorStream().close();
			} catch (IOException e) {
			}
			javaProcess = null;
			return false;
		}
	}	
	
	private static Charset utf8 = Charset.forName("UTF-8");

	
	public synchronized void stop() {
		if (javaProcess == null)
			return;

		String s = "system_powerdown\n";
		try {
			javaProcess.getOutputStream().write(s.getBytes(utf8));								
			javaProcess.getOutputStream().flush();
		} catch (IOException e) {
		}
		
	}
	
	public static void main(String args[]) { // an intermediate process between the parent and qemu, which handles when the parent dies 
		
		
		ProcessBuilder pb = new ProcessBuilder(args);
		final Process p;
		
		try {
    		p = pb.start();       		
		} catch (Throwable e) {
			return;
		}
		
		final BufferedReader parentReader = new BufferedReader(new InputStreamReader(System.in));
		
		final AtomicBoolean stopped = new AtomicBoolean(false);
		

		// reading input from the parent (e.g., system_powerdown)...
		new Thread() {
			public void run(){
				for(;;) {
					try {
						String s = parentReader.readLine();
						if (s.equals("system_powerdown"))
							break;
					}
					catch(Throwable t) {
						break;
					}					
				}
				// now either the parent died, or it sent us the system_powerdown signal...
				logger.debug("now either the parent died, or it has sent us the system_powerdown signal...");
				
				// sending system_powerdown to the VM
				String s = "system_powerdown\n";
				
				stopped.set(true);
				
				
				try {
					p.getOutputStream().write(s.getBytes(utf8));								
					p.getOutputStream().flush();
				} catch (IOException e) {
				}
				
				logger.debug("system_powerdown sent");
				System.out.println("STOPPED");
				logger.debug("STOPPED sent");
				
				
				if (p.isAlive()) {
					// wait for 20 secs... if not terminated, terminate forcefully!
					try {
						Thread.sleep(20000);
					}catch(Throwable t) {						
					}
					p.destroyForcibly();
				}
				
			}
		}.start();
		

		// waiting...
		while (p.isAlive()) {
			try {
				p.waitFor();
			} catch (InterruptedException e) {
			}
		}
				
		if (!stopped.get()) {
			logger.debug("child died");
			System.out.println("HALTED");
			logger.debug("HALTED sent");
		}
		
		
		for (Thread t : Thread.getAllStackTraces().keySet()) 
		{  if (t.getState()==Thread.State.RUNNABLE) 
		     t.interrupt(); 
		} 

		System.exit(0);
	}
}
