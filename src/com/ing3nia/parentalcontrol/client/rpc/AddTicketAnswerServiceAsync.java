package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;

public interface AddTicketAnswerServiceAsync {
	public void addTicketAnswer(TicketAnswerModel answer, String ticketKey, boolean isAdmin, AsyncCallback<String> async);
}