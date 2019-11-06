package org.webappos.util;

import java.security.MessageDigest;

public class SHA256Gen {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("A string argument is required.");
			return;
		}

		String sha = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-256");
			crypt.reset();
			crypt.update(args[0].getBytes("UTF-8"));
			sha = java.util.Base64.getEncoder().encodeToString(crypt.digest());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.out.println(sha);

	}

}
