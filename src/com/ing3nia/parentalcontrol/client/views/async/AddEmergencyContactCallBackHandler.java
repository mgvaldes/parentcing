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
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class AddEmergencyContactCallBackHandler implements AsyncCallback<String> {

	BaseViewHandler baseView;
	ClientUserModel userModel;
	HTMLPanel centerContent;
	EmergencyNumberModel emergencyNumber;
	String cid;
	String smartKey;
	
	public AddEmergencyContactCallBackHandler(BaseViewHandler baseView, ClientUserModel userModel,HTMLPanel centerContent, EmergencyNumberModel emergencyContact,String cid, String smartKey){
		this.baseView = baseView;
		this.userModel = userModel;
		this.centerContent = centerContent;
		this.cid = cid;
		this.smartKey = smartKey;
		this.emergencyNumber = emergencyContact;
	}
	
	public void onFailure(Throwable error) {
		baseView.getBaseBinder()
				.getNotice()
				.setText(
						"An error occured. The new Emergency Contact could not be created");
		centerContent.add(baseView.getBaseBinder().getNotice());
	}

	public void onSuccess(String result) {
		if (result!=null) {
			
			emergencyNumber.setKeyId(result);
			ModificationModel auxMod = new ModificationModel();
			ArrayList<EmergencyNumberModel> addedEmergency = new ArrayList<EmergencyNumberModel>();
			addedEmergency.add(this.emergencyNumber);
			
			auxMod.setAddedEmergencyNumbers(addedEmergency);
			
			SaveSmartphoneModificationsCallbackHandler saveModCallback = new SaveSmartphoneModificationsCallbackHandler(baseView, emergencyNumber, 4);
			SaveSmartphoneModificationsServiceAsync saveModService = GWT.create(SaveSmartphoneModificationsService.class);
			saveModService.saveSmartphoneModifications(this.cid, this.smartKey, auxMod, saveModCallback);
		} 
		else {
			baseView.getBaseBinder().getNotice().setText("An error occured. The new rule couldn't be updated.");
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
}
