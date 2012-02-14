package com.ing3nia.parentalcontrol.services.models.utils;

import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;

public class EmergencyNumberModelUtils {

	public static EmergencyNumberModel convertToEmergencyNumberModel(PCEmergencyNumber emergencyNumber) {
		return new EmergencyNumberModel(KeyFactory.keyToString(emergencyNumber.getKey()), emergencyNumber.getCountry(), emergencyNumber.getNumber(), emergencyNumber.getDescription());
	}
}
