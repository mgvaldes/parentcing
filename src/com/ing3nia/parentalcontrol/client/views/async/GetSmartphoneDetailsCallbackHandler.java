package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.SmartphoneClickHandler;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;

public class GetSmartphoneDetailsCallbackHandler implements AsyncCallback<SmartphoneModel>{

	private BaseViewHandler baseView;
	private int smartphoneIndex;
	private MenuSetterHandler menuSetter;
	private FlowPanel deviceChoiceList;
	private Button button;
	private SmartphoneClickHandler smartphoneClickHandler;
	
	
	public GetSmartphoneDetailsCallbackHandler(BaseViewHandler baseView, MenuSetterHandler menuSetter,Button button, FlowPanel deviceChoiceList,int smartphoneIndex,SmartphoneClickHandler smartphoneClickHandler){
		this.baseView = baseView;
		this.smartphoneIndex = smartphoneIndex;
		this.menuSetter = menuSetter;
		this.deviceChoiceList = deviceChoiceList;
		this.button = button;
		this.smartphoneIndex = smartphoneIndex;
		this.smartphoneClickHandler = smartphoneClickHandler;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		
		baseView.getBaseBinder().getNotice().setText("An error occurred. Smartphone details could not be loaded.");
		baseView.getBaseBinder().getCenterContent().clear();
		baseView.getBaseBinder().getCenterContent().add(baseView.getBaseBinder().getNotice());
		System.out.println("ERROR: "+caught.getMessage());
		caught.printStackTrace();
	}

	@Override
	public void onSuccess(SmartphoneModel resultSmartphoneModel) {	
		System.out.println("Smartphone details succesfully synced");
		SmartphoneModel smartphone = baseView.getUser().getSmartphones().get(this.smartphoneIndex);
		//smartphone.setDetailsSynced(1);		

		smartphone.setActiveContacts(resultSmartphoneModel.getActiveContacts());
		smartphone.setInactiveContacts(resultSmartphoneModel.getInactiveContacts());
		smartphone.setAddedEmergencyNumbers(resultSmartphoneModel.getAddedEmergencyNumbers());
		smartphone.setDeletedEmergencyNumbers(resultSmartphoneModel.getDeletedEmergencyNumbers());
		smartphone.setModification(resultSmartphoneModel.getModification());
		smartphone.setProperties(resultSmartphoneModel.getProperties());
		smartphone.setRoutes(resultSmartphoneModel.getRoutes());
		smartphone.setRules(resultSmartphoneModel.getRules());
		
		SmartphoneClickHandler.setUISmartphoneClickHandler(baseView, menuSetter, button, deviceChoiceList, smartphoneIndex, smartphoneClickHandler);
	
	}
}
