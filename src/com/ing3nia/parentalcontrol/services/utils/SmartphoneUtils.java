package com.ing3nia.parentalcontrol.services.utils;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;


/**
 * Contain a series of utils methods to manipulate and query smartphone info
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public class SmartphoneUtils {
	
	/**
	 * Returns smartphone general information as a json object
	 */
	public static JsonObject getSmartphoneGeneralInfo(PCSmartphone pcSmart){
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		JsonObject smpJson = new JsonObject();
		smpJson.addProperty("id", KeyFactory.keyToString(pcSmart.getKey()));
		smpJson.addProperty("name",pcSmart.getName());
		
		JsonObject deviceJson = new JsonObject();
		PCDevice device = pcSmart.getDevice();
		deviceJson.addProperty("model", device.getModel());
		deviceJson.addProperty("version", device.getVersion());
		deviceJson.addProperty("os", device.getOs().getOsType());
		smpJson.add("device", deviceJson);
		
		JsonArray alertsJson = new JsonArray();
		for(Key notKey : pcSmart.getNotifications()){
			PCNotification notif = (PCNotification) pm.getObjectById(PCNotification.class, notKey);
			JsonObject alert = new JsonObject();
			alert.addProperty("type", String.valueOf(notif.getType()));
			alert.addProperty("verbose", notif.getMessage());
			alert.addProperty("date", notif.getDate().toString());
			alertsJson.add(alert);
		}
		smpJson.add("alerts", alertsJson);
		
		JsonObject locJson = new JsonObject();
		locJson.addProperty("lat", pcSmart.getLocation().getLatitude());
		locJson.addProperty("long", pcSmart.getLocation().getLongitude());
		
		smpJson.add("location", locJson);
		pm.close();
		
		return smpJson;
	}
	
	
	/**
	 * Returns a json array containing the general information of the children smartphones 
	 * corresponding to the given user
	 */
	public static JsonArray getChildrenSmartphonesInfo(PCUser user){
		JsonArray smpList = new JsonArray();
		
		for(PCSmartphone smartphone : user.getSmartphones()){
			JsonObject smpGrlInfo = getSmartphoneGeneralInfo(smartphone);
			smpList.add(smpGrlInfo);
		}
		
		return smpList;
	}
	
	/**
	 * Returns a json array containing detailed information about a given smartphone
	 */
	public static JsonObject getSmartphoneDetails(PCSmartphone smartphone){
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		JsonObject detailsJson = new JsonObject();
		
		//Obtaining and parsing inactive contacts
		JsonArray inactiveArray = new JsonArray();
		
		for(Key contactKey : smartphone.getInactiveContacts()){
			JsonObject contactJson = new JsonObject();
			PCSimpleContact contact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, contactKey);
			contactJson.addProperty("id", KeyFactory.keyToString(contactKey));
			contactJson.addProperty("fname",contact.getFirstName());
			contactJson.addProperty("lname", contact.getLastName());
			
			JsonArray contactNumsJson = new JsonArray();
			
			PCPhone p = (PCPhone)pm.getObjectById(PCPhone.class, contact.getPhone());
			JsonPrimitive prim  = new JsonPrimitive(p.getPhoneNumber());
			contactNumsJson.add(prim);
			
			contactJson.add("nums", contactNumsJson);
			inactiveArray.add(contactJson);
		}
		detailsJson.add("inactive_cts", inactiveArray);
		
		//Obtaining and parsing active contacts
		JsonArray activeArray = new JsonArray();
		
		for(Key contactKey : smartphone.getActiveContacts()){
			JsonObject contactJson = new JsonObject();
			PCSimpleContact contact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, contactKey);
			contactJson.addProperty("id", KeyFactory.keyToString(contactKey));
			contactJson.addProperty("fname",contact.getFirstName());
			contactJson.addProperty("lname", contact.getLastName());
			
			JsonArray contactNumsJson = new JsonArray();
			
			PCPhone p = (PCPhone)pm.getObjectById(PCPhone.class, contact.getPhone());
			JsonPrimitive prim  = new JsonPrimitive(p.getPhoneNumber());
			contactNumsJson.add(prim);
			
			contactJson.add("nums", contactNumsJson);
			inactiveArray.add(contactJson);
		}
		detailsJson.add("active_cts", activeArray);
		
		//Obtaining and parsing added emergency numbers
		JsonArray emergencyArray = new JsonArray();
		
		for(Key contactKey : smartphone.getAddedEmergencyNumbers()){
			JsonObject contactJson = new JsonObject();
			PCEmergencyNumber contact = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, contactKey);
			contactJson.addProperty("id", KeyFactory.keyToString(contactKey));
			contactJson.addProperty("ctry",contact.getCountry());
			contactJson.addProperty("desc", contact.getDescription());
			contactJson.addProperty("num", contact.getNumber().getNumber());
			
			emergencyArray.add(contactJson);
		}
		detailsJson.add("added_em_nums", emergencyArray);
		
		//Obtaining and parsing deleted emergency numbers
		JsonArray emergencyDeletedArray = new JsonArray();
		
		for(Key contactKey : smartphone.getDeletedEmergencyNumbers()){
			JsonObject contactJson = new JsonObject();
			PCEmergencyNumber contact = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, contactKey);
			contactJson.addProperty("id", KeyFactory.keyToString(contactKey));
			contactJson.addProperty("ctry",contact.getCountry());
			contactJson.addProperty("desc", contact.getDescription());
			contactJson.addProperty("num", contact.getNumber().getNumber());
			
			emergencyArray.add(contactJson);
		}
		detailsJson.add("del_em_nums", emergencyDeletedArray);
		
		//Obtaining and parsing properties
		JsonArray propsArray = new JsonArray();
		
		if (smartphone.getProperties() != null) {
			for (PCProperty property : smartphone.getProperties()) {
				JsonObject propertyJson = new JsonObject();

				propertyJson.addProperty("id", KeyFactory.keyToString(property.getKey()));
				propertyJson.addProperty("prop_id", property.getId());
				propertyJson.addProperty("desc", property.getDescription());
				propertyJson.addProperty("value", property.getValue());

				propsArray.add(propertyJson);
			}
		}
		detailsJson.add("props", propsArray);

		//Obtaining and parsing rules
		JsonArray rulesArray = new JsonArray();
		
		if (smartphone.getRules() != null) {
			for (PCRule rule : smartphone.getRules()) {
				JsonObject ruleJson = new JsonObject();

				ruleJson.addProperty("id", KeyFactory.keyToString(rule.getKey()));
				ruleJson.addProperty("init", rule.getStartDate().toString());
				ruleJson.addProperty("end", rule.getEndDate().toString());

				JsonArray funcArray = new JsonArray();
				for (PCFunctionality func : rule.getDisabledFunctionalities()) {
					funcArray.add(new JsonPrimitive(func.getId()));
				}
				ruleJson.add("func", funcArray);
				rulesArray.add(ruleJson);
			}
		}
		detailsJson.add("rules", rulesArray);

		return detailsJson;
	}
}
