package org.webappos.adapters.service.javaservlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.security.Authenticator;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.webappos.classloader.PropertiesClassLoader;
import org.webappos.properties.WebServiceProperties;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.server.IServiceAdapter;
import org.webappos.server.RegistryAuthenticator;

public class ServiceAdapter implements IServiceAdapter {
	
	private Runnable onStopped = null;
	private class PrefixPostfixRequestWrapper extends HttpServletRequestWrapper {
		private HttpServletRequest req;
		private String prefix, postfix, queryString;
		public PrefixPostfixRequestWrapper(HttpServletRequest _req, String _prefix, String _postfix, String _queryString) {
			super(_req);
			req = _req;
			prefix = _prefix;
			postfix = _postfix;
			queryString = _queryString;
		}

		@Override
		public String getPathInfo() {
			String s = super.getPathInfo();
						
            Principal userPrincipal = req.getUserPrincipal();
            if (userPrincipal==null)
            	return s;
            else {
            	return prefix.replace("$LOGIN",userPrincipal.getName())+s+postfix.replace("$LOGIN", userPrincipal.getName());
            }
		}

		@Override
		public String getQueryString() {
			String s = super.getQueryString();
			if ((queryString==null) || (queryString.isEmpty()))
				return s;
			
			if (s==null)
				s="";
			
			Principal userPrincipal = req.getUserPrincipal();
			
			if (userPrincipal==null)
				return s;
			
			if (s.isEmpty())
				return queryString.replace("$LOGIN", userPrincipal.getName());
			else
				return s+"&"+queryString.replace("$LOGIN", userPrincipal.getName());
		}
		
    	            	
		
	}
	
	private class PrefixPostfixServletWrapper implements Servlet {
		private Servlet delegate;
		private String prefix, postfix, queryString;
		public PrefixPostfixServletWrapper(Servlet _delegate, String _prefix, String _postfix, String _queryString) {
			delegate = _delegate;
			prefix = _prefix;
			postfix = _postfix;
			queryString = _queryString;
		}
		@Override
		public void destroy() {
			delegate.destroy();
		}
		@Override
		public ServletConfig getServletConfig() {
			return delegate.getServletConfig();
		}
		@Override
		public String getServletInfo() {
			return delegate.getServletInfo();
		}
		@Override
		public void init(ServletConfig arg0) throws ServletException {
			delegate.init(arg0);
		}
		@Override
		public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
			if (req instanceof HttpServletRequest) {
				HttpServletRequest w = new PrefixPostfixRequestWrapper((HttpServletRequest)req, prefix, postfix, queryString);
				delegate.service(w, resp);
			}
			else
				delegate.service(req, resp);
			
		}
	}

	@Override
	public ContextHandler attachService(WebServiceProperties svcProps, String path, Runnable onStopped, Runnable onHalted) {
		
		String servlet_class = svcProps.properties.getProperty("servlet_class");
		if (servlet_class==null) {
			throw new RuntimeException("Servlet class not specified for "+svcProps.service_full_name+" in service.properties (and no web-root found).");
		}
				
		API.classLoader.addClasspathsForPropertiesId(svcProps.id);
		
		Class<? extends Servlet> servletClass;
		try {
			servletClass = (Class<? extends Servlet>) API.classLoader.findClassByName(servlet_class);
		} catch (Throwable t) {
			throw new RuntimeException("Error getting servlet class for "+svcProps.service_full_name+". Reason: "+t.getMessage());
		} 
		
		
		Method m = null;
		try {
			m = servletClass.getMethod("setProperties", Properties.class); // static method
			if (m!=null)
				m.invoke(null, svcProps.properties);
		} catch (Throwable t) {
		}

		ServletHolder holder = new ServletHolder();
        if (m==null) {
			// setting init parameters instead of calling setProperties
			for (Object k : svcProps.properties.keySet()) {
				holder.setInitParameter(k.toString(), svcProps.properties.getProperty(k.toString()).toString());
			}
		}				        				        			
		
		if ("true".equalsIgnoreCase(svcProps.properties.getProperty("http_auth_required", "false"))) {			
			ConstraintSecurityHandler security = new ConstraintSecurityHandler();
			
			UserStore userStore = new UserStore();

			// if you need to authenticate users in addition to RegistryAuthenticator, add them as follows:
			// userStore.addUser("login", Credential.getCredential("password"), new String[] {"user", "role2"});
			
			HashLoginService l = new HashLoginService();
			l.setUserStore(userStore);
	        l.setName("myrealm");
	        
	        Constraint constraint = new Constraint();
	        constraint.setName("auth");
	        constraint.setRoles(new String[]{"**"});
	        constraint.setAuthenticate(true);
	         
	        ConstraintMapping cm = new ConstraintMapping();
	        cm.setConstraint(constraint);
	        cm.setPathSpec("/*");
	        security.addConstraintMapping(cm);

	        Authenticator a = new RegistryAuthenticator(); // instead of DigestAuthenticator() or BasicAuthenticator()
	        security.setAuthenticator(a);
	        	        	        	        
	        security.setRealmName("myrealm");
	        security.setLoginService(l);
			
			//ServletContextHandler contextHandler = new PrefixPostfixContextHandler(path, svcProps.properties.getProperty("auth_path_prefix", ""), svcProps.properties.getProperty("auth_path_postfix", ""));
			ServletContextHandler contextHandler = new ServletContextHandler(null, path, false, false);

			contextHandler.setSecurityHandler(security);
			

			try {
				holder.setServlet(new PrefixPostfixServletWrapper(servletClass.getConstructor().newInstance(), svcProps.properties.getProperty("auth_path_prefix", ""), svcProps.properties.getProperty("auth_path_postfix", ""), svcProps.properties.getProperty("auth_query_string", "")));
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
			contextHandler.addServlet(holder, "/*");
			
			this.onStopped = onStopped;
			return contextHandler;
		}
		else {		
			ServletContextHandler contextHandler =  new ServletContextHandler(null, path, false, false);

			

			String webroot = ConfigStatic.APPS_DIR+File.separator+svcProps.service_full_name+File.separator+"web-root";
			File f = new File(webroot);
			if (f.exists() && f.isDirectory()) {
				// adding web-root handler followed by a servletClass instance...
				DefaultServlet defaultServlet = new DefaultServlet();
				ServletHolder holderPwd = new ServletHolder("default", defaultServlet);
				holderPwd.setInitParameter("resourceBase", webroot);				
				//holderPwd.setInitParameter("dirAllowed","true");
		        holderPwd.setInitParameter("pathInfoOnly","true");
				contextHandler.addServlet(holderPwd, "/*");
				contextHandler.setErrorHandler(new ErrorHandler() {
					 @Override
				        public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
							// handle 404 to servletClass instance
						 	try {
								holder.handle(baseRequest, request, response);
							} catch (ServletException e) {
							}
							// Variant B:
							// 	String qs = request.getQueryString();
							// 	response.sendRedirect(request.getContextPath()+"/_svc"+request.getPathInfo()+(qs==null?"":"?"+qs));
				        }
				});
				try {
					holder.setServlet(servletClass.getConstructor().newInstance());
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
				// For Variant B:
				// contextHandler.addServlet(holder, "/_svc/*");
			}
			else {				
				// just adding a servletClass instance...
				try {
					holder.setServlet(servletClass.getConstructor().newInstance());
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
				contextHandler.addServlet(holder, "/*");
			}
			this.onStopped = onStopped;
	        return contextHandler;
		}
        
	}

	@Override
	public void stopService() {
		if (this.onStopped != null) {
			Runnable tmp = this.onStopped;
			this.onStopped = null;
			tmp.run();			
		}
	}

}
