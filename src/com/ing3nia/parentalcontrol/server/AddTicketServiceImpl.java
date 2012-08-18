package com.ing3nia.parentalcontrol.server;

import java.text.SimpleDateFormat;
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
import com.ing3nia.parentalcontrol.client.models.cache.TicketCacheParams;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.AddTicketService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
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
			
//			addToAllAdminOpenTickets(newTicketModel.getKey(), pm);
			
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
	
	public void addToAllAdminOpenTickets(String newTicketKey, PersistenceManager pm) {
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
		
		cacheAllOpenTickets.add(newTicketKey);
		WriteToCache.writeAllOpenTicketsToCache(cacheAllOpenTickets);
	}
}
