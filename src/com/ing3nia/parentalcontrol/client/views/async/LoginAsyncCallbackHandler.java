package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.ParentalControl;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.utils.CookieHandler;
import com.ing3nia.parentalcontrol.client.views.LoadingView;


public class LoginAsyncCallbackHandler implements AsyncCallback<ArrayList<SmartphoneModel>> {

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
	public void onSuccess(ArrayList<SmartphoneModel> smartphones) {
		if (smartphones != null) {
			LoadingView.clearLoadingView(pclogin);
			this.userModel.setSmartphones(smartphones);
			
			CookieHandler.setPCCookie("us3rp4r3nt4lc00k13");
			if (pclogin.getRememberMeBox().isEnabled()) {
				CookieHandler.setCredentialsRemember(pclogin.getEmailField()
						.getText(), pclogin.getPassField().getText());
			}

			RootPanel.get().clear();
			ParentalControl.loadPCAdmin(userModel);
			
			this.serviceOk = true;
		} else {
			LoadingView.clearLoadingView(pclogin);
			pclogin.getNotice().setText("OK BUT smartphones NULL");
			this.serviceOk = false;
		}
	}
}