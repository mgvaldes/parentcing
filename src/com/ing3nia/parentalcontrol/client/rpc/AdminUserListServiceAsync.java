package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;

public interface AdminUserListServiceAsync {
	public void getAdminUserList(String userKey, AsyncCallback<ArrayList<ClientAdminUserModel>> async);
}
