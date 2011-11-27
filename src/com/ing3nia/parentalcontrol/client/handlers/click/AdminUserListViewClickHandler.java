package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.rpc.AdminUserListService;
import com.ing3nia.parentalcontrol.client.rpc.AdminUserListServiceAsync;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.async.AdminUserListCallbackHandler;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class AdminUserListViewClickHandler implements ClickHandler {

	BaseViewHandler baseViewHandler;
	
	public AdminUserListViewClickHandler(BaseViewHandler baseViewHandler){
		this.baseViewHandler = baseViewHandler;
	}
	
	@Override
		public void onClick(ClickEvent event) {	
			
			Button userList = baseViewHandler.getMenuSetter().getUserList();
			BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.UserList,
					baseViewHandler.getMenuSetter().getCenterMenuOptions());
			userList.setStyleName("selectedShinnyButton");
			
			baseViewHandler.getBaseBinder().getCenterContent().clear();
			if(baseViewHandler.getUser().getAdmins() == null){
				AdminUserListCallbackHandler adminUserListCallback = new AdminUserListCallbackHandler(baseViewHandler, baseViewHandler.getUser(), baseViewHandler.getBaseBinder().getCenterContent());
				AdminUserListServiceAsync adminUserAsync = GWT.create(AdminUserListService.class);
				adminUserAsync.getAdminUserList(baseViewHandler.getUser().getKey(), adminUserListCallback);		
			}else{
				AdminUserListView listView = new AdminUserListView(baseViewHandler, baseViewHandler.getUser().getAdmins());
				listView.initAdminUserListView();
			}
		}
}

