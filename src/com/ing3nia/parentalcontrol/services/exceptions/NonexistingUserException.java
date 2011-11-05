package com.ing3nia.parentalcontrol.services.exceptions;

/**
 * Exception to be thrown when a user cannot be found by some given  parameters
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public class NonexistingUserException extends Exception{
	public NonexistingUserException(){
		super();
	}
	
	public NonexistingUserException(String message){
		super(message);
	}
}