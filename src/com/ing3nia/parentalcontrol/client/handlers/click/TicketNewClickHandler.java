package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.views.NewTicketView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class TicketNewClickHandler implements ClickHandler{

		private HTMLPanel centerContent;
		private MenuSetterHandler menuSetter;
		private BaseViewHandler baseViewHandler;

		public TicketNewClickHandler(BaseViewHandler baseViewHandler){
			this.baseViewHandler = baseViewHandler;
			this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
			this.menuSetter = baseViewHandler.getMenuSetter();
		}
		
		@Override
		public void onClick(ClickEvent event) {
			this.centerContent.clear();
			
			BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.NewTicket, menuSetter.getCenterMenuOptions());
			this.baseViewHandler.toggleTicketCenterMenu(CenterMenuOptionsClassNames.NewTicket);
			
			NewTicketView newTicketView = new NewTicketView(baseViewHandler);
			newTicketView.initNewTicketView();
		}
}

