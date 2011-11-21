package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	public ArrayList<SmartphoneModel> login(String username, String password) throws IllegalArgumentException;
}