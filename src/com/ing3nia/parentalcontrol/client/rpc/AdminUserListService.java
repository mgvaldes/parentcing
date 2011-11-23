package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;

@RemoteServiceRelativePath("admin-usr-list")
public interface AdminUserListService extends RemoteService {
	public ArrayList<ClientUserModel> getAdminUserList(String userKey) throws IllegalArgumentException;
}
