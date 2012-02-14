package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.DeviceEmergencyNumberListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class EditEmergencyContactCallBackHandler implements AsyncCallback<Boolean> {

	BaseViewHandler baseView;
	ClientUserModel userModel;
	HTMLPanel centerContent;
	EmergencyNumberModel oldEmergencyContact;
	EmergencyNumberModel emergencyNumber;
	String cid;
	SmartphoneModel smartphone;
	
	public EditEmergencyContactCallBackHandler(BaseViewHandler baseView, ClientUserModel userModel,HTMLPanel centerContent,EmergencyNumberModel oldEmergencyContact, EmergencyNumberModel emergencyContact, String cid, SmartphoneModel smartphone){
		this.baseView = baseView;
		this.userModel = userModel;
		this.centerContent = centerContent;
		this.cid = cid;
		this.smartphone = smartphone;
		this.oldEmergencyContact = oldEmergencyContact;
		this.emergencyNumber = emergencyContact;
		
	}
	
	public void onFailure(Throwable error) {
		baseView.getBaseBinder()
				.getNotice()
				.setText(
						"An error occured. The new Emergency Contact could not be updated");
		centerContent.add(baseView.getBaseBinder().getNotice());
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {
			this.oldEmergencyContact.setCountry(emergencyNumber.getCountry());
			this.oldEmergencyContact.setDescription(emergencyNumber.getDescription());
			this.oldEmergencyContact.setNumber(emergencyNumber.getNumber());
			
			baseView.getBaseBinder().getCenterContent().clear();
			BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.DeviceContacts, baseView.getMenuSetter().getCenterMenuOptions());

			DeviceEmergencyNumberListView deviceListView = new DeviceEmergencyNumberListView(baseView, baseView.getUser().getCid(), smartphone);
			deviceListView.initDeviceEmergencyNumberListView();	
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The emergency contact couldn't be updated.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}

}
