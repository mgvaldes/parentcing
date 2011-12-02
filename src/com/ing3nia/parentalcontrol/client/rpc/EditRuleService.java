package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.RuleModel;

@RemoteServiceRelativePath("edit-rule")
public interface EditRuleService extends RemoteService {
	public Boolean editRule(RuleModel editedRule) throws IllegalArgumentException;
}
