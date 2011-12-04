package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListService;
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListServiceAsync;
import com.ing3nia.parentalcontrol.client.views.TicketListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class AddTicketCallbackHandler implements AsyncCallback<String> {
	private BaseViewHandler baseView;
	private TicketModel newTicket;
	
	public AddTicketCallbackHandler(BaseViewHandler baseView, TicketModel newTicket) {
		this.baseView = baseView;
		this.newTicket = newTicket;
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The new ticket couldn't be saved.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	@Override
	public void onSuccess(String result) {
		if (result != null) {
			this.newTicket.setKey(result);
			this.baseView.getUser().getOpenTickets().add(newTicket);
			
			this.baseView.getBaseBinder().getCenterContent().clear();

			BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.TicketList, this.baseView.getMenuSetter().getCenterMenuOptions());
			baseView.toggleTicketCenterMenu(CenterMenuOptionsClassNames.TicketList);
			
			TicketListView view = new TicketListView(baseView, this.baseView.getUser().getOpenTickets(), this.baseView.getUser().getClosedTickets());	
			view.initUserTicketList();
			
//			UserTicketListCallbackHandler ticketsCallback = new UserTicketListCallbackHandler(baseView);
//			UserTicketListServiceAsync ticketService = GWT.create(UserTicketListService.class);
//			ticketService.getUserTicketList(this.baseView.getUser().getKey(), ticketsCallback);
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The new ticket couldn't be saved.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
