package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.TicketModel;

public interface AddTicketServiceAsync {
	public void addTicket(TicketModel ticket, String loggedUserKey, AsyncCallback<String> async);
}
