package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.ing3nia.parentalcontrol.client.PCAdminHelpdeskUIBinder;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.AdminHelpdeskViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.LoadingView;

public class AdminOpenTicketListCallbackHandler implements AsyncCallback<ArrayList<TicketModel>> {
	
	PCLoginUIBinder pclogin;
	ClientAdminUserModel adminModel;
	Image loadingImage;
	
	public AdminOpenTicketListCallbackHandler(PCLoginUIBinder pclogin, ClientAdminUserModel adminModel, Image loadingImage) {
		this.pclogin = pclogin;
		this.adminModel = adminModel;
		this.loadingImage = loadingImage;
	}
	
	public void onFailure(Throwable error) {
		LoadingView.clearLoadingView(pclogin);
		pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
	}

	public void onSuccess(ArrayList<TicketModel> result) {		
		if (result != null) {
			adminModel.setOpenTickets(result);
			
			PCAdminHelpdeskUIBinder helpdeskUI = new PCAdminHelpdeskUIBinder();
			RootPanel.get().clear();
			RootPanel.get().add(helpdeskUI);
			
			AdminHelpdeskViewHandler helpdeskViewHandler = new AdminHelpdeskViewHandler(helpdeskUI, adminModel);
			helpdeskViewHandler.initHelpdeskView();
		} 
		else {
			LoadingView.clearLoadingView(pclogin);
			pclogin.getNotice().setText(AsyncronousCallsMessages.UNPROCESSED_REQUEST);
		}
	}
}
