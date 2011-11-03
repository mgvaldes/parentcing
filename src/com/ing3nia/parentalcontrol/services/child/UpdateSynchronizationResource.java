package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import com.ing3nia.parentalcontrol.services.models.SmartphoneIdModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("upd-sync")
public class UpdateSynchronizationResource {
	
	private static Logger logger = Logger.getLogger(UpdateSynchronizationResource.class.getName());
	
	public UpdateSynchronizationResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	//{'id':'aglub19hcHBfaWRyHgsSBlBDVXNlchgFDAsSDFBDU21hcnRwaG9uZRgzDA'}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<SmartphoneIdModel>(){}.getType();
		
		logger.info("[Update Synchronization Service] Parseando par‡metros de entrada.");
		SmartphoneIdModel smartphoneId = jsonParser.fromJson(body, bodyType);

		ModificationModel modification = getSmartphoneModification(smartphoneId.getId());
		
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
	
	public ModificationModel getSmartphoneModification(String id) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Update Synchronization Service] Convirtiendo id : " + id + " de smartphone a Key.");
		Key smartphoneKey = KeyFactory.stringToKey(id);
		
		logger.info("[Update Synchronization Service] Buscando smartphone en base de datos.");
		PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
		
		logger.info("[Update Synchronization Service] Convirtiendo PCModification en ModificationModel.");
		return ModificationModel.convertToModificationModel(savedSmartphone.getModification());
	}
}
