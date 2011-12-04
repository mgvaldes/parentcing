package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.TicketListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class UserTicketListCallbackHandler implements AsyncCallback<HashMap<String, ArrayList<TicketModel>>> {
	private BaseViewHandler baseView;
	
	public UserTicketListCallbackHandler(BaseViewHandler baseView) {
		this.baseView = baseView;
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The helpdesk tickets couldn't be retrieved.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	@Override
	public void onSuccess(HashMap<String, ArrayList<TicketModel>> result) {
		if (result != null) {
			ClientUserModel userModel = baseView.getUser();
			userModel.setOpenTickets(result.get("opened"));
			userModel.setClosedTickets(result.get("closed"));
			
			TicketListView view = new TicketListView(baseView, userModel.getOpenTickets(), userModel.getClosedTickets());	
			view.initUserTicketList();
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The helpdesk tickets couldn't be retrieved.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
