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
import com.ing3nia.parentalcontrol.client.rpc.UserRegisterService;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.models.UserModel;

public class UserRegisterServiceImpl extends RemoteServiceServlet implements UserRegisterService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserRegisterServiceImpl.class.getName());
	
	public UserRegisterServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean register(String username, String password, String email, String name) {
		boolean saveResult = false;
		
		URL url;
		URLConnection conn;
		
		try {
			UserModel userModel = new UserModel();
			userModel.setUsr(username);
			userModel.setPass(password);
			userModel.setEmail(email);
			userModel.setName(name);
			
			url = new URL("https://localhost:8888/resources/register");
			conn = url.openConnection();
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			conn.setDoOutput(true);
			
			Gson jsonBuilder = new Gson();
			Type userType = new TypeToken<UserModel>(){}.getType();
			String postParams = jsonBuilder.toJson(userModel, userType);
			
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
				logger.info("[UserRegisterService] UserRegister resource returnes successfully.");
			}
			else {
				String verbose = ((JsonPrimitive)jsonResponseStatus.get("verbose")).getAsString();
				String msg = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
				logger.info("[UserRegisterService] An error occured calling UserRegister resource. CODE: " + code + " VERBOSE: " + verbose + " MSG: " + msg);
			}
		} 
		catch (MalformedURLException e) {
			logger.info("[UserRegisterService] An error occured creating resource's url. " + e); 
		}	
		catch (IOException e) {
			logger.info("[UserRegisterService] An error occured writing to OutputotStreamWriteror reading from BufferedReader. " + e);
		}
		
		return saveResult;
	}
}
