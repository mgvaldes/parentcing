package com.ing3nia.parentalcontrol.client.models;

public class ClientPhoneModel {
	private String type;
	
	private String phoneNumber;

	public ClientPhoneModel() {
		super();
	}

	public ClientPhoneModel(String type, String phoneNumber) {
		super();
		this.type = type;
		this.phoneNumber = phoneNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
