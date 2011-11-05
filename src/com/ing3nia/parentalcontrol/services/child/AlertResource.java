package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.utils.PCNotificationTypeId;
import com.ing3nia.parentalcontrol.services.models.AlertModel;
import com.ing3nia.parentalcontrol.services.models.ClearSmartphoneModificationModel;
import com.ing3nia.parentalcontrol.services.models.NotificationModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("alert")
public class AlertResource {

	private static Logger logger = Logger.getLogger(AlertResource.class.getName());
	
	public AlertResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<AlertModel>(){}.getType();
		
		logger.info("[Alert Service] Parseando par‡metros de entrada.");
		AlertModel alertModel = jsonParser.fromJson(body, bodyType);

		saveNotifications(alertModel);
		
		JsonObject jsonObjectStatus = new JsonObject();
		
		jsonObjectStatus.addProperty("code", "00");
		jsonObjectStatus.addProperty("verbose", "OK");
		jsonObjectStatus.addProperty("msg", "OK");
		
		ResponseBuilder rbuilder = Response.ok(jsonObjectStatus.toString(), MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
	
	public void saveNotifications(AlertModel alertModel) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Alert Service] Convirtiendo id : " + alertModel.getId() + " de smartphone a Key.");
		Key smartphoneKey = KeyFactory.stringToKey(alertModel.getId());
		
		logger.info("[Alert Service] Buscando smartphone en base de datos.");
		PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
		
		PCNotification pcNotification;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ArrayList<PCNotification> notifications = new ArrayList<PCNotification>();
		
		for (NotificationModel notification : alertModel.getAlerts()) {
			try {
				pcNotification = new PCNotification();
				pcNotification.setType(notification.getType());
				pcNotification.setMessage(PCNotificationTypeId.getNotificationMessageFromType(notification.getType()));
				pcNotification.setDate(formatter.parse(notification.getDate()));
				
				notifications.add(pcNotification);
			}
			catch (ParseException ex) {
				//TODO manejar excepcion y error.
			}
			finally {
				pm.close();
			}
		}
		
		pm.makePersistentAll(notifications);
	}
}
