package org.webappos.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.webappos.bridge.BridgeSocket;
import org.webappos.properties.AppProperties;
import org.webappos.properties.PropertiesManager;
import org.webappos.properties.EngineProperties;
import org.webappos.properties.ServiceProperties;
import org.webappos.webcaller.WebCaller;
import org.webappos.util.Ports;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import org.slf4j.*;

public class Gate {
	
	private static Logger logger =  LoggerFactory.getLogger(Gate.class);
	
	private static Server webServer = null;
	private static ContextHandlerCollection handlerColl = new ContextHandlerCollection();//new HandlerCollection(true); // mutable when running

	
	
	private static SslContextFactory sslContextFactory = null;
	synchronized public static void reloadCerts() {
		try {
			if (sslContextFactory == null) {		
				sslContextFactory = new SslContextFactory();
				logger.info("Loading certs from "+API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+CertBot4j.DOMAIN_KEY_FILE_NAME +" and "+API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+CertBot4j.DOMAIN_CHAIN_FILE_NAME);
				KeyStore keyStore = CertBot4j.PEMToKeyStore(API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+CertBot4j.DOMAIN_KEY_FILE_NAME, API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+CertBot4j.DOMAIN_CHAIN_FILE_NAME);
				sslContextFactory.setKeyStore(keyStore);
				
				sslContextFactory.setExcludeCipherSuites("SSL_RSA_WITH_DES_CBC_SHA",
		                "SSL_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA",
		                "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
		                "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
		                "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
		                "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");
				
			}
			else {		 
				logger.info("Re-loading certs from "+API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+CertBot4j.DOMAIN_KEY_FILE_NAME +" and "+API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+CertBot4j.DOMAIN_CHAIN_FILE_NAME);
				sslContextFactory.reload(newSslContextFactory -> { 
					try {
						sslContextFactory.setKeyStore(CertBot4j.PEMToKeyStore(API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+CertBot4j.DOMAIN_KEY_FILE_NAME, API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+CertBot4j.DOMAIN_CHAIN_FILE_NAME));
					} catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | IOException e) {
						logger.error("Could not reload certificates: "+e.getMessage());
					}
				});
			}
		} catch (Exception e) {
			logger.error("Could not initialize/reload certificates: "+e.getMessage());
		}
	}
	
	private static IAppAdapter getAppAdapter(String name) {
		try {
			return (IAppAdapter) Class.forName("org.webappos.adapters.app."+name+".AppAdapter").getConstructor().newInstance();
		} catch (Throwable e) {
			logger.error("Could not load app adapter `"+name+"'. Reason: "+e.getMessage());
			return null;
		}
	}

	private static IServiceAdapter getServiceAdapter(String name) {
		try {
			return (IServiceAdapter) Class.forName("org.webappos.adapters.service."+name+".ServiceAdapter").getConstructor().newInstance();
		} catch (Throwable e) {
			logger.error("Could not load service adapter `"+name+"'. Reason: "+e.getMessage());
			return null;
		}
	}
	
	private static void serviceHalted(String name) {
		System.err.println("GATE: service "+name+" halted.");
	}
	
	private static void serviceStopped(String name) {
		System.err.println("GATE: service "+name+" stopped.");		
	}
	
	private static Runnable getOnHalted(String name) {
		return new Runnable() {

			@Override
			public void run() {
				serviceHalted(name);
			}
			
		};
	}
	
	private static Runnable getOnStopped(String name) {
		return new Runnable() {

			@Override
			public void run() {
				serviceStopped(name);
			}
			
		};
	}

	synchronized public static void attachWebAppOrService(String name, String appDir) { // reads app/service config, adds sub-domain and sub-path for the given app/service
		if (name==null) {
			logger.error("Could not attach null app/service.");
			return;
		}
		boolean isApp = name.endsWith(".app");
		if (!isApp) {
			if (!name.endsWith(".service")) {
				logger.error("Could not attach "+name+" since it is neither an app, nor a service.");
				return;
			}
		}
		
		
		// adding .webcalls functions provided by this app or service
		File dir = new File(API.config.APPS_DIR + File.separator +name);
		if (dir.isDirectory())
			for (File f : dir.listFiles()) if (f.getName().endsWith(WebCaller.FILE_EXTENSION)) {
				APIForServerBridge.webCallerForServerBridge.loadWebCalls(dir.getAbsolutePath() + File.separator + f.getName());
			}
		
		
		
		if (isApp) {	
			// app
			AppProperties appProps = APIForServerBridge.propertiesManagerForServerBridge.loadAppProperties(name, appDir);
			if (appProps == null) {
				logger.error("Could not load app "+name+" from "+appDir);
				API.status.setStatus("apps/"+name, "ERROR:Could not load app properties.");
				return;
			}
			// adding .webcalls functions provided by this app
			APIForServerBridge.webCallerForServerBridge.loadWebCalls(appProps);
			
			IAppAdapter appAdapter = getAppAdapter(appProps.app_type);
			if (appAdapter == null) {
				logger.error("Could not load app "+name+": unknown app type "+appProps.app_type);
				API.status.setStatus("apps/"+name, "ERROR:Unknown app type "+appProps.app_type+".");
				return;
			}

			
			// loading web calls of required engines
			for (int i=0; i<appProps.requires_engines.length; i++) {
				EngineProperties props = APIForServerBridge.propertiesManagerForServerBridge.loadEngineProperties(appProps.requires_engines[i]);
				if (props!=null)
					APIForServerBridge.webCallerForServerBridge.loadWebCalls(props);
			}
			

			if (API.config.hasOnlyIP) {
				// only IP specified
				
				if (appProps.requires_root_url_paths) {
					// we don't have a domain, only IP, thus we need to launch the app at the specific port...
					int i=0;
					while ((i<10) && (Ports.portTaken(APIForServerBridge.configForServerBridge.free_port+i)) && (Ports.portTaken(APIForServerBridge.configForServerBridge.free_port+i+1))) {
						i++;
					}
					
					if (i>=10) {
						logger.error("Could not find a suitable port "+APIForServerBridge.configForServerBridge.free_port+"+i for "+name);
						API.status.setStatus("apps/"+name, "ERROR:Could not find free port.");
						return;
					}
					APIForServerBridge.configForServerBridge.free_port+=i;
					
			        Server portServer = new Server();
			        
					HttpConfiguration http_config = new HttpConfiguration();
			        http_config.setSecureScheme("https");
			        http_config.setSecurePort(APIForServerBridge.configForServerBridge.free_port+1);
			        http_config.setOutputBufferSize(32768);
			        http_config.setRequestHeaderSize(8192);
			        http_config.setResponseHeaderSize(8192);
			        http_config.setSendServerVersion(true);
			        http_config.setSendDateHeader(false);
			        
			        ServerConnector http = new ServerConnector(portServer,
			                new HttpConnectionFactory(http_config));
			        http.setPort(APIForServerBridge.configForServerBridge.free_port);
			        http.setIdleTimeout(30000);
			        
			        portServer.addConnector(http);
			        
			        if (API.config.secure) {
						HttpConfiguration https_config = new HttpConfiguration(http_config);
					    https_config.addCustomizer(new SecureRequestCustomizer());
						
						ServerConnector sslConnector = new ServerConnector(portServer,
					            new SslConnectionFactory(sslContextFactory,HttpVersion.HTTP_1_1.asString()),
					            new HttpConnectionFactory(https_config));
					    sslConnector.setPort(APIForServerBridge.configForServerBridge.free_port+1);
					    portServer.addConnector(sslConnector);			        	
			        }
					
					ContextHandler appContextHandler = new ContextHandler("/");
					// divContextHandler.setVirtualHosts - not required for port-based apps
					ContextHandler appContext;
					try {
						 appContext = appAdapter.attachApp(appProps);
					}
					catch(Throwable t) {
						logger.error("Could not attach "+appProps.app_full_name+". "+t.getMessage());
						API.status.setStatus("apps/"+name, "ERROR:Could not attach app context.");
						return;
					}
					appContextHandler.setHandler(appContext);

					portServer.setHandler(appContextHandler);
					try {
						portServer.start();
						logger.info("Attached "+appProps.app_full_name+" at HTTP port "+APIForServerBridge.configForServerBridge.free_port);
						if (API.config.secure) {
							logger.info("Attached "+appProps.app_full_name+" at HTTPS port "+(APIForServerBridge.configForServerBridge.free_port+1));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
	
	
					// redirect to port...
					
					ServletContextHandler portRedirectHandler =  new ServletContextHandler(handlerColl, "/apps/"+appProps.app_url_name, false, false);
					ServletHolder holder2 = new ServletHolder();
					holder2.setServlet(new RedirectServlet(API.config.simple_domain_or_ip, APIForServerBridge.configForServerBridge.free_port, APIForServerBridge.configForServerBridge.free_port+1)); // w/o protocol
			        portRedirectHandler.addServlet(holder2, "/*");
					try {
						portRedirectHandler.start();
						logger.info("Attached "+appProps.app_full_name+" redirect from /apps/"+appProps.app_url_name+" to "+API.config.simple_domain_or_ip+":"+APIForServerBridge.configForServerBridge.free_port+(API.config.secure?"["+(APIForServerBridge.configForServerBridge.free_port+1)+"]":""));
						API.status.setStatus("apps/"+name,"/apps/"+appProps.app_url_name+" -> "+API.config.simple_domain_or_ip+":"+APIForServerBridge.configForServerBridge.free_port+(API.config.secure?"["+(APIForServerBridge.configForServerBridge.free_port+1)+"]":""));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					APIForServerBridge.configForServerBridge.free_port++;
					if (API.config.secure)
						APIForServerBridge.configForServerBridge.free_port++;
					
				}			
				else {
					// only IP specified, but the app does not require root paths, thus, sub-domain is not necessary...
					ContextHandler appContext;
					try {
						 appContext = appAdapter.attachApp(appProps);
					}
					catch(Throwable t) {
						logger.error("Could not attach "+appProps.app_full_name+". "+t.getMessage());
						API.status.setStatus("apps/"+name, "ERROR:Could not attach app context.");
						return;
					}
					ContextHandler appContextHandler = new ContextHandler("/apps/"+appProps.app_url_name);				
					appContextHandler.setHandler(appContext);
					handlerColl.addHandler(appContextHandler);
					try {
						appContextHandler.start();
						logger.info("Attached "+appProps.app_full_name+" at /apps/"+appProps.app_url_name);
						API.status.setStatus("apps/"+name, "/apps/"+appProps.app_url_name);
					} catch (Exception e) {
						e.printStackTrace();
					}								
				}													
			}
			else {
				// domain specified, all OK
				ContextHandler appContextHandler = new ContextHandler("/");
				
				appContextHandler.setVirtualHosts(new String[]{appProps.app_url_name+"."+API.config.simple_domain_or_ip, appProps.app_url_name+".localhost", "*."+appProps.app_url_name+"."+API.config.simple_domain_or_ip, "*."+appProps.app_url_name+".localhost"});
				
				ContextHandler appContext;
				try {
					 appContext = appAdapter.attachApp(appProps);
				}
				catch(Throwable t) {
					logger.error("Could not attach "+appProps.app_full_name+". "+t.getMessage());
					API.status.setStatus("apps/"+name, "ERROR:Could not attach app context.");
					return;
				}
				appContextHandler.setHandler(appContext);
				
				
				handlerColl.addHandler(appContextHandler);
				try {
					appContextHandler.start();
					logger.info("Attached "+appProps.app_full_name+" at "+appProps.app_url_name+"."+API.config.simple_domain_or_ip+" and "+appProps.app_url_name+".localhost");
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		
				// /apps/urlName redirect
				
				ServletContextHandler appRedirectHandler = new ServletContextHandler(handlerColl, "/apps/"+appProps.app_url_name, false, false);
				ServletHolder holder = new ServletHolder();
				holder.setServlet(new RedirectServlet(appProps.app_url_name+"."+API.config.simple_domain_or_ip, -1, -1));
		        appRedirectHandler.addServlet(holder, "/*");
				try {
					appRedirectHandler.start();
					logger.info("Attached "+appProps.app_full_name+" redirect from /apps/"+appProps.app_url_name+" to "+appProps.app_url_name+"."+API.config.simple_domain_or_ip);
					API.status.setStatus("apps/"+name, "/apps/"+appProps.app_url_name+"->"+appProps.app_url_name+"."+API.config.simple_domain_or_ip);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		} // if isApp
		else {			
			// service
			ServiceProperties svcProps = APIForServerBridge.propertiesManagerForServerBridge.loadServiceProperties(name, appDir);
			if (svcProps == null) {
				logger.error("Could not load service "+name+" from "+appDir);
				API.status.setStatus("apps/"+name, "ERROR:Could not load properties.");
				return;
			}
			// adding .webcalls functions provided by this service
			APIForServerBridge.webCallerForServerBridge.loadWebCalls(svcProps);
			
			JsonElement startup_type = API.registry.getValue("apps/"+name+"/startup_type");
			if (startup_type==null) {
				API.registry.setValue("apps/"+name+"/startup_type", "auto");
				startup_type = new JsonPrimitive("auto");
			}
			
			if (!"auto".equalsIgnoreCase(startup_type.getAsString())) {
				API.status.setStatus("apps/"+name, "ERROR:Could not attach app context.");
				return;
			}
			
			IServiceAdapter svcAdapter = getServiceAdapter(svcProps.service_adapter);
			if (svcAdapter == null) {				
				logger.error("Could not load serivice "+name+": unknown service adapter "+svcProps.service_adapter);
				return;
			}
			
			if (API.config.hasOnlyIP || svcProps.requires_http_port || svcProps.requires_https_port) {
				// only IP specified or the service requires a port explicitly
				
				if (svcProps.requires_root_url_paths || svcProps.requires_http_port || svcProps.requires_https_port) {
					// we don't have a domain, only IP, thus we need to launch the service at the specific port...
					// (or the service requires a port explicitly)
					int i=0;
					while ((i<10) && (Ports.portTaken(APIForServerBridge.configForServerBridge.free_port+i)) && (Ports.portTaken(APIForServerBridge.configForServerBridge.free_port+i+1))) {
						i++;
					}
					
					if (i>=10) {
						logger.error("Could not find a suitable port "+APIForServerBridge.configForServerBridge.free_port+"+i for "+name);
						return;
					}
					APIForServerBridge.configForServerBridge.free_port+=i;
					
					if (svcProps.requires_http_port || svcProps.requires_https_port) {
						svcProps.properties.setProperty("http_port", APIForServerBridge.configForServerBridge.free_port+"");
						svcProps.properties.setProperty("https_port", (APIForServerBridge.configForServerBridge.free_port+1)+"");
					}
			        
			        ContextHandler handler = null;
			        try {
			        	handler = svcAdapter.attachService(svcProps, "/", getOnStopped(svcProps.service_full_name), getOnHalted(svcProps.service_full_name));
			        }
			        catch(Throwable t) {
						logger.error("Could not attach "+svcProps.service_full_name+". "+t.getMessage());
						return;
			        }

			        if (handler == null) {
			        	// svcAdapter launched the server by its own...; it can use free_port and/or free_port+1
			        }
			        else {
						// handler != null; attaching...						
						Server portServer = null;
				        portServer = new Server();
						HttpConfiguration http_config = new HttpConfiguration();
				        http_config.setSecureScheme("https");
				        http_config.setSecurePort(APIForServerBridge.configForServerBridge.free_port+1);
				        http_config.setOutputBufferSize(32768);
				        http_config.setRequestHeaderSize(8192);
				        http_config.setResponseHeaderSize(8192);
				        http_config.setSendServerVersion(true);
				        http_config.setSendDateHeader(false);
				        
				        ServerConnector http = new ServerConnector(portServer,
				                new HttpConnectionFactory(http_config));
				        http.setPort(APIForServerBridge.configForServerBridge.free_port);
				        http.setIdleTimeout(30000);
				        
				        portServer.addConnector(http);
				        
				        if (API.config.secure) {
							HttpConfiguration https_config = new HttpConfiguration(http_config);
						    https_config.addCustomizer(new SecureRequestCustomizer());
							
							ServerConnector sslConnector = new ServerConnector(portServer,
						            new SslConnectionFactory(sslContextFactory,HttpVersion.HTTP_1_1.asString()),
						            new HttpConnectionFactory(https_config));
						    sslConnector.setPort(APIForServerBridge.configForServerBridge.free_port+1);
						    portServer.addConnector(sslConnector);			        	
				        }
			        	
						portServer.setHandler(handler);
						try {
							portServer.start();
							logger.info("Attached "+svcProps.service_full_name+" at HTTP port "+APIForServerBridge.configForServerBridge.free_port);
							if (API.config.secure) {
								logger.info("Attached "+svcProps.service_full_name+" at HTTPS port "+(APIForServerBridge.configForServerBridge.free_port+1));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
			        }
		
					// redirect to port...
					boolean securePortExists = svcProps.requires_https_port || API.config.secure;
							
					if (!API.config.hasOnlyIP) {
						// domain specified; attaching subdomain and a proxy from /services/urlName...
						
						
						
						if (svcProps.requires_root_url_paths) {
							// create subdomain with proxy servlet...
							ServletContextHandler subdomainProxyHandler =						
									new ServletContextHandler(handlerColl, "/", ServletContextHandler.SESSIONS);
							
							ServletHolder proxyServlet = new ServletHolder(ProxyServlet.Transparent.class);
							
							subdomainProxyHandler.setVirtualHosts(new String[]{svcProps.service_url_name+"_service."+API.config.simple_domain_or_ip, svcProps.service_url_name+"_service.localhost", "*."+svcProps.service_url_name+"_service."+API.config.simple_domain_or_ip, "*."+svcProps.service_url_name+"_service.localhost"});
														
							if (svcProps.requires_https_port) {
								proxyServlet.setInitParameter("proxyTo", "https://localhost:"+(APIForServerBridge.configForServerBridge.free_port+1)+"/");
							//	subdomainProxyHandler.setInitParameter("proxyTo", "https://localhost:"+(APIForServerBridge.configForServerBridge.free_port+1)+"/");
							}
							else {
								proxyServlet.setInitParameter("proxyTo", "http://localhost:"+(APIForServerBridge.configForServerBridge.free_port)+"/");
							//	subdomainProxyHandler.setInitParameter("proxyTo", "http://localhost:"+(APIForServerBridge.configForServerBridge.free_port)+"/");
							}
							proxyServlet.setInitParameter("Prefix", "/");
					        subdomainProxyHandler.addServlet(proxyServlet, "/*");
							try {
								subdomainProxyHandler.start();
								logger.info("Attached "+svcProps.service_full_name+" proxy redirect from "+svcProps.service_url_name+"_service."+API.config.simple_domain_or_ip+" to localhost:"+APIForServerBridge.configForServerBridge.free_port+(API.config.secure?"["+(APIForServerBridge.configForServerBridge.free_port+1)+"]":""));
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							// ...and redirect to that subdomain...							
							ServletContextHandler svcRedirectHandler = new ServletContextHandler(handlerColl, "/services/"+svcProps.service_url_name, false, false);
							
							svcRedirectHandler.setVirtualHosts(new String[] { API.config.simple_domain_or_ip, "localhost", "*."+API.config.simple_domain_or_ip, "*.localhost" });
								// "*." means that we allow to call this service from all subdomains, e.g., login app from its subdomain login.domain.org can access /services/login
							
							ServletHolder holder = new ServletHolder();
							holder.setServlet(new RedirectServlet(svcProps.service_url_name+"_service."+API.config.simple_domain_or_ip, -1, -1));
					        svcRedirectHandler.addServlet(holder, "/*");
							try {
								svcRedirectHandler.start();
								logger.info("Attached "+svcProps.service_full_name+" redirect from /services/"+svcProps.service_url_name+" to "+svcProps.service_url_name+"_service."+API.config.simple_domain_or_ip);
							} catch (Exception e) {
								e.printStackTrace();
							}						
							
						}
						else {
							ServletContextHandler portProxyHandler =  //new ServletContextHandler(handlerColl, "/services/"+svcProps.service_url_name, true, false);						
									new ServletContextHandler(handlerColl, "/services/"+svcProps.service_url_name, ServletContextHandler.SESSIONS);
							
							ServletHolder proxyServlet = new ServletHolder(ProxyServlet.Transparent.class);
							
							if (svcProps.requires_https_port) {
								proxyServlet.setInitParameter("proxyTo", "https://localhost:"+(APIForServerBridge.configForServerBridge.free_port+1)+"/");
							//	portProxyHandler.setInitParameter("proxyTo", "https://localhost:"+(APIForServerBridge.configForServerBridge.free_port+1)+"/");
							}
							else {
								proxyServlet.setInitParameter("proxyTo", "http://localhost:"+(APIForServerBridge.configForServerBridge.free_port)+"/");
							//	portProxyHandler.setInitParameter("proxyTo", "http://localhost:"+(APIForServerBridge.configForServerBridge.free_port)+"/");
							}
							proxyServlet.setInitParameter("Prefix", "/");
					        portProxyHandler.addServlet(proxyServlet, "/*");
							try {
								portProxyHandler.start();
								logger.info("Attached "+svcProps.service_full_name+" proxy redirect from /services/"+svcProps.service_url_name+" to localhost:"+APIForServerBridge.configForServerBridge.free_port+(API.config.secure?"["+(APIForServerBridge.configForServerBridge.free_port+1)+"]":""));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						
					}
					else {
					
						ServletContextHandler portRedirectHandler =  new ServletContextHandler(handlerColl, "/services/"+svcProps.service_url_name, false, false);
						ServletHolder holder2 = new ServletHolder();
						if (securePortExists)
							holder2.setServlet(new RedirectServlet(API.config.simple_domain_or_ip, APIForServerBridge.configForServerBridge.free_port, APIForServerBridge.configForServerBridge.free_port+1));
						else
							holder2.setServlet(new RedirectServlet(API.config.simple_domain_or_ip, APIForServerBridge.configForServerBridge.free_port));
				        portRedirectHandler.addServlet(holder2, "/*");
						try {
							portRedirectHandler.start();
							logger.info("Attached "+svcProps.service_full_name+" simple redirect from /services/"+svcProps.service_url_name+" to "+API.config.simple_domain_or_ip+":"+APIForServerBridge.configForServerBridge.free_port+(API.config.secure?"["+(APIForServerBridge.configForServerBridge.free_port+1)+"]":""));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					
					APIForServerBridge.configForServerBridge.free_port++;
					if (securePortExists)
						APIForServerBridge.configForServerBridge.free_port++;
					
				}			
				else {
					// only IP specified, but the service does not require root paths, thus, sub-domain is not needed...
			        ContextHandler handler;
			        try {
			        	handler = svcAdapter.attachService(svcProps, "/services/"+svcProps.service_url_name, getOnStopped(svcProps.service_full_name), getOnHalted(svcProps.service_full_name));
			        }
			        catch(Throwable t) {
						logger.error("Could not attach "+svcProps.service_full_name+". "+t.getMessage());
						return;
			        }
					
					handlerColl.addHandler(handler);
					try {
						handler.start();
						logger.info("Attached "+svcProps.service_full_name+" at /services/"+svcProps.service_url_name);
					} catch (Exception e) {
						logger.error("Could not start handler for "+svcProps.service_full_name+". "+e.getMessage());
					}								
				}													
			}
			else {
				// domain specified, all OK
				
		        ContextHandler handler;
		        try {
		        	handler = svcAdapter.attachService(svcProps, "/", getOnStopped(svcProps.service_full_name), getOnHalted(svcProps.service_full_name));
		        }
		        catch(Throwable t) {
					logger.error("Could not attach "+svcProps.service_full_name+". "+t.getMessage());
					t.printStackTrace();
					return;
		        }
				handler.setVirtualHosts(new String[]{"*."+svcProps.service_url_name+"_service."+API.config.simple_domain_or_ip, "*."+svcProps.service_url_name+"_service.localhost"});
				handlerColl.addHandler(handler);
				try {
					handler.start();
					logger.info("Attached "+svcProps.service_full_name+" at "+svcProps.service_url_name+"_service."+API.config.simple_domain_or_ip+" and "+svcProps.service_url_name+"_service.localhost");
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		
				
				if (svcProps.requires_root_url_paths) {
					// /apps/urlName redirect
					
					ServletContextHandler svcRedirectHandler = new ServletContextHandler(handlerColl, "/services/"+svcProps.service_url_name, false, false);
					
					svcRedirectHandler.setVirtualHosts(new String[] { "*."+API.config.simple_domain_or_ip, "*.localhost", "*."+API.config.simple_domain_or_ip });
						// "*." means that we allow to call this service from all subdomains, e.g., login app from its subdomain login.domain.org can access /services/login
					
					ServletHolder holder = new ServletHolder();
					holder.setServlet(new RedirectServlet(svcProps.service_url_name+"_service."+API.config.simple_domain_or_ip, -1, -1));
			        svcRedirectHandler.addServlet(holder, "/*");
					try {
						svcRedirectHandler.start();
						logger.info("Attached "+svcProps.service_full_name+" redirect from /services/"+svcProps.service_url_name+" to "+svcProps.service_url_name+"_service."+API.config.simple_domain_or_ip);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					ContextHandler handler2 = svcAdapter.attachService(svcProps, "/services/"+svcProps.service_url_name, getOnStopped(svcProps.service_full_name), getOnHalted(svcProps.service_full_name));					
					handler2.setVirtualHosts(new String[] { API.config.simple_domain_or_ip, "localhost", "*."+API.config.simple_domain_or_ip });
					handlerColl.addHandler(handler2);
					
					try {
						handler2.start();
						logger.info("Attached non-root "+svcProps.service_full_name+" handler copy at /services/"+svcProps.service_url_name);
					} catch (Exception e) {
						e.printStackTrace();
					}
/*
					try {
					webServer.stop();
					webServer.setHandler(handler2);
					webServer.start();
					}
					catch(Throwable t) {
						t.printStackTrace();
					}*/
					
					
				}
			}
			
		}
	}

	synchronized public static void detachWebAppOrService(String name) {
		
	}

	public static void main(String[] args) {
		
		API.initAPI();
		
		// start server...
		
		
		int i=0;
		while ((i<10) && (Ports.portTaken(API.config.port+i))) {
			i++;
		}
		
		if (i>=10) {
			logger.error("Could not find a suitable port "+API.config.port+"+i.");
			return;
		}
			
		API.config.port += i;

		i=0;
		while ((i<10) && ((API.config.port==API.config.secure_port+i) || (Ports.portTaken(API.config.secure_port+i)))) {
			i++;
		}
		
		if (i>=10) {
			logger.error("Could not find a suitable secure port "+API.config.secure_port+"+i.");
			return;
		}
		API.config.secure_port += i;
		
		try {
			webServer = new Server();

			///// HTTP /////
			
			HttpConfiguration http_config = new HttpConfiguration();
	        http_config.setSecureScheme("https");
	        http_config.setSecurePort(API.config.secure_port);
	        http_config.setOutputBufferSize(32768);
	        http_config.setRequestHeaderSize(8192);
	        http_config.setResponseHeaderSize(8192);
	        http_config.setSendServerVersion(true);
	        http_config.setSendDateHeader(false);
	        
	        ServerConnector http = new ServerConnector(webServer,
	                new HttpConnectionFactory(http_config));
	        http.setPort(API.config.port);
	        http.setIdleTimeout(30000);
	        
	        webServer.addConnector(http);
	        logger.info("PORT is "+API.config.port);
	        API.status.setStatus("server/port", API.config.port+"");
	        

			
			webServer.setHandler(handlerColl);			
//			webServer.start();
			
			// MAIN (FIRST-LEVEL DOMAIN) context
			
			WebAppContext mainContext = new WebAppContext();
			mainContext.setWar(API.config.WEB_ROOT_DIR);
			mainContext.setContextPath("/");		
			
			File dir = new File(ConfigStatic.WEB_ROOT_CACHE_DIR);
			if (!dir.exists())
				dir.mkdirs();
			
	        // web-root + acme-challenge web-root...	       	       
			ResourceCollection mainResources = new ResourceCollection(new String[] {
				API.config.WEB_ROOT_DIR, // must always present, then other folders in the search path follow
				API.config.ETC_DIR+File.separator+"acme"+File.separator+"web-root",
				API.config.WEB_ROOT_CACHE_DIR,
			});
			mainContext.setBaseResource(mainResources);
						
			// NO CACHE
			ServletHolder holder = new ServletHolder(new DefaultServlet());
			holder.setInitParameter("useFileMappedBuffer", "false");
			holder.setInitParameter("cacheControl", "max-age=0, public");
			mainContext.addServlet(holder, "/");
			
			
			ContextHandler mainContextHandler;
			mainContextHandler = new ContextHandler("/");

			if (API.config.hasOnlyIP) { 
				mainContextHandler.setVirtualHosts(new String[]{"localhost"});
			}
			else {
				mainContextHandler.setVirtualHosts(new String[]{API.config.simple_domain_or_ip, "localhost"});
			}
			
			//mainContextHandler.setHandler(mainContext);
			//handlerColl.addHandler(mainContextHandler);
			handlerColl.addHandler(mainContext);
			//mainContextHandler.start();
			//mainContext.start();
			
			webServer.start();
		
			
			///// HTTPS /////
	        
			if (API.config.secure) {				
				while (!webServer.isStarted()) {
					Thread.sleep(100);
				}
		        CertBot4j.ensureCertificates(API.config.acme_url, API.config.simple_domain_or_ip, API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs", API.config.ETC_DIR+File.separator+"acme"+File.separator+"web-root", API.config.acme_renew_interval, new Runnable() {
		
					@Override
					public void run() {
						// Just renewed certs...
						reloadCerts();
						
						String os = System.getProperty("os.name");
						File f;
						if ((os!=null) && (os.indexOf("Windows")>=0))
							f = new File(API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+"onrenew.bat");
						else
							f = new File(API.config.ETC_DIR+File.separator+"acme"+File.separator+"certs"+File.separator+"onrenew.sh");
						if (f.exists()) {
							logger.info("Executing "+f.getAbsolutePath()+"...");
							try {
								Process p = Runtime.getRuntime().exec(f.getAbsolutePath());
								p.waitFor();
								logger.info("Finished "+f.getAbsolutePath());
							} catch (Throwable e) {
								logger.error("Error while executing "+f.getAbsolutePath());
							}
							
						}
						else
							logger.info("Script "+f.getAbsolutePath()+" not found, but that's fine unless you need to do external actions with new certificates.");
					}
		        	
		        });		

		        webServer.stop();
				while (!webServer.isStopped()) {
					Thread.sleep(100);
				}				
				reloadCerts(); // initializes sslContextFactory
								
				HttpConfiguration https_config = new HttpConfiguration(http_config);
			    https_config.addCustomizer(new SecureRequestCustomizer());
				
				ServerConnector sslConnector = new ServerConnector(webServer,
			            new SslConnectionFactory(sslContextFactory,HttpVersion.HTTP_1_1.asString()),
			            new HttpConnectionFactory(https_config));
			    sslConnector.setPort(API.config.secure_port);
			    webServer.addConnector(sslConnector);
				
		        
				logger.info("SECURE_PORT is "+API.config.secure_port);
				API.status.setStatus("server/secure_port", API.config.secure_port+"");
		        webServer.start();
			}
			
			
			// for all not-installing apps and services: attachWebAppOrService; services first (since, e.g., login app requires login service)
			for (File f : new File(API.config.APPS_DIR).listFiles()) {
				if (f.isDirectory() && f.getName().endsWith(".service")) {
					attachWebAppOrService(f.getName(), f.getAbsolutePath());
				}
			}
	        
			for (File f : new File(API.config.APPS_DIR).listFiles()) {
				if (f.isDirectory() && f.getName().endsWith(".app")) {
					attachWebAppOrService(f.getName(), f.getAbsolutePath());
				}
			}

			// ws://domain:port/ws/
			
	        ServletContextHandler wsContextHandler = new ServletContextHandler(handlerColl, "/ws", true, true); // First Server!!!
	        			        
	        
	        ServletHolder holderEvents = new ServletHolder("ws-events", BridgeSocket.Servlet.class);
	        wsContextHandler.addServlet(holderEvents, "/*");  // the path will be ws://domain.org/ws/ or wss://domain.org/ws/  - the trailing slash is mandatory!	       	        	        
	        
	        wsContextHandler.start();

			
		} catch (Throwable e) {
			e.printStackTrace();
			return;
		}
		

	}

}
