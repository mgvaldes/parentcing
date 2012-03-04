package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.ParentalControl;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.utils.CookieHandler;
import com.ing3nia.parentalcontrol.client.utils.PCURLMapper;

public class GetUserSessionCredentialsCallBackHandler implements AsyncCallback<ClientUserModel> {

	private ClientUserModel usermodel;
	
	public GetUserSessionCredentialsCallBackHandler(ClientUserModel usermodel){
		this.usermodel = usermodel;
	}
	
	@Override
	public void onFailure(Throwable error) {
		//clear cookie and redirect to home page
		CookieHandler.clearSessionCookie();
		Window.Location.replace(PCURLMapper.CURRENT_BASE_URL);
	}

	@Override
	public void onSuccess(ClientUserModel result) {	
		if(result==null){
			CookieHandler.clearSessionCookie();
			Window.Location.replace(PCURLMapper.CURRENT_BASE_URL);
			return;
		}
		this.usermodel.setKey(result.getKey());
		this.usermodel.setCid(result.getCid());
		this.usermodel.setUsername(result.getUsername());
		this.usermodel.setPassword(result.getPassword());
		
		ParentalControl.initLogin(this.usermodel);
	}

}
