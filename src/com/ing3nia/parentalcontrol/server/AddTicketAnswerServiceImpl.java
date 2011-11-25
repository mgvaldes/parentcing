package com.ing3nia.parentalcontrol.server;

import java.text.SimpleDateFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.rpc.AddTicketAnswerService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicket;
import com.ing3nia.parentalcontrol.models.PCHelpdeskTicketAnswer;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AddTicketAnswerServiceImpl extends RemoteServiceServlet implements AddTicketAnswerService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddTicketAnswerServiceImpl.class.getName());
	
	public AddTicketAnswerServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String addTicketAnswer(TicketAnswerModel answer, String ticketKey, boolean isAdmin) {
		String answerKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			Key tickKey = KeyFactory.stringToKey(ticketKey);
			PCHelpdeskTicket ticket = (PCHelpdeskTicket)pm.getObjectById(PCHelpdeskTicket.class, tickKey);
			PCHelpdeskTicketAnswer helpdeskAnswer = new PCHelpdeskTicketAnswer();
			Key userOrAdminKey = KeyFactory.stringToKey(answer.getUser());
			
			if (isAdmin) {
				PCAdmin admin = (PCAdmin)pm.getObjectById(PCAdmin.class, userOrAdminKey);
				helpdeskAnswer.setAdmin(admin);
			}
			else {
				PCUser user = (PCUser)pm.getObjectById(PCUser.class, userOrAdminKey);
				helpdeskAnswer.setUser(user);
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			
			helpdeskAnswer.setDate(formatter.parse(answer.getDate()));
			helpdeskAnswer.setAnswer(answer.getAnswer());
			
			pm.makePersistent(helpdeskAnswer);
			
			answerKey = KeyFactory.keyToString(helpdeskAnswer.getKey());
			
			ticket.getAnswers().add(helpdeskAnswer);
		}
		catch (Exception ex) {
			
		}
		finally {
			pm.close();
		}
		
		return answerKey;
	}
}
