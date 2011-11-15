package com.ing3nia.parentalcontrol.client.views.classnames;

public enum DeviceAlertListViewClassName {
	DeviceAlertTable("deviceAlertTable"),
	DeviceAlertTableRow("deviceAlertTableRow");
	
	private String classname;
	
	private DeviceAlertListViewClassName(String classname) {
		this.classname = classname;
	}
	
	public String getClassname(){
		return this.classname;
	}
}
