package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.logging.ConsoleHandler;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.models.SmartphoneModificationIdModel;
import com.ing3nia.parentalcontrol.services.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("total-sync")
public class TotalSynchronizationResource {
	
	private static Logger logger = Logger.getLogger(TotalSynchronizationResource.class.getName());
	
	public TotalSynchronizationResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doGet(@QueryParam(value = "id") final String id) {
		Gson jsonParser = new Gson();

		logger.info("[Total Synchronization Service] Buscando smartphone asociado a id: " + id);
		SmartphoneModel smartphone = getSmartphone(id);
		
		JsonObject jsonObjectStatus = new JsonObject();
		
		if (smartphone != null) {
			Type responseType = new TypeToken<SmartphoneModel>(){}.getType();			
			
			jsonObjectStatus.addProperty("code", "00");
			jsonObjectStatus.addProperty("verbose", "OK");
			jsonObjectStatus.addProperty("msg", "OK");
			jsonObjectStatus.addProperty("smartphone", jsonParser.toJson(smartphone, responseType));
		}
		else {
			jsonObjectStatus.addProperty("code", "01");
			jsonObjectStatus.addProperty("verbose", "INVALID_PHONE_SERIAL");
			jsonObjectStatus.addProperty("msg", "The supplied unique id is not valid");
		}
		
		ResponseBuilder rbuilder = Response.ok(jsonObjectStatus.toString(), MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
	
	public SmartphoneModel getSmartphone(String id) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Total Synchronization Service] Convirtiendo id : " + id + " de smartphone a Key.");
		Key smartphoneKey = KeyFactory.stringToKey(id);
		
		logger.info("[Total Synchronization Service] Buscando smartphone en base de datos.");
		PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
		
		logger.info("[Total Synchronization Service] Convirtiendo PCSmartphone en SmartphoneModel.");
		return SmartphoneModel.convertToSmartphoneModel(savedSmartphone);
	}
}
