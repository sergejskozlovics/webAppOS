package org.webappos.webcalls;

import org.webappos.auth.UsersManager;


public class UsersActions_webcalls {

	public static String userInGroup(String group, String login) { // no raapi; group is the first arg; login is an automatic arg
		return "{\"result\":"+UsersManager.userInGroup(login, group)+"}";
	}	

}
