package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;

public class DeleteRuleCallbackHandler implements AsyncCallback<Boolean> {
	
	private BaseViewHandler baseView;
	private RuleModel rule;
	private String cid;
	private String smartKey;
	
	public DeleteRuleCallbackHandler(BaseViewHandler baseView, RuleModel rule, String cid, String smartKey) {
		this.baseView = baseView;
		this.rule = rule;
		this.cid = cid;
		this.smartKey = smartKey;
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The rule couldn't be deleted.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {
			ModificationModel auxMod = new ModificationModel();
			ArrayList<String> modRules = new ArrayList<String>();
			modRules.add(this.rule.getKeyId());
			
			auxMod.setDeletedRules(modRules);
			
			SaveSmartphoneModificationsCallbackHandler saveModCallback = new SaveSmartphoneModificationsCallbackHandler(baseView, this.rule, 5);
			SaveSmartphoneModificationsServiceAsync saveModService = GWT.create(SaveSmartphoneModificationsService.class);
			saveModService.saveSmartphoneModifications(this.cid, this.smartKey, auxMod, saveModCallback);
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The rule couldn't be deleted.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
