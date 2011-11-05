package com.ing3nia.parentalcontrol.services.exceptions;

/**
 * Exception to be thrown when session query problems are got
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public class SessionQueryException extends Exception{
	public SessionQueryException(){
		super();
	}
	
	public SessionQueryException(String message){
		super(message);
	}
}
