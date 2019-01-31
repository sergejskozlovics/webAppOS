package org.webappos.util;

public class PID {
	public static long getPID() {
		return ProcessHandle.current().pid();
		/* For Java 8 and below:
		try {
		java.lang.management.RuntimeMXBean runtime = 
			    java.lang.management.ManagementFactory.getRuntimeMXBean();
			java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
			jvm.setAccessible(true);
			sun.management.VMManagement mgmt =  
			    (sun.management.VMManagement) jvm.get(runtime);
			java.lang.reflect.Method pid_method =  
			    mgmt.getClass().getDeclaredMethod("getProcessId");
			pid_method.setAccessible(true);

			int pid = (Integer) pid_method.invoke(mgmt);
			return pid;
		}catch(Throwable t) {
			return 0;
		}*/
	}

}
