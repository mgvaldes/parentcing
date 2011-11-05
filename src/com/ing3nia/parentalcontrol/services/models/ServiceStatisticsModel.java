package com.ing3nia.parentalcontrol.services.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.models.PCServiceStatistics;
import com.ing3nia.parentalcontrol.models.PCWSStatus;
import com.ing3nia.parentalcontrol.models.utils.WSInfo;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class ServiceStatisticsModel {
	private String serviceCode;
	
	private String date;
	
	private String smartphone;
	
	private String status;

	public ServiceStatisticsModel() {
		super();
	}

	public ServiceStatisticsModel(String serviceCode,
			String date, String smartphone, String status) {
		super();
		this.serviceCode = serviceCode;
		this.date = date;
		this.smartphone = smartphone;
		this.status = status;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSmartphone() {
		return smartphone;
	}

	public void setSmartphone(String smartphone) {
		this.smartphone = smartphone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public PCServiceStatistics convertToPCServiceStatistics() {
		PCServiceStatistics stat = new PCServiceStatistics();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		try {
			stat.setDate(formatter.parse(this.date));
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stat.setServiceCode(this.serviceCode);
		stat.setServiceName(WSInfo.getWSName(this.serviceCode));
		stat.setSmartphone(KeyFactory.stringToKey(this.smartphone));
		stat.setStatus(WSStatus.getPCWSStatusFromCode(this.status));
		
		return stat;
	}
	
	public static void savePCServiceStatistics(PCServiceStatistics stat) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			pm.makePersistent(stat);
		}
		finally {
			pm.close();
		}
	}
}
