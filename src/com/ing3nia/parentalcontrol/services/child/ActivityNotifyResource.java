package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.NotificationModel;
import com.ing3nia.parentalcontrol.client.models.PCNotificationTypeId;
import com.ing3nia.parentalcontrol.client.models.RouteModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.models.PCServiceStatistics;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.ActivityNotifyModel;
import com.ing3nia.parentalcontrol.services.models.ServiceStatisticsModel;
import com.ing3nia.parentalcontrol.services.models.utils.RouteModelUtils;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

@Path("act-not")
public class ActivityNotifyResource {
	private static Logger logger = Logger.getLogger(ActivityNotifyResource.class.getName());
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	public ActivityNotifyResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		if (ACTUAL.equals(NEW_WS)) {
			return newWS(body);
		}
		
		return oldWS(body);
	}
	
	public Response newWS(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<ActivityNotifyModel>(){}.getType();
		
		ActivityNotifyModel activityNotifyModel;
		ResponseBuilder rbuilder;
		
		logger.info("[Activity Notify Service - Cache Version] Parsing input parameters.");
		
		try {
			activityNotifyModel = jsonParser.fromJson(body, bodyType);			
		}
		catch (Exception e) {
			logger.warning("[Activity Notify Service - Cache Version] ActivityNotifyModel couldn't be created from post input " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		try {
			processActivityNotifyNEW(activityNotifyModel);

			logger.info("[Activity Notify Service - Cache Version] Ok Response. Notifications saved succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Activity Notify Service - Cache Version] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Activity Notify Service - Cache Version] An error ocurred while finding the PCSmartphone by key or adding PCNotification. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
	}
	
	public void processActivityNotifyNEW(ActivityNotifyModel activityNotifyModel) throws SessionQueryException, IllegalArgumentException {
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Activity Notify Service - Cache Version] Convirtiendo id : " + activityNotifyModel.getId() + " de smartphone a Key.");
			Key smartphoneKey = KeyFactory.stringToKey(activityNotifyModel.getId());
			
			logger.info("[Activity Notify Service - Cache Version] Buscando smartphone en base de datos.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			ArrayList<Key> newRoutes = savedSmartphone.getRoutes();
			
			String smartphoneRouteCacheKey = activityNotifyModel.getId() + SmartphoneCacheParams.ROUTES;
			IdentifiableValue cacheIdentSmartphoneRoute = (IdentifiableValue) syncCache.getIdentifiable(smartphoneRouteCacheKey);
			ArrayList<RouteModel> cacheSmartphoneRoutes = null;
			
			if (cacheIdentSmartphoneRoute == null) {
				ArrayList<PCRoute> routeList = new ArrayList<PCRoute>();
				PCRoute auxRoute;
				
				for (Key key : newRoutes) {
					auxRoute = pm.getObjectById(PCRoute.class, key);
					routeList.add(auxRoute);
				}
				
				WriteToCache.writeSmartphoneRoutesToCache(activityNotifyModel.getId(), routeList);
				
				cacheIdentSmartphoneRoute = syncCache.getIdentifiable(smartphoneRouteCacheKey);
				cacheSmartphoneRoutes = (ArrayList<RouteModel>) cacheIdentSmartphoneRoute.getValue();
			}
			else {
				cacheSmartphoneRoutes = (ArrayList<RouteModel>) cacheIdentSmartphoneRoute.getValue();
			}
			
			newRoutes.add(RouteModelUtils.convertToPCRoute(activityNotifyModel.getRoute()));			
			savedSmartphone.setRoutes(newRoutes);
			
			cacheSmartphoneRoutes.add(activityNotifyModel.getRoute());
			WriteToCache.writeSmartphoneRoutesToCache(cacheSmartphoneRoutes, activityNotifyModel.getId());
			
			PCNotification pcNotification;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
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
			
			String smartphoneAlertsCacheKey = activityNotifyModel.getId() + SmartphoneCacheParams.ALERTS;
			IdentifiableValue cacheIdentSmartphoneAlerts = (IdentifiableValue) syncCache.getIdentifiable(smartphoneAlertsCacheKey);
			ArrayList<NotificationModel> cacheSmartphoneAlerts = null;
			
			if (cacheIdentSmartphoneAlerts == null) {
				ArrayList<PCNotification> alertsList = new ArrayList<PCNotification>();
				PCNotification auxAlert;
				
				for (Key key : notificationKeys) {
					auxAlert = pm.getObjectById(PCNotification.class, key);
					alertsList.add(auxAlert);
				}
				
				WriteToCache.writeSmartphoneAlertsToCache(smartphoneKey, alertsList);
				
				cacheIdentSmartphoneAlerts = syncCache.getIdentifiable(smartphoneAlertsCacheKey);
				cacheSmartphoneAlerts = (ArrayList<NotificationModel>) cacheIdentSmartphoneRoute.getValue();
			}
			else {
				cacheSmartphoneAlerts = (ArrayList<NotificationModel>) cacheIdentSmartphoneAlerts.getValue();
			}
			
			for (PCNotification not : pcNotifications) {
				notificationKeys.add(not.getKey());
			}
			
			savedSmartphone.setNotifications(notificationKeys);
			
			for (NotificationModel notification : notifications) {
				cacheSmartphoneAlerts.add(notification);
			}
			
			WriteToCache.writeSmartphoneAlertsToCache(activityNotifyModel.getId(), cacheSmartphoneAlerts);
		
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
	    	logger.info("[Activity Notify Service - Cache Version] An error ocurred while finding the PCSmartphone by key or saving PCNotification or PCServiceStatistics " + ex.getMessage());
	    	
			throw new SessionQueryException();
		}
	}
	
	public Response oldWS(String body) {
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
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		try {
			processActivityNotifyOLD(activityNotifyModel);

			logger.info("[Activity Notify Service] Ok Response. Notifications saved succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Activity Notify Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Activity Notify Service] An error ocurred while finding the PCSmartphone by key or adding PCNotification. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
	}
	
	public void processActivityNotifyOLD(ActivityNotifyModel activityNotifyModel) throws SessionQueryException, IllegalArgumentException {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Activity Notify Service] Convirtiendo id : " + activityNotifyModel.getId() + " de smartphone a Key.");
			Key smartphoneKey = KeyFactory.stringToKey(activityNotifyModel.getId());
			
			logger.info("[Activity Notify Service] Buscando smartphone en base de datos.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			ArrayList<Key> newRoutes = savedSmartphone.getRoutes();
			newRoutes.add(RouteModelUtils.convertToPCRoute(activityNotifyModel.getRoute()));			
			savedSmartphone.setRoutes(newRoutes);
			
			PCNotification pcNotification;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
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
