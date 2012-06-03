package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.CheckAdminUserService;
import com.ing3nia.parentalcontrol.client.rpc.CheckAdminUserServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.GetSmartphoneDetailsService;
import com.ing3nia.parentalcontrol.client.rpc.GetSmartphoneDetailsServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.utils.NavigationViewList;
import com.ing3nia.parentalcontrol.client.views.DeviceAlertListView;
import com.ing3nia.parentalcontrol.client.views.LoadingView;
import com.ing3nia.parentalcontrol.client.views.async.GetSmartphoneDetailsCallbackHandler;
import com.ing3nia.parentalcontrol.services.models.utils.SmartphoneModelUtils;

public class SmartphoneClickHandler implements ClickHandler{
	
	private String key;
	private BaseViewHandler baseView;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private FlowPanel deviceChoiceList;
	private Button button;
	private String smartphoneCount;
	private int smartphoneIndex;
	private SmartphoneModel smartphoneModel;
	
	public SmartphoneClickHandler(String key, BaseViewHandler baseView, Button b, int smartphoneIndex, SmartphoneModel smartphoneModel){
		this.key = key;
		this.baseView = baseView;
		this.centerContent = baseView.getBaseBinder().getCenterContent();
		this.menuSetter = baseView.getMenuSetter();
		this.deviceChoiceList = baseView.getBaseBinder().getDeviceChoiceList();
		this.button  = b;
		this.smartphoneIndex = smartphoneIndex;
		this.smartphoneModel = smartphoneModel;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();
		
		//setting clicked smartphone index
		baseView.setClickedSmartphoneIndex(this.smartphoneIndex);
		
		// Get smartphone details if not got first
		SmartphoneModel smartphone = baseView.getUser().getSmartphones().get(this.smartphoneIndex);
		//if(smartphoneModel.getDetailsSynced() != 1){
		//	Image loadingImage = new Image("/media/images/loading.gif");
		//	LoadingView.setLoadingView(this.baseView.getBaseBinder(), "Loading Smartphone Details", loadingImage);
			
			GetSmartphoneDetailsCallbackHandler getDetailsCallback = new GetSmartphoneDetailsCallbackHandler(baseView, menuSetter, button, deviceChoiceList, smartphoneIndex, this);
			GetSmartphoneDetailsServiceAsync getSmartphoneDetails = GWT.create(GetSmartphoneDetailsService.class);
			getSmartphoneDetails.getDetails(this.baseView.getUser().getCid(), smartphone, getDetailsCallback);
			
			//smartphone = baseView.getUser().getSmartphones().get(this.smartphoneIndex);
		//}else{
		//	setUISmartphoneClickHandler(baseView, menuSetter, button, deviceChoiceList, smartphoneIndex, this);	
		//}

	}
	
	public static void setUISmartphoneClickHandler(BaseViewHandler baseView, MenuSetterHandler menuSetter, Button button, FlowPanel deviceChoiceList, int smartphoneIndex,SmartphoneClickHandler smartphoneClickHandler){
		
		baseView.getBaseBinder().getCenterMenuOptions().clear();
		menuSetter = new MenuSetterHandler(baseView.getBaseBinder());
		baseView.setMenuSetter(menuSetter);
		baseView.initDeviceMenuClickHandlers();
		
		FlowPanel menuOptions = menuSetter.getCenterMenuOptions();
		menuOptions.add(menuSetter.getDailyRoute());
		menuOptions.add(menuSetter.getAlertList());
		menuSetter.getAlertList().setStyleName("selectedShinnyButton");
		menuOptions.add(menuSetter.getAlertRules());
		menuOptions.add(menuSetter.getDeviceContacts());
		menuOptions.add(menuSetter.getDeviceSettings());
		
		menuSetter.setParentSmartphoneButton(button);
		
		deviceChoiceList = BaseViewHandler.clearRouteNamesPanels(deviceChoiceList);
		
		deviceChoiceList = BaseViewHandler.clearSmartphoneListStyle(deviceChoiceList);
		button.setStyleName("selectedSmartphone", true);
		
		//set navigation smarpthone button
		NavigationViewList.setSmartphoneButton(baseView.getUser().getSmartphones().get(smartphoneIndex).getName(), smartphoneClickHandler);
		NavigationHandler navHandler = new NavigationHandler(baseView);
		navHandler.setSmartphoneNavigation(baseView.getBaseBinder().getNavigationPanel());
		
		//TODO set selected button style
		//this.button.setStyleName("selectedSmartphoneButton");
		
		
		DailyRouteClickHandler dailyRouteClickHandler = new DailyRouteClickHandler(baseView.getUser().getKey(), baseView);
		dailyRouteClickHandler.activateDailyRouteClickHandler();
		//DeviceAlertListView view = new DeviceAlertListView(this.baseView, SmartphoneModel.getUserAlertList(smartphoneModel));	
		//DeviceAlertListView view = new DeviceAlertListView(centerContent, smartphoneModel.getAlerts());
		//view.initDeviceAlertListView();
	}

	public int getSmartphoneIndex() {
		return smartphoneIndex;
	}

	public void setSmartphoneIndex(int smartphoneIndex) {
		this.smartphoneIndex = smartphoneIndex;
	}
	
}