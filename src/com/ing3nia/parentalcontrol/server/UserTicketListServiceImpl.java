package com.ing3nia.parentalcontrol.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class UserTicketListServiceImpl extends RemoteServiceServlet implements UserTicketListService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserTicketListServiceImpl.class.getName());
	
	public UserTicketListServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public HashMap<String, ArrayList<TicketModel>> getUserTicketList(String loggedUserKey) {
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
	    	PCAdmin adminR;
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
}
