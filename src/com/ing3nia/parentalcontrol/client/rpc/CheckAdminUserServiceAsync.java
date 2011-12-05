package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CheckAdminUserServiceAsync {
	public void checkAdminUser(String username, String password, AsyncCallback<Boolean> async);
}