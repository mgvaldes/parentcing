package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.LoginService;
import com.ing3nia.parentalcontrol.client.rpc.LoginServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyService;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.LoadingBarImageEnum;
import com.ing3nia.parentalcontrol.client.views.LoadingView;

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
			//LoadingView.changeLoadingMessage(pclogin, AsyncronousCallsMessages.REQUESTING_DATA);		
			LoadingView.setLoadingView(pclogin, AsyncronousCallsMessages.REQUESTING_SMP_DATA, LoadingBarImageEnum.STAGE3);
			
			LoginAsyncCallbackHandler loginAsyncCallback = new LoginAsyncCallbackHandler(pclogin, userModel);
			LoginServiceAsync loginService = GWT.create(LoginService.class);
			userModel.setKey(userKey);
			loginService.login(userModel.getUsername(), userModel.getPassword(), loginAsyncCallback);
		}else{
			LoadingView.clearLoadingView(pclogin);
			pclogin.getNotice().setText(AsyncronousCallsMessages.INVALID_CREDENTIALS);
		}
		this.serviceOk = true;
	}
	
}