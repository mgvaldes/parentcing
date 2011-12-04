package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("del-rule")
public interface DeleteRuleService extends RemoteService {
	public Boolean deleteRule(String ruleKey, String smartKey) throws IllegalArgumentException;
}
