package com.ing3nia.parentalcontrol.client.views.subviews;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.views.async.AddAdminUserCallbackHandler;
import com.ing3nia.parentalcontrol.client.views.async.EditAdminUserCallbackHandler;
import com.ing3nia.parentalcontrol.client.views.classnames.NewAdminUserViewClassName;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.AddAdminUserService;
import com.ing3nia.parentalcontrol.client.rpc.AddAdminUserServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.EditAdminUserService;
import com.ing3nia.parentalcontrol.client.rpc.EditAdminUserServiceAsync;

public class EditAdminUserView {

	public static String VIEW_CONTENT_CLASSNAME = "editAdminUserContent";
	
	BaseViewHandler baseView;
	
	/**
	 * Center Panel containing all the widgets of the 
	 * new admin user view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of new admin user view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent = new HTMLPanel("");
	
	/**
	 * Main label of new admin user view.
	 */
	private Label editUserLabel = new Label("Edit User:");
	
	private FlowPanel usernamePanel = new FlowPanel();
	
	/**
	 * Username label.
	 */
	private Label usernameLabel = new Label("Username:");
	
	/**
	 * Username text box.
	 */
	private TextBox usernameTextBox = new TextBox();
	
	private FlowPanel passwordPanel = new FlowPanel();
	
	/**
	 * Password label.
	 */
	private Label passwordLabel = new Label("Password:");
	
	private FlowPanel confirmPasswordPanel = new FlowPanel();
	
	/**
	 * Repeat Password label.
	 */
	private Label confirmPasswordLabel = new Label("Confirm Password:");
	
	/**
	 * Password text box.
	 */
	private PasswordTextBox passwordTextBox = new PasswordTextBox();
	
	/**
	 * Confirgm Password text box.
	 */
	private PasswordTextBox confirmPasswordTextBox = new PasswordTextBox();
	
	/**
	 * Panel containing view buttons.
	 */
	private FlowPanel buttonPanel = new FlowPanel();
	
	/**
	 * Save button.
	 */
	private Button saveButton = new Button("Save");
	
	/**
	 * Clear button.
	 */
	private Button clearButton = new Button("Clear");
	
	private ClientUserModel userModel;
	private ClientAdminUserModel adminUserModel;
	private int adminUserIndex;
	
	/**
	 * Adding all widgets to the center content main panel.
	 */

	public void initEditAdminUserView(){
		
		//initializing content
		this.centerContent.clear();
		//this.viewContent.setStylePrimaryName(NewAdminUserViewClassName.NewAdminUserBlock.getClassname());
		this.viewContent.setStyleName(VIEW_CONTENT_CLASSNAME);
		centerContent.add(viewContent);
	}
	
	public EditAdminUserView(HTMLPanel centerContent, BaseViewHandler baseView, ClientUserModel userModel, ClientAdminUserModel adminUserModel, int adminUserIndex) {
		
		this.baseView = baseView;
		
		this.centerContent = centerContent;
		this.centerContent.setStyleName("centerContent");
		this.userModel = userModel;
		this.adminUserModel = adminUserModel;
		this.adminUserIndex = adminUserIndex;
		
		usernameTextBox.setText(adminUserModel.getUsername());
		
		editUserLabel.addStyleName("sec-title");
		viewContent.add(editUserLabel);
		
		usernamePanel.setStyleName("usernameContent");
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameTextBox);
		viewContent.add(usernamePanel);
		
		passwordPanel.setStyleName("passwordContent");
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordTextBox);
		viewContent.add(passwordPanel);
		
		confirmPasswordPanel.setStyleName("confirmPasswordContent");
		confirmPasswordPanel.add(confirmPasswordLabel);
		confirmPasswordPanel.add(confirmPasswordTextBox);
		viewContent.add(confirmPasswordPanel);
		
		saveButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		saveAdminUser();
	    	}
	    });
		
		buttonPanel.setStyleName("buttonsContent");
		buttonPanel.add(saveButton);
		
		clearButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		clearTextBoxes();
	    	}
	    });
		
		buttonPanel.add(clearButton);
		viewContent.add(buttonPanel);
	}
	
	private void saveAdminUser() {
		final String username = this.usernameTextBox.getText();
		final String password = this.passwordTextBox.getText();
		
		final String confirmPassword = this.confirmPasswordTextBox.getText();
		//TODO validate Alpahnumeric
		if(confirmPassword.equals("") || password.equals("") || username.equals("")) {
			baseView.getBaseBinder().getNotice().setText("All the fields are required");
			centerContent.add(baseView.getBaseBinder().getNotice());
			return;
		}else if(!confirmPassword.equals(password)){
			baseView.getBaseBinder().getNotice().setText("Passwords do not match");
			centerContent.add(baseView.getBaseBinder().getNotice());
			return;
		}
		
		EditAdminUserCallbackHandler editAdminCallback = new EditAdminUserCallbackHandler(baseView, userModel, centerContent, username, password, adminUserIndex);
		EditAdminUserServiceAsync editAdminUserService = GWT.create(EditAdminUserService.class);
		editAdminUserService.editAdminUser(username, password, adminUserModel.getKey(), editAdminCallback);
		
	}
	
	private void clearTextBoxes() {
		usernameTextBox.setText("");
		passwordTextBox.setText("");
	}	

	public HTMLPanel getCenterContent() {
		return centerContent;
	}

	public void setCenterContent(HTMLPanel centerContent) {
		this.centerContent = centerContent;
	}

	public HTMLPanel getViewContent() {
		return viewContent;
	}

	public void setViewContent(HTMLPanel viewContent) {
		this.viewContent = viewContent;
	}

	public TextBox getUsernameTextBox() {
		return usernameTextBox;
	}

	public void setUsernameTextBox(TextBox usernameTextBox) {
		this.usernameTextBox = usernameTextBox;
	}

	public PasswordTextBox getPasswordTextBox() {
		return passwordTextBox;
	}

	public void setPasswordTextBox(PasswordTextBox passwordTextBox) {
		this.passwordTextBox = passwordTextBox;
	}

	public FlowPanel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(FlowPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

	public Button getClearButton() {
		return clearButton;
	}

	public void setClearButton(Button clearButton) {
		this.clearButton = clearButton;
	}
}
