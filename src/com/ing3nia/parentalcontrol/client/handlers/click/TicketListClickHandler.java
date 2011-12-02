package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListService;
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListServiceAsync;
import com.ing3nia.parentalcontrol.client.views.async.UserTicketListCallbackHandler;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class TicketListClickHandler implements ClickHandler{
	
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private BaseViewHandler baseViewHandler;
	
	public TicketListClickHandler(BaseViewHandler baseViewHandler){
		this.baseViewHandler = baseViewHandler;
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
		this.menuSetter = baseViewHandler.getMenuSetter();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		
		BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.TicketList, menuSetter.getCenterMenuOptions());
		this.baseViewHandler.toggleTicketCenterMenu(CenterMenuOptionsClassNames.TicketList);
		
		UserTicketListCallbackHandler ticketsCallback = new UserTicketListCallbackHandler(baseViewHandler);
		UserTicketListServiceAsync ticketService = GWT.create(UserTicketListService.class);
		ticketService.getUserTicketList(this.baseViewHandler.getUser().getKey(), ticketsCallback);
	}
}