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
import com.ing3nia.parentalcontrol.client.rpc.AdminClosedTicketListService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AdminClosedTicketListServiceImpl extends RemoteServiceServlet implements AdminClosedTicketListService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AdminClosedTicketListServiceImpl.class.getName());
	
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	public AdminClosedTicketListServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public ArrayList<TicketModel> adminClosedTicketList() {
		if (ACTUAL.equals(OLD_WS)) {
			return oldAdminClosedTicketList();
		}
		else {
			return newAdminClosedTicketList();
		}
	}
	
	public ArrayList<TicketModel> oldAdminClosedTicketList() {
		ArrayList<TicketModel> closedTickets = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
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
		
		return closedTickets;
	}
	
	public ArrayList<TicketModel> newAdminClosedTicketList() {
		ArrayList<TicketModel> closedTickets = new ArrayList<TicketModel>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
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
		
		return closedTickets;
	}
}

