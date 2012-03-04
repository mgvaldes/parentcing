package com.ing3nia.parentalcontrol.client.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.PCLoginUIBinder;
import com.ing3nia.parentalcontrol.client.PCRegisterUIBinder;
import com.ing3nia.parentalcontrol.client.utils.LoadingBarImageEnum;
import com.ing3nia.parentalcontrol.client.utils.LoadingBarStyle;

public class LoadingView{
	
	static Image loadingImage = new Image("/media/images/loading.gif");
	
	public static void setLoadingView(PCLoginUIBinder pclogin, String text, Image loadingImage){
		FlowPanel loadingView = pclogin.getLoadingBlock();
		loadingView.clear();
		loadingView.setStyleName("loadingView");
		loadingView.add(loadingImage);
		Label loadingLabel = new Label(text);
		loadingLabel.setStyleName("loadingLabel");
		loadingView.add(loadingLabel);
	}
	
	public static void setLoadingView(PCLoginUIBinder pclogin, String text, LoadingBarImageEnum loadingBar){
		FlowPanel loadingView = pclogin.getLoadingBlock();
		loadingView.clear();
		FlowPanel loadingBarView = LoadingBarStyle.getLoadingBarPanel(text, loadingBar.getId());
		loadingView.add(loadingBarView);
	}
	
	public static void setLoadingView(PCRegisterUIBinder pcregister, String text, Image loadingImage){
		FlowPanel loadingView = pcregister.getLoadingBlock();
		loadingView.clear();
		loadingView.setStyleName("loadingView");
		loadingView.add(loadingImage);
		Label loadingLabel = new Label(text);
		loadingLabel.setStyleName("loadingLabel");
		loadingView.add(loadingLabel);
	}
	
	public static void clearLoadingView(PCLoginUIBinder pclogin){
		pclogin.getLoadingBlock().clear();
	}
	
	public static void clearLoadingView(PCRegisterUIBinder pcregister){
		pcregister.getLoadingBlock().clear();
	}
	
	public static void changeLoadingMessage(PCLoginUIBinder pclogin, String text){
		FlowPanel loadingView = pclogin.getLoadingBlock();
		Label loadingText = (Label) loadingView.getWidget(1);
		loadingText.setText(text);
	}
	
	public static void setLoadingView(PCBaseUIBinder pcbase, String text, Image loadingImage){
		FlowPanel loadingView = pcbase.getLoadingBlock();
		loadingView.clear();
		loadingView.setStyleName("loadingView");
		loadingView.add(loadingImage);
		Label loadingLabel = new Label(text);
		loadingLabel.setStyleName("loadingLabel");
		loadingView.add(loadingLabel);
	}
	
	
	public static void clearLoadingView(PCBaseUIBinder pcbase){
		pcbase.getLoadingBlock().clear();
	}
	
	public static void changeLoadingMessage(PCBaseUIBinder pcbase, String text){
		FlowPanel loadingView = pcbase.getLoadingBlock();
		Label loadingText = (Label) loadingView.getWidget(1);
		loadingText.setText(text);
	}
}
