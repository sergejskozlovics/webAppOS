package org.webappos.email;

/**
 * E-mail Sender API
 *
 */
public interface IEmailSender {
	/**
	 * @param toEmail the e-mail address in the "To:" field
	 * @param subject the subject
	 * @param body text to send
	 * @return whether the operation succeeded
	 */
	public boolean sendEmail(String toEmail, String subject, String body);
}
