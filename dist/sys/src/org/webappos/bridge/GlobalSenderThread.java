package org.webappos.bridge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlobalSenderThread extends Thread {

	private static GlobalSenderThread senderThread = null;//new SenderThread();
	private static Map<BridgeSocket, Long> m = new HashMap<BridgeSocket, Long>();	 // socket -> nanoTime
	
	public void run() {
		long WAIT_MS = 10;
		for (;;) {
			try {
				Thread.sleep(WAIT_MS);
			}
			catch(Throwable t) {				
			}			
			
			
			Set<BridgeSocket> arr;
			synchronized (GlobalSenderThread.class) {
				arr  = new HashSet<BridgeSocket>(m.keySet()); // we copy bridge sockets to avoid synchronized race condition;
				m.clear();
			}
			
			// here we call sendAndClearBuffers out of synchronized context
			for (BridgeSocket s : arr) {
				s.sendAndClearBuffersNow();
			}
		}		
	}
	
	
	
	
	/**
	 * Calls s.sendAndClearBuffers(), but not right away. We will wait until some other actions are put into socket
	 * buffers, and call s.sendAndClearBuffers() a bit later.
	 * @param s the socket, on which sendAndClearBuffers will be invoked some time later
	 */
	public static void sendBuffersLater(BridgeSocket s) {
		synchronized (GlobalSenderThread.class) {
			long lastBufTime = System.nanoTime();
			m.put(s, lastBufTime);
	
			if (senderThread == null) {
				// start sender thread (if not started)
				senderThread = new GlobalSenderThread();
				senderThread.start();
			}
		}
	}

}
