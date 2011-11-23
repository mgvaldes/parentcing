package com.ing3nia.parentalcontrol.client.models.utils;

import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;

public class EmergencyNumberModelUtils {

	public static EmergencyNumberModel convertToEmergencyNumberModel(PCEmergencyNumber emergencyNumber) {
		return new EmergencyNumberModel(emergencyNumber.getCountry(), emergencyNumber.getNumber().getNumber(), emergencyNumber.getDescription());
	}
}
