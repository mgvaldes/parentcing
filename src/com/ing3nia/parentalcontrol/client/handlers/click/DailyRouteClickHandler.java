package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.views.DeviceDailyRouteView;

public class DailyRouteClickHandler implements ClickHandler{

	private String key;
	private PCBaseUIBinder baseBinder;
	private BaseViewHandler baseView;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private FlowPanel deviceChoiceList;
	private static Logger logger = Logger.getLogger("Daily Route Logger");
	
	public DailyRouteClickHandler(String key, BaseViewHandler baseView){
		this.key = key;
		this.baseView = baseView;
		this.baseBinder = baseView.getBaseBinder();
		this.centerContent = baseView.getBaseBinder().getCenterContent();
		this.menuSetter = baseView.getMenuSetter();
		this.deviceChoiceList = baseView.getBaseBinder().getDeviceChoiceList();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		activateDailyRouteClickHandler();
		
		NavigationHandler navHandler = new NavigationHandler(baseView);
		navHandler.setSmartphoneNavigation(baseView.getBaseBinder().getNavigationPanel());
		
		
		//logger.info("Devices Loaded. Displaying route Names "+view.getDeviceRouteNames().size()+" ex: "+view.getDeviceRouteNames().get(0));
		//displayRouteNames(menuSetter.getParentSmartphoneButton(), deviceChoiceList, view.getDeviceRouteNames());
	}
	
	public void activateDailyRouteClickHandler(){
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();

		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getDailyRoute());
		this.menuSetter.getDailyRoute().setStyleName("selectedShinnyButton");
		menuOptions.add(this.menuSetter.getAlertList());
		menuOptions.add(this.menuSetter.getAlertRules());
		menuOptions.add(this.menuSetter.getDeviceContacts());
		menuOptions.add(this.menuSetter.getDeviceSettings());
		
		//initializing route panel
		ScrollPanel routePanel = initRouteNamesDisplay(menuSetter.getParentSmartphoneButton(), menuOptions);
		HTMLPanel scrollBody = new HTMLPanel("");
		if(routePanel != null){
			deviceChoiceList = BaseViewHandler
			.clearRouteNamesPanels(deviceChoiceList);
			
			routePanel.setStyleName("routeScrollPanel");
			routePanel.setHeight("120px");		
			scrollBody.setStyleName("routeScrollBodyPanel");
			routePanel.add(scrollBody);
			placeRouteNamesPanel(routePanel, deviceChoiceList, baseView.getClickedSmartphoneIndex());
		}

		//deviceChoiceList.add(routePanel);
		
		logger.info("Initializing device route map");
		DeviceDailyRouteView view = new DeviceDailyRouteView(baseBinder, baseView, scrollBody);		
		view.setDeviceRoute(getDummyDeviceRoute());
		view.initDeviceLocationLoad();
	}
	
	public ScrollPanel initRouteNamesDisplay(Button smpButton, FlowPanel deviceChoiceList){
		if(smpButton==null) return null;
		int index= deviceChoiceList.getWidgetIndex(smpButton);
		ScrollPanel p = new ScrollPanel();
		return p;
	}
	/*
	public void displayRouteNames(Button smpButton, FlowPanel deviceChoiceList, List<String> deviceRouteNames){
		if(smpButton==null) return;
		int index= deviceChoiceList.getWidgetIndex(smpButton);
		ScrollPanel p = new ScrollPanel();
		for(String route : deviceRouteNames){
			Label l = new Label(route);
			p.add(l);
		}
		deviceChoiceList.add(p);
	}*/
	
	
	public void placeRouteNamesPanel(ScrollPanel routePanel,
			FlowPanel deviceChoiceList, int smpButtonPos) {
		// int deviceMax = deviceChoiceList.getWidgetCount();
		if (smpButtonPos == (deviceChoiceList.getWidgetCount() - 1)) {
			deviceChoiceList.add(routePanel);
		} else {
			deviceChoiceList.insert(routePanel, smpButtonPos + 1);
		}
	}
	
	public ArrayList<GeoPtModel> getDummyDeviceRoute(){
		ArrayList<GeoPtModel> deviceLocations = new ArrayList<GeoPtModel>();
		GeoPtModel geo = new GeoPtModel();
		geo.setLatitude(25.74626466540882);
		geo.setLongitude(-80.270254611969);
		deviceLocations.add(geo);
		
		geo = new GeoPtModel();
		geo.setLatitude(25.75166174584524);
		geo.setLongitude(-80.27297973632812);
		deviceLocations.add(geo);
		
		geo = new GeoPtModel();
		geo.setLatitude(25.772397395383567);
		geo.setLongitude(-80.25585651397705);
		deviceLocations.add(geo);
		
		geo = new GeoPtModel();
		geo.setLatitude(25.721083103539254);
		geo.setLongitude(-80.2756404876709);
		deviceLocations.add(geo);
		
		geo = new GeoPtModel();
		geo.setLatitude(25.72013585186239);
		geo.setLongitude(-80.2772068977356);
		deviceLocations.add(geo);
		
		geo = new GeoPtModel();
		geo.setLatitude(25.719033937358773);
		geo.setLongitude(-80.27860164642334);
		deviceLocations.add(geo);
		
		return deviceLocations;
		
	}
}
