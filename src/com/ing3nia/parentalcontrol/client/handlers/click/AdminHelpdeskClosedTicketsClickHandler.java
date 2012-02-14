package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.AdminHelpdeskViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.AdminClosedTicketListView;

public class AdminHelpdeskClosedTicketsClickHandler implements ClickHandler {
	
	private AdminHelpdeskViewHandler helpdeskViewHandler;
	private HTMLPanel centerContent;
	private ArrayList<TicketModel> closedTickets;
	private MenuSetterHandler menuSetter;

	public AdminHelpdeskClosedTicketsClickHandler(AdminHelpdeskViewHandler helpdeskViewHandler, ArrayList<TicketModel> closedTickets) {
		this.helpdeskViewHandler = helpdeskViewHandler;
		this.centerContent = helpdeskViewHandler.getHelpdeskBinder().getCenterContent();
		this.closedTickets = closedTickets;
		this.menuSetter = this.helpdeskViewHandler.getMenuSetter();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();

		FlowPanel menuOptions = this.menuSetter.getHelpdeskCenterMenuOptions();
		menuOptions.add(this.menuSetter.getAdminOpenTickets());
		menuOptions.add(this.menuSetter.getAdminClosedTickets());
		this.menuSetter.getAdminClosedTickets().setStyleName("selectedShinnyButton");
		
		AdminClosedTicketListView closedTicketsView = new AdminClosedTicketListView(this.helpdeskViewHandler, closedTickets);
		closedTicketsView.initAdminClosedTicketListView();
	}
}
