package org.webappos.util;

import java.io.IOException;
import java.net.ServerSocket;

public class Ports {
	public static boolean portTaken(int port)
	{
	    ServerSocket socket = null;
	    try {
	        socket = new ServerSocket(port);
	    } catch (IOException e) {
	    	return true;
	    } finally {
	        if (socket != null)
	            try {
	                socket.close();
	            } catch (IOException e) { }
	    }
	    return false;
	}

}
