package com.ing3nia.parentalcontrol.client.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.click.AlertListClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.AlertRulesClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DailyRouteClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DashboardAlertListClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DashboardDeviceMapClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DeviceContactsClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DeviceSettingsClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.SmartphoneClickHandler;
import com.ing3nia.parentalcontrol.client.models.ClientSmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.AlertListView;
import com.ing3nia.parentalcontrol.client.views.DeviceAlertListView;
import com.ing3nia.parentalcontrol.client.views.DeviceContactListView;
import com.ing3nia.parentalcontrol.client.views.DeviceDailyRouteView;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;
import com.ing3nia.parentalcontrol.client.views.DeviceSettingsView;
import com.ing3nia.parentalcontrol.client.views.NewAdminUserView;
import com.ing3nia.parentalcontrol.client.views.RuleListView;
import com.ing3nia.parentalcontrol.client.views.TicketDetailsView;
import com.ing3nia.parentalcontrol.client.views.TicketListView;
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
			
			SmartphoneClickHandler smClick = new SmartphoneClickHandler(smartphone.getKeyId(), this, baseBinder.getCenterContent(), menuSetter, deviceChoiceList, b);
			b.addClickHandler(smClick);
			deviceChoiceList.add(b);
		}
		
		initDashboard();
	}
	
	public ArrayList<GeoPtModel> getDummyDeviceLocations(){
		ArrayList<GeoPtModel> deviceLocations = new ArrayList<GeoPtModel>();
		GeoPtModel geo = new GeoPtModel();
		geo.setLatitude(48.89364);
		geo.setLongitude(2.43739);
		deviceLocations.add(geo);
		
		geo = new GeoPtModel();
		geo.setLatitude(48.88364);
		geo.setLongitude(2.5939);
		deviceLocations.add(geo);
		
		geo = new GeoPtModel();
		geo.setLatitude(48.84364);
		geo.setLongitude(2.5539);
		deviceLocations.add(geo);
		
		return deviceLocations;
		
	}
	
	/**
	 * Initializes the dashboard adding the Device Map and Alert List button
	 */
	public void initDashboard(){
		//dummy content
		ArrayList<GeoPtModel> dummyDeviceLocations = this.getDummyDeviceLocations();
		
		this.baseBinder.getCenterContent().clear();
		this.menuSetter.clearMenuOptions();
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		Button alertListButton = this.menuSetter.getDashboardAlertList();
		Button deviceMap = this.menuSetter.getDashboardDeviceMap();
		
		DashboardAlertListClickHandler alertListHandler = new DashboardAlertListClickHandler(user.getKey(),this.baseBinder.getCenterContent(), this.menuSetter);
		DashboardDeviceMapClickHandler dashDeviceMapHandler =  new DashboardDeviceMapClickHandler(user.getKey(), this.baseBinder.getCenterContent(), this.menuSetter, dummyDeviceLocations);
		alertListButton.addClickHandler(alertListHandler);
		deviceMap.addClickHandler(dashDeviceMapHandler);
		
		menuOptions.add(deviceMap);
		menuOptions.add(alertListButton);
		alertListButton.setStyleName("selectedShinnyButton");

		AlertListView view = new AlertListView(baseBinder.getCenterContent());		
		view.initAlertListView();
	}
	
	
	public void initDeviceMenuClickHandlers(){

		Button dailyRouteButton = this.menuSetter.getDailyRoute();
		Button alertListButton = this.menuSetter.getAlertList();
		Button alertRulesButton = this.menuSetter.getAlertRules();
		Button deviceContactsButton = this.menuSetter.getDeviceContacts();
		Button deviceSettings = this.menuSetter.getDeviceSettings();
		
		DailyRouteClickHandler dailyRouteHandler = new DailyRouteClickHandler(user.getKey(), this.baseBinder, this.baseBinder.getCenterContent(), this.menuSetter, this.baseBinder.getDeviceChoiceList());
		dailyRouteButton.addClickHandler(dailyRouteHandler);
		
		AlertListClickHandler alertListHandler =  new AlertListClickHandler(user.getKey(), this.baseBinder.getCenterContent(), this.menuSetter);
		alertListButton.addClickHandler(alertListHandler);
		
		AlertRulesClickHandler alertRulesHandler =  new AlertRulesClickHandler(user.getKey(), this.baseBinder.getCenterContent(), this.menuSetter);
		alertRulesButton.addClickHandler(alertRulesHandler);
		
		DeviceContactsClickHandler deviceContactsHandler =  new DeviceContactsClickHandler(user.getKey(), this.baseBinder.getCenterContent(), this.menuSetter);
		deviceContactsButton.addClickHandler(deviceContactsHandler);
		
		DeviceSettingsClickHandler deviceSettingsHandler = new DeviceSettingsClickHandler(user.getKey(), this.baseBinder.getCenterContent(), this.menuSetter);
		deviceSettings.addClickHandler(deviceSettingsHandler);
		
	}
	
		
	public void setAddAdministratorButton(){
		Button addAdminButton = baseBinder.getAddAdminButton();
		addAdminButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				FlowPanel centerMenu = menuSetter.getCenterMenuOptions();
				menuSetter.getCenterMenuOptions().clear();
				//Clean center menu
				
				
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
	
	public void initTicketAdminCenterMenu(){
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getTicketsButton());
		menuOptions.add(this.menuSetter.getOpenTickets());
		menuOptions.add(this.menuSetter.getClosedTickets());
	}
	
	
	public void initTicketUserCenterMenu(){
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getOpenTickets());
		menuOptions.add(this.menuSetter.getNewTicket());
	}
	
	public void clearCenterMenuStyle(){
		// TODO complete this method and use it out there
	}
	
	public void toggleDeviceCenterMenu(CenterMenuOptionsClassNames menuOption){
		if(menuOption.equals(CenterMenuOptionsClassNames.AlertList)){
			this.menuSetter.getAlertList().setStyleName("selectedShinnyButton");
		}
		else if(menuOption.equals(CenterMenuOptionsClassNames.AlertRules)){
			this.menuSetter.getAlertRules().setStyleName("selectedShinnyButton");
		}
		else if(menuOption.equals(CenterMenuOptionsClassNames.DeviceContacts)){
			this.menuSetter.getDeviceContacts().setStyleName("selectedShinnyButton");
		}
		else if(menuOption.equals(CenterMenuOptionsClassNames.DeviceSettings)){
			this.menuSetter.getDeviceSettings().setStyleName("selectedShinnyButton");
		}
		else if(menuOption.equals(CenterMenuOptionsClassNames.DailyRoute)){
			this.menuSetter.getDailyRoute().setStyleName("selectedShinnyButton");
		}
	}
	
	public void toggleTicketCenterMenu(CenterMenuOptionsClassNames menuOption){
		if(menuOption.equals(CenterMenuOptionsClassNames.TicketList)){
			this.menuSetter.getTicketsButton().setStyleName("selectedShinnyButton");
		}
		else if(menuOption.equals(CenterMenuOptionsClassNames.OpenTickets)){
			this.menuSetter.getOpenTickets().setStyleName("selectedShinnyButton");
		}
		else if(menuOption.equals(CenterMenuOptionsClassNames.ClosedTickets)){
			this.menuSetter.getClosedTickets().setStyleName("selectedShinnyButton");
		}
		else if(menuOption.equals(CenterMenuOptionsClassNames.NewTicket)){
			this.menuSetter.getClosedTickets().setStyleName("selectedShinnyButton");
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
