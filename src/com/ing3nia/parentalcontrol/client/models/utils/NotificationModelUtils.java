package com.ing3nia.parentalcontrol.client.models.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.jdo.PersistenceManager;

import com.ing3nia.parentalcontrol.client.models.NotificationModel;
import com.ing3nia.parentalcontrol.client.models.PCNotificationTypeId;
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class NotificationModelUtils {
	public static PCNotification convertToPCNotification(NotificationModel not) {
		PCNotification notification = new PCNotification();
		
		notification.setType(not.getType());
		
		notification.setMessage(PCNotificationTypeId.getNotificationMessageFromType(not.getType()));
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		try {
			notification.setDate(formatter.parse(not.getDate()));
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notification;
	}
	
	public static void savePCNotification(PCNotification notification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			pm.makePersistent(notification);
		}
		finally {
			pm.close();
		}
	}
}
