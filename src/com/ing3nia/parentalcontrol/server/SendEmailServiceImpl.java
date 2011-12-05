package com.ing3nia.parentalcontrol.server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.AlertModel;
import com.ing3nia.parentalcontrol.client.rpc.SendEmailService;

public class SendEmailServiceImpl extends RemoteServiceServlet implements SendEmailService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SendEmailServiceImpl.class.getName());
	
	public SendEmailServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean sendEmail(String toEmail, String content) {
		Boolean result = false;
		
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
			
			logger.info("[SendEmailService] Content: " + content);
			
			message.setText(content);

			Transport.send(message);
			result = true;
		} 
		catch (MessagingException e) {
			result = false;
		}
		
//		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
//		final String fromEmail = "mgvaldesgraterol@gmail.com";
//		final String fromPassword = "m4churucut0082009";
//		
//		try {
//			Key userKey = KeyFactory.stringToKey(loggedUserKey);
//			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);
//			
//			Properties props = new Properties();
//			props.put("mail.smtp.host", "smtp.gmail.com");
//			props.put("mail.smtp.socketFactory.port", "465");
//			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//			props.put("mail.smtp.auth", "true");
//			props.put("mail.smtp.port", "465");
//	 
//			Session session = Session.getDefaultInstance(props,
//				new javax.mail.Authenticator() {
//					protected PasswordAuthentication getPasswordAuthentication() {
//						return new PasswordAuthentication(fromEmail, fromPassword);
//					}
//				}
//			);
//			
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress(fromEmail));
//			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
//			message.setSubject("[PRC] Weekly alerts for smartphone: " + smartName);
//			message.setText(loadSmartAlerts(smartName, currentWeekAlerts(alerts)));
// 
//			Transport.send(message);
//			result = true;
//		}
//		catch (Exception ex) {
//			
//		}
		
		return result;
	}
	
	public String loadSmartAlerts(String smartName, ArrayList<AlertModel> alerts) {
		String nl = "\n";
		String alertsText = "Weekly alerts for smartphone " + smartName + " " + nl + nl;
		
		for (AlertModel alert : alerts) {
			alertsText += alert.getDate() + " " + alert.getMessage() + nl;
		}
		
		return alertsText;
	}
	
	public ArrayList<AlertModel> currentWeekAlerts(ArrayList<AlertModel> alerts) {
		ArrayList<AlertModel> filteredAlerts = new ArrayList<AlertModel>();
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();	
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		Date startWeek = new Date();
		Date endWeek = new Date();
		Date alertDate;
		
		if (dayOfWeek == 1) {
			//Monday
			startWeek = now;
			
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, 7);
			endWeek = calendar.getTime();
		}
		else if (dayOfWeek == 2) {
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			startWeek = calendar.getTime();
			
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, 6);
			endWeek = calendar.getTime();
		}
		else if (dayOfWeek == 3) {
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, -2);
			startWeek = calendar.getTime();
			
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, 5);
			endWeek = calendar.getTime();
		}
		else if (dayOfWeek == 4) {
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			startWeek = calendar.getTime();
			
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, 4);
			endWeek = calendar.getTime();
		}
		else if (dayOfWeek == 5) {
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, -4);
			startWeek = calendar.getTime();
			
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, 3);
			endWeek = calendar.getTime();
		}
		else if (dayOfWeek == 6) {
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, -5);
			startWeek = calendar.getTime();
			
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, 2);
			endWeek = calendar.getTime();
		}
		else if (dayOfWeek == 0) {
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, -6);
			startWeek = calendar.getTime();
			
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			endWeek = calendar.getTime();
		}
		
		for (AlertModel alert : alerts) {
			alertDate = alert.getDate();
			if (alertDate.after(startWeek) && alertDate.before(endWeek)) {
				filteredAlerts.add(alert);
			}
		}
		
		return filteredAlerts;
	}
}
