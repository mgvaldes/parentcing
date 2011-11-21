package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.views.TicketListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class HelpDeskUserClickHandler implements ClickHandler{

	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private BaseViewHandler baseViewHandler;
	
	public HelpDeskUserClickHandler(BaseViewHandler baseViewHandler){
		this.baseViewHandler = baseViewHandler;
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
		this.menuSetter = baseViewHandler.getMenuSetter();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();
		
		baseViewHandler.initTicketUserCenterMenu();
		baseViewHandler.toggleTicketCenterMenu(CenterMenuOptionsClassNames.OpenTickets);
		
		TicketListView view = new TicketListView(centerContent);		
		view.initUserTicketList();
	}
	
	public void setUserHelpDeskClickHandlers(){
		
		TicketListClickHandler ticketListHandler = new TicketListClickHandler(baseViewHandler);
		menuSetter.getOpenTickets().addClickHandler(ticketListHandler);
		
		TicketNewClickHandler ticketNewClickHandler =  new TicketNewClickHandler(baseViewHandler);
		menuSetter.getNewTicket().addClickHandler(ticketNewClickHandler);
	}
	
}	