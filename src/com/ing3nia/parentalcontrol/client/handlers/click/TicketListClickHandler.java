package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.views.TicketListView;
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

		baseViewHandler.toggleTicketCenterMenu(CenterMenuOptionsClassNames.TicketList);
		//TODO FIX NULL ASSIGNMENTS
		TicketListView view = new TicketListView(centerContent, null, null);
		//TicketListView view = new TicketListView(centerContent);	
		view.initUserTicketList();
	}
}