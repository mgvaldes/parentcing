package com.ing3nia.parentalcontrol.services.models.utils;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;

public class EmergencyNumberModelUtils {

	public static EmergencyNumberModel convertToEmergencyNumberModel(PCEmergencyNumber emergencyNumber) {
		return new EmergencyNumberModel(KeyFactory.keyToString(emergencyNumber.getKey()), emergencyNumber.getCountry(), emergencyNumber.getNumber(), emergencyNumber.getDescription());
	}
	
	public static EmergencyNumberModel removeEmergencyNumber(ArrayList<EmergencyNumberModel> emergencyNums, String keyId) {
		int position = 0;
		boolean found = false;
		int size = emergencyNums.size();
		EmergencyNumberModel foundEmergency = null;
		
		for (int i = 0; i < size; i++) {
			EmergencyNumberModel emergencyNumber = emergencyNums.get(i);
			if (emergencyNumber.getKeyId().equals(keyId)) {
				position = i;
				found = true;
				foundEmergency = emergencyNumber;
				break;
			}
		}
		
		if (found) {
			emergencyNums.remove(position);
			return foundEmergency;
		}
		return null;
	}
	
	public static void addEmergencyNumber(PersistenceManager pm, ArrayList<EmergencyNumberModel> emergencyNums, Key keyId) {
		PCEmergencyNumber pcEmergencyNum = pm.getObjectById(PCEmergencyNumber.class, keyId);
		
		emergencyNums.add(convertToEmergencyNumberModel(pcEmergencyNum));
	}
}
