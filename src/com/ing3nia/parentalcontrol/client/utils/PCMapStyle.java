package com.ing3nia.parentalcontrol.client.utils;

public class PCMapStyle {
	static PCMapMarkersEnum[] mapMarkersArray = {PCMapMarkersEnum.A,PCMapMarkersEnum.B,PCMapMarkersEnum.C,PCMapMarkersEnum.D,PCMapMarkersEnum.E,PCMapMarkersEnum.F,PCMapMarkersEnum.G,PCMapMarkersEnum.H,PCMapMarkersEnum.I,PCMapMarkersEnum.J,PCMapMarkersEnum.K,PCMapMarkersEnum.L,PCMapMarkersEnum.M,PCMapMarkersEnum.N,PCMapMarkersEnum.O,PCMapMarkersEnum.P,PCMapMarkersEnum.Q,PCMapMarkersEnum.V,PCMapMarkersEnum.W,PCMapMarkersEnum.X,PCMapMarkersEnum.Y,PCMapMarkersEnum.Z};
	static String markerImagesURL = "/media/images/markerIcons";
	
	public static PCMapMarkersEnum getMapMarker(Integer smartphoneIndex){
		smartphoneIndex%=22;
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
