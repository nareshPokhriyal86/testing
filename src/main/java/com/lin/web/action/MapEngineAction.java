package com.lin.web.action;

import java.util.Date;
import java.util.logging.Logger;

import com.lin.web.service.IMapEngineService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.LinMobileConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class MapEngineAction extends ActionSupport  {
	
	private static final Logger log = Logger.getLogger(MapEngineAction.class.getName());
	
	/*
	 * @author Shubham Goel 
	 * update the data on GME vector tables by using tableId.
 */
	public String updateProductDataGME(){
		log.info("updateProductDataGME Starts.."+new Date());
		IMapEngineService service = (IMapEngineService)BusinessServiceLocator.locate(IMapEngineService.class);
		try{
			String publisherIdInBQ ="5";
			if(publisherIdInBQ!=null && !publisherIdInBQ.equals("") && !publisherIdInBQ.isEmpty()){
				service.updateProductDataGME(publisherIdInBQ);
			}
		}catch(Exception e){
			log.severe("Exception in updateProductDataGME()  in MapEngineAction : "+e.getMessage());
			 e.printStackTrace();
		}
		return Action.SUCCESS;
		
	}
	

}
