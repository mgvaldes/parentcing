package com.ing3nia.parentalcontrol.client.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.jdo.PersistenceManager;

import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class NotificationModel {
	private int type;
	
	private String date;

	public NotificationModel() {
		super();
	}

	public NotificationModel(int type, String date) {
		super();
		this.type = type;
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public PCNotification convertToPCNotification() {
		PCNotification notification = new PCNotification();
		
		notification.setType(this.type);
		
		notification.setMessage(PCNotificationTypeId.getNotificationMessageFromType(this.type));
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		try {
			notification.setDate(formatter.parse(this.date));
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
