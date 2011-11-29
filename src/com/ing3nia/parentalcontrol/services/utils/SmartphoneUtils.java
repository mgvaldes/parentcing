package com.ing3nia.parentalcontrol.services.utils;

import java.text.SimpleDateFormat;
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
	public static JsonObject getSmartphoneGeneralInfo(Key pcSmartKey){
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		PCSmartphone pcSmart = pm.getObjectById(PCSmartphone.class, pcSmartKey);
		
		JsonObject smpJson = new JsonObject();
		smpJson.addProperty("keyId", KeyFactory.keyToString(pcSmartKey));
		smpJson.addProperty("name",pcSmart.getName());
		
		JsonObject deviceJson = new JsonObject();
		PCDevice device = pm.getObjectById(PCDevice.class, pcSmart.getDevice());
		deviceJson.addProperty("model", device.getModel());
		deviceJson.addProperty("version", device.getVersion());
		deviceJson.addProperty("type", device.getOs().getId());
		smpJson.add("device", deviceJson);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		JsonArray alertsJson = new JsonArray();
		for(Key notKey : pcSmart.getNotifications()){
			PCNotification notif = (PCNotification) pm.getObjectById(PCNotification.class, notKey);
			JsonObject alert = new JsonObject();
			alert.addProperty("type", notif.getType());
			alert.addProperty("date", formatter.format(notif.getDate()));
			alertsJson.add(alert);
		}
		smpJson.add("alerts", alertsJson);
		
		JsonObject locJson = new JsonObject();
		locJson.addProperty("latitude", pcSmart.getLocation().getLatitude());
		locJson.addProperty("longitude", pcSmart.getLocation().getLongitude());
		
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
		
		for(Key smartphoneKey : user.getSmartphones()){
			JsonObject smpGrlInfo = getSmartphoneGeneralInfo(smartphoneKey);
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
			JsonObject phoneJson = new JsonObject();
			phoneJson.addProperty("type", p.getType());
			phoneJson.addProperty("phone", p.getPhoneNumber());
			contactNumsJson.add(phoneJson);
			
			contactJson.add("num", contactNumsJson);
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
			JsonObject phoneJson = new JsonObject();
			phoneJson.addProperty("type", p.getType());
			phoneJson.addProperty("phone", p.getPhoneNumber());
			contactNumsJson.add(phoneJson);
			
			contactJson.add("num", contactNumsJson);
			activeArray.add(contactJson);
		}
		detailsJson.add("active_cts", activeArray);
		
		//Obtaining and parsing added emergency numbers
		JsonArray emergencyArray = new JsonArray();
		
		for(Key contactKey : smartphone.getAddedEmergencyNumbers()){
			JsonObject contactJson = new JsonObject();
			PCEmergencyNumber contact = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, contactKey);
			contactJson.addProperty("keyId", KeyFactory.keyToString(contactKey));
			contactJson.addProperty("country",contact.getCountry());
			contactJson.addProperty("description", contact.getDescription());
			contactJson.addProperty("number", contact.getNumber().getNumber());
			
			emergencyArray.add(contactJson);
		}
		detailsJson.add("addedEmergencyNumbers", emergencyArray);
		
		//Obtaining and parsing deleted emergency numbers
		JsonArray emergencyDeletedArray = new JsonArray();
		
		for(Key contactKey : smartphone.getDeletedEmergencyNumbers()){
			JsonObject contactJson = new JsonObject();
			PCEmergencyNumber contact = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, contactKey);
			contactJson.addProperty("keyId", KeyFactory.keyToString(contactKey));
			contactJson.addProperty("country",contact.getCountry());
			contactJson.addProperty("description", contact.getDescription());
			contactJson.addProperty("number", contact.getNumber().getNumber());
			
			emergencyDeletedArray.add(contactJson);
		}
		detailsJson.add("deletedEmergencyNumbers", emergencyDeletedArray);
		
		//Obtaining and parsing properties
		JsonArray propsArray = new JsonArray();
		PCProperty auxProp;
		
		if (smartphone.getProperties() != null) {
			for (Key property : smartphone.getProperties()) {
				auxProp = pm.getObjectById(PCProperty.class, property);
				JsonObject propertyJson = new JsonObject();

				propertyJson.addProperty("keyId", KeyFactory.keyToString(property));
				propertyJson.addProperty("id", auxProp.getId());
				propertyJson.addProperty("description", auxProp.getDescription());
				propertyJson.addProperty("value", auxProp.getValue());

				propsArray.add(propertyJson);
			}
		}
		detailsJson.add("properties", propsArray);

		//Obtaining and parsing rules
		JsonArray rulesArray = new JsonArray();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		PCRule auxRule;
		
		if (smartphone.getRules() != null) {
			for (Key rule : smartphone.getRules()) {
				auxRule = pm.getObjectById(PCRule.class, rule);
				JsonObject ruleJson = new JsonObject();

				ruleJson.addProperty("keyId", KeyFactory.keyToString(rule));
				ruleJson.addProperty("startDate", formatter.format(auxRule.getStartDate()));
				ruleJson.addProperty("endDate", formatter.format(auxRule.getEndDate()));
				ruleJson.addProperty("name", auxRule.getName());

				JsonArray funcArray = new JsonArray();
				//ArrayList<PCFunctionality> disabledFuncionalities = (ArrayList<PCFunctionality>)pm.getObjectsById(auxRule.getDisabledFunctionalities());
				PCFunctionality auxFunc;
				for (Key func : auxRule.getDisabledFunctionalities()) {
					auxFunc = pm.getObjectById(PCFunctionality.class, func);
					funcArray.add(new JsonPrimitive(auxFunc.getId()));
				}
				ruleJson.add("disabledFunctionalities", funcArray);
				rulesArray.add(ruleJson);
			}
		}
		detailsJson.add("rules", rulesArray);

		return detailsJson;
	}
}
