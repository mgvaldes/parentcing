package com.ing3nia.parentalcontrol.client.models.utils;

import java.util.ArrayList;

import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.models.PCPhone;

public class PhoneModelUtils {
	public static PCPhone convertToPCPhone(PhoneModel phoneModel) {
		PCPhone phone = new PCPhone();
		
		phone.setType(phoneModel.getType());
		phone.setPhoneNumber(phoneModel.getPhoneNumber());
		
		return phone;
	}
	
	public static PhoneModel convertToPhoneModel(PCPhone phone) {
		return new PhoneModel(phone.getType(), phone.getPhoneNumber());
	}
	
	public static boolean checkPhonePresent(ArrayList<PCPhone> phones, PhoneModel phoneModel) {
		boolean present = false;
		
		for (PCPhone phone : phones) {
			if ((phone.getType() == phoneModel.getType()) && phone.getPhoneNumber().equals(phoneModel.getPhoneNumber())) {
				present = true;
				break;
			}
		}
		
		return present;
	}
}
