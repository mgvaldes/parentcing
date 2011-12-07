package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.rpc.AdminOpenTicketListService;
import com.ing3nia.parentalcontrol.client.rpc.AdminOpenTicketListServiceAsync;
import com.ing3nia.parentalcontrol.client.views.LoadingView;

public class AdminClosedTicketListCallbackHandler implements AsyncCallback<ArrayList<TicketModel>> {
	
	PCLoginUIBinder pclogin;
	ClientAdminUserModel adminModel;
	Image loadingImage;
	
	public AdminClosedTicketListCallbackHandler(PCLoginUIBinder pclogin, ClientAdminUserModel adminModel, Image loadingImage) {
		this.pclogin = pclogin;
		this.adminModel = adminModel;
		this.loadingImage = loadingImage;
	}
	
	public void onFailure(Throwable error) {
		LoadingView.clearLoadingView(pclogin);
		pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
	}

	public void onSuccess(ArrayList<TicketModel> result) {		
//		LoadingView.clearLoadingView(pclogin);
//		LoadingView.changeLoadingMessage(pclogin, AsyncronousCallsMessages.REQUESTING_DATA);
		
		if (result != null) {
			adminModel.setClosedTickets(result);
			
			AdminOpenTicketListCallbackHandler adminOpenTicketListCallback = new AdminOpenTicketListCallbackHandler(pclogin, adminModel, loadingImage);
			AdminOpenTicketListServiceAsync adminOpenTicketListService = GWT.create(AdminOpenTicketListService.class);
			adminOpenTicketListService.adminOpenTicketList(adminOpenTicketListCallback);
		} 
		else {
			LoadingView.clearLoadingView(pclogin);
			pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
		}
	}
}
