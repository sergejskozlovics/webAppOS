package org.webappos.service.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.codec.digest.DigestUtils;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.util.RandomToken;
import org.webappos.util.UTCDate;
import org.webappos.webcaller.WebCaller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;




@SuppressWarnings( "serial" )
public class LoginServlet extends HttpServlet
{
	private static Logger logger =  LoggerFactory.getLogger(LoginServlet.class);
	private static Charset utf8_charset = Charset.forName("UTF-8");
	
	private static boolean signup_allowed = false;
	private static String signup_policy = "deny";
	private static Properties properties = new Properties();
	
	private static int signup_interval_minutes = 10; // in minutes
	
	private static int password_expire_days = 401500;
	
	public static void setProperties(Properties p) { // file service.properties for the given webAppOS service
		properties = p;
		signup_policy = properties.getProperty("signup_policy", "deny");
		signup_allowed = !signup_policy.equalsIgnoreCase("deny");
		
		String s = properties.getProperty("password_expire_days", password_expire_days+"");
		
		int i = 0;
		try {
			i = Integer.parseInt(s);
		}
		catch(Throwable t) {			
		}		
		if (i>0)
			password_expire_days = i;
		
		i = 0;
		try {
			i = Integer.parseInt(properties.getProperty("signup_inverval_minutes", signup_interval_minutes+""));
		}
		catch(Throwable t) {			
		}		
		if (i>=0)
			signup_interval_minutes = i;
		
	}
	
	/**
	 * In accordance with the Post-Redirect-Get pattern, we do not write
	 * the response text directly, but redirect to certain HTML 
	 * (see https://en.wikipedia.org/wiki/Post/Redirect/Get).
	 * 
	 * @param htmlName - HTML file name w/o extension; the file should contain the "<!--message-->" comment to be replaced with the actual message
	 * @param msg - the message to replace the comment with
	 * @param response - the response stream
	 * @throws IOException
	 */
	private void redirectToMessage(String htmlName, String msg, HttpServletResponse response) throws IOException {
		
		if (msg==null) {
			response.sendRedirect("/apps/login/"+htmlName+".html");
			return;
		}
		
		int hash = msg.hashCode();
		
		File f = new File(ConfigStatic.WEB_ROOT_CACHE_DIR+File.separator+"login.cache"+File.separator+htmlName+hash+".html");
		
		try {
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				String s = new String(Files.readAllBytes(Paths.get(ConfigStatic.APPS_DIR+File.separator+"login.app"+File.separator+"web-root"+File.separator+htmlName+".html")), "UTF-8");
				s = s.replace("<!--message-->", msg);
				Files.write(f.toPath(), s.getBytes(utf8_charset));
			}
		}
		catch(Throwable tt) {				
		}
		
		if (f.exists())
			response.sendRedirect("/apps/login/login.cache/"+htmlName+hash+".html");
		else
			response.sendRedirect("/apps/login/"+htmlName+".html");					
	}
	
	
	private void verify(String path2, HttpServletResponse response) throws IOException {
	
		try {
			if (!signup_allowed){
				throw new RuntimeException("Signing up not allowed");
			}
			
			int i = path2.indexOf('/');
			if (i<=0)
				throw new RuntimeException("Verification failed - wrong request");
			String login = path2.substring(0, i);
			
			path2 = path2.substring(i+1);
			
			ValidityChecker.checkLogin(login, false); // no standalone
			ValidityChecker.checkTokenLike(path2, "Illegal token provided", false);
			
			String registryPath = "xusers/"+login+"/tokens/emailed/"+path2;
			JsonElement el = API.registry.getValue(registryPath);
			
			if (el == null)
				throw new RuntimeException("Verification failed");
			
			if (UTCDate.expired(el.toString()))
				throw new RuntimeException("Token expired "+el.toString());
			
			API.registry.setValue("xusers/"+login+"/tokens/emailed/"+path2, null);
			API.registry.setValue("xusers/"+login+"/email_verified", true);
			
			el = API.registry.getValue("xusers/"+login+"/blocked");
			if ((el==null) || (el.getAsBoolean()==false))
				response.sendRedirect("/apps/login/signup_ok.html");
			else
				response.sendRedirect("/apps/login/signup_processing.html");
		}
		catch(Throwable t) {
			logger.error(t.getMessage());
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.trace(errors.toString());
			}

			redirectToMessage("signup_failed", t.getMessage(), response);
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
        String path = request.getPathInfo();
        if (path == null)
        	path = "";
        
        if (path.startsWith("/"))
        	path = path.substring(1);
        if (path.endsWith("/"))
        	path = path.substring(0, path.length()-1);
        if (path.equals("signup_allowed")) {
        	response.setContentType("text/html");
        	response.getOutputStream().print(signup_allowed);
        	return;
        }
        if (path.equals("recaptcha_site_key")) {
        	response.setContentType("text/html");
        	response.getOutputStream().print(properties.getProperty("recaptcha_site_key",""));
        	return;
        }
        if (path.startsWith("verify/")) {
        	verify(path.substring("verify/".length()), response);
    		return;
        }
    }
		
	private static boolean captchaSolved(String recaptcha_response) {		
		if (properties.getProperty("recaptcha_site_key","").isEmpty())
			return true; // no captcha required
		
		if ((recaptcha_response==null) || (recaptcha_response.isEmpty()))
			return false;
		
		try {
	        Map<String,String> params = new LinkedHashMap<>();
		    params.put("secret", properties.getProperty("recaptcha_secret_key",""));
		    params.put("response", recaptcha_response);
		    StringBuilder postData = new StringBuilder();
		    for (Map.Entry param : params.entrySet()) {
		        if (postData.length() != 0) postData.append('&');
		        postData.append(URLEncoder.encode(param.getKey().toString(), "UTF-8"));
		        postData.append('=');
		        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		    }
		    byte[] postDataBytes = postData.toString().getBytes("UTF-8");
	        
			HttpURLConnection conn = (HttpURLConnection)new URL(properties.getProperty("recaptcha_verify_url","https://www.google.com/recaptcha/api/siteverify")).openConnection();
	        conn.setRequestMethod("POST");
	        
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		    conn.setDoOutput(true);
		    conn.getOutputStream().write(postDataBytes);
		    
		    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    for (int c; (c = in.read()) >= 0;)
		        sb.append((char)c);
		    JSONObject captchaResponse = new JSONObject(sb.toString());
		    
		    // TODO: verify date
		    //Date captchaDate = javax.xml.bind.DatatypeConverter.parseDateTime(  captchaResponse.getString("challenge_ts") ).getTime();
		    //Date now = new Date();
		    
		    return captchaResponse.getBoolean("success");
		}
		catch(Throwable t) {
			logger.error(t.getMessage());
			return false;
		}
	}
	
	private void signin(HttpServletRequest request, HttpServletResponse response, boolean jsonResult) throws IOException {
		if (jsonResult)
			response.setContentType("application/json");
    	try {
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			if (!"application/x-www-form-urlencoded".equals(request.getContentType()))
				throw new RuntimeException("Bad enctype");
			
			String formData = java.net.URLDecoder.decode(IOUtils.toString(request.getInputStream(), utf8_charset), utf8_charset.name());
			String[] arr = formData.split("&");
			
			String login=null, password=null;
			boolean remember=false;
			for (String s : arr) {    	
				if (s.startsWith("login=")) {
					login = s.substring("login=".length()).trim();
				}
				if (s.startsWith("password=")) {
					password = s.substring("password=".length());
				}
				if (s.startsWith("remember=")) {
					remember = s.substring("remember=".length()).equals("true");
				}
			}
			
			ValidityChecker.checkLogin(login, true); // TODO: only for non-web-mode
			
			login = UsersManager.getUserLogin(login);
			if (login == null)
				throw new RuntimeException("Login or password incorrect");
			
			String redirect = request.getQueryString(); // not encoded
			
			JsonElement _salt = API.registry.getValue("xusers/"+login+"/salt");
			String salt = _salt==null?"":_salt.getAsString();

			JsonElement sha_expire_time = API.registry.getValue("xusers/"+login+"/tokens/hashed/"+DigestUtils.sha256Hex(password+salt));
			if (sha_expire_time==null)
				throw new RuntimeException("Login or password incorrect");
			
						 
			if (UTCDate.expired(sha_expire_time.getAsString())) {
				// redirecting to password change
				
				String change_redirect = "/apps/login/change.html?login="+login;
				if ((redirect!=null) && (!redirect.isEmpty()))
					change_redirect += "&redirect="+redirect;
				
				if (jsonResult) {
					response.getOutputStream().print("{\"redirect\":\""+change_redirect+"\"}");					
				}
				else {
					response.sendRedirect(change_redirect);
				}
				return;
			}
			
			JsonElement email_verified = API.registry.getValue("xusers/"+login+"/email_verified");
			if ((email_verified!=null) && (!email_verified.getAsBoolean()))
				throw new RuntimeException("User's e-mail not verified yet");
			
			JsonElement blocked = API.registry.getValue("xusers/"+login+"/blocked");
			if ((blocked!=null) && (blocked.toString().equals("true")))
				throw new RuntimeException("User is blocked");

			String token = RandomToken.generateRandomToken();
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			if (remember)
				c.add(Calendar.DATE, 365); // token valid for 1 year
			else
				c.add(Calendar.MINUTE, 60); // token valid for 1 hour				
			String token_expires = UTCDate.stringify(c.getTime());
			
			API.registry.setValue("xusers/"+login+"/tokens/ws/"+token, token_expires);
			
						
			
			if ((redirect == null) || (redirect.isEmpty())) {
				// redirecting to desktop app
				redirect = "Desktop"; // default desktop app
				JsonElement el = API.registry.getValue("users/"+login+"/desktop_app");
				if ((el!=null) && (!el.toString().isEmpty())) {
					redirect = el.toString();
				}
				redirect = API.config.domain_or_ip+":"+(request.isSecure()?API.config.secure_port:API.config.port)+"/apps/"+redirect.toLowerCase();
			}
			
			// deleting previous ws_token=value (if any) from the redirect string
			int i = redirect.indexOf("?ws_token=");
			if (i<0)
				i = redirect.indexOf("&ws_token=");
			if (i>=0) {
				int j = redirect.indexOf("&", i+1);
				if (j<0)
					j=redirect.length()-1;
				
				// we will have to delete a substring from i+1 to j inclusive
				redirect = redirect.substring(0, i+1) + redirect.substring(j+1);
			}
			logger.trace("redirect was "+redirect);
			
			// attaching ws_token
			if (redirect.indexOf('?')<0)
				redirect += "?ws_token="+token;
			else {
				if (redirect.endsWith("&"))
					redirect += "ws_token="+token;
				else
					redirect += "&ws_token="+token;
			}
			
			redirect += "&login="+login;
			logger.trace("redirect now "+redirect);
			
			
			if (!redirect.startsWith("http://") && !redirect.startsWith("https://")) {
		    	if (request.isSecure()) {
		    		redirect = "https://"+redirect;
		    	}
		    	else {
		    		redirect = "http://"+redirect;
		    	}
			}
			
			if (jsonResult)
				response.getOutputStream().print("{\"login\":\""+login+"\",\"ws_token\":\""+token+"\",\"expires\":\""+token_expires+"\",\"redirect\":\""+redirect+"\"}");
			else {
				response.sendRedirect(redirect);
			}
    	}
    	catch(Throwable t) {
			logger.error(t.getMessage());
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.trace(errors.toString());
			}
			
    		if (jsonResult)
    			response.getOutputStream().print("{\"error\":\""+t.getMessage().replace('\"', ' ')+"\"}");
    		else {    			
    			redirectToMessage("signin_failed", t.getMessage(), response);    			
    		}
    	}		
	}
	
	private void signup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			if (!signup_allowed){
				throw new RuntimeException("Signing up not allowed");
			}
			
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			if (!"application/x-www-form-urlencoded".equals(request.getContentType()))
				throw new RuntimeException("Bad encoding type");
			
			String formData = java.net.URLDecoder.decode(IOUtils.toString(request.getInputStream(), utf8_charset), utf8_charset.name());
			String[] arr = formData.split("&");
			
			String desired_login = null;
			String email = null;
			String password = null;
			String recaptcha_response = "";
			
			JsonObject xuser = new JsonObject();
			JsonObject user = new JsonObject();
			
			JsonObject formdata = new JsonObject();
			
			JsonObject user_tokens = new JsonObject();
			xuser.add("tokens", user_tokens);
			
			String salt = RandomToken.generateSalt();
			
			xuser.addProperty("salt", salt);
			
			for (String s : arr) {    				
				if (s.startsWith("g-recaptcha-response=")) {
					recaptcha_response = s.substring("g-recaptcha-response=".length());
				}
				else
				if (s.startsWith("password=")) {
					password = s.substring("password=".length());
					
					JsonObject hashed = new JsonObject();						
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					c.add(Calendar.DATE, password_expire_days);						
					hashed.addProperty(DigestUtils.sha256Hex(password+salt), UTCDate.stringify(c.getTime()));
					user_tokens.add("hashed", hashed);
					
					formdata.addProperty(password, password);
				}
				else
				if (s.startsWith("email=")) {
					email = s.substring("email=".length()).trim();
    				xuser.addProperty("email", email);													
					formdata.addProperty("email", email);
				}
				else
				if (s.startsWith("desired_login=")) {
					desired_login = s.substring("desired_login=".length()).trim();
					// do not store in xuser or user
					formdata.addProperty("desired_login", desired_login);
					if ("standalone".equals(desired_login))
						desired_login += "1";
				}
				else {
					// adding to user
					int i = s.indexOf("=");
					if (i<0)
						continue;
					String attr = s.substring(0, i);
					String val = s.substring(i+1);
    				user.addProperty(attr, val);
					formdata.addProperty(attr, val);
				}
			}

			ValidityChecker.checkLogin(email, desired_login, false); // don't allow standalone (but we should have appended 1 above)
			ValidityChecker.checkTokenLike(recaptcha_response, "Illegal reCAPTCHA response", true);			
			
			// verify reCAPTCHA
		    if (!captchaSolved(recaptcha_response)) {
		    	throw new RuntimeException("Captcha not solved");
		    }
		    

		    
			// validate e-mail, user-name, and password
		    if ( ((email==null)||(email.isEmpty())) && ((desired_login==null)||desired_login.isEmpty()))
		    	throw new RuntimeException("E-mail or desired login must be specified");
		    		    
		    // check whether e-mail has been already registered
		    if ((email!=null) && (!email.isEmpty())) {
				ValidityChecker.checkEmail(email);
		    	
		    	JsonElement el = API.registry.getValue("xusers/"+email);
		    	if (el instanceof JsonObject) {
		    		JsonElement el1 = ((JsonObject)el).get("email_verified");
		    		if ((el1!=null)&&(el1.getAsBoolean()))
		    			throw new RuntimeException("E-mail already registered");
		    		JsonElement el2 = ((JsonObject)el).get("last_email_time");
		    		
		    		if (el2!=null) {
		    			if (!UTCDate.expired(UTCDate.addMinutes(el2.getAsString(), signup_interval_minutes))) // max 1 request per <signup_interval_minutes> minutes for the same e-mail
		    				throw new RuntimeException("Too many sign up requests for this e-mail. Please, try again later.");
		    		}
		    	}
		    }
		    
		    
		    xuser.addProperty("signup_time", UTCDate.stringify(new Date()));
		    xuser.addProperty("last_email_time", UTCDate.stringify(new Date()));
		       			
			
			if (signup_policy.startsWith("webcall:")) {
				// TODO: async servlet response...
				
				String action = signup_policy.substring("webcall:".length());
				
				String result = null;
				
				try {
					try {
						WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
						seed.actionName = action;
						seed.jsonArgument = formdata.toString();
						seed.jsonResult = new CompletableFuture<String>();
						API.webCaller.enqueue(seed);
						result = seed.jsonResult.get();
					}
					catch(Throwable t) {
						result = "{\"error\":\"Could not launch verification function\"}";
					}
    				//the web call must return something like this:
    				//	{
    				//    result: "deny"|"manual"|"email"|"email+manual"|"allow",
    				//    error: "Password too short"
    				//  }
				}
				catch(Throwable t) {    					
				}
				
				signup_policy = "deny";
				String error = null;
				try {
					JsonObject res = (JsonObject) new JsonParser().parse(result);
					signup_policy = res.get("result").getAsString();
					error = res.get("error").getAsString();
				}
				catch(Throwable t) {    					
				}
				
				if (!"manual".equals(signup_policy) && !"email".equals(signup_policy) && !"email+manual".equals(signup_policy) && !"allow".equals(signup_policy)) {
					signup_policy = "deny";
					if (error!=null)
						throw new RuntimeException(error);
				}    				
			}
			
			String login;
			if ((desired_login != null) && (!desired_login.trim().isEmpty())) {
				login = desired_login;    				
				JsonElement el = API.registry.getValue("xusers/"+login);
				while (el != null) {
					login = login + new Random().nextInt(10); 
					el = API.registry.getValue("xusers/"+login);
				}
			}
			else
				login = email;
			
			xuser.addProperty("_id", login);
			user.addProperty("_id", login);
				
   			if (signup_policy.startsWith("email")) { // "email" | "email+manual"
				if (email==null)
					throw new RuntimeException("E-mail not specified for validation");

				JsonObject emailedToken = new JsonObject();						
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.DATE, 1); // e-mail verification valid for 1 day					
				String token = RandomToken.generateRandomToken();
				emailedToken.addProperty(token, UTCDate.stringify(c.getTime()));
				user_tokens.add("emailed", emailedToken);				
				xuser.addProperty("email_verified", false); // require to set email_verified=true explicitly after the email validation
				
				if (!"email".equals(signup_policy)) { // "email+manual"
					xuser.addProperty("blocked", true); // require to set blocked=false manually (e.g., via "webappos approveuser")
				}
				
    			API.registry.setValue("xusers/"+login, xuser);
    			API.registry.setValue("users/"+login, user);
    			
    			
    			String verify_url = request.getRequestURI();
    			if (verify_url.endsWith("/signup"))
    				verify_url = verify_url.substring(0, verify_url.length()-"/signup".length());
    			verify_url += "/verify/"+login+"/"+token;

    	    	if (request.isSecure()) {
    	    		verify_url = "https://"+API.config.domain_or_ip+verify_url;
    	    	}
    	    	else {
    	    		verify_url = "http://"+API.config.domain_or_ip+verify_url;
    	    	}
    			
    			if (API.emailSender.sendEmail(email, "E-mail validation", "We have just created a login `"+login+"' for you. Please, click this link to validate your e-mail: "+verify_url)) {        			        			
    				response.sendRedirect("/apps/login/signup_mailed.html");
    			}
    			else
    				throw new RuntimeException("Could not send verification e-mail");
				return;
			}
			    			    			
			if (signup_policy.equals("allow")) {
    			API.registry.setValue("xusers/"+login, xuser);
    			API.registry.setValue("users/"+login, user);
				response.sendRedirect("/apps/login/signup_ok.html");
				return;
			}
			
			if (signup_policy.equals("deny")) {
				// we won't save the xuser and user;
				
				// "deny" must have been checked on top of the function: if (!signup_allowed) ...
				// however, the webcall could also return "deny", thus, we check it again here							
				response.sendRedirect("/apps/login/signup_failed.html");
				return;
			}
			
			
			// else: assume "manual"
			xuser.addProperty("blocked", true); // require to set blocked=false manually (e.g., via "webappos approveuser")			
			API.registry.setValue("xusers/"+login, xuser);
			API.registry.setValue("users/"+login, user);
			response.sendRedirect("/apps/login/signup_processing.html");
		}
		catch (Throwable t) {
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.trace(errors.toString());
			}
			
			redirectToMessage("signup_failed", t.getMessage(), response);
		}        			
	}
	
	private void password_reset_request(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {  			
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			if (!"application/x-www-form-urlencoded".equals(request.getContentType()))
				throw new RuntimeException("Bad enctype");
			
			String formData = java.net.URLDecoder.decode(IOUtils.toString(request.getInputStream(), utf8_charset), utf8_charset.name());
			String[] arr = formData.split("&");
			
			String email = null;
			String recaptcha_response = "";
			for (String s : arr) {
				
				if (s.startsWith("email=")) {
					email = s.substring("email=".length()).trim();
				}
				if (s.startsWith("g-recaptcha-response=")) {
					recaptcha_response = s.substring("g-recaptcha-response=".length());
				}
			}
					    
		    
			// validate
	    	ValidityChecker.checkEmail(email);
	    	ValidityChecker.checkLogin(email, false); // use email as login	    	
			ValidityChecker.checkTokenLike(recaptcha_response, "Illegal reCAPTCHA response", true);			

			// verify reCAPTCHA
		    if (!captchaSolved(recaptcha_response)) {
		    	throw new RuntimeException("Captcha not solved");
		    }
	    	
	    	JsonElement el = API.registry.getValue("xusers/"+email);
	    	if (el instanceof JsonObject) {
	    		JsonElement el1 = ((JsonObject)el).get("email_verified");
	    		if ((el1!=null) && (!el1.getAsBoolean()))
	    			throw new RuntimeException("User's e-mail not verified yet");
	    		
	    		JsonElement el2 = ((JsonObject)el).get("last_email_time");
	    		
	    		if (el2!=null) {
	    			if (!UTCDate.expired(UTCDate.addMinutes(el2.getAsString(), 10))) // max 1 request per 10 minutes for the same e-mail
	    				throw new RuntimeException("Too many requests for this e-mail. Please, try again later.");
	    		}
	    	}
	    	else {
	    		// user not found, but we still inform that the password reset request has been mailed if the user exists
	    		response.sendRedirect("/apps/login/forgot_mailed.html"); 
	    		return;
	    	}
	    	
	    	API.registry.setValue("xusers/"+email+"/last_email_time", UTCDate.stringify(new Date()));
	    	
	    	StringBuilder sb = new StringBuilder();
	    	String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$.,";
	    	Random r = new Random();
	    	for (int i= 0; i<8; i++) {
	    	    int k = r.nextInt(chars.length());
	    	    sb.append(chars.charAt(k));
	    	}
	    	
			JsonElement _salt = API.registry.getValue("xusers/"+email+"/salt");
			String salt = _salt==null?"":_salt.getAsString();
	    	String pass = sb.toString();
	    	
	    	API.registry.setValue("xusers/"+email+"/tokens/hashed/"+DigestUtils.sha256Hex(pass+salt), UTCDate.stringify(new Date()));
	    		// expires right away

			if (API.emailSender.sendEmail(email, "Password reset", "Please, log in with this one-time password `"+pass+"'")) {        			        			
				response.sendRedirect("/apps/login/forgot_mailed.html");
			}
			else
				throw new RuntimeException("Could not send verification e-mail");
			return;
		    
		}
		catch (Throwable t) {
			logger.error(t.getMessage());
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}

			redirectToMessage("forgot_failed", t.getMessage(), response);			
		}        	
		
	}
	
	private void change_password(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {  			
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			if (!"application/x-www-form-urlencoded".equals(request.getContentType()))
				throw new RuntimeException("Bad enctype");
			
			String formData = java.net.URLDecoder.decode(IOUtils.toString(request.getInputStream(), utf8_charset), utf8_charset.name());
			String[] arr = formData.split("&");
			
			String login = null;
			String old_password = null;
			String new_password = null;
			String recaptcha_response = "";
			for (String s : arr) {
				
				if (s.startsWith("login=")) {
					login = s.substring("login=".length()).trim();
				}
				else
				if (s.startsWith("g-recaptcha-response=")) {
					recaptcha_response = s.substring("g-recaptcha-response=".length());
				}
				else
				if (s.startsWith("old_password=")) {
					old_password = s.substring("old_password=".length());
				}
				else
				if (s.startsWith("new_password=")) {
					new_password = s.substring("new_password=".length());
				}
			}
			
			ValidityChecker.checkLogin(login, false);
			ValidityChecker.checkToken(recaptcha_response);
			
			// verify reCAPTCHA
		    if (!captchaSolved(recaptcha_response)) {
		    	throw new RuntimeException("Captcha not solved");
		    }
		    
		    
			// validate login and passwords
		    if ((login==null)||(login.trim().isEmpty())) 
		    	throw new RuntimeException("Login must be specified");
		    
		    if ((old_password==null)||(old_password.isEmpty())) 
		    	throw new RuntimeException("Old password must be specified");
		    
		    if ((new_password==null)||(new_password.isEmpty())) 
		    	throw new RuntimeException("New password must be specified");
		    
	    	JsonElement el = API.registry.getValue("xusers/"+login);	    	
	    	if (el instanceof JsonObject) {
	    		JsonElement el1 = ((JsonObject)el).get("email_verified");
	    		if ((el1!=null) && (!el1.getAsBoolean()))
	    			throw new RuntimeException("User's e-mail not verified yet");	    		
	    		JsonElement el2 = ((JsonObject)el).get("blocked");
	    		if ((el2!=null) && (el2.getAsBoolean()))
	    			throw new RuntimeException("User is blocked");	    		
	    	}
	    	else {
	    		// user not found, but we still inform just that the password change failed 
	    		response.sendRedirect("/apps/login/change_failed.html"); 
	    		return;
	    	}
	    	
	    	JsonElement xuser = API.registry.getValue("xusers/"+login);
	    	if (!(xuser instanceof JsonObject)) {
	    		// user not found, but we still inform just that the password change failed 
	    		response.sendRedirect("/apps/login/change_failed.html"); 
	    		return;	    		
	    	}
	    	
			JsonElement _salt = API.registry.getValue("xusers/"+login+"/salt");
			String salt = _salt==null?"":_salt.getAsString();	    	
	    	
	    	JsonElement tokens = ((JsonObject)xuser).get("tokens");
	    	if (!(tokens instanceof JsonObject)) {
	    		response.sendRedirect("/apps/login/change_failed.html"); 
	    		return;
	    	}
	    	
	    	JsonElement hashed = ((JsonObject)tokens).get("hashed");
	    	if (!(hashed instanceof JsonObject)) {
	    		response.sendRedirect("/apps/login/change_failed.html"); 
	    		return;
	    	}
	    	
	    	JsonElement expire_time = ((JsonObject)hashed).get(DigestUtils.sha256Hex(old_password+salt)); 
	    			
	    	if ((expire_time == null) || (expire_time.toString().isEmpty())) {
	    		// no such old password
	    		response.sendRedirect("/apps/login/change_failed.html"); 
	    		return;	    		
	    	}
	    	
	    	((JsonObject)tokens).remove("hashed");
	    	hashed = new JsonObject();
	    	
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, password_expire_days);						
	    	
	    	((JsonObject)hashed).addProperty(DigestUtils.sha256Hex(new_password+salt), UTCDate.stringify(c.getTime()));
	    	((JsonObject)tokens).add("hashed", hashed); 
	    	

	    	if (!API.registry.setValue("xusers/"+login, xuser))
	    		throw new RuntimeException("Error accessing the registry.");
	    	
			String redirect = null;
			// checking for redirect attribute in query string...
			String qs = request.getQueryString();
			if (qs!=null) {
				String[] arr2 = qs.split("&");
				for (String s : arr2) {
					if (s.startsWith("redirect=")) {
						redirect = s.substring("redirect=".length());
						break;
					}
				}
			}
			
			if ((redirect == null) || (redirect.isEmpty())) {
				response.sendRedirect("/apps/login/change_ok.html");
			}
			else {
				// password changed; redirecting to the required page...
				response.sendRedirect(redirect);
			}
	    	
			return;
		    
		}
		catch (Throwable t) {
			logger.error(t.getMessage());
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
			redirectToMessage("change_failed", t.getMessage(), response);
		}        	
		
	}

	private void check_ws_token(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
    	try {
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			if (!"application/x-www-form-urlencoded".equals(request.getContentType()))
				throw new RuntimeException("Bad enctype");
			
			String formData = java.net.URLDecoder.decode(IOUtils.toString(request.getInputStream(), utf8_charset), utf8_charset.name());
			String[] arr = formData.split("&");
			
			String login=null, ws_token=null;
			for (String s : arr) {    	
				if (s.startsWith("login=")) {
					login = s.substring("login=".length()).trim();
				}
				if (s.startsWith("ws_token=")) {
					ws_token = s.substring("ws_token=".length());
				}
			}
			
			ValidityChecker.checkLogin(login, false);
			
			JsonElement sha_expire_time = API.registry.getValue("xusers/"+login+"/tokens/ws/"+ws_token);
			if (sha_expire_time==null)
				throw new RuntimeException("Login or ws_token incorrect");			
			

			if (UTCDate.expired(sha_expire_time.getAsString())) {
				throw new RuntimeException("ws_token expired");
			}
			
			JsonElement email_verified = API.registry.getValue("xusers/"+login+"/email_verified");
			if ((email_verified!=null) && (!email_verified.getAsBoolean()))
				throw new RuntimeException("User's e-mail not verified yet");
			
			JsonElement blocked = API.registry.getValue("xusers/"+login+"/blocked");
			if ((blocked!=null) && (blocked.toString().equals("true")))
				throw new RuntimeException("User is blocked");

			response.getOutputStream().print("{\"result\":\"true\"}");
    	}
    	catch(Throwable t) {
			logger.error(t.getMessage());
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.trace(errors.toString());
			}
			
   			response.getOutputStream().print("{\"result\":\"false\",\"error\":\""+t.getMessage().replace('\"', ' ')+"\"}");
    	}		
	}
	
	private void signout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
    	try {
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			if (!"application/x-www-form-urlencoded".equals(request.getContentType()))
				throw new RuntimeException("Bad enctype");
			
			String formData = java.net.URLDecoder.decode(IOUtils.toString(request.getInputStream(), utf8_charset), utf8_charset.name());
			String[] arr = formData.split("&");
			
			String login=null, ws_token=null;
			for (String s : arr) {    	
				if (s.startsWith("login=")) {
					login = s.substring("login=".length()).trim();
				}
				if (s.startsWith("ws_token=")) {
					ws_token = s.substring("ws_token=".length());
				}
			}
			
			ValidityChecker.checkLogin(login, false);
			
			JsonElement email_verified = API.registry.getValue("xusers/"+login+"/email_verified");
			if ((email_verified!=null) && (!email_verified.getAsBoolean()))
				throw new RuntimeException("User's e-mail not verified");
			
			JsonElement blocked = API.registry.getValue("xusers/"+login+"/blocked");
			if ((blocked!=null) && (blocked.toString().equals("true")))
				throw new RuntimeException("User is blocked");

			API.registry.setValue("xusers/"+login+"/tokens/ws/"+ws_token, null);
			
    	}
    	catch(Throwable t) {
			logger.error(t.getMessage());
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.trace(errors.toString());
			}
			
    	}		
//		response.sendRedirect("/apps/login?signout=true");
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
        String path = request.getPathInfo();
        if (path == null)
        	path = "";
        
        if (path.startsWith("/"))
        	path = path.substring(1);
        if (path.endsWith("/"))
        	path = path.substring(0, path.length()-1);
	        	       
        if (path.equals("signup")) {
        	signup(request, response);
    		return;
        }
        if (path.equals("password_reset_request")) {
        	password_reset_request(request, response);
    		return;
        }        
        if (path.equals("change_password")) {
        	change_password(request, response);
    		return;
        }
        if (path.equals("signin")) {
        	signin(request, response, false);
    		return;
        }        
        if (path.equals("signin_token")) {
        	signin(request, response, true);
        	return;
        }
        if (path.equals("check_ws_token")) {
        	check_ws_token(request, response);
        	return;
        }
        if (path.equals("signout")) {
        	signout(request, response);
        	return;
        }
    }	
	
}