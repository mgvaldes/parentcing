package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("admin-login")
public interface AdminLoginService extends RemoteService {
	public String adminLogin(String username, String password) throws IllegalArgumentException;
}
