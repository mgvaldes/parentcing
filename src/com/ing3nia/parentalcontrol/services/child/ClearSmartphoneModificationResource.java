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
import com.ing3nia.parentalcontrol.services.models.ClearSmartphoneModificationModel;
import com.ing3nia.parentalcontrol.services.models.ModificationModel;
import com.ing3nia.parentalcontrol.services.models.SmartphoneIdModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("cl-sph-mod")
public class ClearSmartphoneModificationResource {

	private static Logger logger = Logger.getLogger(ClearSmartphoneModificationResource.class.getName());
	
	public ClearSmartphoneModificationResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	//{'id':'aglub19hcHBfaWRyHgsSBlBDVXNlchgFDAsSDFBDU21hcnRwaG9uZRgzDA','modKey':'65980a69e977a8c18d1912dd178db9ef'}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<ClearSmartphoneModificationModel>(){}.getType();
		
		logger.info("[Clear Smartphone Modification Service] Parseando par‡metros de entrada.");
		ClearSmartphoneModificationModel clearSmartphoneMod = jsonParser.fromJson(body, bodyType);

		boolean cleared = clearSmartphoneModification(clearSmartphoneMod);
		
		JsonObject jsonObjectStatus = new JsonObject();
		
		if (cleared) {
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
	
	public boolean clearSmartphoneModification(ClearSmartphoneModificationModel clearSmartphoneMod) {
		boolean cleared = false;
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Clear Smartphone Modification Service] Convirtiendo id : " + clearSmartphoneMod.getId() + " de smartphone a Key.");
		Key smartphoneKey = KeyFactory.stringToKey(clearSmartphoneMod.getId());
		
		logger.info("[Clear Smartphone Modification Service] Buscando smartphone en base de datos.");
		PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
		
		Key modificationKey = null;
		
		try {
			modificationKey = KeyFactory.stringToKey(clearSmartphoneMod.getModKey());
		}
		catch (IllegalArgumentException ex) {
			//TODO manejar exception y error
		}
		
		if (modificationKey != null) {
			if (savedSmartphone.getModification().getKey().equals(modificationKey)) {
				pm.deletePersistent(savedSmartphone.getModification());
				cleared = true;
			}
		}
		
		return cleared;
	}
}
