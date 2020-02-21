package org.webappos.webcaller;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.properties.SomeProperties;
import org.webappos.properties.WebAppProperties;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webmem.IWebMemory;
import org.webappos.webmem.WebMemoryArea;
import org.webappos.webproc.WebProcessorBusService;

import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;

/**
 * The main class for executing web calls from the server side.
 * Normally, the enqueue function (from the IWebCaller interface) is used for that.
 * 
 * Uses internally: TDAKernel.getSynchronizer(), RAAPI_Synchronizer. 
 * 
 * @author Sergejs Kozlovics
 *
 */
public class WebCaller extends UnicastRemoteObject implements IWebCaller, IRWebCaller {
	private static final long serialVersionUID = 1L;
	private static Logger logger =  LoggerFactory.getLogger(WebCaller.class);
	
	public static final String AUTO_LOAD_DIR = ConfigStatic.ETC_DIR+File.separator+"webcalls";
	public static final String FILE_EXTENSION = ".webcalls";
	
	public WebCaller() throws RemoteException {
		super();
		
		File dir = new File(AUTO_LOAD_DIR);
		if (dir.isDirectory())
			for (File f : dir.listFiles()) if (f.getName().endsWith(FILE_EXTENSION)) {
				loadWebCalls(dir.getAbsolutePath() + File.separator + f.getName(), null, "etc/webcalls");
			}

	}
	
	
	private final Map<String, WebCallDeclaration> map = new HashMap<String, WebCallDeclaration>();
	private long clientCallNo = 1; // will be increased...
	
	
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	
	synchronized public Map<String, WebCallDeclaration> getWebCalls(String fullAppName) {
		return Collections.unmodifiableMap(map);
	}
	
	/**
	 * A class used only by the server-side bridge to store information for web calls
	 * not originated at the server side. In certain cases, web calls originated at the server side
	 * can also be repackaged as SyncedWebCallSeed-s for technical reasons. 
	 */
	public static class SyncedWebCallSeed extends WebCallSeed { // used only from the bridge		
		private static final long serialVersionUID = 1L;
		public RAAPI_Synchronizer singleSynchronizer = null; 
			// if null, use multi synchronizer from kernel;
			// otherwise, either singleSynchronizer or multi synchronizer from the kernel is used
			// depending on whether the action is single
		public SyncedWebCallSeed() {
			super();
		}
		public SyncedWebCallSeed(WebCallSeed seed) {
			this.actionName = seed.actionName;
			this.callingConventions = seed.callingConventions;
			this.jsonArgument = seed.jsonArgument;
			this.jsonResult = seed.jsonResult;
			this.webmemArgument = seed.webmemArgument;
			this.login = seed.login;
			this.project_id = seed.project_id;
			this.fullAppName = seed.fullAppName;
			this.timeToLive = seed.timeToLive;
	  		if (seed instanceof SyncedWebCallSeed)
	  			this.singleSynchronizer = ((SyncedWebCallSeed) seed).singleSynchronizer;
		}
	}
	
	
	/**
	 * p2q maps each project_id to a queue of web calls
	 */
	private Map<String, Queue<WebCallSeed> > p2q = new HashMap<String, Queue<WebCallSeed> >();	
	
	@Override
	synchronized public int getQueueSize(String project_id) {
		if (project_id==null)
			return 0;
		Queue<WebCallSeed> q = p2q.get(project_id);
		if (q==null)
			return 0;
		else
			return q.size();
	}
	
	@Override
	synchronized public void enqueue(WebCallSeed _seed) {
		assert API.wpbService instanceof WebProcessorBusService;
		
		if (_seed==null)
			return;
		
		if (_seed.fullAppName == null) {
			_seed.fullAppName = API.dataMemory.getProjectFullAppName(_seed.project_id);
		}
		
		// patches for singleSynchronizer
		if (!(_seed instanceof SyncedWebCallSeed) || (((SyncedWebCallSeed)_seed).singleSynchronizer==null)) {
			if (API.dataMemory instanceof WebMemoryArea) {
				RAAPI_Synchronizer sync = ((WebMemoryArea)API.dataMemory).getSingleSynchronizer(_seed.project_id);
				if (sync!=null) {
					if (_seed instanceof SyncedWebCallSeed)
						((SyncedWebCallSeed)_seed).singleSynchronizer = sync;
					else {
						_seed = new SyncedWebCallSeed(_seed);
						((SyncedWebCallSeed)_seed).singleSynchronizer = sync;
					}
				}
			}
		}
		
		final WebCallSeed seed1 = _seed;
		
		//System.out.println("WebCaller enqueue "+seed1.actionName+" ("+seed1.hashCode()+")");
		logger.debug("WebCaller enqueue "+seed1.actionName+" ("+seed1.hashCode()+")");
		String id = seed1.project_id!=null?seed1.project_id:"";
		Queue<WebCallSeed> q = p2q.get(id);
		if (q != null) {
			q.add(seed1); 
			// qq scheduled earlier
		}
		else {
			q = new LinkedList<WebCallSeed>();
			q.add(seed1);
			p2q.put(id, q);
			
			// schedule qq...			
			scheduler.schedule(new Runnable() {

				@Override
				public void run() {						
					String id = seed1.project_id!=null?seed1.project_id:"";
					
					WebCallSeed seed2 = null; 
					synchronized (WebCaller.this) {
						Queue<WebCallSeed> qq = p2q.get(id);
						if ((qq == null) || (qq.isEmpty())) {
							logger.error("Incorrect queue for "+id+".");
							return;
						}
						seed2 = qq.peek();
					}
					
					boolean tryAgain = false;
					
					try {						
						logger.debug("WebCaller dequeue "+seed2.actionName+" ("+seed2.hashCode()+") app="+seed2.fullAppName+",action="+seed2.actionName+",synced="+(seed2 instanceof SyncedWebCallSeed)+",kernel="+API.dataMemory.getWebMemory(seed2.project_id)+",arg="+seed2.webmemArgument);
						WebCallDeclaration action = map.get(seed2.actionName);
						if (action == null && seed2.fullAppName!=null && API.config.allow_undeclared_webcalls) {
							// Lua patch...
							int i = seed2.actionName.indexOf(':');
							if (i>=0) {
								WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(seed2.fullAppName);
								if (props!=null) {
									action = new WebCallDeclaration(seed2.actionName, props.app_dir);
									action.callingConventions = CallingConventions.WEBMEMCALL;
									API.status.setValue("webcalls/undeclared/"+seed2.actionName, action.toString());
									API.status.setValue("webcalls/undeclared/"+seed2.actionName+"/appName", seed2.fullAppName);
								}
							}
						}
						
						if (action == null) {
							logger.error("Action '"+seed2.actionName+"' not found. Ignoring.");
							return;
						}
						
						if ((seed2.fullAppName==null) && (action.pwd.endsWith(".webapp"))) {
							seed2.fullAppName = new File(action.pwd).getName();
						}
											
						if (action.isClient) {
							// forward to the client
							
							if (seed2.callingConventions != action.callingConventions) {
								logger.error("Count not peform client-side web call "+seed2.actionName+" since calling conventions do not match.");
								return;
							}
							
							if (seed2.callingConventions == CallingConventions.JSONCALL) {
								
								RAAPI_Synchronizer sync = null;
								if (action.isSingle) {
									if (seed2 instanceof SyncedWebCallSeed)
										sync = ((SyncedWebCallSeed) seed2).singleSynchronizer;
									else
										if (API.dataMemory instanceof WebMemoryArea)
											sync = ((WebMemoryArea)API.dataMemory).getSingleSynchronizer(seed2.project_id);
								}
								else {
									IWebMemory webmem = API.dataMemory.getWebMemory(seed2.project_id);
									if (webmem instanceof TDAKernel)
										sync = ((TDAKernel)webmem).getSynchronizer();
								}
								
								if (sync != null)	 {
									sync.flush();
									sync.syncRawAction(new double[] {0xC0, clientCallNo}, RAAPI_Synchronizer.sharpenString(seed2.actionName)+"/"+RAAPI_Synchronizer.sharpenString(seed2.jsonArgument));
								}
								else
									logger.error("Could not forward client-side JSONCALL web call "+seed2.actionName);
								
								if (seed2.jsonResult!=null) {
									if (sync == null)
										seed2.jsonResult.completeExceptionally(new RuntimeException("Client-side web call "+seed2.actionName+" could not be called because no appropriate (single) synchronizer was found."));
									else {
										// TODO LATER?:
										// 1. assign clientCallNo with the Runnable that will call jsonResult.resolve()
										// 2. provide a WebCaller function to call such Runnable, when the 0xC1 (jsoncall result action with the return value) is received 
										
										// Currently:
										seed2.jsonResult.completeExceptionally(new RuntimeException("Client-side web call "+seed2.actionName+" forwarded, but returning JSON results is not implemented."));
									}
								}
								
								if (clientCallNo == Long.MAX_VALUE)
									clientCallNo = 1;
								else
									clientCallNo++;
							}
							else {
								// webmemcall
								
								RAAPI_Synchronizer sync = null;
								if (action.isSingle) {
									if (seed2 instanceof SyncedWebCallSeed)
										sync = ((SyncedWebCallSeed) seed2).singleSynchronizer;
									else
										if (API.dataMemory instanceof WebMemoryArea)
											sync = ((WebMemoryArea)API.dataMemory).getSingleSynchronizer(seed2.project_id);
								}
								else {
									IWebMemory webmem = API.dataMemory.getWebMemory(seed2.project_id);
									if (webmem instanceof TDAKernel)
										sync = ((TDAKernel)webmem).getSynchronizer();
								}
								
								if (sync != null)						
									sync.syncRawAction(new double[] {0xC0, seed2.webmemArgument}, RAAPI_Synchronizer.sharpenString(seed2.actionName));
								else
									logger.error("Could not forward client-side webmemcall web call "+seed2.actionName);
								
								if (seed2.jsonResult!=null) {
									if (sync == null)
										seed2.jsonResult.completeExceptionally(new RuntimeException("Client-side web call "+seed2.actionName+" could not be called because no appropriate (single) synchronizer was found."));
									else {
										seed2.jsonResult.completeExceptionally(new RuntimeException("Client-side web call "+seed2.actionName+" forwarded. No return value is expected."));
									}
								}
							}
							
							return;
						}
						
						
							
						// execute webcall within some appropriate web processor (or make an inline call)...
						
						boolean submitted = false;
						try {
							submitted = ((WebProcessorBusService)API.wpbService).webCallToWebProcessor(seed2, action);
						}
						catch(Throwable t) {
						}
						
						if (!submitted) {
							seed2.timeToLive--;
							if (seed2.timeToLive>0) {
								tryAgain = true;
								logger.info("reschedule/enqueue "+seed2.actionName+" ("+seed2.hashCode()+") app="+seed2.fullAppName+",action="+seed2.actionName+",synced="+(seed2 instanceof SyncedWebCallSeed)+",kernel="+API.dataMemory.getWebMemory(seed2.project_id)+",ttl="+seed2.timeToLive);					
							}
							else {
								logger.info("seed time-to-live expired: "+seed2.actionName+" ("+seed2.hashCode()+") app="+seed2.fullAppName+",action="+seed2.actionName+",synced="+(seed2 instanceof SyncedWebCallSeed)+",kernel="+API.dataMemory.getWebMemory(seed2.project_id));
							}
						}
						
					}
					finally {
						synchronized (WebCaller.this) {
							Queue<WebCallSeed> q = p2q.get(id);
							if ((q == null) || (q.isEmpty())) {
								logger.error("Incorrect queue for "+id+".");
								return;
							}
							if (!tryAgain)
								q.remove();
							
							if (q.isEmpty()) {
								p2q.remove(id);
							}
							else {
								// reschedule...
								scheduler.schedule(this, tryAgain?1000:0, TimeUnit.MILLISECONDS);
							}
						}							
					}
									 
				}
				
			}, 0, TimeUnit.MILLISECONDS);
		}
		
		
 	}
	
	@Override
	synchronized public boolean invokeNow(WebCallSeed _seed) {
		if (_seed==null)
			return false;

		if (_seed.fullAppName == null) {
			_seed.fullAppName = API.dataMemory.getProjectFullAppName(_seed.project_id);
		}

		// patches for singleSynchronizer
		if (!(_seed instanceof SyncedWebCallSeed) || (((SyncedWebCallSeed)_seed).singleSynchronizer==null)) {
			if (API.dataMemory instanceof WebMemoryArea) {
				RAAPI_Synchronizer sync = ((WebMemoryArea)API.dataMemory).getSingleSynchronizer(_seed.project_id);
				if (sync!=null) {
					if (_seed instanceof SyncedWebCallSeed)
						((SyncedWebCallSeed)_seed).singleSynchronizer = sync;
					else {
						_seed = new SyncedWebCallSeed(_seed);
						((SyncedWebCallSeed)_seed).singleSynchronizer = sync;
					}
				}
			}
		}
		
		final WebCallSeed seed2 = _seed;
		String id = seed2.project_id!=null?seed2.project_id:"";
		

	
		logger.trace("WebCaller trying to invoke now "+seed2.actionName+" ("+seed2.hashCode()+") app="+seed2.fullAppName+",action="+seed2.actionName+",synced="+(seed2 instanceof SyncedWebCallSeed)+",kernel="+API.dataMemory.getWebMemory(seed2.project_id)+",arg="+seed2.webmemArgument);
		WebCallDeclaration action = map.get(seed2.actionName);
		if (action==null && seed2.fullAppName!=null) {
			// Lua patch...
			int i = seed2.actionName.indexOf(':');
			if (i>=0) {
				WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(seed2.fullAppName);
				action = new WebCallDeclaration(seed2.actionName, props.app_dir);
				action.callingConventions = CallingConventions.WEBMEMCALL;
			}
		}
		
		if (action == null) {
			logger.error("Action "+seed2.actionName+" not found. Ignoring.");
			return false;
		}

		if ((seed2.fullAppName==null) && (action.pwd.endsWith(".webapp"))) {
			seed2.fullAppName = new File(action.pwd).getName();
		}

		if (action.isClient) {
			// forward to the client
			
			if (seed2.callingConventions != action.callingConventions) {
				logger.error("Count not peform client-side web call "+seed2.actionName+" since calling conventions do not match.");
				return false;
			}
			
			if (seed2.callingConventions == CallingConventions.JSONCALL) {
				
				RAAPI_Synchronizer sync = null;
				if (action.isSingle) {
					if (seed2 instanceof SyncedWebCallSeed)
						sync = ((SyncedWebCallSeed) seed2).singleSynchronizer;
					else
						if (API.dataMemory instanceof WebMemoryArea)
							sync = ((WebMemoryArea)API.dataMemory).getSingleSynchronizer(seed2.project_id);
				}
				else {
					IWebMemory webmem = API.dataMemory.getWebMemory(seed2.project_id);
					if (webmem instanceof TDAKernel)
						sync = ((TDAKernel)webmem).getSynchronizer();
				}
				
				if (sync != null)	 {
					sync.flush();
					sync.syncRawAction(new double[] {0xC0, 0}, RAAPI_Synchronizer.sharpenString(seed2.actionName)+"/"+RAAPI_Synchronizer.sharpenString(seed2.jsonArgument));
				}
				else
					logger.error("Could not forward client-side JSONCALL web call "+seed2.actionName);
				
				if (seed2.jsonResult!=null) {
					if (sync == null)
						seed2.jsonResult.completeExceptionally(new RuntimeException("Client-side web call "+seed2.actionName+" could not be called because no appropriate (single) synchronizer was found."));
					else {
						
						// Currently:
						seed2.jsonResult.completeExceptionally(new RuntimeException("Client-side web call "+seed2.actionName+" forwarded, but returning JSON results is not implemented."));
					}
				}
				
			}
			else {
				// WEBMEMCALL
				
				RAAPI_Synchronizer sync = null;
				if (action.isSingle) {
					if (seed2 instanceof SyncedWebCallSeed)
						sync = ((SyncedWebCallSeed) seed2).singleSynchronizer;
					else
						if (API.dataMemory instanceof WebMemoryArea)
							sync = ((WebMemoryArea)API.dataMemory).getSingleSynchronizer(seed2.project_id);
				}
				else {
					IWebMemory webmem = API.dataMemory.getWebMemory(seed2.project_id);
					if (webmem instanceof TDAKernel)
						sync = ((TDAKernel)webmem).getSynchronizer();					
				}
				
				if (sync != null)						
					sync.syncRawAction(new double[] {0xC0, seed2.webmemArgument}, RAAPI_Synchronizer.sharpenString(seed2.actionName));
				else
					logger.error("Could not forward client-side WEBMEMCALL web call "+seed2.actionName);
				
				if (seed2.jsonResult!=null) {
					if (sync == null)
						seed2.jsonResult.completeExceptionally(new RuntimeException("Client-side web call "+seed2.actionName+" could not be called because no appropriate (single) synchronizer was found."));
					else {
						seed2.jsonResult.completeExceptionally(new RuntimeException("Client-side web call "+seed2.actionName+" forwarded. No return value is expected."));
					}
				}
			}
			
			return true;
		}
		
		// trying to invoke here...
		Class<?> adapterClass = null;
		Object adapter = null;
				
		try {
			adapterClass = Class.forName("org.webappos.adapters.webcalls."+action.resolvedInstructionSet+".WebCallsAdapter");					
			adapter = adapterClass.getConstructor().newInstance();
		}
		catch(Throwable t) {					
		}
		
		if (adapter == null) {
			logger.error("Could not invoke the web call now, since the corresponding web calls adapter is not found in the current web processor.");
			return false;
		}
		
		IWebMemory kernel = API.dataMemory.getWebMemory(seed2.project_id);
		String jsonResult = null;
										
		if ((seed2.callingConventions == CallingConventions.JSONCALL) && (adapter instanceof IJsonWebCallsAdapter)) {
			try {
				jsonResult = ((IJsonWebCallsAdapter)adapter).jsoncall(action.resolvedLocation, action.pwd, seed2.jsonArgument, seed2.project_id, seed2.fullAppName, seed2.login);
				if (seed2.jsonResult!=null)
					seed2.jsonResult.complete(jsonResult);
			}
			catch(Throwable t) {
				jsonResult = "{\"error\":\""+t.getMessage()+"\"}";
				if (seed2.jsonResult!=null)
					seed2.jsonResult.completeExceptionally(new RuntimeException(jsonResult));
			}
			return true;
		}
		else
		if ((seed2.callingConventions == CallingConventions.WEBMEMCALL) && (adapter instanceof IWebMemWebCallsAdapter)) {
			try {
				((IWebMemWebCallsAdapter)adapter).webmemcall(action.resolvedLocation, action.pwd, seed2.webmemArgument, kernel, seed2.project_id, seed2.fullAppName, seed2.login);
				if (seed2.jsonResult!=null)
					seed2.jsonResult.complete(jsonResult);
			}
			catch(Throwable t) {
				jsonResult = "{\"error\":\""+t.getMessage()+"\"}";
				if (seed2.jsonResult!=null)
					seed2.jsonResult.completeExceptionally(new RuntimeException(jsonResult));
			}
			return true;
		}
		else {
			logger.error("Could not peform server-side web call "+seed2.actionName+" within web processor `"+id+"'since calling conventions do not match. ");
			return false;
		}

	}


		
	/*synchronized public void loadWebCalls(String fileName) {
		loadWebCalls(fileName, null, "file");
	}*/
	
	private void loadWebCalls(String fileName, ArrayList<String> loaded, String whereDefined) {
		try {
			
			String pwd = new File(fileName).getParent();
			Properties p = new Properties();
			
			p.load(new FileInputStream(fileName));
			
			Set<String> set = p.stringPropertyNames();
			String[] a = new String[set.size()];
			set.toArray(a);
			Arrays.sort(a);
			
			for(String key:a) {
				String value = p.getProperty(key);
				WebCallDeclaration action = new WebCallDeclaration(value, pwd);
				int i = key.indexOf("public ");
				if (i>=0) {
					key = key.substring(0, i)+key.substring(i+7);
					action.isPublic = true;
				}
				i = key.indexOf("static ");
				if (i>=0) {
					key = key.substring(0, i)+key.substring(i+7);
					action.isStatic = true;
				}
				i = key.indexOf("inline ");
				if (i>=0) {
					key = key.substring(0, i)+key.substring(i+7);
					action.isInline = true;
				}
				i = key.indexOf("single ");
				if (i>=0) {
					key = key.substring(0, i)+key.substring(i+7);
					action.isSingle = true;
				}
				i = key.indexOf("webmemcall ");
				if (i>=0) {
					key = key.substring(0, i)+key.substring(i+11);
					action.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
				}
				/*i = key.indexOf("tdacall "); // legacy; deprecated
				if (i>=0) {
					key = key.substring(0, i)+key.substring(i+8);
					action.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
				}*/
				i = key.indexOf("jsoncall ");
				if (i>=0) {
					key = key.substring(0, i)+key.substring(i+9);
					action.callingConventions = WebCaller.CallingConventions.JSONCALL;
				}
				i = key.indexOf("(");
				if (i>=0) {
					key = key.substring(0, i); // deleting parameter description
				}
				key = key.trim();
				// remove other modifiers from the key
				i = key.lastIndexOf(' ');
				if (i>=0)
					key = key.substring(i+1);				
				
				if (key.isEmpty()) {
					logger.debug("empty web call ignored");
					continue;
				}

				// Validating the action:
				
				// isSingle implies isClient & webmemcall
				if (action.isSingle) {
					if (!action.isClient) {
						String reason = "web call "+value+" not added: it is single, but not client";
						logger.warn(reason);
						API.status.setValue("webcalls/ignored/"+key+"/reason", reason);
						continue;
					}
					if (action.callingConventions != CallingConventions.WEBMEMCALL) {
						String reason = "web call "+value+" not added: it is single, but does not conform to the 'webmemcall' calling conventions";
						logger.warn(reason);
						API.status.setValue("webcalls/ignored/"+key+"/reason", reason);
						continue;
					}
				}
				
				// webmemcall implies !isPublic && !isStatic
				if (action.callingConventions == CallingConventions.WEBMEMCALL)
					if (action.isPublic || action.isStatic) {
						String reason = "web call "+value+" not added: webmemcall calling conventions imply that the web call must be neither public, nor static";
						logger.warn(reason);
						API.status.setValue("webcalls/ignored/"+key+"/reason", reason);
						continue;
					}
				
				map.put(key, action);
				API.status.setValue("webcalls/actions/"+key, action.toString());
				API.status.setValue("webcalls/actions/"+key+"/declared_in", whereDefined);
				if (loaded != null)
					loaded.add(key);
				
				logger.trace("web call: "+(action.isPublic?"public":"[private]")+" "+(action.isStatic?"static":"[project]")+" "+(action.isInline?"inline":"[inproc]")+" "+(action.isSingle?"single":"[multi]")+" "+(action.callingConventions==CallingConventions.JSONCALL?"jsoncall":"webmemcall")+" "+(action.isClient?"[[client-side]]":"[[server-side]]")+" function `"+key+"' added, resolving to `"+value+"'");
			}
			logger.info("Web calls loaded from "+fileName+".");

		} catch (Throwable t) {
			logger.error("Could not load web calls from "+fileName+".");
		}					
		
	}
	
	synchronized public void loadWebCalls(SomeProperties props) {		
		for (String fname : props.webcallsFiles) {
			if (fname.endsWith("auto.webcalls")) {
				ArrayList<String> list = new ArrayList<String>();
				loadWebCalls(fname, list, props.id);
				
				String[] a = new String[props.auto_webcalls.length+list.size()];
				int j=0;
				for (String key : list)
					a[j++] = key;
				System.arraycopy(props.auto_webcalls, 0, a, j, props.auto_webcalls.length);
				props.auto_webcalls = a;
			}
			else
				loadWebCalls(fname, null, props.id);
			
			
		}
	}
	
	synchronized public boolean webCallExists(String actionName) {
		return map.containsKey(actionName);
	}
	
	@Override
	synchronized public WebCallDeclaration getWebCallDeclaration(String actionName) {
		return map.get(actionName);
	}
	
	synchronized public void unloadWebCalls(String fileName)
	{
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(fileName));
			for (String key : p.stringPropertyNames()) {
				key = key.trim();
				int i = key.lastIndexOf(' ');
				if (i>=0)
					key = key.substring(i+1);				
				map.remove(key);
				API.status.setValue("webcalls/"+key, null);
			}
		} catch (Throwable t) {
			logger.error(t.toString()+" - could not read web calls from file "+fileName);
		}					
	}
	
	synchronized public void unloadWebCalls(SomeProperties props) {		
		for (String fname : props.webcallsFiles) {
			unloadWebCalls(fname);
		}
	}

	@Override
	public int getQueueSize_R(String project_id) throws RemoteException {
		return this.getQueueSize(project_id);
	}

	@Override
	public void enqueue_R(WebCallSeed seed) throws RemoteException {
		this.enqueue(seed);
	}

	@Override
	public boolean invokeNow_R(WebCallSeed seed) throws RemoteException {
		return this.invokeNow(seed);
	}
	
	@Override
	public boolean webCallExists_R(String actionName) throws RemoteException {
		return this.webCallExists(actionName);
	}

	@Override
	public Map<String, WebCallDeclaration> getWebCalls_R(String fullAppName) throws RemoteException {
		return this.getWebCalls(fullAppName);
	}

	@Override
	public WebCallDeclaration getWebCallDeclaration_R(String actionName) throws RemoteException {
		return this.getWebCallDeclaration(actionName);
	}

}
