package org.webappos.email;

/**
 * E-mail Sender API.
 * Used by webAppOS login servlet to validate e-mail addresses. Can also be used by other webAppOS apps and services. 
 *
 */
public interface IEmailSender {
	/**
	 * Sends an e-mail.
	 * 
	 * @param toEmail the e-mail address in the "To:" field
	 * @param subject the subject
	 * @param body text to send
	 * @return whether the operation succeeded
	 */
	public boolean sendEmail(String toEmail, String subject, String body);
}
