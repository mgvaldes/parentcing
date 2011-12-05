package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.ing3nia.parentalcontrol.client.views.subviews.NewRuleView;

public class SaveRuleClickHandler implements ClickHandler {

	NewRuleView newRuleView;
	public SaveRuleClickHandler(NewRuleView newRuleView){
		this.newRuleView = newRuleView;
	}
	
	public void onClick(ClickEvent event) {
		this.newRuleView.saveRule();
	}

}
