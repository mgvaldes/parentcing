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
	
	
	public MenuSetterHandler(PCBaseUIBinder baseBinder){
		this.baseBinder = baseBinder;
		this.centerMenuOptions = baseBinder.getCenterMenuOptions();
		this.userList =getCenterMenuButton(centerMenuOptions, CenterMenuOptionsClassNames.UserList.getClassname(), CenterMenuOptionsClassNames.UserList.getText());
		this.addUser = getCenterMenuButton(centerMenuOptions, CenterMenuOptionsClassNames.AddUser.getClassname(), CenterMenuOptionsClassNames.AddUser.getText());
	}
	/*
	public initCenterMenuOptionsHandlers(){
		
		
	}*/
	
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
	
}
