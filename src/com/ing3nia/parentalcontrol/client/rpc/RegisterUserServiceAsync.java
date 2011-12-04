package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.UserModel;

public interface RegisterUserServiceAsync {
	public void registerUser(UserModel user, AsyncCallback<Boolean> async);
}