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
import com.ing3nia.parentalcontrol.client.rpc.AdminOpenTicketListService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AdminOpenTicketListServiceImpl extends RemoteServiceServlet implements AdminOpenTicketListService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AdminOpenTicketListServiceImpl.class.getName());
	
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	public AdminOpenTicketListServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public ArrayList<TicketModel> adminOpenTicketList() {
		if (ACTUAL.equals(OLD_WS)) {
			return oldAdminOpenTicketList();
		}
		else {
			return newAdminOpenTicketList();
		}
	}
	
	public ArrayList<TicketModel> newAdminOpenTicketList() {
		ArrayList<TicketModel> openTickets = new ArrayList<TicketModel>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
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
		
		return openTickets;
	}
	
	public ArrayList<TicketModel> oldAdminOpenTicketList() {
		ArrayList<TicketModel> openTickets = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
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
		    		openTickets.add(auxTicket);
	            }
	        }
	    } 
	    catch (Exception ex) {
	    	logger.info("[AdminOpenTicketList] An error occured: " + ex.getMessage());
	    	openTickets = null;
	    }
	    finally {
	        query.closeAll();
	    }
		
		return openTickets;
	}
}
