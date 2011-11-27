package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.views.NewAdminUserView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class AddAdminClickHandler implements ClickHandler {

	MenuSetterHandler menuSetter;
	BaseViewHandler baseView;
	PCBaseUIBinder baseBinder;
	
	public AddAdminClickHandler(MenuSetterHandler menuSetter, BaseViewHandler baseView, PCBaseUIBinder baseBinder){
		this.baseView = baseView;
		this.menuSetter = menuSetter;
		this.baseBinder =  baseBinder;
	}
	

	public void onClick(ClickEvent event) {

		FlowPanel centerMenu = menuSetter.getCenterMenuOptions();
		menuSetter.getCenterMenuOptions().clear();
		// Clean center menu

		Button addUser = menuSetter.getAddUser();
		addUser.setStyleName("selectedShinnyButton");
		centerMenu.add(addUser);
		Button userList = menuSetter.getUserList();
		centerMenu.add(userList);
		BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.AddUser,
				menuSetter.getCenterMenuOptions());

		NewAdminUserView view = new NewAdminUserView(
				baseBinder.getCenterContent(), baseView.getUser(), baseView);
		view.initNewAdminUseView();
	}
}
