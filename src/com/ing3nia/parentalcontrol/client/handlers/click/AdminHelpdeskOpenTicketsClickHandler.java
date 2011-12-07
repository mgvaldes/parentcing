package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.AdminHelpdeskViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.AdminOpenTicketListView;

public class AdminHelpdeskOpenTicketsClickHandler implements ClickHandler {
	
	private HTMLPanel centerContent;
	private AdminHelpdeskViewHandler helpdeskViewHandler;
	private ArrayList<TicketModel> openTickets;
	private MenuSetterHandler menuSetter;

	public AdminHelpdeskOpenTicketsClickHandler(AdminHelpdeskViewHandler helpdeskViewHandler, ArrayList<TicketModel> openTickets) {
		this.helpdeskViewHandler = helpdeskViewHandler;
		this.centerContent = helpdeskViewHandler.getHelpdeskBinder().getCenterContent();
		this.openTickets = openTickets;
		this.menuSetter = this.helpdeskViewHandler.getMenuSetter();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();

		FlowPanel menuOptions = this.menuSetter.getHelpdeskCenterMenuOptions();
		menuOptions.add(this.menuSetter.getAdminOpenTickets());
		menuOptions.add(this.menuSetter.getAdminClosedTickets());
		this.menuSetter.getAdminOpenTickets().setStyleName("selectedShinnyButton");
		
		AdminOpenTicketListView openTicketsView = new AdminOpenTicketListView(this.helpdeskViewHandler, openTickets);
		openTicketsView.initAdminOpenTicketListView();
	}
}
