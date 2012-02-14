package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;


@RemoteServiceRelativePath("get-user-session-credentials")
public interface GetUserSessionCredentialsService extends RemoteService  {
	public ClientUserModel getSessionCredentials(String cookieId) throws IllegalArgumentException;
}
