package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserRegisterServiceAsync {
	public void register(String username, String password, String email, String name, AsyncCallback<Boolean> async);
}
