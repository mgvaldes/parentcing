package com.ing3nia.parentalcontrol.client.views;

import org.apache.http.impl.conn.tsccm.WaitingThread;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;

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
	private PasswordTextBox passwordTextBox;
	
	/**
	 * Remember me check box.
	 */
	private CheckBox rememberMeCheckBox;
	
	/**
	 * Sign in button.
	 */
	private Button signInButton;
	
	public LoginView(HTMLPanel centerContent) {
		this.centerContent = centerContent;
		
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

	
	public static boolean validateUserName(String username, String password, ClientUserModel userModel){
		
		if(!username.equals("testuser") && !password.equals("pass123")){
			return false;
		}else{
			return true;
		}
	}

}
