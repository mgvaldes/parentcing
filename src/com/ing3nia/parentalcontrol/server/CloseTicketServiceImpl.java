package com.ing3nia.parentalcontrol.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.models.cache.TicketCacheParams;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.CloseTicketService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class CloseTicketServiceImpl extends RemoteServiceServlet implements CloseTicketService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CloseTicketServiceImpl.class.getName());
	
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	public CloseTicketServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean closeTicket(String ticketKey, String loggedUserKey) {
		if (ACTUAL.equals(OLD_WS)) {
			return oldCloseTicket(ticketKey, loggedUserKey);
		}
		else {
			return newCloseTicket(ticketKey, loggedUserKey);
		}
	}
	
	public Boolean newCloseTicket(String ticketKey, String loggedUserKey) {
		boolean result = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		boolean isAdmin = false;
		
		try {					
			logger.info("[Close Ticket Service] Searching for user.");
			PCUser user = pm.getObjectById(PCUser.class, loggedUserKey);
			
			if (user != null) {
				logger.info("[Close Ticket Service] Logged user found. It is not admin.");
				
				Key closingTicketKey = KeyFactory.stringToKey(ticketKey);
				user.getOpenTickets().remove(closingTicketKey);
				
				if (user.getClosedTickets() == null) {
					user.setClosedTickets(new ArrayList<Key>());
				}
				
				user.getClosedTickets().add(closingTicketKey);
				
				MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
				String openTicketsCacheKey = loggedUserKey + UserCacheParams.OPEN_TICKETS_LIST;
				IdentifiableValue cacheIdentOpenTickets = (IdentifiableValue) syncCache.getIdentifiable(openTicketsCacheKey);
				ArrayList<String> cacheOpenTickets = null;
				ArrayList<Key> openTicketsKeys = user.getOpenTickets();
				
				if (cacheIdentOpenTickets == null) {
					logger.info("[Close Ticket Service] User Open Tickets ARE NOT in cache");
					
					ArrayList<PCHelpdeskTicket> openTickets = new ArrayList<PCHelpdeskTicket>();
					PCHelpdeskTicket auxOpenTicket;
					
					for (Key key : openTicketsKeys) {
						auxOpenTicket = pm.getObjectById(PCHelpdeskTicket.class, key);
						openTickets.add(auxOpenTicket);
					}
					
					cacheOpenTickets = WriteToCache.writeOpenTicketsToCache(pm, loggedUserKey, openTickets);
				}
				else {
					logger.info("[Close Ticket Service] User Open Tickets ARE in cache");
					
					cacheOpenTickets = (ArrayList<String>) cacheIdentOpenTickets.getValue();
				}
				
				cacheOpenTickets.remove(ticketKey);				
				WriteToCache.writeOpenTicketsToCache(cacheOpenTickets, loggedUserKey);
				
				String closedTicketsCacheKey = loggedUserKey + UserCacheParams.CLOSED_TICKETS_LIST;
				IdentifiableValue cacheIdentClosedTickets = (IdentifiableValue) syncCache.getIdentifiable(closedTicketsCacheKey);
				ArrayList<String> cacheClosedTickets = null;
				ArrayList<Key> closedTicketsKeys = user.getClosedTickets();
				
				if (cacheIdentClosedTickets == null) {
					logger.info("[Close Ticket Service] User Closed Tickets ARE NOT in cache");
					
					ArrayList<PCHelpdeskTicket> closedTickets = new ArrayList<PCHelpdeskTicket>();
					PCHelpdeskTicket auxClosedTicket;
					
					for (Key key : closedTicketsKeys) {
						auxClosedTicket = pm.getObjectById(PCHelpdeskTicket.class, key);
						closedTickets.add(auxClosedTicket);
					}
					
					cacheClosedTickets = WriteToCache.writeClosedTicketsToCache(pm, loggedUserKey, closedTickets);
				}
				else {
					logger.info("[Close Ticket Service] User Closed Tickets ARE in cache");
					
					cacheClosedTickets = (ArrayList<String>) cacheIdentClosedTickets.getValue();
				}
				
				cacheClosedTickets.add(ticketKey);
				WriteToCache.writeClosedTicketsToCache(cacheClosedTickets, loggedUserKey);
			}
		}
		catch (Exception ex) {
			isAdmin = true;
		}
		
		try {
			logger.info("[Close Ticket Service] Closing ticket.");
			
			PCHelpdeskTicket ticket = pm.getObjectById(PCHelpdeskTicket.class, ticketKey);
			ticket.setStatus(false);
			
			if (isAdmin) {
				PCUser auxUser = pm.getObjectById(PCUser.class, ticket.getUser());
				
				Key closingTicketKey = KeyFactory.stringToKey(ticketKey);
				auxUser.getOpenTickets().remove(closingTicketKey);
				
				if (auxUser.getClosedTickets() == null) {
					auxUser.setClosedTickets(new ArrayList<Key>());
				}
				
				auxUser.getClosedTickets().add(closingTicketKey);
				
				MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
				String openTicketsCacheKey = KeyFactory.keyToString(ticket.getUser()) + UserCacheParams.OPEN_TICKETS_LIST;
				IdentifiableValue cacheIdentOpenTickets = (IdentifiableValue) syncCache.getIdentifiable(openTicketsCacheKey);
				ArrayList<String> cacheOpenTickets = null;
				ArrayList<Key> openTicketsKeys = auxUser.getOpenTickets();
				
				if (cacheIdentOpenTickets == null) {
					ArrayList<PCHelpdeskTicket> openTickets = new ArrayList<PCHelpdeskTicket>();
					PCHelpdeskTicket auxOpenTicket;
					
					for (Key key : openTicketsKeys) {
						auxOpenTicket = pm.getObjectById(PCHelpdeskTicket.class, key);
						openTickets.add(auxOpenTicket);
					}
					
					WriteToCache.writeOpenTicketsToCache(pm, KeyFactory.keyToString(auxUser.getKey()), openTickets);
					
					cacheIdentOpenTickets = syncCache.getIdentifiable(openTicketsCacheKey);
					cacheOpenTickets = (ArrayList<String>) cacheIdentOpenTickets.getValue();
				}
				else {
					cacheOpenTickets = (ArrayList<String>) cacheIdentOpenTickets.getValue();
				}
				
				cacheOpenTickets.remove(ticketKey);
				
				WriteToCache.writeOpenTicketsToCache(cacheOpenTickets, KeyFactory.keyToString(ticket.getUser()));
				
				String closedTicketsCacheKey = KeyFactory.keyToString(ticket.getUser()) + UserCacheParams.CLOSED_TICKETS_LIST;
				IdentifiableValue cacheIdentClosedTickets = (IdentifiableValue) syncCache.getIdentifiable(closedTicketsCacheKey);
				ArrayList<String> cacheClosedTickets = null;
				ArrayList<Key> closedTicketsKeys = auxUser.getClosedTickets();
				
				if (cacheIdentClosedTickets == null) {
					ArrayList<PCHelpdeskTicket> closedTickets = new ArrayList<PCHelpdeskTicket>();
					PCHelpdeskTicket auxClosedTicket;
					
					for (Key key : closedTicketsKeys) {
						auxClosedTicket = pm.getObjectById(PCHelpdeskTicket.class, key);
						closedTickets.add(auxClosedTicket);
					}
					
					WriteToCache.writeClosedTicketsToCache(pm, KeyFactory.keyToString(auxUser.getKey()), closedTickets);
					
					cacheIdentClosedTickets = syncCache.getIdentifiable(closedTicketsCacheKey);
					cacheClosedTickets = (ArrayList<String>) cacheIdentClosedTickets.getValue();
				}
				else {
					cacheClosedTickets = (ArrayList<String>) cacheIdentClosedTickets.getValue();
				}
				
				cacheClosedTickets.add(ticketKey);
				
				WriteToCache.writeClosedTicketsToCache(cacheClosedTickets, KeyFactory.keyToString(ticket.getUser()));
			}
			
			logger.info("[Close Ticket Service] Ok Response. Ticket succesfully closed.");
			
			result = true;
			
//			removeFromAllAdminOpenTickets(ticketKey, pm);
//			addToAllAdminClosedTickets(ticketKey, pm);
		}
		catch (Exception ex) {
			logger.warning("[Close Ticket Service] An error ocurred while closing ticket. " + ex.getMessage());
			
			result = false;
		}
		finally {
			pm.close();
		}
		
		return result;
	}
	
	public void removeFromAllAdminOpenTickets(String newTicketKey, PersistenceManager pm) {
		ArrayList<TicketModel> openTickets = new ArrayList<TicketModel>();
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		String allOpenTicketsCacheKey = TicketCacheParams.ALL_OPEN_TICKETS;
		IdentifiableValue cacheIdentAllOpenTickets = (IdentifiableValue) syncCache.getIdentifiable(allOpenTicketsCacheKey);
		ArrayList<String> cacheAllOpenTickets = null;
		
		String ticketCacheKey = TicketCacheParams.TICKET;
		IdentifiableValue cacheIdentTicket;
		
		if (cacheIdentAllOpenTickets == null) {
			cacheAllOpenTickets = new ArrayList<String>();
			
			Query query = pm.newQuery(PCHelpdeskTicket.class);
		    query.setFilter("status == statusParam");
		    query.setOrdering("date desc");
		    query.declareParameters("Boolean statusParam");

		    try {
		        List<PCHelpdeskTicket> results = (List<PCHelpdeskTicket>) query.execute(true);
		        
		        if (!results.isEmpty()) {
		        	openTickets = new ArrayList<TicketModel>();
		        	TicketModel auxTicket;
			    	PCCategory category;
			    	ArrayList<TicketAnswerModel> answers;
			    	TicketAnswerModel auxAnswer;
			    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			    	String username;
			    	PCUser auxUser;
			    	PCHelpdeskTicketAnswer ans;
			    	PCUser user;
			    	PCAdmin admin;
			    	String userAdminKey;
		        	
		            for (PCHelpdeskTicket t : results) {
		            	category = (PCCategory)pm.getObjectById(PCCategory.class, t.getCategory());
			    		answers = new ArrayList<TicketAnswerModel>();
			    		
			    		for (Key ansKey : t.getAnswers()) {
			    			ans = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
			    			
			    			if (ans.getAdmin() == null) {
			    				user = pm.getObjectById(PCUser.class, ans.getUser());
			    				username = user.getUsername();
			    				userAdminKey = KeyFactory.keyToString(ans.getUser());
			    			}
			    			else {
			    				admin = pm.getObjectById(PCAdmin.class, ans.getAdmin());
			    				username = admin.getUsername();
			    				userAdminKey = KeyFactory.keyToString(ans.getAdmin());
			    			}
			    			
			    			auxAnswer = new TicketAnswerModel(formatter.format(ans.getDate()), userAdminKey, ans.getAnswer(), username);
			    			answers.add(auxAnswer);
			    		}
			    		
			    		auxUser = pm.getObjectById(PCUser.class, t.getUser());
			    		auxTicket = new TicketModel(KeyFactory.keyToString(t.getKey()), category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers, auxUser.getUsername());
			    		
			    		cacheAllOpenTickets.add(KeyFactory.keyToString(t.getKey()));
			    		
			    		cacheIdentTicket = (IdentifiableValue) syncCache.getIdentifiable(ticketCacheKey + KeyFactory.keyToString(t.getKey()));
			    		
			    		if (cacheIdentTicket == null) {
			    			WriteToCache.writeTicketToCache(auxTicket);
			    		}
			    		
			    		openTickets.add(auxTicket);
		            }
		        }
		    } 
		    catch (Exception ex) {
		    	logger.info("[AdminClosedTicketList] An error occured: " + ex.getMessage());
		    	openTickets = null;
		    }
		    finally {
		        query.closeAll();
		    }
			
			WriteToCache.writeAllOpenTicketsToCache(cacheAllOpenTickets);
		}
		else {
			cacheAllOpenTickets = (ArrayList<String>) cacheIdentAllOpenTickets.getValue();
			
			Key tickKey;
			PCHelpdeskTicket ticket;
			
			for (String ticketId : cacheAllOpenTickets) {
				cacheIdentTicket = (IdentifiableValue) syncCache.getIdentifiable(ticketCacheKey + ticketId);
				
				if (cacheIdentTicket == null) {
					tickKey = KeyFactory.stringToKey(ticketId);
					ticket = (PCHelpdeskTicket)pm.getObjectById(PCHelpdeskTicket.class, tickKey);
					
					openTickets.add(WriteToCache.writeTicketToCache(pm, KeyFactory.keyToString(ticket.getUser()), ticket));
	    		}
				else {
					openTickets.add((TicketModel)cacheIdentTicket.getValue());
				}
			}
		}
		
		cacheAllOpenTickets.remove(newTicketKey);
		WriteToCache.writeAllOpenTicketsToCache(cacheAllOpenTickets);
	}
	
	public void addToAllAdminClosedTickets(String newTicketKey, PersistenceManager pm) {
		ArrayList<TicketModel> closedTickets = new ArrayList<TicketModel>();
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		String allClosedTicketsCacheKey = TicketCacheParams.ALL_CLOSED_TICKETS;
		IdentifiableValue cacheIdentAllClosedTickets = (IdentifiableValue) syncCache.getIdentifiable(allClosedTicketsCacheKey);
		ArrayList<String> cacheAllClosedTickets = null;
		
		String ticketCacheKey = TicketCacheParams.TICKET;
		IdentifiableValue cacheIdentTicket;
		
		if (cacheIdentAllClosedTickets == null) {
			cacheAllClosedTickets = new ArrayList<String>();
			
			Query query = pm.newQuery(PCHelpdeskTicket.class);
		    query.setFilter("status == statusParam");
		    query.setOrdering("date desc");
		    query.declareParameters("Boolean statusParam");

		    try {
		        List<PCHelpdeskTicket> results = (List<PCHelpdeskTicket>) query.execute(false);
		        
		        if (!results.isEmpty()) {
		        	closedTickets = new ArrayList<TicketModel>();
		        	TicketModel auxTicket;
			    	PCCategory category;
			    	ArrayList<TicketAnswerModel> answers;
			    	TicketAnswerModel auxAnswer;
			    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			    	String username;
			    	PCUser auxUser;
			    	PCHelpdeskTicketAnswer ans;
			    	PCUser user;
			    	PCAdmin admin;
			    	String userAdminKey;
		        	
		            for (PCHelpdeskTicket t : results) {
		            	category = (PCCategory)pm.getObjectById(PCCategory.class, t.getCategory());
			    		answers = new ArrayList<TicketAnswerModel>();
			    		
			    		for (Key ansKey : t.getAnswers()) {
			    			ans = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
			    			
			    			if (ans.getAdmin() == null) {
			    				user = pm.getObjectById(PCUser.class, ans.getUser());
			    				username = user.getUsername();
			    				userAdminKey = KeyFactory.keyToString(ans.getUser());
			    			}
			    			else {
			    				admin = pm.getObjectById(PCAdmin.class, ans.getAdmin());
			    				username = admin.getUsername();
			    				userAdminKey = KeyFactory.keyToString(ans.getAdmin());
			    			}
			    			
			    			auxAnswer = new TicketAnswerModel(formatter.format(ans.getDate()), userAdminKey, ans.getAnswer(), username);
			    			answers.add(auxAnswer);
			    		}
			    		
			    		auxUser = pm.getObjectById(PCUser.class, t.getUser());
			    		auxTicket = new TicketModel(KeyFactory.keyToString(t.getKey()), category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers, auxUser.getUsername());
			    		
			    		cacheAllClosedTickets.add(KeyFactory.keyToString(t.getKey()));
			    		
			    		cacheIdentTicket = (IdentifiableValue) syncCache.getIdentifiable(ticketCacheKey + KeyFactory.keyToString(t.getKey()));
			    		
			    		if (cacheIdentTicket == null) {
			    			WriteToCache.writeTicketToCache(auxTicket);
			    		}
			    		
			    		closedTickets.add(auxTicket);
		            }
		        }
		    } 
		    catch (Exception ex) {
		    	logger.info("[AdminClosedTicketList] An error occured: " + ex.getMessage());
		    	closedTickets = null;
		    }
		    finally {
		        query.closeAll();
		    }
			
			WriteToCache.writeAllClosedTicketsToCache(cacheAllClosedTickets);
		}
		else {
			cacheAllClosedTickets = (ArrayList<String>) cacheIdentAllClosedTickets.getValue();
			
			Key tickKey;
			PCHelpdeskTicket ticket;
			
			for (String ticketId : cacheAllClosedTickets) {
				cacheIdentTicket = (IdentifiableValue) syncCache.getIdentifiable(ticketCacheKey + ticketId);
				
				if (cacheIdentTicket == null) {
					tickKey = KeyFactory.stringToKey(ticketId);
					ticket = (PCHelpdeskTicket)pm.getObjectById(PCHelpdeskTicket.class, tickKey);
					
					closedTickets.add(WriteToCache.writeTicketToCache(pm, KeyFactory.keyToString(ticket.getUser()), ticket));
	    		}
				else {
					closedTickets.add((TicketModel)cacheIdentTicket.getValue());
				}
			}
		}
		
		cacheAllClosedTickets.add(newTicketKey);
		WriteToCache.writeAllClosedTicketsToCache(cacheAllClosedTickets);
	}
	
	public Boolean oldCloseTicket(String ticketKey, String loggedUserKey) {
		boolean result = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		boolean isAdmin = false;
		
		try {					
			logger.info("[CloseTicketService] Searching for user.");
			PCUser user = pm.getObjectById(PCUser.class, loggedUserKey);
			
			if (user != null) {
				logger.info("[CloseTicketService] Logged user found. It is not admin.");
				Key closingTicketKey = KeyFactory.stringToKey(ticketKey);
				user.getOpenTickets().remove(closingTicketKey);
				
				if (user.getClosedTickets() == null) {
					user.setClosedTickets(new ArrayList<Key>());
				}
				
				user.getClosedTickets().add(closingTicketKey);
			}
		}
		catch (Exception ex) {
			isAdmin = true;
		}
		
		try {
			logger.info("[CloseTicketService] Closing ticket.");
			PCHelpdeskTicket ticket = pm.getObjectById(PCHelpdeskTicket.class, ticketKey);
			ticket.setStatus(false);
			
			if (isAdmin) {
				PCUser auxUser = pm.getObjectById(PCUser.class, ticket.getUser());
				
				Key closingTicketKey = KeyFactory.stringToKey(ticketKey);
				auxUser.getOpenTickets().remove(closingTicketKey);
				
				if (auxUser.getClosedTickets() == null) {
					auxUser.setClosedTickets(new ArrayList<Key>());
				}
				
				auxUser.getClosedTickets().add(closingTicketKey);
			}
			
			result = true;
		}
		catch (Exception ex) {
			result = false;
		}
		finally {
			pm.close();
		}
		
		return result;
	}
}
