package com.ing3nia.parentalcontrol.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.rpc.AddTicketService;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AddTicketServiceImpl extends RemoteServiceServlet implements AddTicketService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddTicketServiceImpl.class.getName());
	
	public AddTicketServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String addTicket(TicketModel ticket, String loggedUserKey) {
		String ticketKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			Key userKey = KeyFactory.stringToKey(loggedUserKey);
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);
			
			PCHelpdeskTicket newTicket = new PCHelpdeskTicket();
			newTicket.setAnswers(new ArrayList<PCHelpdeskTicketAnswer>());
			
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
			newTicket.setUser(user);
			
			pm.makePersistent(newTicket);
			
			ticketKey = KeyFactory.keyToString(newTicket.getKey());
		}
		catch (Exception ex) {
			
		}
		finally {
			pm.close();
		}
		
		return ticketKey;
	}
}
