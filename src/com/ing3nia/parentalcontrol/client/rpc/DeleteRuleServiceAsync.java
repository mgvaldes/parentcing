package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeleteRuleServiceAsync {
	public void deleteRule(String ruleKey, String smartKey, AsyncCallback<Boolean> async);
}