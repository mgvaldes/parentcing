package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

public class ClientSimpleContactModel {
	private String key;

	private String firstName;
	
	private String lastName;
	
	private ArrayList<ClientPhoneModel> phones;

	public ClientSimpleContactModel() {
		super();
	}

	public ClientSimpleContactModel(String key, String firstName, String lastName,
			ArrayList<ClientPhoneModel> phones) {
		super();
		this.key = key;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
	}
	
	public ClientSimpleContactModel(String firstName, String lastName,
			ArrayList<ClientPhoneModel> phones) {
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

	public ArrayList<ClientPhoneModel> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<ClientPhoneModel> phones) {
		this.phones = phones;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}	
}
