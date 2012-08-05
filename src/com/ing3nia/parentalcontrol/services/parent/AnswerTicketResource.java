package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.models.cache.TicketCacheParams;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.AnswerTicketModel;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

@Path("answer-ticket")
public class AnswerTicketResource {

	private static Logger logger = Logger.getLogger(AnswerTicketResource.class.getName());
	
	public AnswerTicketResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	/**
	 * {
		    "ticket": "ahJzfnBhcmVudGFsLWNvbnRyb2xyGAsSEFBDSGVscGRlc2tUaWNrZXQY5d0IDA",
		    "answer": {
		        "userKey": "ahJzfnBhcmVudGFsLWNvbnRyb2xyDQsSBlBDVXNlchjQZQw",
		        "answer": "Prueba de respuesta."
		    }
	 * }
	 */
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<AnswerTicketModel>(){}.getType();
		
		AnswerTicketModel answerTicketModel;
		ResponseBuilder rbuilder;
		
		logger.info("[Answer Ticket Service] Parsing input parameters.");		
		
		try {
			answerTicketModel = jsonParser.fromJson(body, bodyType);
		}
		catch (Exception e) {
			logger.warning("[Answer Ticket Service] AnswerTicketModel couldn't be created from post input " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		try {
			String ticketAnswerKey = saveTicketAnswer(answerTicketModel);
			
			logger.info("[Answer Ticket Service] Ok Response. Ticket saved succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			okResponse.addProperty("key", ticketAnswerKey);
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Answer Ticket Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Answer Ticket Service] An error ocurred. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
	}
	
	public String saveTicketAnswer(AnswerTicketModel answerTicketModel) throws SessionQueryException, IllegalArgumentException {
		String answerKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Answer Ticket Service] searching for ticket with key: " + answerTicketModel.getTicket());
			Key tickKey = KeyFactory.stringToKey(answerTicketModel.getTicket());			
			PCHelpdeskTicket ticket = (PCHelpdeskTicket)pm.getObjectById(PCHelpdeskTicket.class, tickKey);
			
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			String openTicketCacheKey = TicketCacheParams.TICKET + answerTicketModel.getTicket();
			IdentifiableValue cacheIdentOpenTicket = (IdentifiableValue) syncCache.getIdentifiable(openTicketCacheKey);
			TicketModel cacheOpenTicket = null;
			
			if (cacheIdentOpenTicket == null) {
				WriteToCache.writeTicketToCache(pm, KeyFactory.keyToString(ticket.getUser()), ticket);
				
				cacheIdentOpenTicket = syncCache.getIdentifiable(openTicketCacheKey);
				cacheOpenTicket = (TicketModel) cacheIdentOpenTicket.getValue();
			}
			else {
				cacheOpenTicket = (TicketModel) cacheIdentOpenTicket.getValue();
			}
			
			logger.info("[Answer Ticket Service] Creating new ticket answer.");
			PCHelpdeskTicketAnswer helpdeskAnswer = new PCHelpdeskTicketAnswer();
			Key userOrAdminKey = KeyFactory.stringToKey(answerTicketModel.getAnswer().getUserKey());
			
			logger.info("[Answer Ticket Service] User who answered is parent");
			helpdeskAnswer.setUser(userOrAdminKey);
			helpdeskAnswer.setAdmin(null);
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			
//			helpdeskAnswer.setDate(formatter.parse(answerTicketModel.getAnswer().getDate()));
			helpdeskAnswer.setDate(Calendar.getInstance().getTime());
			helpdeskAnswer.setAnswer(answerTicketModel.getAnswer().getAnswer());
			
			logger.info("[Answer Ticket Service] Saving answer.");
			pm.makePersistent(helpdeskAnswer);
			
			answerKey = KeyFactory.keyToString(helpdeskAnswer.getKey());
			logger.info("[Answer Ticket Service] Answer key: " + answerKey);
			
			TicketAnswerModel ticketAnswer = new TicketAnswerModel();
			ticketAnswer.setAnswer(helpdeskAnswer.getAnswer());
			ticketAnswer.setDate(formatter.format(helpdeskAnswer.getDate()));
			ticketAnswer.setKey(answerKey);
			ticketAnswer.setUserKey(answerTicketModel.getAnswer().getUserKey());
			
			cacheOpenTicket.getAnswers().add(ticketAnswer);
			
			WriteToCache.writeTicketToCache(cacheOpenTicket);
			
			ticket.getAnswers().add(helpdeskAnswer.getKey());
			logger.info("[Answer Ticket Service] Asociating answer to ticket.");
		}
		catch (Exception ex) {
			logger.info("[Answer Ticket Service] An error occured. " + ex.getMessage());
		}
		finally {
			pm.close();
		}
		
		return answerKey;
		
//		String ticketKey = null;
//		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
//		
//		try {
//			Key userKey = KeyFactory.stringToKey(addTicketModel.getKey());
//			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);
//			
//			PCHelpdeskTicket newTicket = new PCHelpdeskTicket();
//			newTicket.setAnswers(new ArrayList<Key>());
//			
//			//Buscar categoria
//			Query query = pm.newQuery(PCCategory.class);
//		    query.setFilter("description == descriptionParam");
//		    query.declareParameters("String descriptionParam");
//		    query.setRange(0, 1);
//		    
//		    List<PCCategory> results = (List<PCCategory>)query.execute(addTicketModel.getTicket().getCategory());
//		    
//		    if (!results.isEmpty()) {
//		    	newTicket.setCategory(results.get(0).getKey());
//		    }
//			
//			newTicket.setDate(Calendar.getInstance().getTime());
//			newTicket.setQuestion(addTicketModel.getTicket().getComment());
//			newTicket.setSubject(addTicketModel.getTicket().getSubject());
//			newTicket.setStatus(true);
//			newTicket.setUser(userKey);
//			
//			pm.makePersistent(newTicket);
//			
//			if (user.getOpenTickets() == null) {
//				user.setOpenTickets(new ArrayList<Key>());
//			}
//			
//			user.getOpenTickets().add(newTicket.getKey());
//			
//			ticketKey = KeyFactory.keyToString(newTicket.getKey());
//		}
//		catch (IllegalArgumentException ex) {
//			logger.info("[Answer Ticket Service] An error occured saving the new ticket.");
//			
//			throw ex;
//		}
//		catch (Exception ex) {
//			logger.info("[Answer Ticket Service] An error occured saving the new ticket.");
//	    	
//			throw new SessionQueryException();
//		}
//		finally {
//			pm.close();
//		}
//		
//		return ticketKey;
	}
}
