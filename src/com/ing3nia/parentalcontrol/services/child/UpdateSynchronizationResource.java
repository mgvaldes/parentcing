package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.SmartphoneModificationIdModel;
import com.ing3nia.parentalcontrol.services.models.utils.ModificationModelUtils;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

@Path("upd-sync")
public class UpdateSynchronizationResource {
	
	private static Logger logger = Logger.getLogger(UpdateSynchronizationResource.class.getName());
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;

	
	public UpdateSynchronizationResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doGet(@QueryParam(value = "id") final String id) {
		if (ACTUAL.equals(NEW_WS)) {
            return newGETWS(id);
	}

    return olGETdWS(id);
	}
		
	public Response olGETdWS(String id){	
		ResponseBuilder rbuilder;

		logger.info("[Total Synchronization Service] Buscando smartphone asociado a id: " + id);
		ModificationModel modification = null;
		
		try {
			modification = getSmartphoneModification(id);
		}
		catch (SessionQueryException ex) {
			logger.warning("[Total Synchronization Service] An error ocurred while searching for smartphone. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Total Synchronization Service] An error ocurred while searching for smartphone. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		logger.info("[Total Synchronization Service] Ok Response. Smartphone info succesfully sent.");
		
		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		Gson jsonBuilder = new Gson();
		JsonParser jsonParser = new JsonParser();
		Type type = new TypeToken<ModificationModel>(){}.getType();
		JsonObject modificationObject = (JsonObject)jsonParser.parse(jsonBuilder.toJson(modification, type));
		okResponse.add("modification", modificationObject);
		
		rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
	}
	
	public Response newGETWS(String id) {
		ResponseBuilder rbuilder;

		logger.info("[Total Synchronization Service] Looking for smartphone associated with id: "
				+ id);
		ModificationModel modification = null;

		logger.info("[Total Synchronization Service] Looking for smartphone in cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		IdentifiableValue ident = syncCache.getIdentifiable(id
				+ SmartphoneCacheParams.MODIFICATION);

		if (ident == null) {
			logger.info("[Total Synchronization Service] Smartphone Modification not found in cache");
			try {
				modification = getSmartphoneModification(id);
			} catch (SessionQueryException ex) {
				logger.warning("[Total Synchronization Service] An error ocurred while searching for smartphone. "
						+ ex.getMessage());

				rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR
						.getStatusAsJson().toString(),
						MediaType.APPLICATION_JSON);
				WebServiceUtils.setUTF8Encoding(
						WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
				return rbuilder.build();
			} catch (IllegalArgumentException ex) {
				logger.warning("[Total Synchronization Service] An error ocurred while searching for smartphone. "
						+ ex.getMessage());

				rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR
						.getStatusAsJson().toString(),
						MediaType.APPLICATION_JSON);
				WebServiceUtils.setUTF8Encoding(
						WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
				return rbuilder.build();
			}
		} else {
			modification = (ModificationModel) ident.getValue();
			logger.info("[Total Synchronization Service] Found Modification in cache");
		}

		logger.info("[Total Synchronization Service] Ok Response. Smartphone info succesfully sent.");

		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		Gson jsonBuilder = new Gson();
		JsonParser jsonParser = new JsonParser();
		Type type = new TypeToken<ModificationModel>() {
		}.getType();
		JsonObject modificationObject = (JsonObject) jsonParser
				.parse(jsonBuilder.toJson(modification, type));
		okResponse.add("modification", modificationObject);

		rbuilder = Response.ok(okResponse.toString(),
				MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE,
				rbuilder);
		return rbuilder.build();

	}
	
	//{'id':'aglub19hcHBfaWRyHgsSBlBDVXNlchgFDAsSDFBDU21hcnRwaG9uZRgzDA','modKey':''}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {

		if (ACTUAL.equals(NEW_WS)) {
			return newPOSTWS(body);
		}

		return oldPOSTWS(body);
	}
	
	public Response oldPOSTWS(String body){
		
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<SmartphoneModificationIdModel>(){}.getType();
		
		SmartphoneModificationIdModel smartphoneModificationId;
		ResponseBuilder rbuilder;
		
		logger.info("[Update Synchronization Service] Parsing input parameters.");
		
		try {
			smartphoneModificationId = jsonParser.fromJson(body, bodyType);
		}
		catch (Exception e) {
			logger.warning("[Update Synchronization Service] SmartphoneModificationIdModel couldn't be created from post input " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		boolean smartphoneModificationFound;
		
		try {
			smartphoneModificationFound = updateSmartphoneModificationOLD(smartphoneModificationId);
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Update Synchronization Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (SessionQueryException ex) {
			logger.warning("[Update Synchronization Service] An error ocurred while finding the PCSmartphone by key or deleting PCModification. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		if (smartphoneModificationFound) {
			logger.info("[Update Synchronization Service] Ok Response. User succesfully deleted modification.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		else {
			logger.warning("[Update Synchronization Service] An error ocurred while finding the PCSmartphone by key or deleting PCModification");
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
	}
	
	public Response newPOSTWS(String body){
		
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<SmartphoneModificationIdModel>(){}.getType();
		
		SmartphoneModificationIdModel smartphoneModificationId;
		ResponseBuilder rbuilder;
		
		logger.info("[Update Synchronization Service] Parsing input parameters.");
		
		try {
			smartphoneModificationId = jsonParser.fromJson(body, bodyType);
		}
		catch (Exception e) {
			logger.warning("[Update Synchronization Service] SmartphoneModificationIdModel couldn't be created from post input " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		boolean smartphoneModificationFound;
		
		try {
			smartphoneModificationFound = updateSmartphoneModificationNEW(smartphoneModificationId);
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Update Synchronization Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (SessionQueryException ex) {
			logger.warning("[Update Synchronization Service] An error ocurred while finding the PCSmartphone by key or deleting PCModification. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		if (smartphoneModificationFound) {
			logger.info("[Update Synchronization Service] Ok Response. User succesfully deleted modification.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		else {
			logger.warning("[Update Synchronization Service] An error ocurred while finding the PCSmartphone by key or deleting PCModification");
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
	}	
	
	public boolean updateSmartphoneModificationOLD(SmartphoneModificationIdModel smartphoneModIdModel) throws SessionQueryException, IllegalArgumentException {
		boolean found = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Update Synchronization Service] Convirtiendo id : " + smartphoneModIdModel.getId() + " de smartphone a Key.");
			Key smartphoneKey = KeyFactory.stringToKey(smartphoneModIdModel.getId());
			
			logger.info("[Update Synchronization Service] Buscando smartphone en base de datos.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			if (savedSmartphone.getModification() != null) {
				logger.info("[Update Synchronization Service] Convirtiendo modKey : " + smartphoneModIdModel.getModKey() + " de smartphone a Key.");
				Key modificationKey = KeyFactory.stringToKey(smartphoneModIdModel.getModKey());
				
				if (modificationKey.equals(savedSmartphone.getModification())) {
					logger.info("[Update Synchronization Service] Modification Found, deleting");
					found = true;
					PCModification mod = pm.getObjectById(PCModification.class, savedSmartphone.getModification());
					pm.deletePersistent(mod);
				
					PCModification newModification = new PCModification();
					pm.makePersistent(newModification);
					savedSmartphone.setModification(newModification.getKey());
				}
			}
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	logger.info("[Update Synchronization Service] An error ocurred while finding the PCSmartphone by key or deleting PCModification " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }
		
		pm.close();	
		return found;
	}
	
	public boolean updateSmartphoneModificationNEW(SmartphoneModificationIdModel smartphoneModIdModel) throws SessionQueryException, IllegalArgumentException {
		boolean found = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Update Synchronization Service] Convirtiendo id : " + smartphoneModIdModel.getId() + " de smartphone a Key.");
			Key smartphoneKey = KeyFactory.stringToKey(smartphoneModIdModel.getId());
			
			logger.info("[Update Synchronization Service] Buscando smartphone en base de datos.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			if (savedSmartphone.getModification() != null) {
				logger.info("[Update Synchronization Service] Convirtiendo modKey : " + smartphoneModIdModel.getModKey() + " de smartphone a Key.");
				Key modificationKey = KeyFactory.stringToKey(smartphoneModIdModel.getModKey());
				
				if (modificationKey.equals(savedSmartphone.getModification())) {
					logger.info("[Update Synchronization Service] Modification Found, deleting");
					found = true;
					PCModification mod = pm.getObjectById(PCModification.class, savedSmartphone.getModification());
					pm.deletePersistent(mod);
				
					PCModification newModification = new PCModification();
					pm.makePersistent(newModification);
					savedSmartphone.setModification(newModification.getKey());
				}
			}
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	logger.info("[Update Synchronization Service] An error ocurred while finding the PCSmartphone by key or deleting PCModification " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }
		
		logger.info("[Update Synchronization Service] Deleting modification from Cache");
		WriteToCache.deleteModificationFromCache(smartphoneModIdModel.getId());
		
		pm.close();	
		return found;
	}
	
	public ModificationModel getSmartphoneModification(String id) throws SessionQueryException, IllegalArgumentException {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Update Synchronization Service] Converting id : " + id + " of smartphone to Key.");
			Key smartphoneKey = KeyFactory.stringToKey(id);
			
			logger.info("[Update Synchronization Service] Searching for smartphone in DB.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);					
			
			logger.info("[Update Synchronization Service] Converting PCModification en ModificationModel.");
			ModificationModel modModel = ModificationModelUtils.convertToModificationModel(savedSmartphone.getModification());
			
			pm.close();
			
			return modModel;
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	logger.info("[Update Synchronization Service] An error ocurred while finding the PCSmartphone by key " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }
	}
}
