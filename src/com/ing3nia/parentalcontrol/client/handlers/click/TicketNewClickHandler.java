package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.NewTicketView;
import com.ing3nia.parentalcontrol.client.views.TicketDetailsView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class TicketNewClickHandler implements ClickHandler{

		private String userKey;
		private HTMLPanel centerContent;
		private MenuSetterHandler menuSetter;
		private BaseViewHandler baseViewHandler;
		private ArrayList<TicketModel> tickets;

		
		public TicketNewClickHandler(BaseViewHandler baseViewHandler, String userKey, ArrayList<TicketModel> tickets){
			this.userKey = userKey;
			this.tickets = tickets;
			this.baseViewHandler = baseViewHandler;
			this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
			this.menuSetter = baseViewHandler.getMenuSetter();
		}
		
		@Override
		public void onClick(ClickEvent event) {

			baseViewHandler.toggleTicketCenterMenu(CenterMenuOptionsClassNames.NewTicket);
			//NewTicketView view = new NewTicketView(this.centerContent);	
			NewTicketView view = new NewTicketView(centerContent, userKey, tickets);
			view.initNewTicketView();		
		}
}

