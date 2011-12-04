package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;

public class EditRuleCallbackHandler implements AsyncCallback<Boolean> {
	private BaseViewHandler baseView;
	private RuleModel newRule;
	private String cid;
	private String smartKey;
	private RuleModel oldRule;
	
	public EditRuleCallbackHandler(BaseViewHandler baseView, RuleModel oldRule, RuleModel newRule, String cid, String smartKey){
		this.baseView = baseView;
		this.newRule = newRule;
		this.cid = cid;
		this.smartKey = smartKey;
		this.oldRule = oldRule;
	}
	
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The new rule couldn't be updated.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	public void onSuccess(Boolean result) {
		if (result) {
			this.oldRule.setDisabledFunctionalities(this.newRule.getDisabledFunctionalities());
			this.oldRule.setEndDate(this.newRule.getEndDate());
			this.oldRule.setName(this.newRule.getName());
			this.oldRule.setStartDate(this.newRule.getStartDate());
			this.oldRule.setType(this.newRule.getType());
			
			ModificationModel auxMod = new ModificationModel();
			ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
			rules.add(newRule);
			
			auxMod.setRules(rules);
			
			SaveSmartphoneModificationsCallbackHandler saveModCallback = new SaveSmartphoneModificationsCallbackHandler(baseView, newRule, 0);
			SaveSmartphoneModificationsServiceAsync saveModService = GWT.create(SaveSmartphoneModificationsService.class);
			saveModService.saveSmartphoneModifications(this.cid, this.smartKey, auxMod, saveModCallback);
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The new rule couldn't be updated.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
