package com.ing3nia.parentalcontrol.client;

import java.util.ArrayList;

import com.ibm.icu.impl.CalendarAstronomer.Horizon;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.models.ClientSmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.panels.PCDockLayoutPanel;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;
import com.ing3nia.parentalcontrol.client.views.TicketListView;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.services.models.UserModel;
import com.ing3nia.parentalcontrol.shared.FieldVerifier;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParentalControl implements EntryPoint {

	/**
	 * Entry point method.
	 */  
	public void onModuleLoad() {

		
		  PCBaseUIBinder pcbase = new PCBaseUIBinder();
		  RootPanel.get().add(pcbase);
		  
		  ClientUserModel user = getDummyUser(); BaseViewHandler
		  baseViewHandler = new BaseViewHandler(pcbase);
		  baseViewHandler.setUser(user);
		  baseViewHandler.setSlist(getDummySmartphoneList());
		  
		  LogoImageClickHandler logoClickHandler = new LogoImageClickHandler(baseViewHandler);
		  pcbase.getPclogo().addClickHandler(logoClickHandler);

		  HelpDeskUserClickHandler helpDeskClickHandler =  new HelpDeskUserClickHandler(baseViewHandler);
		  pcbase.getHelpDesk().addClickHandler(helpDeskClickHandler);
	  
		  baseViewHandler.initBaseView();
		  baseViewHandler.initDashboard();
		  
		  baseViewHandler.setAddAdministratorButton();
		  baseViewHandler.setAdminUserListView();
		  baseViewHandler.setNewAdminUserViewHandler();
		 
	}

	private ClientUserModel getDummyUser() {
		ClientUserModel user = new ClientUserModel();
		user.setName("Nombre de usuario");
		user.setEmail("javierfdr@gmail.com");
		return user;
	}

	private ArrayList<ClientSmartphoneModel> getDummySmartphoneList() {
		ArrayList<ClientSmartphoneModel> slist = new ArrayList<ClientSmartphoneModel>();
		ClientSmartphoneModel s1 = new ClientSmartphoneModel();
		s1.setName("Michael's iPhone");

		ClientSmartphoneModel s2 = new ClientSmartphoneModel();
		s2.setName("John's Android");

		ClientSmartphoneModel s3 = new ClientSmartphoneModel();
		s3.setName("Julie");

		slist.add(s1);
		slist.add(s2);
		slist.add(s3);

		return slist;
	}
	
	public class LogoImageClickHandler implements ClickHandler{

		private BaseViewHandler baseViewHandler;
		public LogoImageClickHandler(BaseViewHandler baseViewHandler){
			this.baseViewHandler = baseViewHandler;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			this.baseViewHandler.initDashboard();		
		}	
	}
	
	public class HelpDeskUserClickHandler implements ClickHandler{

		private HTMLPanel centerContent;
		private MenuSetterHandler menuSetter;
		private BaseViewHandler baseViewHandler;
		
		public HelpDeskUserClickHandler(BaseViewHandler baseViewHandler){
			this.baseViewHandler = baseViewHandler;
			this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
			this.menuSetter = baseViewHandler.getMenuSetter();
		}
		
		@Override
		public void onClick(ClickEvent event) {
			this.centerContent.clear();
			this.menuSetter.clearMenuOptions();
			
			baseViewHandler.initTicketCenterMenu();
			baseViewHandler.toggleTicketCenterMenu(CenterMenuOptionsClassNames.TicketList);
			
			TicketListView view = new TicketListView(centerContent);		
			view.initTicketList();
		}
	}	

}
