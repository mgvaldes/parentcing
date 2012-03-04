package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;

public interface GetUserSessionCredentialsServiceAsync {

	void getSessionCredentials(String cookieId,
			AsyncCallback<ClientUserModel> callback);

}
