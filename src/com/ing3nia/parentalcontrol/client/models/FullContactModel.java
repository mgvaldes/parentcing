package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

public class FullContactModel {
	/**
	 * Contact's first name.
	 */
	private String firstName;
	
	/**
	 * Contact's last name.
	 */
	private String lastName;
	
	/**
	 * Contact's emails
	 */
	private ArrayList<String> emails;
	
	/**
	 * Contact's organization name.
	 */	
	private String organizationName;
	
	/**
	 * Contact's organization title.
	 */
	private String organizationTitle;
	
	/**
	 * Contact's phone type.
	 */
	private String phoneType;
	
	/**
	 * Contact's phone number.
	 */
	private String phoneNumber;
	
	/**
	 * Contact's street addres.
	 */
	private String street;
	
	/**
	 * Contact's city address.
	 */
	private String city;
	
	/**
	 * Contact's state address.
	 */
	private String state;
	
	/**
	 * Contact's zip code address.
	 */
	private String zipCode;
	
	/**
	 * Contact's country address.
	 */
	private String country;

	public FullContactModel(String firstName, String lastName,
			ArrayList<String> emails, String organizationName,
			String organizationTitle, String phoneType, String phoneNumber,
			String street, String city, String state, String zipCode,
			String country) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emails = emails;
		this.organizationName = organizationName;
		this.organizationTitle = organizationTitle;
		this.phoneType = phoneType;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
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

	public ArrayList<String> getEmails() {
		return emails;
	}

	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationTitle() {
		return organizationTitle;
	}

	public void setOrganizationTitle(String organizationTitle) {
		this.organizationTitle = organizationTitle;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
