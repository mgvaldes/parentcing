package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.ing3nia.parentalcontrol.client.handlers.AdminHelpdeskViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.AdminClosedTicketListView;

public class AdminCloseTicketCallbackHandler implements AsyncCallback<Boolean> {
	
	private AdminHelpdeskViewHandler baseView;
	private TicketModel ticket;
	private MenuSetterHandler menuSetter;
	
	public AdminCloseTicketCallbackHandler(AdminHelpdeskViewHandler baseView, TicketModel ticket) {
		this.baseView = baseView;
		this.ticket = ticket;
		this.menuSetter = this.baseView.getMenuSetter();
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getHelpdeskBinder().getNotice().setText("An error occured. The ticket couldn't be closed.");
		baseView.getHelpdeskBinder().getCenterContent().add(baseView.getHelpdeskBinder().getNotice());
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {
			ClientAdminUserModel userModel = baseView.getUser();
			
			userModel.getOpenTickets().remove(ticket);
			userModel.getClosedTickets().add(ticket);
			
			baseView.getHelpdeskBinder().getCenterContent().clear();
			this.menuSetter.clearMenuOptions();

			FlowPanel menuOptions = this.menuSetter.getHelpdeskCenterMenuOptions();
			menuOptions.add(this.menuSetter.getAdminOpenTickets());
			menuOptions.add(this.menuSetter.getAdminClosedTickets());
			this.menuSetter.getAdminClosedTickets().setStyleName("selectedShinnyButton");
			
			AdminClosedTicketListView closedTicketsView = new AdminClosedTicketListView(this.baseView, userModel.getClosedTickets());
			closedTicketsView.initAdminClosedTicketListView();
		} 
		else {
			baseView.getHelpdeskBinder().getNotice().setText("An error occured. The ticket couldn't be closed.");
			baseView.getHelpdeskBinder().getCenterContent().add(baseView.getHelpdeskBinder().getNotice());
		}
	}
}