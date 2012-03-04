package com.ing3nia.parentalcontrol.services.models.utils;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.client.models.AddressModel;
import com.ing3nia.parentalcontrol.client.models.ContactModel;
import com.ing3nia.parentalcontrol.client.models.OrganizationModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.models.PCAddress;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCOrganization;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class ContactModelUtils {

	public static ArrayList<Key> saveAsPCSimpleContact(ContactModel contact) {
		ArrayList<Key> contactKeys = new ArrayList<Key>();
		PCSimpleContact pcSimpleContact;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		ArrayList<PCSimpleContact> pscList = new ArrayList<PCSimpleContact>();
		
		for (PhoneModel phone : contact.getPhones()) {
			
			PCPhone pcphone = PhoneModelUtils.convertToPCPhone(phone);
			pm.makePersistent(pcphone);

			pcSimpleContact = new PCSimpleContact(contact.getFirstName(), contact.getLastName(), pcphone.getKey());
			pscList.add(pcSimpleContact);
		}
		//pm.makePersistentAll(pscList);
		for(PCSimpleContact psc : pscList){
			try{
				pm.makePersistent(psc);
				contactKeys.add(psc.getKey());
			}catch(Exception e){
				//continue
			}
		}

		// getting and saving keys from every simple contact
		/*
		for(PCSimpleContact psc : pscList){
			contactKeys.add(psc.getKey());
		}*/
		
		pm.close();
		return contactKeys;
	}
	
	public static Key convertToPCContact(ContactModel contactModel) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCContact contact = new PCContact();
		
		contact.setFirstName(contactModel.getFirstName());
		contact.setLastName(contactModel.getLastName());
		
		//key to PCPhone
		ArrayList<Key> pcPhones = new ArrayList<Key>();
		
		if (contactModel.getPhones() != null) {
			for (PhoneModel phone : contactModel.getPhones()) {
				PCPhone pcphone = PhoneModelUtils.convertToPCPhone(phone);
				pm.makePersistent(pcphone);
				pcPhones.add(pcphone.getKey());
				//pcPhones.add(phone.convertToPCPhone());
			}
		}
		
		contact.setPhones(pcPhones);
		contact.setEmails(contactModel.getEmails());
		
		ArrayList<PCAddress> pcAddress = new ArrayList<PCAddress>();
		
		if (contactModel.getAddresses() != null) {
			for (AddressModel address : contactModel.getAddresses()) {
				pcAddress.add(AdressModelUtils.convertToPCAddress(address));
			}			
		}
		
		contact.setAddresses(pcAddress);
		
		ArrayList<PCOrganization> pcOrganizations = new ArrayList<PCOrganization>();
		
		if (contactModel.getOrganizations() != null) {
			for (OrganizationModel organization : contactModel.getOrganizations()) {
				pcOrganizations.add(OrganizationModelUtils.convertToPCOrganization(organization));
			}			
		}
		
		contact.setOrganizations(pcOrganizations);
		
		pm.makePersistent(contact);
		
		pm.close();
		
		return contact.getKey();
	}
	
	public static ContactModel convertToContactModel(PCContact savedContact) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		ContactModel contact = new ContactModel();
		
		contact.setFirstName(savedContact.getFirstName());
		contact.setLastName(savedContact.getLastName());
		
		ArrayList<PhoneModel> phones = new ArrayList<PhoneModel>();
		ArrayList<Key> pcphoneListKeys = savedContact.getPhones();
		ArrayList<PCPhone> pcphoneList = new ArrayList<PCPhone>();
		PCPhone auxPhone;
		
		for (Key k : pcphoneListKeys) {
			auxPhone = (PCPhone)pm.getObjectById(PCPhone.class, k);
			pcphoneList.add(auxPhone);
		}
		
		for (PCPhone phone : pcphoneList) {
			phones.add(PhoneModelUtils.convertToPhoneModel(phone));
		}
		
		contact.setPhones(phones);
		contact.setEmails(savedContact.getEmails());
		
		ArrayList<AddressModel> addresses = new ArrayList<AddressModel>();
		
		for (PCAddress address : savedContact.getAddresses()) {
			addresses.add(AdressModelUtils.convertToAddressModel(address));
		}
		
		contact.setAddresses(addresses);
		
		ArrayList<OrganizationModel> organizations = new ArrayList<OrganizationModel>();
		
		for (PCOrganization organization : savedContact.getOrganizations()) {
			organizations.add(OrganizationModelUtils.convertToOrganizationModel(organization));
		}
		
		contact.setOrganizations(organizations);
		
		return contact;
	}
}
