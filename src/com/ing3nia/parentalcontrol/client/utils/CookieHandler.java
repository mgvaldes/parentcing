package com.ing3nia.parentalcontrol.client.utils;

import java.util.Date;

import com.google.gwt.user.client.Cookies;

public class CookieHandler {
  public static String COOKIE_ID = "CookieId";
  public static String USERNAME_ID ="Content-PCcred-1-val";
  public static String PASSWORD_ID ="encr-PCval-cont2";
  
  public static Date getCookieTimeout(){
	  Date now = new Date();
	  long nowLong = now.getTime();
	  //nowLong = nowLong + (1000 * 60 * 60 * 24 * 7);//seven days
	  nowLong = nowLong + (1000* 60);
	  now.setTime(nowLong);
	  return now;
  }
  
  public static void setPCCookie(String cookieValue){
	  Cookies.setCookie(COOKIE_ID, cookieValue, getCookieTimeout());	  
  }
  
  public static void setCredentialsRemember(String mail, String pass){
	  Cookies.setCookie(USERNAME_ID, mail);
	  Cookies.setCookie(PASSWORD_ID, pass);
  }
  
  public static String getMailRemeberedCredential(){
	  return Cookies.getCookie(USERNAME_ID);
  }
  
  public static  String getPasswordRemeberedCredential(){
	  return Cookies.getCookie(PASSWORD_ID);
  }
  
  public static String getPCCookie(){
	  return Cookies.getCookie(COOKIE_ID);  
  }
}
