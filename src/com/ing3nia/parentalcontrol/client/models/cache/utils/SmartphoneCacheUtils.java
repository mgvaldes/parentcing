package com.ing3nia.parentalcontrol.client.models.cache.utils;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.child.RegisterSmartphoneResource;

public class SmartphoneCacheUtils {
	private static Logger logger = Logger.getLogger(SmartphoneCacheUtils.class.getName());
	
	public boolean setSmartphoneToCache(PCSmartphone smartphone){
		boolean setSucessfull = false;
		
		logger.info("[Register Smartphone Service - Cache Version] Creating SmartphoneCacheModel");
		SmartphoneCacheModel smartphoneCacheModel = pcSmartphoneToCacheModel(smartphone);
		
		

		
		return setSucessfull;
	}
	
	public SmartphoneCacheModel pcSmartphoneToCacheModel(PCSmartphone smartphone){
		SmartphoneCacheModel smartphoneCacheModel = new SmartphoneCacheModel();
		smartphoneCacheModel.setName(smartphone.getName());
		smartphoneCacheModel.setKeyId(KeyFactory.keyToString(smartphone.getKey()));
		smartphoneCacheModel.setSerialNumber(smartphone.getSerialNumber());
		return smartphoneCacheModel;
	}
}
