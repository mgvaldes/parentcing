package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.TicketDetailsView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class TicketDetailsClickHandler implements ClickHandler{
	
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private String userKey;
	private BaseViewHandler baseViewHandler;

	private TicketModel ticket;
	private Boolean isAdmin;
	
	public TicketDetailsClickHandler(BaseViewHandler baseViewHandler, TicketModel ticket, String userKey, Boolean isAdmin){
		this.baseViewHandler = baseViewHandler;
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
		this.menuSetter = baseViewHandler.getMenuSetter();

		this.ticket = ticket;
		this.isAdmin = isAdmin;
	}
	
	@Override
	public void onClick(ClickEvent event) {

//		baseViewHandler.toggleTicketCenterMenu(CenterMenuOptionsClassNames.TicketList);
//		
//		//TicketDetailsView view = new TicketDetailsView(centerContent, subject, comment, date);		
//		TicketDetailsView view = new TicketDetailsView(centerContent, ticket, userKey, isAdmin);
//		
//		DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");
//		view.initTicketDetailsView(ticket.getSubject(), ticket.getComment(), formatter.format(ticket.getDate()));
	}
}
	