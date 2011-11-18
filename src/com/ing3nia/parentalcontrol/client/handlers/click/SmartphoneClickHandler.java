package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.views.DeviceAlertListView;

public class SmartphoneClickHandler implements ClickHandler{
	
	private String key;
	private BaseViewHandler baseView;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private FlowPanel deviceChoiceList;
	private Button button;
	
	public SmartphoneClickHandler(String key, BaseViewHandler baseView, HTMLPanel centerContent, MenuSetterHandler menuSetter, FlowPanel deviceChoiceList, Button b){
		this.key = key;
		this.baseView = baseView;
		this.centerContent = centerContent;
		this.menuSetter = menuSetter;
		this.deviceChoiceList = deviceChoiceList;
		this.button  = b;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();
		baseView.initDeviceMenuClickHandlers();
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getDailyRoute());
		menuOptions.add(this.menuSetter.getAlertList());
		this.menuSetter.getAlertList().setStyleName("selectedShinnyButton");
		menuOptions.add(this.menuSetter.getAlertRules());
		menuOptions.add(this.menuSetter.getDeviceContacts());
		menuOptions.add(this.menuSetter.getDeviceSettings());
		
		menuSetter.setParentSmartphoneButton(this.button);
		
		//TODO set selected button style
		//this.button.setStyleName("selectedSmartphoneButton");
		
		DeviceAlertListView view = new DeviceAlertListView(centerContent);		
		view.initDeviceAlertListView();
	}
}