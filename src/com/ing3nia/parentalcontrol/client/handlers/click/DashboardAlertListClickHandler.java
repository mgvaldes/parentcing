package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.views.DeviceAlertListView;

public class DashboardAlertListClickHandler implements ClickHandler {

	private String key;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;

	public DashboardAlertListClickHandler(String key,
			HTMLPanel centerContent, MenuSetterHandler menuSetter) {
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
		this.menuSetter.getDashboardAlertList().setStyleName(
				"selectedShinnyButton");

		DeviceAlertListView view = new DeviceAlertListView(
				centerContent);
		view.initDeviceAlertListView();
	}
}