package org.webappos.registry;

import com.google.gson.JsonElement;

public interface IRegistry {
	public String getUserLogin(String emailOrLogin);
	public JsonElement getValue(String key);	
	public boolean setValue(String key, Object value);
}
