package com.ing3nia.parentalcontrol.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PCRegisterUIBinder extends Composite {
	
	@UiField Label notice;
	@UiField Button registerButton;
	@UiField TextBox emailField;
	@UiField TextBox nameField;
	@UiField PasswordTextBox passField;
	@UiField PasswordTextBox confirmPassField;
	@UiField FlowPanel loginFieldsBox;
	@UiField FlowPanel loadingBlock;

	private static PCRegisterUIBinderUiBinder uiBinder = GWT.create(PCRegisterUIBinderUiBinder.class);

	interface PCRegisterUIBinderUiBinder extends UiBinder<Widget, PCRegisterUIBinder> {
	}

	public PCRegisterUIBinder() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Label getNotice() {
		return notice;
	}

	public void setNotice(Label notice) {
		this.notice = notice;
	}

	public Button getRegisterButton() {
		return registerButton;
	}

	public void setRegisterButton(Button registerButton) {
		this.registerButton = registerButton;
	}

	public TextBox getEmailField() {
		return emailField;
	}

	public void setEmailField(TextBox emailField) {
		this.emailField = emailField;
	}

	public TextBox getNameField() {
		return nameField;
	}

	public void setNameField(TextBox nameField) {
		this.nameField = nameField;
	}

	public PasswordTextBox getPassField() {
		return passField;
	}

	public void setPassField(PasswordTextBox passField) {
		this.passField = passField;
	}

	public PasswordTextBox getConfirmPassField() {
		return confirmPassField;
	}

	public void setConfirmPassField(PasswordTextBox confirmPassField) {
		this.confirmPassField = confirmPassField;
	}

	public FlowPanel getRegisterFieldsBox() {
		return loginFieldsBox;
	}

	public void setRegisterFieldsBox(FlowPanel registerFieldsBox) {
		this.loginFieldsBox = registerFieldsBox;
	}

	public FlowPanel getLoadingBlock() {
		return loadingBlock;
	}

	public void setLoadingBlock(FlowPanel loadingBlock) {
		this.loadingBlock = loadingBlock;
	}
}
