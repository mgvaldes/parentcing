package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.AdminLoginService;
import com.ing3nia.parentalcontrol.client.rpc.AdminLoginServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyService;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyServiceAsync;
import com.ing3nia.parentalcontrol.client.views.LoadingView;

public class CheckAdminUserCallbackHandler implements AsyncCallback<Boolean> {
	
	PCLoginUIBinder pclogin;
	ClientUserModel userModel;
	Image loadingImage;
	
	public CheckAdminUserCallbackHandler(PCLoginUIBinder pclogin, ClientUserModel userModel, Image loadingImage) {
		this.pclogin = pclogin;
		this.userModel = userModel;
		this.loadingImage = loadingImage;
	}
	
	public void onFailure(Throwable error) {
		LoadingView.clearLoadingView(pclogin);
		pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
	}

	public void onSuccess(Boolean result) {		
		LoadingView.clearLoadingView(pclogin);
		LoadingView.setLoadingView(pclogin, AsyncronousCallsMessages.LOADING_LOGIN, loadingImage);
		
		if (result) {
			ClientAdminUserModel adminModel = new ClientAdminUserModel();
			adminModel.setUsername(userModel.getUsername());
			adminModel.setPassword(userModel.getPassword());
			
			AdminLoginCallbackHandler adminLoginCallback = new AdminLoginCallbackHandler(pclogin, adminModel, loadingImage);
			AdminLoginServiceAsync adminLoginService = GWT.create(AdminLoginService.class);
			adminLoginService.adminLogin(adminModel.getUsername(), adminModel.getPassword(), adminLoginCallback);
		} 
		else {
			SignInUserCallbackHandler getSignInCallback = new SignInUserCallbackHandler(userModel, pclogin);
			UserKeyServiceAsync userKeyService = GWT.create(UserKeyService.class);
			userKeyService.getUserKey(userModel.getUsername(), userModel.getPassword(), getSignInCallback);
		}
	}
}
