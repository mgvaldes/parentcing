package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.cell.client.FieldUpdater;
import com.ing3nia.parentalcontrol.client.handlers.AdminHelpdeskViewHandler;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.AdminTicketDetailsView;

public class AdminTicketDetailsViewHandler implements FieldUpdater<TicketModel, String> {

	AdminHelpdeskViewHandler baseView;
	boolean isAdmin;
	boolean openOrClosed;
	
	public AdminTicketDetailsViewHandler(AdminHelpdeskViewHandler baseView, boolean isAdmin, boolean openOrClosed) {
		this.baseView = baseView;
		this.isAdmin = isAdmin;
		this.openOrClosed = openOrClosed;
	}
	
	@Override
	public void update(int index, TicketModel ticket, String value) {
		baseView.getHelpdeskBinder().getCenterContent().clear();
		
		AdminTicketDetailsView ticketView = new AdminTicketDetailsView(this.baseView, ticket, isAdmin, this.openOrClosed);
		ticketView.initTicketDetailsView();
	}
}
