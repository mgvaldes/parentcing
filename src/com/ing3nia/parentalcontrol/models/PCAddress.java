package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about an address.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCAddress {
	/**
	 * Unique key that identifies the address.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Specifies the type of an address.  
	 */
	@Persistent
	private int type;
	
	/**
	 * Specifies the street of the address.
	 */
	@Persistent
	private String street;
	
	/**
	 * Specifies the city of the address.
	 */
	@Persistent
	private String city;
	
	/**
	 * Specifies the state/province/region of the address.
	 */
	@Persistent
	private String state;
	
	/**
	 * Specifies the zipCode of the address.
	 */
	@Persistent
	private String zipCode;
	
	/**
	 * Specifies the country of the address.
	 */
	@Persistent
	private String country;

	public PCAddress() {
		super();
	}

	public PCAddress(Key key, int type, String street, String city,
			String state, String zipCode, String country) {
		super();
		this.key = key;
		this.type = type;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
