package com.ing3nia.parentalcontrol.services.models.utils;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class SimpleContactModelUtils {
	public static SimpleContactModel convertToSimpleContactModel(PCSimpleContact savedContact) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		SimpleContactModel contact = new SimpleContactModel();
		
		contact.setKeyId(KeyFactory.keyToString(savedContact.getKey()));
		contact.setFirstName(savedContact.getFirstName());
		contact.setLastName(savedContact.getLastName());
		
		ArrayList<PhoneModel> phones = new ArrayList<PhoneModel>();
		
		PCPhone pcphone = pm.getObjectById(PCPhone.class, savedContact.getPhone());
		phones.add(PhoneModelUtils.convertToPhoneModel(pcphone));		
		contact.setPhones(phones);
		
		return contact;
	}
	
	public static void removeSimpleContact(ArrayList<SimpleContactModel> contacts, String keyId) {
		int position = 0;
		boolean found = false;
		int size = contacts.size();
		
		for (int i = 0; i < size; i++) {
			if (contacts.get(i).getKeyId().equals(keyId)) {
				position = i;
				found = true;
				break;
			}
		}
		
		if (found) {
			contacts.remove(position);
		}
	}
	
	public static void addSimpleContact(PersistenceManager pm, ArrayList<SimpleContactModel> contacts, Key keyId) {
		PCSimpleContact pcContact = pm.getObjectById(PCSimpleContact.class, keyId);
		
		contacts.add(convertToSimpleContactModel(pcContact));
	}
}
