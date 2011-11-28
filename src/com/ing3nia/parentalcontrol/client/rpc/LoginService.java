package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	//public ArrayList<SmartphoneModel> login(String username, String password) throws IllegalArgumentException;
	public ClientUserModel login(String username, String password) throws IllegalArgumentException;
}