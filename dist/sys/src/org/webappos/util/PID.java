package org.webappos.util;

import java.lang.reflect.Method;
import java.util.Optional;

public class PID {
	public static long getPID() {
		/*
		 * For Java9+ implementing:
		 *   return ProcessHandle.current().pid();
		 */
		try {
			Class<?> c = Class.forName("java.lang.ProcessHandle");
			Method m_current = c.getDeclaredMethod("current");
			Object processHandleInstance = m_current.invoke(null);
			Method m_pid = c.getDeclaredMethod("pid");
			long result = (long)m_pid.invoke(processHandleInstance);
			return result;
		} catch (Throwable t) {
		}
		
		// For Java 8 and below implementing the following code:
		try {
		java.lang.management.RuntimeMXBean runtime = 
			    java.lang.management.ManagementFactory.getRuntimeMXBean();
			java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
			jvm.setAccessible(true);
			Object mgmt =  
			    jvm.get(runtime);
			java.lang.reflect.Method pid_method =  
			    mgmt.getClass().getDeclaredMethod("getProcessId");
			pid_method.setAccessible(true);

			int pid = (Integer) pid_method.invoke(mgmt);
			return pid;
		}catch(Throwable t) {
			return 0;
		}
	}
	
	public static boolean isRunning(long pid) {		
		/*
		 * For Java9+ implementing:
		 *   return ProcessHandle.of(pid).isPresent();
		 */
		try {
			Class<?> c = Class.forName("java.lang.ProcessHandle");				
			Method m_of = c.getDeclaredMethod("of");
			Object obj = m_of.invoke(null, pid);
	
			Class<?> o = Class.forName("java.util.Optional");
			Method m_isPresent = o.getDeclaredMethod("isPresent");
			boolean result = (boolean)m_isPresent.invoke(obj);
			return result;
		}
		catch(Throwable t) {			
		}
		
		return true; // stub for Java8-
	}
	
	public static void main(String args[]) {
		System.out.println(getPID());
	}

}
