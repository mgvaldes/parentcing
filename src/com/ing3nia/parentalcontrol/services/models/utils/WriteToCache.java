package com.ing3nia.parentalcontrol.services.models.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.ing3nia.parentalcontrol.client.models.DeviceModel;
import com.ing3nia.parentalcontrol.client.models.LocationModel;
import com.ing3nia.parentalcontrol.client.models.NotificationModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCNotification;
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
		}
		
		logger.info("Setting Alert List: "+smartphoneKey+SmartphoneCacheParams.ALERTS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ALERTS,cacheAlertList, null);
	}
	
	public static void writeSmartphoneActiveContactsToCache(Key pcSmartphoneKey, ArrayList<PCSimpleContact> pcActiveContactsList){
	}
	
	public static void writeSmartphoneInactiveContactsToCache(Key pcSmartphoneKey, ArrayList<PCSimpleContact> pcInactiveContactsList){
	}
	
	public static void writeSmartphoneAddedEmergencyNumbersToCache(Key pcSmartphoneKey, ArrayList<PCEmergencyNumber> pcAddedEmergencyList){
	}
	
	public static void writeSmartphoneDeletedEmergencyNumbersToCache(Key pcSmartphoneKey, ArrayList<PCEmergencyNumber> pcDeletedEmergencyList){
	}
	
}
