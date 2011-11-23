package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.TicketModel;

@RemoteServiceRelativePath("usr-tickets")
public interface UserTicketListService extends RemoteService {
	public HashMap<String, ArrayList<TicketModel>> getUserTicketList(String loggedUserKey) throws IllegalArgumentException;
}
