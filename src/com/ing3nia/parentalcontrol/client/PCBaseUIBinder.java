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

public class PCBaseUIBinder extends Composite {
	
	
	
	@UiField HTMLPanel centerContent;
	
	@UiField FlowPanel deviceChoiceList;
	
	@UiField FlowPanel centerMenuOptions;
	
	@UiField Button addAdminButton;
	
	/*
	@UiField Button userListButton;
	@UiField Button addUserButton;
	*/
	
	/*
	 * 					<g:Button styleName="centerMenuButton" text="User List" ui:field="userListButton"></g:Button>
					<g:Button styleName="centerMenuButton" text="Add User" ui:field="addUserButton"></g:Button>
	 */
	
	private static PCBaseUIBinderUiBinder uiBinder = GWT
			.create(PCBaseUIBinderUiBinder.class);

	interface PCBaseUIBinderUiBinder extends UiBinder<Widget, PCBaseUIBinder> {
	}

	public PCBaseUIBinder() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public FlowPanel getDeviceChoiceList(){
		return deviceChoiceList;
	}

	public Button getAddAdminButton() {
		return addAdminButton;
	}

	public HTMLPanel getCenterContent() {
		return centerContent;
	}

	public FlowPanel getCenterMenuOptions() {
		return centerMenuOptions;
	}

	public void setCenterMenuOptions(FlowPanel centerMenuOptions) {
		this.centerMenuOptions = centerMenuOptions;
	}
	
	

}
