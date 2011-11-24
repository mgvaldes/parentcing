package com.ing3nia.parentalcontrol.services.models.utils;

import com.ing3nia.parentalcontrol.client.models.DeviceModel;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.utils.PCOsTypeId;

public class DeviceModelUtils {

	public static PCDevice convertToPCDevice(DeviceModel deviceModel) {
		PCDevice device = new PCDevice();
		
		device.setModel(deviceModel.getModel());
		device.setVersion(deviceModel.getVersion());
		
		PCDevice.PCOs pcOs = new PCDevice.PCOs();
		pcOs.setId(deviceModel.getType());
		pcOs.setOsType(PCOsTypeId.getOsTypeFromId(deviceModel.getType()));
		
		device.setOs(pcOs);
		
		return device;
	}
	
	public static DeviceModel convertToDeviceModel(PCDevice device) {
		return new DeviceModel(device.getModel(), device.getVersion(), device.getOs().getId());
	}
}
