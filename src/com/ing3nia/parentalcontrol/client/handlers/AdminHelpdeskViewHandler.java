package com.ing3nia.parentalcontrol.client.handlers;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.ing3nia.parentalcontrol.client.PCAdminHelpdeskUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.click.AdminHelpdeskClosedTicketsClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.AdminHelpdeskOpenTicketsClickHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.views.AdminOpenTicketListView;

public class AdminHelpdeskViewHandler {
	
	private PCAdminHelpdeskUIBinder helpdeskBinder;
	private MenuSetterHandler menuSetter;
	private ClientAdminUserModel user;
	private Image loadingImage;
	
	public AdminHelpdeskViewHandler(PCAdminHelpdeskUIBinder helpdeskBinder, ClientAdminUserModel user){
		this.helpdeskBinder = helpdeskBinder;
		this.menuSetter = new MenuSetterHandler(helpdeskBinder);
		this.loadingImage = new Image("/media/images/loading-black.gif");
		this.user = user;
	}

	public void initHelpdeskView(){
		this.helpdeskBinder.getCenterContent().clear();
		this.menuSetter.clearMenuOptions();
		
		FlowPanel menuOptions = this.menuSetter.getHelpdeskCenterMenuOptions();
		Button openTicketsButton = this.menuSetter.getAdminOpenTickets();
		Button closedTicketsButton = this.menuSetter.getAdminClosedTickets();
		
		AdminHelpdeskOpenTicketsClickHandler openTicketsHandler = new AdminHelpdeskOpenTicketsClickHandler(this, user.getOpenTickets());
		openTicketsButton.addClickHandler(openTicketsHandler);
		
		AdminHelpdeskClosedTicketsClickHandler closedTicketsHandler = new AdminHelpdeskClosedTicketsClickHandler(this, user.getClosedTickets());
		closedTicketsButton.addClickHandler(closedTicketsHandler);
		
		menuOptions.add(openTicketsButton);
		menuOptions.add(closedTicketsButton);
		openTicketsButton.setStyleName("selectedShinnyButton");

		AdminOpenTicketListView openTicketsView = new AdminOpenTicketListView(this, user.getOpenTickets());
		openTicketsView.initAdminOpenTicketListView();
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
	
	public static void clearAllStyles(FlowPanel menuOptions) {
		int numButtons = menuOptions.getWidgetCount();
		
		for (int i = 0; i < numButtons; i++) {
			Button b = (Button)menuOptions.getWidget(i);
			b.setStyleName("");	
		}	
	}
}
