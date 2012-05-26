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
import com.ing3nia.parentalcontrol.client.models.NotificationModel;
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
		
		logger.info("Looking in cache for alerts" + smid
				+ SmartphoneCacheParams.ALERTS);
		ident = syncCache.getIdentifiable(smid
				+ SmartphoneCacheParams.ALERTS);
		
		if (smphCacheModel != null && ident != null) {

			ArrayList<NotificationModel> notifications = (ArrayList<NotificationModel>) ident
					.getValue();

			for (NotificationModel alertModel : notifications) {
				JsonObject alert = new JsonObject();
				alert.addProperty("type", alertModel.getType());
				alert.addProperty("date",
						formatter.format(alertModel.getDate()));
				alertsJson.add(alert);

			}
			smpJson.add("alerts", alertsJson);		
		}else{
			ArrayList<PCNotification> pcNotificationList = new ArrayList<PCNotification>();
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
		
		if(smphCacheModel != null && smphCacheModel.getLocation() != null){
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
		
		
		ArrayList<Key> activeKeys = smartphone.getActiveContacts();
		//Query q= pm.newQuery("select from " + PCSimpleContact.class.getName() + " where :keys.contains(key)");
	    //Query q = pm.newQuery("select from " + PCSimpleContact.class.getName() + " where key == :keys");
		//List<PCSimpleContact> simpleContacts = (List<PCSimpleContact>) q.execute(activeKeys);
		List<Object> keyObjectList = new ArrayList<Object>();
		for(Key key : activeKeys){
			keyObjectList.add(pm.newObjectIdInstance(PCSimpleContact.class, key));
		}

		List<PCSimpleContact> simpleContacts = (List<PCSimpleContact>) pm.getObjectsById(keyObjectList);

		//List<PCSimpleContact> simpleContacts = (List<PCSimpleContact>) pm.getObjectsById(activeKeys);
		
		//for(Key contactKey : smartphone.getActiveContacts()){
		for(PCSimpleContact contact : simpleContacts){
			JsonObject contactJson = new JsonObject();
			//PCSimpleContact contact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, contactKey);
			//contactJson.addProperty("id", KeyFactory.keyToString(contactKey));
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
}
