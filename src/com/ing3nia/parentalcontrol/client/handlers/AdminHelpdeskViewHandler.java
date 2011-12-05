package com.ing3nia.parentalcontrol.client.handlers;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.ing3nia.parentalcontrol.client.PCAdminHelpdeskUIBinder;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.click.AdminHelpdeskClosedTicketsClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.AdminHelpdeskOpenTicketsClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DashboardAlertListClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DashboardDeviceMapClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.SmartphoneClickHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.utils.PCSmartphoneMenuStyle;
import com.ing3nia.parentalcontrol.client.views.AdminOpenTicketListView;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;

public class AdminHelpdeskViewHandler {
	
	private PCAdminHelpdeskUIBinder helpdeskBinder;
	private MenuSetterHandler menuSetter;
	private ClientAdminUserModel user;
	private Image loadingImage;
	
	public AdminHelpdeskViewHandler(PCAdminHelpdeskUIBinder helpdeskBinder){
		this.helpdeskBinder = helpdeskBinder;
		this.menuSetter = new MenuSetterHandler(helpdeskBinder);
		this.loadingImage = new Image("/media/images/loading-black.gif");
	}

	public void initBaseView(){
		this.helpdeskBinder.getCenterContent().clear();
		this.menuSetter.clearMenuOptions();
		
		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		Button openTicketsButton = this.menuSetter.getAdminOpenTickets();
		Button closedTicketsButton = this.menuSetter.getAdminClosedTickets();
		
		AdminHelpdeskOpenTicketsClickHandler openTicketsHandler = new AdminHelpdeskOpenTicketsClickHandler();
		openTicketsButton.addClickHandler(openTicketsHandler);
		
		AdminHelpdeskClosedTicketsClickHandler closedTicketsHandler = new AdminHelpdeskClosedTicketsClickHandler();
		closedTicketsButton.addClickHandler(closedTicketsHandler);
		
		menuOptions.add(openTicketsButton);
		menuOptions.add(closedTicketsButton);
		openTicketsButton.setStyleName("selectedShinnyButton");

		AdminOpenTicketListView openTicketsView = new AdminOpenTicketListView(helpdeskBinder.getCenterContent(), user.getOpenTickets());
	}

	public PCAdminHelpdeskUIBinder getHelpdeskBinder() {
		return helpdeskBinder;
	}

	public void setHelpdeskBinder(PCAdminHelpdeskUIBinder helpdeskBinder) {
		this.helpdeskBinder = helpdeskBinder;
	}

	public MenuSetterHandler getMenuSetter() {
		return menuSetter;
	}

	public void setMenuSetter(MenuSetterHandler menuSetter) {
		this.menuSetter = menuSetter;
	}

	public ClientAdminUserModel getUser() {
		return user;
	}

	public void setUser(ClientAdminUserModel user) {
		this.user = user;
	}

	public Image getLoadingImage() {
		return loadingImage;
	}

	public void setLoadingImage(Image loadingImage) {
		this.loadingImage = loadingImage;
	}
}
