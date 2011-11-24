package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("register")
public interface UserRegisterService extends RemoteService {
	public Boolean register(String username, String password, String email, String name) throws IllegalArgumentException;
}
