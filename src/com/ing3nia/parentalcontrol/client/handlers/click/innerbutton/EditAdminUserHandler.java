package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.views.subviews.EditAdminUserView;

public class EditAdminUserHandler implements FieldUpdater<ClientAdminUserModel, String>{

	BaseViewHandler baseView;
	HTMLPanel centerContent;
	ClientUserModel userModel;
	public EditAdminUserHandler(HTMLPanel centerContent, BaseViewHandler baseView, 	ClientUserModel userModel){
		this.baseView = baseView;
		this.centerContent = centerContent;
		this.userModel = userModel;
	}
	
	
	@Override
	public void update(int index, ClientAdminUserModel adminUser, String value) {
		baseView.getBaseBinder().getCenterContent().clear();
		EditAdminUserView editView = new EditAdminUserView(centerContent, baseView, userModel, adminUser, index);
		editView.initEditAdminUserView();
	}

}
