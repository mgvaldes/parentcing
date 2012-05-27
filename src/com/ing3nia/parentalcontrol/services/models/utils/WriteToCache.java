package com.ing3nia.parentalcontrol.services.models.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.ing3nia.parentalcontrol.client.models.DeviceModel;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.LocationModel;
import com.ing3nia.parentalcontrol.client.models.NotificationModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.RouteModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.parent.ParentSmartphoneDetails;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class WriteToCache {

	private static final Logger logger = Logger
	.getLogger(WriteToCache.class.getName());
	
	public static void writeSmartphoneToCache(PCSmartphone pcSmartphone){
		SmartphoneCacheModel smartphoneCacheModel = new SmartphoneCacheModel();
		String smartphoneKey = KeyFactory.keyToString(pcSmartphone.getKey());
		
		smartphoneCacheModel.setKeyId(smartphoneKey);
		smartphoneCacheModel.setName(pcSmartphone.getName());
		smartphoneCacheModel.setSerialNumber(pcSmartphone.getSerialNumber());
		
		DeviceModel deviceModel = new DeviceModel();
		if(pcSmartphone.getDevice() == null){
			deviceModel.setModel("Default");
			deviceModel.setType(0);
			deviceModel.setVersion("Default");
		}else{
			PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
			PCDevice device = pm.getObjectById(PCDevice.class, pcSmartphone.getDevice());
			deviceModel.setModel(device.getModel());
			deviceModel.setType(0);
			deviceModel.setVersion(device.getVersion());
		}
		smartphoneCacheModel.setDevice(deviceModel);
		

		LocationModel location = new LocationModel();
		if(pcSmartphone.getLocation() == null){
			location.setLatitude("0.0");
			location.setLongitude("0.0");
		}else{
			location.setLatitude(String.valueOf(pcSmartphone.getLocation().getLatitude()));
			location.setLongitude(String.valueOf(pcSmartphone.getLocation().getLongitude()));
		}
		smartphoneCacheModel.setLocation(location);

		logger.info("Setting Smartphone: "+smartphoneKey+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(SmartphoneCacheParams.SMARTPHONE+smartphoneKey, smartphoneCacheModel);		
	}
	
	public static void writeSmartphoneAlertsToCache(Key pcSmartphoneKey, ArrayList<PCNotification> pcAlertList){
		ArrayList<NotificationModel> cacheAlertList = new ArrayList<NotificationModel>();
		String smartphoneKey = KeyFactory.keyToString(pcSmartphoneKey);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		for(PCNotification notification : pcAlertList){
			NotificationModel alertModel = new NotificationModel();
			alertModel.setDate(formatter.format(notification.getDate()));
			alertModel.setType(notification.getType());
			cacheAlertList.add(alertModel);
		}
		
		logger.info("Setting Alert List: "+smartphoneKey+SmartphoneCacheParams.ALERTS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ALERTS,cacheAlertList, null);
	}
	
	public static void writeSmartphoneActiveContactsToCache(String smartphoneKey, ArrayList<PCSimpleContact> pcActiveContactsList, ArrayList<PCPhone> pcActiveContactsPhoneList){
		int count = 0;
		ArrayList<SimpleContactModel> cacheContactList = new ArrayList<SimpleContactModel>();
		for(PCSimpleContact simpleContact : pcActiveContactsList){
			SimpleContactModel simpleModel = new SimpleContactModel();
			simpleModel.setFirstName(simpleContact.getFirstName());
			simpleModel.setLastName(simpleContact.getLastName());
			simpleModel.setKeyId(KeyFactory.keyToString(simpleContact.getKey()));
			
			PCPhone phone = pcActiveContactsPhoneList.get(count);
			PhoneModel phoneModel = new PhoneModel();
			phoneModel.setPhoneNumber(phone.getPhoneNumber());
			phoneModel.setType(phone.getType());
			
			
			ArrayList<PhoneModel> phones = new ArrayList<PhoneModel>();
			phones.add(phoneModel);
			simpleModel.setPhones(phones);
			
			cacheContactList.add(simpleModel);
			count+=1;
		}
		
		logger.info("Setting Active List: "+smartphoneKey+SmartphoneCacheParams.ACTIVE_CONTACTS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ACTIVE_CONTACTS,cacheContactList, null);
	
	}
	
	public static void writeSmartphoneInactiveContactsToCache(String smartphoneKey, ArrayList<PCSimpleContact> pcInactiveContactsList,  ArrayList<PCPhone> pcInactiveContactsPhoneList){
		int count = 0;
		ArrayList<SimpleContactModel> cacheContactList = new ArrayList<SimpleContactModel>();
		for(PCSimpleContact simpleContact : pcInactiveContactsList){
			SimpleContactModel simpleModel = new SimpleContactModel();
			simpleModel.setFirstName(simpleContact.getFirstName());
			simpleModel.setLastName(simpleContact.getLastName());
			simpleModel.setKeyId(KeyFactory.keyToString(simpleContact.getKey()));
			
			PCPhone phone = pcInactiveContactsPhoneList.get(count);
			PhoneModel phoneModel = new PhoneModel();
			phoneModel.setPhoneNumber(phone.getPhoneNumber());
			phoneModel.setType(phone.getType());
			
			ArrayList<PhoneModel> phones = new ArrayList<PhoneModel>();
			phones.add(phoneModel);
			simpleModel.setPhones(phones);
			
			cacheContactList.add(simpleModel);
			count+=1;
		}
		
		logger.info("Setting Inactive List: "+smartphoneKey+SmartphoneCacheParams.INACTIVE_CONTACTS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.INACTIVE_CONTACTS,cacheContactList, null);
		
	
	}
	
	public static void writeSmartphoneAddedEmergencyNumbersToCache(String smartphoneKey, ArrayList<PCEmergencyNumber> pcAddedEmergencyList){
		ArrayList<SimpleContactModel> cacheContactList = new ArrayList<SimpleContactModel>();
		for(PCEmergencyNumber simpleContact : pcAddedEmergencyList){
			EmergencyNumberModel simpleModel = new EmergencyNumberModel();
			simpleModel.setCountry(simpleContact.getCountry());
			simpleModel.setDescription(simpleContact.getDescription());
			simpleModel.setKeyId(KeyFactory.keyToString(simpleContact.getKey()));
			simpleModel.setNumber(simpleContact.getNumber());
		}
		
		logger.info("Setting Added Emergency List: "+smartphoneKey+SmartphoneCacheParams.ADD_EMERGENCY_NUMBERS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ADD_EMERGENCY_NUMBERS,cacheContactList, null);
	}
	
	public static void writeSmartphoneDeletedEmergencyNumbersToCache(String smartphoneKey, ArrayList<PCEmergencyNumber> pcDeletedEmergencyList){
		ArrayList<SimpleContactModel> cacheContactList = new ArrayList<SimpleContactModel>();
		for(PCEmergencyNumber simpleContact : pcDeletedEmergencyList){
			EmergencyNumberModel simpleModel = new EmergencyNumberModel();
			simpleModel.setCountry(simpleContact.getCountry());
			simpleModel.setDescription(simpleContact.getDescription());
			simpleModel.setKeyId(KeyFactory.keyToString(simpleContact.getKey()));
			simpleModel.setNumber(simpleContact.getNumber());
		}
		
		logger.info("Setting Deleted Emergency Numbers List: "+smartphoneKey+SmartphoneCacheParams.DELETE_EMERGENCY_NUMBERS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.DELETE_EMERGENCY_NUMBERS,cacheContactList, null);
	}
	
	public static void writeSmartphoneRoutesToCache(String smartphoneKey, ArrayList<PCRoute> routeList){
		ArrayList<RouteModel> cacheContactList = new ArrayList<RouteModel>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		for(PCRoute route : routeList){
			RouteModel routeModel = new RouteModel();
			try{
				routeModel.setDate(formatter.format(route.getDate()));
			} catch (Exception e){
				logger.warning("Could not parse Date correctly: "+route.getDate());
				routeModel.setDate("00/00/0000 00:00:00 AM");
			}
			routeModel.setKeyId(KeyFactory.keyToString(route.getKey()));
			
			ArrayList<LocationModel> locations = new ArrayList<LocationModel>();
			for(GeoPt point: route.getRoute()){
				LocationModel locationModel = new LocationModel();
				locationModel.setLatitude(String.valueOf(point.getLatitude()));
				locationModel.setLongitude(String.valueOf(point.getLongitude()));
				locations.add(locationModel);
			}
			routeModel.setPoints(locations);
			cacheContactList.add(routeModel);
		}
		
		logger.info("Setting Route List: "+smartphoneKey+SmartphoneCacheParams.ROUTES+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ROUTES,cacheContactList, null);
	}
	
	/*
	public static void writeSmartphonePropertiesToCache(String smartphoneKey, ArrayList<PCRoute> routeList){
		ArrayList<RouteModel> cacheContactList = new ArrayList<RouteModel>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		for(PCRoute route : routeList){
			RouteModel routeModel = new RouteModel();
			try{
				routeModel.setDate(formatter.format(route.getDate()));
			} catch (Exception e){
				logger.warning("Could not parse Date correctly: "+route.getDate());
				routeModel.setDate("00/00/0000 00:00:00 AM");
			}
			routeModel.setKeyId(KeyFactory.keyToString(route.getKey()));
			
			ArrayList<LocationModel> locations = new ArrayList<LocationModel>();
			for(GeoPt point: route.getRoute()){
				LocationModel locationModel = new LocationModel();
				locationModel.setLatitude(String.valueOf(point.getLatitude()));
				locationModel.setLongitude(String.valueOf(point.getLongitude()));
				locations.add(locationModel);
			}
			routeModel.setPoints(locations);
			cacheContactList.add(routeModel);
		}
		
		logger.info("Setting Route List: "+smartphoneKey+SmartphoneCacheParams.ROUTES+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ROUTES,cacheContactList, null);
	}
	*/
}
