package com.ing3nia.parentalcontrol.client.utils;

import java.util.ArrayList;

import org.apache.http.cookie.SM;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.AlertRulesClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.InitDashboardClickHandler;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;

public class NavigationViewList {
	
	static Button DASHBOARD_BUTTON = new Button("Dashboard");
	static Button SMARTPHONE_BUTTON = new Button("Device");
	static Button RULE_LIST = new Button("Rule List");
	static Button ADD_RULE = new Button("Add New Rule");
	static Button ADD_ADMIN = new Button("Add Administrator");
	static Button HELP_DESK = new Button("Helpdesk");
	
	static void initNavigationButtons(BaseViewHandler baseView){
		
		//Dashboard click handler
		InitDashboardClickHandler dashboardClick = new InitDashboardClickHandler(baseView);
		DASHBOARD_BUTTON.addClickHandler(dashboardClick);
		
	}
	
	public static Button getDashboardNavButton(){
		return DASHBOARD_BUTTON;
	}
	
	public static Button getSmartphoneButton(){
		return SMARTPHONE_BUTTON;
	}
	
	public static Button getAddNewRule(){
		return ADD_RULE;
	}
	
	public static Button getAdmin(){
		return ADD_ADMIN;
	}
	
	public static Button getHelpDesk(){
		return HELP_DESK;
	}
	
	public static void setSmartphoneButton(String smartphoneName, ClickHandler clickHandler){
		SMARTPHONE_BUTTON = new Button(smartphoneName);
		SMARTPHONE_BUTTON.setStyleName("navButton");
		SMARTPHONE_BUTTON.addClickHandler(clickHandler);
	}
	
	public static Button getRuleListNavButton(BaseViewHandler baseView, ArrayList<RuleModel> rules){
		//Rule List click handler
		AlertRulesClickHandler alertRulesHandler =  new AlertRulesClickHandler(baseView.getUser().getKey(), baseView, rules);
		RULE_LIST = new Button("Rule List");
		RULE_LIST.addClickHandler(alertRulesHandler);
		return RULE_LIST;
	}
	
	
	public static Button[] getNavigationDashboard(BaseViewHandler baseView){
		Button[] buttonList = new Button[1];
		InitDashboardClickHandler initDashboardHandler = new InitDashboardClickHandler(baseView);
		DASHBOARD_BUTTON = new Button("Dashboard");
		DASHBOARD_BUTTON.addClickHandler(initDashboardHandler);
		buttonList[0] = getDashboardNavButton();
		return buttonList;
	}
	
	public static Button[] getNavigationSmartphone(BaseViewHandler baseView){
		Button[] buttonList = new Button[2];
		InitDashboardClickHandler initDashboardHandler = new InitDashboardClickHandler(baseView);
		DASHBOARD_BUTTON = new Button("Dashboard");
		DASHBOARD_BUTTON.addClickHandler(initDashboardHandler);
		buttonList[0] = getDashboardNavButton();
		buttonList[1] = getSmartphoneButton();
		
		return buttonList;
	}
	
	public static Button[] getNavigationRuleList(BaseViewHandler baseView, ArrayList<RuleModel> rules){
		Button[] buttonList = new Button[3];
		buttonList[0] = getDashboardNavButton();
		buttonList[1] = getSmartphoneButton();
		buttonList[2] = getRuleListNavButton(baseView, rules);
		return buttonList;
	}
	
	public static Button[] getNavigationAddNewRule(BaseViewHandler baseView){
		Button[] buttonList = new Button[3];
		InitDashboardClickHandler initDashboardHandler = new InitDashboardClickHandler(baseView);
		DASHBOARD_BUTTON = new Button("Dashboard");
		DASHBOARD_BUTTON.addClickHandler(initDashboardHandler);
		buttonList[0] = getDashboardNavButton();
		buttonList[1] = getSmartphoneButton();
		buttonList[2] = getAddNewRule();
		
		return buttonList;
	}
		
	public static Button[] getNavigationAddAdmin(BaseViewHandler baseView){
		Button[] buttonList = new Button[2];
		InitDashboardClickHandler initDashboardHandler = new InitDashboardClickHandler(baseView);
		DASHBOARD_BUTTON = new Button("Dashboard");
		DASHBOARD_BUTTON.addClickHandler(initDashboardHandler);
		buttonList[0] = getDashboardNavButton();
		buttonList[1] = getAdmin();
		
		return buttonList;
	}
	
	public static Button[] getNavigationHelpdesk(BaseViewHandler baseView){
		Button[] buttonList = new Button[2];
		InitDashboardClickHandler initDashboardHandler = new InitDashboardClickHandler(baseView);
		DASHBOARD_BUTTON = new Button("Dashboard");
		DASHBOARD_BUTTON.addClickHandler(initDashboardHandler);
		buttonList[0] = getDashboardNavButton();
		buttonList[1] = getHelpDesk();
		
		return buttonList;
	}	
	
}
