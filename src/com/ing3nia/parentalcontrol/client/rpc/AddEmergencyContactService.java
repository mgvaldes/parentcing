package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;

@RemoteServiceRelativePath("add-emcy-contact")
public interface AddEmergencyContactService extends RemoteService {
	public String addEmergencyContact(EmergencyNumberModel emergencyNumber, String smartphoneKey) throws IllegalArgumentException;
}