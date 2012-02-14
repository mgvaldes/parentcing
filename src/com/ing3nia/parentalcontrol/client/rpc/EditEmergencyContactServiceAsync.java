package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;

public interface EditEmergencyContactServiceAsync {
	void editEmergencyContact(EmergencyNumberModel emergencyNumber, String smartphoneKey, AsyncCallback<Boolean> callback);

}
