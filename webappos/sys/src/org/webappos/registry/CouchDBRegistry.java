package org.webappos.registry;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CouchDBRegistry extends UnicastRemoteObject implements IRegistry, IRRegistry {

	private static final long serialVersionUID = 1L;

	private static Logger logger =  LoggerFactory.getLogger(CouchDBRegistry.class);

	private static CouchDbClient xusers;
	private static CouchDbClient users;
	private static CouchDbClient projects;	
	private static CouchDbClient apps;
	//private static CouchDbClient machines;
	
	public CouchDBRegistry(String url) throws RemoteException {
		super();
		String s = url;
		logger.info("Initializing Registry as CouchDB at "+s);
		
		String protocol = "http";
		String username = "webappos";
		String password = "webappos";
		String host = "127.0.0.1";
		int port = 5984;
		
		int i = s.indexOf("@");
		if (i>=0) {
			String s1 = s.substring(0, i);
			s = s.substring(i+1);
			
			// s1 = protocol://username:password
			i = s1.indexOf("://");
			if (i>=0) {
				protocol = s1.substring(0, i);
				s1 = s1.substring(i+3);
			}
			
			// s1 = username:password
			i = s1.indexOf(":");
			if (i>=0) {
				username = s1.substring(0, i);
				password = s1.substring(i+1);
			}
			else
				username = s1;
		}
		
		// s = host:port
		i = s.indexOf(":");
		if (i>=0) {
			host = s.substring(0, i);
			try {
				port = Integer.parseInt(s.substring(i+1));
			}
			catch(Throwable t){					
			}
		}
		else
			host = s;

	
		xusers = new CouchDbClient("webappos_xusers", true, protocol, host, port, username, password);
		users = new CouchDbClient("webappos_users", true, protocol, host, port, username, password);
		projects = new CouchDbClient("webappos_projects", true, protocol, host, port, username, password);
		apps = new CouchDbClient("webappos_apps", true, protocol, host, port, username, password);
//		machines = new CouchDbClient("webappos_machines", true, protocol, host, port, username, password);		
	}
	
	/*
	replaced by UsersManager.getUserLogin
	synchronized public String getUserLogin(String emailOrLogin) {
		JsonObject xuser = null;
		try {
			xuser = xusers.find(JsonObject.class, emailOrLogin);
		}
		catch(Throwable t) {					
		}
		if (xuser == null)
			return null;
		
		JsonElement el = xuser.get("alias_of");
		if ((el != null) && (!el.getAsString().isEmpty())) {
			xuser = null;
			try {
				xuser = xusers.find(JsonObject.class, el.getAsString());
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
	}
	*/
	
	@Override
	synchronized public JsonElement getValue(String key) {
		if (key == null)
			return null;
		try {
			String[] path = key.split("/");
			if (path.length<2)
				return null; // path must contain at least short db-name and doc-id
			
			JsonObject doc = null;
			
			if ("xusers".equalsIgnoreCase(path[0])) {
				
				String id = path[1];
				
				JsonObject xuser = null;
				try {
					xuser = xusers.find(JsonObject.class, id);
				}
				catch(Throwable t) {					
				}
				if (xuser == null)
					return null;
				
				JsonElement el = xuser.get("alias_of");
				if ((el != null) && (!el.getAsString().isEmpty())) {
					xuser = null;
					try {
						xuser = xusers.find(JsonObject.class, el.getAsString());
					}
					catch(Throwable t) {						
					}
				}
				if (xuser == null)
					return null;
				
				doc = xuser;
			}
			else
			if ("users".equalsIgnoreCase(path[0])) {
				
				String id = path[1];
				
				JsonObject user = null;
				try {
					user = users.find(JsonObject.class, id);
				}
				catch(Throwable t) {					
				}
				if (user == null) {
					// trying to redirect from alias...
					JsonObject xuser = null;
					try {
						xuser = xusers.find(JsonObject.class, id);
					}
					catch(Throwable t) {						
					}
					if (xuser == null)
						return null;
					String alias_of = xuser.get("alias_of").getAsString();
					if ((alias_of != null) && (!alias_of.isEmpty())) {
						user = null;
						try {
							user = users.find(JsonObject.class, alias_of);
						}
						catch(Throwable t) {							
						}
					}
					if (user == null)
						return null; // could not redirect from alias
				}
				
				doc = user;				
			}
			else
			if ("projects".equalsIgnoreCase(path[0])) {
				
				CouchDbClient client = projects;
				doc = null;
				try {
					doc = client.find(JsonObject.class, path[1]);
				}
				catch(Throwable t) {					
				}
			}
			else
			if ("apps".equalsIgnoreCase(path[0])) {
				
				CouchDbClient client = apps;
				doc = null;
				try {
					doc = client.find(JsonObject.class, path[1]);
				}
				catch(Throwable t) {					
				}
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

			CouchDbClient db = null;
			Object doc = null;
			
			if ("xusers".equalsIgnoreCase(path[0])) {
				
				String id = path[1];
				
				JsonObject xuser = null;
				try {
					xuser = xusers.find(JsonObject.class, id);
				}
				catch (Throwable t) {					
				}
				if (xuser == null) {					
					// try to create user
					
					if (path.length==2) {				
						if (value instanceof JsonObject) {
							Object val_id = (((JsonObject) value).get("_id"));
							if (val_id == null)
								((JsonObject) value).addProperty("_id", id);
							
							// if id is not e-mail then create redirect
							if (id.indexOf('@')<0) {
								String email = ((JsonObject) value).get("email").getAsString();
								if (email != null) {
									JsonObject redirect = new JsonObject();
									redirect.addProperty("_id", email);
									redirect.addProperty("alias_of", id);
									JsonObject old_redirect = null;
									try {
										old_redirect = xusers.find(JsonObject.class, email);
									}
									catch(Throwable t) {										
									}
									if (old_redirect!=null) {
										try {
											xusers.remove(old_redirect);
										}
										catch(Throwable t) {											
										}
									}
									Response resp = xusers.save(redirect);
									String err = resp.getError();
									if (err!=null) {
										logger.error("Error when creating xuser alias "+email+" for "+id);
										return false;
									}
									
								}
							}
							
							xusers.save(value);
							return true;
						}
						else {
							xuser = new JsonObject();
							xuser.addProperty("_id", value.toString());
							Response resp = xusers.save(xuser);
							String err = resp.getError();
							if (err!=null) {
								logger.error("Error when creating xuser with id "+value);
								return false;
							}
							return true;
						}
					}
					else {
						xuser = new JsonObject();
						xuser.addProperty("_id", id);
						Response resp = xusers.save(xuser);
						String err = resp.getError();
						if (err!=null) {
							logger.error("Error when creating xuser with id "+id+" for setting values of the key "+key);
							return false;
						}
					}
				}
								
				
				JsonElement alias_of = xuser.get("alias_of");
				if ((alias_of != null) && (!alias_of.getAsString().isEmpty())) {	
					if (value == null) {
						xusers.remove(xuser);
					}
					JsonObject xuser2 = null;
					try {
						xuser2 = xusers.find(JsonObject.class, alias_of.getAsString());
					}
					catch(Throwable t){						
					}
					if (xuser2 == null) {
						// delete alias that points to nothing
						xusers.remove(xuser);
						return false;
					}
					else
						xuser = xuser2;
				}				
				
				db = xusers;
				doc = xuser;
			}
			else
			if ("users".equalsIgnoreCase(path[0])) {
			
				String id = path[1];

				JsonObject user = null;
				try {
					user = users.find(JsonObject.class, id);
				}
				catch(Throwable t){						
				}
				if (user == null) {
					// trying to redirect from alias...
					JsonObject xuser = null;
					try {
						xuser = xusers.find(JsonObject.class, id);
					}
					catch (Throwable t){						
					}
					if (xuser == null)
						return false;
					JsonElement el = xuser.get("alias_of");
					String alias_of = (el!=null)?el.getAsString():null;
					if ((alias_of != null) && (!alias_of.isEmpty())) {
						user = users.find(JsonObject.class, alias_of);
					}
					
					
					if (user == null) {
						// could not redirect from alias
						// checking, perhaps we just need to create a new user document
						
						if (path.length==2) {				
							if (value instanceof JsonObject) {
								Object val_id = (((JsonObject) value).get("_id"));
								if (val_id == null)
									((JsonObject) value).addProperty("_id", id);
																
								users.save(value);
								return true;
							}
							else {
								user = new JsonObject();
								user.addProperty("_id", value.toString());
								Response resp = users.save(user);
								String err = resp.getError();
								if (err!=null) {
									logger.error("Error when creating user with id "+value);
									return false;
								}
								return true;
							}
						}
						else {
							user = new JsonObject();
							user.addProperty("_id", id);
							Response resp = users.save(user);
							String err = resp.getError();
							if (err!=null) {
								logger.error("Error when creating user with id "+id+" for setting values of the key "+key);
								return false;
							}
						}
						
					}
				}
				
				db = users;
				doc = user;
			}
			else
			if ("projects".equalsIgnoreCase(path[0])) {
				db = projects;
				try {
					doc = db.find(JsonObject.class, path[1]);
				}
				catch(Throwable t) {					
				}
				if (doc == null) {
					doc = new JsonObject();
					((JsonObject)doc).addProperty("_id", path[1]);
					Response resp = db.save(doc);
					String err = resp.getError();
					if (err!=null) {
						logger.error("Error when creating project with id "+path[1]+" for setting values of the key "+key);
						return false;
					}				
					doc = db.find(JsonObject.class, path[1]);
				}
			}
			else
			if ("apps".equalsIgnoreCase(path[0])) {
				db = apps;
				try {
					doc = db.find(JsonObject.class, path[1]);
				}
				catch(Throwable t) {					
				}
				if (doc == null) {
					doc = new JsonObject();
					((JsonObject)doc).addProperty("_id", path[1]);
					Response resp = db.save(doc);
					String err = resp.getError();
					if (err!=null) {
						logger.error("Error when creating registry app node "+path[1]+" for setting values of the key "+key);
						return false;
					}				
					doc = db.find(JsonObject.class, path[1]);
				}
			}
						
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
					Response resp = db.remove(obj);
					String err = resp.getError();
					if (err!=null) {
						logger.error("Error when deleting key "+key+" - "+err);
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
					db.remove(doc);
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
			
			
			Response resp;
			if (((JsonObject)doc).get("_rev")==null)
				resp = db.save(doc);
			else {
				try {
					db.remove(doc);
				}
				catch(Throwable t){					
				}
				((JsonObject)doc).remove("_rev");
				resp = db.save(doc);
//				resp = db.update(doc);
			}
			String err = resp.getError();
			if (err!=null) {
				logger.error("Error when updating key "+key+" - "+err);
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

	@Override
	public JsonElement getValue_R(String key) throws RemoteException {
		return this.getValue(key);
	}

	@Override
	public boolean setValue_R(String key, Object value) throws RemoteException {
		return this.setValue(key, value);
	}
	
	/*
	public static void main(String[] args) {
		System.out.println("in Registry!");
		JsonObject user = new JsonObject();
		try {
			user.addProperty("email", "9@a.lv");
		} catch (Throwable t) {
			// TODO Auto-generated catch block
			t.printStackTrace();
		}
		Registry.setValue("xusers/user1", user);
		Registry.setValue("xusers/user1/name", null);
		//Registry.setValue("xusers/9@a.lv", null);
	}
	*/
}
