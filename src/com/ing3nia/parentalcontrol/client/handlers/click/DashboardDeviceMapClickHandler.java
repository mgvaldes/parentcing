package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;

public class DashboardDeviceMapClickHandler implements ClickHandler{

	private String key;
	private BaseViewHandler baseView;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private ArrayList<GeoPtModel> deviceLocations;
	
	public DashboardDeviceMapClickHandler (String key, BaseViewHandler baseView){
		this.key = key;
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
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getDashboardDeviceMap());
		menuOptions.add(this.menuSetter.getDashboardAlertList());
		this.menuSetter.getDashboardDeviceMap().setStyleName("selectedShinnyButton");

		DeviceMapView view = new DeviceMapView(centerContent);		
		view.setDeviceLocations(this.deviceLocations);
		view.initDeviceLocationLoad();
	}
}