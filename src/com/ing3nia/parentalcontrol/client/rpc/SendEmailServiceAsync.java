package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.AlertModel;

public interface SendEmailServiceAsync {
	//public void sendEmail(String loggedUserKey, String smartName, ArrayList<AlertModel> alerts, AsyncCallback<Boolean> async);
	public void sendEmail(String toEmail, String content, AsyncCallback<Boolean> async);	
}
