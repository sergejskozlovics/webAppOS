package org.webappos.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class TLS_SMTP_EmailSender implements IEmailSender {
	private Session session = null;
	private String from;
	private String fromName;
	
	public TLS_SMTP_EmailSender(String smtp_server, String smtp_auth, String _from, String _fromName) {
		if (session==null)
			session = initSession(smtp_server, smtp_auth);		
		from = _from;
		fromName = _fromName;
	}
	
	private Session initSession(String smtp_server, String smtp_auth) {
		
		Properties props = new Properties();
		
		int i=smtp_server.lastIndexOf(":");
		
		props.put("mail.smtp.host", (i<0)?smtp_server:smtp_server.substring(0, i));
		props.put("mail.smtp.port", (i<0)?"587":smtp_server.substring(i+1));
		props.put("mail.smtp.auth", "true"); //enable authentication
		
		if (props.get("mail.smtp.port").equals("587"))
			props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
        //create Authenticator object to pass in Session.getInstance argument
		int j=smtp_auth.indexOf(":");
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				//System.err.println("google login="+Config.smtp_auth.substring(0, j));
				//System.err.println("google pass="+Config.smtp_auth.substring(j+1));
				return new PasswordAuthentication((j<0)?smtp_auth:smtp_auth.substring(0, j), (j<0)?"":smtp_auth.substring(j+1));
			}
		};
		return Session.getInstance(props, auth);		
	}

	@Override
	public boolean sendEmail(String toEmail, String subject, String body) {
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress(from, fromName));

	      msg.setReplyTo(InternetAddress.parse(from, false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      System.out.println("Message is ready");
    	  Transport.send(msg);  

	      System.out.println("EMail Sent Successfully!!");
	      return true;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return false;
	    }
	}



	
}