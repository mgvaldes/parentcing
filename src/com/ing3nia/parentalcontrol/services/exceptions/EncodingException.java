package com.ing3nia.parentalcontrol.services.exceptions;

/**
 * Exception to be thrown when encoding problems are found
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public class EncodingException extends Exception{
	public EncodingException(){
		super();
	}
	
	public EncodingException(String message){
		super(message);
	}
}
