package com.ing3nia.parentalcontrol.client.rpc;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.AlertModel;

public interface WeekAlertsServiceAsync {
	public void weekAlerts(String smartKey, Date now, AsyncCallback<ArrayList<AlertModel>> async);
}
