package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PhoneNumber;

/**
 * This class contains information about a phone.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCPhone {
	/**
	 * Unique key that identifies the phone.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Specifies the type of the phone number.
	 */
	private int type;
	
	/**
	 * Contains the specific phone number.
	 */
	private String phoneNumber;

	public PCPhone() {
		super();
	}

	public PCPhone(int type, String phoneNumber) {
		super();
		this.key = key;
		this.type = type;
		this.phoneNumber = phoneNumber;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
