package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.ParentalControl;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.utils.CookieHandler;
import com.ing3nia.parentalcontrol.client.views.LoadingView;


public class LoginAsyncCallbackHandler implements AsyncCallback<ClientUserModel> {

    ClientUserModel userModel;
    Boolean serviceOk;
    PCLoginUIBinder pclogin;

    public LoginAsyncCallbackHandler(PCLoginUIBinder pclogin, ClientUserModel userModel){
    	this.pclogin = pclogin;
    	this.userModel = userModel;
    	this.serviceOk = null;
    }

	@Override
	public void onFailure(Throwable error) {
		LoadingView.clearLoadingView(pclogin);
		pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
		this.serviceOk = false;
	}

	@Override
	public void onSuccess(ClientUserModel user) {
		if (user != null) {
			LoadingView.clearLoadingView(pclogin);
			this.userModel.setCid(user.getCid());
			this.userModel.setSmartphones(user.getSmartphones());
			this.userModel.setAdmins(user.getAdmins());
			
			CookieHandler.setPCCookie(user.getCid());
			if (pclogin.getRememberMeBox().getValue()) {
				CookieHandler.setCredentialsRemember(pclogin.getEmailField()
						.getText(), pclogin.getPassField().getText());
			}else{
				CookieHandler.clearCredentialsRemember();
			}

			RootPanel.get().clear();
			ParentalControl.loadPCAdmin(userModel);
			
			this.serviceOk = true;
		} else {
			LoadingView.clearLoadingView(pclogin);
			pclogin.getNotice().setText("An unexpected error ocurred. Try again later");
			this.serviceOk = false;
		}
	}
}