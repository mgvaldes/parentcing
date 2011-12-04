package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.TicketListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class CloseTicketCallbackHandler implements AsyncCallback<Boolean> {
	
	private BaseViewHandler baseView;
	private TicketModel ticket;
	
	public CloseTicketCallbackHandler(BaseViewHandler baseView, TicketModel ticket) {
		this.baseView = baseView;
		this.ticket = ticket;
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The ticket couldn't be closed.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {
			ClientUserModel userModel = baseView.getUser();
			
			userModel.getOpenTickets().remove(ticket);
			userModel.getClosedTickets().add(ticket);
			
			baseView.getBaseBinder().getCenterContent().clear();
			baseView.getMenuSetter().clearMenuOptions();
			
			baseView.initTicketUserCenterMenu();
			baseView.toggleTicketCenterMenu(CenterMenuOptionsClassNames.TicketList);
			
			TicketListView view = new TicketListView(baseView, userModel.getOpenTickets(), userModel.getClosedTickets());	
			view.initUserTicketList();
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The ticket couldn't be closed.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
