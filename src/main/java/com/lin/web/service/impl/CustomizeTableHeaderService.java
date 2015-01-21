package com.lin.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.lin.persistance.dao.impl.CustomizeTableHeaderDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.server.bean.CustomizeTableHeaderObj;
import com.lin.web.dto.AdvertiserPerformerDTO;
import com.lin.web.util.DateUtil;

public class CustomizeTableHeaderService {
	private static final Logger log = Logger.getLogger(CustomizeTableHeaderService.class.getName());
	private HttpServletRequest request;
	
	public List<CustomizeTableHeaderObj> advertiserPerformer(List<CustomizeTableHeaderObj> headerList, AdvertiserPerformerDTO perfoemerDTO) throws DataServiceException{
		log.info("In CustomizeTableHeaderService advertiserPerformer method");
		List<AdvertiserReportObj> advertiserReportList=null;
		List<String> campaignLineItemList = new ArrayList<String>();
		List<Long> adServerImpressionsList = new ArrayList<Long>() ;
		List<Long> adServerClicksList = new ArrayList<Long>();
		List<Double> adServerCTRList = new ArrayList<Double>();
		AdvertiserReportObj advertiserReportObj;
		CustomizeTableHeaderDAO dao = new CustomizeTableHeaderDAO();
		String lowerDate=request.getParameter("startDate");
		String upperDate=request.getParameter("endDate");
		if( (lowerDate!=null && lowerDate.trim().length()>0) && (upperDate !=null && upperDate.trim().length()>0)){
			String [] dateArrayU=upperDate.split("-");
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+dateArrayU[2];
			upperDate =dateArrayU[0]+"-"+dateArrayU[1]+"-"+(Integer.parseInt(dateArrayU[2])+1);						
		}else{
			Date currentDate=new Date();
			lowerDate=DateUtil.getModifiedDateStringByDays(currentDate,-7, "yyyy-MM-dd");
			upperDate=DateUtil.getModifiedDateStringByDays(currentDate,1, "yyyy-MM-dd");
		}
		advertiserReportList =  dao.loadPerformingLineItemsForAdvertiser(5, lowerDate, upperDate);
		if(advertiserReportList!=null && advertiserReportList.size()!=0){
			Iterator<AdvertiserReportObj> iterator = advertiserReportList.iterator();
			
			while(iterator.hasNext()){
				advertiserReportObj = iterator.next();
				campaignLineItemList.add(advertiserReportObj.getLineItemName());
				adServerImpressionsList.add(advertiserReportObj.getAdServerImpressions());
				adServerClicksList.add(advertiserReportObj.getAdServerClicks());
				adServerCTRList.add(advertiserReportObj.getAdServerCTR());
				}
			if(campaignLineItemList!=null && campaignLineItemList.size()!=0){
				//perfoemerDTO.setCampaignLineItems(campaignLineItemList);
			}
			
			if(adServerImpressionsList!=null && adServerImpressionsList.size()!=0){
				//perfoemerDTO.setImpressionDelivered(adServerImpressionsList);
			}
			if(adServerClicksList!=null && adServerClicksList.size()!=0){
				//perfoemerDTO.setClicks(adServerClicksList);
			}
			if(adServerCTRList!=null && adServerCTRList.size()!=0){
				//perfoemerDTO.setCTR(adServerCTRList);
			}
			
		}
		
		return null;
		
	}
}
