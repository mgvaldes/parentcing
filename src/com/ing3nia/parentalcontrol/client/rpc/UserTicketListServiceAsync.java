package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.TicketModel;

public interface UserTicketListServiceAsync {
	public void getUserTicketList(String loggedUserKey, AsyncCallback<HashMap<String, ArrayList<TicketModel>>> async);
}
