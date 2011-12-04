package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.UserModel;

@RemoteServiceRelativePath("reg-usr")
public interface RegisterUserService extends RemoteService {
	public Boolean registerUser(UserModel user) throws IllegalArgumentException;
}

