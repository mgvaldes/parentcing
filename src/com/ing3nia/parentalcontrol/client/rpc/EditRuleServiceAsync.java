package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.RuleModel;

public interface EditRuleServiceAsync {
	public void editRule(RuleModel editedRule, AsyncCallback<Boolean> async);
}