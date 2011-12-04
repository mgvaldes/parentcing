package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.TicketDetailsView;
import com.ing3nia.parentalcontrol.client.views.TicketListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class AddTicketAnswerCallbackHandler implements AsyncCallback<String> {

	private BaseViewHandler baseView;
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
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The ticket answer couldn't be saved.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	@Override
	public void onSuccess(String result) {
		if (result != null) {
			ticketAnswer.setKey(result);
			ticket.getAnswers().add(ticketAnswer);
			
			baseView.getBaseBinder().getCenterContent().clear();			
			
			TicketDetailsView ticketView = new TicketDetailsView(this.baseView, ticket, isAdmin, this.openOrClosed);
			ticketView.initTicketDetailsView();
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The ticket answer couldn't be saved.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
