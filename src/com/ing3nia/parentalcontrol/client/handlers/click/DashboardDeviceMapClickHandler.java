package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;

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

		DeviceMapView view = new DeviceMapView(centerContent);		
		view.setDeviceLocations(this.deviceLocations);
		view.initDeviceLocationLoad();
	}
}