package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;

public class AdminUserListCallbackHandler implements AsyncCallback<ArrayList<ClientAdminUserModel>> {

	BaseViewHandler baseView;
	ClientUserModel userModel;
	HTMLPanel centerContent;
	
	public AdminUserListCallbackHandler(BaseViewHandler baseView, ClientUserModel userModel,HTMLPanel centerContent){
		this.baseView = baseView;
		this.userModel = userModel;
		this.centerContent = centerContent;
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder()
				.getNotice()
				.setText(
						"An error occured. The admin user list could not be retrieved.");
	}

	@Override
	public void onSuccess(ArrayList<ClientAdminUserModel> result) {
		
		if (result != null) {
			userModel.setAdmins(result);
			centerContent.clear();

			AdminUserListView listView = new AdminUserListView(baseView, userModel.getAdmins());
			listView.initAdminUserListView();
		} else {
			baseView.getBaseBinder()
					.getNotice()
					.setText(
							"An error occured. The admin user list could not be retrieved.");
			centerContent.add(baseView.getBaseBinder().getNotice());
		}
	}
}
