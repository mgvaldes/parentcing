package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;

public class AddRuleCallbackHandler implements AsyncCallback<String> {
	
	private BaseViewHandler baseView;
	private RuleModel newRule;
	private String cid;
	private String smartKey;
	
	public AddRuleCallbackHandler(BaseViewHandler baseView, RuleModel newRule, String cid, String smartKey){
		this.baseView = baseView;
		this.newRule = newRule;
		this.cid = cid;
		this.smartKey = smartKey;
	}
	
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The new rule couldn't be created.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	public void onSuccess(String result) {
		//if (result != null) {
			//newRule.setKeyId(result); //NOT ANYMORE
			
			ModificationModel auxMod = new ModificationModel();
			ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
			rules.add(newRule);
			
			auxMod.setRules(rules);
			
			SaveSmartphoneModificationsCallbackHandler saveModCallback = new SaveSmartphoneModificationsCallbackHandler(baseView, newRule, 0);
			SaveSmartphoneModificationsServiceAsync saveModService = GWT.create(SaveSmartphoneModificationsService.class);
			saveModService.saveSmartphoneModifications(this.cid, this.smartKey, auxMod, saveModCallback);
		//} 
		/*else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The new rule couldn't be created.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}*/
	}
}
