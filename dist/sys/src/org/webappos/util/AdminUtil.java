package org.webappos.util;

import com.google.gson.JsonElement;
import org.webappos.auth.UsersManager;
import org.webappos.server.API;
import org.webappos.server.ConfigEx;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminUtil {
	public static void main(String[] args) {
		try {
		    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		    System.out.println("VM does not support mandatory encoding UTF-8");
		}
		
		API.initOfflineAPI();
		if (args.length == 0) {
			showHelpMessage();
			System.exit(0);
			return;
		}
		
		String cmd = args[0];
		
		switch (cmd){
			case "show_waiting_users":
				showWaitingUsers();
				break;
			case "show_blocked_users":
				showBlockedUsers();
				break;
			case "block_users":
				if (args[1].length() != 0){
					blockUsers(args[1]);
				}
				break;
			case "unblock_users":
				if (args[1].length() != 0) {
					unblockUsers(args[1]);
				}
				break;
			case "create_new_user":
				if (args[1].length() != 0) {
					createNewUser(args[1]);
				}
				break;
			case "check_login_availability":
				if (args[1].length() != 0){
					checkLoginAvailability(args[1]);
				}
				break;
			case "mount":
				if (args[1].length() != 0 && args[2].length() != 0){
					mountDrive(args[1], args[2]);
				}
			case "help":
			default:
				showHelpMessage();
				break;
		}

		System.exit(0);
	}
	
	
	private static void showHelpMessage() {
		System.out.println("██╗    ██╗███████╗██████╗  █████╗ ██████╗ ██████╗  ██████╗ ███████╗");
		System.out.println("██║    ██║██╔════╝██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔═══██╗██╔════╝");
		System.out.println("██║ █╗ ██║█████╗  ██████╔╝███████║██████╔╝██████╔╝██║   ██║███████╗");
		System.out.println("██║███╗██║██╔══╝  ██╔══██╗██╔══██║██╔═══╝ ██╔═══╝ ██║   ██║╚════██║");
		System.out.println("╚███╔███╔╝███████╗██████╔╝██║  ██║██║     ██║     ╚██████╔╝███████║");
		System.out.println(" ╚══╝╚══╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝      ╚═════╝ ╚══════╝");
		System.out.println(" ----------------------------------------------------------------------");
		System.out.println("| help: show this message                                              |");
		System.out.println("| show_waiting_users: show waiting users                               |");
		System.out.println("| show_blocked_users: show blocked users                               |");
		System.out.println("| unblock_users [path/to/file] OR [username]: unblock list of users    |");
		System.out.println("|                        from file or one just by writing his login    |");
		System.out.println("| create_new_user [email]: create new user and verify him              |");
		System.out.println("| check_login_availability [email]: check username/ email              |");
		System.out.println("|                                   availability                       |");
		System.out.println("| mount [user] [path/to/mount/point] [prefix:path/to/remote/location]: |");
		System.out.println("|                                                    mount remote drive|");
		System.out.println(" ----------------------------------------------------------------------");
		
	}
	
	private static void showWaitingUsers(){
		List <String> users = getUsersFromRegistry(true);
		for (String user : users){
			JsonElement verified = API.registry.getValue(ConfigEx.ETC_DIR+"xusers/" + user + "/email_verified");
			JsonElement blocked = API.registry.getValue(ConfigEx.ETC_DIR+"xusers/" + user + "/blocked");
			try {
				if ((verified==null||!verified.getAsBoolean()) && (blocked!=null && blocked.getAsBoolean())) {
					System.out.println(user + ": verified = " + verified + " blocked = " + blocked);
				}
			}
			catch(Throwable t) {
				// since getAsBoolean may crash
				System.out.println("Something went wrong");
			}
		}
	}
	
	private static void showBlockedUsers() {
		List <String> users = getUsersFromRegistry(true);
		if(users.size() > 0){
			for (String user : users){
				JsonElement verified = API.registry.getValue(ConfigEx.ETC_DIR+"xusers/" + user + "/email_verified");
				JsonElement blocked = API.registry.getValue(ConfigEx.ETC_DIR+"xusers/" + user + "/blocked");
				try {
					if (verified.getAsBoolean() && (blocked!=null && blocked.getAsBoolean())) {
						System.out.println(user + ": verified = " + verified + " blocked = " + blocked);
					}
				}
				catch(Throwable t) {
					// since getAsBoolean may crash
				}
			}
		}
	}
	
	private static void unblockUsers(String path) {
		List <String> users = getUsersFromFile(path);
		
		for (String user : users){
			API.registry.setValue(ConfigEx.ETC_DIR+"xusers/" + user + "/blocked", false);
			System.out.println("User " + user + " has been unblocked");
		}
	}
	
	private static void blockUsers(String path) {
		List <String> users = getUsersFromFile(path);
		
		for (String user : users){
			API.registry.setValue(ConfigEx.ETC_DIR+"xusers/" + user + "/blocked", true);
			System.out.println("User " + user + " has been blocked");
		}
	}
	
	private static void createNewUser(String login) {
		if(checkLoginAvailability(login)){
			UsersManager.addUser(login, true);
			unblockUsers(login);
			API.registry.setValue(ConfigEx.ETC_DIR+"xusers/" + login + "/email_verified", true);
			File file = new File(ConfigEx.HOME_DIR + "/" + login);
			if (file.mkdirs()){
				System.out.println("Directory successfully created");
			} else {
				System.out.println("Failed to create directory");
			}
		} else {
			System.out.println("User with this email or username already exist!");
		}
	}
	
	private static Boolean checkLoginAvailability(String login) {
		List<String> users = getUsersFromRegistry(false);
		for (String user : users){
			if (user.equals(login)){
				return false;
			}
		}
		return true;
	}
	
	private static void mountDrive(String user, String drive) {
		API.registry.setValue(ConfigEx.ETC_DIR+"users/" + user + "/fs_mount_points", drive);
	}
	
	
	private static List<String> getUsersFromFile(String path){
		List<String> users = new ArrayList<>();
		try {
			File file = new File(path);
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
				String login = scanner.nextLine();
				login = login.substring(0, login.indexOf(":"));
				System.out.println(login);
				users.add(login);
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			//file not found, but it can be a login of user
			String user = getUserFromRegistry(path);
			
			if (user == null){
				System.out.println("File or user not found.");
			} else {
				users.add(user);
			}
		}
		return users;
	}
	
	private static List<String> getUsersFromRegistry(Boolean makeAliasCheck){
		List<String> users = new ArrayList<>();
		File[] files = new File(ConfigEx.ETC_DIR+"/registry/xusers/").listFiles();
		
		if (files != null){
			for (File file : files) {
				if (file.isFile()) {
					if(makeAliasCheck){
						String emailOrLogin = file.getName();
						String login = UsersManager.getUserLogin(emailOrLogin);
						if (emailOrLogin.equals(login)) { // not an alias
							users.add(login);
						}
					} else {
						users.add(file.getName());
					}
				}
			}
		}
		return users;
	}
	
	private static String getUserFromRegistry(String user){
		File file = new File(ConfigEx.ETC_DIR+"/registry/xusers/" + user);
		
		if (file.isFile()) {
			return file.getName();
		}
		return null;
	}
}
