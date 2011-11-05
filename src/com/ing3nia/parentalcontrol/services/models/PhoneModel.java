package com.ing3nia.parentalcontrol.services.models;

import com.google.appengine.api.datastore.PhoneNumber;
import com.ing3nia.parentalcontrol.models.PCPhone;

public class PhoneModel {
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
	
	public PCPhone convertToPCPhone() {
		PCPhone phone = new PCPhone();
		
		phone.setType(this.type);
		phone.setPhoneNumber(this.phoneNumber);
		
		return phone;
	}
	
	public static PhoneModel convertToPhoneModel(PCPhone phone) {
		return new PhoneModel(phone.getType(), phone.getPhoneNumber());
	}
}
