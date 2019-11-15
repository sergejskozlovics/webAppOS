package org.webappos.bridge;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.WebCaller;
import org.webappos.webmem.IWebMemory;
import org.webappos.webmem.WebMemoryContext;

import lv.lumii.tda.kernel.IEventsCommandsHook;
import lv.lumii.tda.raapi.RAAPIHelper;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public class BridgeEventsCommandsHook implements IEventsCommandsHook {

	private static Logger logger =  LoggerFactory.getLogger(BridgeEventsCommandsHook.class);

	
	public static BridgeEventsCommandsHook INSTANCE = new BridgeEventsCommandsHook();
	
	private void tokenizeEventHandlers(String names, ArrayList<String> arr) {
		if ((names==null) || (names.isEmpty()))
			return;
		
		String s;
		StringTokenizer tknz = new StringTokenizer(names, ",;");
		while (tknz.hasMoreTokens()) {		
			s = tknz.nextToken();
			
			// legacy fix:
			if (s.startsWith("lua_engine#lua."))
				s = "lua:"+s.substring(15);
			
			arr.add(s);
		}		
	}
	
	private String[] getEventHandlers(IWebMemory webmem, long rEvent) {
		ArrayList<String> arr = new ArrayList<String>();
		
		String className = RAAPIHelper.getObjectClassName(webmem, rEvent);
		if (className == null)
			return new String[0];
		
		String shortClassName = className.substring( className.lastIndexOf(':')+1 );
		
		// searching for the all on<EventName> in the context of the event...
		long rEventClass = RAAPIHelper.getObjectClass(webmem, rEvent);
		if (rEventClass != 0) {				
			long it = webmem.getIteratorForAllOutgoingAssociationEnds(rEventClass);
			if (it != 0) {
				long rAssoc = webmem.resolveIteratorFirst(it);
				while (rAssoc != 0) {
					
					long rCls2 = webmem.getTargetClass(rAssoc);
					long rAttr2 = webmem.findAttribute(rCls2, "on"+shortClassName);
					if ((rCls2 !=0) && (rAttr2 != 0)) {
					
						long itLinked = webmem.getIteratorForLinkedObjects(rEvent, rAssoc);
						long r2 = webmem.resolveIteratorFirst(itLinked);
						while (r2 != 0) {
							String val = webmem.getAttributeValue(r2, rAttr2);
							if (val != null)
								tokenizeEventHandlers(val, arr);
							
							webmem.freeReference(r2);
							r2 = webmem.resolveIteratorNext(itLinked);
						}
						webmem.freeIterator(itLinked);
					}
					
					webmem.freeReference(rCls2);
					webmem.freeReference(rAttr2);
					
					webmem.freeReference(rAssoc);
					rAssoc = webmem.resolveIteratorNext(it);
				}
				webmem.freeIterator(it);					
			}				
			webmem.freeReference(rEventClass);
		}										
		
		// searching on<EventName> in engines...
		long it = webmem.getIteratorForClasses();
		if (it != 0) {
			long rCls = webmem.resolveIteratorFirst(it);
			
			while (rCls != 0) {
				
				String clsName = webmem.getClassName(rCls);
				if ((clsName!=null) && clsName.endsWith("Engine")) {
					long rAttr = webmem.findAttribute(rCls, "on"+shortClassName);
					if (rAttr != 0) {
					
						long itObj = webmem.getIteratorForAllClassObjects(rCls);
						long r = webmem.resolveIteratorFirst(itObj);
						while (r != 0) {
							String val = webmem.getAttributeValue(r, rAttr);
							if (val != null)
								tokenizeEventHandlers(val, arr);							
							webmem.freeReference(r);
							r = webmem.resolveIteratorNext(itObj);
						}
						webmem.freeIterator(itObj);
					}
					
				}				
				
				webmem.freeReference(rCls);
				rCls = webmem.resolveIteratorNext(it);
			}
			webmem.freeIterator(it);
		}				
		
		
		return arr.toArray(new String[] {});
	}
	
	@Override
	synchronized public boolean handleEvent(IWebMemory webmem, long rEvent) {
		logger.trace("Caught web memory event "+rEvent);
		
		try {
			
			WebMemoryContext o = webmem.getContext();
	
			String[] handlers = getEventHandlers(webmem, rEvent);
			if (handlers.length==0)
				return true;
			
			for (String handler : handlers) {
	
				long rEvent2 = webmem.replicateObject(rEvent);
				if (rEvent2 == 0)
					return false;
						
				IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
				
				seed.actionName = handler;
				
				seed.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
				seed.webmemArgument = rEvent2;
				
		
				if (o!=null) {
					seed.login = o.login;
					seed.project_id = o.project_id;
				}

				webmem.flush();
		  		API.webCaller.enqueue(seed);
			}
	  		
			return true; // we forwarded this event to the foreground thread
		}
		finally {
			webmem.deleteObject(rEvent);
		}
	}

	@Override
	synchronized public boolean executeCommand(IWebMemory webmem, long rCommand) {		
		
		try {
			
			WebMemoryContext o = webmem.getContext();
			
			long it = webmem.getIteratorForDirectObjectClasses(rCommand);
			long rCls = webmem.resolveIteratorFirst(it);		
			String className = webmem.getClassName(rCls);
			webmem.freeReference(rCls);
			webmem.freeIterator(it);
			
			if (className == null)
				return false;
			
			className = className.substring(className.lastIndexOf(':')+1); // remove the prefix before ::
			
			long rCommand2 = webmem.replicateObject(rCommand);
			if (rCommand2 == 0)
				return false;
			
			IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
			
			seed.actionName = className;
			
			seed.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
			seed.webmemArgument = rCommand2;
			
	
			if (o!=null) {
				seed.login = o.login;
				seed.project_id = o.project_id;
			}
	  		
	  		API.webCaller.enqueue(seed);
	  		
			return true; // we forwarded this event to the foreground thread
		}
		finally {
			webmem.deleteObject(rCommand);
		}
	}
	
	synchronized public boolean handleSyncedEvent(IWebMemory webmem, long rEvent, RAAPI_Synchronizer singleSynchronizer, String login, String project_id, String fullAppName) {		
		try {
			String[] handlers = getEventHandlers(webmem, rEvent);
			if (handlers.length==0)
				return true;
			
			for (String handler : handlers) {
	
				long rEvent2 = webmem.replicateObject(rEvent);
				if (rEvent2 == 0)
					return false;
				
				WebCaller.SyncedWebCallSeed seed = new WebCaller.SyncedWebCallSeed();
				
				seed.actionName = handler;
				
				seed.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
				seed.webmemArgument = rEvent2;
				
				seed.singleSynchronizer = singleSynchronizer;
				seed.login = login;
				seed.project_id = project_id;
		  		
				webmem.flush();
		  		API.webCaller.enqueue(seed);
			}
	  		
			return true; // we forwarded this event to the foreground thread
		}
		finally {
			webmem.deleteObject(rEvent);
		}
	}
	
	synchronized public boolean executeSyncedCommand(IWebMemory webmem, long rCommand, RAAPI_Synchronizer singleSynchronizer, String login, String project_id, String fullAppName) {
		try {
			long it = webmem.getIteratorForDirectObjectClasses(rCommand);
			long rCls = webmem.resolveIteratorFirst(it);		
			String className = webmem.getClassName(rCls);
			webmem.freeReference(rCls);
			webmem.freeIterator(it);
			if (className == null)
				return false;
			
			className = className.substring(className.lastIndexOf(':')+1); // remove the prefix before ::
			
			long rCommand2 = webmem.replicateObject(rCommand);
			if (rCommand2 == 0)
				return false;
			
			WebCaller.SyncedWebCallSeed seed = new WebCaller.SyncedWebCallSeed();
			
			seed.actionName = className;
			
			seed.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
			seed.webmemArgument = rCommand2;
			
			seed.singleSynchronizer = singleSynchronizer;
			seed.login = login;
			seed.project_id = project_id;
	  		
	  		API.webCaller.enqueue(seed);
	  		
			return true; // we forwarded this event to the foreground thread
		}
		finally {
			webmem.deleteObject(rCommand);
		}
	}
	
}
