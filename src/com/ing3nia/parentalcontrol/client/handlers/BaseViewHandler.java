package com.ing3nia.parentalcontrol.client.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.ParentalControl;
import com.ing3nia.parentalcontrol.client.handlers.click.AddAdminClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.AdminUserListViewClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.AlertListClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.AlertRulesClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DailyRouteClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DashboardAlertListClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DashboardDeviceMapClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DeviceContactsClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DeviceSettingsClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.SmartphoneClickHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.utils.CookieHandler;
import com.ing3nia.parentalcontrol.client.utils.ModelLogger;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.utils.PCMapMarkersEnum;
import com.ing3nia.parentalcontrol.client.utils.PCMapStyle;
import com.ing3nia.parentalcontrol.client.utils.PCSmartphoneMenuStyle;
import com.ing3nia.parentalcontrol.client.utils.PCURLMapper;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.AlertListView;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;
import com.ing3nia.parentalcontrol.client.views.LoadingView;
import com.ing3nia.parentalcontrol.client.views.NewAdminUserView;
import com.ing3nia.parentalcontrol.client.views.async.AsyncronousCallsMessages;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class BaseViewHandler {
	
	/**
	 * UIBinder for base view
	 */
	private PCBaseUIBinder baseBinder;
	private MenuSetterHandler menuSetter;
	private ClientUserModel user;
	private List<SmartphoneModel> slist;
	private int clickedSmartphoneIndex = -1;
	private Image loadingImage;
	
	public BaseViewHandler(PCBaseUIBinder baseBinder){
		this.baseBinder = baseBinder;
		this.menuSetter = new MenuSetterHandler(baseBinder);
		this.slist = new ArrayList<SmartphoneModel>();
		//this.user = userModel;
		this.loadingImage = new Image("/media/images/loading-black.gif");
	}

	public void initBaseView(){
		FlowPanel deviceChoiceList = baseBinder.getDeviceChoiceList();
		int smartphoneCount = 0;
		for(SmartphoneModel smartphone : slist){
			Button b = new Button();
			FlowPanel buttonPanel = new FlowPanel();
			buttonPanel.setStyleName("buttonPanel");
			
			Image buttonIcon = new Image(PCSmartphoneMenuStyle.getIconImageURL(smartphoneCount));
			buttonIcon.setStyleName("buttonIcon");
			
			b.setStyleName("buttonFromList");
			b.setText(smartphone.getName());
			SmartphoneClickHandler smClick = new SmartphoneClickHandler(smartphone.getKeyId(), this, b, smartphoneCount, smartphone);
			b.addClickHandler(smClick);
			
			buttonPanel.add(b);
			buttonPanel.add(buttonIcon);
			deviceChoiceList.add(buttonPanel);
			smartphoneCount++;
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
		
		DashboardAlertListClickHandler alertListHandler = new DashboardAlertListClickHandler(user.getKey(), this, user.getUserAlertList());
		DashboardDeviceMapClickHandler dashDeviceMapHandler =  new DashboardDeviceMapClickHandler(user.getKey(), this, baseBinder);
		alertListButton.addClickHandler(alertListHandler);
		deviceMap.addClickHandler(dashDeviceMapHandler);
		
		menuOptions.add(deviceMap);
		menuOptions.add(alertListButton);
		deviceMap.setStyleName("selectedShinnyButton");

		DeviceMapView view = new DeviceMapView(baseBinder,this);
		
		//TODO Set deviceLocations from user
		ArrayList<GeoPtModel> deviceLocations =  user.getDeviceLocations();
		view.setDeviceLocations(deviceLocations);
		//view.setDeviceLocations(user.getDeviceLocations());
		view.initDeviceLocationLoad();

		//AlertListView view = new AlertListView(baseBinder.getCenterContent());		
		//view.initAlertListView();
	}
	
	
	public void initDeviceMenuClickHandlers(){

		Button dailyRouteButton = this.menuSetter.getDailyRoute();
		Button alertListButton = this.menuSetter.getAlertList();
		Button alertRulesButton = this.menuSetter.getAlertRules();
		Button deviceContactsButton = this.menuSetter.getDeviceContacts();
		Button deviceSettings = this.menuSetter.getDeviceSettings();
		
		// getting the associated smartphone
		SmartphoneModel smartphone = user.getSmartphones().get(this.getClickedSmartphoneIndex());
		
		DailyRouteClickHandler dailyRouteHandler = new DailyRouteClickHandler(user.getKey(), this);
		dailyRouteButton.addClickHandler(dailyRouteHandler);
		
		//AlertListClickHandler alertListHandler =  new AlertListClickHandler(user.getKey(), this);
		
		// TOOOOODOOOOOOOOOO  ----------------------------------------------------------------------
		
		AlertListClickHandler alertListHandler = new AlertListClickHandler(user.getKey(), this, SmartphoneModel.getUserAlertList(smartphone));
		alertListButton.addClickHandler(alertListHandler);
		
		//AlertRulesClickHandler alertRulesHandler =  new AlertRulesClickHandler(user.getKey(), this);
		
		//TODO
		if(smartphone.getRules()==null){
			smartphone.setRules(new ArrayList<RuleModel>());
		}
		
		
		AlertRulesClickHandler alertRulesHandler =  new AlertRulesClickHandler(user.getKey(), this, smartphone.getRules());
		alertRulesButton.addClickHandler(alertRulesHandler);
		
		DeviceContactsClickHandler deviceContactsHandler =  new DeviceContactsClickHandler(user.getKey(), this, smartphone);
		deviceContactsButton.addClickHandler(deviceContactsHandler);
		
		DeviceSettingsClickHandler deviceSettingsHandler = new DeviceSettingsClickHandler(user.getKey(), this, smartphone);
		deviceSettings.addClickHandler(deviceSettingsHandler);
		
	}
	
		
	public void setAddAdministratorButton(){

		Button addAdminButton = baseBinder.getAddAdminButton();
		addAdminButton.addClickHandler(new AddAdminClickHandler(menuSetter, this, baseBinder));
			
	} 
	
	public void setLogoutButton(){

		Button logoutButton = baseBinder.getLogout();
		logoutButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				CookieHandler.clearSessionCookie();
				RootPanel.get().clear();
				//Window.Location.replace(GWT.getModuleBaseURL());
				Window.Location.replace(PCURLMapper.CURRENT_BASE_URL);
				//Window.Location.reload();
			}
		});
	} 
	
	public void setAdminUserListView(){
		Button userListButton = menuSetter.getUserList();
		AdminUserListViewClickHandler adminUserListClickHandler = new AdminUserListViewClickHandler(this);
		userListButton.addClickHandler(adminUserListClickHandler);
	} 
	
	
	public void setNewAdminUserViewHandler(){
		Button newAdminUserButton = menuSetter.getAddUser();		
		newAdminUserButton.addClickHandler(new AddAdminClickHandler(menuSetter, this, baseBinder));
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
	
	public static FlowPanel clearRouteNamesPanels(FlowPanel deviceChoiceList){
		int widgetCount = deviceChoiceList.getWidgetCount();
		//Window.alert("Widget count "+widgetCount);
		ArrayList<Integer> removeList = new ArrayList<Integer>();
		for(int i = 0; i<widgetCount; i++){
			Widget w = deviceChoiceList.getWidget(i);
			if(w instanceof ScrollPanel){
				removeList.add(i);
			}
		}
		for(int j:removeList){
			deviceChoiceList.remove(j);
		}
		return deviceChoiceList;
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

	public List<SmartphoneModel> getSlist() {
		return slist;
	}

	public int getClickedSmartphoneIndex() {
		return clickedSmartphoneIndex;
	}

	public void setClickedSmartphoneIndex(int clickedSmartphoneIndex) {
		this.clickedSmartphoneIndex = clickedSmartphoneIndex;
	}

	public void setSlist(List<SmartphoneModel> slist) {
		this.slist = slist;
	}

	public Image getLoadingImage() {
		return loadingImage;
	}

	public void setLoadingImage(Image loadingImage) {
		this.loadingImage = loadingImage;
	}
	
}
