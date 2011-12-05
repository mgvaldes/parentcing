package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.AlertModel;

@RemoteServiceRelativePath("send-email")
public interface SendEmailService extends RemoteService {
	//public Boolean sendEmail(String loggedUserKey, String smartName, ArrayList<AlertModel> alerts) throws IllegalArgumentException;
	public Boolean sendEmail(String toEmail, String content) throws IllegalArgumentException;	
}
