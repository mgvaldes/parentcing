package com.ing3nia.parentalcontrol.server;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.models.cache.TicketCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.AddTicketAnswerService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AddTicketAnswerServiceImpl extends RemoteServiceServlet implements AddTicketAnswerService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddTicketAnswerServiceImpl.class.getName());

	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	public AddTicketAnswerServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String addTicketAnswer(TicketAnswerModel answer, String ticketKey, boolean isAdmin) {
		if (ACTUAL.equals(OLD_WS)) {
			return oldAddTicketAnswer(answer, ticketKey, isAdmin);
		}
		else {
			return newAddTicketAnswer(answer, ticketKey, isAdmin);
		}
	}
	
	public String oldAddTicketAnswer(TicketAnswerModel answer, String ticketKey, boolean isAdmin) {
		String answerKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[AddTicketAnswerService] searching for ticket with key: " + ticketKey);
			Key tickKey = KeyFactory.stringToKey(ticketKey);			
			PCHelpdeskTicket ticket = (PCHelpdeskTicket)pm.getObjectById(PCHelpdeskTicket.class, tickKey);
			
			logger.info("[AddTicketAnswerService] Creating new ticket answer.");
			PCHelpdeskTicketAnswer helpdeskAnswer = new PCHelpdeskTicketAnswer();
			Key userOrAdminKey = KeyFactory.stringToKey(answer.getUserKey());
			
			if (isAdmin) {
				logger.info("[AddTicketAnswerService] User who answered is admin");
				helpdeskAnswer.setAdmin(userOrAdminKey);
				helpdeskAnswer.setUser(null);
			}
			else {
				logger.info("[AddTicketAnswerService] User who answered is parent");
				helpdeskAnswer.setUser(userOrAdminKey);
				helpdeskAnswer.setAdmin(null);
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			
			helpdeskAnswer.setDate(formatter.parse(answer.getDate()));
			helpdeskAnswer.setAnswer(answer.getAnswer());
			
			logger.info("[AddTicketAnswerService] Saving answer.");
			pm.makePersistent(helpdeskAnswer);
			
			answerKey = KeyFactory.keyToString(helpdeskAnswer.getKey());
			logger.info("[AddTicketAnswerService] Answer key: " + answerKey);
			
			ticket.getAnswers().add(helpdeskAnswer.getKey());
			logger.info("[AddTicketAnswerService] Asociating answer to ticket.");
		}
		catch (Exception ex) {
			logger.info("[AddTicketAnswerService] An error occured. " + ex.getMessage());
		}
		finally {
			pm.close();
		}
		
		return answerKey;
	}
	
	public String newAddTicketAnswer(TicketAnswerModel answer, String ticketKey, boolean isAdmin) {
		String answerKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[AddTicketAnswerService] searching for ticket with key: " + ticketKey);
			Key tickKey = KeyFactory.stringToKey(ticketKey);			
			PCHelpdeskTicket ticket = (PCHelpdeskTicket)pm.getObjectById(PCHelpdeskTicket.class, tickKey);
			
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			String openTicketCacheKey = TicketCacheParams.TICKET + ticketKey;
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
			
			logger.info("[AddTicketAnswerService] Creating new ticket answer.");
			PCHelpdeskTicketAnswer helpdeskAnswer = new PCHelpdeskTicketAnswer();
			Key userOrAdminKey = KeyFactory.stringToKey(answer.getUserKey());
			String username;
			
			if (isAdmin) {
				logger.info("[AddTicketAnswerService] User who answered is admin");
				helpdeskAnswer.setAdmin(userOrAdminKey);
				helpdeskAnswer.setUser(null);
				
				PCAdmin admin = pm.getObjectById(PCAdmin.class, userOrAdminKey);
				username = admin.getUsername();				
			}
			else {
				logger.info("[AddTicketAnswerService] User who answered is parent");
				helpdeskAnswer.setUser(userOrAdminKey);
				helpdeskAnswer.setAdmin(null);
				
				PCUser user = pm.getObjectById(PCUser.class, userOrAdminKey);
				username = user.getUsername();
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			
			helpdeskAnswer.setDate(formatter.parse(answer.getDate()));
			helpdeskAnswer.setAnswer(answer.getAnswer());
			
			logger.info("[AddTicketAnswerService] Saving answer.");
			pm.makePersistent(helpdeskAnswer);
			
			answerKey = KeyFactory.keyToString(helpdeskAnswer.getKey());
			logger.info("[AddTicketAnswerService] Answer key: " + answerKey);
			
			TicketAnswerModel ticketAnswer = new TicketAnswerModel();
			ticketAnswer.setAnswer(helpdeskAnswer.getAnswer());
			ticketAnswer.setDate(formatter.format(helpdeskAnswer.getDate()));
			ticketAnswer.setKey(answerKey);
			ticketAnswer.setUserKey(answer.getUserKey());
			ticketAnswer.setUsername(username);
			
			cacheOpenTicket.getAnswers().add(ticketAnswer);
			
			WriteToCache.writeTicketToCache(cacheOpenTicket);
			
			ticket.getAnswers().add(helpdeskAnswer.getKey());
			logger.info("[AddTicketAnswerService] Asociating answer to ticket.");
		}
		catch (Exception ex) {
			logger.info("[AddTicketAnswerService] An error occured. " + ex.getMessage());
		}
		finally {
			pm.close();
		}
		
		return answerKey;
	}
}
