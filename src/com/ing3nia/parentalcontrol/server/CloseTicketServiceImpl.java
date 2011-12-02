package com.ing3nia.parentalcontrol.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.rpc.CloseTicketService;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class CloseTicketServiceImpl extends RemoteServiceServlet implements CloseTicketService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CloseTicketServiceImpl.class.getName());
	
	public CloseTicketServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean closeTicket(String ticketKey, String loggedUserKey) {
		boolean result = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {						
			PCUser user = pm.getObjectById(PCUser.class, loggedUserKey);
			
			Key closingTicketKey = KeyFactory.stringToKey(ticketKey);
			user.getOpenTickets().remove(closingTicketKey);
			
			if (user.getClosedTickets() == null) {
				user.setClosedTickets(new ArrayList<Key>());
			}
			
			user.getClosedTickets().add(closingTicketKey);
			
			PCHelpdeskTicket ticket = pm.getObjectById(PCHelpdeskTicket.class, ticketKey);
			ticket.setStatus(false);
			
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
