package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;

public class InitDashboardClickHandler implements ClickHandler {

		private BaseViewHandler baseViewHandler;
		public InitDashboardClickHandler(BaseViewHandler baseViewHandler){
			this.baseViewHandler = baseViewHandler;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			this.baseViewHandler.initDashboard();		
		}	
	}