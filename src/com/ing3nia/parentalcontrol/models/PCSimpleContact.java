package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class PCSimpleContact {
	/**
	 * Unique key that identifies the contact.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String firstName;
	
	@Persistent
	private String lastName;
	
	@Persistent
	//key references PCPhone
	private Key phone;

	public PCSimpleContact() {
		super();
	}

	public PCSimpleContact(String firstName, String lastName,
			Key phone) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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

	public Key getPhone() {
		return phone;
	}

	public void setPhone(Key phone) {
		this.phone = phone;
	}
}
