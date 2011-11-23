package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class SimpleContactModel {
	
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

	public static SimpleContactModel convertToSimpleContactModel(PCSimpleContact savedContact) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		SimpleContactModel contact = new SimpleContactModel();
		
		contact.setFirstName(savedContact.getFirstName());
		contact.setLastName(savedContact.getLastName());
		
		ArrayList<PhoneModel> phones = new ArrayList<PhoneModel>();
		
		PCPhone pcphone = pm.getObjectById(PCPhone.class, savedContact.getPhone());
		phones.add(PhoneModel.convertToPhoneModel(pcphone));		
		contact.setPhones(phones);
		
		return contact;
	}
}
