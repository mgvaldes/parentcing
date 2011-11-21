package com.ing3nia.parentalcontrol.client;

import java.util.ArrayList;

import com.ibm.icu.impl.CalendarAstronomer.Horizon;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.HelpDeskUserClickHandler;
import com.ing3nia.parentalcontrol.client.models.ClientSmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.panels.PCDockLayoutPanel;
import com.ing3nia.parentalcontrol.client.utils.CookieHandler;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;
import com.ing3nia.parentalcontrol.client.views.LoadingView;
import com.ing3nia.parentalcontrol.client.views.LoginView;
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
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParentalControl implements EntryPoint {

	public static String INVALID_CREDENTIALS = "Invalid username or password, please try again";
	public static String LOADING_LOGIN = "Authenticating user";
	
	/**
	 * Entry point method.
	 */  
	public void onModuleLoad() {
		
		Image loadingImage = new Image("/media/images/loading.gif");
		
		// Check if user session is active
		String userCookie = CookieHandler.getPCCookie();
		if(userCookie == null){
			
			PCLoginUIBinder loginUI = new PCLoginUIBinder();
			RootPanel.get().add(loginUI);
			ClientUserModel userModel = new ClientUserModel();
			SignInButtonClickHandler signInHandler =  new SignInButtonClickHandler(loginUI, userModel, loadingImage);
			loginUI.getSignInButton().addClickHandler(signInHandler);
			
			//If remembered credentials 
			String remUser = CookieHandler.getMailRemeberedCredential();
			String remPass = CookieHandler.getPasswordRemeberedCredential();
			if(remUser !=null && remPass !=null){
				loginUI.getEmailField().setText(remUser);
				loginUI.getPassField().setText(remPass);
			}
			
		}else{
			//TODO retrieve user details and start the thing
			ClientUserModel userModel = new ClientUserModel(); 
			RootPanel.get().clear();
			loadPCAdmin(userModel);
		}

		
		/*
		  PCBaseUIBinder pcbase = new PCBaseUIBinder();
		  RootPanel.get().add(pcbase);
		  
		  ClientUserModel user = getDummyUser(); 
		  BaseViewHandler baseViewHandler = new BaseViewHandler(pcbase);
		  baseViewHandler.setUser(user);
		  baseViewHandler.setSlist(getDummySmartphoneList());
		  
		  LogoImageClickHandler logoClickHandler = new LogoImageClickHandler(baseViewHandler);
		  pcbase.getPclogo().addClickHandler(logoClickHandler);

		  //TODO ask for user type
		  HelpDeskUserClickHandler helpDeskClickHandler =  new HelpDeskUserClickHandler(baseViewHandler);
		  helpDeskClickHandler.setUserHelpDeskClickHandlers();
		  pcbase.getHelpDesk().addClickHandler(helpDeskClickHandler);
	  
		  baseViewHandler.initBaseView();
		  baseViewHandler.initDashboard();
		  
		  baseViewHandler.setAddAdministratorButton();
		  baseViewHandler.setAdminUserListView();
		  baseViewHandler.setNewAdminUserViewHandler();
		  */
		 
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
	
	public void loadPCAdmin(ClientUserModel usermodel){
		  PCBaseUIBinder pcbase = new PCBaseUIBinder();
		  RootPanel.get().add(pcbase);
		  
		  ClientUserModel user = getDummyUser(); 
		  BaseViewHandler baseViewHandler = new BaseViewHandler(pcbase);
		  baseViewHandler.setUser(user);
		  baseViewHandler.setSlist(getDummySmartphoneList());
		  
		  LogoImageClickHandler logoClickHandler = new LogoImageClickHandler(baseViewHandler);
		  pcbase.getPclogo().addClickHandler(logoClickHandler);

		  //TODO ask for user type
		  HelpDeskUserClickHandler helpDeskClickHandler =  new HelpDeskUserClickHandler(baseViewHandler);
		  helpDeskClickHandler.setUserHelpDeskClickHandlers();
		  pcbase.getHelpDesk().addClickHandler(helpDeskClickHandler);
	  
		  baseViewHandler.initBaseView();
		  baseViewHandler.initDashboard();
		  
		  baseViewHandler.setAddAdministratorButton();
		  baseViewHandler.setAdminUserListView();
		  baseViewHandler.setNewAdminUserViewHandler();
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
	
	public class SignInButtonClickHandler implements ClickHandler{
		
		PCLoginUIBinder pclogin;
		ClientUserModel userModel;
		Image loadingImage;
		public SignInButtonClickHandler(PCLoginUIBinder pclogin, ClientUserModel userModel, Image loadinImage){
			this.pclogin = pclogin;
			this.userModel = userModel;
			this.loadingImage = loadinImage;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			String email = pclogin.getEmailField().getText();
			String pass = pclogin.getPassField().getText();
			LoadingView.setLoadingView(pclogin, LOADING_LOGIN, loadingImage);
			
			boolean validSignIn = LoginView.validateUserName(email, pass, userModel);

			if(validSignIn){
				RootPanel.get().clear();
				loadPCAdmin(userModel);
			}else{
				CookieHandler.setPCCookie("us3rp4r3nt4lc00k13");
				if(pclogin.getRememberMeBox().isEnabled()){
					CookieHandler.setCredentialsRemember(pclogin.getEmailField().getText(), pclogin.getPassField().getText());
				}
				pclogin.getLoadingBlock().clear();
				pclogin.getNotice().setText(INVALID_CREDENTIALS);
			}
		}
		
	}

}
