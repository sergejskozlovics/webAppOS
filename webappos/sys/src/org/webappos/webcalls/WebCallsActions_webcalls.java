package org.webappos.webcalls;

import java.util.Map;

import org.webappos.server.API;
import org.webappos.webcaller.IWebCaller.WebCallDeclaration;

import lv.lumii.tda.raapi.RAAPI;

public class WebCallsActions_webcalls {
	
	private static String processMap(Map<String, WebCallDeclaration> map) {
		if (map == null)
			return "{}";
		
		StringBuffer b = new StringBuffer("{");
		for (String name : map.keySet()) {
			if (b.length()>1)
				b.append(',');
			b.append("\""+name+"\":"+map.get(name));
		}
		b.append("}");
		return b.toString();
	}
	
	public static String getAvailableWebCalls(RAAPI raapi, String arg, String login, String fullAppName) {
		return processMap(API.webCaller.getWebCalls(fullAppName));
	}

	public static String getAvailableWebCalls(String arg) {
		return processMap(API.webCaller.getWebCalls(null));		
	}
	
}
