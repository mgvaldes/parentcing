package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

import com.google.gwt.i18n.client.DateTimeFormat;


public class ClientUserModel {
	/**
	 * String that represents the key of this PCAdmin object.
	 */
	String key;
	
	/**
	 * Admin user's username.
	 */
	String username;
	
	/**
	 * Admin user's password.
	 */
	String password;
	
	/**
	 * Smartphone list associated to a user.
	 */
	ArrayList<SmartphoneModel> smartphones;
	
	/**
	 * Admin users added by owner user.
	 */
	ArrayList<ClientAdminUserModel> admins;
	
	public ClientUserModel(String key, String username, String password, ArrayList<SmartphoneModel> smartphones, ArrayList<ClientAdminUserModel> admins) {
		this.key = key;
		this.username = username;
		this.password = password;
		this.smartphones = smartphones;
		this.admins = admins;
	}
	
	public ClientUserModel(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<SmartphoneModel> getSmartphones() {
		return smartphones;
	}

	public void setSmartphones(ArrayList<SmartphoneModel> smartphones) {
		this.smartphones = smartphones;
	}

	public ArrayList<ClientAdminUserModel> getAdmins() {
		return admins;
	}

	public void setAdmins(ArrayList<ClientAdminUserModel> admins) {
		this.admins = admins;
	}
	
	public ArrayList<AlertModel> getUserAlertList() {
		ArrayList<AlertModel> alerts = new ArrayList<AlertModel>();
		AlertModel auxAlert;
		DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");
		
		for (SmartphoneModel smart : this.getSmartphones()) {
			for (NotificationModel not : smart.getAlerts()) {
				auxAlert = new AlertModel(formatter.parse(not.getDate()), smart.getName(), PCNotificationTypeId.getNotificationMessageFromType(not.getType()));
				alerts.add(auxAlert);
			}
		}
		
		return alerts;
	}
}
