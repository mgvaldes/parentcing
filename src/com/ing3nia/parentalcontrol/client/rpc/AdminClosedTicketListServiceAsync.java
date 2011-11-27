package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.TicketModel;

public interface AdminClosedTicketListServiceAsync {
	public void adminClosedTicketList(AsyncCallback<ArrayList<TicketModel>> async);
}