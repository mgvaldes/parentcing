package com.ing3nia.parentalcontrol.services.models.utils;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.client.models.DeviceModel;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.utils.PCOsTypeId;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class DeviceModelUtils {

	public static Key convertToPCDevice(DeviceModel deviceModel) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCDevice device = new PCDevice();
		
		device.setModel(deviceModel.getModel());
		device.setVersion(deviceModel.getVersion());
		
		PCDevice.PCOs pcOs = new PCDevice.PCOs();
		pcOs.setId(deviceModel.getType());
		pcOs.setOsType(PCOsTypeId.getOsTypeFromId(deviceModel.getType()));
		
		device.setOs(pcOs);
		
		pm.makePersistent(device);
		
		pm.close();
		
		return device.getKey();
	}
	
	public static DeviceModel convertToDeviceModel(Key devKey) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCDevice device = pm.getObjectById(PCDevice.class, devKey);
		
		DeviceModel d = new DeviceModel(device.getModel(), device.getVersion(), device.getOs().getId());
		
		pm.close();
		
		return d;
	}
}
