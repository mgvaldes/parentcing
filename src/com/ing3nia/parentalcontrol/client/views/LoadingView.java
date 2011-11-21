package com.ing3nia.parentalcontrol.client.views;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;

public class LoadingView{
	
	public static void setLoadingView(PCLoginUIBinder pclogin, String text, Image loadingImage){
		FlowPanel loadingView = pclogin.getLoadingBlock();
		loadingView.setStyleName("loadingView");
		loadingView.add(loadingImage);
		Label loadingLabel = new Label(text);
		loadingLabel.setStyleName("loadingLabel");
		loadingView.add(loadingLabel);
	}
}
