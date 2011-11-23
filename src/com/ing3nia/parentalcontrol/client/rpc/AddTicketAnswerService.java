package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;

@RemoteServiceRelativePath("add-ticket-answer")
public interface AddTicketAnswerService extends RemoteService {
	public String addTicketAnswer(TicketAnswerModel answer, String ticketKey, boolean isAdmin) throws IllegalArgumentException;
}
