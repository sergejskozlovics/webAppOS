package org.webappos.util;

//import java.security.SecureRandom;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

public class RandomToken {
	
	public static String generateSalt() {
        /*SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);*/
		return generateRandomToken();
    }
	

	public static String generateRandomToken() {
		RandomStringGenerator randomStringGenerator =
		        new RandomStringGenerator.Builder()
		                .withinRange('0', 'z')
		                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
		                .build();
		return randomStringGenerator.generate(20);
	}

}
