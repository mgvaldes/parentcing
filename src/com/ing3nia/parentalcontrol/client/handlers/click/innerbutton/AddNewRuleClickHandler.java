package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.ing3nia.parentalcontrol.client.views.RuleListView;

public class AddNewRuleClickHandler implements ClickHandler {

	RuleListView ruleListView;
	
	public AddNewRuleClickHandler(RuleListView ruleListView){
		this.ruleListView = ruleListView;
	}
	
	public void onClick(ClickEvent event) {
		// loadNewRule();
		
		ruleListView.loadAddRuleView();
	}
}
