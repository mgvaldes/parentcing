package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.ing3nia.parentalcontrol.client.PCRegisterUIBinder;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.rpc.RegisterUserService;
import com.ing3nia.parentalcontrol.client.rpc.RegisterUserServiceAsync;
import com.ing3nia.parentalcontrol.client.views.LoadingView;
import com.ing3nia.parentalcontrol.client.views.async.AsyncronousCallsMessages;
import com.ing3nia.parentalcontrol.client.views.async.RegisterUserCallbackHandler;

public class RegisterButtonClickHandler implements ClickHandler {

	PCRegisterUIBinder pcregister;
	UserModel userModel;
	Image loadingImage;

	public RegisterButtonClickHandler(PCRegisterUIBinder pcregister, Image loadinImage) {
		this.pcregister = pcregister;
		this.loadingImage = loadinImage;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		userModel = new UserModel();
		
		String email = pcregister.getEmailField().getText();
		String pass = pcregister.getPassField().getText();
		String confirmPass = pcregister.getConfirmPassField().getText();
		String name = pcregister.getNameField().getText();
		
		pcregister.getNotice().setText("");
		
		if (!pass.equals(confirmPass)) {
			pcregister.getNotice().setText(AsyncronousCallsMessages.EQUAL_PASS_ERROR);
		}
		else {
			LoadingView.clearLoadingView(pcregister);
			LoadingView.setLoadingView(pcregister, AsyncronousCallsMessages.LOADING_REGISTER, loadingImage);
			
			userModel.setUsr(email);
			userModel.setPass(pass);
			userModel.setName(name);
			userModel.setEmail(email);
			
			RegisterUserCallbackHandler registerCallback = new RegisterUserCallbackHandler(userModel, pcregister, loadingImage);
			RegisterUserServiceAsync registerUserService = GWT.create(RegisterUserService.class);
			registerUserService.registerUser(userModel, registerCallback);
		}
	}
}
