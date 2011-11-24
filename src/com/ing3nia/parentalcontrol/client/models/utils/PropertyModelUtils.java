package com.ing3nia.parentalcontrol.client.models.utils;

import java.text.SimpleDateFormat;

import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.models.PCProperty;

public class PropertyModelUtils {
	public static PropertyModel convertToPropertyModel(PCProperty property) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		
		return new PropertyModel(property.getDescription(), property.getValue(), property.getId(), formatter.format(property.getCreationDate()));
	}
}
