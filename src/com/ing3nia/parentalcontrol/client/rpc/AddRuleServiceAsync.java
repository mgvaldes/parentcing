package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.RuleModel;

public interface AddRuleServiceAsync {
	public void addRule(String cid, String smartphoneKey, RuleModel newRule, AsyncCallback<String> async);
}
