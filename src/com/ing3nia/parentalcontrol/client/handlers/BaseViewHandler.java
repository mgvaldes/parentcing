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
import com.ing3nia.parentalcontrol.client.handlers.click.DailyRouteClickHandler;
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
			
			SmartphoneClickHandler smClick = new SmartphoneClickHandler(smartphone.getKeyId(), baseBinder.getCenterContent(), menuSetter, deviceChoiceList, b);
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
		
		DashboardAlertListClickHandler alertListHandler = new DashboardAlertListClickHandler(user.getKey(), this.baseBinder.getCenterContent(), this.menuSetter);
		DashboardDeviceMapClickHandler dashDeviceMapHandler =  new DashboardDeviceMapClickHandler(user.getKey(), this.baseBinder.getCenterContent(), this.menuSetter, dummyDeviceLocations);
		alertListButton.addClickHandler(alertListHandler);
		deviceMap.addClickHandler(dashDeviceMapHandler);
		
		menuOptions.add(deviceMap);
		menuOptions.add(alertListButton);
		alertListButton.setStyleName("selectedShinnyButton");

		AlertListView view = new AlertListView(baseBinder.getCenterContent());		
		view.initAlertListView();
	}
	

	public class DashboardAlertListClickHandler implements ClickHandler{

			private String key;
			private HTMLPanel centerContent;
			private MenuSetterHandler menuSetter;
			
			public DashboardAlertListClickHandler(String key, HTMLPanel centerContent, MenuSetterHandler menuSetter){
				this.key = key;
				this.centerContent = centerContent;
				this.menuSetter = menuSetter;
			}
			
			@Override
			public void onClick(ClickEvent event) {
				this.centerContent.clear();
				this.menuSetter.clearMenuOptions();
				
				FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
				menuOptions.add(this.menuSetter.getDashboardDeviceMap());
				menuOptions.add(this.menuSetter.getDashboardAlertList());
				this.menuSetter.getDashboardAlertList().setStyleName("selectedShinnyButton");

				DeviceAlertListView view = new DeviceAlertListView(baseBinder.getCenterContent());		
				view.initDeviceAlertListView();
			}
		}
	
	public class DashboardDeviceMapClickHandler implements ClickHandler{

		private String key;
		private HTMLPanel centerContent;
		private MenuSetterHandler menuSetter;
		private ArrayList<GeoPtModel> deviceLocations;
		
		public DashboardDeviceMapClickHandler (String key, HTMLPanel centerContent, MenuSetterHandler menuSetter, ArrayList<GeoPtModel> deviceLocations){
			this.key = key;
			this.centerContent = centerContent;
			this.menuSetter = menuSetter;
			this.deviceLocations = deviceLocations;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			this.centerContent.clear();
			this.menuSetter.clearMenuOptions();
			
			FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
			menuOptions.add(this.menuSetter.getDashboardDeviceMap());
			menuOptions.add(this.menuSetter.getDashboardAlertList());
			this.menuSetter.getDashboardDeviceMap().setStyleName("selectedShinnyButton");

			DeviceMapView view = new DeviceMapView(baseBinder.getCenterContent());		
			view.setDeviceLocations(this.deviceLocations);
			view.initDeviceLocationLoad();
		}
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
			
			AlertListView view = new AlertListView(baseBinder.getCenterContent());		
			view.initAlertListView();
		}
	}
	
	public class AlertRulesClickHandler implements ClickHandler{

		private String key;
		private HTMLPanel centerContent;
		private MenuSetterHandler menuSetter;
		
		public AlertRulesClickHandler(String key, HTMLPanel centerContent, MenuSetterHandler menuSetter){
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
			this.menuSetter.getAlertRules().setStyleName("selectedShinnyButton");
			menuOptions.add(this.menuSetter.getDeviceContacts());
			menuOptions.add(this.menuSetter.getDeviceSettings());
			
			RuleListView view = new RuleListView(baseBinder.getCenterContent());		
			view.initRuleListView();
		}
	}
	
	public class DeviceContactsClickHandler implements ClickHandler{

		private String key;
		private HTMLPanel centerContent;
		private MenuSetterHandler menuSetter;
		
		public DeviceContactsClickHandler(String key, HTMLPanel centerContent, MenuSetterHandler menuSetter){
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
			this.menuSetter.getDeviceContacts().setStyleName("selectedShinnyButton");
			menuOptions.add(this.menuSetter.getDeviceSettings());
			
			DeviceContactListView view = new DeviceContactListView(baseBinder.getCenterContent());		
			view.initDeviceContactListView();
		}
	}
	
	
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
			
			DeviceSettingsView view = new DeviceSettingsView(baseBinder.getCenterContent());		
			view.initDeviceSettingsView();
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
	
	public void initTicketCenterMenu(){
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getTicketsButton());
		menuOptions.add(this.menuSetter.getOpenTickets());
		menuOptions.add(this.menuSetter.getClosedTickets());
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
