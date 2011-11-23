package com.ing3nia.parentalcontrol.client.models;

import java.util.Date;

/**
 * Class that represents a single input in the alert list view.
 * 
 * @author María Gabriela Valdés.
 * @author Javier Fernández.
 * @author Ing3nia.
 *
 */
public class AlertModel {
	/**
	 * Date when the alert was created.
	 */
	private Date date;
	
	/**
	 * Name of the device associated to this alert.
	 */
	private String device;
	
	/**
	 * Message associated to this alert.
	 */
	private String message;
	
	public AlertModel(Date date, String device, String message) {
		this.date = date;
		this.device = device;
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
}
