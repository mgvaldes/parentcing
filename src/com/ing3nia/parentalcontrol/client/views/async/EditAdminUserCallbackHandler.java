package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class EditAdminUserCallbackHandler implements AsyncCallback<Boolean> {

	BaseViewHandler baseView;
	ClientUserModel userModel;
	HTMLPanel centerContent;
	String newAdminUserName;
	String newAdminPass;
	int userAdminIndex;
	
	public EditAdminUserCallbackHandler(BaseViewHandler baseView, ClientUserModel userModel,HTMLPanel centerContent, String newAdminUserName, String newAdminPass, int userAdminIndex){
		this.baseView = baseView;
		this.userModel = userModel;
		this.centerContent = centerContent;
		this.newAdminUserName = newAdminUserName;
		this.newAdminPass = newAdminPass;
		this.userAdminIndex = userAdminIndex;
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder()
				.getNotice()
				.setText(
						"An error occured. The new admin user couldn't be saved.");
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {
			//ClientAdminUserModel newAdminUser = new ClientAdminUserModel(newAdminUserName,newAdminPass);
			//userModel.getAdmins().add(newAdminUser);
			ClientAdminUserModel adminUser = userModel.getAdmins().get(userAdminIndex);
			adminUser.setUsername(newAdminUserName);
			adminUser.setPassword(newAdminPass);
			centerContent.clear();
			
			Button userList = baseView.getMenuSetter().getUserList();
			userList.setStyleName("selectedShinnyButton");
			BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.UserList,
					baseView.getMenuSetter().getCenterMenuOptions());

			AdminUserListView listView = new AdminUserListView(baseView, userModel.getAdmins());
			listView.initAdminUserListView();
		} else {
			baseView.getBaseBinder()
					.getNotice()
					.setText(
							"An error occured. The admin user could not be updated.");
			centerContent.add(baseView.getBaseBinder().getNotice());
		}
	}
}
