package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.utils.NavigationViewList;
import com.ing3nia.parentalcontrol.client.views.DeviceAlertListView;
import com.ing3nia.parentalcontrol.services.models.utils.SmartphoneModelUtils;

public class SmartphoneClickHandler implements ClickHandler{
	
	private String key;
	private BaseViewHandler baseView;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private FlowPanel deviceChoiceList;
	private Button button;
	private String smartphoneCount;
	private int smartphoneIndex;
	private SmartphoneModel smartphoneModel;
	
	public SmartphoneClickHandler(String key, BaseViewHandler baseView, Button b, int smartphoneIndex, SmartphoneModel smartphoneModel){
		this.key = key;
		this.baseView = baseView;
		this.centerContent = baseView.getBaseBinder().getCenterContent();
		this.menuSetter = baseView.getMenuSetter();
		this.deviceChoiceList = baseView.getBaseBinder().getDeviceChoiceList();
		this.button  = b;
		this.smartphoneIndex = smartphoneIndex;
		this.smartphoneModel = smartphoneModel;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();
		
		//setting clicked smartphone index
		baseView.setClickedSmartphoneIndex(this.smartphoneIndex);
		
		baseView.getBaseBinder().getCenterMenuOptions().clear();
		this.menuSetter = new MenuSetterHandler(baseView.getBaseBinder());
		baseView.setMenuSetter(this.menuSetter);
		baseView.initDeviceMenuClickHandlers();
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getDailyRoute());
		menuOptions.add(this.menuSetter.getAlertList());
		this.menuSetter.getAlertList().setStyleName("selectedShinnyButton");
		menuOptions.add(this.menuSetter.getAlertRules());
		menuOptions.add(this.menuSetter.getDeviceContacts());
		menuOptions.add(this.menuSetter.getDeviceSettings());
		
		menuSetter.setParentSmartphoneButton(this.button);
		
		deviceChoiceList = BaseViewHandler
		.clearRouteNamesPanels(deviceChoiceList);
		
		this.deviceChoiceList = BaseViewHandler.clearSmartphoneListStyle(this.deviceChoiceList);
		this.button.setStyleName("selectedSmartphone", true);
		
		//set navigation smarpthone button
		NavigationViewList.setSmartphoneButton(baseView.getUser().getSmartphones().get(smartphoneIndex).getName(), this);
		NavigationHandler navHandler = new NavigationHandler(baseView);
		navHandler.setSmartphoneNavigation(baseView.getBaseBinder().getNavigationPanel());
		
		//TODO set selected button style
		//this.button.setStyleName("selectedSmartphoneButton");
		
		
		DailyRouteClickHandler dailyRouteClickHandler = new DailyRouteClickHandler(baseView.getUser().getKey(), baseView);
		dailyRouteClickHandler.activateDailyRouteClickHandler();
		//DeviceAlertListView view = new DeviceAlertListView(this.baseView, SmartphoneModel.getUserAlertList(smartphoneModel));	
		//DeviceAlertListView view = new DeviceAlertListView(centerContent, smartphoneModel.getAlerts());
		//view.initDeviceAlertListView();
	}

	public int getSmartphoneIndex() {
		return smartphoneIndex;
	}

	public void setSmartphoneIndex(int smartphoneIndex) {
		this.smartphoneIndex = smartphoneIndex;
	}
	
}