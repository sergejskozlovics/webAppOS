package org.webappos.server;

public interface IEmailSender {
	public boolean sendEmail(String toEmail, String subject, String body);
}
