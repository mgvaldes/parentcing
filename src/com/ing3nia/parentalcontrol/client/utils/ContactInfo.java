package com.ing3nia.parentalcontrol.client.utils;

import java.util.ArrayList;

import com.ing3nia.parentalcontrol.client.models.PhoneModel;

public class ContactInfo {
	private String key;
	
	private ArrayList<PhoneModel> phones;
	
	public ContactInfo() {
		
	}
	
	public ContactInfo(String key, ArrayList<PhoneModel> phones) {
		this.key = key;
		this.phones = phones;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<PhoneModel> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<PhoneModel> phones) {
		this.phones = phones;
	}
}
