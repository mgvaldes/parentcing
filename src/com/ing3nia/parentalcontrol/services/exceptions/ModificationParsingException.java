package com.ing3nia.parentalcontrol.services.exceptions;

/**
 * Exception to be thrown when parsing modification problems are found
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public class ModificationParsingException extends Exception{
	public ModificationParsingException(){
		super();
	}
	
	public ModificationParsingException(String message){
		super(message);
	}
}
