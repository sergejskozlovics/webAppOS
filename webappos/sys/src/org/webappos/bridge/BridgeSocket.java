package org.webappos.bridge;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CompletableFuture;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.memory.MRAM.MRAM_Handle;
import org.webappos.server.API;
import org.webappos.server.APIForServerBridge;
import org.webappos.webcaller.WebCaller;

import lv.lumii.tda.raapi.RAAPI_Synchronizer;

// for each project connection there is a bridge socket instance;
// if project sharing is allowed, there may be multiple BridgeSockets to one project 
public class BridgeSocket extends WebSocketAdapter implements RAAPI_Synchronizer
// RAAPI_Synchronizer interface is needed for MRAM slot that will use <this> to sync the repository
{

	private static Logger logger =  LoggerFactory.getLogger(BridgeSocket.class);

	@SuppressWarnings("serial")
	public static class Servlet extends WebSocketServlet	
	{
		public Servlet() 
		{
			super();
		}
		
	    @Override
	    public void configure(WebSocketServletFactory factory)
	    {
	    	factory.getPolicy().setIdleTimeout(30*60*1000); // 30 mins
	        factory.register(BridgeSocket.class);
	    	
	        
//	        factory.setCreator(creator);
	    }
	    
	}
	
/*	public static WebSocketCreator creator = new MySocketCreator();
	static Integer i = 5;
	
	public static class MySocketCreator implements WebSocketCreator
	{
	    @Override
	    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp)
	    {
	        return new BridgeSocket("");
	    }
	}*/

	private Session s = null;
	private RemoteEndpoint remote = null;
	
	private boolean authenticated = false;
	private String login = null;			
	private boolean closed = false;

	private MRAM_Handle h = null;
	
	private long lastActivity = System.currentTimeMillis();
	
	
	private boolean initialSyncDone = false;
	private final static int MAX_BUF_A = 128*1024/*10*1024*1024*/;
	private ByteBuffer bufA = ByteBuffer.allocate(MAX_BUF_A); // TODO: max repo size
	private StringBuffer bufS = null;
	private long recvArr[] = null;
	
	private static Runnable EmptyRunnable = new Runnable() {
		@Override
		public void run() {
		}		
	};
	
	public Runnable onClose = EmptyRunnable; // empty by default
	
	public BridgeSocket()
    {
    }
	
    @Override
    public void onWebSocketConnect(Session sess)
    {	    	
    	super.onWebSocketConnect(sess);
    	s = sess;
        remote = s.getRemote();
    	
        logger.debug("Socket Connected: session=" + sess.hashCode()+" object="+this.hashCode()+" remote="+sess.getRemote().hashCode());
    }
	
    private void cleanup() {    
    	if (h!=null)
    		APIForServerBridge.memoryForServerBridge.disconnectFromMRAM(h);
    	authenticated = false;
    	login = null;
    	h = null;
    }
    
    
    private void kick() {
    	logger.debug("KICKED");
		try {
			throw new RuntimeException("stack");
		} catch (Throwable e) {
			e.printStackTrace();
		}
    	
		try {
			s.disconnect(); // will call cleanup
		} catch (IOException e) {
		}
    	cleanup();
		s = null; //?? null checks required    	
    }
    
    /**
     * @param message
     * @return +1 if the user has been just authenticated
     * 	       0 if the user was authenticated earlier
     *         -1 if the user has been kicked 
     */
    private int checkAuthenticationOrKick(String message) {
    	if (!authenticated) {
    		// message is in the form: OPEN|REOPEN|OPEN_TEMPLATE|NEW LOGIN TOKEN [ACK_FOR_REOPEN] [TEMPLATE_ID] PROJECT_ID|DESIRED_PROJECT_ID  
    		
    		logger.debug("AUTH MSG=`"+message+"'");
    		System.err.println("AUTH MSG=`"+message+"'");
    		int i = message.indexOf(' ');
    		if (i<0) {
    			kick();
    			return -1;
    		}
    		String op = message.substring(0, i);
    		message = message.substring(i+1);
    		
    		i = message.indexOf(' ');
    		if (i<0) {
    			kick();
    			return -1;   			
    		}
    		
    		
    		String login1 = message.substring(0, i);
    		message = message.substring(i+1);
    		
    		i = message.indexOf(' ');
    		if (i<0) {
    			kick();
    			return -1;   			
    		}
    		
    		String ws_token = message.substring(0, i);
    		message = message.substring(i+1);
    		
    		String collaboration_token = null;
    		
    		i = ws_token.indexOf('/');
    		if (i>=0) {
    			collaboration_token = ws_token.substring(i+1);
    			ws_token = ws_token.substring(0, i);
    		}
    		
    		String ack = null;
    		String template = null;
    		boolean bootstrap = false;
    		
    		
    		i = message.indexOf(' ');
    		if (i<0) {
    			kick();
    			return -1;   			
    		}
    		
    		String app_url_name = message.substring(0, i);
    		message = message.substring(i+1);
    		
    		
    		if (op.equalsIgnoreCase("REOPEN")) {
    			i = message.indexOf(' ');
        		if (i<0) {
        			kick();
        			return -1;    			
        		}
        		ack = message.substring(0, i);
        		message = message.substring(i+1);
    		}
    		else
    		if (op.equalsIgnoreCase("FROM_TEMPLATE")) {
    			i = message.indexOf(' ');
    			while ((i>0) && (message.charAt(i-1)=='\\')) // skipping "\\ " (escaped space)
    				i = message.indexOf(' ', i+1);
        		if (i<=0) {
        			kick();
        			return -1;    			
        		}
        		template = message.substring(0, i).replaceAll("\\\\ ", " ");
        		System.out.println("TEMPLATE ='"+template+"'");
        		message = message.substring(i+1);    			
    		}
    		else
    		if (op.equalsIgnoreCase("NEW")) {
    			bootstrap = true;
    		}
    		else
       		if (op.equalsIgnoreCase("OPEN")) {        		
        	}
       		else {
       			kick(); // invalid op
       			return -1;
       		}
    		    		
    		
    		String project_id = message.replaceAll("\\ ", " "); // remaining part of message
        	        	
    		//System.err.println("MESSAGE = "+message);
			i = project_id.indexOf('/');
			if (i<0) { // login part not found in project_id
				kick();
				return -1;
			}						
			String login2 = project_id.substring(0, i);			
			
			long available_action_for_ack = -1;
			if (ack!=null) {
				try {
					available_action_for_ack = Long.parseLong(ack);
				}
				catch(Throwable t) {				
				}
			}
			
			
			System.err.println("BRIDGE SOCKET AUTHENTICATING op="+op+" project_id="+project_id+" login="+login1+"/"+login2+" app_url_name="+app_url_name+" ack="+available_action_for_ack+" template="+template);
			// validating...
			try {
				ValidityChecker.checkLogin(login1, true);
				ValidityChecker.checkLogin(login2, true);
				if (template!=null) {
					if (template.startsWith("apptemplate:"))
						ValidityChecker.checkRelativePath(template.substring("apptemplate:".length()), false);
					else
					if (template.startsWith("publishedtemplate:"))
						ValidityChecker.checkRelativePath(template.substring("publishedtemplate:".length()), false);
					else
					if (template.startsWith("usertemplate:"))
						ValidityChecker.checkRelativePath(template.substring("usertemplate:".length()), false);
					else
						kick();
				}
				ValidityChecker.checkRelativePath(project_id, false);
			}
			catch(Throwable t) {
				t.printStackTrace();
				kick();
			}
			
			
			if (!UsersManager.projectOK(project_id, login1, ws_token, true)) {
				if (login1.equals(login2)) {
					// if the user is the owner, send 0xFE, thus the user can login again; then kicking...
	        		ByteBuffer bb = ByteBuffer.allocate(8);
	        		bb.order(ByteOrder.LITTLE_ENDIAN);
	        		bb.putDouble(0xFE); // token not found or expired
	        		bb.rewind();        		
	        		try {
	        			s.getRemote().sendBytes(bb);
	        		} catch (IOException e) {
	        		}					
				}
				// for non-owners just kicking...
				kick();
				return -1;					
			}
			
			
			this.login = login1;
        	authenticated = true;
        	final String ws_token1 = ws_token;
   			h = APIForServerBridge.memoryForServerBridge.connectToMRAM(bootstrap, app_url_name, template, project_id, this.login, ws_token, available_action_for_ack, this, new Runnable() {

				@Override
				public void run() {
					logger.debug("MRAM fault for "+project_id+" - kicking client with ws_token="+ws_token1); 
					BridgeSocket.this.kick(); // on memory fault
				}
   				
   			});
    		if (h==null) {
    			if (available_action_for_ack>=0) {
            		ByteBuffer bb = ByteBuffer.allocate(8);
            		bb.order(ByteOrder.LITTLE_ENDIAN);
            		bb.putDouble(0xFA); // ack sync error
            		bb.rewind();        		
            		try {
            			s.getRemote().sendBytes(bb);
            		} catch (IOException e) {
            		}
    			}
    			kick(); // could not connect
    			return -1;
    		}
    		
    		
    		if ((project_id==null) || (!project_id.equals(h.project_id))) {
				// sending back the updated project_id...
	    		ByteBuffer bb = ByteBuffer.allocate(8);
	    		bb.order(ByteOrder.LITTLE_ENDIAN);
	    		bb.putDouble(0xFC); // update project_id
	    		bb.rewind();        		
	    		try {
	    			s.getRemote().sendBytes(bb);
	    			s.getRemote().sendString(RAAPI_Synchronizer.sharpenString(h.project_id));
	    		} catch (IOException e) {
	    		}	
    		}
    		// TODO: Registry.setValue("users/"+login+"/recent/"+appName, value);
    		
    		return +1;
    	}
    	else
    		return 0;    	
    }
    
 
    
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {	    			    
        super.onWebSocketClose(statusCode,reason);
        if (!closed) {
        	closed = true;
        	cleanup();
        	onClose.run();
        }
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        if (!closed) {
        	closed = true;
        	cleanup();
        	onClose.run();
        }
    }
 
	private void updateLastActivity() {
		lastActivity = System.currentTimeMillis();		
	}
    
	private ByteBuffer getBuf1() {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}
	
	private ByteBuffer getBuf2() {
		ByteBuffer bb = ByteBuffer.allocate(16);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}
	
	private ByteBuffer getBuf3() {
		ByteBuffer bb = ByteBuffer.allocate(24);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}
	
	private ByteBuffer getBuf4() {
		ByteBuffer bb = ByteBuffer.allocate(32);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}

	private ByteBuffer getBuf5() {
		ByteBuffer bb = ByteBuffer.allocate(40);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}

	private ByteBuffer getBuf6() {
		ByteBuffer bb = ByteBuffer.allocate(48);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb;
	}

    @Override
    public void onWebSocketText(final String message)
    {
    	if (checkAuthenticationOrKick(message)!=0)
    		return; // if the user has been just authenticated or kicked, do nothing during this message

    	// now the user is authenticated
   		
    	final long recvArrCopy[];
		recvArrCopy = recvArr;
		recvArr = null;
		updateLastActivity();
		
		if (recvArrCopy==null) {
			System.err.println("BridgeSocket WIDOW MESSAGE: `"+message+"`");
			return;
		}

		try {
			switch ((int)recvArrCopy[0]) {
			case 0x01:	h.raapi_wr.createClass(RAAPI_Synchronizer.unsharpenString(message), recvArrCopy[1]);
						h.otherSynchronizers.syncCreateClass(RAAPI_Synchronizer.unsharpenString(message), recvArrCopy[1]);
						break;
			case 0x03:	h.raapi_wr.createAttribute (recvArrCopy[1], RAAPI_Synchronizer.unsharpenString(message), recvArrCopy[2], recvArrCopy[3]);
						h.otherSynchronizers.syncCreateAttribute(recvArrCopy[1], RAAPI_Synchronizer.unsharpenString(message), recvArrCopy[2], recvArrCopy[3]);
						break;
    		case 0x04:	String curval1 = h.raapi_wr.getAttributeValue(recvArrCopy[1], recvArrCopy[2]);
    					int k = message.indexOf('/');
    					String msgpart1;
    					String msgpart2;
    					if (k>=0) {
    						msgpart1 = RAAPI_Synchronizer.unsharpenString(message.substring(0, k));
    						msgpart2 = RAAPI_Synchronizer.unsharpenString(message.substring(k+1));
    					}
    					else {
    						msgpart1 = RAAPI_Synchronizer.unsharpenString(message);
    						msgpart2 = null;
    					}
    						 
    					if (h.raapi_wr.setSynchronizedAttributeValue (recvArrCopy[1], recvArrCopy[2], msgpart1, msgpart2)) {
    						h.otherSynchronizers.syncSetAttributeValue(recvArrCopy[1], recvArrCopy[2], msgpart1, msgpart2);
    						if ((msgpart2==null && curval1==null) || (msgpart2!=null && msgpart2.equals(curval1))) {
    							h.otherSynchronizers.syncValidateAttributeValue(recvArrCopy[1], recvArrCopy[2], h.raapi_wr.getAttributeValue(recvArrCopy[1], recvArrCopy[2]));
    						}
    					}
    					break;
    		case 0xA4:	String curval = h.raapi_wr.getAttributeValue(recvArrCopy[1], recvArrCopy[2]);
    					if (curval == null) {
    						h.kernel.getSynchronizer().syncDeleteAttributeValue(recvArrCopy[1], recvArrCopy[2]);
    					}
    					else {
        					String message2=RAAPI_Synchronizer.unsharpenString(message);
	    					if (!message2.equals(curval)) {
	    						if (message2.compareTo(curval)<0) {
	    							h.raapi_wr.setAttributeValue(recvArrCopy[1], recvArrCopy[2], message2);
	    							h.otherSynchronizers.syncSetAttributeValue(recvArrCopy[1], recvArrCopy[2], message2, curval);
	    						}
	    						else
	    							h.kernel.getSynchronizer().syncSetAttributeValue(recvArrCopy[1], recvArrCopy[2], curval, message2);
	    					}
						}
    					break;    			
    		case 0x05:	h.raapi_wr.createAssociation (recvArrCopy[1], recvArrCopy[2], RAAPI_Synchronizer.unsharpenString(message.substring(0, message.indexOf('/'))), RAAPI_Synchronizer.unsharpenString(message.substring(message.indexOf('/')+1)), (int)recvArrCopy[3]!=0, recvArrCopy[4], recvArrCopy[5]);
    					h.otherSynchronizers.syncCreateAssociation(recvArrCopy[1], recvArrCopy[2], RAAPI_Synchronizer.unsharpenString(message.substring(0, message.indexOf('/'))), RAAPI_Synchronizer.unsharpenString(message.substring(message.indexOf('/')+1)), (int)recvArrCopy[3]!=0, recvArrCopy[4], recvArrCopy[5]);
    					break;
    		case 0x15:
    					h.raapi_wr.createDirectedAssociation (recvArrCopy[1], recvArrCopy[2], RAAPI_Synchronizer.unsharpenString(message), (int)recvArrCopy[3]!=0, recvArrCopy[4]);
    					h.otherSynchronizers.syncCreateDirectedAssociation (recvArrCopy[1], recvArrCopy[2], RAAPI_Synchronizer.unsharpenString(message), (int)recvArrCopy[3]!=0, recvArrCopy[4]);
    					break;
    		case 0x25:	h.raapi_wr.createAdvancedAssociation (RAAPI_Synchronizer.unsharpenString(message), (int)recvArrCopy[1]!=0, (int)recvArrCopy[2]!=0, recvArrCopy[3]);
    					h.otherSynchronizers.syncCreateAdvancedAssociation (RAAPI_Synchronizer.unsharpenString(message), (int)recvArrCopy[1]!=0, (int)recvArrCopy[2]!=0, recvArrCopy[3]);
    					break;
    		case 0xC0: 
						logger.debug("webcall received via web socket");
						
						int i = message.indexOf('/');
						if (i<0) {
							logger.trace("tdacall "+message);
							
							WebCaller.SyncedWebCallSeed seed = new WebCaller.SyncedWebCallSeed();							
							seed.actionName = message;
							seed.callingConventions = WebCaller.CallingConventions.TDACALL;
							seed.tdaArgument = recvArrCopy[1];
							seed.singleSynchronizer = h.currentSynchronizer;
							seed.login = this.login;
							seed.project_id = h.project_id;
					  		
					  		API.webCaller.enqueue(seed);
							
						}
						else {
							String action = RAAPI_Synchronizer.unsharpenString(message.substring(0, i));
							String arg = RAAPI_Synchronizer.unsharpenString(message.substring(i+1));
							logger.trace("jsoncall "+action);
							
								
							WebCaller.SyncedWebCallSeed seed = new WebCaller.SyncedWebCallSeed();							
							seed.actionName = action;
							seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
							seed.jsonArgument = arg;
							seed.jsonResult = new CompletableFuture<String>();
							seed.singleSynchronizer = h.currentSynchronizer;
							seed.login = this.login;
							seed.project_id = h.project_id;

					  		seed.jsonResult.whenComplete((result, exception) -> {
			    				ByteBuffer buf2 = getBuf2();
			    				buf2.putDouble(0xC1);
			    				buf2.putDouble(recvArrCopy[1]);
			    				buf2.rewind();
			    				if (result!=null)
			    					sendBufAndString(buf2, RAAPI_Synchronizer.sharpenString(result));
			    				else
			    					sendBufAndString(buf2, "{}");					  			
					  		});

					  		API.webCaller.enqueue(seed);					  		
						}
						
	            break;						
    		case 0xC1: 
				logger.debug("webcall return value received: `"+message+"`");
	            break;						
			}
		}
		catch (Throwable t) {
			logger.error("Error processing ws text - "+t.toString()+" "+t.getMessage());
			t.printStackTrace();
		}	    			
		
    }
    
    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len)
    {
    	if (!authenticated) {
    		kick();
    		return;
    	}
    	if (closed)
    		return;
    	
    	final long recvArrCopy[];
    		
		int k = Double.SIZE / Byte.SIZE;
		recvArr = new long[len/k];
		for(int i=0;i<recvArr.length;i++){
			recvArr[i] = (long)ByteBuffer.wrap(payload, offset+i*k, k).order(java.nio.ByteOrder.LITTLE_ENDIAN).getDouble();
		}
		
		if (recvArr.length==0) {
			recvArr = null;
			return;
		}
		
		
		switch ((int)recvArr[0]) {
		case 0x01:
		case 0x03:
		case 0x04:
		case 0xA4:
		case 0x05:
		case 0x15:
		case 0x25:
		case 0xC0:
		case 0xC1:
			// keep recvArr for the string message...
			updateLastActivity();
			return;
		}
		
		recvArrCopy = recvArr;
		recvArr = null;
    	
    	
		try {
    		switch ((int)recvArrCopy[0]) {	    		
    		case 0xF1:	h.raapi_wr.deleteClass(recvArrCopy[1]);
    					h.otherSynchronizers.syncDeleteClass(recvArrCopy[1]);
    					break;
    		case 0x11:	h.raapi_wr.createGeneralization (recvArrCopy[1], recvArrCopy[2]);
    					h.otherSynchronizers.syncCreateGeneralization(recvArrCopy[1], recvArrCopy[2]);
    					break;
    		case 0xE1:	h.raapi_wr.deleteGeneralization (recvArrCopy[1], recvArrCopy[2]);
    					h.otherSynchronizers.syncDeleteGeneralization(recvArrCopy[1], recvArrCopy[2]);
    					break;
    		case 0x02:	h.raapi_wr.createObject (recvArrCopy[1], recvArrCopy[2]);
    					h.otherSynchronizers.syncCreateObject(recvArrCopy[1], recvArrCopy[2]);
    					break;
    		case 0xF2:	h.raapi_wr.deleteObject (recvArrCopy[1]);
    					h.otherSynchronizers.syncDeleteObject(recvArrCopy[1]);
    					break;
    		case 0x12:	h.raapi_wr.includeObjectInClass (recvArrCopy[1], recvArrCopy[2]);
    					h.otherSynchronizers.syncIncludeObjectInClass(recvArrCopy[1], recvArrCopy[2]);
    					break;
    		case 0xE2:	h.raapi_wr.excludeObjectFromClass (recvArrCopy[1], recvArrCopy[2]);
    					h.otherSynchronizers.syncExcludeObjectFromClass(recvArrCopy[1], recvArrCopy[2]);
    					break;
    		case 0x22:	h.raapi_wr.moveObject (recvArrCopy[1], recvArrCopy[2]);
    					h.otherSynchronizers.syncMoveObject(recvArrCopy[1], recvArrCopy[2]);
    					break;
    		case 0xF3:	h.raapi_wr.deleteAttribute (recvArrCopy[1]);
    					h.otherSynchronizers.syncDeleteAttribute(recvArrCopy[1]);
    					break;
    		case 0xF4:	h.raapi_wr.deleteAttributeValue(recvArrCopy[1], recvArrCopy[2]);
    					h.otherSynchronizers.syncDeleteAttributeValue(recvArrCopy[1], recvArrCopy[2]);
    					break;
    		case 0xF5:	h.raapi_wr.deleteAssociation (recvArrCopy[1]);
    					h.otherSynchronizers.syncDeleteAssociation(recvArrCopy[1]);
    					break;
    		case 0x06:  if (h.kernel.creatingSubmitLink(recvArrCopy[1], recvArrCopy[2], recvArrCopy[3])) {
    						long r = recvArrCopy[1];
    						if (h.kernel.isSubmitter(r))
    							r = recvArrCopy[2];
   							if (h.kernel.isEvent(r))
   								BridgeEventsCommandsHook.INSTANCE.handleSyncedEvent(h.kernel, r, h.currentSynchronizer, h.login, h.project_id, h.getFullAppName());
   							else
   							if (h.kernel.isCommand(r))
   								BridgeEventsCommandsHook.INSTANCE.executeSyncedCommand(h.kernel, r, h.currentSynchronizer, h.login, h.project_id, h.getFullAppName());
    					}
    					else {
    						h.raapi_wr.createLink (recvArrCopy[1], recvArrCopy[2], recvArrCopy[3]);
    						h.otherSynchronizers.syncCreateLink(recvArrCopy[1], recvArrCopy[2], recvArrCopy[3]);
    					}
    					break;
    		case 0x16:  if (h.kernel.creatingSubmitLink(recvArrCopy[1], recvArrCopy[2], recvArrCopy[3])) {
							long r = recvArrCopy[1];
							if (h.kernel.isSubmitter(r))
								r = recvArrCopy[2];
							if (h.kernel.isEvent(r))
								BridgeEventsCommandsHook.INSTANCE.handleSyncedEvent(h.kernel, r, h.currentSynchronizer, h.login, h.project_id, h.getFullAppName());
							else
							if (h.kernel.isCommand(r))
								BridgeEventsCommandsHook.INSTANCE.executeSyncedCommand(h.kernel, r, h.currentSynchronizer, h.login, h.project_id, h.getFullAppName());
    					}
    					else {
    						h.raapi_wr.createOrderedLink (recvArrCopy[1], recvArrCopy[2], recvArrCopy[3], (int)recvArrCopy[4]);
							h.otherSynchronizers.syncCreateOrderedLink(recvArrCopy[1], recvArrCopy[2], recvArrCopy[3], (int)recvArrCopy[4]);
    					}
    					break;
    		case 0xF6: 
    					h.raapi_wr.deleteLink (recvArrCopy[1], recvArrCopy[2], recvArrCopy[3]);
    					h.otherSynchronizers.syncDeleteLink(recvArrCopy[1], recvArrCopy[2], recvArrCopy[3]);
    					break;
    		case 0xA6:
   						// validate that the link exists...
						if (h.raapi_wr.linkExists(recvArrCopy[1], recvArrCopy[2], recvArrCopy[3]))
							; // ok
						else {
							// the link does not exist; 
							// we force to delete the link at all synchronizers
	    					h.kernel.getSynchronizer().syncDeleteLink(recvArrCopy[1], recvArrCopy[2], recvArrCopy[3]);
						}
    					break;
    		case 0xBB: // saveBall
    			
    			/*ForegroundThread.runInForegroundThread(new Runnable() {

					@Override
					public void run()*/ {
    					boolean b1=false, b2=false;
    					boolean err = false;
    					try {
    						b1=h.kernel.startSave();
    						b2=h.kernel.finishSave();
    					}
    					catch(Throwable t) {
    						err = true;
    					}
    					if (err || !b1 || !b2)
    						syncSaveBall(); // try save later
    				}/*
    			}, login);*/
    		}
    		
    		updateLastActivity();
    		
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
    	
    }	    	    
	@Override
	synchronized public void syncCreateClass(String name, long rClass) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0x01);
		buf2.putDouble(rClass);
		buf2.rewind();
		sendBufAndString(buf2, RAAPI_Synchronizer.sharpenString(name));
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteClass(long rClass) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0xF1);
		buf2.putDouble(rClass);
		buf2.rewind();
		sendBufAndString(buf2, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateGeneralization(long rSub, long rSuper) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x11);
		buf3.putDouble(rSub);
		buf3.putDouble(rSuper);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteGeneralization(long rSub, long rSuper) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0xE1);
		buf3.putDouble(rSub);
		buf3.putDouble(rSuper);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateObject(long rClass, long rObject) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x02);
		buf3.putDouble(rClass);
		buf3.putDouble(rObject);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteObject(long rObject) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0xF2);
		buf2.putDouble(rObject);
		buf2.rewind();
		sendBufAndString(buf2, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncIncludeObjectInClass(long rObject, long rClass) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x12);
		buf3.putDouble(rObject);
		buf3.putDouble(rClass);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncExcludeObjectFromClass(long rObject, long rClass) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0xE2);
		buf3.putDouble(rObject);
		buf3.putDouble(rClass);
		buf3.rewind();
		sendBufAndString(buf3, null);			
		updateLastActivity();
	}

	@Override
	synchronized public void syncMoveObject(long rObject, long rClass) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x22);
		buf3.putDouble(rObject);
		buf3.putDouble(rClass);
		buf3.rewind();
		sendBufAndString(buf3, null);						
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateAttribute(long rClass, String name,
			long rPrimitiveType, long rAttr) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0x03);
		buf4.putDouble(rClass);
		buf4.putDouble(rPrimitiveType);
		buf4.putDouble(rAttr);
		buf4.rewind();
		sendBufAndString(buf4, RAAPI_Synchronizer.sharpenString(name));
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteAttribute(long rAttr) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0xF3);
		buf2.putDouble(rAttr);
		buf2.rewind();
		sendBufAndString(buf2, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncSetAttributeValue(long rObject, long rAttr, String value, String oldValue) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0x04);
		buf3.putDouble(rObject);
		buf3.putDouble(rAttr);
		buf3.rewind();
		if (oldValue == null)
			sendBufAndString(buf3, RAAPI_Synchronizer.sharpenString(value));
		else
			sendBufAndString(buf3, RAAPI_Synchronizer.sharpenString(value)+"/"+RAAPI_Synchronizer.sharpenString(oldValue));
		updateLastActivity();
	}
	
	@Override
	synchronized public void syncDeleteAttributeValue(long rObject, long rAttr) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0xF4);
		buf3.putDouble(rObject);
		buf3.putDouble(rAttr);
		buf3.rewind();
		sendBufAndString(buf3, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncValidateAttributeValue(long rObject, long rAttr, String value) {
		ByteBuffer buf3 = getBuf3();
		buf3.putDouble(0xA4);
		buf3.putDouble(rObject);
		buf3.putDouble(rAttr);
		buf3.rewind();
		sendBufAndString(buf3, RAAPI_Synchronizer.sharpenString(value));
		updateLastActivity();		
	}

	@Override
	synchronized public void syncCreateAssociation(long rSourceClass, long rTargetClass,
			String sourceRoleName, String targetRoleName,
			boolean isComposition, long rAssoc, long rInv) {
		ByteBuffer buf6 = getBuf6();
		buf6.putDouble(0x05);
		buf6.putDouble(rSourceClass);
		buf6.putDouble(rTargetClass);
		buf6.putDouble(isComposition?1:0);
		buf6.putDouble(rAssoc);
		buf6.putDouble(rInv);
		buf6.rewind();
		sendBufAndString(buf6, RAAPI_Synchronizer.sharpenString(sourceRoleName)+"/"+RAAPI_Synchronizer.sharpenString(targetRoleName));
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateDirectedAssociation(long rSourceClass,
			long rTargetClass, String targetRoleName,
			boolean isComposition, long rAssoc) {
		ByteBuffer buf5 = getBuf5();
		buf5.putDouble(0x15);
		buf5.putDouble(rSourceClass);
		buf5.putDouble(rTargetClass);
		buf5.putDouble(isComposition?1:0);
		buf5.putDouble(rAssoc);
		buf5.rewind();
		sendBufAndString(buf5, RAAPI_Synchronizer.sharpenString(targetRoleName));
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateAdvancedAssociation(String name, boolean nAry,
			boolean associationClass, long rAssoc) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0x25);
		buf4.putDouble(nAry?1:0);
		buf4.putDouble(associationClass?1:0);
		buf4.putDouble(rAssoc);
		buf4.rewind();
		sendBufAndString(buf4, RAAPI_Synchronizer.sharpenString(name));
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteAssociation(long rAssoc) {
		ByteBuffer buf2 = getBuf2();
		buf2.putDouble(0xF5);
		buf2.putDouble(rAssoc);
		buf2.rewind();
		sendBufAndString(buf2, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateLink(long rSourceObject, long rTargetObject,
			long rAssociationEnd) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0x06);
		buf4.putDouble(rSourceObject);
		buf4.putDouble(rTargetObject);
		buf4.putDouble(rAssociationEnd);
		buf4.rewind();
		sendBufAndString(buf4, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncCreateOrderedLink(long rSourceObject,
			long rTargetObject, long rAssociationEnd, long targetPosition) {
		ByteBuffer buf5 = getBuf5();
		buf5.putDouble(0x16);
		buf5.putDouble(rSourceObject);
		buf5.putDouble(rTargetObject);
		buf5.putDouble(rAssociationEnd);
		buf5.putDouble(targetPosition);
		buf5.rewind();
		sendBufAndString(buf5, null);
		updateLastActivity();
	}

	@Override
	synchronized public void syncDeleteLink(long rSourceObject, long rTargetObject,
			long rAssociationEnd) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0xF6);
		buf4.putDouble(rSourceObject);
		buf4.putDouble(rTargetObject);
		buf4.putDouble(rAssociationEnd);
		buf4.rewind();
		sendBufAndString(buf4, null);
		updateLastActivity();
	}
		
	@Override
	synchronized public void syncValidateLink(long rSourceObject, long rTargetObject,
			long rAssociationEnd) {
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0xA6);
		buf4.putDouble(rSourceObject);
		buf4.putDouble(rTargetObject);
		buf4.putDouble(rAssociationEnd);
		buf4.rewind();
		sendBufAndString(buf4, null);
		updateLastActivity();
	}
	
	@Override
	synchronized public void syncRawAction(double[] arr, String str) {
		
		ByteBuffer bb = ByteBuffer.allocate(arr.length*8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for (int i=0; i<arr.length; i++)
			bb.putDouble(arr[i]);
		
		bb.rewind();
		sendBufAndString(bb, str);
		updateLastActivity();
	}
	
	synchronized public void sendAndClearBuffersNow() {
		String s = "";
		if (bufS!=null)
			s = bufS.toString();
		if ((bufA.position()==0) && (bufS==null))
			return; // nothing to send
		
		try {
			int pos = bufA.position();
			
			bufA.limit(pos);
			bufA.rewind();
			boolean bulk = false;
			if (pos>0) {
				bulk = (bufA.getDouble()==0xEE);
				bufA.rewind();
				remote.sendBytesByFuture(bufA);
			}
			
			if (bufS != null) {
				remote.sendStringByFuture(s);
			}
			else {
				if (bulk) {
					remote.sendString("");
				}
			}
		}
		catch(Throwable t) {
			logger.error("Error sending buffers "+t.getMessage());
		}
		
		bufA = ByteBuffer.allocate(MAX_BUF_A);
		// bufA.limit(MAX_BUF_A);
		// bufA.rewind();
		
		bufS = null;		
	}
	
	synchronized private void checkBuffersOverflow(ByteBuffer bb, String s) {
		boolean overflow = false;
		if ((bb!=null) && (bufA.position()+bb.capacity() >= MAX_BUF_A))
			overflow = true;
		
		if ((s!=null) && (bufS!=null) && (bufS.length()+s.length()>MAX_BUF_A))
			overflow = true;
				
		if (overflow) {
			sendAndClearBuffersNow();
		}
	}
	
	synchronized private void sendBuffersLater() {
		
		assert (bufA.position()>0) || (bufS!=null); // there must be smth to send
		
		GlobalSenderThread.sendBuffersLater( this);
//or (much slower):		sendAndClearBuffersNow();		
	}
	
	@Override
	synchronized public void syncMaxReference(long r, int bitsCount, long bitsValues) {			
		ByteBuffer buf4 = getBuf4();
		buf4.putDouble(0xFF);
		buf4.putDouble(r);
		buf4.putDouble(bitsCount);
		buf4.putDouble(bitsValues);
		buf4.rewind();
		sendBufAndString(buf4, null);			

		sendAndClearBuffersNow();
		
		initialSyncDone = true;

		updateLastActivity();
	}
	
	public void syncSaveBall() {
		ByteBuffer buf1 = getBuf1();
		buf1.putDouble(0xBB);
		buf1.rewind();
		//remote.sendBytesByFuture(buf1);
		sendBufAndString(buf1, null);
	}

	synchronized private void sendBufAndString(ByteBuffer bb/*optional*/, String s/*optional*/) {
//		if (initialSyncDone) {	
			
			checkBuffersOverflow(bb, s);
			
			if (bb!=null) {
				if (bufA.position()==0) {
					bufA.order(ByteOrder.LITTLE_ENDIAN);
					bufA.putDouble(0xEE);
					bufA.put(bb);
				}
				else {
					bufA.put(bb);
				}
			}
			if (s != null) {
				if (bufS==null) {
					bufS = new StringBuffer();
					bufS.append(s);
				}
				else {
					bufS.append("`");
					bufS.append(s);
				}
			}
			
			sendBuffersLater();			
/*		}
		else {
			checkBuffersOverflow(bb, s);
			
			if (bb!=null) {
				if (bufA.position()==0) {
					bufA.order(ByteOrder.LITTLE_ENDIAN);
					bufA.putDouble(0xEE);
					bufA.put(bb);
				}
				else {
					bufA.put(bb);
				}
			}
			if (s != null) {
				if (bufS==null) {
					bufS = new StringBuffer();
					bufS.append(s);
				}
				else {
					bufS.append("`");
					bufS.append(s);
				}
			}
		}*/
		updateLastActivity();
	}
	
	@Override
	synchronized public void sendString(String s) {
		sendBufAndString(null, s);
	}
	@Override
	public void syncBulk(double[] actions, String[] strings) {
		syncBulk(actions.length, actions, strings.length, strings);

	}
	
	@Override
	public void syncBulk(int nActions, double[] actions, int nStrings, String[] strings) {
		assert actions[0] == 0xEE;
		sendAndClearBuffersNow();
		
		ByteBuffer bb = ByteBuffer.allocate(nActions*8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for (int i=0; i<nActions; i++)
			bb.putDouble(actions[i]);
		bb.rewind();
		
		StringBuffer sb = new StringBuffer();

		boolean first = true;
		for (int i=0; i<nStrings; i++) {
		  	  if (first)
				  first = false;
			  else
				  sb.append('`');
		  	  sb.append(strings[i]);
		}
		remote.sendBytesByFuture(bb);
		remote.sendStringByFuture(sb.toString());
	}

	@Override
	public void flush() {
		sendAndClearBuffersNow();
		updateLastActivity();
	}

	@Override
	public void syncBulk(int nActions, double[] actions, String delimitedStrings) {
		assert actions[0] == 0xEE;
		sendAndClearBuffersNow();
		
		ByteBuffer bb = ByteBuffer.allocate(nActions*8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for (int i=0; i<nActions; i++)
			bb.putDouble(actions[i]);
		bb.rewind();
		
		remote.sendBytesByFuture(bb);
		remote.sendStringByFuture(delimitedStrings);		
	}

/*	@Override
	public void syncBulk(ByteBuffer actions, String delimitedEscapedStrings) {
		sendAndClearBuffersNow();
		remote.sendBytesByFuture(actions);
		remote.sendStringByFuture(delimitedEscapedStrings);		
	}*/
	
}