package com.ing3nia.parentalcontrol.client.models;

import com.google.gwt.user.client.rpc.IsSerializable;


public class PhoneModel implements IsSerializable{
	private static final long serialVersionUID = 1L;
	
	private int type;
	
	private String phoneNumber;

	public PhoneModel() {
		super();
	}

	public PhoneModel(int type, String phoneNumber) {
		super();
		this.type = type;
		this.phoneNumber = phoneNumber;
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
