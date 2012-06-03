package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ContactModel implements IsSerializable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyId;
	
	private String firstName;
	
	private String lastName;
	
	private ArrayList<PhoneModel> phones;
	
	private ArrayList<String> emails;
	
	private ArrayList<AddressModel> addresses;
	
	private ArrayList<OrganizationModel> organizations;

	public ContactModel() {
		super();
	}

	public ContactModel(String keyId, String firstName, String lastName,
			ArrayList<PhoneModel> phones, ArrayList<String> emails,
			ArrayList<AddressModel> addresses,
			ArrayList<OrganizationModel> organizations) {
		super();
		this.keyId = keyId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
		this.emails = emails;
		this.addresses = addresses;
		this.organizations = organizations;
	}
	
	public ContactModel(String firstName, String lastName,
			ArrayList<PhoneModel> phones) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
	}
	
	public ContactModel(String key, String firstName, String lastName,
			ArrayList<PhoneModel> phones) {
		super();
		this.keyId = key;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
	}
	
	public ContactModel(String firstName, String lastName,
			ArrayList<PhoneModel> phones, ArrayList<String> emails,
			ArrayList<AddressModel> addresses,
			ArrayList<OrganizationModel> organizations) {
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

	public ArrayList<PhoneModel> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<PhoneModel> phones) {
		this.phones = phones;
	}

	public ArrayList<String> getEmails() {
		return emails;
	}

	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	}

	public ArrayList<AddressModel> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<AddressModel> addresses) {
		this.addresses = addresses;
	}

	public ArrayList<OrganizationModel> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(ArrayList<OrganizationModel> organizations) {
		this.organizations = organizations;
	}
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

}
