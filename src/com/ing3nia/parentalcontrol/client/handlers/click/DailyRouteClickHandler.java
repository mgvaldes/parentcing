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
import com.ing3nia.parentalcontrol.client.models.LocationModel;
import com.ing3nia.parentalcontrol.client.models.RouteModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
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
	private static int ROUTE_MAX_SIZE = 100;
	
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
		view.setDeviceRoute(getDeviceRoute());
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
	
	public ArrayList<GeoPtModel> getDeviceRoute(){
		
		logger.info("Getting Device ROUTE");
		ArrayList<GeoPtModel> deviceLocations = new ArrayList<GeoPtModel>();
		
		/*
		if(true){
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
			
			geo = new GeoPtModel();
			geo.setLatitude(25.759033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			
			geo = new GeoPtModel();
			geo.setLatitude(25.799033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			
			geo = new GeoPtModel();
			geo.setLatitude(25.89033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			
			geo = new GeoPtModel();
			geo.setLatitude(25.519033937358773);
			geo.setLongitude(-80.29860164642334);
			deviceLocations.add(geo);
			
			geo = new GeoPtModel();
			geo.setLatitude(25.919033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			geo = new GeoPtModel();
			geo.setLatitude(25.759033937358773);
			geo.setLongitude(-80.29860164642334);
			deviceLocations.add(geo);
			
			geo = new GeoPtModel();
			geo.setLatitude(25.749033937358773);
			geo.setLongitude(-80.27860164642334);
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
			
			geo = new GeoPtModel();
			geo.setLatitude(25.759033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			
			geo = new GeoPtModel();
			geo.setLatitude(25.799033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			
			geo = new GeoPtModel();
			geo.setLatitude(25.89033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			
			geo = new GeoPtModel();
			geo.setLatitude(25.519033937358773);
			geo.setLongitude(-80.29860164642334);
			deviceLocations.add(geo);
			
			geo = new GeoPtModel();
			geo.setLatitude(25.919033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			geo = new GeoPtModel();
			geo.setLatitude(25.759033937358773);
			geo.setLongitude(-80.29860164642334);
			deviceLocations.add(geo);
			
			geo = new GeoPtModel();
			geo.setLatitude(25.749033937358773);
			geo.setLongitude(-80.27860164642334);
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
			
			geo = new GeoPtModel();
			geo.setLatitude(25.759033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			
			geo = new GeoPtModel();
			geo.setLatitude(25.799033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			
			geo = new GeoPtModel();
			geo.setLatitude(25.89033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			
			geo = new GeoPtModel();
			geo.setLatitude(25.519033937358773);
			geo.setLongitude(-80.29860164642334);
			deviceLocations.add(geo);
			
			geo = new GeoPtModel();
			geo.setLatitude(25.919033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			geo = new GeoPtModel();
			geo.setLatitude(25.759033937358773);
			geo.setLongitude(-80.29860164642334);
			deviceLocations.add(geo);
			
			geo = new GeoPtModel();
			geo.setLatitude(25.749033937358773);
			geo.setLongitude(-80.27860164642334);
			deviceLocations.add(geo);
			
			return deviceLocations;

		}
		
		*/
		int smartphoneIndex = this.baseView.getClickedSmartphoneIndex();
		SmartphoneModel smartphone = this.baseView.getUser().getSmartphones().get(smartphoneIndex);
		ArrayList<RouteModel> routes = smartphone.getRoutes();
		
		if(smartphone.getRoutes() == null){
			logger.warning("Smartphone ROUTE list is null");
			GeoPtModel geopt = new GeoPtModel();
			LocationModel currentLocation = smartphone.getLocation();
			geopt.setLatitude(Double.valueOf(currentLocation.getLatitude()));
			geopt.setLongitude(Double.valueOf(currentLocation.getLongitude()));
			
			deviceLocations.add(geopt);
			
			return deviceLocations;
		}
		if(smartphone.getRoutes().size() ==0){
			logger.warning("Smartphone Route is empty");
			GeoPtModel geopt = new GeoPtModel();
			LocationModel currentLocation = smartphone.getLocation();
			geopt.setLatitude(Double.valueOf(currentLocation.getLatitude()));
			geopt.setLongitude(Double.valueOf(currentLocation.getLongitude()));
			
			deviceLocations.add(geopt);
			
			return deviceLocations;
		}
		RouteModel route = routes.get(routes.size()-1);
		logger.info("Iterating last route "+route.getDate());
		
		ArrayList<LocationModel> points = route.getPoints();
		int routeSize = points.size();
		ArrayList<Integer> pointDistribution = getPointDistribution(routeSize, ROUTE_MAX_SIZE);
		
		for(Integer pos : pointDistribution){
			LocationModel location = points.get(pos); 
			GeoPtModel geo = new GeoPtModel();
			Number lat = Float.valueOf(location.getLatitude());
			Number lon = Float.valueOf(location.getLongitude());
			
			geo.setLatitude(lat.doubleValue());
			geo.setLongitude(lon.doubleValue());
			
			deviceLocations.add(geo);
		}
		
		/*
		for(LocationModel location : route.getPoints()){
			GeoPtModel geo = new GeoPtModel();
			Number lat = Float.valueOf(location.getLatitude());
			Number lon = Float.valueOf(location.getLongitude());
			
			geo.setLatitude(lat.doubleValue());
			geo.setLongitude(lon.doubleValue());
			
			deviceLocations.add(geo);
		}*/
	
		return deviceLocations;
		
	}
	
	public ArrayList<Integer> getPointDistribution(int routeSize, int maxSize){
		ArrayList<Integer> pointDistribution = new ArrayList<Integer>(maxSize);

		if(routeSize < maxSize){
			int count=0;
			while(count<routeSize){
				pointDistribution.add(count);
				count++;
			}
			return pointDistribution;
		}
		
		// if maxSize is greater or equals maxSize
		double sep = routeSize/maxSize;
		double posDouble = 0.0;
		int nextPos = 0;
		
		while(nextPos < routeSize){
			pointDistribution.add(nextPos);
			posDouble +=sep;
			nextPos = (int) posDouble;
		}
		
		return pointDistribution;
	}
}
