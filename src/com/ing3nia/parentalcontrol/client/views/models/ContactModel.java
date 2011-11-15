package com.ing3nia.parentalcontrol.client.views.models;

public class ContactModel {
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
	
	public ContactModel(String firstName, String lastName, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
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
}
