package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.RuleListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class SaveSmartphoneModificationsCallbackHandler implements AsyncCallback<Boolean> {
	private BaseViewHandler baseView;
	private RuleModel newRule;
	
	public SaveSmartphoneModificationsCallbackHandler(BaseViewHandler baseView, RuleModel newRule) {
		this.baseView = baseView;
		this.newRule = newRule;
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The new rule couldn't be saved.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {		
			SmartphoneModel smartphone = baseView.getUser().getSmartphones().get(baseView.getClickedSmartphoneIndex());
			ArrayList<RuleModel> rules = smartphone.getRules();
			rules.add(newRule);								
			smartphone.setRules(rules);
			
			baseView.getBaseBinder().getCenterContent().clear();
			
			Button ruleList = baseView.getMenuSetter().getAlertRules();
			ruleList.setStyleName("selectedShinnyButton");
			BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.AlertRules, baseView.getMenuSetter().getCenterMenuOptions());

			RuleListView ruleListView = new RuleListView(baseView, rules);
			ruleListView.initRuleListView();
		}
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The new rule couldn't be saved.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
