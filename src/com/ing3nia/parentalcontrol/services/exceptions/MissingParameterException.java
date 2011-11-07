package com.ing3nia.parentalcontrol.services.exceptions;

public class MissingParameterException extends Exception {
	public MissingParameterException(){
		super();
	}
	
	public MissingParameterException(String message){
		super(message);
	}
}
