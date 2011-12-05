package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.views.LoadingView;

public class CheckAdminUserCallbackHandler implements AsyncCallback<Boolean> {
	
	PCLoginUIBinder pclogin;
	Boolean isAdmin;
	
	public CheckAdminUserCallbackHandler(PCLoginUIBinder pclogin, Boolean isAdmin) {
		this.pclogin = pclogin;
		this.isAdmin = isAdmin;
	}
	
	public void onFailure(Throwable error) {
		LoadingView.clearLoadingView(pclogin);
		pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
		this.isAdmin = false;
	}

	public void onSuccess(Boolean result) {
		if (result) {
			this.isAdmin = true;
		} 
		else {
			LoadingView.clearLoadingView(pclogin);
			pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
			this.isAdmin = false;
		}
	}
}
