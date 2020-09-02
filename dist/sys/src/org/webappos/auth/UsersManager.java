package org.webappos.auth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.security.DefaultUserIdentity;
import org.eclipse.jetty.security.UserAuthentication;
import org.eclipse.jetty.server.UserIdentity;
import org.webappos.properties.WebServiceProperties;
import org.webappos.server.API;
import org.webappos.server.ConfigEx;
import org.webappos.util.RandomToken;
import org.webappos.util.UTCDate;

import javax.security.auth.Subject;
import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;

// all values must be validated (by org.webappos.util.ValidityChecker) in advance
public class UsersManager {
	
	enum Response {
		OK,
		FAILED,
		EMAILED,
		MANUAL_PROCESSING
	}

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

	public static Response addUser(String login, Boolean emailToAllow) {
		JsonObject xuser = new JsonObject();
		JsonObject user = new JsonObject();
		
		String salt = RandomToken.generateSalt();
		
		xuser.addProperty("salt", salt);
		xuser.addProperty("signup_time", UTCDate.stringify(new Date()));	
		
		WebServiceProperties properties = API.propertiesManager
										     .getWebServicePropertiesByFullName("Login.webservice");
		String signup_policy = properties.properties
										 .getProperty("signup_policy", "deny");
		
		System.out.println(signup_policy);
			
		if(emailToAllow && signup_policy.equals("email")) {
			signup_policy = "allow";
		}
		
		xuser.addProperty("_id", login);
		user.addProperty("_id", login);

		if ("allow".equals(signup_policy)) {
			API.registry.setValue("xusers/" + login, xuser);
			API.registry.setValue("users/" + login, user);
			
			File file = new File(ConfigEx.HOME_DIR + "/" + user);
			
			return Response.OK;
		}
		else if ("deny".equals(signup_policy)) {
				// we won't save the xuser and user;

				// "deny" must have been checked on top of the function: if (!signup_allowed) ...
				// however, the webcall could also return "deny", thus, we check it again here
				return Response.FAILED;
		}
		else {
				//assume "manual"
				xuser.addProperty("blocked", true); // require to set blocked=false manually (e.g., via "webappos approveuser")
				API.registry.setValue("xusers/" + login, xuser);
				API.registry.setValue("users/" + login, user);
				return Response.MANUAL_PROCESSING;
		}
	}

	public static void generateToken(String login, Boolean remember, String expirationDate){
		JsonElement email_verified = API.registry.getValue("xusers/"+login+"/email_verified");
		if ((email_verified!=null) && (!email_verified.getAsBoolean()))
			throw new RuntimeException("User's e-mail not verified yet");

		JsonElement blocked = API.registry.getValue("xusers/"+login+"/blocked");
		if ((blocked!=null) && (blocked.toString().equals("true")))
			throw new RuntimeException("User is blocked");

		String token = RandomToken.generateRandomToken();

		if(expirationDate.isEmpty()){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());

			if (remember) {
				calendar.add(Calendar.DATE, 365); // token valid for 1 year
			} else {
				calendar.add(Calendar.MINUTE, 60); // token valid for 1 hour
			}

			expirationDate = UTCDate.stringify(calendar.getTime());
		}

		API.registry.setValue("xusers/"+login+"/tokens/ws/"+token, expirationDate);
	}
	
	public static String getUserLogin(String emailOrLogin) {
		// implement via alias_of
		String alias = emailOrLogin;
		JsonElement login = API.registry.getValue("xusers/"+alias+"/_id");
		try {
			return login.getAsString();
		}
		catch(Throwable t) {
			return null;
		}
	}
	
	public static boolean passwordOK(String emailOrLogin, String password, boolean checkIfExpired) {
		JsonElement _salt = API.registry.getValue("xusers/"+emailOrLogin+"/salt");
		String salt = _salt==null?"":_salt.getAsString();
		JsonElement sha_expire_time = API.registry.getValue("xusers/"+emailOrLogin+"/tokens/hashed/"+DigestUtils.sha256Hex(password+salt));
		if (sha_expire_time==null)
			return false;

		if (checkIfExpired)
			return !UTCDate.expired(sha_expire_time.getAsString());
		else
			return true;
	}
	
	public static boolean ws_token_OK(String emailOrLogin, String ws_token, boolean checkIfExpired) {		
		JsonElement sha_expire_time = API.registry.getValue("xusers/"+emailOrLogin+"/tokens/ws/"+ws_token);
		if (sha_expire_time==null)
			return false;

		if (checkIfExpired)
			return !UTCDate.expired(sha_expire_time.getAsString());
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
	
	public static boolean userInGroup(String emailOrLogin, String group) {
		
		String login = getUserLogin(emailOrLogin);
		
		if (group==null)
			return false;

    	JsonElement el = API.registry.getValue("xusers/"+login+"/groups");
    	
    	if (el!=null) {
    		try {
    			JsonObject obj = el.getAsJsonObject();
    			for (Entry<String, JsonElement> entry : obj.entrySet()) {
    				System.out.println(entry.getKey());
    				if (group.equals(entry.getKey())) {
    					return true;
    				}
    				
    			}
    		}
    		catch(Throwable t) { 
    			t.printStackTrace();
    		}
    		
    	}

    	return false;
	}	

}
