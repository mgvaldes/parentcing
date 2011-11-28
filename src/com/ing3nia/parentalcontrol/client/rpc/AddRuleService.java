package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.RuleModel;

@RemoteServiceRelativePath("add-rule")
public interface AddRuleService extends RemoteService {
	public String addRule(String cid, String smartphoneKey, RuleModel newRule) throws IllegalArgumentException;
}
