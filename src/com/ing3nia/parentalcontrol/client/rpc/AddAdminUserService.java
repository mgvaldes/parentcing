package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("add-admin-usr")
public interface AddAdminUserService extends RemoteService {
	public String addAdminUser(String username, String password, String loggedUserKey) throws IllegalArgumentException;
}