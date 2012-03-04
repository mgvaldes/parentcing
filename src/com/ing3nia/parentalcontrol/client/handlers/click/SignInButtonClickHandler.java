package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.ParentalControl;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.CheckAdminUserService;
import com.ing3nia.parentalcontrol.client.rpc.CheckAdminUserServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.LoginServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyService;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.CookieHandler;
import com.ing3nia.parentalcontrol.client.utils.LoadingBarImageEnum;
import com.ing3nia.parentalcontrol.client.views.LoadingView;
import com.ing3nia.parentalcontrol.client.views.LoginView;
import com.ing3nia.parentalcontrol.client.views.async.AsyncronousCallsMessages;
import com.ing3nia.parentalcontrol.client.views.async.CheckAdminUserCallbackHandler;
import com.ing3nia.parentalcontrol.client.views.async.LoginAsyncCallbackHandler;
import com.ing3nia.parentalcontrol.client.views.async.SignInUserCallbackHandler;

public class SignInButtonClickHandler implements ClickHandler {

	PCLoginUIBinder pclogin;
	ClientUserModel userModel;
	Image loadingImage;

	public SignInButtonClickHandler(PCLoginUIBinder pclogin,
			ClientUserModel userModel, Image loadinImage) {
		this.pclogin = pclogin;
		this.userModel = userModel;
		this.loadingImage = loadinImage;
	}

	@Override
	public void onClick(ClickEvent event) {
		String email = pclogin.getEmailField().getText();
		String pass = pclogin.getPassField().getText();
		pclogin.getNotice().setText("");
		
		userModel.setUsername(email);
		userModel.setPassword(pass);
		
		LoadingView.clearLoadingView(pclogin);
		//LoadingView.setLoadingView(pclogin, AsyncronousCallsMessages.LOADING_LOGIN, loadingImage);
		LoadingView.setLoadingView(pclogin, AsyncronousCallsMessages.LOADING_LOGIN, LoadingBarImageEnum.STAGE1);
		
		CheckAdminUserCallbackHandler checkAdminUserCallback = new CheckAdminUserCallbackHandler(pclogin, userModel);
		CheckAdminUserServiceAsync checkAdminUserService = GWT.create(CheckAdminUserService.class);
		checkAdminUserService.checkAdminUser(email, pass, checkAdminUserCallback);
		
		// initializing getUserKey that will obtain users key and data and
		// then initialize the base view
//		userModel.setUsername(email);
//		userModel.setPassword(pass);
//		
//		SignInUserCallbackHandler getSignInCallback = new SignInUserCallbackHandler(userModel, pclogin);
//		UserKeyServiceAsync userKeyService = GWT.create(UserKeyService.class);
//		userKeyService.getUserKey(email, pass, getSignInCallback);			 
	}
}
