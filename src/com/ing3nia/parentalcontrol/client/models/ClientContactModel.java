package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

public class ClientContactModel {
	private String key;
	
	private String firstName;
	
	private String lastName;
	
	private ArrayList<ClientPhoneModel> phones;
	
	private ArrayList<String> emails;
	
	private ArrayList<ClientAddressModel> addresses;
	
	private ArrayList<ClientOrganizationModel> organizations;

	public ClientContactModel() {
		super();
	}

	public ClientContactModel(String key, String firstName, String lastName,
			ArrayList<ClientPhoneModel> phones, ArrayList<String> emails,
			ArrayList<ClientAddressModel> addresses,
			ArrayList<ClientOrganizationModel> organizations) {
		super();
		this.key = key;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
		this.emails = emails;
		this.addresses = addresses;
		this.organizations = organizations;
	}
	
	public ClientContactModel(String firstName, String lastName,
			ArrayList<ClientPhoneModel> phones) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
	}
	
	public ClientContactModel(String firstName, String lastName,
			ArrayList<ClientPhoneModel> phones, ArrayList<String> emails,
			ArrayList<ClientAddressModel> addresses,
			ArrayList<ClientOrganizationModel> organizations) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
		this.emails = emails;
		this.addresses = addresses;
		this.organizations = organizations;
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

	public ArrayList<String> getEmails() {
		return emails;
	}

	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	}

	public ArrayList<ClientAddressModel> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<ClientAddressModel> addresses) {
		this.addresses = addresses;
	}

	public ArrayList<ClientOrganizationModel> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(ArrayList<ClientOrganizationModel> organizations) {
		this.organizations = organizations;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
