package com.ing3nia.parentalcontrol.services.models;

import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;

public class RegisterSmartphoneModel {
	private String usr;
	
	private String pass;
	
	private SmartphoneModel smartphone;

	RegisterSmartphoneModel() {
		
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public SmartphoneModel getSmartphone() {
		return smartphone;
	}

	public void setSmartphone(SmartphoneModel smartphone) {
		this.smartphone = smartphone;
	}
}
