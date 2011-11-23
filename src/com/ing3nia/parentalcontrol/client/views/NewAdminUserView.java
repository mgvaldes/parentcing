package com.ing3nia.parentalcontrol.client.views;

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
import com.ing3nia.parentalcontrol.client.views.classnames.NewAdminUserViewClassName;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.AddAdminUserService;
import com.ing3nia.parentalcontrol.client.rpc.AddAdminUserServiceAsync;

public class NewAdminUserView {
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
	
	/**
	 * Password label.
	 */
	private Label passwordLabel = new Label("Password:");
	
	/**
	 * Password text box.
	 */
	private PasswordTextBox passwordTextBox = new PasswordTextBox();
	
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
		this.viewContent.setStylePrimaryName(NewAdminUserViewClassName.NewAdminUserBlock.getClassname());
		centerContent.add(viewContent);
	}
	
	public NewAdminUserView(HTMLPanel centerContent, ClientUserModel loggedUser) {
		this.centerContent = centerContent;
		this.loggedUser = loggedUser;
		
		viewContent.add(newUserLabel);
		viewContent.add(usernameLabel);
		viewContent.add(usernameTextBox);
		viewContent.add(passwordLabel);
		viewContent.add(passwordTextBox);
		
		saveButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		saveAdminUser();
	    	}
	    });
		
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
		
		AddAdminUserServiceAsync addAdminUserService = GWT.create(AddAdminUserService.class);
		addAdminUserService.addAdminUser(this.usernameTextBox.getText(), this.passwordTextBox.getText(), this.loggedUser.getKey(), 
				new AsyncCallback<String>() {
					public void onFailure(Throwable error) {
					}
		
					public void onSuccess(String result) {
						if (result != null) {
							ClientAdminUserModel newAdminUser = new ClientAdminUserModel(username, password);
							loggedUser.getAdmins().add(newAdminUser);
						}
						else {
							Window.alert("An error occured. The new admin user couldn't be saved.");
						}
					}
				}
		);
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
