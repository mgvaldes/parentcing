package com.ing3nia.parentalcontrol.models.utils;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.PCWSStatus;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

/**
 * 
 * @author gaby
 *
 */
public enum WSStatus {
	OK("00", "OK", "OK"),
	INVALID_PHONE_ID("01", "INVALID_PHONE_ID", "The supplied unique id is not valid");
	//TODO Agregar todos tipos de status
	
	/**
	 * 
	 */
	private String code;
	
	/**
	 * 
	 */
	private String verbose;
	
	/**
	 * 
	 */
	private String msg;	
	
	WSStatus(String code, String verbose, String msg) {
		this.code = code;
		this.verbose = verbose;
		this.msg = msg;
	}
	
	public static PCWSStatus getPCWSStatusFromCode(String code) {
		PCWSStatus status = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		Query query = pm.newQuery(PCWSStatus.class);
		query.setFilter("code == codeParam");
	    query.declareParameters("String codeParam");
	    query.setRange(0, 1);
	    
	    try {
	    	List<PCWSStatus> result = (List<PCWSStatus>)query.execute(code);
	    	Iterator iter = result.iterator();
	    	
	    	status = (PCWSStatus)iter.next();
	    }
	    finally {
	    	pm.close();
	    }
	    
	    return status;
	}
}
