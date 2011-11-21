package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EditAdminUserServiceAsync {
	public void editAdminUser(String username, String password, String userKey, AsyncCallback<Boolean> async);
}
