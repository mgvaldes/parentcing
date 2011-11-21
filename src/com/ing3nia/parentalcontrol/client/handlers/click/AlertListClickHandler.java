package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.views.AlertListView;

public class AlertListClickHandler implements ClickHandler{

	private String key;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	
	public AlertListClickHandler(String key, HTMLPanel centerContent, MenuSetterHandler menuSetter){
		this.key = key;
		this.centerContent = centerContent;
		this.menuSetter = menuSetter;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getDailyRoute());
		menuOptions.add(this.menuSetter.getAlertList());
		this.menuSetter.getAlertList().setStyleName("selectedShinnyButton");
		menuOptions.add(this.menuSetter.getAlertRules());
		menuOptions.add(this.menuSetter.getDeviceContacts());
		menuOptions.add(this.menuSetter.getDeviceSettings());
		
		AlertListView view = new AlertListView(centerContent);		
		view.initAlertListView();
	}
}