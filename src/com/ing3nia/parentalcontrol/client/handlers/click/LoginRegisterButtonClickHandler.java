package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.ing3nia.parentalcontrol.client.PCRegisterUIBinder;

public class LoginRegisterButtonClickHandler implements ClickHandler {
	
	private Image loadingImage;

	public LoginRegisterButtonClickHandler(Image loadingImage) {
		this.loadingImage = loadingImage;
	}

	@Override
	public void onClick(ClickEvent event) {
		PCRegisterUIBinder registerUI = new PCRegisterUIBinder();
		
		RegisterButtonClickHandler registerHandler = new RegisterButtonClickHandler(registerUI, this.loadingImage);
		registerUI.getRegisterButton().addClickHandler(registerHandler);
		
		RootPanel.get().clear();
		RootPanel.get().add(registerUI);
	}
}
