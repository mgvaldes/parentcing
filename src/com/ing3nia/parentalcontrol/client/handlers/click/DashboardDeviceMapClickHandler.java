package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;

public class DashboardDeviceMapClickHandler implements ClickHandler{

	private String key;
	private PCBaseUIBinder baseBinder;
	private BaseViewHandler baseView;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private ArrayList<GeoPtModel> deviceLocations;
	
	public DashboardDeviceMapClickHandler (String key, BaseViewHandler baseView, PCBaseUIBinder baseBinder){
		this.key = key;
		this.baseBinder = baseBinder;
		this.baseView = baseView;
		this.centerContent = baseView.getBaseBinder().getCenterContent();
		this.menuSetter = baseView.getMenuSetter();
		this.deviceLocations = getDeviceLocationsFromModel(baseView);
	}
	
	public ArrayList<GeoPtModel> getDeviceLocationsFromModel(BaseViewHandler baseView){
		return baseView.getDummyDeviceLocations();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();
		BaseViewHandler.clearSmartphoneListStyle(this.baseBinder.getDeviceChoiceList());
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getDashboardDeviceMap());
		menuOptions.add(this.menuSetter.getDashboardAlertList());
		this.menuSetter.getDashboardDeviceMap().setStyleName("selectedShinnyButton");

		NavigationHandler navHandler = new NavigationHandler(baseView);
		navHandler.setDashboardNavigation(baseView.getBaseBinder().getNavigationPanel());
		
		DeviceMapView view = new DeviceMapView(baseBinder,baseView);		
		view.setDeviceLocations(this.baseView.getUser().getDeviceLocations());
		view.initDeviceLocationLoad();
		
		
	}
}