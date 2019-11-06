package org.webappos.service.webcalls;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.properties.WebAppProperties;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webcaller.IWebCaller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lv.lumii.tda.kernel.TDAKernel;



@SuppressWarnings( "serial" )
public class WebCallsServlet extends HttpServlet
{
	private static Logger logger =  LoggerFactory.getLogger(WebCallsServlet.class);
	private static Charset utf8_charset = Charset.forName("UTF-8");
	
	
/*	public static void setProperties(Properties p) { // file service.properties for the given webAppOS service
		properties = p;
	}*/
	
	
	/**
	 * In accordance with the Post-Redirect-Get pattern, we do not write
	 * the response text directly, but redirect to certain HTML 
	 * (see https://en.wikipedia.org/wiki/Post/Redirect/Get).
	 * 
	 * @param fullAppName - the full name of the app used to make this web call
	 * @param app_url_name - the url name of the app used to make this web call
	 * @param htmlName - HTML file name w/o extension; the file should contain the "<!--[field-name]-->" comments to be replaced with actual values
	 * @param obj - the JSON object, which fields will be used to replace the <!--[field-name]--> comments
	 * @param response - the response stream
	 * @param redirect - whether to redirect or just write to the output stream
	 * @throws IOException
	 */
	private void pipeToHTML(String fullAppName, String app_url_name, String htmlName, JsonObject obj, HttpServletResponse response, boolean redirect) throws IOException {
		
		if (obj==null || fullAppName==null) {
			if (app_url_name == null)
				response.sendRedirect(htmlName); // direct redirect
			else
				response.sendRedirect("/apps/"+app_url_name+"/"+htmlName+".html");
			return;
		}
		
		
		
		try {
			String s = new String(Files.readAllBytes(Paths.get(ConfigStatic.APPS_DIR+File.separator+fullAppName+File.separator+"web-root"+File.separator+htmlName+".html")), "UTF-8");
			String orig = s;
			for (String key : obj.keySet())
				s = s.replace("<!--"+key+"-->", obj.get(key).getAsString());
			
			if (orig.equals(s)) {
				// nothing was replaced; redirecting to the original file
				if (redirect)
					response.sendRedirect("/apps/"+app_url_name+"/"+htmlName+".html");
				else
					response.getOutputStream().println(s);
			}
			else {
				if (redirect) {
					int hash = s.hashCode();
					File f = new File(ConfigStatic.WEB_ROOT_CACHE_DIR+File.separator+app_url_name+".cache"+File.separator+htmlName+hash+".html");
					if (!f.exists()) {
						f.getParentFile().mkdirs();
						Files.write(f.toPath(), s.getBytes(utf8_charset));
					}
					if (f.exists())
						response.sendRedirect("/apps/"+app_url_name+"/"+app_url_name+".cache/"+htmlName+hash+".html");
					else
						response.sendRedirect("/apps/"+app_url_name+"/"+htmlName+".html");					
				}
				else
					response.getOutputStream().println(s);
			}
			
		}
		catch(Throwable tt) {				
			response.sendRedirect("/apps/"+app_url_name+"/"+htmlName+".html");					
		}
		
	}
	
	public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
/*		Cookie[] cookies = request.getCookies();

		if (cookies != null)
			for (int i = 0; i < cookies.length; i++) {
			  String name = cookies[i].getName();
			  String value = cookies[i].getValue();
			  System.out.println("COOKIE "+name+"="+value);
			}
		else {
			System.out.println("NO COOKIE");
		}*/
		
		String fullAppName = null, app_url_name=null;
        String action = request.getPathInfo(); 
        boolean isJsonData = "application/json".equals(request.getContentType());
        boolean isFormData = "multipart/form-data".equals(request.getContentType());
        boolean isURLData = "application/x-www-form-urlencoded".equals(request.getContentType());        
        boolean PRG = false; // post-redirect-get pattern
        
		String on_success_html=null, on_error_html=null;

        try {        	
            if ((action == null) || (action.isEmpty())) {
            	throw new RuntimeException("No webcall action specified");            	
            }
            
            if (action.startsWith("/"))
            	action = action.substring(1);
            
            ValidityChecker.checkWebCallActionName(action);
            
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			
			
			String login=null, ws_token=null, project_id=null;
			String argument = null;
			JsonObject jsonArgument = new JsonObject();
			long tdaArgument = 0;
			
			// checking for attributes in query string...
			String qs = request.getQueryString();			
			if (qs!=null) {
				int k = Math.max(qs.lastIndexOf('|'), qs.lastIndexOf('^'));
				if (k>=0) {
					if (qs.charAt(k)=='^')
						PRG = true;
					String[] arr = qs.substring(k+1).split(",");
					if (arr.length>=1)
						on_success_html = arr[0];
					if (arr.length>=2)
						on_error_html = arr[1];
					qs = qs.substring(0, k);
				}
				
				qs =  URLDecoder.decode(qs, "UTF-8");
				String[] arr2 = qs.split("&");
				for (String s : arr2) {
					if (s.startsWith("login=")) {
						login = s.substring("login=".length());
					}
					else
					if (s.startsWith("ws_token=")) {
						ws_token = s.substring("ws_token=".length());
					}
					else
					if (s.startsWith("project_id=")) {
						project_id = s.substring("project_id=".length());
					}
					else
					if (s.startsWith("app_url_name=")) {
						app_url_name = s.substring("app_url_name=".length());
					}
					else
					if (s.startsWith("json_argument=")) {
						String arg = s.substring("json_argument=".length());
						// parse arg...
						try {
							JsonElement jelement = new JsonParser().parse(arg);
							jsonArgument = jelement.getAsJsonObject();
						}
						catch(Throwable t) {							
						}						
					}
					else
					if (s.startsWith("argument=")) {
						argument = s.substring("argument=".length());
					}
					else
					if (s.startsWith("tda_argument=")) {
						String arg = s.substring("tda_argument=".length());
						// parse arg...
						try {
							tdaArgument = Long.parseLong(arg);
						}
						catch(Throwable t) {							
						}						
					}
					else {
						int i = s.indexOf('=');
						if (i>=0) {
							jsonArgument.addProperty(s.substring(0, i), s.substring(i+1));
						}
					}
				}
			}
						
			TDAKernel kernel = null;
			
			if (login != null) {			
				ValidityChecker.checkLogin(login, false);
				ValidityChecker.checkToken(ws_token);
				if (project_id!=null) {
					ValidityChecker.checkRelativePath(project_id, false);				
					if (!UsersManager.projectOK(project_id, login, ws_token, true))
						throw new RuntimeException("Login/token invalid");
				}
				else {
					if (!UsersManager.ws_token_OK(login, ws_token, true))						
						throw new RuntimeException("Login/token invalid");
				}
				
				if (project_id!=null) {
					kernel = API.dataMemory.getTDAKernel(project_id);				
					if (kernel == null)
						throw new RuntimeException("Project not active");
					
										
					// validate project content...
					// !!!TODO: validate projectDir
					// validate other system-specific settings, e.g., language, country codes...

				}
			}
			
/*			if (cookies == null) {
				Cookie cookie = new Cookie("login", login);
				response.addCookie(cookie);
				Cookie cookie2 = new Cookie("ws_token", ws_token);
				cookie2.setHttpOnly(true);
				cookie2.setPath("/");
				//cookie2.setSecure(true);
				response.addCookie(cookie2);
			}*/
			
			
			if (isJsonData) {
				String data = IOUtils.toString(request.getInputStream(), utf8_charset);
				argument = data;
			}
			else
			if (isFormData) {
		        FileItemFactory factory = new DiskFileItemFactory();
		        ServletFileUpload upload = new ServletFileUpload(factory);
		        List<FileItem> items = upload.parseRequest(request);
				
		        upload.setHeaderEncoding("UTF-8"); 
			    for (FileItem item : items) {
			        if (!item.isFormField()) {
			        } else {
			            String name = item.getFieldName();
			            String value = item.getString();
			            jsonArgument.addProperty(name, value);
			        }
			    }
			}
			else
			if (isURLData) {
				String data = IOUtils.toString(request.getInputStream(), utf8_charset);
				data = java.net.URLDecoder.decode(data, "UTF-8");
				String[] arr = qs.split("&");
				for (String s : arr) {
					int i = s.indexOf('=');
					if (i>=0) {
						jsonArgument.addProperty(s.substring(0, i), s.substring(i+1));
					}					
				}
			}
			
			// TODO: limit data length
        	
			String retVal;
			IWebCaller.WebCallDeclaration decl = API.webCaller.getWebCallDeclaration(action);
			if (decl == null) {
				fullAppName = API.dataMemory.getProjectFullAppName(project_id);
				if ((fullAppName == null) && (app_url_name!=null)) {
					WebAppProperties props = API.propertiesManager.getWebAppPropertiesByUrlName(app_url_name);
					if (props != null)
						fullAppName = props.app_full_name;
				}
				if ((app_url_name==null) && (fullAppName!=null)) {
					WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(fullAppName);
					if (props != null)
						app_url_name = props.app_url_name;
				}
				retVal = "{\"error\":\"Webcall action not found.\"}";
			}
			else {
				fullAppName = API.dataMemory.getProjectFullAppName(project_id);
				if ((fullAppName == null) && (app_url_name!=null)) {
					WebAppProperties props = API.propertiesManager.getWebAppPropertiesByUrlName(app_url_name);
					if (props != null)
						fullAppName = props.app_full_name;
				}
				if (fullAppName==null) {
					if (decl.pwd!=null && decl.pwd.endsWith(".app"))
						fullAppName = new File(decl.pwd).getName();
				}
				if ((app_url_name==null) && (fullAppName!=null)) {
					WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(fullAppName);
					if (props != null)
						app_url_name = props.app_url_name;
				}
				if (decl.callingConventions == IWebCaller.CallingConventions.JSONCALL) {
					try {
						IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
						seed.actionName = action;
						if (argument != null)
							seed.jsonArgument = argument;
						else
							seed.jsonArgument = jsonArgument.toString();
						seed.login = login;
						seed.project_id = project_id;
						
						seed.fullAppName = fullAppName;
						seed.jsonResult = new CompletableFuture<String>();
						API.webCaller.enqueue(seed);
						retVal = seed.jsonResult.get();
						if (retVal==null)
							throw new RuntimeException("The webcall action "+action+" returned null.");
					}
					catch(Throwable t) {
						String msg = t.getMessage();
						if (msg != null)
							retVal = "{\"error\":\""+msg+"\"}";
						else
							retVal = "{\"error\":\"Could not perform webcall action "+action+".\"}";
		        	}
				}
				else {
					// tdacall
					try {
						IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
						seed.actionName = action;
						seed.callingConventions = IWebCaller.CallingConventions.TDACALL;
						if ((tdaArgument == 0) && (argument!=null)) {
							try {
								tdaArgument = Long.parseLong(argument);
							}
							catch(Throwable t) {								
							}
						}
						seed.tdaArgument = tdaArgument;
						seed.login = login;
						seed.project_id = project_id;
						seed.fullAppName = fullAppName;
						seed.jsonResult = null;
						API.webCaller.enqueue(seed);
						retVal = "{}";
					}
					catch(Throwable t) {
						String msg = t.getMessage();
						t.printStackTrace();
						if (msg != null)
							retVal = "{\"error\":\""+msg+"\"}";
						else
							retVal = "{\"error\":\"Could not perform webcall action "+action+"\"}";
		        	}
				}
			}
			
			// parse retVal...
			JsonObject result;
			try {
				JsonElement jelement = new JsonParser().parse(retVal);
				if (jelement.isJsonArray()) {
					// sending result without pipes..
					response.setContentType("application/json");
					response.getOutputStream().println(retVal);
					return;
				}
				else
					result = jelement.getAsJsonObject();				
			}
			catch(Throwable t) {
				System.err.println(retVal);
				throw new RuntimeException("Could not parse the returned JSON.");
			}
			
			if (on_success_html!=null || on_error_html!=null) { // pipe HTML specified...
				
				if (on_error_html!=null) {
					if (result.has("error")) {
						// sending error as response...
						pipeToHTML(fullAppName, app_url_name, on_error_html, result, response, PRG);
						return;
					}
				}
				
				// no error, or on_error_html not specified; piping to on_success_html...
				if (on_success_html!=null) {
					pipeToHTML(fullAppName, app_url_name, on_success_html, result, response, PRG);
					return;
				}				
			}
			
			// sending result without pipes..
			response.setContentType("application/json");
			response.getOutputStream().println(result.toString());
		}
		catch(Throwable t) {
			logger.error(t.getMessage());
			t.printStackTrace();
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.trace(errors.toString());
			}
			String msg = t.getMessage();
			if (msg == null)
				msg = "An error occurred during the webcall: "+t.toString();
			
			JsonObject result = new JsonObject();
			result.addProperty("error", msg);

			if (on_error_html!=null) {
				pipeToHTML(fullAppName, app_url_name, on_error_html, result, response, PRG);
				return;
			}
			
			if (on_success_html!=null) {
				pipeToHTML(fullAppName, app_url_name, on_success_html, result, response, PRG);
				return;
			}				
			
			
			// sending result without pipes..
			response.setContentType("application/json");			
			response.getOutputStream().println(result.toString());
		}
    }
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {		
		processRequest(request, response);
    }
		
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		processRequest(request, response);
    }
		
	
}