package org.webappos.util;

import java.io.IOException;

public class Browser {
	
	private static boolean launchInWindows(String url) {
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private static boolean launchInMac(String url) {
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("open " + url);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	private static boolean launchInNix(String url) {
		String[] browsers = { "epiphany", "firefox", "mozilla", "konqueror",
		                                 "netscape", "opera", "links", "lynx", "echo !!!No browser found to navigate to " };

		StringBuffer cmd = new StringBuffer();
		for (int i = 0; i < browsers.length; i++) {
		    if(i == 0)
		        cmd.append(String.format(    "%s \"%s\"", browsers[i], url));
		    else
		        cmd.append(String.format(" || %s \"%s\"", browsers[i], url)); 
		}

		ProcessBuilder builder = new ProcessBuilder("sh", "-c", cmd.toString());
		builder.inheritIO();
		
		try {
			builder.start(); 
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
		
		return true;
	}
	
	public static boolean openURL(String url) {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win")>=0)
			return launchInWindows(url);
		if (os.indexOf("mac")>=0)
			return launchInMac(url);		
		return launchInNix(url);
	}
}
