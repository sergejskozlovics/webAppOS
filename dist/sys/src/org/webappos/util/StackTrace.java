package org.webappos.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;


public class StackTrace {
	
	public static String get(Throwable t) {
	 	StringWriter sw = new StringWriter();
	 	t.printStackTrace(new PrintWriter(sw));
		return sw.toString();		
	}

	public static String get() {
		try {
			throw new RuntimeException("Stack trace requested.");
		}
		catch(Throwable t) {
			return get(t);
		}
	}
	
	public static void log(Logger logger) {
		logger.debug(get());
	}
	

	public static void log(Throwable t, Logger logger) {
		logger.debug(get(t));
	}
	
}
