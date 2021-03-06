package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;

public class AddressModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int type;
	
	private String street;
	
	private String city;
	
	private String state;
	
	private String zipCode;
	
	private String country;

	public AddressModel(int type, String street, String city, String state,
			String zipCode, String country) {
		super();
		this.type = type;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
	

}
