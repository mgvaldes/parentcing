package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.AlertModel;
import com.ing3nia.parentalcontrol.client.views.AlertListView;
import com.ing3nia.parentalcontrol.client.views.DeviceAlertListView;

public class DashboardAlertListClickHandler implements ClickHandler {

	private String key;
	private BaseViewHandler baseView;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private ArrayList<AlertModel> alertList;

	public DashboardAlertListClickHandler(String key,BaseViewHandler baseView, ArrayList<AlertModel> alertList) {
		this.key = key;
		this.baseView = baseView;
		this.centerContent = baseView.getBaseBinder().getCenterContent();
		this.menuSetter = baseView.getMenuSetter();
		this.alertList = alertList;
	}


	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();

		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getDashboardDeviceMap());
		menuOptions.add(this.menuSetter.getDashboardAlertList());
		this.menuSetter.getDashboardAlertList().setStyleName(
				"selectedShinnyButton");

		AlertListView view = new AlertListView(centerContent, alertList);
		view.initAlertListView();
		/*
		DeviceAlertListView view = new DeviceAlertListView(
				centerContent, alertList);
		view.initDeviceAlertListView();
		*/
	}
}