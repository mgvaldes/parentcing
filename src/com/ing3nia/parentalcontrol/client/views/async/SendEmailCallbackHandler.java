package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.AlertModel;
import com.ing3nia.parentalcontrol.client.rpc.SendEmailService;
import com.ing3nia.parentalcontrol.client.rpc.SendEmailServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.WeekAlertsService;
import com.ing3nia.parentalcontrol.client.rpc.WeekAlertsServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.EmailSender;

public class SendEmailCallbackHandler implements AsyncCallback<Boolean> {
	
	BaseViewHandler baseView;
	
	public SendEmailCallbackHandler(BaseViewHandler baseView) {
		this.baseView = baseView;
	}
	
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The email couldn't be sent.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	public void onSuccess(Boolean result) {
		if (result) {
			baseView.getBaseBinder().getNotice().setText("The emial was successfully sent.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The email couldn't be sent.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
