package com.ing3nia.parentalcontrol.services.cachetest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModelCache;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.client.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.models.PCAddress;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.AddAdminUserModel;
import com.ing3nia.parentalcontrol.services.models.InternalModificationsModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SmartphoneUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

@Path("smph-details-test")
public class SmartphoneDetailsCacheTestService {
	
	private static Logger logger = Logger.getLogger(SmartphoneDetailsCacheTestService.class.getName());
	
	public SmartphoneDetailsCacheTestService() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doGet(@QueryParam(value = "smid") final String smid) {

		ResponseBuilder rbuilder;
			
		/*
		// testing abstract objects in cache
		PCContact pcContact = new PCContact();
		pcContact.setLastName("JULIAN");
		pcContact.setKey(KeyFactory.stringToKey("ahBwYXJlbnRhbC1jb250cm9schMLEgxQQ1NtYXJ0cGhvbmUYxgEM"));
		
		PCAddress pcaddress = new PCAddress();
		pcaddress.setCity("Barquisimeto");
		ArrayList<PCAddress> adlist =new ArrayList<PCAddress>();
		adlist.add(pcaddress);
		
		pcContact.setAddresses(adlist);
		*/
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    System.out.println("Looking in cache "+SmartphoneCacheParams.SMARTPHONE+smid);
	    IdentifiableValue ident = syncCache.getIdentifiable(SmartphoneCacheParams.SMARTPHONE+smid);
	    
	    if(ident==null){
	    	logger.info("Not in cache");
	    }else{
	    	SmartphoneCacheModel smphCacheModel = (SmartphoneCacheModel) ident.getValue();
	    	logger.info(smphCacheModel.getName());
	    }
	    
	    System.out.println("Looking in cache "+smid+SmartphoneCacheParams.ACTIVE_CONTACTS);
	    ident = syncCache.getIdentifiable(smid+SmartphoneCacheParams.ACTIVE_CONTACTS);
	    if(ident==null){
	    	logger.info("Active Contacts not in cache");
	    }else{
	    	ArrayList<SimpleContactModel> activeSimple = (ArrayList<SimpleContactModel>) ident.getValue();
	    	logger.info(activeSimple.size()+"");
	    	for(SimpleContactModel contact: activeSimple){
	    		logger.info(contact.getPhones().get(0).getPhoneNumber());
	    	}
	    }
	    
	    
		rbuilder = Response.ok("{}", MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
		
		/*
		try {
			addAdminUserModel = jsonParser.fromJson(body, bodyType);
		}
		catch (Exception e) {
			logger.warning("[Add Admin User Service] AddAdminUserModel couldn't be created from post input " + e.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		*/
		
		//String smid = "ahBwYXJlbnRhbC1jb250cm9schMLEgxQQ1NtYXJ0cGhvbmUYxgEM";
		
		/*
	    String key = smid;
	    byte[] value;

	    // Using the synchronous cache
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    //value = (byte[]) syncCache.get(key); // read from cache
	    IdentifiableValue smphIdent = (IdentifiableValue)syncCache.getIdentifiable(key); // read from cache as model
	    SmartphoneModelCache smodel = null;
	    if(smphIdent != null){
	    	smodel = (SmartphoneModelCache) smphIdent.getValue();
	    }
	    if (smodel != null) {
	    	logger.info("Value in cache!!!");

			//Gson jsonParser = new Gson();
			//Type bodyType = new TypeToken<SmartphoneModelCache>(){}.getType();
			//SmartphoneModelCache smodel = jsonParser.fromJson(new String(value), bodyType);
			
			if(smodel == null){
				logger.severe("SMODEL NULL");
			}else{
				logger.info("SMODEL: Active->"+smodel.getActiveContacts().size()+" "+smodel.getActiveContacts().get(0).getFirstName());
			}
			
			//logger.info("SMODEL: Active->"+smodel.getActiveContacts().size()+" ,Properties->"+smodel.getProperties().size());
			//logger.info("A CONTACT: "+smodel.getActiveContacts().get(0).getFirstName()+smodel.getActiveContacts().get(0).getPhones());
			
			Gson jsonParser = new Gson();
			Type bodyType = new TypeToken<SmartphoneModelCache>(){}.getType();
			//SmartphoneModelCache smodel = jsonParser.fromJson(new String(value), bodyType);
			String smodelString = jsonParser.toJson(smodel, bodyType);
			
	    	rbuilder = Response.ok(smodelString, MediaType.APPLICATION_JSON);	    	
	    	return rbuilder.build();
	    }
	   
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCSmartphone smartphone;
		try{
			 smartphone = (PCSmartphone)pm.getObjectById(PCSmartphone.class, KeyFactory.stringToKey(smid));	 
		} catch(IllegalArgumentException e){
			rbuilder = Response.ok(WSStatus.INVALID_SMARTPHONE.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		if(smartphone == null){
			logger.severe("[Parent Smartphone Details] No smartphone found from the given key "+smid);
			rbuilder = Response.ok(WSStatus.INVALID_SMARTPHONE.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}

		pm.close();
	
		
		logger.info("[Parent Smartphone Details] Obtaining smarpthone details");
		JsonObject smartphoneInfo = SmartphoneUtils.getSmartphoneDetails(smartphone);
		
		JsonObject responseMsg = WSStatus.OK.getStatusAsJson();
		responseMsg.add("smartphone", smartphoneInfo);
		
		logger.info("Smartphone not in cache, setting cache");
		//byte[] smartphoneCacheJson = smartphoneInfo.toString().getBytes();
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<SmartphoneModelCache>(){}.getType();
		SmartphoneModelCache smodelCacheJson = jsonParser.fromJson(smartphoneInfo, bodyType);
		
		if(smphIdent == null){
			syncCache.put(key, smodelCacheJson, null);
		}else{
			syncCache.putIfUntouched(key, smphIdent, smodelCacheJson, null); // populate cache
		}
	    logger.info("Smartphone successfully set: "+smodelCacheJson.getActiveContacts().size());
	    
		rbuilder = Response.ok(responseMsg.toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
		*/
		
	}
	
}
