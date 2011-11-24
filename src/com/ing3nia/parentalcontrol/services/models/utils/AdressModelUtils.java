package com.ing3nia.parentalcontrol.services.models.utils;

import com.ing3nia.parentalcontrol.client.models.AddressModel;
import com.ing3nia.parentalcontrol.models.PCAddress;

public class AdressModelUtils {
	public static PCAddress convertToPCAddress(AddressModel adressModel) {
		PCAddress address = new PCAddress();
		
		address.setCity(adressModel.getCity());
		address.setCountry(adressModel.getCountry());
		address.setState(adressModel.getState());
		address.setStreet(adressModel.getStreet());
		address.setType(adressModel.getType());
		address.setZipCode(adressModel.getZipCode());
		
		return address;
	}
	
	public static AddressModel convertToAddressModel(PCAddress address) {
		return new AddressModel(address.getType(), address.getStreet(), address.getCity(), address.getState(), address.getZipCode(), address.getCountry());
	}
}
