package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;

public interface LoginServiceAsync {
	public void login(String username, String password, AsyncCallback<ClientUserModel> async);
}
