package com.ing3nia.parentalcontrol.client.views;

import org.apache.http.impl.conn.tsccm.WaitingThread;

import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.LoginService;
import com.ing3nia.parentalcontrol.client.rpc.LoginServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyService;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyServiceAsync;
import com.ing3nia.parentalcontrol.client.views.async.SignInUserCallbackHandler;

public class LoginView {
	
	private static final Logger logger = Logger
	.getLogger(SignInUserCallbackHandler.class.getName());
	
	/**
	 * Center Panel containing all the widgets of the 
	 * login view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of login view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent;
	
	/**
	 * Username label.
	 */
	private Label usernameLabel;
	
	/**
	 * Username text box.
	 */
	private TextBox usernameTextBox;
	
	/**
	 * Password label.
	 */
	private Label passwordLabel;
	
	/**
	 * Password text box.
	 */
	private PasswordTextBox passwordTextBox;

	/**
	 * Remember me check box.
	 */
	private CheckBox rememberMeCheckBox;
	
	/**
	 * Sign in button.
	 */
	private Button signInButton;
	
	private ClientUserModel userModel;
	
	public LoginView(HTMLPanel centerContent, ClientUserModel userModel) {
		//logger.addHandler(new ConsoleHandler());
		
		this.centerContent = centerContent;
		this.centerContent.setStyleName("centerContent");
		this.userModel = userModel;
		
		viewContent = new HTMLPanel("");
		usernameLabel = new Label("Username:");
		usernameTextBox = new TextBox();
		passwordLabel = new Label("Password:");
		passwordTextBox = new PasswordTextBox();

		rememberMeCheckBox = new CheckBox("Remember me");
		signInButton = new Button("Sign In");
		
		this.centerContent.clear();
	}
	
	public void initLoginView() {
		viewContent.add(usernameLabel);
		viewContent.add(usernameTextBox);
		viewContent.add(passwordLabel);
		viewContent.add(passwordTextBox);
		viewContent.add(rememberMeCheckBox);
	
		viewContent.add(signInButton);
		centerContent.add(viewContent);
	}
/*
	public static boolean validateUserName(String username, String password,
			ClientUserModel userModel, PCLoginUIBinder loginBinder) {

		boolean isValid = callLoginService(userModel, username, password, loginBinder);
		return isValid;
		/*
		 * if(username.equals("testuser") && password.equals("pass123")){ return
		 * true; }else{ return false; }
		 */
	//}
/*
	public static boolean callLoginService(ClientUserModel userModel,
			String username, String password, PCLoginUIBinder loginBinder) {
		final String usr = username;
		final String pass = password;
		// final ClientUserModel auxUserModel = new ClientUserModel();
		String auxKey;
		final ArrayList<SmartphoneModel> smartphoneAuxList;

		UserKeyServiceAsync userKeyService = GWT.create(UserKeyService.class);

		ClientUserModel auxKeyObject = new ClientUserModel();
		//SignInUserCallbackHandler callback = new SignInUserCallbackHandler(userModel, pclogin);
		//userKeyService.getUserKey(usr, pass, callback);
		logger.info("NUEVO VALOR DE USERMODEL "+userModel.getKey());
		
		//if (loginBinder.getAsync().getText().equals("")) {
		//	return false;
		//}
		if(true){loginBinder.getAsync().setText("THE KEY IS: "+userModel.getKey());return false;}

		LoginServiceAsync loginService = GWT.create(LoginService.class);
		LoginAsyncCallbackHandler callback2 = new LoginAsyncCallbackHandler();
		loginService.login(username, password, callback2);

		//userModel.setKey(callback.getAuxKey());
		userModel.setUsername(username);
		userModel.setPassword(password);
		userModel.setSmartphones(callback2.getSmartphoneList());

		return true;
	}
	*/
	public boolean setUserKey(ClientUserModel userModel, String key){
		if(key==null) return false;
		userModel.setKey(key);
		return true;
	}

}
