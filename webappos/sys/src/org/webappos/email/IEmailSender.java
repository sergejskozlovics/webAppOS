package org.webappos.email;

public interface IEmailSender {
	public boolean sendEmail(String toEmail, String subject, String body);
}
