package org.webappos.auth;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.security.auth.Subject;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.security.DefaultUserIdentity;
import org.eclipse.jetty.security.UserAuthentication;
import org.eclipse.jetty.server.UserIdentity;
import org.webappos.server.API;
import org.webappos.util.UTCDate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

// all values must be validated (by org.webappos.util.ValidityChecker) in advance
public class UsersManager {

	synchronized public static UserAuthentication getUserAuthentication(String emailOrLogin) {
		
		String login = getUserLogin(emailOrLogin);
		if (login == null)
			return null;
		
    	Subject subj = new Subject();
    	Principal princ = new Principal() {
    		
    		private String name = login;

			@Override
			public String getName() {
				return name;
			}
			
			public String toString() {
				return name;
			}
			
			public boolean equals(Object another) {
				return name.equals(another.toString());
			}
    		
    	};
    	
    	String[] groupsArr = new String[] {};
    	
    	JsonElement el = API.registry.getValue("xusers/"+login+"/groups");
    	if (el!=null) {
    		ArrayList<String> arr = new ArrayList<String>();
    		try {
    			JsonObject obj = el.getAsJsonObject();
    			for (Entry<String, JsonElement> entry : obj.entrySet()) {
    				arr.add( entry.getKey() );
    				
    			}
    		}
    		catch(Throwable t) {    			
    		}
    		
    		groupsArr = arr.toArray(groupsArr);
    	}
    	
    	UserIdentity identity = new DefaultUserIdentity(subj, princ, groupsArr);
    	UserAuthentication auth = new UserAuthentication(login, identity);
		// TODO: hash auth
		
    	return auth;
	}
	
	public static String getUserLogin(String emailOrLogin) {
		// implement via alias_of
		String alias = emailOrLogin;
		int level = 5;
		for (;;) {
			JsonElement newAlias = API.registry.getValue("xusers/"+alias+"/alias_of");
			if ((newAlias == null) || (newAlias.getAsString().isEmpty()))
				return alias;
			else {
				alias = newAlias.getAsString();
				level--;
			}
			if (level<=0)
				return null;
		}
	}
	
	public static boolean passwordOK(String emailOrLogin, String password, boolean checkIfExpired) {
		JsonElement sha1_expire_time = API.registry.getValue("users/"+emailOrLogin+"/tokens/hashed/"+DigestUtils.sha1Hex(password));
		if (sha1_expire_time==null)
			return false;

		if (checkIfExpired)
			return !UTCDate.expired(sha1_expire_time.getAsString());
		else
			return true;
	}
	
	public static boolean ws_token_OK(String emailOrLogin, String ws_token, boolean checkIfExpired) {		
		JsonElement sha1_expire_time = API.registry.getValue("users/"+emailOrLogin+"/tokens/ws/"+ws_token);
		if (sha1_expire_time==null)
			return false;

		if (checkIfExpired)
			return !UTCDate.expired(sha1_expire_time.getAsString());
		else
			return true;
	}
	

	public static boolean projectOK(String project_id, String emailOrLogin, String ws_or_collaboriation_token, boolean checkIfExpired) {
		if (project_id==null)
			return false;
		
		String login = getUserLogin(emailOrLogin);
		if (login == null)
			return false;
		
		if (project_id.startsWith(login+"/")) {
			// user's own project; just check the token
			return ws_token_OK(login, ws_or_collaboriation_token, checkIfExpired);
		}

		
		// collaborative project; check collaboration token			
		
		JsonElement _uuid = API.registry.getValue("projects/"+project_id);
		if (_uuid==null)
			return false;
		String uuid = _uuid.getAsString();
		if ((uuid==null) || uuid.isEmpty())
			return false;

		
		JsonElement expire_time = API.registry.getValue("projects/"+uuid+"/collaboration_tokens/"+ws_or_collaboriation_token);
		if (expire_time==null)
			return false;

		if (checkIfExpired)
			return !UTCDate.expired(expire_time.getAsString());
		else
			return true;
	}
	
}
