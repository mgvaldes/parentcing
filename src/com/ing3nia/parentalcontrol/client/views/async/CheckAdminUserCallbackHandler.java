package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.AdminLoginService;
import com.ing3nia.parentalcontrol.client.rpc.AdminLoginServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyService;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.LoadingBarImageEnum;
import com.ing3nia.parentalcontrol.client.views.LoadingView;

public class CheckAdminUserCallbackHandler implements AsyncCallback<Boolean> {
	
	PCLoginUIBinder pclogin;
	ClientUserModel userModel;
	
	public CheckAdminUserCallbackHandler(PCLoginUIBinder pclogin, ClientUserModel userModel) {
		this.pclogin = pclogin;
		this.userModel = userModel;
	}
	
	public void onFailure(Throwable error) {
		LoadingView.clearLoadingView(pclogin);
		pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
	}

	public void onSuccess(Boolean result) {		
		
		if (result) {
			ClientAdminUserModel adminModel = new ClientAdminUserModel();
			adminModel.setUsername(userModel.getUsername());
			adminModel.setPassword(userModel.getPassword());
			
			LoadingView.setLoadingView(pclogin, AsyncronousCallsMessages.REQUESTING_DATA, LoadingBarImageEnum.STAGE2);
			
			AdminLoginCallbackHandler adminLoginCallback = new AdminLoginCallbackHandler(pclogin, adminModel);
			AdminLoginServiceAsync adminLoginService = GWT.create(AdminLoginService.class);
			adminLoginService.adminLogin(adminModel.getUsername(), adminModel.getPassword(), adminLoginCallback);
		} 
		else {
			
			LoadingView.setLoadingView(pclogin, AsyncronousCallsMessages.REQUESTING_DATA, LoadingBarImageEnum.STAGE2);	
			
			SignInUserCallbackHandler getSignInCallback = new SignInUserCallbackHandler(userModel, pclogin);
			UserKeyServiceAsync userKeyService = GWT.create(UserKeyService.class);
			userKeyService.getUserKey(userModel.getUsername(), userModel.getPassword(), getSignInCallback);
		}
	}
}
