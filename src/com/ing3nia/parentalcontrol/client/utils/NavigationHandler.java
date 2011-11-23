package com.ing3nia.parentalcontrol.client.utils;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.InitDashboardClickHandler;

public class NavigationHandler {
	private static Button dashboardButton;
	public BaseViewHandler baseView;
	
	public NavigationHandler(BaseViewHandler baseView){
		this.baseView = baseView;
		initNavigationButtons();
	}
	
	public void initNavigationButtons(){
		dashboardButton = new Button();
		dashboardButton.setText("Dashboard");
		InitDashboardClickHandler dashboardClick = new InitDashboardClickHandler(baseView);
		dashboardButton.addClickHandler(dashboardClick);
	}
	
	public void setNavigationBar(FlowPanel navigationPanel, Button[] navigationButtons){
		navigationPanel.clear();

		if(navigationButtons.length>0){
			navigationPanel.add(navigationButtons[0]);
		}
		for(int b=1; b<navigationButtons.length; b++){
			Label sepLabel = getSeparationLabel();
			sepLabel.addStyleName("separationLabel");
			navigationPanel.add(sepLabel);
			navigationPanel.add(navigationButtons[b]);
		}
	}
	
	public Label getSeparationLabel(){
		Label separationLabel = new Label(">>");
		return separationLabel;
	}
	
	public void setDashboardNavigation(FlowPanel navigationPanel){
		Button[] navigationButtons = new Button[1];
		navigationButtons[0] = dashboardButton;
		setNavigationBar(navigationPanel, navigationButtons);
	}
}
