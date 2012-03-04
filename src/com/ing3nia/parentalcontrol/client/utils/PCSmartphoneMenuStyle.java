package com.ing3nia.parentalcontrol.client.utils;

public class PCSmartphoneMenuStyle {
	static PCSmartphoneIconsEnum[] smartphoneIconArray = {
			PCSmartphoneIconsEnum.A, PCSmartphoneIconsEnum.B,
			PCSmartphoneIconsEnum.C, PCSmartphoneIconsEnum.D,
			PCSmartphoneIconsEnum.E, PCSmartphoneIconsEnum.F,
			PCSmartphoneIconsEnum.G, PCSmartphoneIconsEnum.H,
			PCSmartphoneIconsEnum.I, PCSmartphoneIconsEnum.J,
			PCSmartphoneIconsEnum.K, PCSmartphoneIconsEnum.L,
			PCSmartphoneIconsEnum.M, PCSmartphoneIconsEnum.N,
			PCSmartphoneIconsEnum.M, PCSmartphoneIconsEnum.O,
			PCSmartphoneIconsEnum.P, PCSmartphoneIconsEnum.Q,
			PCSmartphoneIconsEnum.R, PCSmartphoneIconsEnum.S,
			PCSmartphoneIconsEnum.T, PCSmartphoneIconsEnum.U,
			PCSmartphoneIconsEnum.V, PCSmartphoneIconsEnum.W,
			PCSmartphoneIconsEnum.X, PCSmartphoneIconsEnum.Y,
			PCSmartphoneIconsEnum.Z };
	static String smartphoneIconURL = "/media/images/smarpthoneIcons";

	public static PCSmartphoneIconsEnum getSmartphoneMarker(
			Integer smartphoneIndex) {
		return smartphoneIconArray[smartphoneIndex];
	}

	public static Integer gerSmartphoneIconArrayLength() {
		return smartphoneIconArray.length;
	}

	public static String getIconImageURL(Integer smartphoneIndex) {
		PCSmartphoneIconsEnum smartphoneIcon = getSmartphoneMarker(smartphoneIndex);
		return smartphoneIconURL + "/" + smartphoneIcon.getImageName();
	}
}