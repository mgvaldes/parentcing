package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AddAdminUserServiceAsync {
	public void addAdminUser(String username, String password, String loggedUserKey, AsyncCallback<String> async);
}
