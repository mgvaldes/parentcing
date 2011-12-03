package com.ing3nia.parentalcontrol.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.ContactModel;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.rpc.LoginService;
import com.ing3nia.parentalcontrol.client.utils.ContactInfo;
import com.ing3nia.parentalcontrol.client.utils.PCURLMapper;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.models.utils.SmartphoneModelUtils;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());
	
	public LoginServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public ClientUserModel login(String username, String password) {
		ArrayList<SmartphoneModel> smartphones = new ArrayList<SmartphoneModel>();
		SmartphoneModel auxSmartphone;
		JsonObject auxObject;
		JsonArray auxArray;
		Type sphType = new TypeToken<SmartphoneModel>(){}.getType();
		Gson gson = new Gson();
		ArrayList<ContactModel> auxContacts;
		ClientUserModel user = null;
		
		String cid = callParentLoginResource(username, password);
		
		if (cid != null) {
			user = new ClientUserModel();
			user.setCid(cid);
			
			smartphones = callParentSmartphoneGeneral(cid);			
			
			if (smartphones != null) {
				for (SmartphoneModel sph : smartphones) {
					auxObject = callParentSmartphoneDetails(cid, sph.getKeyId());
					auxSmartphone = gson.fromJson(auxObject, sphType); 
					
					SmartphoneModelUtils.updateSmartphone(auxSmartphone, sph);
					//sph.updateSmartphone(auxSmartphone);
					
					auxArray = auxObject.getAsJsonArray("inactive_cts");
					auxContacts = parseContacts(auxArray);
					sph.setInactiveContacts(auxContacts);
					
					auxArray = auxObject.getAsJsonArray("active_cts");
					auxContacts = parseContacts(auxArray);
					sph.setActiveContacts(auxContacts);
				}
				
				user.setSmartphones(smartphones);
			}
		}
		
		return user;
	}
	
	public ArrayList<ContactModel> parseContacts(JsonArray simpleContactsArray) {
		ArrayList<ContactModel> contactsList = new ArrayList<ContactModel>();
		HashMap<String, ContactInfo> auxContactsHashMap = new HashMap<String, ContactInfo>();
		String contactName;
		ArrayList<PhoneModel> auxPhoneList;
		ContactInfo auxContactInfo;
		JsonObject auxObject;
		JsonObject auxPhone;
		PhoneModel phoneModel;
		
		for (JsonElement sc : simpleContactsArray) {
			auxObject = (JsonObject)sc;
			
			contactName = ((JsonPrimitive)auxObject.get("fname")).getAsString() + "|" + ((JsonPrimitive)auxObject.get("lname")).getAsString();

			if (auxContactsHashMap.containsKey(contactName)) {
				auxContactInfo = auxContactsHashMap.get(contactName);
				auxContactInfo.setKey(((JsonPrimitive)auxObject.get("id")).getAsString());
				auxPhoneList = auxContactInfo.getPhones();

				auxPhone = (JsonObject)auxObject.getAsJsonArray("num").get(0);
				phoneModel = new PhoneModel(((JsonPrimitive)auxPhone.get("type")).getAsInt(), ((JsonPrimitive)auxPhone.get("phone")).getAsString());
				auxPhoneList.add(phoneModel);
				auxContactInfo.setPhones(auxPhoneList);
				auxContactsHashMap.put(contactName, auxContactInfo);
			} 
			else {
				auxContactInfo = new ContactInfo();
				auxContactInfo.setKey(((JsonPrimitive)auxObject.get("id")).getAsString());
				auxPhoneList = new ArrayList<PhoneModel>();

				auxPhone = (JsonObject)auxObject.getAsJsonArray("num").get(0);
				phoneModel = new PhoneModel(((JsonPrimitive)auxPhone.get("type")).getAsInt(), ((JsonPrimitive)auxPhone.get("phone")).getAsString());
				auxPhoneList.add(phoneModel);
				auxContactInfo.setPhones(auxPhoneList);
				auxContactsHashMap.put(contactName, auxContactInfo);
			}
		}
		
		Iterator<Map.Entry<String, ContactInfo>> it = auxContactsHashMap.entrySet().iterator();
		ContactModel auxContact;
		Map.Entry<String, ContactInfo> pair;
		String[] auxName;
		auxContactInfo = null;
		
	    while (it.hasNext()) {
	        pair = (Map.Entry<String, ContactInfo>)it.next();	        

	        auxName = ((String)pair.getKey()).split("\\|");
	        auxContactInfo = (ContactInfo)pair.getValue();
	        auxContact = new ContactModel(auxContactInfo.getKey(), auxName[0], auxName[1], auxContactInfo.getPhones());
	        contactsList.add(auxContact);
	    }
		
		return contactsList;
	}
	
	public ArrayList<EmergencyNumberModel> parseEmergencyNumbers(JsonArray simpleContactsArray) {
		ArrayList<EmergencyNumberModel> emergencyNumbers = new ArrayList<EmergencyNumberModel>();
		
		return emergencyNumbers;
	}
	
	public JsonObject callParentSmartphoneDetails(String cid, String smid) {
		JsonObject smartphone = null;
		
		URL url;
		URLConnection conn;
		
		try {
			logger.info("[login Service Connection] creating new URL");
			url = new URL(PCURLMapper.CURRENT_BASE_URL+"/resources/smartphone-details?cid=" + cid + "&smid=" + smid);
			conn = url.openConnection();
			conn.setConnectTimeout(60000);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
				
			rd.close();
			
			logger.info("DETAILS: "+sb.toString());
			
			JsonParser jsonParser = new JsonParser();			
			JsonObject jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
			JsonObject jsonResponseStatus = jsonResponse.getAsJsonObject("status");
			String code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString(); 
			
			if (code.equals(WSStatus.OK.getCode())) {
				logger.info("[LoginService] ParentSmartphoneGeneral resource returnes successfully.");

				smartphone = jsonResponse.getAsJsonObject("smartphone");
			}
			else {
				String verbose = ((JsonPrimitive)jsonResponseStatus.get("verbose")).getAsString();
				String msg = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
				logger.info("[LoginService] An error occured calling ParentSmartphoneGeneral resource. CODE: " + code + " VERBOSE: " + verbose + " MSG: " + msg);
			}
		} 
		catch (MalformedURLException e) {
			logger.info("[LoginService] An error occured creating resource's url. " + e); 
		}	
		catch (IOException e) {
			logger.info("[LoginService] An error occured writing to InputStreamReader reading from BufferedReader. " + e);
		}
		
		return smartphone;
	}
	
	public ArrayList<SmartphoneModel> callParentSmartphoneGeneral(String cid) {
		ArrayList<SmartphoneModel> smartphones = null;
		
		URL url;
		URLConnection conn;
		
		try {
			logger.info("CONNECT URL: "+PCURLMapper.CURRENT_BASE_URL+"/resources/smartphone-grl?cid=" + cid);
			url = new URL(PCURLMapper.CURRENT_BASE_URL+"/resources/smartphone-grl?cid=" + cid);
			conn = url.openConnection();
			conn.setConnectTimeout(60000);
			conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
				
			rd.close();
			
			logger.info("GENERAL: "+sb.toString());
			
			JsonParser jsonParser = new JsonParser();			
			JsonObject jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
			JsonObject jsonResponseStatus = jsonResponse.getAsJsonObject("status");
			String code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString(); 
			
			if (code.equals(WSStatus.OK.getCode())) {
				logger.info("[LoginService] ParentSmartphoneGeneral resource returnes successfully.");
				
				Type sphArrayType = new TypeToken<ArrayList<SmartphoneModel>>(){}.getType();
				Gson gson = new Gson();
				smartphones = gson.fromJson(jsonResponse.getAsJsonArray("smartphones"), sphArrayType);
			}
			else {
				String verbose = ((JsonPrimitive)jsonResponseStatus.get("verbose")).getAsString();
				String msg = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
				logger.info("[LoginService] An error occured calling ParentSmartphoneGeneral resource. CODE: " + code + " VERBOSE: " + verbose + " MSG: " + msg);
			}
		} 
		catch (MalformedURLException e) {
			logger.info("[LoginService] An error occured creating resource's url. " + e); 
		}	
		catch (IOException e) {
			logger.info("[LoginService] An error occured writing to OutputotStreamWriteror reading from BufferedReader. " + e);
		}
		
		return smartphones;
	}
	
	public String callParentLoginResource(String username, String password) {
		String cookieId = null;
		
		URL url;
		URLConnection conn;
		
		try {
			UserModel userModel = new UserModel();
			userModel.setUsr(username);
			userModel.setPass(password);
			
			logger.info("Requesting for login web service");
			url = new URL(PCURLMapper.CURRENT_BASE_URL+"/resources/login");
			
			conn = url.openConnection();
			conn.setConnectTimeout(60000);
			conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
			conn.setDoOutput(true);
			
			Gson jsonBuilder = new Gson();
			Type modificationType = new TypeToken<UserModel>(){}.getType();
			String postParams = jsonBuilder.toJson(userModel, modificationType);
			
			logger.info("Adding OutputStreamWriter to request");
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(postParams);
			wr.flush();
			
			logger.info("Parsing Response");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
				
			rd.close();
			
			logger.info("Obtained response "+sb.toString());
			
			JsonParser jsonParser = new JsonParser();			
			JsonObject jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
			JsonObject jsonResponseStatus = jsonResponse.getAsJsonObject("status");
			String code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString(); 
			
			if (code.equals(WSStatus.OK.getCode())) {
				logger.info("[LoginService] ParentLoginResource resource returnes successfully.");
				return ((JsonPrimitive)jsonResponse.get("cid")).getAsString();
			}
			else {
				String verbose = ((JsonPrimitive)jsonResponseStatus.get("verbose")).getAsString();
				String msg = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
				logger.info("[LoginService] An error occured calling ParentLoginResource resource. CODE: " + code + " VERBOSE: " + verbose + " MSG: " + msg);
			}
		} 
		catch (MalformedURLException e) {
			logger.info("[LoginService] An error occured creating resource's url. " + e); 
		}	
		catch (IOException e) {
			logger.info("[LoginService] An error occured writing to OutputotStreamWriteror reading from BufferedReader. " + e);
		}

		return cookieId;
	}
	
}
