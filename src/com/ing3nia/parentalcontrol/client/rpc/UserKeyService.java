package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("usr-key")
public interface UserKeyService extends RemoteService {
	public String getUserKey(String username, String password) throws IllegalArgumentException;
}
