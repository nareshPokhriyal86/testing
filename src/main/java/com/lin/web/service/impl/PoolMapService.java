package com.lin.web.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.struts2.views.xslt.ArrayAdapter;

import com.google.gdata.data.introspection.Collection;
import com.lin.persistance.dao.IPoolMapDAO;
import com.lin.persistance.dao.impl.PoolMapDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdUnitDataObj;
import com.lin.server.bean.ForcastInventoryObj;
import com.lin.server.bean.PublisherPropertiesObj;
import com.lin.web.dto.ForcastInventoryDTO;
import com.lin.web.service.IPoolMapService;
import com.lin.web.util.DateUtil;
import com.lin.web.util.MemcacheUtil;

public class PoolMapService implements IPoolMapService{
	
	private static final Logger log = Logger.getLogger(PoolMapService.class.getName());

	/*
	 * Load all adunitId1 for a publisherId
	 * @author Youdhveer Panwar
	 * @param String publisherId
	 * @return Map<String,String>
	 * @see com.lin.web.service.IPoolMapService#loadAdUnitId(java.lang.String)
	 */
	public Map<String,String> loadAdUnitIda(String publisherId){		
		
		String memcacheKey="AdUnitIds_"+publisherId;
		Map<String,String> adUnitMap=MemcacheUtil.getDataMapFromCacheByKey(memcacheKey);
		
		if(adUnitMap == null || adUnitMap.size()==0){
			adUnitMap=new HashMap<String,String>();
			log.info("Data not found in memcache, load from datastore...");
			IPoolMapDAO poolMapDAO=new PoolMapDAO();		
			try {
				List<AdUnitDataObj> adUnitList=poolMapDAO.loadAllAdUnitIds(publisherId);				
				for(AdUnitDataObj adUnitObj:adUnitList){					
					String adUnitId1=adUnitObj.getAdUnitId1();					
					if(!adUnitMap.containsKey(adUnitId1)){
						adUnitMap.put(adUnitId1, adUnitObj.getAdUnit1());
					}				
				}
				MemcacheUtil.setDataMapInCacheByKey(adUnitMap, memcacheKey);
				
			} catch (DataServiceException e) {
				log.severe("DataServiceException:"+e.getMessage());
				e.printStackTrace();
			}
		}
		
		log.info("adUnitMap size:"+adUnitMap.size());
		return adUnitMap;
	}
	
	public List<ForcastInventoryDTO> loadAllDMAsWithInventory(String startDate, String endDate){
		log.info("in loadAllDMAsWithInventory() of poolMapService");
		List<ForcastInventoryObj> forcastInventoryObjList = new ArrayList<>();
		List<ForcastInventoryObj> forcastInventoryResultList = new ArrayList<>();
		List<ForcastInventoryDTO> forcastInventoryDTOlist = new ArrayList<>();
		List<ForcastInventoryObj> forcastInventoryStartDateList = new ArrayList<>();
		ForcastInventoryDTO forcastInventoryDTO = new ForcastInventoryDTO();
		PoolMapDAO dao = new PoolMapDAO();
		
		Map<String, ForcastInventoryObj> map = new HashMap<>();
		try{
			Date formatedDate = null;
			forcastInventoryStartDateList = dao.loadAllDMAsWithInventory(startDate, endDate);
			for (ForcastInventoryObj forcastInventoryObj : forcastInventoryStartDateList) {
				
				if(forcastInventoryObj!=null && forcastInventoryObj.getEndDate().compareTo(endDate)<=0){
					
					forcastInventoryObjList.add(forcastInventoryObj);
				}
			}	
			Collections.sort(forcastInventoryObjList);
			
			if(forcastInventoryObjList!=null && forcastInventoryObjList.size()>0){
				map.put(forcastInventoryObjList.get(0).getCode()+forcastInventoryObjList.get(0).getName(), forcastInventoryObjList.get(0));
				for (ForcastInventoryObj forcastInventoryObj : forcastInventoryObjList) {
					if(map!=null && map.containsKey(forcastInventoryObj.getCode()+forcastInventoryObj.getName())){
						ForcastInventoryObj  obj = copyObject( map.get(forcastInventoryObj.getCode()+forcastInventoryObj.getName()));
						Long availableImpressions = forcastInventoryObj.getAvailableImpressions() +  obj.getAvailableImpressions();
						obj.setAvailableImpressions(availableImpressions);
						map.put(obj.getCode()+obj.getName(), obj);
					}else{
						map.put(forcastInventoryObj.getCode()+forcastInventoryObj.getName(),forcastInventoryObj);
					}
				}
				
				Iterator iterator = map.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry mapEntry = (Map.Entry) iterator.next();
					forcastInventoryResultList.add((ForcastInventoryObj) mapEntry.getValue());
			        }
				for (ForcastInventoryObj forcastInventoryObj : forcastInventoryResultList) {
					if(forcastInventoryObj!=null){
						ForcastInventoryDTO dto = new ForcastInventoryDTO();
						if(forcastInventoryObj.getCode()!=null){
							dto.setCode(forcastInventoryObj.getCode());
						}
						if(forcastInventoryObj.getAvailableImpressions()!=null){
							dto.setAvailableImpressions(forcastInventoryObj.getAvailableImpressions());
						}
						if(forcastInventoryObj.getName()!=null){
							dto.setName(forcastInventoryObj.getName());
						}
						forcastInventoryDTOlist.add(dto);
					}
				}
			}
			
		}catch(Exception e){
			log.severe("Exception in loadAllDMAsWithInventory() of PoolMapService"+e.getMessage());
			e.printStackTrace();
		}
		return forcastInventoryDTOlist;
		
	}
	
	public ForcastInventoryObj copyObject(ForcastInventoryObj sourceObj){
		ForcastInventoryObj destinationObj = new ForcastInventoryObj(sourceObj.getId(),sourceObj.getDFPPropertyName(),sourceObj.getCode(),sourceObj.getAdUnitId(),sourceObj.getPropertyName(),sourceObj.getPublisherId(),
				sourceObj.getAddress(),sourceObj.getZipCode(),sourceObj.getState(),sourceObj.getName(),sourceObj.getCreativeSize(),sourceObj.getStartDate(),
				sourceObj.getEndDate(),sourceObj.getForcastedImpressions(),sourceObj.getAvailableImpressions(),sourceObj.getReservedImpressions(),
				sourceObj.getPublisherName());
		
		destinationObj.setAvailableImpressions(sourceObj.getAvailableImpressions());
		destinationObj.setCode(sourceObj.getCode());
		destinationObj.setName(sourceObj.getName());
		destinationObj.setAvailableImpressions(sourceObj.getAvailableImpressions());
		destinationObj.setPropertyName(sourceObj.getPropertyName());
		destinationObj.setDFPPropertyName(sourceObj.getDFPPropertyName());
		destinationObj.setPublisherId(sourceObj.getPublisherId());
		destinationObj.setPublisherName(sourceObj.getPublisherName());
		return destinationObj;
		
	}
	
	public List<ForcastInventoryDTO> loadAllocateInventry(String startDate, String endDate, String codeList){
		log.info("in loadAllocateInventry() of poolMapService");
		List<ForcastInventoryObj> forcastInventoryObjList = new ArrayList<>();
		List<ForcastInventoryObj> groupForcastList = new ArrayList<>();
		List<ForcastInventoryDTO> allocateInventryList = new ArrayList<>();
		List<ForcastInventoryObj> forcastInventoryStartDateList = new ArrayList<>();
		PoolMapDAO dao = new PoolMapDAO();
		Map<String, ForcastInventoryObj> map = new HashMap<>();
		String key = "";
		try{
			List<String> codeArrList = new ArrayList<String>();
			String[] str = codeList.split(",");
			if(str!=null){
				for (String code : str) {
					codeArrList.add(code);
				}
			}
			forcastInventoryStartDateList = dao.loadAllocateInventry(startDate, endDate, codeList, codeArrList);
            for (ForcastInventoryObj forcastInventoryObj : forcastInventoryStartDateList) {
				
				if(forcastInventoryObj!=null && forcastInventoryObj.getEndDate().compareTo(endDate)<=0){
					
					forcastInventoryObjList.add(forcastInventoryObj);
				}
			}	
			if(forcastInventoryObjList!=null && forcastInventoryObjList.size()>0){
				for (ForcastInventoryObj forcastInventoryObj : forcastInventoryObjList) {
					if(forcastInventoryObj!=null){
						 key = forcastInventoryObj.getCode()+"_"+forcastInventoryObj.getPublisherId()+"_"+forcastInventoryObj.getPublisherName()+"_"+forcastInventoryObj.getDFPPropertyName();
						 if(!map.containsKey(key)){
							 map.put(key, forcastInventoryObj);
						 }else{
							 ForcastInventoryObj obj = copyObject(map.get(key));
							 Long availableImpressions = forcastInventoryObj.getAvailableImpressions() +  obj.getAvailableImpressions();
							 obj.setAvailableImpressions(availableImpressions);
							 map.put(key, obj);
							 
						 }
					}
				}
			}
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) iterator.next();
				groupForcastList.add((ForcastInventoryObj) mapEntry.getValue());
		        }
			
			for (ForcastInventoryObj forcastInventoryObj : groupForcastList) {
				if(forcastInventoryObj!=null){
					ForcastInventoryDTO dto = new ForcastInventoryDTO();
					if(forcastInventoryObj.getCode()!=null){
						dto.setCode(forcastInventoryObj.getCode());
					}
					if(forcastInventoryObj.getAvailableImpressions()!=null){
						dto.setAvailableImpressions(forcastInventoryObj.getAvailableImpressions());
					}
					if(forcastInventoryObj.getDFPPropertyName()!=null){
						dto.setDfpPropertyName(forcastInventoryObj.getDFPPropertyName());
					}
					if(forcastInventoryObj.getPublisherId()!=null){
						dto.setPublisherId(forcastInventoryObj.getPublisherId());
					}
					if(forcastInventoryObj.getPublisherName()!=null){
						dto.setPublisherName(forcastInventoryObj.getPublisherName());
					}
					allocateInventryList.add(dto);
				}
			}
		}catch(Exception e){
				log.severe("Exception in loadAllocateInventry of poolmapService "+e.getMessage());
				e.printStackTrace();
		}
		return allocateInventryList;
		
	}
}
