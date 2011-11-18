package com.ing3nia.parentalcontrol.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.models.ModificationModel;
import com.ing3nia.parentalcontrol.services.models.ParentModificationsModel;

public class SaveSmartphoneModificationsServiceImpl extends RemoteServiceServlet implements SaveSmartphoneModificationsService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SaveSmartphoneModificationsServiceImpl.class.getName());
	
	public SaveSmartphoneModificationsServiceImpl() {
		logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean saveSmartphoneModifications(String cid, String smphid, ModificationModel modifications) {
		boolean saveResult = false;
		
		URL url;
		URLConnection conn;
		
		try {
			ParentModificationsModel parentMod = new ParentModificationsModel(cid, smphid, modifications);
			
			url = new URL("https://localhost:8888/resources/parent-mod");
			conn = url.openConnection();
			conn.setDoOutput(true);
			
			Gson jsonBuilder = new Gson();
			Type modificationType = new TypeToken<ParentModificationsModel>(){}.getType();
			String postParams = jsonBuilder.toJson(parentMod, modificationType);
			
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(postParams);
			wr.flush();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
				
			rd.close();
			
			logger.info(sb.toString());
			
			JsonParser jsonParser = new JsonParser();			
			JsonObject jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
			JsonObject jsonResponseStatus = jsonResponse.getAsJsonObject("status");
			String code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString(); 
			
			if (code.equals(WSStatus.OK.getCode())) {
				saveResult = true;
				logger.info("[SaveDeviceSettingsService] ParentSmartphoneModifications resource returnes successfully.");
			}
			else {
				String verbose = ((JsonPrimitive)jsonResponseStatus.get("verbose")).getAsString();
				String msg = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
				logger.info("[SaveDeviceSettingsService] An error occured calling ParentSmartphoneModifications resource. CODE: " + code + " VERBOSE: " + verbose + " MSG: " + msg);
			}
		} 
		catch (MalformedURLException e) {
			logger.info("[SaveDeviceSettingsService] An error occured creating resource's url. " + e); 
		}	
		catch (IOException e) {
			logger.info("[SaveDeviceSettingsService] An error occured writing to OutputotStreamWriteror reading from BufferedReader. " + e);
		}
		
		return saveResult;
	}
}
