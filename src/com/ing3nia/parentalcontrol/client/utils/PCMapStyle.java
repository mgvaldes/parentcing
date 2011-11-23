package com.ing3nia.parentalcontrol.client.utils;

public class PCMapStyle {
	static PCMapMarkersEnum[] mapMarkersArray = {PCMapMarkersEnum.A,PCMapMarkersEnum.B,PCMapMarkersEnum.C,PCMapMarkersEnum.D,PCMapMarkersEnum.E,PCMapMarkersEnum.F,PCMapMarkersEnum.G,PCMapMarkersEnum.H,PCMapMarkersEnum.I,PCMapMarkersEnum.J,PCMapMarkersEnum.K};
	static String markerImagesURL = "/media/images/markerIcons";
	public static PCMapMarkersEnum getMapMarker(Integer smartphoneIndex){
		return mapMarkersArray[smartphoneIndex];
	}
	
	public static Integer getMapMarkersLength(){
		return mapMarkersArray.length;
	}
	
	public static String getMarkerImageURL(Integer smartphoneIndex){
		PCMapMarkersEnum mapMarker = getMapMarker(smartphoneIndex);
		return markerImagesURL+"/"+mapMarker.getImageName();
	}
}
