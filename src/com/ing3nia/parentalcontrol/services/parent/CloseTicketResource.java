package com.ing3nia.parentalcontrol.services.parent;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.JsonObject;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("close-ticket")
public class CloseTicketResource {

	private static Logger logger = Logger.getLogger(CloseTicketResource.class.getName());
	
	public CloseTicketResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	//user=ahJzfnBhcmVudGFsLWNvbnRyb2xyDQsSBlBDVXNlchjQZQw&ticket=ahJzfnBhcmVudGFsLWNvbnRyb2xyGAsSEFBDSGVscGRlc2tUaWNrZXQYnPQGDA
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(@QueryParam(value = "user") final String loggedUserKey, @QueryParam(value = "ticket") final String ticketKey) {		
		ResponseBuilder rbuilder;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		boolean isAdmin = false;
		
		try {					
			logger.info("[Close Ticket Service] Searching for user.");
			PCUser user = pm.getObjectById(PCUser.class, loggedUserKey);
			
			if (user != null) {
				logger.info("[Close Ticket Service] Logged user found. It is not admin.");
				
				Key closingTicketKey = KeyFactory.stringToKey(ticketKey);
				user.getOpenTickets().remove(closingTicketKey);
				
				if (user.getClosedTickets() == null) {
					user.setClosedTickets(new ArrayList<Key>());
				}
				
				user.getClosedTickets().add(closingTicketKey);
			}
		}
		catch (Exception ex) {
			isAdmin = true;
		}
		
		try {
			logger.info("[Close Ticket Service] Closing ticket.");
			
			PCHelpdeskTicket ticket = pm.getObjectById(PCHelpdeskTicket.class, ticketKey);
			ticket.setStatus(false);
			
			if (isAdmin) {
				PCUser auxUser = pm.getObjectById(PCUser.class, ticket.getUser());
				
				Key closingTicketKey = KeyFactory.stringToKey(ticketKey);
				auxUser.getOpenTickets().remove(closingTicketKey);
				
				if (auxUser.getClosedTickets() == null) {
					auxUser.setClosedTickets(new ArrayList<Key>());
				}
				
				auxUser.getClosedTickets().add(closingTicketKey);
			}
			
			logger.info("[Close Ticket Service] Ok Response. Ticket succesfully closed.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Close Ticket Service] An error ocurred while closing ticket. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		finally {
			pm.close();
		}
	}
}
