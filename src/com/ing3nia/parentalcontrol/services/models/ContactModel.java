package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.models.PCAddress;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCOrganization;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class ContactModel {
	private String firstName;
	
	private String lastName;
	
	private ArrayList<PhoneModel> phones;
	
	private ArrayList<String> emails;
	
	private ArrayList<AddressModel> addresses;
	
	private ArrayList<OrganizationModel> organizations;

	public ContactModel() {
		super();
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
	
	public Key savePCContact(PersistenceManager pm) {
		Key contactKey = null;
		PCContact pcContact = this.convertToPCContact();
<<<<<<< HEAD
=======
		//System.out.println("Holaaaaaa!: " + pcContact.getFirstName() + " " + pcContact.getLastName() + " numeros: " + pcContact.getPhones().get(0).getPhoneNumber().getNumber());
>>>>>>> 32fe3e75765a428f38435f47b91af58b3eaf5a4d
		pm.makePersistent(pcContact);
		contactKey = pcContact.getKey();
		
		return contactKey;
	}
	
	public PCContact convertToPCContact() {
		PCContact contact = new PCContact();
		
		contact.setFirstName(this.firstName);
		contact.setLastName(this.lastName);
		
		ArrayList<PCPhone> pcPhones = new ArrayList<PCPhone>();
		
		if (this.phones != null) {
			for (PhoneModel phone : this.phones) {
				pcPhones.add(phone.convertToPCPhone());
			}
		}
		
		contact.setPhones(pcPhones);
		contact.setEmails(this.emails);
		
		ArrayList<PCAddress> pcAddress = new ArrayList<PCAddress>();
		
		if (this.addresses != null) {
			for (AddressModel address : this.addresses) {
				pcAddress.add(address.convertToPCAddress());
			}			
		}
		
		contact.setAddresses(pcAddress);
		
		ArrayList<PCOrganization> pcOrganizations = new ArrayList<PCOrganization>();
		
		if (this.organizations != null) {
			for (OrganizationModel organization : this.organizations) {
				pcOrganizations.add(organization.convertToPCOrganization());
			}			
		}
		
		contact.setOrganizations(pcOrganizations);
		
		return contact;
	}
	
	public static ContactModel convertToContactModel(PCContact savedContact) {
		ContactModel contact = new ContactModel();
		
		contact.setFirstName(savedContact.getFirstName());
		contact.setLastName(savedContact.getLastName());
		
		ArrayList<PhoneModel> phones = new ArrayList<PhoneModel>();
		
		for (PCPhone phone : savedContact.getPhones()) {
			phones.add(PhoneModel.convertToPhoneModel(phone));
		}
		
		contact.setPhones(phones);
		contact.setEmails(savedContact.getEmails());
		
		ArrayList<AddressModel> addresses = new ArrayList<AddressModel>();
		
		for (PCAddress address : savedContact.getAddresses()) {
			addresses.add(AddressModel.convertToAddressModel(address));
		}
		
		contact.setAddresses(addresses);
		
		ArrayList<OrganizationModel> organizations = new ArrayList<OrganizationModel>();
		
		for (PCOrganization organization : savedContact.getOrganizations()) {
			organizations.add(OrganizationModel.convertToOrganizationModel(organization));
		}
		
		contact.setOrganizations(organizations);
		
		return contact;
	}
}
