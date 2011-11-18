package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.services.models.SmartphoneModel;

public interface LoginServiceAsync {
	public void login(String username, String password, AsyncCallback<ArrayList<SmartphoneModel>> async);
}
