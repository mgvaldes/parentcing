package com.ing3nia.parentalcontrol.client;

import java.util.ArrayList;

import org.apache.http.client.RedirectHandler;

import com.ibm.icu.impl.CalendarAstronomer.Horizon;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.MenuSetterHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.HelpDeskUserClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.LogoImageClickHandler;
import com.ing3nia.parentalcontrol.client.models.ClientSmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
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

	public static String INVALID_CREDENTIALS = "Invalid username or password, please try again";
	public static String LOADING_LOGIN = "Authenticating user";
	public Image loadingImage;
	
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
				
				TextBox emailfield = loginUI.getEmailField();
				PasswordTextBox passfield = loginUI.getPassField();
				
				emailfield.setText(remUser);
				emailfield.setSelectionRange(0, emailfield.getText().length());
				passfield.setText(remPass);
				passfield.setSelectionRange(0, passfield.getText().length());
			}
			
		}else{
			//TODO retrieve user details and start the thing
			ClientUserModel userModel = new ClientUserModel(); 
			RootPanel.get().clear();
			loadPCAdmin(userModel);
		}
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
		  
		  NavigationHandler navHandler = new NavigationHandler(baseViewHandler);
		  navHandler.setDashboardNavigation(baseViewHandler.getBaseBinder().getNavigationPanel());
		  
		  LogoImageClickHandler logoClickHandler = new LogoImageClickHandler(baseViewHandler);
		  pcbase.getPclogo().addClickHandler(logoClickHandler);
 
		  //TODO ask for user type
		  HelpDeskUserClickHandler helpDeskClickHandler =  new HelpDeskUserClickHandler(baseViewHandler);
		  helpDeskClickHandler.setUserHelpDeskClickHandlers();
		  pcbase.getHelpDesk().addClickHandler(helpDeskClickHandler);
	  
		  baseViewHandler.initBaseView();
		  baseViewHandler.initDashboard();
		  
		  baseViewHandler.setAddAdministratorButton();
		  baseViewHandler.setLogoutButton();
		  baseViewHandler.setAdminUserListView();
		  baseViewHandler.setNewAdminUserViewHandler();
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
				CookieHandler.setPCCookie("us3rp4r3nt4lc00k13");
				if(pclogin.getRememberMeBox().isEnabled()){
					CookieHandler.setCredentialsRemember(pclogin.getEmailField().getText(), pclogin.getPassField().getText());
				}
				
				RootPanel.get().clear();
				loadPCAdmin(userModel);
			}else{
				pclogin.getLoadingBlock().clear();
				pclogin.getNotice().setText(INVALID_CREDENTIALS);
			}
		}
	}

	public Image getLoadingImage() {
		return loadingImage;
	}

	public void setLoadingImage(Image loadingImage) {
		this.loadingImage = loadingImage;
	}
	
}
