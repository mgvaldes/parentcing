package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.LoginService;
import com.ing3nia.parentalcontrol.client.rpc.LoginServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;
import com.ing3nia.parentalcontrol.services.models.SmartphoneModel;

public class LoginView {
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
	private TextBox passwordTextBox;
	
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
		this.centerContent = centerContent;
		this.userModel = userModel;
		
		viewContent = new HTMLPanel("");
		usernameLabel = new Label("Username:");
		usernameTextBox = new TextBox();
		passwordLabel = new Label("Password:");
		passwordTextBox = new TextBox();
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
		
		signInButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				callLoginService();
			}
		});
		
		viewContent.add(signInButton);
		
		centerContent.add(viewContent);
	}
	
	public void callLoginService() {
		final String usr = this.usernameTextBox.getText();
		final String pass = this.passwordTextBox.getText();
		final ArrayList<SmartphoneModel> smartphoneResults;
		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(usr, pass, 
				new AsyncCallback<ArrayList<SmartphoneModel>>() {
					public void onFailure(Throwable error) {
					}
		
					public void onSuccess(ArrayList<SmartphoneModel> smartphones) {
						if (smartphones != null) {
							smartphoneResults = smartphones;
						}
						else {
							smartphoneResults = null;
							Window.alert("An error occured. The smartphones could not be loaded.");
						}
					}
				}
		);
	}
}
