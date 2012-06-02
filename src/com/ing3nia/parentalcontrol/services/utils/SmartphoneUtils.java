package com.ing3nia.parentalcontrol.services.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.LocationModel;
import com.ing3nia.parentalcontrol.client.models.NotificationModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RouteModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.child.RegisterSmartphoneResource;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;


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
	
	private static Logger logger = Logger.getLogger(SmartphoneUtils.class.getName());
	
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
	 * Returns smartphone general information as a json object
	 */
	public static JsonObject getSmartphoneGeneralInfoCache(Key pcSmartKey){
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		String smid = KeyFactory.keyToString(pcSmartKey);
		
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    //System.out.println("Looking in cache "+SmartphoneCacheParams.SMARTPHONE+smid);
	    
	    SmartphoneCacheModel smphCacheModel = null;
	    PCSmartphone pcSmart = null;
	    IdentifiableValue ident = syncCache.getIdentifiable(SmartphoneCacheParams.SMARTPHONE+smid);
	    
	    if(ident==null){
	    	logger.info("Smartphone: "+smid+" not in cache. Getting from datastore");
	    	pcSmart = pm.getObjectById(PCSmartphone.class, pcSmartKey);
	    	logger.info("Setting smartphone to cache and continuing");
	    	WriteToCache.writeSmartphoneToCache(pcSmart);
	    }else{
	    	smphCacheModel = (SmartphoneCacheModel) ident.getValue();
	    	logger.info("Smartphone: "+smid+" found in cache");
	    }
	    
		JsonObject smpJson = new JsonObject();
		smpJson.addProperty("keyId", KeyFactory.keyToString(pcSmartKey));
		
		if(smphCacheModel != null){
			smpJson.addProperty("name",smphCacheModel.getName());
		}else{
			smpJson.addProperty("name", pcSmart.getName());
		}
		
		if(smphCacheModel != null){
			JsonObject deviceJson = new JsonObject();
			deviceJson.addProperty("model", smphCacheModel.getDevice().getModel());
			deviceJson.addProperty("version", smphCacheModel.getDevice().getVersion());
			deviceJson.addProperty("type", smphCacheModel.getDevice().getType());
			smpJson.add("device", deviceJson);
		}else{
			JsonObject deviceJson = new JsonObject();
			PCDevice device = pm.getObjectById(PCDevice.class, pcSmart.getDevice());
			deviceJson.addProperty("model", device.getModel());
			deviceJson.addProperty("version", device.getVersion());
			deviceJson.addProperty("type", device.getOs().getId());
			smpJson.add("device", deviceJson);
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		JsonArray alertsJson = new JsonArray();
		
		logger.info("Looking in cache for alerts: " + smid+ SmartphoneCacheParams.ALERTS);
		ident = syncCache.getIdentifiable(smid + SmartphoneCacheParams.ALERTS);
		
		if (smphCacheModel != null && ident != null) {

			ArrayList<NotificationModel> notifications = (ArrayList<NotificationModel>) ident
					.getValue();

			logger.info("Alerts found. Iterating through: "+notifications.size()+" alerts");
			for (NotificationModel alertModel : notifications) {
				JsonObject alert = new JsonObject();
				alert.addProperty("type", alertModel.getType());
				
				
				/*
				String formattedDate;
				try{
					formattedDate = formatter.format(alertModel.getDate());
				}catch(Exception e){
					formattedDate = "00/00/0000 00:00:00 AM";
					logger.warning("Alert Date could not be formatted properly: "+alertModel.getDate());
				}*/
				alert.addProperty("date",alertModel.getDate());
				
				alertsJson.add(alert);

			}
			smpJson.add("alerts", alertsJson);		
		} else{
			logger.info("Alerts not found in cache");
			ArrayList<PCNotification> pcNotificationList = new ArrayList<PCNotification>();

			if (pcSmart == null) {
				logger.info("PCSmartphone not present. Getting from datastore");
				pcSmart = pm.getObjectById(PCSmartphone.class, pcSmartKey);

				if (smphCacheModel == null) {
					logger.info("Cache model not getted before. Setting smartphone to cache and continuing");
					WriteToCache.writeSmartphoneToCache(pcSmart);
				}
			}
			
			for(Key notKey : pcSmart.getNotifications()){
				PCNotification notif = (PCNotification) pm.getObjectById(PCNotification.class, notKey);
				JsonObject alert = new JsonObject();
				alert.addProperty("type", notif.getType());
				alert.addProperty("date", formatter.format(notif.getDate()));
				alertsJson.add(alert);
				
				pcNotificationList.add(notif);
			}
			smpJson.add("alerts", alertsJson);
			logger.info("Writing smartphone alerts to cache");
			WriteToCache.writeSmartphoneAlertsToCache(pcSmart.getKey(), pcNotificationList);
		}

		
		JsonObject locJson = new JsonObject();
		
		if(smphCacheModel != null){
			locJson.addProperty("latitude", smphCacheModel.getLocation().getLatitude());
			locJson.addProperty("longitude", smphCacheModel.getLocation().getLongitude());
		}else{
			locJson.addProperty("latitude", pcSmart.getLocation().getLatitude());
			locJson.addProperty("longitude", pcSmart.getLocation().getLongitude());			
		}
		
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
	 * Returns a json array containing the general information of the children smartphones 
	 * corresponding to the given user, getting info from cache
	 */
	public static JsonArray getChildrenSmartphonesInfoCache(PCUser user){
		JsonArray smpList = new JsonArray();
		
		for(Key smartphoneKey : user.getSmartphones()){
			JsonObject smpGrlInfo = getSmartphoneGeneralInfoCache(smartphoneKey);
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
		
	
		//Query q= pm.newQuery("select from " + PCSimpleContact.class.getName() + " where :keys.contains(key)");
	    //Query q = pm.newQuery("select from " + PCSimpleContact.class.getName() + " where key == :keys");
		//List<PCSimpleContact> simpleContacts = (List<PCSimpleContact>) q.execute(activeKeys);

		//List<PCSimpleContact> simpleContacts = (List<PCSimpleContact>) pm.getObjectsById(activeKeys);
		
		for (Key contactKey : smartphone.getActiveContacts()) {
			JsonObject contactJson = new JsonObject();
			PCSimpleContact contact = (PCSimpleContact) pm.getObjectById(
					PCSimpleContact.class, contactKey);

			// PCSimpleContact contact =
			// (PCSimpleContact)pm.getObjectById(PCSimpleContact.class,
			// contactKey);
			// contactJson.addProperty("id", KeyFactory.keyToString(contactKey));
			contactJson.addProperty("id", KeyFactory.keyToString(contact.getKey()));
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
			contactJson.addProperty("number", contact.getNumber());
			
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
			contactJson.addProperty("number", contact.getNumber());
			
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
		String ruleKey;
		
		if (smartphone.getRules() != null) {
			for (Key rule : smartphone.getRules()) {
				auxRule = pm.getObjectById(PCRule.class, rule);
				JsonObject ruleJson = new JsonObject();

				ruleKey = KeyFactory.keyToString(rule);
				ruleJson.addProperty("keyId", ruleKey);
				ruleJson.addProperty("startDate", formatter.format(auxRule.getStartDate()));
				ruleJson.addProperty("endDate", formatter.format(auxRule.getEndDate()));
				ruleJson.addProperty("name", auxRule.getName());
				ruleJson.addProperty("type", auxRule.getType());

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
		
		
		JsonArray routesArray = new JsonArray();
		PCRoute auxRoute;
		String routeKey;
		if(smartphone.getRoutes() != null){
			for(Key route : smartphone.getRoutes()){
				
				auxRoute = pm.getObjectById(PCRoute.class, route);
				
				JsonArray points = new JsonArray();
				ArrayList<GeoPt> routePoints = auxRoute.getRoute();
				for(GeoPt geopt : routePoints){
					JsonObject locationObject = new JsonObject();
					locationObject.addProperty("latitude", String.valueOf(geopt.getLatitude()));
					locationObject.addProperty("longitude", String.valueOf(geopt.getLongitude()));
					points.add(locationObject);
				}	
				JsonObject routeJson = new JsonObject();
				
				routeKey = KeyFactory.keyToString(route);
				routeJson.addProperty("keyId", routeKey);
				routeJson.add("points", points);
				routesArray.add(routeJson);
			}
		}
		detailsJson.add("routes", routesArray);

		return detailsJson;
	}
	
	/**
	 * Returns a json array containing detailed information about a given smartphone. 'smartphone' can be null
	 */
	public static JsonObject getSmartphoneDetailsCache(String smid, PCSmartphone smartphone){
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

		JsonObject detailsJson = new JsonObject();
		
		//Obtaining and parsing inactive contacts
		JsonArray inactiveArray = new JsonArray();
		
		logger.info("[Smartphone Details - Cache] Getting inactive contacts");
		ArrayList<SimpleContactModel> inactiveContacts = null;

		IdentifiableValue ident = syncCache.getIdentifiable(smid
				+ SmartphoneCacheParams.INACTIVE_CONTACTS);
		if (ident == null) {
			logger.info("[Smartphone Details - Cache] Inactive contacts: " + smid
					+ " not in cache. Getting smartphone from datastore");
			if (smartphone == null) {
				Key pcSmartKey = KeyFactory.stringToKey(smid);
				smartphone = pm.getObjectById(PCSmartphone.class, pcSmartKey);
				logger.info("[Smartphone Details - Cache] Setting smartphone to cache and continuing");
				WriteToCache.writeSmartphoneToCache(smartphone);
			}
			
			logger.info("[Smartphone Details - Cache] Inactive contacts will be obtained from datastore and set to cache");

		} else {
			inactiveContacts = (ArrayList<SimpleContactModel>) ident.getValue();
			logger.info("[Smartphone Details - Cache] Inactive contacts: " + smid
					+ " found in cache. Size: " + inactiveContacts.size());
		}

		if (inactiveContacts != null) {
			logger.info("[Smartphone Details - Cache] Iterating cache inactive contacts");
			for(SimpleContactModel simpleContact : inactiveContacts){
				JsonObject contactJson = new JsonObject();
				contactJson.addProperty("id", simpleContact.getKeyId());
				contactJson.addProperty("fname", simpleContact.getFirstName());
				contactJson.addProperty("lname", simpleContact.getLastName());

				JsonArray contactNumsJson = new JsonArray();

				if (simpleContact.getPhones().size() > 0) {
					PhoneModel phone = simpleContact.getPhones().get(0);
					JsonObject phoneJson = new JsonObject();
					phoneJson.addProperty("type", phone.getType());
					phoneJson.addProperty("phone", phone.getPhoneNumber());
					contactNumsJson.add(phoneJson);
				}else{
					JsonObject phoneJson = new JsonObject();
					phoneJson.addProperty("type", "0");
					phoneJson.addProperty("phone", "-");
					contactNumsJson.add(phoneJson);
				}

				contactJson.add("num", contactNumsJson);
				inactiveArray.add(contactJson);
			}
			
		} else {
			logger.info("[Smartphone Details - Cache] Obtaining inactive contacts from datastore");
			ArrayList<PCSimpleContact> inactiveContactsDSList = new ArrayList<PCSimpleContact>();
			ArrayList<PCPhone> inactiveContactsPhoneList = new ArrayList<PCPhone>();
			for (Key contactKey : smartphone.getInactiveContacts()) {
				JsonObject contactJson = new JsonObject();
				PCSimpleContact contact = (PCSimpleContact) pm.getObjectById(
						PCSimpleContact.class, contactKey);
				inactiveContactsDSList.add(contact);
				
				contactJson.addProperty("id",
						KeyFactory.keyToString(contactKey));
				contactJson.addProperty("fname", contact.getFirstName());
				contactJson.addProperty("lname", contact.getLastName());

				JsonArray contactNumsJson = new JsonArray();

				PCPhone p = (PCPhone) pm.getObjectById(PCPhone.class,
						contact.getPhone());
				inactiveContactsPhoneList.add(p);
				
				JsonObject phoneJson = new JsonObject();
				phoneJson.addProperty("type", p.getType());
				phoneJson.addProperty("phone", p.getPhoneNumber());
				contactNumsJson.add(phoneJson);

				contactJson.add("num", contactNumsJson);
				inactiveArray.add(contactJson);
			}
			logger.info("[Smartphone Details - Cache] Writing to cache inactive contact list");
			WriteToCache.writeSmartphoneInactiveContactsToCache(smid, inactiveContactsDSList, inactiveContactsPhoneList);
		}

		detailsJson.add("inactive_cts", inactiveArray);
		
		//Obtaining and parsing active contacts
		JsonArray activeArray = new JsonArray();
		
		logger.info("[Smartphone Details - Cache] Getting Active contacts");
		ArrayList<SimpleContactModel> activeContacts = null;

		ident = syncCache.getIdentifiable(smid+ SmartphoneCacheParams.ACTIVE_CONTACTS);
		if (ident == null) {
			logger.info("[Smartphone Details - Cache] Active contacts: " + smid
					+ " not in cache.");
			
			if (smartphone == null) {
				Key pcSmartKey = KeyFactory.stringToKey(smid);
				smartphone = pm.getObjectById(PCSmartphone.class, pcSmartKey);
				logger.info("[Smartphone Details - Cache] Setting smartphone to cache and continuing");
				WriteToCache.writeSmartphoneToCache(smartphone);
			}
			logger.info("[Smartphone Details - Cache] Active contacts will be obtained from datastore and set to cache");

		} else {
			activeContacts = (ArrayList<SimpleContactModel>) ident.getValue();
			logger.info("[Smartphone Details - Cache] Active contacts: " + smid
					+ " found in cache. Size: " + activeContacts.size());
		}
		
		if (activeContacts != null) {
			logger.info("[Smartphone Details - Cache] Iterating cache active contacts");
			for(SimpleContactModel simpleContact : activeContacts){
				JsonObject contactJson = new JsonObject();
				contactJson.addProperty("id", simpleContact.getKeyId());
				contactJson.addProperty("fname", simpleContact.getFirstName());
				contactJson.addProperty("lname", simpleContact.getLastName());

				JsonArray contactNumsJson = new JsonArray();

				if (simpleContact.getPhones().size() > 0) {
					PhoneModel phone = simpleContact.getPhones().get(0);
					JsonObject phoneJson = new JsonObject();
					phoneJson.addProperty("type", phone.getType());
					phoneJson.addProperty("phone", phone.getPhoneNumber());
					contactNumsJson.add(phoneJson);
				}else{
					JsonObject phoneJson = new JsonObject();
					phoneJson.addProperty("type", "0");
					phoneJson.addProperty("phone", "-");
					contactNumsJson.add(phoneJson);
				}

				contactJson.add("num", contactNumsJson);
				activeArray.add(contactJson);
			}
			
		} else {
			logger.info("[Smartphone Details - Cache] Obtaining Active contacts from datastore");
			ArrayList<PCSimpleContact> activeContactsDSList = new ArrayList<PCSimpleContact>();
			ArrayList<PCPhone> activeContactsPhoneList = new ArrayList<PCPhone>();

			// for(Key contactKey : smartphone.getActiveContacts()){
			 for (Key contactKey : smartphone.getActiveContacts()) {
				PCSimpleContact contact = (PCSimpleContact) pm.getObjectById(
						PCSimpleContact.class, contactKey);
				activeContactsDSList.add(contact);
				
				JsonObject contactJson = new JsonObject();
				// PCSimpleContact contact =
				// (PCSimpleContact)pm.getObjectById(PCSimpleContact.class,
				// contactKey);
				// contactJson.addProperty("id",
				// KeyFactory.keyToString(contactKey));
				contactJson.addProperty("id",
						KeyFactory.keyToString(contact.getKey()));
				contactJson.addProperty("fname", contact.getFirstName());
				contactJson.addProperty("lname", contact.getLastName());

				JsonArray contactNumsJson = new JsonArray();

				PCPhone p = (PCPhone) pm.getObjectById(PCPhone.class,
						contact.getPhone());
				activeContactsPhoneList.add(p);
				
				JsonObject phoneJson = new JsonObject();
				phoneJson.addProperty("type", p.getType());
				phoneJson.addProperty("phone", p.getPhoneNumber());
				contactNumsJson.add(phoneJson);

				contactJson.add("num", contactNumsJson);
				activeArray.add(contactJson);
				
			}
				logger.info("[Smartphone Details - Cache] Writing to cache active contact list");
				WriteToCache.writeSmartphoneActiveContactsToCache(smid, activeContactsDSList, activeContactsPhoneList);
		}
		detailsJson.add("active_cts", activeArray);
		
		//Obtaining and parsing added emergency numbers
		JsonArray emergencyArray = new JsonArray();
		
		/*
		if(smartphone==null){ /////// CAMBIAR
			Key pcSmartKey = KeyFactory.stringToKey(smid);
			smartphone = pm.getObjectById(PCSmartphone.class, pcSmartKey);
		}
		*/
		
		logger.info("[Smartphone Details - Cache] Getting Added Emergency contacts");
		ArrayList<EmergencyNumberModel> addedEmergency = null;

		ident = syncCache.getIdentifiable(smid+ SmartphoneCacheParams.ADD_EMERGENCY_NUMBERS);
		if (ident == null) {
			logger.info("[Smartphone Details - Cache] Added Emergency: " + smid
					+ " not in cache.");
			
			if (smartphone == null) {
				Key pcSmartKey = KeyFactory.stringToKey(smid);
				smartphone = pm.getObjectById(PCSmartphone.class, pcSmartKey);
				logger.info("[Smartphone Details - Cache] Setting smartphone to cache and continuing");
				WriteToCache.writeSmartphoneToCache(smartphone);
			}
			logger.info("[Smartphone Details - Cache] Added Emergency will be obtained from datastore and set to cache");

		} else {
			addedEmergency = (ArrayList<EmergencyNumberModel>) ident.getValue();
			logger.info("[Smartphone Details - Cache] Added Emergency contacts: " + smid
					+ " found in cache. Size: " + addedEmergency.size());
		}
		
		if (addedEmergency != null) {
			logger.info("[Smartphone Details - Cache] Iterating added emergency");
			for (EmergencyNumberModel emergencyModel : addedEmergency) {
				JsonObject contactJson = new JsonObject();
				contactJson.addProperty("keyId", emergencyModel.getKeyId());
				contactJson.addProperty("country", emergencyModel.getCountry());
				contactJson.addProperty("description",emergencyModel.getDescription());
				contactJson.addProperty("number", emergencyModel.getNumber());

				emergencyArray.add(contactJson);
			}
		} else {
			logger.info("[Smartphone Details - Cache] Obtaining Added Emergency contacts from datastore");
			ArrayList<PCEmergencyNumber> addedEmergencyDS = new ArrayList<PCEmergencyNumber>();
			
			for (Key contactKey : smartphone.getAddedEmergencyNumbers()) {
				JsonObject contactJson = new JsonObject();
				PCEmergencyNumber contact = (PCEmergencyNumber) pm
						.getObjectById(PCEmergencyNumber.class, contactKey);
				addedEmergencyDS.add(contact);
				
				contactJson.addProperty("keyId",
						KeyFactory.keyToString(contactKey));
				contactJson.addProperty("country", contact.getCountry());
				contactJson
						.addProperty("description", contact.getDescription());
				contactJson.addProperty("number", contact.getNumber());

				emergencyArray.add(contactJson);
			}
			logger.info("[Smartphone Details - Cache] Writing to cache Added Emergency List");
			WriteToCache.writeSmartphoneAddedEmergencyNumbersToCache(smid, addedEmergencyDS);
		}
		
		detailsJson.add("addedEmergencyNumbers", emergencyArray);
		
		//Obtaining and parsing deleted emergency numbers
		JsonArray emergencyDeletedArray = new JsonArray();
		
		logger.info("[Smartphone Details - Cache] Getting Deleted Emergency contacts");
		ArrayList<EmergencyNumberModel> deletedEmergency = null;

		ident = syncCache.getIdentifiable(smid+ SmartphoneCacheParams.DELETE_EMERGENCY_NUMBERS);
		if (ident == null) {
			logger.info("[Smartphone Details - Cache] Deleted Emergency: " + smid
					+ " not in cache.");
			
			if (smartphone == null) {
				Key pcSmartKey = KeyFactory.stringToKey(smid);
				smartphone = pm.getObjectById(PCSmartphone.class, pcSmartKey);
				logger.info("[Smartphone Details - Cache] Setting smartphone to cache and continuing");
				WriteToCache.writeSmartphoneToCache(smartphone);
			}
			logger.info("[Smartphone Details - Cache] Deleted Emergency will be obtained from datastore and set to cache");

		} else {
			deletedEmergency = (ArrayList<EmergencyNumberModel>) ident.getValue();
			logger.info("[Smartphone Details - Cache] Deleted Emergency contacts: " + smid
					+ " found in cache. Size: " + deletedEmergency.size());
		}
		
		if (deletedEmergency != null) {
			logger.info("[Smartphone Details - Cache] Iterating deleted emergency");
			for (EmergencyNumberModel emergencyModel : deletedEmergency) {
				JsonObject contactJson = new JsonObject();
				contactJson.addProperty("keyId", emergencyModel.getKeyId());
				contactJson.addProperty("country", emergencyModel.getCountry());
				contactJson.addProperty("description",emergencyModel.getDescription());
				contactJson.addProperty("number", emergencyModel.getNumber());

				emergencyDeletedArray.add(contactJson);
			}
		} else {
			logger.info("[Smartphone Details - Cache] Obtaining Deleted Emergency contacts from datastore");
			ArrayList<PCEmergencyNumber> deletedEmergencyDS = new ArrayList<PCEmergencyNumber>();
			
			for (Key contactKey : smartphone.getDeletedEmergencyNumbers()) {
				JsonObject contactJson = new JsonObject();
				PCEmergencyNumber contact = (PCEmergencyNumber) pm
						.getObjectById(PCEmergencyNumber.class, contactKey);
				contactJson.addProperty("keyId",
						KeyFactory.keyToString(contactKey));
				contactJson.addProperty("country", contact.getCountry());
				contactJson
						.addProperty("description", contact.getDescription());
				contactJson.addProperty("number", contact.getNumber());

				emergencyDeletedArray.add(contactJson);
			}
			logger.info("[Smartphone Details - Cache] Writing to cache Deleted Emergency List");
			WriteToCache.writeSmartphoneDeletedEmergencyNumbersToCache(smid, deletedEmergencyDS);
		}
		detailsJson.add("deletedEmergencyNumbers", emergencyDeletedArray);
		
		//Obtaining and parsing properties
		JsonArray propsArray = new JsonArray();
		PCProperty auxProp;
		
		ArrayList<PropertyModel> propertyCacheList = null;
		ident = syncCache.getIdentifiable(smid+ SmartphoneCacheParams.PROPERTIES);
		if (ident == null) {
			logger.info("[Smartphone Details - Cache] Properties: " + smid
					+ " not in cache.");
			
			if (smartphone == null) {
				Key pcSmartKey = KeyFactory.stringToKey(smid);
				smartphone = pm.getObjectById(PCSmartphone.class, pcSmartKey);
				logger.info("[Smartphone Details - Cache] Setting smartphone to cache and continuing");
				WriteToCache.writeSmartphoneToCache(smartphone);
			}
			logger.info("[Smartphone Details - Cache] Properties will be obtained from datastore and set to cache");

		} else {
			propertyCacheList = (ArrayList<PropertyModel>) ident.getValue();
			logger.info("[Smartphone Details - Cache] Properties " + smid
					+ " found in cache. Size: " + propertyCacheList.size());
		}
		
		if (propertyCacheList != null) {
			logger.info("[Smartphone Details - Cache] Iterating Properties");
			
			for(PropertyModel propertyModel : propertyCacheList){
				JsonObject propertyJson = new JsonObject();

				propertyJson.addProperty("keyId",propertyModel.getKeyId());
				propertyJson.addProperty("id", propertyModel.getId());
				propertyJson.addProperty("description", propertyModel.getDescription());
				propertyJson.addProperty("value", propertyModel.getValue());

				propsArray.add(propertyJson);				
			}
			
		} else {
			logger.info("[Smartphone Details - Cache] Obtaining Properties from datastore");
			ArrayList<PCProperty> propertiesDS = new ArrayList<PCProperty>();
			if (smartphone.getProperties() != null) {
				for (Key property : smartphone.getProperties()) {
					auxProp = pm.getObjectById(PCProperty.class, property);
					propertiesDS.add(auxProp);
					
					JsonObject propertyJson = new JsonObject();

					propertyJson.addProperty("keyId",
							KeyFactory.keyToString(property));
					propertyJson.addProperty("id", auxProp.getId());
					propertyJson.addProperty("description",
							auxProp.getDescription());
					propertyJson.addProperty("value", auxProp.getValue());

					propsArray.add(propertyJson);
				}
			}
			logger.info("[Smartphone Details - Cache] Writing to cache Property List List");
			WriteToCache.writeSmartphonePropertiesToCache(smid, propertiesDS);
		}
		detailsJson.add("properties", propsArray);

		//Obtaining and parsing rules
		JsonArray rulesArray = new JsonArray();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		PCRule auxRule;
		String ruleKey;
		
		
		ArrayList<RuleModel> rulesModelList = null;
		ident = syncCache.getIdentifiable(smid+ SmartphoneCacheParams.RULES);
		if (ident == null) {
			logger.info("[Smartphone Details - Cache] Rules: " + smid
					+ " not in cache.");
			
			if (smartphone == null) {
				Key pcSmartKey = KeyFactory.stringToKey(smid);
				smartphone = pm.getObjectById(PCSmartphone.class, pcSmartKey);
				logger.info("[Smartphone Details - Cache] Setting smartphone to cache and continuing");
				WriteToCache.writeSmartphoneToCache(smartphone);
			}
			logger.info("[Smartphone Details - Cache] Rules will be obtained from datastore and set to cache");

		} else {
			rulesModelList = (ArrayList<RuleModel>) ident.getValue();
			logger.info("[Smartphone Details - Cache] Rules " + smid
					+ " found in cache. Size: " + rulesModelList.size());
		}

		if (rulesModelList != null) {
			logger.info("[Smartphone Details - Cache] Iterating Rules");
			
			for(RuleModel ruleModel : rulesModelList){
				JsonObject ruleJson = new JsonObject();

				ruleJson.addProperty("keyId", ruleModel.getKeyId());
				ruleJson.addProperty("startDate", ruleModel.getStartDate());
				ruleJson.addProperty("endDate", ruleModel.getEndDate());
				ruleJson.addProperty("name", ruleModel.getName());
				ruleJson.addProperty("type", ruleModel.getType());

				JsonArray funcArray = new JsonArray();
				// ArrayList<PCFunctionality> disabledFuncionalities =
				// (ArrayList<PCFunctionality>)pm.getObjectsById(auxRule.getDisabledFunctionalities());
				PCFunctionality auxFunc;
				
				for(Integer funcId: ruleModel.getDisabledFunctionalities()){
					funcArray.add(new JsonPrimitive(funcId));
				}

				ruleJson.add("disabledFunctionalities", funcArray);
				rulesArray.add(ruleJson);
			}
			
		} else {
			logger.info("[Smartphone Details - Cache] Obtaining Rules from datastore");
			ArrayList<PCRule> rulesDS = new ArrayList<PCRule>();
			ArrayList<ArrayList<Integer>> disabledFuncionalitiesDS = new ArrayList<ArrayList<Integer>>();
			
			if (smartphone.getRules() != null) {
				for (Key rule : smartphone.getRules()) {
					auxRule = pm.getObjectById(PCRule.class, rule);
					rulesDS.add(auxRule);
					
					JsonObject ruleJson = new JsonObject();

					ruleKey = KeyFactory.keyToString(rule);
					ruleJson.addProperty("keyId", ruleKey);
					ruleJson.addProperty("startDate",
							formatter.format(auxRule.getStartDate()));
					ruleJson.addProperty("endDate",
							formatter.format(auxRule.getEndDate()));
					ruleJson.addProperty("name", auxRule.getName());
					ruleJson.addProperty("type", auxRule.getType());

					JsonArray funcArray = new JsonArray();
					// ArrayList<PCFunctionality> disabledFuncionalities =
					// (ArrayList<PCFunctionality>)pm.getObjectsById(auxRule.getDisabledFunctionalities());
					PCFunctionality auxFunc;
					
					ArrayList<Integer> ruleDisableFuncDS = new ArrayList<Integer>();
					for (Key func : auxRule.getDisabledFunctionalities()) {
						auxFunc = pm.getObjectById(PCFunctionality.class, func);
						ruleDisableFuncDS.add(auxFunc.getId());
						
						funcArray.add(new JsonPrimitive(auxFunc.getId()));
					}
					
					disabledFuncionalitiesDS.add(ruleDisableFuncDS);
					
					ruleJson.add("disabledFunctionalities", funcArray);
					rulesArray.add(ruleJson);
				}
				logger.info("[Smartphone Details - Cache] Writing to cache Rule List");
				WriteToCache.writeSmartphoneRulesToCache(smid, rulesDS, disabledFuncionalitiesDS);
			}
		}
		detailsJson.add("rules", rulesArray);
		
		
		
		JsonArray routesArray = new JsonArray();
		PCRoute auxRoute;
		String routeKey;
		
		ArrayList<RouteModel> routeModelList = null;
		ident = syncCache.getIdentifiable(smid+ SmartphoneCacheParams.ROUTES);
		if (ident == null) {
			logger.info("[Smartphone Details - Cache] Routes: " + smid
					+ " not in cache.");
			
			if (smartphone == null) {
				Key pcSmartKey = KeyFactory.stringToKey(smid);
				smartphone = pm.getObjectById(PCSmartphone.class, pcSmartKey);
				logger.info("[Smartphone Details - Cache] Setting smartphone to cache and continuing");
				WriteToCache.writeSmartphoneToCache(smartphone);
			}
			logger.info("[Smartphone Details - Cache] Routes will be obtained from datastore and set to cache");

		} else {
			routeModelList = (ArrayList<RouteModel>) ident.getValue();
			logger.info("[Smartphone Details - Cache] Routes " + smid
					+ " found in cache. Size: " + routeModelList.size());
		}
		
		if (routeModelList != null) {
			logger.info("[Smartphone Details - Cache] Iterating Routes from cache");			
			for(RouteModel route : routeModelList){
				
				JsonArray points = new JsonArray();

				for (LocationModel location : route.getPoints()) {
					JsonObject locationObject = new JsonObject();
					locationObject.addProperty("latitude",
							String.valueOf(location.getLatitude()));
					locationObject.addProperty("longitude",
							String.valueOf(location.getLongitude()));
					points.add(locationObject);
				}
				JsonObject routeJson = new JsonObject();

				routeJson.addProperty("keyId", route.getKeyId());
				routeJson.add("points", points);
				routesArray.add(routeJson);				
			}
		} else {
			logger.info("[Smartphone Details - Cache] Iterating Routes from DS");
			ArrayList<PCRoute> routesDS = new ArrayList<PCRoute>();
			
			if (smartphone.getRoutes() != null
					&& smartphone.getRoutes().size() > 1) {

				// for(Key route : smartphone.getRoutes()){

				// just taking the last route - CHANGEEEEE
				ArrayList<Key> routes = smartphone.getRoutes();
				Key route = routes.get(routes.size() - 1);

				auxRoute = pm.getObjectById(PCRoute.class, route);
				routesDS.add(auxRoute);

				JsonArray points = new JsonArray();
				ArrayList<GeoPt> routePoints = auxRoute.getRoute();
				for (GeoPt geopt : routePoints) {
					JsonObject locationObject = new JsonObject();
					locationObject.addProperty("latitude",
							String.valueOf(geopt.getLatitude()));
					locationObject.addProperty("longitude",
							String.valueOf(geopt.getLongitude()));
					points.add(locationObject);
				}
				JsonObject routeJson = new JsonObject();

				routeKey = KeyFactory.keyToString(route);
				routeJson.addProperty("keyId", routeKey);
				routeJson.add("points", points);
				routesArray.add(routeJson);
				
				logger.info("[Smartphone Details - Cache] Writing to Cache Route List");
				WriteToCache.writeSmartphoneRoutesToCache(smid, routesDS);
			}else{
				logger.info("[Smartphone Details - Cache] Writing empty route to cache");
				WriteToCache.writeSmartphoneRoutesToCache(smid, routesDS);
			}
		}
		detailsJson.add("routes", routesArray);

		return detailsJson;
	}
	
}
