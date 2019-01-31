package org.webappos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Console {
	
	private static Logger logger =  LoggerFactory.getLogger(Console.class);
	
	public static void println(Object... arr) {
		
		String s = "";
		for (Object o : arr)
			if (s.isEmpty())
				s = o.toString();
			else
				s += " "+o.toString();
					
		
//		logger.info(s);
		System.err.println("CONSOLE: "+s);
	}

}
