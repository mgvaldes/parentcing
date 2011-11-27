package com.ing3nia.parentalcontrol.server;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.rpc.CloseTicketService;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class CloseTicketServiceImpl extends RemoteServiceServlet implements CloseTicketService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CloseTicketServiceImpl.class.getName());
	
	public CloseTicketServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean closeTicket(String ticketKey) {
		boolean result = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {						
			PCHelpdeskTicket ticket = (PCHelpdeskTicket)pm.getObjectById(PCHelpdeskTicket.class, ticketKey);
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
