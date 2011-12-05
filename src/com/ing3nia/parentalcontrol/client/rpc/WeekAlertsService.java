package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.AlertModel;

@RemoteServiceRelativePath("week-alerts")
public interface WeekAlertsService extends RemoteService {
	public ArrayList<AlertModel> weekAlerts(String smartKey, Date now) throws IllegalArgumentException;
}