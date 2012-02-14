package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;

public interface AddEmergencyContactServiceAsync {

	void addEmergencyContact(EmergencyNumberModel emergencyNumber, String smartphoneKey, AsyncCallback<String> callback);

}
