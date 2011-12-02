package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.cell.client.FieldUpdater;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.views.subviews.EditRuleView;

public class EditRuleHandler implements FieldUpdater<RuleModel, String>{

	BaseViewHandler baseView;
	RuleModel ruleModel;
	
	public EditRuleHandler(BaseViewHandler baseView, RuleModel ruleModel){
		this.baseView = baseView;
		this.ruleModel = ruleModel;
	}
	
	
	@Override
	public void update(int index, RuleModel rule, String value) {
		baseView.getBaseBinder().getCenterContent().clear();
		
		SmartphoneModel smart = this.baseView.getUser().getSmartphones().get(this.baseView.getClickedSmartphoneIndex()); 
		
		EditRuleView editView = new EditRuleView(this.baseView, this.baseView.getUser().getCid(), smart, this.ruleModel);
		editView.initEditRuleView();
	}
}
