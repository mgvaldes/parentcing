package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;

public interface GetSmartphoneDetailsServiceAsync {
	
	public void getDetails(String cid, SmartphoneModel smartphoneModel, AsyncCallback<SmartphoneModel> async) throws IllegalArgumentException;
}
