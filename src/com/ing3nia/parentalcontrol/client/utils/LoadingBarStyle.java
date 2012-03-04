package com.ing3nia.parentalcontrol.client.utils;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class LoadingBarStyle {

	static LoadingBarImageEnum[] loadingBarImageEnum = {
		LoadingBarImageEnum.STAGE1,
		LoadingBarImageEnum.STAGE2,
		LoadingBarImageEnum.STAGE3
	};
	static String loadingBarImageBaseURL = "/media/images/loadingbar";

	public static LoadingBarImageEnum getLoadingBarStageImage(
			Integer stageIndex) {
		return loadingBarImageEnum[stageIndex];
	}

	public static String getLoadingImageURL(Integer stageIndex) {
		LoadingBarImageEnum loadingBarImage = getLoadingBarStageImage(stageIndex-1);
		return loadingBarImageBaseURL + "/" + loadingBarImage.getImageName();
	}
	
	public static FlowPanel getLoadingBarPanel(String loadingText, Integer stageIndex){
		FlowPanel loadingPanel = new FlowPanel();
		loadingPanel.setStyleName("loadingBarBlock");
		
		Label textLabel = new Label();
		textLabel.setText(loadingText);
		textLabel.setStyleName("loadingLabel");
		
		Image barImage = new Image(getLoadingImageURL(stageIndex));
		loadingPanel.add(textLabel);
		loadingPanel.add(barImage);
		
		return loadingPanel;
		
	}
	
}
