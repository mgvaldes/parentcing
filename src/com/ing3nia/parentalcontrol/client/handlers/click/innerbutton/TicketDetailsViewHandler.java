package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.cell.client.FieldUpdater;
import com.ing3nia.parentalcontrol.client.handlers.AdminHelpdeskViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.AdminTicketDetailsView;
import com.ing3nia.parentalcontrol.client.views.TicketDetailsView;

public class TicketDetailsViewHandler implements FieldUpdater<TicketModel, String> {

	private BaseViewHandler baseView;
	private AdminHelpdeskViewHandler helpdeskView;
	boolean isAdmin;
	boolean openOrClosed;
	
	public TicketDetailsViewHandler(BaseViewHandler baseView, boolean isAdmin, boolean openOrClosed) {
		this.baseView = baseView;
		this.isAdmin = isAdmin;
		this.openOrClosed = openOrClosed;
	}
	
	public TicketDetailsViewHandler(AdminHelpdeskViewHandler helpdeskView, boolean isAdmin, boolean openOrClosed) {
		this.helpdeskView = helpdeskView;
		this.isAdmin = isAdmin;
		this.openOrClosed = openOrClosed;
	}
	
	@Override
	public void update(int index, TicketModel ticket, String value) {
		if (isAdmin) {
			helpdeskView.getHelpdeskBinder().getCenterContent().clear();
			
			AdminTicketDetailsView ticketView = new AdminTicketDetailsView(this.helpdeskView, ticket, isAdmin, this.openOrClosed);
			ticketView.initTicketDetailsView();
		}
		else {
			baseView.getBaseBinder().getCenterContent().clear();
			
			TicketDetailsView ticketView = new TicketDetailsView(this.baseView, ticket, isAdmin, this.openOrClosed);
			ticketView.initTicketDetailsView();
		}
	}
}
