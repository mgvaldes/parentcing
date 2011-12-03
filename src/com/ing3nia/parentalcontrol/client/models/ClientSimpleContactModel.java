package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

public class ClientSimpleContactModel {
	
	/**
	 * Contact Key
	 */
	private String keyId;
	
	/**
	 * Contact's first name.
	 */
	private String firstName;
	
	/**
	 * Contact's last name.
	 */
	private String lastName;
	
	/**
	 * Contact's phone number.
	 */
	private String phone;
	
	private int phoneType;
	
	private ArrayList<String> emails;
	
	private ArrayList<AddressModel> addresses;
	
	private ArrayList<OrganizationModel> organizations;
	
	private boolean wasOriginallyActive;
	
	public ClientSimpleContactModel(String keyId, String firstName, String lastName, String phone, int phoneType, ArrayList<String> emails, ArrayList<AddressModel> addresses, ArrayList<OrganizationModel> organizations) {
		this.keyId = keyId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.phoneType = phoneType;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(int phoneType) {
		this.phoneType = phoneType;
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

	public void setWasOriginallyActive(boolean wasOriginallyActive) {
		this.wasOriginallyActive = wasOriginallyActive;
	}

	public boolean getWasOriginallyActive() {
		return wasOriginallyActive;
	}

	

}
