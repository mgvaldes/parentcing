package com.ing3nia.parentalcontrol.client;

import java.util.ArrayList;

import org.apache.http.client.RedirectHandler;

import com.ibm.icu.impl.CalendarAstronomer.Horizon;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.HelpDeskUserClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.LogoImageClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.LoginRegisterButtonClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.SignInButtonClickHandler;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.panels.PCDockLayoutPanel;
import com.ing3nia.parentalcontrol.client.utils.CookieHandler;
import com.ing3nia.parentalcontrol.client.utils.NavigationHandler;
import com.ing3nia.parentalcontrol.client.views.DeviceMapView;
import com.ing3nia.parentalcontrol.client.views.LoadingView;
import com.ing3nia.parentalcontrol.client.views.LoginView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParentalControl implements EntryPoint {

	public Image loadingImage;
	//public ClientUserModel userModel; 
	
	/**
	 * Entry point method.
	 */  
	public void onModuleLoad() {
		
		loadingImage = new Image("/media/images/loading.gif");
		loadPage();
	}
	
	public void loadPage(){
		
		// Check if user session is active
		String userCookie = CookieHandler.getPCCookie();
		
		if (userCookie == null) {			
			PCLoginUIBinder loginUI = new PCLoginUIBinder();
			RootPanel.get().add(loginUI);
			ClientUserModel userModel = new ClientUserModel();
			SignInButtonClickHandler signInHandler =  new SignInButtonClickHandler(loginUI, userModel, loadingImage);
			loginUI.getSignInButton().addClickHandler(signInHandler);
			
			LoginRegisterButtonClickHandler registerHandler = new LoginRegisterButtonClickHandler(loadingImage);
			loginUI.getRegisterButton().addClickHandler(registerHandler);
			
			//If remembered credentials 
			String remUser = CookieHandler.getMailRemeberedCredential();
			String remPass = CookieHandler.getPasswordRemeberedCredential();
			
			if (remUser !=null && remPass !=null) {
				
				TextBox emailfield = loginUI.getEmailField();
				PasswordTextBox passfield = loginUI.getPassField();
				
				emailfield.setText(remUser);
				emailfield.setSelectionRange(0, emailfield.getText().length());
				passfield.setText(remPass);
				passfield.setSelectionRange(0, passfield.getText().length());
			}
		}
		else{
			//TODO retrieve user details and start the thing
			/*if(userModel==null){
				userModel = new ClientUserModel();
			} */
			ClientUserModel userModel=new ClientUserModel();
			RootPanel.get().clear();
			loadPCAdmin(userModel);
		}
	}

	private ClientUserModel getDummyUser() {
		ClientUserModel user = new ClientUserModel();
		user.setUsername("javierfdr@gmail.com");
		return user;
	}

	private ArrayList<SmartphoneModel> getDummySmartphoneList() {
		/*
		ArrayList<SmartphoneModel> slist = new ArrayList<SmartphoneModel>();
		SmartphoneModel s1 = new SmartphoneModel();
		s1.setName("Michael's iPhone");

		SmartphoneModel s2 = new SmartphoneModel();
		s2.setName("John's Android");

		SmartphoneModel s3 = new SmartphoneModel();
		s3.setName("Julie");

		slist.add(s1);
		slist.add(s2);
		slist.add(s3);
		 */
		return new ArrayList<SmartphoneModel>();
	}
	
	public static void loadPCAdmin(ClientUserModel usermodel){
		  PCBaseUIBinder pcbase = new PCBaseUIBinder();
		  RootPanel.get().add(pcbase);
		  
		  ClientUserModel user = usermodel;
		  
		  BaseViewHandler baseViewHandler = new BaseViewHandler(pcbase);
		  baseViewHandler.setUser(user);
		  baseViewHandler.setSlist(user.getSmartphones());
		 
		  //baseViewHandler.setSlist(getDummySmartphoneList());
		  
		  NavigationHandler navHandler = new NavigationHandler(baseViewHandler);
		  navHandler.setDashboardNavigation(baseViewHandler.getBaseBinder().getNavigationPanel());
		  
		  LogoImageClickHandler logoClickHandler = new LogoImageClickHandler(baseViewHandler);
		  pcbase.getPclogo().addClickHandler(logoClickHandler);
 
		  //TODO ask for user type
		  HelpDeskUserClickHandler helpDeskClickHandler =  new HelpDeskUserClickHandler(baseViewHandler, false);
		  helpDeskClickHandler.setUserHelpDeskClickHandlers();
		  pcbase.getHelpDesk().addClickHandler(helpDeskClickHandler);
	  
		  baseViewHandler.initBaseView();
		  baseViewHandler.initDashboard();
		  
		  baseViewHandler.setAddAdministratorButton();
		  baseViewHandler.setLogoutButton();
		  baseViewHandler.setAdminUserListView();
		  baseViewHandler.setNewAdminUserViewHandler();
	}


	public Image getLoadingImage() {
		return loadingImage;
	}

	public void setLoadingImage(Image loadingImage) {
		this.loadingImage = loadingImage;
	}

}
