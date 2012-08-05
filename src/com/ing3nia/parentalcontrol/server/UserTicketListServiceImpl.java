package com.ing3nia.parentalcontrol.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

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
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListService;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class UserTicketListServiceImpl extends RemoteServiceServlet implements UserTicketListService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserTicketListServiceImpl.class.getName());
	
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	public UserTicketListServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public HashMap<String, ArrayList<TicketModel>> getUserTicketList(String loggedUserKey) {
		if (ACTUAL.equals(NEW_WS)) {
			return getUserTicketListNEW(loggedUserKey);
		}

		return getUserTicketListOLD(loggedUserKey);
	}
	
	public HashMap<String, ArrayList<TicketModel>> getUserTicketListOLD(String loggedUserKey) {
		ArrayList<TicketModel> openedTickets = null;
		ArrayList<TicketModel> closedTickets = null;
		HashMap<String, ArrayList<TicketModel>> tickets = null;
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			Key userKey = KeyFactory.stringToKey(loggedUserKey);
			
			logger.info("[UserTicketListService] Searching for user with key: " + loggedUserKey);
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);

		    tickets = new HashMap<String, ArrayList<TicketModel>>();
	    	openedTickets = new ArrayList<TicketModel>();
	    	closedTickets = new ArrayList<TicketModel>();
	    	
	    	PCHelpdeskTicketAnswer ans;
	    	PCUser userR;
	    	TicketModel auxTicket;
	    	PCCategory category;
	    	ArrayList<TicketAnswerModel> answers;
	    	TicketAnswerModel auxAnswer;
	    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	    	String username;	
	    	PCHelpdeskTicket t;
	    	String userAdminKey;
	    	
	    	for (Key tKey : user.getOpenTickets()) {
	    		t = pm.getObjectById(PCHelpdeskTicket.class, tKey);
	    		
	    		category = (PCCategory)pm.getObjectById(PCCategory.class, t.getCategory());
	    		answers = new ArrayList<TicketAnswerModel>();
	    		
	    		for (Key ansKey : t.getAnswers()) {
	    			ans = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
	    			
	    			if (ans.getAdmin() == null) {
	    				userR = pm.getObjectById(PCUser.class, ans.getUser());
	    				username = userR.getUsername();
	    				userAdminKey = KeyFactory.keyToString(ans.getUser());
	    			}
	    			else {
	    				//adminR = pm.getObjectById(PCAdmin.class, ans.getAdmin());
	    				username = "Admin";//adminR.getUsername();
	    				userAdminKey = KeyFactory.keyToString(ans.getAdmin());
	    			}
	    			
	    			auxAnswer = new TicketAnswerModel(formatter.format(ans.getDate()), userAdminKey, ans.getAnswer(), username);
	    			answers.add(auxAnswer);
	    		}
	    		
	    		auxTicket = new TicketModel(KeyFactory.keyToString(t.getKey()), category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers);
	    		
	    		openedTickets.add(auxTicket);
	    	}
		    
		    logger.info("[UserTicketListService] Executing query to search tickets.");
		    
	    	for (Key tKey : user.getClosedTickets()) {
	    		t = pm.getObjectById(PCHelpdeskTicket.class, tKey);
	    		
	    		category = (PCCategory)pm.getObjectById(PCCategory.class, t.getCategory());
	    		answers = new ArrayList<TicketAnswerModel>();
	    		
	    		for (Key ansKey : t.getAnswers()) {
	    			ans = pm.getObjectById(PCHelpdeskTicketAnswer.class, ansKey);
	    			
	    			if (ans.getAdmin() == null) {
	    				userR = pm.getObjectById(PCUser.class, ans.getUser());
	    				username = userR.getUsername();
	    				userAdminKey = KeyFactory.keyToString(ans.getUser());
	    			}
	    			else {
	    				//adminR = pm.getObjectById(PCAdmin.class, ans.getAdmin());
	    				username = "Admin";//adminR.getUsername();
	    				userAdminKey = KeyFactory.keyToString(ans.getAdmin());
	    			}
	    			
	    			auxAnswer = new TicketAnswerModel(formatter.format(ans.getDate()), userAdminKey, ans.getAnswer(), username);
	    			answers.add(auxAnswer);
	    		}
	    		
	    		auxTicket = new TicketModel(KeyFactory.keyToString(t.getKey()), category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers);
	    		
	    		closedTickets.add(auxTicket);
	    	}
		    
		    tickets.put("opened", openedTickets);
		    tickets.put("closed", closedTickets);
		}
		catch (Exception ex) {
			logger.info("[UserTicketListService] An error occured. " + ex.getMessage());
			tickets = null;
		}
		finally {
			pm.close();
		}
		
		return tickets;
	}
	
	public HashMap<String, ArrayList<TicketModel>> getUserTicketListNEW(String loggedUserKey) {
		ArrayList<TicketModel> openedTickets = null;
		ArrayList<TicketModel> closedTickets = null;
		HashMap<String, ArrayList<TicketModel>> tickets = null;
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			Key userKey = KeyFactory.stringToKey(loggedUserKey);
			
			logger.info("[UserTicketListService] Searching for user with key: " + loggedUserKey);
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);

		    tickets = new HashMap<String, ArrayList<TicketModel>>();
	    	openedTickets = new ArrayList<TicketModel>();
	    	closedTickets = new ArrayList<TicketModel>();
	    	
	    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			String openTicketsCacheKey = loggedUserKey + UserCacheParams.OPEN_TICKETS_LIST;
			IdentifiableValue cacheIdentOpenTickets = (IdentifiableValue) syncCache.getIdentifiable(openTicketsCacheKey);
			ArrayList<Key> openTicketsKeys = user.getOpenTickets();
			
			if (cacheIdentOpenTickets == null) {
				logger.info("[UserTicketListService] User Open Tickets ARE NOT in cache");
				
				ArrayList<PCHelpdeskTicket> openTickets = new ArrayList<PCHelpdeskTicket>();
				PCHelpdeskTicket auxOpenTicket;
				
				for (Key key : openTicketsKeys) {
					auxOpenTicket = pm.getObjectById(PCHelpdeskTicket.class, key);
					openTickets.add(auxOpenTicket);
				}
				
				openedTickets = WriteToCache.writeOpenTicketsToCache(pm, loggedUserKey, openTickets, null);
			}
			else {
				logger.info("[UserTicketListService] User Open Tickets ARE in cache");
				
				ArrayList<String> openTicketKeys = (ArrayList<String>) cacheIdentOpenTickets.getValue();
				IdentifiableValue cacheIdentTicket;
				PCHelpdeskTicket auxTicket;
				
				for (String ticketKey : openTicketKeys) {
					cacheIdentTicket = (IdentifiableValue) syncCache.getIdentifiable(TicketCacheParams.TICKET + ticketKey);
					
					if (cacheIdentTicket == null) {
						auxTicket = pm.getObjectById(PCHelpdeskTicket.class, KeyFactory.stringToKey(ticketKey));
						
						openedTickets.add(WriteToCache.writeTicketToCache(pm, loggedUserKey, auxTicket));
					}
					else {
						openedTickets.add((TicketModel)cacheIdentTicket.getValue());
					}
				}
				
				logger.info("[UserTicketListService] User Open Tickets size: " + openedTickets.size());
			}
		    
			String closeTicketsCacheKey = loggedUserKey + UserCacheParams.CLOSED_TICKETS_LIST;
			IdentifiableValue cacheIdentCloseTickets = (IdentifiableValue) syncCache.getIdentifiable(closeTicketsCacheKey);
			ArrayList<Key> closeTicketsKeys = user.getClosedTickets();
			
			if (cacheIdentCloseTickets == null) {
				logger.info("[UserTicketListService] User Closed Tickets ARE NOT in cache");
				
				ArrayList<PCHelpdeskTicket> closeTickets = new ArrayList<PCHelpdeskTicket>();
				PCHelpdeskTicket auxCloseTicket;
				
				for (Key key : closeTicketsKeys) {
					auxCloseTicket = pm.getObjectById(PCHelpdeskTicket.class, key);
					closeTickets.add(auxCloseTicket);
				}
				
				closedTickets = WriteToCache.writeClosedTicketsToCache(pm, loggedUserKey, closeTickets, null);
			}
			else {
				logger.info("[UserTicketListService] User Closed Tickets ARE in cache");
				
				ArrayList<String> closeTicketKeys = (ArrayList<String>) cacheIdentCloseTickets.getValue();
				IdentifiableValue cacheIdentTicket;
				PCHelpdeskTicket auxTicket;
				
				for (String ticketKey : closeTicketKeys) {
					cacheIdentTicket = (IdentifiableValue) syncCache.getIdentifiable(TicketCacheParams.TICKET + ticketKey);
					
					if (cacheIdentTicket == null) {
						auxTicket = pm.getObjectById(PCHelpdeskTicket.class, KeyFactory.stringToKey(ticketKey));
						
						closedTickets.add(WriteToCache.writeTicketToCache(pm, loggedUserKey, auxTicket));
					}
					else {
						closedTickets.add((TicketModel)cacheIdentTicket.getValue());
					}
				}
				
				logger.info("[UserTicketListService] User Closed Tickets size: " + closedTickets.size());
			}
		    
		    tickets.put("opened", openedTickets);
		    tickets.put("closed", closedTickets);
		}
		catch (Exception ex) {
			logger.info("[UserTicketListService] An error occured. " + ex.getMessage());
			tickets = null;
		}
		finally {
			pm.close();
		}
		
		return tickets;
	}
}
