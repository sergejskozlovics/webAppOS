package org.webappos.registry;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.ConfigStatic;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class JsonFilesRegistry extends UnicastRemoteObject implements IRegistry, IRRegistry {		

	private static final long serialVersionUID = 1L;

	private static Logger logger =  LoggerFactory.getLogger(JsonFilesRegistry.class);
	
	private static final String DIR_REGISTRY = ConfigStatic.ETC_DIR+File.separator+"registry";
	private static final String DIR_XUSERS = ConfigStatic.ETC_DIR+File.separator+"registry"+File.separator+"xusers";
	private static final String DIR_USERS = ConfigStatic.ETC_DIR+File.separator+"registry"+File.separator+"users";
	private static final String DIR_PROJECTS = ConfigStatic.ETC_DIR+File.separator+"registry"+File.separator+"projects";
	private static final String DIR_APPS = ConfigStatic.ETC_DIR+File.separator+"registry"+File.separator+"apps";

	
	public JsonFilesRegistry() throws RemoteException {
		super();
		logger.info("Initializing Registry as JSON files at "+DIR_REGISTRY);
		new File(DIR_REGISTRY).mkdirs();
		new File(DIR_XUSERS).mkdirs();
		new File(DIR_USERS).mkdirs();
		new File(DIR_PROJECTS).mkdirs();
		new File(DIR_APPS).mkdirs();
	}
	
	
	private static JsonObject fileToJson(String fileName){
		File f = new File(fileName);
		if (!f.exists())
			return null;
		
		// else waiting for unlock...
        JsonObject jsonObject = new JsonObject();        
		for (;;) {					
	        try {
	            JsonParser parser = new JsonParser();
	            JsonElement jsonElement = parser.parse(new FileReader(fileName));
	            jsonObject = jsonElement.getAsJsonObject();
	            return jsonObject;
	        } catch (Throwable t) {
	        	// locked or error...
	        	if (f.exists() && !f.canRead()) {
	        		// locked...
		        	try {
		        		Thread.sleep(1);
		        	}
		        	catch(Throwable tt) {	        		
		        	}
	        	}
	        	else
	        		return null; // error
	        }        
		}
    }
	
	private static boolean jsonToFile(JsonObject obj, String fileName) {
		File f = new File(fileName);
		
		for (;;) {
			try { 
				Writer writer = new FileWriter(fileName);
			    Gson gson = new GsonBuilder().create();
			    gson.toJson(obj, writer);
			    writer.close();
			    return true;
			}
			catch(Throwable t){
				// locked or error...
	        	if (f.exists() && !f.canWrite()) {
	        		// locked...
		        	try {
		        		Thread.sleep(1);
		        	}
		        	catch(Throwable tt) {	        		
		        	}
	        	}
	        	else
	        		return false; // error
			}
		}
	}

	private static boolean deleteFile(String fileName) {
		File f = new File(fileName);
		if (!f.exists())
			return true;
		
		for (;;) {
			if (f.delete())
				return true;
			else {
	        	if (f.exists() && !f.canWrite()) {
	        		// locked...
		        	try {
		        		Thread.sleep(1);
		        	}
		        	catch(Throwable tt) {	        		
		        	}
	        	}
	        	else
	        		return false; // error
				
			}
		}
	}
	
/* replaced by UsersManager.getUserLogin
 	synchronized public String getUserLogin(String emailOrLogin) {
		JsonObject xuser = fileToJson(DIR_XUSERS+File.separator+emailOrLogin);
		if (xuser == null)
			return null;
		
		JsonElement el = xuser.get("alias_of");
		if ((el != null) && (!el.getAsString().isEmpty())) {
			xuser = null;
			try {
				xuser = fileToJson(DIR_XUSERS+File.separator+el.getAsString());
			}
			catch(Throwable t) {						
			}
		}
		if (xuser == null)
			return null;
		
		JsonElement id = xuser.get("_id");
		if (id == null)
			return null;
		else
			return id.getAsString();
	}*/
	
	@Override
	synchronized public JsonElement getValue(String key) {
		if (key == null)
			return null;
		
		if ("xusers".equals(key)) {
			
			File dir = new File(DIR_XUSERS);
			if (!dir.exists() || !dir.isDirectory())			
				return new JsonPrimitive(0);
			
			String[] names = dir.list();
			if (names==null)
				return new JsonPrimitive(0);
			
			int cnt = 0;
			for (String s : names)
				if (!s.startsWith("."))
					cnt++;
			
			return new JsonPrimitive(cnt);
		}
		
		try {
			String[] path = key.split("/");
			if (path.length<2)
				return null; // path must contain at least short db-name and doc-id
			
			JsonObject doc = null;
			
			if ("xusers".equalsIgnoreCase(path[0])) {
				
				String id = path[1];
				
				JsonObject xuser = null;
				xuser = fileToJson(DIR_XUSERS+File.separator+id);
				
				
				if (xuser != null) {
					// trying to redirect from alias...				
					JsonElement el = xuser.get("alias_of");
					if ((el != null) && (!el.getAsString().isEmpty())) {
						xuser = fileToJson(DIR_XUSERS+File.separator+el.getAsString());
					}
				}
				
				if (xuser == null)
					return null;
				
				doc = xuser;
			}
			else
			if ("users".equalsIgnoreCase(path[0])) {
				
				String id = path[1];
				
				JsonObject user = fileToJson(DIR_USERS+File.separator+id);
				if (user == null) {
					// trying to redirect from alias...
					JsonObject xuser = fileToJson(DIR_XUSERS+File.separator+id);
					if (xuser == null)
						return null;
					JsonElement el = xuser.get("alias_of");				
					String alias_of = (el!=null)?el.getAsString():null;

					if ((alias_of != null) && (!alias_of.isEmpty())) {
						user = fileToJson(DIR_USERS+File.separator+alias_of);
					}
					if (user == null)
						return null; // could not redirect from alias
				}
				
				doc = user;				
			}
			else
			if ("projects".equalsIgnoreCase(path[0])) {				
				doc = fileToJson(DIR_PROJECTS+File.separator+path[1]);
			}
			else
			if ("apps".equalsIgnoreCase(path[0])) {				
				doc = fileToJson(DIR_APPS+File.separator+path[1]);
			}

			if (doc == null)
				return null;
			
			int i = 2;
			JsonElement obj = doc;
			while (i<path.length) {
				if (!(obj instanceof JsonObject))
					return null; // final element reached, but path continues
				obj = ((JsonObject)obj).get(path[i]);
				i++;
			}
			
			if (obj == null)
				return null;
			
			return obj;
		}
		catch(Throwable t) {	
			t.printStackTrace();
			logger.error(t.toString());
			return null;
		}		
	}
	
	@Override
	synchronized public boolean setValue(String key, Object value) {
		if (key == null)
			return false;
		try {
			String[] path = key.split("/");
			
			if (path.length<2) {
				return false;// path must contain at least short db-name, doc-id
			}

			Object doc = null;
			
			if ("xusers".equalsIgnoreCase(path[0])) {
				
				String id = path[1];
				
				JsonObject xuser = fileToJson(DIR_XUSERS+File.separator+id);
				if (xuser == null) {					
					// try to create user
					
					if (path.length==2) {				
						if (value instanceof JsonObject) {
							Object val_id = (((JsonObject) value).get("_id"));
							if (val_id == null)
								((JsonObject) value).addProperty("_id", id);
							
							// if id is not e-mail then create redirect
							if (id.indexOf('@')<0) {
								String email = null;
								try {
									email = ((JsonObject) value).get("email").getAsString();
								}
								catch(Throwable t) {
								}
								if (email != null) {
									JsonObject redirect = new JsonObject();
									redirect.addProperty("_id", email);
									redirect.addProperty("alias_of", id);
									JsonObject old_redirect = fileToJson(DIR_XUSERS+File.separator+email);
									if (old_redirect!=null) {
										deleteFile(DIR_XUSERS+File.separator+email);
									}
									
									if (!jsonToFile(redirect, DIR_XUSERS+File.separator+email)) {
										logger.error("Error when creating xuser alias "+email+" for "+id);
										return false;
									}
									
								}
							}
							
							jsonToFile((JsonObject) value, DIR_XUSERS+File.separator+id);
							return true;
						}
						else {
							xuser = new JsonObject();
							xuser.addProperty("_id", value.toString());
							if (!jsonToFile(xuser, DIR_XUSERS+File.separator+value)) {
								logger.error("Error when creating xuser with id "+value);
								return false;
							}
							return true;
						}
					}
					else {
						xuser = new JsonObject();
						xuser.addProperty("_id", id);
						if (!jsonToFile(xuser, DIR_XUSERS+File.separator+id)) {
							logger.error("Error when creating xuser with id "+id+" for setting values of the key "+key);
							return false;
						}
					}
				}
								
				
				JsonElement alias_of = xuser.get("alias_of");
				if ((alias_of != null) && (!alias_of.getAsString().isEmpty())) {	
					if (value == null) {
						deleteFile(DIR_XUSERS+File.separator+id);
					}
					JsonObject xuser2 = fileToJson(DIR_XUSERS+File.separator+alias_of.getAsString());
					if (xuser2 == null) {
						// delete alias that points to nothing
						deleteFile(DIR_XUSERS+File.separator+id);
						return false;
					}
					else {
						xuser = xuser2;
						path[1] = xuser2.get("_id").getAsString();
					}
				}				
				
				doc = xuser;
			}
			else
			if ("users".equalsIgnoreCase(path[0])) {
			
				String id = path[1];

				JsonObject user = fileToJson(DIR_USERS+File.separator+id);
				if (user == null) {
					// trying to redirect from alias...
					JsonObject xuser = fileToJson(DIR_XUSERS+File.separator+id);
					if (xuser == null)
						return false;
					JsonElement el = xuser.get("alias_of");
					String alias_of = (el!=null)?el.getAsString():null;
					if ((alias_of != null) && (!alias_of.isEmpty())) {
						user = fileToJson(DIR_USERS+File.separator+alias_of);
					}
					
					
					if (user == null) {
						// could not redirect from alias
						// checking, perhaps we just need to create a new user document
						
						if (path.length==2) {				
							if (value instanceof JsonObject) {
								Object val_id = (((JsonObject) value).get("_id"));
								if (val_id == null)
									((JsonObject) value).addProperty("_id", id);
																
								jsonToFile((JsonObject)value, DIR_USERS+File.separator+id);
								return true;
							}
							else {
								user = new JsonObject();
								user.addProperty("_id", value.toString());
								
								if (!jsonToFile(user, DIR_USERS+File.separator+value)) {
									logger.error("Error when creating user with id "+value);
									return false;
								}
								return true;
							}
						}
						else {
							user = new JsonObject();
							user.addProperty("_id", id);
							
							if (!jsonToFile(user, DIR_USERS+File.separator+id)) {
								logger.error("Error when creating user with id "+id+" for setting values of the key "+key);
								return false;
							}
						}
						
					}
					else {
						path[1] = user.get("_id").getAsString();						
					}
				}
				
				doc = user;
			}
			else
			if ("projects".equalsIgnoreCase(path[0])) {
				doc = fileToJson(DIR_PROJECTS+File.separator+path[1]);
				if (doc == null) {
					doc = new JsonObject();
					((JsonObject)doc).addProperty("_id", path[1]);
					
					if (!jsonToFile((JsonObject) doc, DIR_PROJECTS+File.separator+path[1])) {
						logger.error("Error when creating project with id "+path[1]+" for setting values of the key "+key);
						return false;
					}				
				}
			}
			else
			if ("apps".equalsIgnoreCase(path[0])) {
				doc = fileToJson(DIR_PROJECTS+File.separator+path[1]);
				if (doc == null) {
					doc = new JsonObject();
					((JsonObject)doc).addProperty("_id", path[1]);
					
					if (!jsonToFile((JsonObject) doc, DIR_PROJECTS+File.separator+path[1])) {
						logger.error("Error when creating registry app node "+path[1]+" for setting values of the key "+key);
						return false;
					}				
				}
			}
			else
				return false;
						
			int i = 2;
			Object obj = doc;
			while ((i<path.length-1) && (obj instanceof JsonObject)) {
				JsonElement obj2 = ((JsonObject)obj).get(path[i]);
				if (obj2 == null) {
					// create child branch...					
					obj2 = new JsonObject();
					((JsonObject)obj).add(path[i], obj2);
				}
				obj = obj2;
				i++;
			}
			
			if (!(obj instanceof JsonObject))
				return false;
			
			if (value == null) {
				if (path.length==2) {
					
					if (!deleteFile(DIR_PROJECTS+File.separator+path[1])) {
						logger.error("Error when deleting key "+key);
						return false;
					}
					return true;
				}
				else {
					((JsonObject)obj).remove(path[path.length-1]);
				}
			}
			else {
				if (path.length==2) {
					// replace document...
					deleteFile(DIR_PROJECTS+File.separator+path[1]);
					if (value instanceof JsonObject) {
						doc = value;
						JsonElement id = ((JsonObject)value).get("_id");
						if (id==null)
							((JsonObject)value).addProperty("_id", path[1]);
					}
					else {
						doc = new JsonObject();
						((JsonObject)doc).addProperty("_id", value.toString());
					}
				}
				else {				
					if (value instanceof JsonElement)
						((JsonObject)obj).add(path[path.length-1], (JsonElement)value);
					else
					if (value instanceof Number)
						((JsonObject)obj).addProperty(path[path.length-1], (Number)value);
					else
					if (value instanceof Boolean)
						((JsonObject)obj).addProperty(path[path.length-1], (Boolean)value);
					else
						((JsonObject)obj).addProperty(path[path.length-1], (String)value);
				}
			}
			
			
			if (!jsonToFile((JsonObject) doc, DIR_REGISTRY+File.separator+path[0]+File.separator+path[1])) {
				logger.error("Error when updating key "+key);
				return false;
			}
			
			return true;
		}
		catch(Throwable t) {	
			logger.error(t.toString());
			t.printStackTrace();
			return false;
		}				
	}

	
}
