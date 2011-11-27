package com.ing3nia.parentalcontrol.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.rpc.AdminOpenTicketListService;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AdminOpenTicketListServiceImpl extends RemoteServiceServlet implements AdminOpenTicketListService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AdminOpenTicketListServiceImpl.class.getName());
	
	public AdminOpenTicketListServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public ArrayList<TicketModel> adminOpenTicketList() {
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
		    		
		    		auxTicket = new TicketModel(KeyFactory.keyToString(t.getKey()), category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers, t.getUser().getUsername());
		    		openTickets.add(auxTicket);
	            }
	        }
	    } 
	    catch (Exception ex) {
	    	openTickets = null;
	    }
	    finally {
	        query.closeAll();
	    }
		
		return openTickets;
	}
}
