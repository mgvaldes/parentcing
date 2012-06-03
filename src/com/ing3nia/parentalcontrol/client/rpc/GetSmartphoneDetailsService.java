package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;

@RemoteServiceRelativePath("get-details")
public interface GetSmartphoneDetailsService extends RemoteService {

	public SmartphoneModel getDetails(String cid, SmartphoneModel smartphoneModel) throws IllegalArgumentException;
}
