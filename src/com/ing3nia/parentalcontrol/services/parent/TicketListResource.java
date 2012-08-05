package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.models.cache.TicketCacheParams;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("tickets")
public class TicketListResource {
	private static Logger logger = Logger.getLogger(TicketListResource.class.getName());
	
	public TicketListResource() {
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(@QueryParam(value = "user") final String loggedUserKey) {
		logger.info("[Tickets List Service] Version 1-2-22");
		
		ArrayList<TicketModel> openedTicketsList = new ArrayList<TicketModel>();
		ArrayList<TicketModel> closedTicketsList = new ArrayList<TicketModel>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCUser user;
		ResponseBuilder rbuilder;

		logger.info("[Tickets List Service] Buscando user asociado a id: " + loggedUserKey);
		
		try {
			user = getUser(loggedUserKey, pm);
			
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			String openTicketsCacheKey = loggedUserKey + UserCacheParams.OPEN_TICKETS_LIST;
			IdentifiableValue cacheIdentOpenTickets = (IdentifiableValue) syncCache.getIdentifiable(openTicketsCacheKey);
			ArrayList<String> cacheOpenTickets = null;
			ArrayList<Key> openTicketsKeys = user.getOpenTickets();
			
			if (cacheIdentOpenTickets == null) {
				ArrayList<PCHelpdeskTicket> openTickets = new ArrayList<PCHelpdeskTicket>();
				PCHelpdeskTicket auxOpenTicket;
				
				for (Key key : openTicketsKeys) {
					auxOpenTicket = pm.getObjectById(PCHelpdeskTicket.class, key);
					openTickets.add(auxOpenTicket);
				}
				
				WriteToCache.writeOpenTicketsToCache(pm, loggedUserKey, openTickets);
				
				cacheIdentOpenTickets = syncCache.getIdentifiable(openTicketsCacheKey);
				cacheOpenTickets = (ArrayList<String>) cacheIdentOpenTickets.getValue();
			}
			else {
				cacheOpenTickets = (ArrayList<String>) cacheIdentOpenTickets.getValue();
			}
			
			String ticketCacheKey = TicketCacheParams.TICKET;
			IdentifiableValue cacheIdentOpenTicket;
			TicketModel cacheOpenTicket = null;
			Key tickKey;
			PCHelpdeskTicket ticket;
			
			for (String ticketId : cacheOpenTickets) {
				cacheIdentOpenTicket = (IdentifiableValue) syncCache.getIdentifiable(ticketCacheKey + ticketId);
				cacheOpenTicket = (TicketModel)cacheIdentOpenTicket.getValue();
				
				if (cacheOpenTicket == null) {
					tickKey = KeyFactory.stringToKey(ticketId);			
					ticket = (PCHelpdeskTicket)pm.getObjectById(PCHelpdeskTicket.class, tickKey);
					
					openedTicketsList.add(WriteToCache.writeTicketToCache(pm, KeyFactory.keyToString(ticket.getUser()), ticket));
				}
				else {
					openedTicketsList.add(cacheOpenTicket);
				}
			}
			
			String closedTicketsCacheKey = loggedUserKey + UserCacheParams.CLOSED_TICKETS_LIST;
			IdentifiableValue cacheIdentClosedTickets = (IdentifiableValue) syncCache.getIdentifiable(closedTicketsCacheKey);
			ArrayList<String> cacheClosedTickets = null;
			ArrayList<Key> closedTicketsKeys = user.getClosedTickets();
			
			if (cacheIdentClosedTickets == null) {
				ArrayList<PCHelpdeskTicket> closedTickets = new ArrayList<PCHelpdeskTicket>();
				PCHelpdeskTicket auxClosedTicket;
				
				for (Key key : closedTicketsKeys) {
					auxClosedTicket = pm.getObjectById(PCHelpdeskTicket.class, key);
					closedTickets.add(auxClosedTicket);
				}
				
				WriteToCache.writeClosedTicketsToCache(pm, loggedUserKey, closedTickets);
				
				cacheIdentClosedTickets = syncCache.getIdentifiable(closedTicketsCacheKey);
				cacheClosedTickets = (ArrayList<String>) cacheIdentClosedTickets.getValue();
			}
			else {
				cacheClosedTickets = (ArrayList<String>) cacheIdentClosedTickets.getValue();
			}
			
			IdentifiableValue cacheIdentClosedTicket;
			TicketModel cacheClosedTicket = null;
			
			for (String ticketId : cacheClosedTickets) {
				cacheIdentClosedTicket = (IdentifiableValue) syncCache.getIdentifiable(ticketCacheKey + ticketId);
				cacheClosedTicket = (TicketModel)cacheIdentClosedTicket.getValue();
				
				if (cacheClosedTicket == null) {
					tickKey = KeyFactory.stringToKey(ticketId);			
					ticket = (PCHelpdeskTicket)pm.getObjectById(PCHelpdeskTicket.class, tickKey);
					
					closedTicketsList.add(WriteToCache.writeTicketToCache(pm, KeyFactory.keyToString(ticket.getUser()), ticket));
				}
				else {
					closedTicketsList.add(cacheClosedTicket);
				}
			}
			
//			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
//			String openTicketsCacheKey = loggedUserKey + UserCacheParams.OPEN_TICKETS_LIST;
//			IdentifiableValue cacheIdentOpenTickets = (IdentifiableValue) syncCache.getIdentifiable(openTicketsCacheKey);
//			ArrayList<String> cacheOpenTickets = null;
//			ArrayList<Key> openTicketsKeys = user.getOpenTickets();
//			
//			if (cacheIdentOpenTickets == null) {
//				ArrayList<PCHelpdeskTicket> openTickets = new ArrayList<PCHelpdeskTicket>();
//
//				for (Key tKey : openTicketsKeys) {
//		    		t = pm.getObjectById(PCHelpdeskTicket.class, tKey);
//		    		openTickets.add(t);
//		    		
////		    		category = (PCCategory)pm.getObjectById(PCCategory.class, t.getCategory());
////		    		answers = new ArrayList<TicketAnswerModel>();
////		    		
////		    		for (Key ansKey : t.getAnswers()) {
////		    			ans = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
////		    			
////		    			if (ans.getAdmin() == null) {
////		    				userR = pm.getObjectById(PCUser.class, ans.getUser());
////		    				username = userR.getUsername();
////		    				userAdminKey = KeyFactory.keyToString(ans.getUser());
////		    			}
////		    			else {
////		    				username = "Admin";
////		    				userAdminKey = KeyFactory.keyToString(ans.getAdmin());
////		    			}
////		    			
////		    			auxAnswer = new TicketAnswerModel(formatter.format(ans.getDate()), userAdminKey, ans.getAnswer(), username);
////		    			answers.add(auxAnswer);
////		    		}
////		    		
////		    		auxTicket = new TicketModel(KeyFactory.keyToString(t.getKey()), category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers);
////		    		
////		    		openedTickets.add(auxTicket);
//		    	}
//				
//				openedTicketsList = WriteToCache.writeOpenTicketsToCache(pm, loggedUserKey, openTickets, "");
//			}
//			else {
//				cacheOpenTickets = (ArrayList<String>) cacheIdentOpenTickets.getValue();
//				
//				String openTicketsCacheKey = loggedUserKey + UserCacheParams.OPEN_TICKETS_LIST;
//				IdentifiableValue cacheIdentOpenTickets = (IdentifiableValue) syncCache.getIdentifiable(openTicketsCacheKey);
//				ArrayList<String> cacheOpenTickets = null;
//				ArrayList<Key> openTicketsKeys = user.getOpenTickets();
//				
//				for (String ticketId : cacheOpenTickets) {
//					
//				}
//			}
			
//			logger.info("[Tickets List Service] Executing query to search tickets.");
//		    
//	    	for (Key tKey : user.getClosedTickets()) {
//	    		t = pm.getObjectById(PCHelpdeskTicket.class, tKey);
//	    		
//	    		category = (PCCategory)pm.getObjectById(PCCategory.class, t.getCategory());
//	    		answers = new ArrayList<TicketAnswerModel>();
//	    		
//	    		for (Key ansKey : t.getAnswers()) {
//	    			ans = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
//	    			
//	    			if (ans.getAdmin() == null) {
//	    				userR = pm.getObjectById(PCUser.class, ans.getUser());
//	    				username = userR.getUsername();
//	    				userAdminKey = KeyFactory.keyToString(ans.getUser());
//	    			}
//	    			else {
//	    				username = "Admin";
//	    				userAdminKey = KeyFactory.keyToString(ans.getAdmin());
//	    			}
//	    			
//	    			auxAnswer = new TicketAnswerModel(formatter.format(ans.getDate()), userAdminKey, ans.getAnswer(), username);
//	    			answers.add(auxAnswer);
//	    		}
//	    		
//	    		auxTicket = new TicketModel(KeyFactory.keyToString(t.getKey()), category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers);
//	    		
//	    		closedTickets.add(auxTicket);
//	    	}
		}
		catch (SessionQueryException ex) {
			logger.warning("[Tickets List Service] An error ocurred while searching for user. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Tickets List Service] An error ocurred while searching for user. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Tickets List Service] Unexpected error occurred. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.UNEXPECTED_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		finally {
			pm.close();
		}
		
		logger.info("[Tickets List Service] Ok Response. Ticket lists succesfully sent.");
		
		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		Gson jsonBuilder = new Gson();
		JsonParser jsonParser = new JsonParser();
		Type type = new TypeToken<ArrayList<TicketModel>>(){}.getType();
		JsonArray openTicketsObject = (JsonArray)jsonParser.parse(jsonBuilder.toJson(openedTicketsList, type));
		JsonArray closedTicketsObject = (JsonArray)jsonParser.parse(jsonBuilder.toJson(closedTicketsList, type));
		JsonObject ticketsObject = new JsonObject();
		ticketsObject.add("open", openTicketsObject);
		ticketsObject.add("closed", closedTicketsObject);
		okResponse.add("tickets", ticketsObject);
		
		rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
		return rbuilder.build();
	}
	
	public PCUser getUser(String id, PersistenceManager pm) throws SessionQueryException, IllegalArgumentException {
		try {
			logger.info("[Tickets List Service] Converting id : " + id + " of user to Key.");
			Key userKey = KeyFactory.stringToKey(id);
						
			logger.info("[Tickets List Service] Searching for user in DB.");
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);					
			
			return user;
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
