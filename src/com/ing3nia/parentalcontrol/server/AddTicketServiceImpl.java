package com.ing3nia.parentalcontrol.server;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.AddTicketService;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AddTicketServiceImpl extends RemoteServiceServlet implements AddTicketService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddTicketServiceImpl.class.getName());
	
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	public AddTicketServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String addTicket(TicketModel ticket, String loggedUserKey) {
		if (ACTUAL.equals(OLD_WS)) {
			return oldAddTicket(ticket, loggedUserKey);
		}
		else {
			return newAddTicket(ticket, loggedUserKey);
		}
	}
	
	public String oldAddTicket(TicketModel ticket, String loggedUserKey) {
		String ticketKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			Key userKey = KeyFactory.stringToKey(loggedUserKey);
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);
			
			PCHelpdeskTicket newTicket = new PCHelpdeskTicket();
			newTicket.setAnswers(new ArrayList<Key>());
			
			//Buscar categoria
			Query query = pm.newQuery(PCCategory.class);
		    query.setFilter("description == descriptionParam");
		    query.declareParameters("String descriptionParam");
		    query.setRange(0, 1);
		    
		    List<PCCategory> results = (List<PCCategory>)query.execute(ticket.getCategory());
		    if (!results.isEmpty()) {
		    	newTicket.setCategory(results.get(0).getKey());
		    }
			
			newTicket.setDate(Calendar.getInstance().getTime());
			newTicket.setQuestion(ticket.getComment());
			newTicket.setSubject(ticket.getSubject());
			newTicket.setStatus(true);
			newTicket.setUser(userKey);
			
			pm.makePersistent(newTicket);
			
			if (user.getOpenTickets() == null) {
				user.setOpenTickets(new ArrayList<Key>());
			}
			
			user.getOpenTickets().add(newTicket.getKey());
			
			ticketKey = KeyFactory.keyToString(newTicket.getKey());
		}
		catch (Exception ex) {
			logger.info("[AddTicketService] An error occured saving the new ticket.");
		}
		finally {
			pm.close();
		}
		
		return ticketKey;
	}
	
	public String newAddTicket(TicketModel ticket, String loggedUserKey) {
		String ticketKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			Key userKey = KeyFactory.stringToKey(loggedUserKey);
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);
			
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
			
			PCHelpdeskTicket newTicket = new PCHelpdeskTicket();
			newTicket.setAnswers(new ArrayList<Key>());
			
			//Buscar categoria
			Query query = pm.newQuery(PCCategory.class);
		    query.setFilter("description == descriptionParam");
		    query.declareParameters("String descriptionParam");
		    query.setRange(0, 1);
		    
		    List<PCCategory> results = (List<PCCategory>)query.execute(ticket.getCategory());
		    if (!results.isEmpty()) {
		    	newTicket.setCategory(results.get(0).getKey());
		    }
			
			newTicket.setDate(Calendar.getInstance().getTime());
			newTicket.setQuestion(ticket.getComment());
			newTicket.setSubject(ticket.getSubject());
			newTicket.setStatus(true);
			newTicket.setUser(userKey);
			
			pm.makePersistent(newTicket);
			
			if (user.getOpenTickets() == null) {
				user.setOpenTickets(new ArrayList<Key>());
			}
			
			user.getOpenTickets().add(newTicket.getKey());
			
			TicketModel newTicketModel = new TicketModel(KeyFactory.keyToString(newTicket.getKey()), ticket.getCategory(), ticket.getSubject(), newTicket.getDate(), ticket.getComment(), new ArrayList<TicketAnswerModel>());
			cacheOpenTickets.add(newTicketModel.getKey());
			
			WriteToCache.writeOpenTicketsToCache(cacheOpenTickets, loggedUserKey);
			
			WriteToCache.writeTicketToCache(newTicketModel);
			
			ticketKey = KeyFactory.keyToString(newTicket.getKey());
		}
		catch (Exception ex) {
			logger.info("[AddTicketService] An error occured saving the new ticket.");
		}
		finally {
			pm.close();
		}
		
		return ticketKey;
	}
}
