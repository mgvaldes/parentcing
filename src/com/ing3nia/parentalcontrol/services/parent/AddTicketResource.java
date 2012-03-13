package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.AddTicketModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

@Path("add-ticket")
public class AddTicketResource {

	private static Logger logger = Logger.getLogger(AddTicketResource.class.getName());
	
	public AddTicketResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<AddTicketModel>(){}.getType();
		
		AddTicketModel addTicketModel;
		ResponseBuilder rbuilder;
		
		logger.info("[Add Ticket Service] Parsing input parameters.");		
		
		try {
			addTicketModel = jsonParser.fromJson(body, bodyType);
		}
		catch (Exception e) {
			logger.warning("[Add Ticket Service] AddTicketModel couldn't be created from post input " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		try {
			String ticketKey = saveTicket(addTicketModel);
			
			logger.info("[Add Ticket Service] Ok Response. Ticket saved succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			okResponse.addProperty("key", ticketKey);
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Add Ticket Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Add Ticket Service] An error ocurred while finding the PCUser by key or adding PCHelpdeskTicket. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
	}
	
	public String saveTicket(AddTicketModel addTicketModel) throws SessionQueryException, IllegalArgumentException {
		String ticketKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			Key userKey = KeyFactory.stringToKey(addTicketModel.getKey());
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);
			
			PCHelpdeskTicket newTicket = new PCHelpdeskTicket();
			newTicket.setAnswers(new ArrayList<Key>());
			
			//Buscar categoria
			Query query = pm.newQuery(PCCategory.class);
		    query.setFilter("description == descriptionParam");
		    query.declareParameters("String descriptionParam");
		    query.setRange(0, 1);
		    
		    List<PCCategory> results = (List<PCCategory>)query.execute(addTicketModel.getTicket().getCategory());
		    
		    if (!results.isEmpty()) {
		    	newTicket.setCategory(results.get(0).getKey());
		    }
			
			newTicket.setDate(Calendar.getInstance().getTime());
			newTicket.setQuestion(addTicketModel.getTicket().getComment());
			newTicket.setSubject(addTicketModel.getTicket().getSubject());
			newTicket.setStatus(true);
			newTicket.setUser(userKey);
			
			pm.makePersistent(newTicket);
			
			if (user.getOpenTickets() == null) {
				user.setOpenTickets(new ArrayList<Key>());
			}
			
			user.getOpenTickets().add(newTicket.getKey());
			
			ticketKey = KeyFactory.keyToString(newTicket.getKey());
		}
		catch (IllegalArgumentException ex) {
			logger.info("[Add Ticket Service] An error occured saving the new ticket.");
			
			throw ex;
		}
		catch (Exception ex) {
			logger.info("[Add Ticket Service] An error occured saving the new ticket.");
	    	
			throw new SessionQueryException();
		}
		finally {
			pm.close();
		}
		
		return ticketKey;
	}
}
