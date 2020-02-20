package org.webappos.adapters.app.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.properties.WebAppProperties;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.server.IAppAdapter;


public class AppAdapter implements IAppAdapter {
	private class DefaultServletEx extends DefaultServlet {
		protected void	doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException	{
			String p = request.getServletPath();
			if (p!=null && p.startsWith("/*")) {
				
				p = p.substring(2);
				try {
					ValidityChecker.checkRelativePath(p, false);
				} catch (Throwable t) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED); 
					return;
				}
								
				String q = request.getQueryString();
				if (q==null) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED); 
					return;
				}				
				q = URLDecoder.decode(q, "UTF-8");
				
				String project_id = null, login=null, ws_token = null;
				Boolean auto_content_type=false;
				for (String pair : q.split("&")) {
					String[] arr = pair.split("=");
					if (arr.length>=2) {
						if ("project_id".equals(arr[0]))
							project_id = arr[1];
						else
							if ("login".equals(arr[0]))
								login = arr[1];
							else
								if ("ws_token".equals(arr[0]))
									ws_token = arr[1];
					}
					else {
						if ((arr.length==1) && ("auto_content_type".equals(arr[0])))
							auto_content_type = true;						
					}
				}
				
				if (project_id==null || login==null || ws_token==null) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED); 
					return;
				}

				File f = null;
				try {
					ValidityChecker.checkLogin(login, false);
					ValidityChecker.checkToken(ws_token);
					if (!UsersManager.ws_token_OK(login, ws_token, true))						
						throw new RuntimeException("Login/token invalid");
		        	ValidityChecker.checkRelativePath(project_id, false);
		        	if (!project_id.startsWith(login+"/"))
		        		throw new RuntimeException("Project not owned");
		        	String projectFolder = API.dataMemory.getProjectFolder(project_id);
		        	if (projectFolder == null)
		        		throw new RuntimeException("Project not active");
		        	
		        	if (p.isEmpty())
		        		throw new RuntimeException("Invalid path to download");
		        	
		        	f = new File(projectFolder + File.separator + p);
		        	if (!f.exists() || !f.isFile())
		        		throw new RuntimeException("Invalid path to download");
		        	
				} catch (Throwable t) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED); 
					return;
				}								
				
	        	InputStream is = null;
	        	OutputStream out = null;
	        	try {
		        	is = new FileInputStream(f);
		        	if (auto_content_type) {
		            	// guessing content-type
		        		String mimeType = getServletContext().getMimeType(f.getAbsolutePath());
			            response.setContentType(mimeType);
		        	}
		        	else
		        		response.setContentType("application/octet-stream");
		        	out = response.getOutputStream();
		            IOUtils.copy(is, response.getOutputStream());
	        	}
	            catch(Throwable t) {
	            	throw new IOException("Error while downloading the file", t);
	            }
		        finally {
		        	if (is!=null)
		        		try { is.close(); } catch (Throwable t) {} 
		        	if (out!=null)
		        		try { out.close(); } catch (Throwable t) {} 
		        }
			}
			else
				super.doGet(request, response);
		}
	}

	@Override
	public ContextHandler attachApp(WebAppProperties appProps) {
		
		WebAppContext appContext = new WebAppContext();
		appContext.setWar(ConfigStatic.APPS_DIR+File.separator+appProps.app_full_name+File.separator+"web-root");
		appContext.setContextPath("/");		
		appContext.getMimeTypes().addMimeMapping("mjs", "application/javascript");
		
		ArrayList<String> arr = new ArrayList<String>();
		
		arr.add( ConfigStatic.APPS_DIR+File.separator+appProps.app_full_name+File.separator+"web-root" ); // must always present, then other folders in the search path follow
		for (String lib : appProps.all_required_web_libraries) {
			String dir = ConfigStatic.APPS_DIR+File.separator+lib+File.separator+"web-root"; 
			if (new File(dir).isDirectory()) 
				arr.add(dir);
			
			
		}
		
		arr.add( ConfigStatic.WEB_ROOT_DIR );
		arr.add( ConfigStatic.WEB_ROOT_CACHE_DIR);
		
		ResourceCollection resources = new ResourceCollection(arr.toArray(new String[] {}));
		appContext.setBaseResource(resources);
		
		DefaultServletEx servlet = new DefaultServletEx();
		
		// NO CACHE
		ServletHolder holder = new ServletHolder(servlet);
		holder.setInitParameter("useFileMappedBuffer", "false");
		holder.setInitParameter("cacheControl", "max-age=0, public");
		appContext.addServlet(holder, "/");
				
		// CORS ALLOW (NOT WORKING)
/*		FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
	    filterHolder.setInitParameter("allowedOrigins", "*");
	    filterHolder.setInitParameter("allowedMethods", "GET, POST");
	    appContext.addFilter(filterHolder, "*", null);*/
		
/*		FilterHolder cors = appContext.addFilter(CrossOriginFilter.class,"*",EnumSet.of(DispatcherType.REQUEST));
		cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,DELETE,PUT,HEAD");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");*/
		
		return appContext;
	}


}
