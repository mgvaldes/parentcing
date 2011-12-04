package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.PCRegisterUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.click.LoginRegisterButtonClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.SignInButtonClickHandler;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.utils.CookieHandler;
import com.ing3nia.parentalcontrol.client.views.LoadingView;
import com.google.gwt.user.client.ui.Image;

public class RegisterUserCallbackHandler implements AsyncCallback<Boolean> {

    UserModel userModel;
    Boolean serviceOk;
    PCRegisterUIBinder pcregister;
    Image loadingImage;

	public RegisterUserCallbackHandler(UserModel userModel, PCRegisterUIBinder pcregister, Image loadingImage){
		this.userModel = userModel;
		this.serviceOk = false;
		this.pcregister = pcregister;
		this.loadingImage = loadingImage;
	}
	
	@Override
	public void onFailure(Throwable error) {
		LoadingView.clearLoadingView(pcregister);
		pcregister.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
		this.serviceOk = false;
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {
			RootPanel.get().clear();
			
			PCLoginUIBinder loginUI = new PCLoginUIBinder();
			RootPanel.get().add(loginUI);
			ClientUserModel userModel = new ClientUserModel();
			SignInButtonClickHandler signInHandler =  new SignInButtonClickHandler(loginUI, userModel, loadingImage);
			loginUI.getSignInButton().addClickHandler(signInHandler);
			
			LoginRegisterButtonClickHandler registerHandler = new LoginRegisterButtonClickHandler(loadingImage);
			loginUI.getRegisterButton().addClickHandler(registerHandler);
			
			//If remembered credentials 
			String remUser = CookieHandler.getMailRemeberedCredential();
			String remPass = CookieHandler.getPasswordRemeberedCredential();
			
			if (remUser !=null && remPass !=null) {
				
				TextBox emailfield = loginUI.getEmailField();
				PasswordTextBox passfield = loginUI.getPassField();
				
				emailfield.setText(remUser);
				emailfield.setSelectionRange(0, emailfield.getText().length());
				passfield.setText(remPass);
				passfield.setSelectionRange(0, passfield.getText().length());
			}
		}
		else {
			LoadingView.clearLoadingView(pcregister);
			pcregister.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
		}
		
		this.serviceOk = true;
	}
}
