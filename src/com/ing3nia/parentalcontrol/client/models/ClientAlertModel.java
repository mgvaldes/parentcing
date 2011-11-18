package com.ing3nia.parentalcontrol.client.models;

import java.util.Date;

public class ClientAlertModel {
	/**
	 * Date when the alert was created.
	 */
	Date date;
	
	/**
	 * Name of the device that generated the alert.
	 */
	String deviceName;
	
	/**
	 * Message of the alert.
	 */
	String message;
	
	public ClientAlertModel(Date date, String deviceName, String message) {
		this.date = date;
		this.deviceName = deviceName;
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
