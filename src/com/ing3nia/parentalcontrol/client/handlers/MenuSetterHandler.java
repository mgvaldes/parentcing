package com.ing3nia.parentalcontrol.client.handlers;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;


public class MenuSetterHandler {
	private PCBaseUIBinder baseBinder;
	private FlowPanel centerMenuOptions;
	
	private Button addUser;
	private Button userList;
	private Button dailyRoute;
	private Button alertList;
	private Button alertRules;
	private Button deviceContacts;
	private Button deviceSettings;
	
	
	public MenuSetterHandler(PCBaseUIBinder baseBinder){
		this.baseBinder = baseBinder;
		this.centerMenuOptions = baseBinder.getCenterMenuOptions();
		this.userList =getCenterMenuButton(centerMenuOptions, CenterMenuOptionsClassNames.UserList.getClassname(), CenterMenuOptionsClassNames.UserList.getText());
		this.addUser = getCenterMenuButton(centerMenuOptions, CenterMenuOptionsClassNames.AddUser.getClassname(), CenterMenuOptionsClassNames.AddUser.getText());
		this.dailyRoute = getCenterMenuButton(centerMenuOptions, CenterMenuOptionsClassNames.DailyRoute.getClassname(), CenterMenuOptionsClassNames.DailyRoute.getText());
		this.alertList = getCenterMenuButton(centerMenuOptions, CenterMenuOptionsClassNames.AlertList.getClassname(), CenterMenuOptionsClassNames.AlertList.getText());
		this.alertRules = getCenterMenuButton(centerMenuOptions, CenterMenuOptionsClassNames.AlertRules.getClassname(), CenterMenuOptionsClassNames.AlertRules.getText());
		this.deviceContacts = getCenterMenuButton(centerMenuOptions, CenterMenuOptionsClassNames.DeviceContacts.getClassname(), CenterMenuOptionsClassNames.DeviceContacts.getText());
		this.deviceSettings = getCenterMenuButton(centerMenuOptions, CenterMenuOptionsClassNames.DeviceSettings.getClassname(), CenterMenuOptionsClassNames.DeviceSettings.getText());
		
	}
	/*
	public initCenterMenuOptionsHandlers(){
		
		
	}*/
	
	/**
	 * Clears menu buttons style name and then the whole menu
	 */
	public void clearMenuOptions(){
		BaseViewHandler.clearAllStyles(centerMenuOptions);
		centerMenuOptions.clear();
	}
	
	public FlowPanel getCenterMenuOptions(){
		FlowPanel centerMenu = baseBinder.getCenterMenuOptions();
		return centerMenu;
	}
	
	public Button getCenterMenuButton(FlowPanel centerMenu, String styleName, String text){
		Button b = new Button();
		b.setStyleName(styleName);
		b.setText(text);
		return b;
	}

	public Button getAddUser() {
		return addUser;
	}

	public void setAddUser(Button addUser) {
		this.addUser = addUser;
	}

	public Button getUserList() {
		return userList;
	}

	public void setUserList(Button userList) {
		this.userList = userList;
	}

	public PCBaseUIBinder getBaseBinder() {
		return baseBinder;
	}

	public void setBaseBinder(PCBaseUIBinder baseBinder) {
		this.baseBinder = baseBinder;
	}

	public Button getDailyRoute() {
		return dailyRoute;
	}

	public void setDailyRoute(Button dailyRoute) {
		this.dailyRoute = dailyRoute;
	}

	public void setCenterMenuOptions(FlowPanel centerMenuOptions) {
		this.centerMenuOptions = centerMenuOptions;
	}

	public Button getAlertList() {
		return alertList;
	}

	public void setAlertList(Button alertList) {
		this.alertList = alertList;
	}

	public Button getAlertRules() {
		return alertRules;
	}

	public void setAlertRules(Button alertRules) {
		this.alertRules = alertRules;
	}

	public Button getDeviceContacts() {
		return deviceContacts;
	}

	public void setDeviceContacts(Button deviceContacts) {
		this.deviceContacts = deviceContacts;
	}

	public Button getDeviceSettings() {
		return deviceSettings;
	}

	public void setDeviceSettings(Button deviceSettings) {
		this.deviceSettings = deviceSettings;
	}

	
	
}
