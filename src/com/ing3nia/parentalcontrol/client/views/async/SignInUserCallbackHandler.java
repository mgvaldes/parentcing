package com.ing3nia.parentalcontrol.client.views.async;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.LoginService;
import com.ing3nia.parentalcontrol.client.rpc.LoginServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyService;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyServiceAsync;
import com.ing3nia.parentalcontrol.client.views.LoadingView;
import com.ing3nia.parentalcontrol.services.parent.ParentTestResource;


public class SignInUserCallbackHandler implements AsyncCallback<String> {

    ClientUserModel userModel;
    Boolean serviceOk;
    PCLoginUIBinder pclogin;

	public SignInUserCallbackHandler(ClientUserModel userModel, PCLoginUIBinder pclogin){
		this.userModel = userModel;
		this.serviceOk = false;
		this.pclogin = pclogin;
	}
	
	@Override
	public void onFailure(Throwable error) {
		LoadingView.clearLoadingView(pclogin);
		pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
		this.serviceOk = false;
	}

	@Override
	public void onSuccess(String userKey) {
		if(userKey!=null){
			//pclogin.getNotice().setText("GOING OK "+userKey);
			LoadingView.changeLoadingMessage(pclogin, AsyncronousCallsMessages.REQUESTING_DATA);		
			LoginAsyncCallbackHandler loginAsyncCallback = new LoginAsyncCallbackHandler(pclogin, userModel);
			LoginServiceAsync loginService = GWT.create(LoginService.class);
			loginService.login(userModel.getUsername(), userModel.getPassword(), loginAsyncCallback);
		}else{
			LoadingView.clearLoadingView(pclogin);
			pclogin.getNotice().setText(AsyncronousCallsMessages.INVALID_CREDENTIALS);
		}
		this.serviceOk = true;
	}
	
}