package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class AddAdminUserCallbackHandler implements AsyncCallback<String> {

	BaseViewHandler baseView;
	ClientUserModel userModel;
	HTMLPanel centerContent;
	String newAdminUserName;
	String newAdminPass;
	
	public AddAdminUserCallbackHandler(BaseViewHandler baseView, ClientUserModel userModel,HTMLPanel centerContent, String newAdminUserName, String newAdminPass){
		this.baseView = baseView;
		this.userModel = userModel;
		this.centerContent = centerContent;
		this.newAdminUserName = newAdminUserName;
		this.newAdminPass = newAdminPass;
	}
	
	public void onFailure(Throwable error) {
		baseView.getBaseBinder()
				.getNotice()
				.setText(
						"An error occured. The new admin user couldn't be saved.");
		centerContent.add(baseView.getBaseBinder().getNotice());
	}

	public void onSuccess(String result) {
		if (result != null) {
			ClientAdminUserModel newAdminUser = new ClientAdminUserModel(result,newAdminUserName,newAdminPass);
			if(userModel.getAdmins() == null){
				userModel.setAdmins(new ArrayList<ClientAdminUserModel>());
			}
			userModel.getAdmins().add(newAdminUser);
			
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
							"An error occured. The new admin user couldn't be saved.");
			centerContent.add(baseView.getBaseBinder().getNotice());
		}
	}
}
