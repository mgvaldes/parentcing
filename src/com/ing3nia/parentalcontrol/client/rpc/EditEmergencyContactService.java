package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;

@RemoteServiceRelativePath("edit-emcy-contact")
public interface EditEmergencyContactService extends RemoteService {
	public boolean editEmergencyContact(EmergencyNumberModel emergencyNumber, String smartphoneKey) throws IllegalArgumentException;
}