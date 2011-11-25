package com.ing3nia.parentalcontrol.server;
/*
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
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class UserTicketListServiceImpl extends RemoteServiceServlet implements UserTicketListService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserTicketListServiceImpl.class.getName());
	
	public UserTicketListServiceImpl() {
		logger.addHandler(new ConsoleHandler());
	}

	@Override
	public HashMap<String, ArrayList<TicketModel>> getUserTicketList(String loggedUserKey) {
		ArrayList<TicketModel> openedTickets = null;
		ArrayList<TicketModel> closedTickets = null;
		HashMap<String, ArrayList<TicketModel>> tickets = null;
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			Key userKey = KeyFactory.stringToKey(loggedUserKey);
			
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey); 
			
			Query query = pm.newQuery(PCHelpdeskTicket.class);
		    query.setFilter("user == userparam");
		    query.declareParameters("PCUser userparam");
		    query.setRange(0, 1);
		    
		    List<PCHelpdeskTicket> results = (List<PCHelpdeskTicket>) query.execute(user);
		    
		    if (!results.isEmpty()) {
		    	tickets = new HashMap<String, ArrayList<TicketModel>>();
		    	openedTickets = new ArrayList<TicketModel>();
		    	closedTickets = new ArrayList<TicketModel>();
		    	
		    	TicketModel auxTicket;
		    	PCCategory category;
		    	ArrayList<TicketAnswerModel> answers;
		    	TicketAnswerModel auxAnswer;
		    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		    	String username;
		    	
		    	for (PCHelpdeskTicket t : results) {
		    		category = (PCCategory)pm.getObjectById(PCCategory.class, t.getCategory());
		    		answers = new ArrayList<TicketAnswerModel>();
		    		
		    		for (PCHelpdeskTicketAnswer ans : t.getAnswers()) {
		    			if (ans.getAdmin() == null) {
		    				username = ans.getUser().getUsername();
		    			}
		    			else {
		    				username = ans.getAdmin().getUsername();
		    			}
		    			
		    			auxAnswer = new TicketAnswerModel(formatter.format(ans.getDate()), username, ans.getAnswer());
		    			answers.add(auxAnswer);
		    		}
		    		
		    		auxTicket = new TicketModel(category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers);
		    		
		    		if (t.getStatus()) {
		    			openedTickets.add(auxTicket);
		    		}
		    		else {
		    			closedTickets.add(auxTicket);
		    		}
		    	}
		    }
		    
		    tickets.put("opened", openedTickets);
		    tickets.put("closed", closedTickets);
		}
		catch (Exception ex) {
			
		}
		finally {
			pm.close();
		}
		
		return tickets;
	}
}*/
