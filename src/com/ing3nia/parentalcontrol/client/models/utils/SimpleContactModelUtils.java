package com.ing3nia.parentalcontrol.client.models.utils;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class SimpleContactModelUtils {
	public static SimpleContactModel convertToSimpleContactModel(PCSimpleContact savedContact) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		SimpleContactModel contact = new SimpleContactModel();
		
		contact.setFirstName(savedContact.getFirstName());
		contact.setLastName(savedContact.getLastName());
		
		ArrayList<PhoneModel> phones = new ArrayList<PhoneModel>();
		
		PCPhone pcphone = pm.getObjectById(PCPhone.class, savedContact.getPhone());
		phones.add(PhoneModelUtils.convertToPhoneModel(pcphone));		
		contact.setPhones(phones);
		
		return contact;
	}
}
