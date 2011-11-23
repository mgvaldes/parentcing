package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.TicketModel;

@RemoteServiceRelativePath("add-ticket")
public interface AddTicketService extends RemoteService {
	public String addTicket(TicketModel ticket, String loggedUserKey) throws IllegalArgumentException;
}
