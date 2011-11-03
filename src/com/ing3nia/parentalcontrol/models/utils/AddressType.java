package com.ing3nia.parentalcontrol.models.utils;

/**
 * 
 * @author gaby
 *
 */
public enum AddressType {
	WORK(1),
	PARTICULAR(2);
	
	private int type;
	
	AddressType(int type) {
		this.type = type;
	}
}
