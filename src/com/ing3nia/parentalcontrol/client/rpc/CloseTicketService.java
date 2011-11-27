package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("close-ticket")
public interface CloseTicketService extends RemoteService {
	public Boolean closeTicket(String ticketKey) throws IllegalArgumentException;
}