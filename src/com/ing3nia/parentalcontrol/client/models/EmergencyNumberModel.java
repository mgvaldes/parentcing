package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;

public class EmergencyNumberModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String keyId;
	
	private String country;
	
	private String number;
	
	private String description;

	public EmergencyNumberModel() {
		super();
	}

	public EmergencyNumberModel(String keyId, String country, String number,
			String description) {
		super();
		this.keyId = keyId;
		this.country = country;
		this.number = number;
		this.description = description;
	}
	
	public EmergencyNumberModel(String country, String number,
			String description) {
		super();
		this.country = country;
		this.number = number;
		this.description = description;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

}
