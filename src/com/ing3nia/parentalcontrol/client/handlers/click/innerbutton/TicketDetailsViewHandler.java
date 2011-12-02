package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.TicketDetailsView;

public class TicketDetailsViewHandler implements FieldUpdater<TicketModel, String> {

	BaseViewHandler baseView;
	boolean isAdmin;
	boolean openOrClosed;
	
	public TicketDetailsViewHandler(BaseViewHandler baseView, boolean isAdmin, boolean openOrClosed) {
		this.baseView = baseView;
		this.isAdmin = isAdmin;
		this.openOrClosed = openOrClosed;
	}
	
	@Override
	public void update(int index, TicketModel ticket, String value) {
		baseView.getBaseBinder().getCenterContent().clear();
		
		TicketDetailsView ticketView = new TicketDetailsView(this.baseView, ticket, isAdmin, this.openOrClosed);
		ticketView.initTicketDetailsView();
	}
}
