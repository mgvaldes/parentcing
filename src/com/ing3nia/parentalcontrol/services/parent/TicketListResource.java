package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("tickets")
public class TicketListResource {
	private static Logger logger = Logger.getLogger(TicketListResource.class.getName());
	
	public TicketListResource() {
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(@QueryParam(value = "user") final String loggedUserKey) {
		logger.info("[Tickets List Service] Version 1-2-22");
		
		ArrayList<TicketModel> openedTickets = new ArrayList<TicketModel>();
		ArrayList<TicketModel> closedTickets = new ArrayList<TicketModel>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCUser user;
		ResponseBuilder rbuilder;
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

		logger.info("[Tickets List Service] Buscando user asociado a id: " + loggedUserKey);
		
		try {
			user = getUser(loggedUserKey, pm);
			
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
	    				username = "Admin";
	    				userAdminKey = KeyFactory.keyToString(ans.getAdmin());
	    			}
	    			
	    			auxAnswer = new TicketAnswerModel(formatter.format(ans.getDate()), userAdminKey, ans.getAnswer(), username);
	    			answers.add(auxAnswer);
	    		}
	    		
	    		auxTicket = new TicketModel(KeyFactory.keyToString(t.getKey()), category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers);
	    		
	    		openedTickets.add(auxTicket);
	    	}
		    
		    logger.info("[Tickets List Service] Executing query to search tickets.");
		    
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
	    				username = "Admin";
	    				userAdminKey = KeyFactory.keyToString(ans.getAdmin());
	    			}
	    			
	    			auxAnswer = new TicketAnswerModel(formatter.format(ans.getDate()), userAdminKey, ans.getAnswer(), username);
	    			answers.add(auxAnswer);
	    		}
	    		
	    		auxTicket = new TicketModel(KeyFactory.keyToString(t.getKey()), category.getDescription(), t.getSubject(), t.getDate(), t.getQuestion(), answers);
	    		
	    		closedTickets.add(auxTicket);
	    	}
		}
		catch (SessionQueryException ex) {
			logger.warning("[Tickets List Service] An error ocurred while searching for user. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Tickets List Service] An error ocurred while searching for user. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Tickets List Service] Unexpected error occurred. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.UNEXPECTED_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		finally {
			pm.close();
		}
		
		logger.info("[Tickets List Service] Ok Response. Ticket lists succesfully sent.");
		
		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		Gson jsonBuilder = new Gson();
		JsonParser jsonParser = new JsonParser();
		Type type = new TypeToken<ArrayList<TicketModel>>(){}.getType();
		JsonArray openTicketsObject = (JsonArray)jsonParser.parse(jsonBuilder.toJson(openedTickets, type));
		JsonArray closedTicketsObject = (JsonArray)jsonParser.parse(jsonBuilder.toJson(closedTickets, type));
		JsonObject ticketsObject = new JsonObject();
		ticketsObject.add("open", openTicketsObject);
		ticketsObject.add("closed", closedTicketsObject);
		okResponse.add("tickets", ticketsObject);
		
		rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
		return rbuilder.build();
	}
	
	public PCUser getUser(String id, PersistenceManager pm) throws SessionQueryException, IllegalArgumentException {
		try {
			logger.info("[Tickets List Service] Converting id : " + id + " of user to Key.");
			Key userKey = KeyFactory.stringToKey(id);
						
			logger.info("[Tickets List Service] Searching for user in DB.");
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);					
			
			return user;
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	logger.info("[Update Synchronization Service] An error ocurred while finding the PCSmartphone by key " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }
	}
}
