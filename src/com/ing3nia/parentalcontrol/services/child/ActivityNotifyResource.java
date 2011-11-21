package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
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
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.models.PCServiceStatistics;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.utils.PCNotificationTypeId;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.ActivityNotifyModel;
import com.ing3nia.parentalcontrol.services.models.AlertModel;
import com.ing3nia.parentalcontrol.services.models.ServiceStatisticsModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("act-not")
public class ActivityNotifyResource {
	private static Logger logger = Logger.getLogger(ActivityNotifyResource.class.getName());
	
	public ActivityNotifyResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<ActivityNotifyModel>(){}.getType();
		
		ActivityNotifyModel activityNotifyModel;
		ResponseBuilder rbuilder;
		
		logger.info("[Activity Notify Service] Parsing input parameters.");
		
		try {
			activityNotifyModel = jsonParser.fromJson(body, bodyType);			
		}
		catch (Exception e) {
			logger.warning("[Activity Notify Service] ActivityNotifyModel couldn't be created from post input " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		try {
			processActivityNotify(activityNotifyModel);

			logger.info("[Activity Notify Service] Ok Response. Notifications saved succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Activity Notify Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Activity Notify Service] An error ocurred while finding the PCSmartphone by key or adding PCNotification. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
	}
	
	public void processActivityNotify(ActivityNotifyModel activityNotifyModel) throws SessionQueryException, IllegalArgumentException {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Activity Notify Service] Convirtiendo id : " + activityNotifyModel.getId() + " de smartphone a Key.");
			Key smartphoneKey = KeyFactory.stringToKey(activityNotifyModel.getId());
			
			logger.info("[Activity Notify Service] Buscando smartphone en base de datos.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			ArrayList<PCRoute> newRoutes = savedSmartphone.getRoutes();
			newRoutes.add(activityNotifyModel.getRoute().convertToPCRoute());			
			savedSmartphone.setRoutes(newRoutes);
			
			PCNotification pcNotification;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			ArrayList<PCNotification> pcNotifications = new ArrayList<PCNotification>();
			ArrayList<NotificationModel> notifications = activityNotifyModel.getAlerts();
			
			for (NotificationModel notification : notifications) {
				pcNotification = new PCNotification();
				pcNotification.setType(notification.getType());
				pcNotification.setMessage(PCNotificationTypeId.getNotificationMessageFromType(notification.getType()));
				pcNotification.setDate(formatter.parse(notification.getDate()));
				
				pcNotifications.add(pcNotification);
			}
			
			pm.makePersistentAll(pcNotifications);
			
			ArrayList<Key> notificationKeys = savedSmartphone.getNotifications();
			
			for (PCNotification not : pcNotifications) {
				notificationKeys.add(not.getKey());
			}
			
			savedSmartphone.setNotifications(notificationKeys);
		
			ArrayList<ServiceStatisticsModel> stats = activityNotifyModel.getSyncStat();
			ArrayList<PCServiceStatistics> pcServiceStats = new ArrayList<PCServiceStatistics>();
			PCServiceStatistics stat;
			
			for (ServiceStatisticsModel s : stats) {
				s.setSmartphone(activityNotifyModel.getId());
				stat = s.convertToPCServiceStatistics();
				pcServiceStats.add(stat);				
			}
			
			pm.makePersistentAll(pcServiceStats);
			
			pm.close();
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	logger.info("[Activity Notify Service] An error ocurred while finding the PCSmartphone by key or saving PCNotification or PCServiceStatistics " + ex.getMessage());
	    	
			throw new SessionQueryException();
		}
	}
}
