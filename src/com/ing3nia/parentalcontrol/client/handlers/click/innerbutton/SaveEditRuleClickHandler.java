package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.ing3nia.parentalcontrol.client.views.subviews.EditRuleView;

public class SaveEditRuleClickHandler implements ClickHandler {

	EditRuleView editRuleView;

	public SaveEditRuleClickHandler(EditRuleView editRuleView) {
		this.editRuleView = editRuleView;
	}

	@Override
	public void onClick(ClickEvent event) {
		this.editRuleView.saveRule();
	}
}
