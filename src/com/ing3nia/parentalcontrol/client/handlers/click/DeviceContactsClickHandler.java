package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.views.DeviceContactListView;

public class DeviceContactsClickHandler implements ClickHandler{

	private String key;
	BaseViewHandler baseView;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private SmartphoneModel smartphone;
	
	public DeviceContactsClickHandler(String key, BaseViewHandler baseView, SmartphoneModel smartphone){
		this.key = key;
		this.centerContent = baseView.getBaseBinder().getCenterContent();
		this.menuSetter = baseView.getMenuSetter();
		this.baseView = baseView;
		this.centerContent = baseView.getBaseBinder().getCenterContent();
		this.menuSetter = baseView.getMenuSetter();
		this.smartphone = smartphone;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();
		
		//Clear smartphone routes panel
		baseView.getBaseBinder().setDeviceChoiceList(BaseViewHandler
		.clearRouteNamesPanels(baseView.getBaseBinder().getDeviceChoiceList()));
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getDailyRoute());
		menuOptions.add(this.menuSetter.getAlertList());
		menuOptions.add(this.menuSetter.getAlertRules());
		menuOptions.add(this.menuSetter.getDeviceContacts());
		this.menuSetter.getDeviceContacts().setStyleName("selectedShinnyButton");
		menuOptions.add(this.menuSetter.getDeviceSettings());
		
		NavigationHandler navHandler = new NavigationHandler(baseView);
		navHandler.setSmartphoneNavigation(baseView.getBaseBinder().getNavigationPanel());
		
		//DeviceContactListView view = new DeviceContactListView(centerContent);
		DeviceContactListView view = new DeviceContactListView(baseView, baseView.getUser().getCid(), smartphone);
		view.initDeviceContactListView();
	}
}