package com.ing3nia.parentalcontrol.server;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.ContactModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.GetSmartphoneDetailsService;
import com.ing3nia.parentalcontrol.services.models.utils.SmartphoneModelUtils;

public class GetSmartphoneDetailsServiceImpl extends RemoteServiceServlet implements GetSmartphoneDetailsService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public SmartphoneModel getDetails(String cid, SmartphoneModel smartphoneModel) throws IllegalArgumentException {

		JsonObject auxObject;
		JsonArray auxArray;
		SmartphoneModel auxSmartphone;
		Type sphType = new TypeToken<SmartphoneModel>(){}.getType();
		Gson gson = new Gson();
		ArrayList<ContactModel> auxContacts;
		
		auxObject = LoginServiceImpl.callParentSmartphoneDetails(cid, smartphoneModel.getKeyId());
		if(auxObject == null){
			auxObject = new JsonObject();
		}
		
		auxSmartphone = gson.fromJson(auxObject, sphType); 
		
		SmartphoneModelUtils.updateSmartphone(auxSmartphone, smartphoneModel);
		//sph.updateSmartphone(auxSmartphone);
		
		auxArray = auxObject.getAsJsonArray("inactive_cts");
		auxContacts = LoginServiceImpl.parseContacts(auxArray);
		smartphoneModel.setInactiveContacts(auxContacts);
		
		auxArray = auxObject.getAsJsonArray("active_cts");
		auxContacts = LoginServiceImpl.parseContacts(auxArray);
		smartphoneModel.setActiveContacts(auxContacts);
			
		System.out.println("SMARTPHONE NAME IMPL: "+smartphoneModel);
		
		return smartphoneModel;
	}
}

