package com.ing3nia.parentalcontrol.services.models.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.xpath.functions.FuncId;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.ing3nia.parentalcontrol.client.models.DeviceModel;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.LocationModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.NotificationModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RouteModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.models.cache.SessionCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.client.models.cache.TicketCacheParams;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSession;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class WriteToCache {

	private static final Logger logger = Logger.getLogger(WriteToCache.class.getName());
	
	public static void writeSmartphoneToCache(SmartphoneCacheModel cacheSmartphone) {
		logger.info("Setting Smartphone: " + cacheSmartphone.getKeyId() + " to cache");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(SmartphoneCacheParams.SMARTPHONE + cacheSmartphone.getKeyId(), cacheSmartphone);
	}
	
	public static SmartphoneCacheModel writeSmartphoneToCache(PCSmartphone pcSmartphone){
		SmartphoneCacheModel smartphoneCacheModel = new SmartphoneCacheModel();
		String smartphoneKey = KeyFactory.keyToString(pcSmartphone.getKey());
		
		smartphoneCacheModel.setKeyId(smartphoneKey);
		smartphoneCacheModel.setName(pcSmartphone.getName());
		smartphoneCacheModel.setSerialNumber(pcSmartphone.getSerialNumber());
		
		DeviceModel deviceModel = new DeviceModel();
		if(pcSmartphone.getDevice() == null){
			deviceModel.setModel("Default");
			deviceModel.setType(0);
			deviceModel.setVersion("Default");
		}else{
			PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
			PCDevice device = pm.getObjectById(PCDevice.class, pcSmartphone.getDevice());
			deviceModel.setModel(device.getModel());
			deviceModel.setType(0);
			deviceModel.setVersion(device.getVersion());
		}
		smartphoneCacheModel.setDevice(deviceModel);
		

		LocationModel location = new LocationModel();
		if(pcSmartphone.getLocation() == null){
			location.setLatitude("0.0");
			location.setLongitude("0.0");
		}else{
			location.setLatitude(String.valueOf(pcSmartphone.getLocation().getLatitude()));
			location.setLongitude(String.valueOf(pcSmartphone.getLocation().getLongitude()));
		}
		smartphoneCacheModel.setLocation(location);

		logger.info("Setting Smartphone: "+smartphoneKey+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(SmartphoneCacheParams.SMARTPHONE+smartphoneKey, smartphoneCacheModel);
		return smartphoneCacheModel;
	}
	
	public static void writeAdminUsersToCache(String pcSmartphoneKey, ArrayList<PCUser> adminUsers){
		ArrayList<UserModel> cacheAdminUserList = new ArrayList<UserModel>();
		UserModel auxUser;
		ArrayList<String> smartphoneKeys;
		ArrayList<Key> keys;
		
		for (PCUser adminUser : adminUsers) {
			auxUser = new UserModel();
			auxUser.setEmail(adminUser.getEmail());
			auxUser.setKey(KeyFactory.keyToString(adminUser.getKey()));
			auxUser.setName(adminUser.getName());
			auxUser.setPass(adminUser.getPassword());
			auxUser.setUsr(adminUser.getUsername());
			
			keys = adminUser.getSmartphones();
			smartphoneKeys = new ArrayList<String>();
			
			for (Key sphKey : keys) {
				smartphoneKeys.add(KeyFactory.keyToString(sphKey));
			}
			
			auxUser.setSmartphoneKeys(smartphoneKeys);
			
			cacheAdminUserList.add(auxUser);
		}
		
		logger.info("Setting Admin User List: " + UserCacheParams.USER + pcSmartphoneKey + UserCacheParams.ADMIN_LIST + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(UserCacheParams.USER + pcSmartphoneKey + UserCacheParams.ADMIN_LIST, cacheAdminUserList, null);
	}
	
	public static void writeAdminUsersToCache(ArrayList<UserModel> adminUsers, String pcSmartphoneKey){
		logger.info("Setting Admin User List: " + UserCacheParams.USER + pcSmartphoneKey + UserCacheParams.ADMIN_LIST + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(UserCacheParams.USER + pcSmartphoneKey + UserCacheParams.ADMIN_LIST, adminUsers, null);
	}
	
	public static ArrayList<TicketModel> writeOpenTicketsToCache(PersistenceManager pm, String userKey, ArrayList<PCHelpdeskTicket> openTickets, String dummy) {
		ArrayList<String> cacheOpenTicketList = new ArrayList<String>();
		TicketModel auxOpenTicket;
		ArrayList<TicketModel> openT = new ArrayList<TicketModel>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		Query query = pm.newQuery(PCCategory.class);
	    query.setFilter("description == descriptionParam");
	    query.declareParameters("String descriptionParam");
	    query.setRange(0, 1);
	    
	    List<PCCategory> results;
	    ArrayList<TicketAnswerModel> answers = new ArrayList<TicketAnswerModel>();
	    PCHelpdeskTicketAnswer auxTicketAnswer;
	    TicketAnswerModel auxAnswer;
	    ArrayList<Key> ticketAnswerKeys;
	    String username;
	    PCUser user;
    	PCAdmin admin;
    	String userAdminKey;
	    
		for (PCHelpdeskTicket openTicket : openTickets) {
			auxOpenTicket = new TicketModel();
			
			results = (List<PCCategory>)query.execute(openTicket.getCategory());
			
			if (!results.isEmpty()) {
				auxOpenTicket.setCategory(results.get(0).getDescription());
			}
			
			auxOpenTicket.setDate(openTicket.getDate());
			auxOpenTicket.setComment(openTicket.getQuestion());
			auxOpenTicket.setSubject(openTicket.getSubject());
			auxOpenTicket.setKey(KeyFactory.keyToString(openTicket.getKey()));
			
			ticketAnswerKeys = openTicket.getAnswers();
			
			for (Key ansKey : ticketAnswerKeys) {
				auxTicketAnswer = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
    			
    			if (auxTicketAnswer.getAdmin() == null) {    				
    				user = pm.getObjectById(PCUser.class, auxTicketAnswer.getUser());
    				username = user.getUsername();
    				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getUser());
    				
    				logger.info("Answer by USER: " + username + " with key: " + userAdminKey);
    			}
    			else {
    				admin = pm.getObjectById(PCAdmin.class, auxTicketAnswer.getAdmin());
    				username = admin.getUsername();
    				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getAdmin());
    				
    				logger.info("Answer by ADMIN: " + username + " with key: " + userAdminKey);
    			}
    			
    			auxAnswer = new TicketAnswerModel(dateFormatter.format(auxTicketAnswer.getDate()), userAdminKey, auxTicketAnswer.getAnswer(), username);
    			answers.add(auxAnswer);
    			
    			logger.info("Adding answer to answers list");
    		}
			
			user = pm.getObjectById(PCUser.class, openTicket.getUser());
			auxOpenTicket.setName(user.getUsername());
			auxOpenTicket.setAnswers(answers);
			
			openT.add(auxOpenTicket);
			writeTicketToCache(auxOpenTicket);
			
			cacheOpenTicketList.add(auxOpenTicket.getKey());
		}
		
		logger.info("Setting Open Ticket List: " + userKey + UserCacheParams.OPEN_TICKETS_LIST + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(userKey + UserCacheParams.OPEN_TICKETS_LIST, cacheOpenTicketList, null);
		
		return openT;
	}
	
	public static ArrayList<String> writeOpenTicketsToCache(PersistenceManager pm, String userKey, ArrayList<PCHelpdeskTicket> openTickets){
		ArrayList<String> cacheOpenTicketList = new ArrayList<String>();
		TicketModel auxOpenTicket;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		Query query = pm.newQuery(PCCategory.class);
	    query.setFilter("description == descriptionParam");
	    query.declareParameters("String descriptionParam");
	    query.setRange(0, 1);
	    
	    List<PCCategory> results;
	    ArrayList<TicketAnswerModel> answers = new ArrayList<TicketAnswerModel>();
	    PCHelpdeskTicketAnswer auxTicketAnswer;
	    TicketAnswerModel auxAnswer;
	    ArrayList<Key> ticketAnswerKeys;
	    String username;
	    PCUser user;
    	PCAdmin admin;
    	String userAdminKey;
	    
		for (PCHelpdeskTicket openTicket : openTickets) {
			auxOpenTicket = new TicketModel();
			
			results = (List<PCCategory>)query.execute(openTicket.getCategory());
			
			if (!results.isEmpty()) {
				auxOpenTicket.setCategory(results.get(0).getDescription());
			}
			
			auxOpenTicket.setDate(openTicket.getDate());
			auxOpenTicket.setComment(openTicket.getQuestion());
			auxOpenTicket.setSubject(openTicket.getSubject());
			auxOpenTicket.setKey(KeyFactory.keyToString(openTicket.getKey()));
			
			ticketAnswerKeys = openTicket.getAnswers();
			
			for (Key ansKey : ticketAnswerKeys) {
				auxTicketAnswer = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
    			
    			if (auxTicketAnswer.getAdmin() == null) {
    				user = pm.getObjectById(PCUser.class, auxTicketAnswer.getUser());
    				username = user.getUsername();
    				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getUser());
    			}
    			else {
    				admin = pm.getObjectById(PCAdmin.class, auxTicketAnswer.getAdmin());
    				username = admin.getUsername();
    				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getAdmin());
    			}
    			
    			auxAnswer = new TicketAnswerModel(dateFormatter.format(auxTicketAnswer.getDate()), userAdminKey, auxTicketAnswer.getAnswer(), username);
    			answers.add(auxAnswer);
    		}
			
			user = pm.getObjectById(PCUser.class, openTicket.getUser());
			auxOpenTicket.setName(user.getUsername());
			auxOpenTicket.setAnswers(answers);
			
			writeTicketToCache(auxOpenTicket);
			
			cacheOpenTicketList.add(auxOpenTicket.getKey());
		}
		
		logger.info("Setting Open Ticket List: " + userKey + UserCacheParams.OPEN_TICKETS_LIST + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(userKey + UserCacheParams.OPEN_TICKETS_LIST, cacheOpenTicketList, null);
		
		return cacheOpenTicketList;
	}
	
	public static void writeOpenTicketsToCache(ArrayList<String> openTickets, String userKey){
		logger.info("Setting Open Ticket List: " + userKey + UserCacheParams.OPEN_TICKETS_LIST + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(userKey + UserCacheParams.OPEN_TICKETS_LIST, openTickets, null);
	}
	
	public static void writeClosedTicketsToCache(ArrayList<String> closedTickets, String userKey){
		logger.info("Setting Closed Ticket List: " + userKey + UserCacheParams.CLOSED_TICKETS_LIST + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(userKey + UserCacheParams.CLOSED_TICKETS_LIST, closedTickets, null);
	}
	
	public static void writeAllClosedTicketsToCache(ArrayList<String> allCloseTickets) {
		logger.info("Setting All Closed Ticket List: " + TicketCacheParams.ALL_CLOSED_TICKETS + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(TicketCacheParams.ALL_CLOSED_TICKETS, allCloseTickets, null);
	}
	
	public static void writeAllOpenTicketsToCache(ArrayList<String> allOpenTickets) {
		logger.info("Setting All Open Ticket List: " + TicketCacheParams.ALL_OPEN_TICKETS + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(TicketCacheParams.ALL_OPEN_TICKETS, allOpenTickets, null);
	}
	
	public static TicketModel writeTicketToCache(PersistenceManager pm, String userKey, PCHelpdeskTicket openTicket){
		TicketModel auxOpenTicket;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		Query query = pm.newQuery(PCCategory.class);
	    query.setFilter("description == descriptionParam");
	    query.declareParameters("String descriptionParam");
	    query.setRange(0, 1);
	    
	    List<PCCategory> results;
	    ArrayList<TicketAnswerModel> answers = new ArrayList<TicketAnswerModel>();
	    PCHelpdeskTicketAnswer auxTicketAnswer;
	    TicketAnswerModel auxAnswer;
	    ArrayList<Key> ticketAnswerKeys;
	    String username;
	    PCUser user;
    	PCAdmin admin;
    	String userAdminKey;
	    
		auxOpenTicket = new TicketModel();
		
		results = (List<PCCategory>)query.execute(openTicket.getCategory());
		
		if (!results.isEmpty()) {
			auxOpenTicket.setCategory(results.get(0).getDescription());
		}
		
		auxOpenTicket.setDate(openTicket.getDate());
		auxOpenTicket.setComment(openTicket.getQuestion());
		auxOpenTicket.setSubject(openTicket.getSubject());
		auxOpenTicket.setKey(KeyFactory.keyToString(openTicket.getKey()));
		
		ticketAnswerKeys = openTicket.getAnswers();
		
		for (Key ansKey : ticketAnswerKeys) {
			auxTicketAnswer = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
			
			if (auxTicketAnswer.getAdmin() == null) {
				user = pm.getObjectById(PCUser.class, auxTicketAnswer.getUser());
				username = user.getUsername();
				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getUser());
			}
			else {
				admin = pm.getObjectById(PCAdmin.class, auxTicketAnswer.getAdmin());
				username = admin.getUsername();
				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getAdmin());
			}
			
			auxAnswer = new TicketAnswerModel(dateFormatter.format(auxTicketAnswer.getDate()), userAdminKey, auxTicketAnswer.getAnswer(), username);
			answers.add(auxAnswer);
		}
		
		user = pm.getObjectById(PCUser.class, openTicket.getUser());
		auxOpenTicket.setName(user.getUsername());
		auxOpenTicket.setAnswers(answers);
		
		writeTicketToCache(auxOpenTicket);
		
		return auxOpenTicket;
	}
	
	public static void writeTicketToCache(TicketModel openTicket) {
		logger.info("Setting Open Ticket: " + TicketCacheParams.TICKET + openTicket.getKey() + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(TicketCacheParams.TICKET + openTicket.getKey(), openTicket, null);
	}
	
	public static ArrayList<TicketModel> writeClosedTicketsToCache(PersistenceManager pm, String userKey, ArrayList<PCHelpdeskTicket> closedTickets, String dummy){
		ArrayList<String> cacheClosedTicketList = new ArrayList<String>();
		TicketModel auxClosedTicket;
		ArrayList<TicketModel> closedT = new ArrayList<TicketModel>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		Query query = pm.newQuery(PCCategory.class);
	    query.setFilter("description == descriptionParam");
	    query.declareParameters("String descriptionParam");
	    query.setRange(0, 1);
	    
	    List<PCCategory> results;
	    ArrayList<TicketAnswerModel> answers = new ArrayList<TicketAnswerModel>();
	    PCHelpdeskTicketAnswer auxTicketAnswer;
	    TicketAnswerModel auxAnswer;
	    ArrayList<Key> ticketAnswerKeys;
	    String username;
	    PCUser user;
    	PCAdmin admin;
    	String userAdminKey;
	    
		for (PCHelpdeskTicket closedTicket : closedTickets) {
			auxClosedTicket = new TicketModel();
			
			results = (List<PCCategory>)query.execute(closedTicket.getCategory());
			
			if (!results.isEmpty()) {
				auxClosedTicket.setCategory(results.get(0).getDescription());
			}
			
			auxClosedTicket.setDate(closedTicket.getDate());
			auxClosedTicket.setComment(closedTicket.getQuestion());
			auxClosedTicket.setSubject(closedTicket.getSubject());
			auxClosedTicket.setKey(KeyFactory.keyToString(closedTicket.getKey()));
			
			ticketAnswerKeys = closedTicket.getAnswers();
			
			for (Key ansKey : ticketAnswerKeys) {
				auxTicketAnswer = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
    			
    			if (auxTicketAnswer.getAdmin() == null) {
    				user = pm.getObjectById(PCUser.class, auxTicketAnswer.getUser());
    				username = user.getUsername();
    				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getUser());
    			}
    			else {
    				admin = pm.getObjectById(PCAdmin.class, auxTicketAnswer.getAdmin());
    				username = admin.getUsername();
    				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getAdmin());
    			}
    			
    			auxAnswer = new TicketAnswerModel(dateFormatter.format(auxTicketAnswer.getDate()), userAdminKey, auxTicketAnswer.getAnswer(), username);
    			answers.add(auxAnswer);
    		}
			
			user = pm.getObjectById(PCUser.class, closedTicket.getUser());
			auxClosedTicket.setName(user.getUsername());
			auxClosedTicket.setAnswers(answers);
			
			closedT.add(auxClosedTicket);
			writeTicketToCache(auxClosedTicket);
			
			cacheClosedTicketList.add(auxClosedTicket.getKey());
		}
		
		logger.info("Setting Closed Ticket List: " + userKey + UserCacheParams.CLOSED_TICKETS_LIST + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(userKey + UserCacheParams.CLOSED_TICKETS_LIST, closedTickets, null);
		
		return closedT;
	}
	
	public static ArrayList<String> writeClosedTicketsToCache(PersistenceManager pm, String userKey, ArrayList<PCHelpdeskTicket> closedTickets){
		ArrayList<String> cacheClosedTicketList = new ArrayList<String>();
		TicketModel auxClosedTicket;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		Query query = pm.newQuery(PCCategory.class);
	    query.setFilter("description == descriptionParam");
	    query.declareParameters("String descriptionParam");
	    query.setRange(0, 1);
	    
	    List<PCCategory> results;
	    ArrayList<TicketAnswerModel> answers = new ArrayList<TicketAnswerModel>();
	    PCHelpdeskTicketAnswer auxTicketAnswer;
	    TicketAnswerModel auxAnswer;
	    ArrayList<Key> ticketAnswerKeys;
	    String username;
	    PCUser user;
    	PCAdmin admin;
    	String userAdminKey;
	    
		for (PCHelpdeskTicket closedTicket : closedTickets) {
			auxClosedTicket = new TicketModel();
			
			results = (List<PCCategory>)query.execute(closedTicket.getCategory());
			
			if (!results.isEmpty()) {
				auxClosedTicket.setCategory(results.get(0).getDescription());
			}
			
			auxClosedTicket.setDate(closedTicket.getDate());
			auxClosedTicket.setComment(closedTicket.getQuestion());
			auxClosedTicket.setSubject(closedTicket.getSubject());
			auxClosedTicket.setKey(KeyFactory.keyToString(closedTicket.getKey()));
			
			ticketAnswerKeys = closedTicket.getAnswers();
			
			for (Key ansKey : ticketAnswerKeys) {
				auxTicketAnswer = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
    			
    			if (auxTicketAnswer.getAdmin() == null) {
    				user = pm.getObjectById(PCUser.class, auxTicketAnswer.getUser());
    				username = user.getUsername();
    				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getUser());
    			}
    			else {
    				admin = pm.getObjectById(PCAdmin.class, auxTicketAnswer.getAdmin());
    				username = admin.getUsername();
    				userAdminKey = KeyFactory.keyToString(auxTicketAnswer.getAdmin());
    			}
    			
    			auxAnswer = new TicketAnswerModel(dateFormatter.format(auxTicketAnswer.getDate()), userAdminKey, auxTicketAnswer.getAnswer(), username);
    			answers.add(auxAnswer);
    		}
			
			user = pm.getObjectById(PCUser.class, closedTicket.getUser());
			auxClosedTicket.setName(user.getUsername());
			auxClosedTicket.setAnswers(answers);
			
			writeTicketToCache(auxClosedTicket);
			
			cacheClosedTicketList.add(auxClosedTicket.getKey());
		}
		
		logger.info("Setting Closed Ticket List: " + userKey + UserCacheParams.CLOSED_TICKETS_LIST + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(userKey + UserCacheParams.CLOSED_TICKETS_LIST, cacheClosedTicketList, null);
		
		return cacheClosedTicketList;
	}
	
	public static void writeSmartphoneAlertsToCache(Key pcSmartphoneKey, ArrayList<PCNotification> pcAlertList){
		ArrayList<NotificationModel> cacheAlertList = new ArrayList<NotificationModel>();
		String smartphoneKey = KeyFactory.keyToString(pcSmartphoneKey);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		for(PCNotification notification : pcAlertList){
			NotificationModel alertModel = new NotificationModel();
			alertModel.setDate(formatter.format(notification.getDate()));
			alertModel.setType(notification.getType());
			cacheAlertList.add(alertModel);
		}
		
		logger.info("Setting Alert List: "+smartphoneKey+SmartphoneCacheParams.ALERTS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ALERTS,cacheAlertList, null);
	}
	
	public static void writeSmartphoneAlertsToCache(String smartphoneKey, ArrayList<NotificationModel> alerts) throws IllegalArgumentException, SessionQueryException {
		logger.info("Setting Alerts: " + smartphoneKey + SmartphoneCacheParams.ALERTS + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey + SmartphoneCacheParams.ALERTS, alerts, null);
	}
	
	public static void addSmartphoneAlertsToCache(Key pcSmartphoneKey, ArrayList<PCNotification> pcAlertList){
		
		String smartphoneKey = KeyFactory.keyToString(pcSmartphoneKey);
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    
		logger.info("Finding alert list in cache");
		IdentifiableValue ident = syncCache.getIdentifiable(smartphoneKey+SmartphoneCacheParams.ALERTS);
		ArrayList<NotificationModel> cacheAlertList = null;
		if(ident==null){
			logger.info("Alerts not found in cache, creating empty list");
			cacheAlertList = new ArrayList<NotificationModel>();
		}else{
			logger.info("Alerts found in cache");
			cacheAlertList = (ArrayList<NotificationModel>)ident.getValue();
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		for(PCNotification notification : pcAlertList){
			NotificationModel alertModel = new NotificationModel();
			alertModel.setDate(formatter.format(notification.getDate()));
			alertModel.setType(notification.getType());
			cacheAlertList.add(alertModel);
		}
		
		logger.info("Setting Alert List: "+smartphoneKey+SmartphoneCacheParams.ALERTS+" to cache");
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ALERTS,cacheAlertList, null);
	}
	
	public static ModificationModel writeSmartphoneModificationToCache(String smartphoneKey, PCModification pcModification) throws IllegalArgumentException, SessionQueryException {
		ModificationModel cacheModificationModel = ModificationModelUtils.convertToModificationModel(pcModification);
		
		logger.info("Setting Modification: " + smartphoneKey + SmartphoneCacheParams.MODIFICATION + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey + SmartphoneCacheParams.MODIFICATION, cacheModificationModel, null);
		return cacheModificationModel;
	}
	
	public static void writeSmartphoneModificationToCache(String smartphoneKey, ModificationModel cacheModification) {
		logger.info("Setting Smartphone Modification: " + smartphoneKey + " to cache");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey + SmartphoneCacheParams.MODIFICATION, cacheModification);
	}
	
	public static void deleteModificationFromCache(String smartphoneKey) {
		logger.info("Deleting modification from cache: " + smartphoneKey);
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.delete(smartphoneKey + SmartphoneCacheParams.MODIFICATION);
	}
	
	public static ArrayList<RuleModel> writeSmartphoneRulesToCache(String smartphoneKey, ArrayList<PCRule> pcRules) throws IllegalArgumentException, SessionQueryException {
		ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
		RuleModel ruleModel;

		for (PCRule pcRule : pcRules) {
			ruleModel = RuleModelUtils.convertToRuleModel(pcRule);
			rules.add(ruleModel);
		}
		
		logger.info("Setting Rules List: " + smartphoneKey + SmartphoneCacheParams.RULES + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey + SmartphoneCacheParams.RULES, rules, null);
		return rules;
	}
	
	public static void addSmartphoneRuleToCache(String smartphoneKey, PCRule pcRule) {
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
				
		RuleModel ruleModel;
		ruleModel = RuleModelUtils.convertToRuleModel(pcRule);
		
		logger.info("Getting Smartphone rules from Cache");
		IdentifiableValue ident = syncCache.getIdentifiable(smartphoneKey+SmartphoneCacheParams.RULES);
		
		if(ident == null){
			logger.warning("Smartphone Rules not found in cache. Not adding rule to cache");
			return;
		}
		
		ArrayList<RuleModel> rules = (ArrayList<RuleModel>)ident.getValue();
		rules.add(ruleModel);
		
		logger.info("Setting Rules List to cache: " + smartphoneKey + SmartphoneCacheParams.RULES + " to cache");
		syncCache.put(smartphoneKey + SmartphoneCacheParams.RULES, rules, null);
	}
	
	public static void writeSmartphoneRulesToCache(ArrayList<RuleModel> cacheRules, String smartphoneKey) {
		logger.info("Setting Smartphone Rules: " + smartphoneKey + " to cache");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey + SmartphoneCacheParams.RULES, cacheRules);
	}
	
	public static ArrayList<SimpleContactModel> writeSmartphoneActiveContactsToCache(String smartphoneKey, ArrayList<PCSimpleContact> pcActiveContactsList, ArrayList<PCPhone> pcActiveContactsPhoneList){
		int count = 0;
		ArrayList<SimpleContactModel> cacheContactList = new ArrayList<SimpleContactModel>();
		for(PCSimpleContact simpleContact : pcActiveContactsList){
			SimpleContactModel simpleModel = new SimpleContactModel();
			simpleModel.setFirstName(simpleContact.getFirstName());
			simpleModel.setLastName(simpleContact.getLastName());
			simpleModel.setKeyId(KeyFactory.keyToString(simpleContact.getKey()));
			
			PCPhone phone = pcActiveContactsPhoneList.get(count);
			PhoneModel phoneModel = new PhoneModel();
			phoneModel.setPhoneNumber(phone.getPhoneNumber());
			phoneModel.setType(phone.getType());
			
			
			ArrayList<PhoneModel> phones = new ArrayList<PhoneModel>();
			phones.add(phoneModel);
			simpleModel.setPhones(phones);
			
			cacheContactList.add(simpleModel);
			count+=1;
		}
		
		logger.info("Setting Active List: "+smartphoneKey+SmartphoneCacheParams.ACTIVE_CONTACTS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ACTIVE_CONTACTS,cacheContactList, null);
		return cacheContactList;
	
	}
	
	public static void writeSmartphoneActiveContactsToCache(String smartphoneKey, ArrayList<SimpleContactModel> cacheActiveContacts) {
		logger.info("Setting Smartphone Active Contacts: " + smartphoneKey + " to cache");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey + SmartphoneCacheParams.ACTIVE_CONTACTS, cacheActiveContacts);
	}
	
	public static ArrayList<SimpleContactModel> writeSmartphoneInactiveContactsToCache(String smartphoneKey, ArrayList<PCSimpleContact> pcInactiveContactsList,  ArrayList<PCPhone> pcInactiveContactsPhoneList){
		int count = 0;
		ArrayList<SimpleContactModel> cacheContactList = new ArrayList<SimpleContactModel>();
		for(PCSimpleContact simpleContact : pcInactiveContactsList){
			SimpleContactModel simpleModel = new SimpleContactModel();
			simpleModel.setFirstName(simpleContact.getFirstName());
			simpleModel.setLastName(simpleContact.getLastName());
			simpleModel.setKeyId(KeyFactory.keyToString(simpleContact.getKey()));
			
			PCPhone phone = pcInactiveContactsPhoneList.get(count);
			PhoneModel phoneModel = new PhoneModel();
			phoneModel.setPhoneNumber(phone.getPhoneNumber());
			phoneModel.setType(phone.getType());
			
			ArrayList<PhoneModel> phones = new ArrayList<PhoneModel>();
			phones.add(phoneModel);
			simpleModel.setPhones(phones);
			
			cacheContactList.add(simpleModel);
			count+=1;
		}
		
		logger.info("Setting Inactive List: "+smartphoneKey+SmartphoneCacheParams.INACTIVE_CONTACTS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.INACTIVE_CONTACTS,cacheContactList, null);
		return cacheContactList;
	}
	
	public static void writeSmartphoneInactiveContactsToCache(String smartphoneKey, ArrayList<SimpleContactModel> cacheInactiveContacts) {
		logger.info("Setting Smartphone Inactive Contacts: " + smartphoneKey + " to cache");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey + SmartphoneCacheParams.INACTIVE_CONTACTS, cacheInactiveContacts);
	}
	
	public static ArrayList<EmergencyNumberModel> writeSmartphoneAddedEmergencyNumbersToCache(String smartphoneKey, ArrayList<PCEmergencyNumber> pcAddedEmergencyList){
		ArrayList<EmergencyNumberModel> cacheContactList = new ArrayList<EmergencyNumberModel>();
		for(PCEmergencyNumber simpleContact : pcAddedEmergencyList){
			EmergencyNumberModel simpleModel = new EmergencyNumberModel();
			simpleModel.setCountry(simpleContact.getCountry());
			simpleModel.setDescription(simpleContact.getDescription());
			simpleModel.setKeyId(KeyFactory.keyToString(simpleContact.getKey()));
			simpleModel.setNumber(simpleContact.getNumber());
		}
		
		logger.info("Setting Added Emergency List: "+smartphoneKey+SmartphoneCacheParams.ADD_EMERGENCY_NUMBERS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ADD_EMERGENCY_NUMBERS,cacheContactList, null);
		return cacheContactList;
	}
	
	public static void writeSmartphoneAddedEmergencyNumbersToCache(ArrayList<EmergencyNumberModel> cacheAddedEmergencyNumbers, String smartphoneKey) {
		logger.info("Setting Smartphone Added EN: " + smartphoneKey + " to cache");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey + SmartphoneCacheParams.ADD_EMERGENCY_NUMBERS, cacheAddedEmergencyNumbers);
	}
	
	public static ArrayList<EmergencyNumberModel> writeSmartphoneDeletedEmergencyNumbersToCache(String smartphoneKey, ArrayList<PCEmergencyNumber> pcDeletedEmergencyList){
		ArrayList<EmergencyNumberModel> cacheContactList = new ArrayList<EmergencyNumberModel>();
		for(PCEmergencyNumber simpleContact : pcDeletedEmergencyList){
			EmergencyNumberModel simpleModel = new EmergencyNumberModel();
			simpleModel.setCountry(simpleContact.getCountry());
			simpleModel.setDescription(simpleContact.getDescription());
			simpleModel.setKeyId(KeyFactory.keyToString(simpleContact.getKey()));
			simpleModel.setNumber(simpleContact.getNumber());
		}
		
		logger.info("Setting Deleted Emergency Numbers List: "+smartphoneKey+SmartphoneCacheParams.DELETE_EMERGENCY_NUMBERS+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.DELETE_EMERGENCY_NUMBERS,cacheContactList, null);
		return cacheContactList;
	}
	
	public static void writeSmartphoneDeletedEmergencyNumbersToCache(ArrayList<EmergencyNumberModel> cacheDeletedEmergencyNumbers, String smartphoneKey) {
		logger.info("Setting Smartphone Deleted EN: " + smartphoneKey + " to cache");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey + SmartphoneCacheParams.DELETE_EMERGENCY_NUMBERS, cacheDeletedEmergencyNumbers);
	}
	
	public static ArrayList<RouteModel> writeSmartphoneRoutesToCache(String smartphoneKey, ArrayList<PCRoute> routeList){
		ArrayList<RouteModel> cacheContactList = new ArrayList<RouteModel>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		for(PCRoute route : routeList){
			RouteModel routeModel = new RouteModel();
			try{
				routeModel.setDate(formatter.format(route.getDate()));
			} catch (Exception e){
				logger.warning("Could not parse Date correctly: "+route.getDate());
				routeModel.setDate("00/00/0000 00:00:00 AM");
			}
			routeModel.setKeyId(KeyFactory.keyToString(route.getKey()));
			
			ArrayList<LocationModel> locations = new ArrayList<LocationModel>();
			for(GeoPt point: route.getRoute()){
				LocationModel locationModel = new LocationModel();
				locationModel.setLatitude(String.valueOf(point.getLatitude()));
				locationModel.setLongitude(String.valueOf(point.getLongitude()));
				locations.add(locationModel);
			}
			routeModel.setPoints(locations);
			cacheContactList.add(routeModel);
		}
		
		logger.info("Setting Route List: "+smartphoneKey+SmartphoneCacheParams.ROUTES+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ROUTES,cacheContactList, null);
		return cacheContactList;
	}
	
	public static void writeSmartphoneRoutesToCache(ArrayList<RouteModel> cacheRoutes, String smartphoneKey) {
		logger.info("Setting Smartphone Routes: " + smartphoneKey + " to cache");
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey + SmartphoneCacheParams.ROUTES, cacheRoutes);
	}

	public static void writeSmartphonePropertiesToCache(String smartphoneKey,
			ArrayList<PCProperty> propertyList) {
		ArrayList<PropertyModel> cachePropertyList = new ArrayList<PropertyModel>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		for (PCProperty property : propertyList) {
			PropertyModel propModel = new PropertyModel();

			try {
				propModel.setCreationDate(formatter.format(property
						.getCreationDate()));
			} catch (Exception e) {
				logger.warning("Could not parse Date correctly: "+ property.getCreationDate());
				propModel.setCreationDate("00/00/0000 00:00:00 AM");
			}

			propModel.setDescription(property.getDescription());
			propModel.setId(property.getId());
			propModel.setKeyId(KeyFactory.keyToString(property.getKey()));
			propModel.setValue(property.getValue());
			cachePropertyList.add(propModel);
		}

		logger.info("Setting Property List: " + smartphoneKey
				+ SmartphoneCacheParams.PROPERTIES + " to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.put(smartphoneKey + SmartphoneCacheParams.PROPERTIES,
				cachePropertyList, null);
	}

	public static void writeSmartphoneRulesToCache(String smartphoneKey, ArrayList<PCRule> ruleList, ArrayList<ArrayList<Integer>> disabledFuncionalities){
		ArrayList<RuleModel> cacheRuleList = new ArrayList<RuleModel>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		int count=0;
		
		for(PCRule rule : ruleList){
			RuleModel ruleModel = new RuleModel();

			try{
				ruleModel.setCreationDate(formatter.format(rule.getCreationDate()));
			} catch (Exception e){
				logger.severe("Could not parse Date correctly: "+rule.getCreationDate()+" Not adding rule to list");
				count+=1;
				continue;
			}	
			
			try{
				ruleModel.setEndDate(formatter.format(rule.getEndDate()));
			} catch (Exception e){
				logger.severe("Could not parse Date correctly: "+rule.getEndDate()+" Not adding rule to list");
				count+=1;
				continue;
			}	
			
			try{
				ruleModel.setStartDate(formatter.format(rule.getStartDate()));
			} catch (Exception e){
				logger.severe("Could not parse Date correctly: "+rule.getStartDate()+" Not adding rule to list");
				count+=1;
				continue;
			}			

			ruleModel.setDisabledFunctionalities(disabledFuncionalities.get(count));

			ruleModel.setKeyId(KeyFactory.keyToString(rule.getKey()));
			ruleModel.setName(rule.getName());
			ruleModel.setType(rule.getType());
			
			cacheRuleList.add(ruleModel);
			count+=1;
		}
		
		logger.info("Setting Rules List: "+smartphoneKey+SmartphoneCacheParams.RULES+" to cache");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.RULES,cacheRuleList, null);
	}	
	
	public static void writeSessionToCache(PCSession session, PCUser user){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		SessionCacheModel sessionCache = new SessionCacheModel();
		sessionCache.setCookieId(session.getCookieId());
		sessionCache.setKey(KeyFactory.keyToString(session.getKey()));
		sessionCache.setLastUpdate(formatter.format(session.getLastUpdate()));
		
		UserModel userModel = new UserModel();
		userModel.setEmail(user.getEmail());
		userModel.setKey(KeyFactory.keyToString(user.getKey()));
		userModel.setName(user.getName());
		userModel.setPass(user.getPassword());
		userModel.setUsr(user.getUsername());
		
		ArrayList<String> smartphoneKeys = new ArrayList<String>();
		for(Key key : user.getSmartphones()){
			String keyString = KeyFactory.keyToString(key);
			smartphoneKeys.add(keyString);
		}
		userModel.setSmartphoneKeys(smartphoneKeys);
		
		sessionCache.setUserModel(userModel);
		
		logger.info("Setting Session in cache: "+UserCacheParams.SESSION+session.getCookieId());
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(UserCacheParams.SESSION+session.getCookieId(), sessionCache, null);
	}
	
	public static void writeSessionToCache(PCSession session, UserModel userModel){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		SessionCacheModel sessionCache = new SessionCacheModel();
		sessionCache.setCookieId(session.getCookieId());
		sessionCache.setKey(KeyFactory.keyToString(session.getKey()));
		sessionCache.setLastUpdate(formatter.format(session.getLastUpdate()));
		
		sessionCache.setUserModel(userModel);
		
		logger.info("Setting Session in cache: "+UserCacheParams.SESSION+session.getCookieId());
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(UserCacheParams.SESSION+session.getCookieId(), sessionCache, null);
	}

	
	public static UserModel writeUserToCache(PCUser user){
		UserModel userModel = new UserModel();
		userModel.setEmail(user.getEmail());
		userModel.setKey(KeyFactory.keyToString(user.getKey()));
		userModel.setName(user.getName());
		userModel.setPass(user.getPassword());
		userModel.setUsr(user.getUsername());
		
		ArrayList<String> smartphoneKeys = new ArrayList<String>();
		for(Key key : user.getSmartphones()){
			String keyString = KeyFactory.keyToString(key);
			smartphoneKeys.add(keyString);
		}
		userModel.setSmartphoneKeys(smartphoneKeys);
		
		logger.info("Setting User in cache: "+UserCacheParams.USER+userModel.getUsr()+"-"+userModel.getPass()+" number of smartphone: "+userModel.getSmartphoneKeys().size());
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(UserCacheParams.USER+userModel.getUsr()+"-"+userModel.getPass(), userModel, null);
		
		return userModel;
	}
	
	public static UserModel writeAdminUserToCache(PCAdmin user){
		UserModel userModel = new UserModel();
		userModel.setKey(KeyFactory.keyToString(user.getKey()));
		userModel.setPass(user.getPassword());
		userModel.setUsr(user.getUsername());
		
		logger.info("Setting Admin User in cache: "+UserCacheParams.ADMIN+userModel.getUsr()+"-"+userModel.getPass());
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();		
		syncCache.put(UserCacheParams.ADMIN+userModel.getUsr()+"-"+userModel.getPass(), userModel, null);
		
		return userModel;
	}
	
	public static void addUserAdminToCache(String userKey, String username, String userPassword, PCUser user, UserModel adminUserModel){
		logger.info("Finding PCUser in cache by userKey: "+userKey);
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		IdentifiableValue ident = syncCache.getIdentifiable(UserCacheParams.USER+username+"-"+userPassword);
		
		if(ident==null){
			logger.info("User not present in cache, writing user to cache first: "+username);
			WriteToCache.writeUserToCache(user);
		}
		
		logger.info("Finding user Admin List by userKey: "+userKey);
		ident = syncCache.getIdentifiable(UserCacheParams.USER+username+"-"+userPassword);
		syncCache.getIdentifiable(UserCacheParams.USER+KeyFactory.keyToString(user.getKey())+UserCacheParams.ADMIN_LIST);
		
		ArrayList<UserModel> adminList;
		if(ident ==null){
			logger.info("Admin list not found for user, creating new admin list" +userKey);
			adminList = new ArrayList<UserModel>();
		}else{
			adminList = (ArrayList<UserModel>)ident.getValue();
		}	
		
		adminList.add(adminUserModel);

		logger.info("Writing admin list to cache: "+UserCacheParams.USER+userKey+UserCacheParams.ADMIN_LIST);
		syncCache.put(UserCacheParams.USER+userKey+UserCacheParams.ADMIN_LIST, adminList, null);
				
	}
}


