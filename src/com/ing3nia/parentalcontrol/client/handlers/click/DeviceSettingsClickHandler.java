package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.views.DeviceSettingsView;

public class DeviceSettingsClickHandler implements ClickHandler{

	private String key;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	
	public DeviceSettingsClickHandler(String key, HTMLPanel centerContent, MenuSetterHandler menuSetter){
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
		menuOptions.add(this.menuSetter.getAlertRules());
		menuOptions.add(this.menuSetter.getDeviceContacts());
		menuOptions.add(this.menuSetter.getDeviceSettings());
		this.menuSetter.getDeviceSettings().setStyleName("selectedShinnyButton");
		
		DeviceSettingsView view = new DeviceSettingsView(centerContent);		
		view.initDeviceSettingsView();
	}
}