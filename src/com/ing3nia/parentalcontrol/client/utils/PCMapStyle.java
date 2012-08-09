package com.ing3nia.parentalcontrol.client.utils;

public class PCMapStyle {
//	static PCMapMarkersEnum[] mapMarkersArray = {PCMapMarkersEnum.A,PCMapMarkersEnum.B,PCMapMarkersEnum.C,PCMapMarkersEnum.D,PCMapMarkersEnum.E,PCMapMarkersEnum.F,PCMapMarkersEnum.G,PCMapMarkersEnum.H,PCMapMarkersEnum.I,PCMapMarkersEnum.J,PCMapMarkersEnum.K,PCMapMarkersEnum.L,PCMapMarkersEnum.M,PCMapMarkersEnum.N,PCMapMarkersEnum.O,PCMapMarkersEnum.P,PCMapMarkersEnum.Q,PCMapMarkersEnum.V,PCMapMarkersEnum.W,PCMapMarkersEnum.X,PCMapMarkersEnum.Y,PCMapMarkersEnum.Z};
	static PCMapMarkersEnum[] mapMarkersArray = {PCMapMarkersEnum.M000,PCMapMarkersEnum.M001,PCMapMarkersEnum.M002,PCMapMarkersEnum.M003,PCMapMarkersEnum.M004,PCMapMarkersEnum.M005,PCMapMarkersEnum.M006,PCMapMarkersEnum.M007,PCMapMarkersEnum.M008,PCMapMarkersEnum.M009,PCMapMarkersEnum.M010,PCMapMarkersEnum.M011,PCMapMarkersEnum.M012,PCMapMarkersEnum.M013,PCMapMarkersEnum.M014,PCMapMarkersEnum.M015,PCMapMarkersEnum.M016,PCMapMarkersEnum.M017,PCMapMarkersEnum.M018,PCMapMarkersEnum.M019,PCMapMarkersEnum.M020,PCMapMarkersEnum.M021,PCMapMarkersEnum.M022,PCMapMarkersEnum.M023,PCMapMarkersEnum.M024,PCMapMarkersEnum.M025,PCMapMarkersEnum.M026,PCMapMarkersEnum.M027,PCMapMarkersEnum.M028,PCMapMarkersEnum.M029,PCMapMarkersEnum.M030,PCMapMarkersEnum.M031,PCMapMarkersEnum.M032,PCMapMarkersEnum.M033,PCMapMarkersEnum.M034,PCMapMarkersEnum.M035,PCMapMarkersEnum.M036,PCMapMarkersEnum.M037,PCMapMarkersEnum.M038,PCMapMarkersEnum.M039,PCMapMarkersEnum.M040,PCMapMarkersEnum.M041,PCMapMarkersEnum.M042,PCMapMarkersEnum.M043,PCMapMarkersEnum.M044,PCMapMarkersEnum.M045,PCMapMarkersEnum.M046,PCMapMarkersEnum.M047,PCMapMarkersEnum.M048,PCMapMarkersEnum.M049,PCMapMarkersEnum.M050,PCMapMarkersEnum.M051,PCMapMarkersEnum.M052,PCMapMarkersEnum.M053,PCMapMarkersEnum.M054,PCMapMarkersEnum.M055,PCMapMarkersEnum.M056,PCMapMarkersEnum.M057,PCMapMarkersEnum.M058,PCMapMarkersEnum.M059,PCMapMarkersEnum.M060,PCMapMarkersEnum.M061,PCMapMarkersEnum.M062,PCMapMarkersEnum.M063,PCMapMarkersEnum.M064,PCMapMarkersEnum.M065,PCMapMarkersEnum.M066,PCMapMarkersEnum.M067,PCMapMarkersEnum.M068,PCMapMarkersEnum.M069,PCMapMarkersEnum.M070,PCMapMarkersEnum.M071,PCMapMarkersEnum.M072,PCMapMarkersEnum.M073,PCMapMarkersEnum.M074,PCMapMarkersEnum.M075,PCMapMarkersEnum.M076,PCMapMarkersEnum.M077,PCMapMarkersEnum.M078,PCMapMarkersEnum.M079,PCMapMarkersEnum.M080,PCMapMarkersEnum.M081,PCMapMarkersEnum.M082,PCMapMarkersEnum.M083,PCMapMarkersEnum.M084,PCMapMarkersEnum.M085,PCMapMarkersEnum.M086,PCMapMarkersEnum.M087,PCMapMarkersEnum.M088,PCMapMarkersEnum.M089,PCMapMarkersEnum.M090,PCMapMarkersEnum.M091,PCMapMarkersEnum.M092,PCMapMarkersEnum.M093,PCMapMarkersEnum.M094,PCMapMarkersEnum.M095,PCMapMarkersEnum.M096,PCMapMarkersEnum.M097,PCMapMarkersEnum.M098,PCMapMarkersEnum.M099,PCMapMarkersEnum.M100};
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
