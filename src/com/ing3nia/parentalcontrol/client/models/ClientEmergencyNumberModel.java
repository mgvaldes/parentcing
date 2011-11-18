package com.ing3nia.parentalcontrol.client.models;

public class ClientEmergencyNumberModel {
	private String key;
	
	private String country;
	
	private String number;
	
	private String description;

	public ClientEmergencyNumberModel() {
		super();
	}

	public ClientEmergencyNumberModel(String key, String country, String number,
			String description) {
		super();
		this.key = key;
		this.country = country;
		this.number = number;
		this.description = description;
	}
	
	public ClientEmergencyNumberModel(String country, String number,
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
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
