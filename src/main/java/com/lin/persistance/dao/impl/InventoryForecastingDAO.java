package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.collections.functors.ForClosure;

import com.googlecode.objectify.Objectify;
import com.lin.persistance.dao.IInventoryForecastingDAO;
import com.lin.server.bean.DFPSitesWithDMAObj;
import com.lin.server.bean.ForcastInventoryObj;
import com.lin.web.service.impl.OfyService;
import com.lin.web.util.MemcacheUtil;

public class InventoryForecastingDAO implements IInventoryForecastingDAO {
	
	private static final Logger log = Logger.getLogger(AdvertiserDAO.class.getName());
	private Objectify obfy = OfyService.ofy();
	
	public List<DFPSitesWithDMAObj> getAllDFPSitesWithDMA(){
		log.info("in getAllDFPSitesWithDMA() of InventoryForecastingDAO ");
		List<DFPSitesWithDMAObj> DFPSitesWithDMAObjList = new ArrayList<>();
		try{
			DFPSitesWithDMAObjList = MemcacheUtil.getDFPSitesWithDMAObjs();
			if(DFPSitesWithDMAObjList==null || DFPSitesWithDMAObjList.size()<=0){
				DFPSitesWithDMAObjList = obfy.load().type(DFPSitesWithDMAObj.class).list();
				MemcacheUtil.setDFPSitesWithDMAObjs(DFPSitesWithDMAObjList);
			}
		}catch(Exception e){
			log.severe("Exception in getAllDFPSitesWithDMA()"+ e.getMessage());
			e.printStackTrace();
		}
		return DFPSitesWithDMAObjList;
	}
	
	public void deleteUpdateForcastInventoryObj(List<ForcastInventoryObj> forcastInventoryObjList, boolean doEmptyTable){
		log.info("in deleteUpdateForcastInventoryObj() of InventoryForcastDAO");
		try{
			/*if(doEmptyTable){
				List<ForcastInventoryObj> inventoryObjs = new ArrayList<>();
				inventoryObjs = obfy.query(ForcastInventoryObj.class).list();
				if(inventoryObjs!=null && inventoryObjs.size()>0){
					obfy.delete(inventoryObjs);
				}
			}*/
			obfy.save().entity(forcastInventoryObjList);
			
		}catch(Exception e){
			log.severe("Exception in deleteUpdateForcastInventoryObj() of InventoryForcastDAO"+e.getMessage());
			e.printStackTrace();
		}
	}

}
