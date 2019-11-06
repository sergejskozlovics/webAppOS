package org.webappos.webcalls;

import org.codehaus.jettison.json.JSONObject;
import org.webappos.server.API;

import com.google.gson.JsonElement;

public class RegistryActions_webcalls {
	
	public static String getUserRegistryValue(String key, String login) {
		// login will be non-null
		
		try {
			JsonElement value = API.registry.getValue("user/"+key);
			if (value.isJsonNull()) {
				return "{}";
			}
			if (value.isJsonPrimitive())
				return "{\"result\":\""+value.getAsString()+"\"}";
			else
				return "{\"result\": "+value.getAsString()+"}";
		}
		catch(Throwable t) {
			return "{\"error\":\""+t.getMessage()+"\"}";
		}
		
	}

	public static String setUserRegistryValue(String json, String login) {
		// login will be non-null
		
		try {
			JSONObject obj = new JSONObject(json);
			String key = obj.getString("key");
			Object value = obj.get("value");
			if (key==null)
				return "{\"result\":false, \"error\":\"No key specified\"}";
			
			return "{\"result\":"+API.registry.setValue("user/"+key, value)+"}";
		}
		catch(Throwable t) {
			return "{\"result\":false, \"error\":\""+t.getMessage()+"\"}";
		}
		
	}
}
