package com.ing3nia.parentalcontrol.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.rpc.EditRuleService;
import com.ing3nia.parentalcontrol.client.utils.ModelLogger;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class EditRuleServiceImpl extends RemoteServiceServlet implements EditRuleService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(EditRuleServiceImpl.class.getName());
	
	public EditRuleServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean editRule(RuleModel editedRule) {
		boolean editResult = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {						
			logger.info("[EditRuleService] Searching for rule with key: " + editedRule.getKeyId());
			PCRule rule = (PCRule)pm.getObjectById(PCRule.class, editedRule.getKeyId());
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			Date date;
			
			logger.info("[EditRuleService] Updating new values");
			date = sdf.parse(editedRule.getStartDate());
			rule.setStartDate(date);
			date = sdf.parse(editedRule.getEndDate());
			rule.setEndDate(date);
			rule.setName(editedRule.getName());
			rule.setType(editedRule.getType());
			
			PersistenceManager pm2 = ServiceUtils.PMF.getPersistenceManager();
			
			logger.info("[EditRuleService] Loading new functionalities");
			rule.setDisabledFunctionalities(getNewFuncionalitiesAsKeys(pm2, editedRule));
			
			editResult = true;
		}
		catch (Exception ex) {
			editResult = false;
		}
		finally {
			pm.close();
		}
		
		return editResult;
	}
	
	private static ArrayList<Key> getNewFuncionalitiesAsKeys(PersistenceManager pm, RuleModel ruleModel) {
		Logger logger = ModelLogger.logger;

		Query query = pm.newQuery(PCFunctionality.class);
		query.setFilter("id == id_param");
		query.declareParameters("int id_param");
		query.setRange(0, 1);

		int idFuncionality;
		ArrayList<Key> newDisabledFuncionalities = new ArrayList<Key>();
		
		for (Integer idFunc : ruleModel.getDisabledFunctionalities()) {
			PCFunctionality funcionality;

			idFuncionality = idFunc;
			logger.info("[Add Rule Service] Finding funcionality by id: " + idFuncionality);
			
			try {
				List<PCFunctionality> results = (List<PCFunctionality>) query.execute(idFuncionality);
				
				if (!results.isEmpty()) {
					logger.info("[Add Rule Service] Returning found PCFuncionality");
					funcionality = results.get(0);
					newDisabledFuncionalities.add(funcionality.getKey());
				} 
				else {
					logger.severe("[Add Rule Service] No funcionality with id: " + idFuncionality + " was found ");
					newDisabledFuncionalities = null;
				}
			} 
			catch (Exception e) {
				logger.info("[Add Rule Service] Could not find funcionality from id: " + idFuncionality + " " + e.getMessage());
				newDisabledFuncionalities = null;
			}
		}
		pm.close();
		
		return newDisabledFuncionalities;
	}
}
