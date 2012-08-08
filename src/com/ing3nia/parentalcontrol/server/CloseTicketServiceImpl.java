package com.ing3nia.parentalcontrol.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.CloseTicketService;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
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
					
					WriteToCache.writeOpenTicketsToCache(pm, loggedUserKey, openTickets);
					
					cacheIdentOpenTickets = syncCache.getIdentifiable(openTicketsCacheKey);
					cacheOpenTickets = (ArrayList<String>) cacheIdentOpenTickets.getValue();
				}
				else {
					cacheOpenTickets = (ArrayList<String>) cacheIdentOpenTickets.getValue();
				}
				
				cacheOpenTickets.remove(ticketKey);
				
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
					
					WriteToCache.writeClosedTicketsToCache(pm, loggedUserKey, closedTickets);
					
					cacheIdentClosedTickets = syncCache.getIdentifiable(closedTicketsCacheKey);
					cacheClosedTickets = (ArrayList<String>) cacheIdentClosedTickets.getValue();
				}
				else {
					cacheClosedTickets = (ArrayList<String>) cacheIdentClosedTickets.getValue();
				}
				
				cacheClosedTickets.add(ticketKey);
			}
			
			logger.info("[Close Ticket Service] Ok Response. Ticket succesfully closed.");
			
			result = true;
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
