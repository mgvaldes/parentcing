package com.ing3nia.parentalcontrol.models;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about a notification sent to 
 * a smartphone of a user.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCNotification {
	/**
	 * Unique key that identifies the notification.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Contains the message of a specific notification.
	 */
	@Persistent
	private String message;
	
	/**
	 * Represents the date when the notification was sent.
	 */
	@Persistent
	private Date date;
	
	/**
	 * 
	 */
	private int type;
	
	/**
	 * Represents the smartphone that will receive a specific
	 * notification.
	 */
	@Persistent
	private PCSmartphone smatphone;

	public PCNotification() {
		super();
	}

	public PCNotification(Key key, String message, Date date, int type,
			PCSmartphone smatphone) {
		super();
		this.key = key;
		this.message = message;
		this.date = date;
		this.type = type;
		this.smatphone = smatphone;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public PCSmartphone getSmatphone() {
		return smatphone;
	}

	public void setSmatphone(PCSmartphone smatphone) {
		this.smatphone = smatphone;
	}
}
