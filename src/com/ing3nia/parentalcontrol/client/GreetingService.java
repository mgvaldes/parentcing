package com.ing3nia.parentalcontrol.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

//import com.google.gwt.user.client.rpc.RemoteService;
//import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
/*
//@RemoteServiceRelativePath("greet")
//public interface GreetingService extends RemoteService {
//	String greetServer(String name) throws IllegalArgumentException;
//}

public interface GreetingService {
	//String greetServer(String name) throws IllegalArgumentException;
}*/



/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
}
