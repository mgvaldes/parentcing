package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListService;
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.views.async.UserTicketListCallbackHandler;

public class HelpDeskUserClickHandler implements ClickHandler {

	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private BaseViewHandler baseViewHandler;
	private boolean isAdmin;
	
	public HelpDeskUserClickHandler(BaseViewHandler baseViewHandler, boolean isAdmin){
		this.baseViewHandler = baseViewHandler;
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
		this.menuSetter = baseViewHandler.getMenuSetter();
		this.isAdmin = isAdmin;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();
		
		BaseViewHandler.clearSmartphoneListStyle(this.baseViewHandler.getBaseBinder().getDeviceChoiceList());

		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		
		
		if (isAdmin) {
			menuOptions.add(this.menuSetter.getAdminOpenTickets());
			menuOptions.add(this.menuSetter.getAdminClosedTickets());
			
			this.menuSetter.getAdminOpenTickets().setStyleName("selectedShinnyButton");
		}
		else {
			menuOptions.add(this.menuSetter.getUserTicketsButton());
			menuOptions.add(this.menuSetter.getUserNewTicket());
			
			this.menuSetter.getUserTicketsButton().setStyleName("selectedShinnyButton");
			
			UserTicketListCallbackHandler ticketsCallback = new UserTicketListCallbackHandler(baseViewHandler);
			UserTicketListServiceAsync ticketService = GWT.create(UserTicketListService.class);
			ticketService.getUserTicketList(this.baseViewHandler.getUser().getKey(), ticketsCallback);
		}
		
		NavigationHandler navHandler = new NavigationHandler(baseViewHandler);
		navHandler.setHelpdesk(baseViewHandler.getBaseBinder().getNavigationPanel());
		
	}
	
	public void setUserHelpDeskClickHandlers(){
		
		TicketListClickHandler ticketListHandler = new TicketListClickHandler(baseViewHandler);
		menuSetter.getUserTicketsButton().addClickHandler(ticketListHandler);
		
		TicketNewClickHandler ticketNewClickHandler = new TicketNewClickHandler(baseViewHandler);
		menuSetter.getUserNewTicket().addClickHandler(ticketNewClickHandler);
	}
	
}	