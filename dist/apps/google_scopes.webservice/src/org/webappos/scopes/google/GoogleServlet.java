package org.webappos.scopes.google;

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
public class GoogleServlet extends HttpServlet
{
	private static Logger logger =  LoggerFactory.getLogger(GoogleServlet.class);
	private static Charset utf8_charset = Charset.forName("UTF-8");
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {		
		
		try {
	        String path = request.getPathInfo();
	        if (path == null)
	        	path = "";
	        
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			if (!"application/x-www-form-urlencoded".equals(request.getContentType()))
				throw new RuntimeException("Bad enctype");
			
			String formData = java.net.URLDecoder.decode(IOUtils.toString(request.getInputStream(), utf8_charset), utf8_charset.name());
			String[] arr = formData.split("&");
			
			String idtoken=null;
			for (String s : arr) {    	
				if (s.startsWith("idtoken=")) {
					idtoken = s.substring("idtoken=".length()).trim();
				}
			}
			
			
			// yeeah, we have an idtoken!

			// check with google...
			// if not correct...
			//String email = payload.getEmail();
			 //boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			boolean emailVerified = true;
			if (!emailVerified)
				throw new RuntimeException("idtoken incorrect");
							
			
			// if correct:
			String email = "a@a.lv";// googleObj.getEmail();
			// we have a verified email by Google!
			
			String login = UsersManager.getUserLogin(email);
			if (login == null)
				throw new RuntimeException("Login or email incorrect");
			
				
			ValidityChecker.checkLogin(login, true); // check for wrong symbols; throws an exception on error...
			
			// Checking whether the user exists...
			JsonElement userData = API.registry.getValue("xusers/"+login);
			if (userData == null) {
				// user does not exist; register him...
				
				// TODO: v klass UsersManager dobavitj funkciju addUser(email, login=email, name, ..., emailToAllow=true dlja google usera, false dlja ostalnih)
				//                                               ^^^ vzjatj kod iz LoginServlet.signup
				// v addUser proverjaem:
				// LoginServlet.signupAllowed()
			}
			

			// prodolzhaem...
			
			// now the user certainly exists! generate our own ws_token..
			// v klass UsersManager dobavitj funkciju generateToken(login, expirationDateTime)
			// LoginServlet lines 315-332
			
			
			String redirect = request.getQueryString(); // not encoded
			// LoginServlet line 336...385
			response.sendRedirect(redirect);
			
		}
		catch(Throwable t) {
			response.getOutputStream().print("Hello from our servlet! An exception occurred. User denied!");
			//response.sendError(400);			
		}
		

    }	
	
}