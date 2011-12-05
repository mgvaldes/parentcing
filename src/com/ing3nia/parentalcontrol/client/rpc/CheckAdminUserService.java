package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("check-admin")
public interface CheckAdminUserService extends RemoteService {
	public Boolean checkAdminUser(String username, String password) throws IllegalArgumentException;
}
