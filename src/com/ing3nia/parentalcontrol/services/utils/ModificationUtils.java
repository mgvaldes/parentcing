package com.ing3nia.parentalcontrol.services.utils;

import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.models.ModificationModel;

/**
 * Contain a series of utils methods to manipulate and query smarpthone modifications
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public class ModificationUtils {
	/**
	 * Processes the modifications made by the parent user on a child smartphone
	 */
	public PCModification ProcessParentModifications(PCSmartphone pcsmartphone, ModificationModel modifications){
		PCModification pcmodification = pcsmartphone.getModification();
		if(pcmodification == null){
			pcmodification = processNewParentModifications(modifications);
			return pcmodification;
		}
		return null;
	}
	
	private PCModification processNewParentModifications(ModificationModel modifications){
		return null;
	}
	
}


