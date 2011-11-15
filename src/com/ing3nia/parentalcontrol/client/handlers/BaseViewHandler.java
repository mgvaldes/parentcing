package com.ing3nia.parentalcontrol.client.handlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.NewAdminUserView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class BaseViewHandler {
	
	/**
	 * UIBinder for base view
	 */
	private PCBaseUIBinder baseBinder;
	private MenuSetterHandler menuSetter;
	
	public BaseViewHandler(PCBaseUIBinder baseBinder){
		this.baseBinder = baseBinder;
		this.menuSetter = new MenuSetterHandler(baseBinder);
		
	}
	
	public void setAddAdministratorButton(){
		Button addAdminButton = baseBinder.getAddAdminButton();
		addAdminButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				FlowPanel centerMenu = menuSetter.getCenterMenuOptions();
				
				centerMenu.add(menuSetter.getAddUser());
				Button userList = menuSetter.getUserList();
				userList.setStyleName("selectedShinnyButton");
				centerMenu.add(userList);
				
				AdminUserListView view = new AdminUserListView(baseBinder.getCenterContent());		
				view.initAdminUserListView();
			}
		});
	} 
	
	
	public void setAdminUserListView(){
		Button userListButton = menuSetter.getUserList();
		userListButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AdminUserListView view = new AdminUserListView(baseBinder.getCenterContent());		
				
				Button addUser = menuSetter.getAddUser();
				addUser.setStyleName("");
				
				Button userList = menuSetter.getUserList();
				userList.setStyleName("selectedShinnyButton");
				
				view.initAdminUserListView();
			}
		});
	} 
	
	
	public void setNewAdminUserViewHandler(){
		Button newAdminUserButton = menuSetter.getAddUser();
		newAdminUserButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				NewAdminUserView view = new NewAdminUserView(baseBinder.getCenterContent());		
				
				Button addUser = menuSetter.getAddUser();
				addUser.setStyleName("selectedShinnyButton");
				
				Button userList = menuSetter.getUserList();
				userList.setStyleName("");
				
				view.initNewAdminUseView();
			}
		});
	}
	
	public PCBaseUIBinder getBaseBinder() {
		return baseBinder;
	}

	public void setBaseBinder(PCBaseUIBinder baseBinder) {
		this.baseBinder = baseBinder;
	}

	public MenuSetterHandler getMenuSetter() {
		return menuSetter;
	}

	public void setMenuSetter(MenuSetterHandler menuSetter) {
		this.menuSetter = menuSetter;
	}
	
	
	
}
