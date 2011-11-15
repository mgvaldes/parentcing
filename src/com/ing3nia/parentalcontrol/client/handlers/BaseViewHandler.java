package com.ing3nia.parentalcontrol.client.handlers;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.models.ClientSmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.AlertListView;
import com.ing3nia.parentalcontrol.client.views.DeviceAlertListView;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;
import com.ing3nia.parentalcontrol.client.views.NewAdminUserView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class BaseViewHandler {
	
	/**
	 * UIBinder for base view
	 */
	private PCBaseUIBinder baseBinder;
	private MenuSetterHandler menuSetter;
	private ClientUserModel user;
	private List<ClientSmartphoneModel> slist;
	
	public BaseViewHandler(PCBaseUIBinder baseBinder){
		this.baseBinder = baseBinder;
		this.menuSetter = new MenuSetterHandler(baseBinder);
	}

	public void initBaseView(){
		FlowPanel deviceChoiceList = baseBinder.getDeviceChoiceList();
		for(ClientSmartphoneModel smartphone : slist){
			Button b = new Button();
			b.setStyleName("buttonFromList");
			b.setText(smartphone.getName());
			
			SmartphoneClickHandler smClick = new SmartphoneClickHandler(smartphone.getKeyId(), baseBinder.getCenterContent(), menuSetter);
			b.addClickHandler(smClick);
			deviceChoiceList.add(b);
		}
	}
	
	public class SmartphoneClickHandler implements ClickHandler{
		
		private String key;
		private HTMLPanel centerContent;
		private MenuSetterHandler menuSetter;
		
		public SmartphoneClickHandler(String key, HTMLPanel centerContent, MenuSetterHandler menuSetter){
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
			
			/*
			DeviceAlertListView view = new DeviceAlertListView(baseBinder.getCenterContent());		
			view.initDeviceAlertListView();
			*/
			DeviceMapView view = new DeviceMapView();
			
		}

	}
	
	public void setAddAdministratorButton(){
		Button addAdminButton = baseBinder.getAddAdminButton();
		addAdminButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				FlowPanel centerMenu = menuSetter.getCenterMenuOptions();
				menuSetter.getCenterMenuOptions().clear();
				
				Button addUser = menuSetter.getAddUser();
				addUser.setStyleName("selectedShinnyButton");
				centerMenu.add(addUser);
				Button userList = menuSetter.getUserList();
				centerMenu.add(userList);
				clearOthersStyle(CenterMenuOptionsClassNames.AddUser, menuSetter.getCenterMenuOptions());
				
				NewAdminUserView view = new NewAdminUserView(baseBinder.getCenterContent());		
				view.initNewAdminUseView();
			}
		});
	} 
	
	
	public void setAdminUserListView(){
		Button userListButton = menuSetter.getUserList();
		userListButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AdminUserListView view = new AdminUserListView(baseBinder.getCenterContent());		

				Button userList = menuSetter.getUserList();
				userList.setStyleName("selectedShinnyButton");
				clearOthersStyle(CenterMenuOptionsClassNames.UserList, menuSetter.getCenterMenuOptions());
				
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
				
				clearOthersStyle(CenterMenuOptionsClassNames.AddUser, menuSetter.getCenterMenuOptions());
				
				view.initNewAdminUseView();
			}
		});
	}
	
	public static void clearOthersStyle(CenterMenuOptionsClassNames onFocusButton, FlowPanel menuOptions){
		int numButtons = menuOptions.getWidgetCount();
		for(int i=0; i<numButtons; i++){
			Button b = (Button)menuOptions.getWidget(i);
			if(!b.getText().equals(onFocusButton.getText())){
				b.setStyleName("");
			}
		}
	}
	
	public static void clearAllStyles(FlowPanel menuOptions){
		int numButtons = menuOptions.getWidgetCount();
		for(int i=0; i<numButtons; i++){
			Button b = (Button)menuOptions.getWidget(i);
			b.setStyleName("");	
			}	
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

	public ClientUserModel getUser() {
		return user;
	}

	public void setUser(ClientUserModel user) {
		this.user = user;
	}

	public List<ClientSmartphoneModel> getSlist() {
		return slist;
	}

	public void setSlist(List<ClientSmartphoneModel> slist) {
		this.slist = slist;
	}

	
	
}
