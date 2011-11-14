package com.ing3nia.parentalcontrol.client.handlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;

public class BaseViewHandler {
	
	/**
	 * UIBinder for base view
	 */
	private PCBaseUIBinder baseBinder;
	
	public BaseViewHandler(PCBaseUIBinder baseBinder){
		this.baseBinder = baseBinder;
		
	}

	public void setAddAdminButtonHandler(){
		Button addAdminButton = baseBinder.getAddAdminButton();
		addAdminButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AdminUserListView view = new AdminUserListView(baseBinder.getCenterContent());		
				view.initAdminUserListView();
			}
		});
		
	}
	
	public PCBaseUIBinder getBaseBinder() {
		return baseBinder;
	}

	public void setBaseBinder(PCBaseUIBinder baseBinder) {
		this.baseBinder = baseBinder;
	}
	
	
	
}
