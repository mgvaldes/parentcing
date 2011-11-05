package com.ing3nia.parentalcontrol.services.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;

import com.ing3nia.parentalcontrol.services.exceptions.EncodingException;
import com.ing3nia.parentalcontrol.services.parent.ParentTestResource;

/**
 * Contains a series of utils for data encryption and decryption
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public class EncryptionUtils {
	
	private static final Logger logger = Logger
	.getLogger(ParentTestResource.class.getName());
	
	/**
	 * Returns a MD5 string from the provided message string
	 * @throws EncodingException 
	 */
	public static String toMD5(String message) throws EncodingException{
		
		String md5Hash;
		try{
			md5Hash = DigestUtils.md5Hex(message);
		}
		catch(Exception e){
			logger.severe("An error ocurred while obtaining the md5 hash from the string: "+message+" "+ e.getMessage());
			throw new EncodingException();
		}
		return md5Hash;
		
		/*
		byte[] bytesOfMessage;
		try {
			bytesOfMessage = message.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.severe("An error ocurred when getting bytes in UTF-8 from String: "+message+" "+e.getMessage());	
			throw new EncodingException();
		}

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.severe("An error ocurred when getting a MD5 instance. " +e.getMessage());	
			throw new EncodingException();
		}
		
		byte[] thedigest = md.digest(bytesOfMessage);
		return new String(thedigest);
		*/
		
	}
	
}
