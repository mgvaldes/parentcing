package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserKeyServiceAsync {
	public void getUserKey(String username, String password, AsyncCallback<String> async);
}
