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
import com.ing3nia.parentalcontrol.client.models.NotificationModel;
import com.ing3nia.parentalcontrol.client.models.PCNotificationTypeId;
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.AlertModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("alert")
public class AlertResource {

	private static Logger logger = Logger.getLogger(AlertResource.class.getName());
	
	public AlertResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<AlertModel>(){}.getType();
		
		AlertModel alertModel;
		ResponseBuilder rbuilder;
		
		logger.info("[Alert Service] Parsing input parameters.");
		
		try {
			alertModel = jsonParser.fromJson(body, bodyType);
		}
		catch (Exception e) {
			logger.warning("[Alert Service] AlertModel couldn't be created from post input " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		try {
			saveNotifications(alertModel);
			
			logger.info("[Alert Service] Ok Response. Notifications saved succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (ParseException ex) {
			logger.warning("[Alert Service] An error occured while parsing notification date String to Date");
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Alert Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Alert Service] An error ocurred while finding the PCSmartphone by key or adding PCNotification. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
	}
	
	public void saveNotifications(AlertModel alertModel) throws ParseException, SessionQueryException, IllegalArgumentException {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Alert Service] Convirtiendo id : " + alertModel.getId() + " de smartphone a Key.");
			Key smartphoneKey = KeyFactory.stringToKey(alertModel.getId());
			
			logger.info("[Alert Service] Buscando smartphone en base de datos.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			PCNotification pcNotification;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			ArrayList<PCNotification> notifications = new ArrayList<PCNotification>();
			
			for (NotificationModel notification : alertModel.getAlerts()) {
				pcNotification = new PCNotification();
				pcNotification.setType(notification.getType());
				pcNotification.setMessage(PCNotificationTypeId.getNotificationMessageFromType(notification.getType()));
				logger.info("FORMATTING DATE: "+notification.getDate());
				pcNotification.setDate(formatter.parse(notification.getDate()));
				logger.info("CREATED DATE: "+pcNotification.getDate().toString());
				notifications.add(pcNotification);
			}
			
			pm.makePersistentAll(notifications);
			
			ArrayList<Key> notificationKeys = savedSmartphone.getNotifications();
			
			for (PCNotification not : notifications) {
				notificationKeys.add(not.getKey());
			}
			
			savedSmartphone.setNotifications(notificationKeys);
			
			pm.close();
		}
//		catch (ParseException ex) {
//			throw ex;
//		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	logger.info("[Alert Service] An error ocurred while finding the PCSmartphone by key or saving PCNotification" + ex.getMessage());
	    	
			throw new SessionQueryException();
		}
	}
}
