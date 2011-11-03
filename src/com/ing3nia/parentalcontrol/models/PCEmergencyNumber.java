package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PhoneNumber;

/**
 * This class contains information of emergency phone 
 * numbers on a specific country.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCEmergencyNumber {
	/**
	 * Unique key that identifies the emergency number.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Represents the country corresponding to this phone number.
	 */
	@Persistent
	private String country;
	
	/**
	 * Specifies the emergency phone number.
	 */
	@Persistent
	private PhoneNumber number;
	
	@Persistent
	private String description;

	public PCEmergencyNumber() {
		super();
	}

	public PCEmergencyNumber(Key key, String country, PhoneNumber number,
			String description) {
		super();
		this.key = key;
		this.country = country;
		this.number = number;
		this.description = description;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public PhoneNumber getNumber() {
		return number;
	}

	public void setNumber(PhoneNumber number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
