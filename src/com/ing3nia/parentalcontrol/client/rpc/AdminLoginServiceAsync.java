package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminLoginServiceAsync {
	public void adminLogin(String username, String password, AsyncCallback<String> async);
}
