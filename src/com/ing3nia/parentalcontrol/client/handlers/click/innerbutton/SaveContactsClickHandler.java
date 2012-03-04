package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.ing3nia.parentalcontrol.client.views.DeviceContactListView;

public class SaveContactsClickHandler implements ClickHandler {

	DeviceContactListView deviceContactsView;

	public SaveContactsClickHandler(DeviceContactListView deviceContactsView) {
		this.deviceContactsView = deviceContactsView;
	}

	@Override
	public void onClick(ClickEvent event) {
		this.deviceContactsView.saveContacts();
	}
}
