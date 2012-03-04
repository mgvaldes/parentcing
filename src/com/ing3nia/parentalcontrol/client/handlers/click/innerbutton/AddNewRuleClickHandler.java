package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.views.RuleListView;

public class AddNewRuleClickHandler implements ClickHandler {

	RuleListView ruleListView;
	BaseViewHandler baseView;
	
	public AddNewRuleClickHandler(RuleListView ruleListView, BaseViewHandler baseView){
		this.ruleListView = ruleListView;
		this.baseView = baseView;
	}
	
	public void onClick(ClickEvent event) {
		// loadNewRule();
		
		NavigationHandler navHandler = new NavigationHandler(baseView);
		navHandler.setAddNewRuleNavigation(baseView.getBaseBinder().getNavigationPanel());
		
		ruleListView.loadAddRuleView();
	}
}
