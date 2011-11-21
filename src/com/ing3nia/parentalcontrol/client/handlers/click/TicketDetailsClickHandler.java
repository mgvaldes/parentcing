package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.views.TicketDetailsView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class TicketDetailsClickHandler implements ClickHandler{
	
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private BaseViewHandler baseViewHandler;
	private String subject;
	private String comment;
	private String date;
	
	public TicketDetailsClickHandler(BaseViewHandler baseViewHandler, String subject, String comment, String date){
		this.baseViewHandler = baseViewHandler;
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
		this.menuSetter = baseViewHandler.getMenuSetter();
		this.subject = subject;
		this.comment = comment;
		this.date = date;
	}
	
	@Override
	public void onClick(ClickEvent event) {

		baseViewHandler.toggleTicketCenterMenu(CenterMenuOptionsClassNames.TicketDetails);
		TicketDetailsView view = new TicketDetailsView(centerContent, subject, comment, date);		
		view.initTicketDetailsView(subject, comment, date);
	}
}
	