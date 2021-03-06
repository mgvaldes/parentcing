package com.ing3nia.parentalcontrol.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.rpc.RegisterUserService;
import com.ing3nia.parentalcontrol.client.utils.PCURLMapper;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;

public class RegisterUserServiceImpl extends RemoteServiceServlet implements RegisterUserService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(RegisterUserServiceImpl.class.getName());
	
	public RegisterUserServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean registerUser(UserModel user) {
		boolean registerResult = false;
		
		URL url;
		URLConnection conn;
		
		try {
			url = new URL(PCURLMapper.CURRENT_BASE_URL + "/resources/register");
			conn = url.openConnection();
			conn.setConnectTimeout(60000);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type","application/json; charset=utf-8");
			
			Gson jsonBuilder = new Gson();
			Type userModelType = new TypeToken<UserModel>(){}.getType();
			String postParams = jsonBuilder.toJson(user, userModelType);
			
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
				registerResult = true;
				logger.info("[RegisterUserService] RegisterUser resource returnes successfully.");
			}
			else {
				String verbose = ((JsonPrimitive)jsonResponseStatus.get("verbose")).getAsString();
				String msg = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
				logger.info("[RegisterUserService] An error occured calling RegisterUser resource. CODE: " + code + " VERBOSE: " + verbose + " MSG: " + msg);
			}
		} 
		catch (MalformedURLException e) {
			logger.info("[RegisterUserService] An error occured creating resource's url. " + e); 
		}	
		catch (IOException e) {
			logger.info("[RegisterUserService] An error occured writing to OutputotStreamWriteror reading from BufferedReader. " + e);
		}
		
		return registerResult;
	}
}
