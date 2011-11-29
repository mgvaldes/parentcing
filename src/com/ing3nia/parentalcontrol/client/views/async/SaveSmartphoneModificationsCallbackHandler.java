package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DeviceSettingsClickHandler;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.utils.PCPropertyType;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.DeviceSettingsView;
import com.ing3nia.parentalcontrol.client.views.RuleListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class SaveSmartphoneModificationsCallbackHandler implements AsyncCallback<Boolean> {
	private BaseViewHandler baseView;
	private RuleModel newRule;
	private int option;
	private String deviceName;
	private String speedLimit;
	
	public SaveSmartphoneModificationsCallbackHandler(BaseViewHandler baseView, RuleModel newRule, int option) {
		this.baseView = baseView;
		this.newRule = newRule;
		this.option = option;
	}
	
	public SaveSmartphoneModificationsCallbackHandler(BaseViewHandler baseView, String deviceName, String speedLimit, int option) {
		this.baseView = baseView;
		this.deviceName = deviceName;
		this.speedLimit = speedLimit;
		this.option = option;
	}
	
	@Override
	public void onFailure(Throwable error) {
		baseView.getBaseBinder().getNotice().setText("An error occured. The new rule couldn't be saved.");
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {		
			SmartphoneModel smartphone = baseView.getUser().getSmartphones().get(baseView.getClickedSmartphoneIndex());
			//ArrayList<RuleModel> rules = smartphone.getRules();
			//rules.add(newRule);								
			//smartphone.setRules(rules);
			
			baseView.getBaseBinder().getCenterContent().clear();

			
			if (this.option == 0) { //SaveMod rules
				ArrayList<RuleModel> rules = smartphone.getRules();
				rules.add(newRule);								
				smartphone.setRules(rules);
				
				baseView.getBaseBinder().getCenterContent().clear();
				
				Button ruleList = baseView.getMenuSetter().getAlertRules();
				ruleList.setStyleName("selectedShinnyButton");
				BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.AlertRules, baseView.getMenuSetter().getCenterMenuOptions());

				RuleListView ruleListView = new RuleListView(baseView, rules);
				ruleListView.initRuleListView();
			}
			else if (this.option == 1) { //SaveMod device settings
				smartphone.setName(this.deviceName);
				findSpeedLimitProperty(smartphone).setValue(this.speedLimit);
				
				baseView.getBaseBinder().getCenterContent().clear();
				
				DeviceSettingsView view = new DeviceSettingsView(baseView, smartphone, baseView.getUser().getCid());
				view.initDeviceSettingsView();
				
				baseView.getBaseBinder().getNotice().setText("Device settings saved successfully.");
				baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
			}
			else if (this.option == 2) { //SaveMod contacts
				
			}
		}
		else {
			if (this.option == 0) {
				baseView.getBaseBinder().getNotice().setText("An error occured. The new rule couldn't be saved.");
			}
			else if (this.option == 2) {
				baseView.getBaseBinder().getNotice().setText("An error occured. The device settings couldn't be saved.");
			}
			else if (this.option == 3) {
				
			}
			
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
	
	public PropertyModel findSpeedLimitProperty(SmartphoneModel smartphone) {
		PropertyModel speedLimitProp = null;
		ArrayList<PropertyModel> props = smartphone.getProperties();
		
		for (PropertyModel p : props) {
			if (p.getId() == PCPropertyType.SPEED_LIMIT) {
				speedLimitProp = p;
				break;
			}
		}
		
		return speedLimitProp;
	}
}
