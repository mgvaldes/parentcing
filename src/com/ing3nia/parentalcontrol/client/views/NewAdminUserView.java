package com.ing3nia.parentalcontrol.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.views.async.AddAdminUserCallbackHandler;
import com.ing3nia.parentalcontrol.client.views.classnames.NewAdminUserViewClassName;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.AddAdminUserService;
import com.ing3nia.parentalcontrol.client.rpc.AddAdminUserServiceAsync;

public class NewAdminUserView {

	BaseViewHandler baseView;
	
	public static String VIEW_CONTENT_CLASSNAME = "newAdminUserContent";
	
	
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
	private Label newUserLabel = new Label("New User:");
	
	/**
	 * Username label.
	 */
	private Label usernameLabel = new Label("Username:");
	
	/**
	 * Username text box.
	 */
	private TextBox usernameTextBox = new TextBox();
	
	private FlowPanel usernamePanel = new FlowPanel();
	
	/**
	 * Password label.
	 */
	private Label passwordLabel = new Label("Password:");
	
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
	
	private FlowPanel passwordPanel = new FlowPanel();
	
	private FlowPanel confirmPasswordPanel = new FlowPanel();
	
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
	
	private ClientUserModel loggedUser;
	
	/**
	 * Adding all widgets to the center content main panel.
	 */

	public void initNewAdminUseView(){
		
		//initializing content
		this.centerContent.clear();
		this.centerContent.setStyleName("centerContent");
		this.viewContent.setStyleName(VIEW_CONTENT_CLASSNAME);
		//this.viewContent.setStylePrimaryName(NewAdminUserViewClassName.NewAdminUserBlock.getClassname());
		centerContent.add(viewContent);
	}
	
	public NewAdminUserView(HTMLPanel centerContent, ClientUserModel loggedUser, BaseViewHandler baseView) {
		
		this.baseView = baseView;
		
		this.centerContent = centerContent;
		this.loggedUser = loggedUser;
		
		newUserLabel.addStyleName("sec-title");
		viewContent.add(newUserLabel);
		
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
		
		DOM.setElementProperty(saveButton.getElement(), "id", "saveNewAdminButton");
		buttonPanel.add(saveButton);
		
		DOM.setElementProperty(clearButton.getElement(), "id", "clearNewAdminButton");
		clearButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		clearTextBoxes();
	    	}
	    });
		
		buttonPanel.setStyleName("buttonsContent");
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
		
		Image loadingImage = new Image("/media/images/loading.gif");
		LoadingView.setLoadingView(baseView.getBaseBinder(), "Creating Admin User", loadingImage);
		
		AddAdminUserCallbackHandler addAdminCallback = new AddAdminUserCallbackHandler(baseView, loggedUser, centerContent, username, password);
		AddAdminUserServiceAsync addAdminUserService = GWT.create(AddAdminUserService.class);
		addAdminUserService.addAdminUser(this.usernameTextBox.getText(), this.passwordTextBox.getText(), this.loggedUser.getKey(), addAdminCallback);
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
