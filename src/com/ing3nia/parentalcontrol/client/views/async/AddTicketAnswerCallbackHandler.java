package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.AdminHelpdeskViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.AdminTicketDetailsView;
import com.ing3nia.parentalcontrol.client.views.TicketDetailsView;

public class AddTicketAnswerCallbackHandler implements AsyncCallback<String> {

	private BaseViewHandler baseView;
	private AdminHelpdeskViewHandler helpdeskView;
	private TicketAnswerModel ticketAnswer;
	private TicketModel ticket;
	private boolean isAdmin;
	private boolean openOrClosed;
	
	public AddTicketAnswerCallbackHandler(BaseViewHandler baseView, TicketAnswerModel ticketAnswer, TicketModel ticket, boolean isAdmin, boolean openOrClosed) {
		this.baseView = baseView;
		this.ticketAnswer = ticketAnswer;
		this.ticket = ticket;
		this.isAdmin = isAdmin;
		this.openOrClosed = openOrClosed;
	}
	
	public AddTicketAnswerCallbackHandler(AdminHelpdeskViewHandler helpdeskView, TicketAnswerModel ticketAnswer, TicketModel ticket, boolean isAdmin, boolean openOrClosed) {
		this.helpdeskView = helpdeskView;
		this.ticketAnswer = ticketAnswer;
		this.ticket = ticket;
		this.isAdmin = isAdmin;
		this.openOrClosed = openOrClosed;
	}
	
	@Override
	public void onFailure(Throwable error) {
		if (isAdmin) {
			helpdeskView.getHelpdeskBinder().getNotice().setText("An error occured. The ticket answer couldn't be saved.");
			helpdeskView.getHelpdeskBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The ticket answer couldn't be saved.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}

	@Override
	public void onSuccess(String result) {
		if (result != null) {
			ticketAnswer.setKey(result);
			ticket.getAnswers().add(ticketAnswer);
			
			if (isAdmin) {
				helpdeskView.getHelpdeskBinder().getCenterContent().clear();
				
				AdminTicketDetailsView ticketView = new AdminTicketDetailsView(this.helpdeskView, ticket, isAdmin, this.openOrClosed);
				ticketView.initTicketDetailsView();
			}
			else {
				baseView.getBaseBinder().getCenterContent().clear();			
				
				TicketDetailsView ticketView = new TicketDetailsView(this.baseView, ticket, isAdmin, this.openOrClosed);
				ticketView.initTicketDetailsView();
			}
		} 
		else {
			if (isAdmin) {
				helpdeskView.getHelpdeskBinder().getNotice().setText("An error occured. The ticket answer couldn't be saved.");
				helpdeskView.getHelpdeskBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
			}
			else {
				baseView.getBaseBinder().getNotice().setText("An error occured. The ticket answer couldn't be saved.");
				baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
			}
		}
	}
}
