package com.ing3nia.parentalcontrol.client.views.async;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.DeviceSettingsClickHandler;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.utils.PCPropertyType;
import com.ing3nia.parentalcontrol.client.views.AdminUserListView;
import com.ing3nia.parentalcontrol.client.views.DeviceContactListView;
import com.ing3nia.parentalcontrol.client.views.DeviceEmergencyNumberListView;
import com.ing3nia.parentalcontrol.client.views.DeviceSettingsView;
import com.ing3nia.parentalcontrol.client.views.RuleListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;

public class SaveSmartphoneModificationsCallbackHandler implements AsyncCallback<Boolean> {
	private BaseViewHandler baseView;
	private RuleModel newRule;
	private EmergencyNumberModel emergencyModel;
	private int option;
	private String deviceName;
	private String speedLimit;
	
	public SaveSmartphoneModificationsCallbackHandler(BaseViewHandler baseView, RuleModel newRule, int option) {
		this.baseView = baseView;
		this.newRule = newRule;
		this.option = option;
	}
	
	public SaveSmartphoneModificationsCallbackHandler(BaseViewHandler baseView, EmergencyNumberModel emergencyNumber, int option) {
		this.baseView = baseView;
		this.emergencyModel = emergencyNumber;
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
				
				changeSmarpthoneUIName(this.deviceName, baseView.getClickedSmartphoneIndex());			
			}
			else if (this.option == 2) { //SaveMod contacts
				
			}
			else if (this.option == 3) { //SaveMod edit rule
				ArrayList<RuleModel> rules = smartphone.getRules();
				baseView.getBaseBinder().getCenterContent().clear();
				
				Button ruleList = baseView.getMenuSetter().getAlertRules();
				ruleList.setStyleName("selectedShinnyButton");
				BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.AlertRules, baseView.getMenuSetter().getCenterMenuOptions());

				RuleListView ruleListView = new RuleListView(baseView, rules);
				ruleListView.initRuleListView();
			}
			else if (this.option == 4) { //SaveMod Add Emergency Contact
				smartphone.getAddedEmergencyNumbers().add(emergencyModel);
				baseView.getBaseBinder().getCenterContent().clear();

				BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.DeviceContacts, baseView.getMenuSetter().getCenterMenuOptions());

				DeviceEmergencyNumberListView deviceListView = new DeviceEmergencyNumberListView(baseView, baseView.getUser().getCid(), smartphone);
				deviceListView.initDeviceEmergencyNumberListView();			
			}
			else if (this.option == 5) { //SaveMod Delete Rule
				ArrayList<RuleModel> rules = smartphone.getRules();
				rules.remove(this.newRule);
				baseView.getBaseBinder().getCenterContent().clear();
				
				Button ruleList = baseView.getMenuSetter().getAlertRules();
				ruleList.setStyleName("selectedShinnyButton");
				BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.AlertRules, baseView.getMenuSetter().getCenterMenuOptions());

				RuleListView ruleListView = new RuleListView(baseView, rules);
				ruleListView.initRuleListView();
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
				baseView.getBaseBinder().getNotice().setText("An error occured. The rule couldn't be edited.");
				
			}
			if (this.option == 4) {
				baseView.getBaseBinder().getNotice().setText("An error occured. The new emergency contact couldn't be saved.");
			}
			
			baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		}
	}
	
	public void changeSmarpthoneUIName(String newName, int index) {
		FlowPanel deviceChoiceList = baseView.getBaseBinder()
				.getDeviceChoiceList();
		int count = 0;
		boolean breakAll = false;
		for (int i = 0; i < deviceChoiceList.getWidgetCount(); i++) {
			Widget w = deviceChoiceList.getWidget(i);
			if (w instanceof FlowPanel) {
				FlowPanel flowpanel = (FlowPanel) w;
				for (int j = 0; j < flowpanel.getWidgetCount(); j++) {
					Widget w2 = flowpanel.getWidget(j);
					if (w2 instanceof Button) {
						if (count == index) {
							Button b = (Button) w2;
							b.setText(newName);
							// augment if loop will break
							count++;
							breakAll = true;
							break;
						}
						count++;
					}
				}
				if(breakAll){
					break;
				}
			}
		}
	}
	
	
	public EmergencyNumberModel getEmergencyModel() {
		return emergencyModel;
	}

	public void setEmergencyModel(EmergencyNumberModel emergencyModel) {
		this.emergencyModel = emergencyModel;
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
