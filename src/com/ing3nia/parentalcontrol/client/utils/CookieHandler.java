package com.ing3nia.parentalcontrol.client.utils;

import java.util.Date;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gwt.user.client.Cookies;

public class CookieHandler {
  public static String COOKIE_ID = "CookieId";
  public static String USERNAME_ID ="Content-PCcred-1-val";
  public static String PASSWORD_ID ="encr-PCval-cont2";
  public static String REMEMBER_PASS= "rem-pass-PC";
  
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
	  Cookies.setCookie(REMEMBER_PASS, "True");
  }
  
  public static void clearSessionCookie(){
	  Cookies.removeCookie(COOKIE_ID);
  }
  
  public static void clearCredentialsRemember(){
	  Cookies.removeCookie(USERNAME_ID);
	  Cookies.removeCookie(PASSWORD_ID);
	  Cookies.removeCookie(REMEMBER_PASS);
  }
  
  public static String getRememberPass(){
	  return Cookies.getCookie(REMEMBER_PASS);
  }
  
  public static void setRememberPass(String remember){
	  Cookies.setCookie(REMEMBER_PASS, remember);
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
