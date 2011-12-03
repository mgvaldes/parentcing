package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;
import java.util.ArrayList;

public class SimpleContactModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyId;

	private String firstName;
	
	private String lastName;
	
	private ArrayList<PhoneModel> phones;

	public SimpleContactModel() {
		super();
	}

	public SimpleContactModel(String keyId, String firstName, String lastName,
			ArrayList<PhoneModel> phones) {
		super();
		this.keyId = keyId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
	}
	
	public SimpleContactModel(String firstName, String lastName,
			ArrayList<PhoneModel> phones) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
	}
	


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ArrayList<PhoneModel> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<PhoneModel> phones) {
		this.phones = phones;
	}
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
}
