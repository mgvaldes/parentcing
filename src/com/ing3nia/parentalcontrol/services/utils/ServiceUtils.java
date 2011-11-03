package com.ing3nia.parentalcontrol.services.utils;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class ServiceUtils {
	public static PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
}
