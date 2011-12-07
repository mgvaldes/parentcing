package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.ing3nia.parentalcontrol.client.PCAdminHelpdeskUIBinder;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.rpc.AdminClosedTicketListService;
import com.ing3nia.parentalcontrol.client.rpc.AdminClosedTicketListServiceAsync;
import com.ing3nia.parentalcontrol.client.views.LoadingView;

public class AdminLoginCallbackHandler implements AsyncCallback<String> {
	
	PCLoginUIBinder pclogin;
	ClientAdminUserModel adminModel;
	Image loadingImage;
	
	public AdminLoginCallbackHandler(PCLoginUIBinder pclogin, ClientAdminUserModel adminModel, Image loadingImage) {
		this.pclogin = pclogin;
		this.adminModel = adminModel;
		this.loadingImage = loadingImage;
	}
	
	public void onFailure(Throwable error) {
		LoadingView.clearLoadingView(pclogin);
		pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
	}

	public void onSuccess(String result) {		
		if (result != null) {
			adminModel.setKey(result);
			
			AdminClosedTicketListCallbackHandler adminClosedTicketListCallback = new AdminClosedTicketListCallbackHandler(pclogin, adminModel, loadingImage);
			AdminClosedTicketListServiceAsync adminClosedTicketListService = GWT.create(AdminClosedTicketListService.class);
			adminClosedTicketListService.adminClosedTicketList(adminClosedTicketListCallback);
		}
		else {
			LoadingView.clearLoadingView(pclogin);
			pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
		}
	}
}
