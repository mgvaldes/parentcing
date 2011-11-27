package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CloseTicketServiceAsync {
	public void closeTicket(String ticketKey, AsyncCallback<Boolean> async);
}
