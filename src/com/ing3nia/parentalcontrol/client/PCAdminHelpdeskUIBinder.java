package com.ing3nia.parentalcontrol.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PCAdminHelpdeskUIBinder extends Composite {
	
	@UiField Button logout;
	
	@UiField HTMLPanel centerContent;
	
	@UiField FlowPanel centerMenuOptions;
	
	@UiField FlowPanel navigationPanel;
	
	@UiField Label notice;

	private static PCAdminHelpdeskUIBinderUiBinder uiBinder = GWT.create(PCAdminHelpdeskUIBinderUiBinder.class);

	interface PCAdminHelpdeskUIBinderUiBinder extends UiBinder<Widget, PCAdminHelpdeskUIBinder> {
	
	}

	public PCAdminHelpdeskUIBinder() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Button getLogout() {
		return logout;
	}

	public void setLogout(Button logout) {
		this.logout = logout;
	}

	public HTMLPanel getCenterContent() {
		return centerContent;
	}

	public void setCenterContent(HTMLPanel centerContent) {
		this.centerContent = centerContent;
	}

	public FlowPanel getCenterMenuOptions() {
		return centerMenuOptions;
	}

	public void setCenterMenuOptions(FlowPanel centerMenuOptions) {
		this.centerMenuOptions = centerMenuOptions;
	}

	public FlowPanel getNavigationPanel() {
		return navigationPanel;
	}

	public void setNavigationPanel(FlowPanel navigationPanel) {
		this.navigationPanel = navigationPanel;
	}

	public Label getNotice() {
		return notice;
	}

	public void setNotice(Label notice) {
		this.notice = notice;
	}
}
