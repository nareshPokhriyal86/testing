package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.IPoolMapDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdUnitDataObj;
import com.lin.server.bean.ForcastInventoryObj;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.MemcacheUtil;


/*
 * @author Youdhveer Panwar
 * PoolMapDAO 
 */
public class PoolMapDAO implements IPoolMapDAO{

	private static final Logger log=Logger.getLogger(PoolMapDAO.class.getName());
	private Objectify obfy = OfyService.ofy();
	
	
	public void saveObject(Object obj) throws DataServiceException {
		obfy.save().entity(obj);		
	}	
	
	/*
	 * Load all AdUnitIds from datastore for a publisherId
	 * @param String 
	 * @return List<AdUnitDataObj>
	 */
	public List<AdUnitDataObj> loadAllAdUnitIds(String publisherId) throws DataServiceException{
		List<AdUnitDataObj> resultList=obfy.load().type(AdUnitDataObj.class) 
								 .filter("publisherId = ",publisherId)
		                         .list();
		return resultList;
	}
	
	public List<ForcastInventoryObj> loadAllDMAsWithInventory(String startDate, String endDate){
		log.info("in loadAllDMAsWithInventory() of poolMapDAO");
		List<ForcastInventoryObj> forcastInventoryObjList = new ArrayList<>();
		try{
			forcastInventoryObjList = MemcacheUtil.getAllDMAsWithInventory(startDate, endDate);
			if(forcastInventoryObjList==null || forcastInventoryObjList.size()<=0){
				forcastInventoryObjList = obfy.load().type(ForcastInventoryObj.class)
				          .filter("startDate >= ", startDate)
				         // .order("availableImpressions")
				          .list();
				MemcacheUtil.setAllDMAsWithInventory(startDate, endDate, forcastInventoryObjList);
			}
		}catch(Exception e){
			log.severe("Exception in loadAllDMAsWithInventory  of poolMapDAO "+e.getMessage());
			e.printStackTrace();
		}
		if(forcastInventoryObjList != null) {
			log.info("forcastInventoryObjList  size : "+forcastInventoryObjList.size());
		}
		else {
			log.info("forcastInventoryObjList  size : null");
		}
		return forcastInventoryObjList;
		
	}
	
	public List<ForcastInventoryObj> loadAllocateInventry(String startDate, String endDate, String codeListstr, List<String> codeList){
		log.info("in loadAllocateInventry() of poolMapDAO");
		List<ForcastInventoryObj> forcastInventoryObjList = new ArrayList<>();
		try{
			forcastInventoryObjList = MemcacheUtil.getAllocateInventry(startDate, endDate, codeListstr);
			if(forcastInventoryObjList==null || forcastInventoryObjList.size()<=0){
				forcastInventoryObjList = obfy.load().type(ForcastInventoryObj.class)
				          .filter("startDate >= ", startDate)
				          .filter("code IN", codeList)
				         // .order("availableImpressions")
				          .list();
				MemcacheUtil.setAllocateInventry(startDate, endDate, codeListstr, forcastInventoryObjList);
			}
		}catch(Exception e){
			log.severe("Exception in loadAllocateInventry of poolMapDAO "+e.getMessage());
			e.printStackTrace();
		}
		return forcastInventoryObjList;
		
	}
	
}
