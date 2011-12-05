package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.AlertModel;
import com.ing3nia.parentalcontrol.client.rpc.SendEmailService;
import com.ing3nia.parentalcontrol.client.rpc.SendEmailServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.EmailSender;

public class WeekAlertsCallbackHandler implements AsyncCallback<ArrayList<AlertModel>> {
	
	BaseViewHandler baseView;
	
	public WeekAlertsCallbackHandler(BaseViewHandler baseView) {
		this.baseView = baseView;
	}
	
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The weekly alerts couldn't be retrieved.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	public void onSuccess(ArrayList<AlertModel> result) {
		if (result != null) {
			SendEmailCallbackHandler sendEmailCallback = new SendEmailCallbackHandler(this.baseView);
			SendEmailServiceAsync sendEmailService = GWT.create(SendEmailService.class);
			sendEmailService.sendEmail(this.baseView.getUser().getUsername(), convertAlertsToEmailContent(result), sendEmailCallback);
			
			//EmailSender.sendEmail(this.baseView.getUser().getUsername(), convertAlertsToEmailContent(result), this.baseView);
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The weekly alerts couldn't be retrieved.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
	
	public String convertAlertsToEmailContent(ArrayList<AlertModel> alerts) {
		String content = "";
		DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");
		int counter = 1;
		
		for (AlertModel alert : alerts) {
			content += counter + ") " + formatter.format(alert.getDate()) + " " + alert.getDevice() + " " + alert.getMessage() + "\n";
			counter++;
		}
		
		return content;
	}
}
