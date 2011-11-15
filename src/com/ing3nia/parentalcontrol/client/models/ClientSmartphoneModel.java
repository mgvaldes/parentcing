package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

import com.ing3nia.parentalcontrol.services.models.ContactModel;
import com.ing3nia.parentalcontrol.services.models.DeviceModel;
import com.ing3nia.parentalcontrol.services.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.services.models.LocationModel;
import com.ing3nia.parentalcontrol.services.models.ModificationModel;
import com.ing3nia.parentalcontrol.services.models.PropertyModel;
import com.ing3nia.parentalcontrol.services.models.RouteModel;
import com.ing3nia.parentalcontrol.services.models.RuleModel;

public class ClientSmartphoneModel {
private String keyId;

	private String name;
	
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
