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
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.views.DeviceDailyRouteView;

public class DailyRouteClickHandler implements ClickHandler{

	private String key;
	private PCBaseUIBinder baseBinder;
	private HTMLPanel centerContent;
	private MenuSetterHandler menuSetter;
	private FlowPanel deviceChoiceList;
	private static Logger logger = Logger.getLogger("Daily Route Logger");
	
	public DailyRouteClickHandler(String key, PCBaseUIBinder basebinder, HTMLPanel centerContent, MenuSetterHandler menuSetter, FlowPanel deviceChoiceList){
		this.key = key;
		this.baseBinder = basebinder;
		this.centerContent = centerContent;
		this.menuSetter = menuSetter;
		this.deviceChoiceList = deviceChoiceList;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.centerContent.clear();
		this.menuSetter.clearMenuOptions();

		FlowPanel menuOptions = this.menuSetter.getCenterMenuOptions();
		menuOptions.add(this.menuSetter.getDailyRoute());
		this.menuSetter.getDailyRoute().setStyleName("selectedShinnyButton");
		menuOptions.add(this.menuSetter.getAlertList());
		menuOptions.add(this.menuSetter.getAlertRules());
		menuOptions.add(this.menuSetter.getDeviceContacts());
		menuOptions.add(this.menuSetter.getDeviceSettings());
		
		logger.info("Initializing device route map");
		DeviceDailyRouteView view = new DeviceDailyRouteView(baseBinder.getCenterContent());		
		view.setDeviceRoute(getDummyDeviceRoute());
		view.initDeviceLocationLoad();
		
		logger.info("Devices Loaded. Displaying route Names "+view.getDeviceRouteNames().size()+" ex: "+view.getDeviceRouteNames().get(0));

		displayRouteNames(menuSetter.getParentSmartphoneButton(), deviceChoiceList, view.getDeviceRouteNames());
	}
	

	
	public void displayRouteNames(Button smpButton, FlowPanel deviceChoiceList, List<String> deviceRouteNames){
		if(smpButton==null) return;
		int index= deviceChoiceList.getWidgetIndex(smpButton);
		ScrollPanel p = new ScrollPanel();
		for(String route : deviceRouteNames){
			Label l = new Label(route);
			p.add(l);
		}
		deviceChoiceList.add(p);
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
