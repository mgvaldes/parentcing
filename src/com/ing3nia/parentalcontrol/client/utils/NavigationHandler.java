package com.ing3nia.parentalcontrol.client.utils;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;

public class NavigationHandler {
	//private static Button dashboardButton;
	public BaseViewHandler baseView;
	
	public NavigationHandler(BaseViewHandler baseView){
		this.baseView = baseView;
		//initNavigationButtons();
	}
	/*
	public void initNavigationButtons(){
		dashboardButton = new Button();
		dashboardButton.setText("Dashboard");
		InitDashboardClickHandler dashboardClick = new InitDashboardClickHandler(baseView);
		dashboardButton.addClickHandler(dashboardClick);
	}
	*/
	public void setNavigationBar(FlowPanel navigationPanel, Button[] navigationButtons){
		navigationPanel.clear();

		if(navigationButtons.length>0){
			navigationButtons[0].setStyleName("navButton");
			navigationPanel.add(navigationButtons[0]);
			Label sepLabel = getSeparationLabel();
			sepLabel.setStyleName("navSeparationLabel");
			navigationPanel.add(sepLabel);
		}
		for(int b=1; b<navigationButtons.length; b++){
			navigationButtons[b].setStyleName("navButton");
			navigationPanel.add(navigationButtons[b]);
			Label sepLabel = getSeparationLabel();
			sepLabel.setStyleName("navSeparationLabel");
			navigationPanel.add(sepLabel);
		}
	}
	
	public Label getSeparationLabel(){
		Label separationLabel = new Label(">>");
		return separationLabel;
	}
	
	public void setDashboardNavigation(FlowPanel navigationPanel){
		navigationPanel.clear();
		Button[] navigationButtons = NavigationViewList.getNavigationDashboard(baseView);
		setNavigationBar(navigationPanel, navigationButtons);
	}
	
	public void setSmartphoneNavigation(FlowPanel navigationPanel){
		navigationPanel.clear();
		Button[] navigationButtons = NavigationViewList.getNavigationSmartphone(baseView);
		setNavigationBar(navigationPanel, navigationButtons);
	}
	
	public void setRuleListNavigation(FlowPanel navigationPanel, ArrayList<RuleModel> rules){
		navigationPanel.clear();
		Button[] navigationButtons = NavigationViewList.getNavigationRuleList(baseView, rules);
		setNavigationBar(navigationPanel, navigationButtons);
	}
	
	public void setAddNewRuleNavigation(FlowPanel navigationPanel){
		navigationPanel.clear();
		Button[] navigationButtons = NavigationViewList.getNavigationAddNewRule(baseView);
		setNavigationBar(navigationPanel, navigationButtons);
	}	
	
	public void setAddAdministrator(FlowPanel navigationPanel){
		navigationPanel.clear();
		Button[] navigationButtons = NavigationViewList.getNavigationAddAdmin(baseView);
		setNavigationBar(navigationPanel, navigationButtons);
	}
	
	public void setHelpdesk(FlowPanel navigationPanel){
		navigationPanel.clear();
		Button[] navigationButtons = NavigationViewList.getNavigationHelpdesk(baseView);
		setNavigationBar(navigationPanel, navigationButtons);
	}
	
}
