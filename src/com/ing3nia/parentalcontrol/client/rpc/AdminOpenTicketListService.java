package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.TicketModel;

@RemoteServiceRelativePath("admin-open-tickets")
public interface AdminOpenTicketListService extends RemoteService {
	public ArrayList<TicketModel> adminOpenTicketList() throws IllegalArgumentException;
}