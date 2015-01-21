package com.lin.web.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.lin.web.service.IBusinessService;

/*
 * Business Service Locator class
 */
		
public class BusinessServiceLocator {

private final static Properties services;
private static final Logger log = Logger.getLogger(BusinessServiceLocator.class.getName());
	static {
		services = new Properties();
		try {
			services.load(BusinessServiceLocator.class.getResourceAsStream("/env/businessServices.properties"));
		} catch (IOException e) {
			throw new RuntimeException("No properties");
		}
	}
	
	private static Map<String,Object> bMap = new HashMap<String,Object>();

	public static IBusinessService locate(Class className) {
		if (bMap.containsKey(className.getName())){
			return (IBusinessService)bMap.get(className.getName());
		}
		//LogManager.debug(new BusinessServiceLocator(), services.toString());
		String clazz = services.getProperty(className.getName());
		//LogManager.debug(new BusinessServiceLocator(), clazz);
		Object obj = null;
		try {			
			obj = Class.forName(clazz).newInstance();
		} catch (InstantiationException e) {
			log.severe(new BusinessServiceLocator()+ e.getMessage());
			//LogManager.error(new BusinessServiceLocator(), e.getMessage());
			throw new RuntimeException("No Business Service Found : "+className.getName());
		} catch (IllegalAccessException e) {
			//LogManager.error(new BusinessServiceLocator(), e.getMessage());
			log.severe(new BusinessServiceLocator()+ e.getMessage());
			throw new RuntimeException("No Business Service Found : "+className.getName());
		} catch (ClassNotFoundException e) {
			//LogManager.error(new BusinessServiceLocator(), e.getMessage());
			log.severe(new BusinessServiceLocator()+ e.getMessage());
			throw new RuntimeException("No Business Service Found : "+className.getName());
		}
		if (null != obj && obj instanceof IBusinessService){
			bMap.put(className.getName(), obj);
			return (IBusinessService)obj;
		}else{
			//LogManager.error(new BusinessServiceLocator(),"not an instance");
			log.severe(new BusinessServiceLocator()+"not an instance");
		}
		return null;
	}
}
