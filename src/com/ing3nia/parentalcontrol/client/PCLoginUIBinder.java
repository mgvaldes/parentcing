package com.ing3nia.parentalcontrol.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;

public class PCLoginUIBinder extends Composite {

	@UiField Label notice;
	@UiField Button signInButton;
	@UiField TextBox emailField;
	@UiField PasswordTextBox passField;
	@UiField FlowPanel loginFieldsBox;
	@UiField FlowPanel loadingBlock;
	@UiField CheckBox rememberMeBox;
	
	private static PCLoginUIBinderUiBinder uiBinder = GWT
			.create(PCLoginUIBinderUiBinder.class);

	interface PCLoginUIBinderUiBinder extends UiBinder<Widget, PCLoginUIBinder> {
	}

	public PCLoginUIBinder() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Label getNotice() {
		return notice;
	}

	public void setNotice(Label notice) {
		this.notice = notice;
	}

	public Button getSignInButton() {
		return signInButton;
	}

	public void setSignInButton(Button signInButton) {
		this.signInButton = signInButton;
	}

	public TextBox getEmailField() {
		return emailField;
	}

	public void setEmailField(TextBox emailField) {
		this.emailField = emailField;
	}

	public PasswordTextBox getPassField() {
		return passField;
	}

	public void setPassField(PasswordTextBox passField) {
		this.passField = passField;
	}

	public FlowPanel getLoginFieldsBox() {
		return loginFieldsBox;
	}

	public void setLoginFieldsBox(FlowPanel loginFieldsBox) {
		this.loginFieldsBox = loginFieldsBox;
	}

	public FlowPanel getLoadingBlock() {
		return loadingBlock;
	}

	public void setLoadingBlock(FlowPanel loadingBlock) {
		this.loadingBlock = loadingBlock;
	}

	public CheckBox getRememberMeBox() {
		return rememberMeBox;
	}

	public void setRememberMeBox(CheckBox rememberMeBox) {
		this.rememberMeBox = rememberMeBox;
	}


	
	
}
