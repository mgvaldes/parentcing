package com.ing3nia.parentalcontrol.client.utils;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;

public class EmailSender {
	
	public static void sendEmail(String toEmail, String content, BaseViewHandler baseView) {
		final String fromEmail = "prc.daddytext@gmail.com";
		final String fromPass = "p4r3nt4l";
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
        
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(fromEmail, fromPass);
					}
				}
		);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail));
			message.setSubject("[Parental Control] Weekly Alerts");
			message.setText(content);

			Transport.send(message);

		} 
		catch (MessagingException e) {
			baseView.getBaseBinder().getNotice().setText("An error occured. The email couldn't be sent.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
