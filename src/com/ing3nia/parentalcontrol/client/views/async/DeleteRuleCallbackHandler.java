package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.RuleListView;
import com.ing3nia.parentalcontrol.client.views.TicketListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class DeleteRuleCallbackHandler implements AsyncCallback<Boolean> {
	
	private BaseViewHandler baseView;
	private RuleModel rule;
	
	public DeleteRuleCallbackHandler(BaseViewHandler baseView, RuleModel rule) {
		this.baseView = baseView;
		this.rule = rule;
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The rule couldn't be deleted.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {
			SmartphoneModel smartphone = baseView.getUser().getSmartphones().get(this.baseView.getClickedSmartphoneIndex());
			ArrayList<RuleModel> rules = smartphone.getRules();
			rules.remove(this.rule);
			
			baseView.getBaseBinder().getCenterContent().clear();
			
			Button ruleList = baseView.getMenuSetter().getAlertRules();
			ruleList.setStyleName("selectedShinnyButton");
			BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.AlertRules, baseView.getMenuSetter().getCenterMenuOptions());

			RuleListView ruleListView = new RuleListView(baseView, rules);
			ruleListView.initRuleListView();
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The ticket couldn't be closed.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
