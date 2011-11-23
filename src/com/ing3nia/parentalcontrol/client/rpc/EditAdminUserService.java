package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("edit-admin-usr")
public interface EditAdminUserService extends RemoteService {
	public Boolean editAdminUser(String username, String password, String userKey) throws IllegalArgumentException;
}
