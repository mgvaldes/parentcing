package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.models.ModificationModel;
import com.ing3nia.parentalcontrol.services.models.SmartphoneModificationIdModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("upd-sync")
public class UpdateSynchronizationResource {
	
	private static Logger logger = Logger.getLogger(UpdateSynchronizationResource.class.getName());
	
	public UpdateSynchronizationResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doGet(@QueryParam(value = "id") final String id) {
		ModificationModel modification = getSmartphoneModification(id);
		
		Gson jsonParser = new Gson();
		JsonObject jsonObjectStatus = new JsonObject();
		
		if (modification != null) {
			Type responseType = new TypeToken<ModificationModel>(){}.getType();			
			
			jsonObjectStatus.addProperty("code", "00");
			jsonObjectStatus.addProperty("verbose", "OK");
			jsonObjectStatus.addProperty("msg", "OK");
			jsonObjectStatus.addProperty("modification", jsonParser.toJson(modification, responseType));
		}
		else {
			jsonObjectStatus.addProperty("code", "01");
			jsonObjectStatus.addProperty("verbose", "INVALID_PHONE_SERIAL");
			jsonObjectStatus.addProperty("msg", "The supplied unique id is not valid");
		}
		
		ResponseBuilder rbuilder = Response.ok(jsonObjectStatus.toString(), MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
	
	//{'id':'aglub19hcHBfaWRyHgsSBlBDVXNlchgFDAsSDFBDU21hcnRwaG9uZRgzDA','modKey':''}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<SmartphoneModificationIdModel>(){}.getType();
		
		logger.info("[Update Synchronization Service] Parseando par‡metros de entrada.");
		SmartphoneModificationIdModel smartphoneModificationId = jsonParser.fromJson(body, bodyType);
		
		boolean smartphoneModificationFound = updateSmartphoneModification(smartphoneModificationId);

		JsonObject jsonObjectStatus = new JsonObject();
		
		if (smartphoneModificationFound) {
			jsonObjectStatus.addProperty("code", "00");
			jsonObjectStatus.addProperty("verbose", "OK");
			jsonObjectStatus.addProperty("msg", "OK");			
		}
		else {
			jsonObjectStatus.addProperty("code", "01");
			jsonObjectStatus.addProperty("verbose", "INVALID_PHONE_SERIAL");
			jsonObjectStatus.addProperty("msg", "The supplied unique id is not valid");
		}
		
		ResponseBuilder rbuilder = Response.ok(jsonObjectStatus.toString(), MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
	
	public boolean updateSmartphoneModification(SmartphoneModificationIdModel smartphoneModIdModel) {
		boolean found = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Update Synchronization Service] Convirtiendo id : " + smartphoneModIdModel.getId() + " de smartphone a Key.");
		Key smartphoneKey = KeyFactory.stringToKey(smartphoneModIdModel.getId());
		
		logger.info("[Update Synchronization Service] Buscando smartphone en base de datos.");
		PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
		
		if (savedSmartphone.getModification() != null) {
			logger.info("[Update Synchronization Service] Convirtiendo modKey : " + smartphoneModIdModel.getModKey() + " de smartphone a Key.");
			Key modificationKey = KeyFactory.stringToKey(smartphoneModIdModel.getModKey());
			
			if (modificationKey.equals(savedSmartphone.getModification().getKey())) {
				found = true;
				
				pm.deletePersistent(savedSmartphone.getModification());
				pm.close();
			}
		}
		
		return found;
	}
	
	public ModificationModel getSmartphoneModification(String id) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Update Synchronization Service] Convirtiendo id : " + id + " de smartphone a Key.");
		Key smartphoneKey = KeyFactory.stringToKey(id);
		
		logger.info("[Update Synchronization Service] Buscando smartphone en base de datos.");
		PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
		
		pm.close();
		
		logger.info("[Update Synchronization Service] Convirtiendo PCModification en ModificationModel.");
		return ModificationModel.convertToModificationModel(savedSmartphone.getModification());
	}
}
