package com.lin.web.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import com.lin.persistance.dao.IReportDAO;
import com.lin.persistance.dao.IUserDetailsDAO;
import com.lin.persistance.dao.impl.ReportDAO;
import com.lin.persistance.dao.impl.UserDetailsDAO;
import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AccountsEntity;
import com.lin.server.bean.CompanyObj;
import com.lin.server.bean.CorePerformanceReportObj;
import com.lin.server.bean.DFPTargetReportObj;
import com.lin.server.bean.DFPTaskEntity;
import com.lin.server.bean.DataCollectorReport;
import com.lin.server.bean.DataProcessorReport;
import com.lin.server.bean.DataUploaderReport;
import com.lin.server.bean.DfpOrderIdsObj;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.server.bean.LocationReportObj;
import com.lin.server.bean.ProductPerformanceReportObj;
import com.lin.server.bean.RichMediaCommonReportObj;
import com.lin.server.bean.RichMediaCustomEventReportObj;
import com.lin.server.bean.RootReportObj;
import com.lin.server.bean.SellThroughReportObj;
import com.lin.server.bean.TrackCronJobReport;
import com.lin.web.dto.MojivaReportDTO;
import com.lin.web.dto.MojiveReportWebServiceResponse;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IHistoricalReportService;
import com.lin.web.service.IReportService;
import com.lin.web.servlet.GCSUtil;
import com.lin.web.servlet.GCStorageUtil;
import com.lin.web.util.AdExchangeReportUtil;
import com.lin.web.util.CloudStorageUtil;
import com.lin.web.util.CloudStroageFileProcessUtil;
import com.lin.web.util.DFPTableSchemaUtil;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileUtil;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.MemcacheUtil;
import com.lin.web.util.MojivaReportUtil;
import com.lin.web.util.NexageReportUtil;
import com.lin.web.util.ProcessQueryBuilder;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.RichMediaProcessReportUtil;
import com.lin.web.util.StringUtil;

public class HistoricalReportService implements IHistoricalReportService{

	private static final Logger log = Logger.getLogger(HistoricalReportService.class.getName()); 
	/* 
	 * generateNexageReport on cloud storage
	 * @see com.lin.web.service.IReportService#generateNexageReport(java.lang.String, java.lang.String, java.lang.String)
	 * @ Return : reportURL on cloud storage
	 */
	public String generateNexageReport(String startDate,String endDate,String dimensions){
		String reportURL=null;
		String subject="";
		String message="";
		log.info("generateNexageReport: startDate:"+startDate+" & endDate:"+endDate+" & dimensions:"+dimensions);
		
		//List<NexageReport> reportList=new ArrayList<NexageReport>();
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		
		String reportsResponse=NexageReportUtil.getReportsBySite(startDate,endDate);
		if(reportsResponse !=null && (!reportsResponse.contains("IOException"))){
			JSONObject nexageSiteDetails = (JSONObject) JSONSerializer.toJSON(reportsResponse);			
			dimensions="day";	
	        JSONArray jsonArray=nexageSiteDetails.getJSONArray("detail");
	        if(jsonArray.size()==0){
	        	log.warning("No sites loaded, our API failed to get data from nexage, please restart cron job.");
	        	subject="Nexage cron job failed to fecth data - no sites found";
	        	message="Hi, Please check your application log, nexage cron failed for startDate:"
					+startDate+" and endDate:"+endDate+"\n"+"No sites found...";
	        	LinMobileUtil.sendMailOnGAE(
	        			LinMobileVariables.SENDER_EMAIL_ADDRESS, 
	    				LinMobileConstants.TO_EMAIL_ADDRESS,
	    				LinMobileConstants.CC_EMAIL_ADDRESS, 
	    				subject,message);
	        }else{
	        	String fileName=timestamp+"_NEXAGE_CorePerformance_"+startDate+"_"+endDate+"_proc"+".csv";	        	
	        	for(int i=0;i<jsonArray.size();i++){
	    			JSONObject obj=jsonArray.getJSONObject(i);
	    			String site=obj.getString("site");
	    			String siteId=obj.getString("siteId");
	    			String reportsResponseBySiteId=NexageReportUtil.getReportsBySiteId(startDate,endDate,dimensions,siteId);
	    			//List<NexageReport> reportListById=NexageReportUtil.parseJSONResponseFromNexage(reportsResponseBySiteId,siteId,site);
	    			if(reportsResponseBySiteId !=null){
	    				List<CorePerformanceReportObj> reportListById=NexageReportUtil.createNexageCorePerformanceReportData(reportsResponseBySiteId,siteId,site);
		    			log.info("reportListById:"+reportListById.size());
		    			reportList.addAll(reportListById);
	    			}else{
	    				log.warning("Failed to get report for siteId:"+siteId);
	    			}	    			
	    		}
	    		log.info("Nexage reportList: "+reportList.size());
	    		Collections.sort(reportList, CorePerformanceComparator);
	    		
	    		StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(reportList);
	    		//log.info("Nexage CSV Report:"+strBuffer.toString());
	    		
	    		String dirName=LinMobileConstants.REPORT_FOLDER+"/"
	    		                 +LinMobileConstants.NEXAGE_REPORTS_BUCKET+"/"
	    		                 +DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
	    		
	    		try {
	    			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName);
	    		} catch (IOException e) {
	    			log.severe("generateNexageReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
	    			
	    		}		
	    		log.info("generateNexageReport: reportURL:"+reportURL);    		
	        }		
		}else{			
        	
			if(reportsResponse !=null && (reportsResponse.contains("IOException"))){
				subject="Nexage cron job failed to fecth data - IOException";
	        	message="Hi, Please check your application log, nexage cron failed for startDate:"
					+startDate+" and endDate:"+endDate+"\n"+reportsResponse;
			}else{
				subject="Nexage cron job failed to fecth data - no sites found";
	        	message="Hi, Please check your application log, nexage cron failed for startDate:"
					+startDate+" and endDate:"+endDate+"\n"+"No sites found...";
			}
			log.warning("Cron job failed, send email alert...reportsResponse:"+reportsResponse);
			
			LinMobileUtil.sendMailOnGAE(
				LinMobileVariables.SENDER_EMAIL_ADDRESS, 
				LinMobileConstants.TO_EMAIL_ADDRESS,
				LinMobileConstants.CC_EMAIL_ADDRESS, 
				subject,message);
			
		}
		
        return reportURL;
	}
	
/*	public String generateNewNexageReport(String startDate,String endDate,String dimensions){
		String reportURL=null;
		String subject="";
		String message="";
		log.info("generateNewNexageReport: startDate:"+startDate+" & endDate:"+endDate+" & dimensions:"+dimensions);
		
		//List<NexageReport> reportList=new ArrayList<NexageReport>();
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		
		List<String> reportIDList = new ArrayList<String>();
		reportIDList.add("sellerrevenue");
		reportIDList.add("sellertraffic");
		reportIDList.add("selleranalytics");
		
		List<String> dimensionList = new ArrayList<String>();
		dimensionList.add("site");
		dimensionList.add("source");
		dimensionList.add("tag");
		
		for(String reportID : reportIDList)
		{
			for(String dim : dimensionList)
			{
				String reportsResponse=NexageReportUtil.getNexageReportsByDim(startDate,endDate,reportID,dim);
				
				if(reportsResponse !=null && (!reportsResponse.contains("IOException"))){
					List<CorePerformanceReportObj> reportListById=NexageReportUtil.createNewNexageCorePerformanceReportData(reportsResponse,dim);
	    			log.info("reportListById:"+reportListById.size());
	    			reportList.addAll(reportListById);
			    }
				else
				{
			    	log.warning("Failed to get report for dimension :"+dim);
			    }
			    		    		
			}//end of inner for loop
			
		}//end of outer for loop
		
		log.info("Nexage reportList: "+reportList.size());
		
		if(reportList.size() > 0){
			
			String fileName=timestamp+"_NEXAGE_CorePerformance_"+startDate+"_"+endDate+"_proc"+".csv";	
			//Collections.sort(reportList, CorePerformanceComparator);
			
			StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(reportList);
			//log.info("Nexage CSV Report:"+strBuffer.toString());
			
			String dirName=LinMobileConstants.REPORT_FOLDER+"/"
			                 +"new_nexage_reports"+"/"
			                 +DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
			
			try {
				reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName);
			} catch (IOException e) {
				log.severe("generateNexageReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
				
			}
		
		}
		log.info("generateNexageReport: reportURL:"+reportURL);
		
		
        return reportURL;
	}*/
		
		
	/*
	 * generateMojivaReportByDay
	 * @see com.lin.web.service.IReportService#ggenerateMojivaReportByDay(java.lang.String)
	 * @return fileUploadURL on cloud storage
	 */
	public String generateMojivaReportByDay(String date){
		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		String reportURL=null;
		String fileName=timestamp+"_Mojiva_CorePerformance_"+date+"_"+date+"_proc"+".csv";
		MojiveReportWebServiceResponse mojivaResponseObj=MojivaReportUtil.getAllMojivaSites();
		if(mojivaResponseObj !=null){
			List<MojivaReportDTO> resultList=mojivaResponseObj.getSiteList();
			
			List<String> siteIdList=new ArrayList<String>();
			for(MojivaReportDTO obj:resultList){
				siteIdList.add(obj.getId());
			}
			resultList=new ArrayList<MojivaReportDTO>();
			log.info("Mojiva SiteIds :"+siteIdList);
			
			for(String siteId:siteIdList){				
				MojiveReportWebServiceResponse response=MojivaReportUtil.getMojivaSiteReportByDate(date, siteId);
				if(response ==null ){
					log.warning("Report failed to get response...for siteId:"+siteId);
				}else{
					List<MojivaReportDTO> siteWiseList=response.getSiteList();						
					if(siteWiseList.size() > 0){
						log.info("Add in list:  siteId:"+siteId+" : siteWiseList :"+siteWiseList.size()+" : date:"+date);
						resultList.addAll(siteWiseList);
					}else{
						log.warning("Don't get any siteWiseList : for siteId:"+siteId+" : siteWiseList: "+siteWiseList.size()+" date:"+date);
					}
				}				
			}
			log.info("Total DTO objects for mojiva :"+resultList.size());
			List<CorePerformanceReportObj> rootDataList=MojivaReportUtil.createMojivaCorePerformanceReportData(resultList, date);
			
			StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(rootDataList);
			//log.info("Mojiva CSV report:"+strBuffer.toString());
			
			String dirName=LinMobileConstants.REPORT_FOLDER+"/"+LinMobileConstants.MOJIVA_REPORTS_BUCKET+"/"
			               +DateUtil.getFormatedDate(date, "yyyy-MM-dd", "yyyy_MM");			
			try {
				reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName);
			} catch (IOException e) {
				log.severe("generateMojivaReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
				
			}		
			log.info("generateMojivaReport: reportURL:"+reportURL);
		}else{
			log.severe("Failed to get data from Mojiva...");
			reportURL="Failed to get data from Mojiva";			
		}		
		
		return reportURL;
	}
	
	/*
	 * generateMojivaReport
	 * @see com.lin.web.service.IReportService#generateMojivaReport(java.lang.String, java.lang.String)
	 * @return fileUploadURL on cloud storage
	 */
	public String generateMojivaReport(String startDate,String endDate){
		String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
		String reportURL=null;
		String fileName=timestamp+"_Mojiva_CorePerformance_"+startDate+"_"+endDate+"_proc"+".csv";
		
		MojiveReportWebServiceResponse mojivaResponseObj=MojivaReportUtil.getAllMojivaSites();
		if(mojivaResponseObj!= null){
			List<MojivaReportDTO> resultList=mojivaResponseObj.getSiteList();
			
			List<String> siteIdList=new ArrayList<String>();
			for(MojivaReportDTO obj:resultList){
				siteIdList.add(obj.getId());
			}
			resultList=new ArrayList<MojivaReportDTO>();
			log.info("Mojiva SiteIds :"+siteIdList);
			
			int days=(int) DateUtil.getDifferneceBetweenTwoDates(startDate, endDate, "yyyy-MM-dd");
			days=days+1;
			for(int i=0;i<days;i++){
				log.info("fetching data daywise: day:"+i+" and startDate:"+startDate);
				List<MojivaReportDTO> dayWiseList=new ArrayList<MojivaReportDTO>();
				for(String siteId:siteIdList){
					//List<MojivaReportDTO> siteWiseList=MojivaReportUtil.getMojivaSiteReportByDateInterval(startDate,endDate, siteId).getSiteList();
					MojiveReportWebServiceResponse response=MojivaReportUtil.getMojivaSiteReportByDate(startDate,siteId);
					if(response ==null ){
						log.warning("Report failed to get response...for siteId:"+siteId);
					}else{
						List<MojivaReportDTO> siteWiseList=response.getSiteList();						
						if(siteWiseList.size() > 0){
							log.info("Add in list:  siteId:"+siteId+" : siteWiseList :"+siteWiseList.size()+" : startDate:"+startDate);
							dayWiseList.addAll(siteWiseList);
						}else{
							log.warning("Don't get any siteWiseList : for siteId:"+siteId+" : siteWiseList: "+siteWiseList.size()+" startDate:"+startDate);
						}
					}
				}
				Date dt=DateUtil.getFormatedDate(startDate,"yyyy-MM-dd");
				startDate=DateUtil.getModifiedDateStringByDays(dt, 1, "yyyy-MM-dd");				
				resultList.addAll(dayWiseList);				
			}
			
			log.info("Total DTO objects for mojiva :"+resultList.size());
			List<CorePerformanceReportObj> rootDataList=MojivaReportUtil.createMojivaCorePerformanceReportData(resultList, startDate);
			StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(rootDataList);
			
			String dirName=LinMobileConstants.REPORT_FOLDER+"/"+LinMobileConstants.MOJIVA_REPORTS_BUCKET+"/"
			               +DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
			
			try {
				reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName);
			} catch (IOException e) {
				log.severe("generateMojivaReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
				
			}		
			log.info("generateMojivaReport: reportURL:"+reportURL);
		}else{
			log.severe("Failed to get data from Mojiva...");
			reportURL="Failed to get data from Mojiva";			
		}
		
		return reportURL;
	}
	
	
	/*
	 *  Generate AdEx report and save on cloud storage
	 * @see com.lin.web.service.IReportService#generateAdExReport(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String generateAdExReport(String reportsResponse,String startDate,String fileName, String dirName){
		String reportURL=null;
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
				
		try {
			if(reportsResponse!=null && reportsResponse.trim().length()>0){	
				reportList=AdExchangeReportUtil.createAdExCorePerformanceReportData(reportsResponse);
				if(reportList !=null && reportList.size()>0){
					log.info("Total objects for CSV reports: reportList:"+reportList.size());
					StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(reportList);
					//log.info("AdEx CSV Report:"+strBuffer.toString());
					reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName);						
					log.info("generateAdExReport: reportURL:"+reportURL);  
				}else{			
					log.info("No report generated as no records found:");
				}
			}else{
				log.warning("No report generated: some exception occured..");
			}
		}catch (IOException e){
			log.severe("generateAdExReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		} catch (Exception e) {
			log.severe("Exception:"+e.getMessage());
			
		}		
		return reportURL;
	}
	
	
	public String generateDFPReport(String startDate,String endDate,String rawFileName,
			String dirName,String timestamp){
		String reportURL=null;
		
		log.info("generateDFPReport: startDate:"+startDate+" & endDate:"+endDate);		
	    
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		List<CorePerformanceReportObj>  reportList2=new ArrayList<CorePerformanceReportObj>();
		
		Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		
		//allDataMap=CloudStorageUtil.readCorePerformanceDFPRawCSVFromCloudStorage(rawFileName, dirName);
		allDataMap=CloudStorageUtil.readNewCorePerformanceDFPRawCSVFromCloudStorage(rawFileName, dirName);
		
		//log.info("Total objects for CSV reports: allDataMap:"+allDataMap.size());
		reportList=allDataMap.get("CorrectData");
		reportList2=allDataMap.get("InCorrectData");
		log.info("Total objects for CSV reports: CorrectData: reportList :"+reportList.size()+",InCorrectData: reportList2 :"+reportList2.size());
		
		//Collections.sort(reportList, RootReportComparator);
		
		StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(reportList);		
		//log.info("CSV Report:"+strBuffer.toString());		
		try {	
			log.info("Going to upload process file at cloud storage..");
			String procFileName=rawFileName.replace("raw", "proc");
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), procFileName, dirName);
			StringBuffer strBuffer2=new StringBuffer();
			if(reportList2 !=null && reportList2.size()>0){
				strBuffer2=ReportUtil.createCorePerformanceCSVReport(reportList2);
				String rejectFileName=rawFileName.replace("raw", "reject");
				String inCorrectDataReportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer2.toString(), rejectFileName, dirName);
				log.info("inCorrectDataReportURL:"+inCorrectDataReportURL);
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, "InCorrect data found.", "Hi, Incorrect data found while processing DFP Core performance table, please see csv file at cloud storage : "+inCorrectDataReportURL);
			}
		} catch (IOException e) {
			log.severe("generateDFPReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}		
		log.info("generateDFPReport: reportURL:"+reportURL);  
       
        return reportURL;
	}
	
	/*
	 * Report by network code
	 * @see com.lin.web.service.IReportService#generateDFPReport(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String generateDFPReport(String startDate,String endDate,String rawFileName,
			String dirName,String timestamp,String networkCode){
		String reportURL=null;
		
		log.info("generateDFPReport: startDate:"+startDate+" & endDate:"+endDate+" and networkCode:"+networkCode);		
	    
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		List<CorePerformanceReportObj>  reportList2=new ArrayList<CorePerformanceReportObj>();
		
		Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		
		allDataMap=CloudStorageUtil.readCorePerformanceDFPRawCSVFromCloudStorage(rawFileName, dirName,networkCode);
		log.info("Total objects for CSV reports: allDataMap:"+allDataMap.size());
		reportList=allDataMap.get("CorrectData");
		reportList2=allDataMap.get("InCorrectData");
		log.info("Total objects for CSV reports: CorrectData: reportList :"+reportList.size());
		log.info("Total objects for CSV reports: InCorrectData: reportList2 :"+reportList2.size());
		//Collections.sort(reportList, RootReportComparator);
		
		StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(reportList);		
		//log.info("CSV Report:"+strBuffer.toString());		
		try {			
			String procFileName=rawFileName.replace("raw", "proc");
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), procFileName, dirName);
			StringBuffer strBuffer2=new StringBuffer();
			if(reportList2 !=null && reportList2.size()>0){
				strBuffer2=ReportUtil.createCorePerformanceCSVReport(reportList2);
				String rejectFileName=rawFileName.replace("raw", "reject");
				String inCorrectDataReportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer2.toString(), rejectFileName, dirName);
				log.info("inCorrectDataReportURL:"+inCorrectDataReportURL);
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, "InCorrect data found.", "Hi, Incorrect data found while processing DFP Core performance table, please see csv file at cloud storage : "+inCorrectDataReportURL);
			}
		} catch (IOException e) {
			log.severe("generateDFPReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}		
		log.info("generateDFPReport: reportURL:"+reportURL);  
       
        return reportURL;
	}
	
	/*
	 * generateLinDigitalDFPReport -- For LinDigital
	 * @param String startDate,String endDate,String rawFileName,
			String dirName
		@return String reportURL
	 */
	public String generateLinDigitalDFPReport(String startDate,String endDate,String rawFileName,
			String dirName){
		String reportURL=null;
		
		log.info("startDate:"+startDate+" & endDate:"+endDate);		
	    
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		List<CorePerformanceReportObj>  reportList2=new ArrayList<CorePerformanceReportObj>();
		
		Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		
		allDataMap=CloudStorageUtil.readLinDigitalDFPRawCSVFromCloudStorage(rawFileName, dirName);
		log.info("Total objects for CSV reports: allDataMap:"+allDataMap.size());
		reportList=allDataMap.get("CorrectData");
		reportList2=allDataMap.get("InCorrectData");
		log.info("Total objects for CSV reports: CorrectData: reportList :"+reportList.size());
		log.info("Total objects for CSV reports: InCorrectData: reportList2 :"+reportList2.size());
		
		StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(reportList);		
		
		try {			
			String procFileName=rawFileName.replace("raw", "proc");
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), procFileName, dirName);
			StringBuffer strBuffer2=new StringBuffer();
			if(reportList2 !=null && reportList2.size()>0){
				strBuffer2=ReportUtil.createCorePerformanceCSVReport(reportList2);
				String rejectFileName=rawFileName.replace("raw", "reject");
				String inCorrectDataReportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer2.toString(), rejectFileName, dirName);
				log.info("inCorrectDataReportURL:"+inCorrectDataReportURL);
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, "InCorrect data found.", "Hi, Incorrect data found while processing DFP Core performance table, please see csv file at cloud storage : "+inCorrectDataReportURL);
			}
		} catch (IOException e) {
			log.severe("generateDFPReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}		
		log.info("generateDFPReport: reportURL:"+reportURL);  
       
        return reportURL;
	}
	
	
	public String generateDFPReportByLocation(String startDate,String endDate,String rawFileName,
			String dirName,String timestamp){
        String reportURL=null;
		String bucketName=LinMobileVariables.APPLICATION_BUCKET_NAME;
		log.info("generateDFPReportByLocation: startDate:"+startDate+" & endDate:"+endDate);
	
		
		List<LocationReportObj> reportList=new ArrayList<LocationReportObj>();
		
		reportList=CloudStorageUtil.readDFPReportByLocationCSVFromCloudStorage(rawFileName, dirName,
				LinMobileConstants.DFP_NETWORK_CODE,null,null,bucketName);
		
		log.info("Total objects for CSV reports: CorrectData: reportList :"+reportList.size());
		StringBuffer strBuffer=ReportUtil.createLocationSchemaCSVReport(reportList);		
		//log.info("CSV Report:"+strBuffer.toString());		
		try {			
			String proccessFileName=rawFileName.replace("raw", "proc");
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), proccessFileName, dirName);
		} catch (IOException e) {
			log.severe("generateDFPReportByLocation :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}		
		log.info("reportURL:"+reportURL);  
       
        return reportURL;
	}
	
	/*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate PerformanceByLocation report from cloud stoarge
     *  
     * @param String rawFileName,String proccessFileName,String dirName,String timestamp
     * @return String reportURL
     */
	public String generateDFPReportByLocation(String rawFileName,String proccessFileName,
			String dirName,String timestamp){
		
        String reportURL=null;		
		List<LocationReportObj> reportList=new ArrayList<LocationReportObj>();		
		reportList=CloudStorageUtil.readDFPReportByLocationCSVFromCloudStorage(rawFileName, dirName,
				LinMobileConstants.DFP_NETWORK_CODE,null,null,LinMobileVariables.APPLICATION_BUCKET_NAME);
		log.info("Total objects for CSV reports: CorrectData: reportList :"+reportList.size());
		StringBuffer strBuffer=ReportUtil.createLocationSchemaCSVReport(reportList);
		try {			
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), proccessFileName, dirName);
		} catch (IOException e) {
			log.severe("generateDFPReportByLocation :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}       
        return reportURL;
	}
	
	
	/*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate PerformanceByLocation report from cloud stoarge and split it if file is too large
     *  
     * @param String rawFileName,String proccessFileName,String dirName,String networkCode
     * @return List<String> fileList
     */
	public List<String> generateDFPReportByLocationWithSplit(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String publisherId,String publisherName,String bucketName){
		List<String> fileList=new ArrayList<String>();
        String reportURL=null;		
		List<LocationReportObj> reportList=new ArrayList<LocationReportObj>();
		List<LocationReportObj> subReportList=new ArrayList<LocationReportObj>();
		if(networkCode !=null ){			
			reportList=CloudStorageUtil.readDFPReportByLocationCSVFromCloudStorage(rawFileName, dirName,
					networkCode,publisherId,publisherName,bucketName);
		}else{
			log.warning("No data available for this network code:"+networkCode);
		}
		
		int total=reportList.size();
		log.info("Total objects for CSV reports: reportList size:"+total);
		int count=total/10000;
		if(total >10000){
			log.info("Going to create multiple csv ....");
			int i=0;
			while(i<=count){
				int start=i*10000;
				int end=start+10000-1;
				if(i==count){
					start=i*10000;
					end=total-1;
				}
				log.info("Create sub list:start: "+start+" and end:"+end);
				subReportList=reportList.subList(start , end);
				StringBuffer strBuffer=ReportUtil.createLocationSchemaCSVReport(subReportList);
				try {			
					String splitFileName=proccessFileName.replace("_proc", "_proc_"+i);
					reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), splitFileName, dirName,bucketName);
					if(reportURL !=null){
						fileList.add(reportURL);
					}
				} catch (IOException e) {
					log.severe("generateDFPReportByLocation :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
					
				}      
				i++;
			}
		}else{
			log.info("Going to create complete csv ....");
			StringBuffer strBuffer=ReportUtil.createLocationSchemaCSVReport(reportList);
			try {
				reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), proccessFileName, dirName,bucketName);
				if(reportURL !=null){
					fileList.add(reportURL);
				}
				
			} catch (IOException e) {
				log.severe("generateDFPReportByLocation :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
				
			}      
		}
        return fileList;
	}
	
	
	/*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate PerformanceByLocation report from cloud stoarge
     *  
     * @param String rawFileName,String proccessFileName,String dirName,String networkCode
     *        String publisherId,String publisherName,String bucketName
     * @return List<String> fileList
     */
	public List<String> generateDFPReportByLocation(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String publisherId,String publisherName,String bucketName){
		List<String> fileList=new ArrayList<String>();
        String reportURL=null;		
		List<LocationReportObj> reportList=new ArrayList<LocationReportObj>();
		//List<LocationReportObj> subReportList=new ArrayList<LocationReportObj>();
		if(networkCode !=null ){			
			reportList=CloudStorageUtil.readDFPReportByLocationCSVFromCloudStorage(rawFileName, dirName,
					networkCode,publisherId,publisherName,bucketName);
		}else{
			log.warning("No data available for this network code:"+networkCode);
		}
		
		int total=reportList.size();
		log.info("Total objects for CSV reports: reportList size:"+total);
		
		StringBuffer strBuffer=ReportUtil.createLocationSchemaCSVReport(reportList);
	   	try {
			if(total>0){
				//reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), proccessFileName, dirName,bucketName);
				//GCStorageUtil gcsUtil=new GCStorageUtil();
				//reportURL=gcsUtil.uploadReportOnCloudStorage(strBuffer.toString(), proccessFileName, dirName,bucketName);
				GCSUtil gcsUtil=new GCSUtil();
				reportURL=gcsUtil.uploadFileUsingGCSClient(strBuffer.toString(), proccessFileName, dirName,bucketName);
				if(reportURL !=null){
					fileList.add(reportURL);
				}else{
					log.severe("Failed to upload file at cloud storage : reportURL: "+reportURL+" and proccessFileName"+proccessFileName);
				}
			}else{
				log.severe("Failed to processed file, check raw file. failed proccessFileName:"+proccessFileName);
			}
			
		} catch (IOException e) {
			log.severe("generateDFPReportByLocation :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());			
		}  
		
		log.info("returning file list......");
        return fileList;
	}
	
	/*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate dfp report for CorePerformance
     *  
     * @param String rawFileName,String proccessFileName,String dirName,String timestamp
     * @return String reportURL
     */
	public String generateDFPReport(String rawFileName,String proccessFileName,
			String dirName,String timestamp){
		String reportURL=null;
	    
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		List<CorePerformanceReportObj>  reportList2=new ArrayList<CorePerformanceReportObj>();
		
		Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		
		//allDataMap=CloudStorageUtil.readCorePerformanceDFPRawCSVFromCloudStorage(rawFileName, dirName);
		allDataMap=CloudStorageUtil.readNewCorePerformanceDFPRawCSVFromCloudStorage(rawFileName, dirName);
		log.info("Total objects for CSV reports: allDataMap:"+allDataMap.size());
		reportList=allDataMap.get("CorrectData");
		reportList2=allDataMap.get("InCorrectData");
		log.info("Total objects for CSV reports: CorrectData: reportList :"+reportList.size());
		log.info("Total objects for CSV reports: InCorrectData: reportList2 :"+reportList2.size());
		//Collections.sort(reportList, RootReportComparator);
		
		StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(reportList);		
		//log.info("CSV Report:"+strBuffer.toString());		
		try {			
			
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), proccessFileName, dirName);
			StringBuffer strBuffer2=new StringBuffer();
			if(reportList2 !=null && reportList2.size()>0){
				strBuffer2=ReportUtil.createCorePerformanceCSVReport(reportList2);
				String rejectFileName=proccessFileName.replace("_proc", "_reject");
				String inCorrectDataReportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer2.toString(), rejectFileName, dirName);
				log.info("inCorrectDataReportURL:"+inCorrectDataReportURL);
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, "InCorrect data found.", "Hi, Incorrect data found while processing DFP Core performance table, please see csv file at cloud storage : "+inCorrectDataReportURL);
			}
		} catch (IOException e) {
			log.severe("generateDFPReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}		
		log.info("generateDFPReport: reportURL:"+reportURL);  
       
        return reportURL;
	}
	
	/*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will load data collector report entities from datastore for processing
     *  
     * @param String downloadStatus
     * @return List<DataCollectorReport> reportList
     */
	public List<DataCollectorReport> loadDataCollectorReportToDownload(String downloadStatus){
		List<DataCollectorReport> reportList=null;
		IReportDAO reportDAO=new ReportDAO();
		try {
			reportList=reportDAO.loadDataCollectorReportByDownloadStatus(downloadStatus);
		} catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
		}
		return reportList;
		
	}
	
	
	
	/*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will save data framework report in datastore
     *  
     * @param Object obj(DataCollectorReport,DataProcessorReport,DataUploaderReport etc)
     * 
     */
    public void saveDataFrameworkReport(Object obj){
    	IReportDAO reportDAO=new ReportDAO();
    	try {
			reportDAO.saveObject(obj);
			log.info("Object saved successfully in datastore ");
		} catch (DataServiceException e) {
			log.severe("Object failed to save in datastore :: "+e.getMessage());
			
		}
	}
   
        
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate undertone report
     *  
     * @param String rawFileName,String procFileName,String dirName,String bucketName
     */
    public String generateUndertoneReport(String rawFileName,String procFileName,String dirName,String bucketName){
    	String reportPath=null;
    	log.info("Going to process raw file: "+rawFileName);    	
    	List<CorePerformanceReportObj> reportList=CloudStorageUtil.createUndertoneCSVReportFromCloudStorage(rawFileName, dirName,bucketName);
    	log.info("reportList:"+reportList.size());
    	if(reportList!=null && reportList.size()>0){
    		StringBuffer csvDate=ReportUtil.createCorePerformanceCSVReport(reportList);
    		//log.info("csvDate:"+csvDate.toString());
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate sell through report
     *  
     * @param String rawFileName,String procFileName,String dirName
     * @return String reportPath
     */
    public String generateSellThroughReport(String rawFileName,String procFileName,
    		String dirName,String networkCode,String bucketName){
    	String reportPath=null;
    	log.info("Going to process raw file: "+rawFileName);    	
    	List<SellThroughReportObj> reportList=CloudStorageUtil.createSellThroughCSVReportFromCloudStorage(
    			rawFileName, dirName,networkCode,bucketName);
    	log.info("reportList:"+reportList.size());
    	if(reportList!=null && reportList.size()>0){
    		StringBuffer csvData=ReportUtil.createSellThroughSchemaCSVReport(reportList);    	
    		//log.info("csvData:"+csvData.toString());
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvData.toString(), procFileName, dirName,bucketName);
    			
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will find the download file path from datastore and process it 
     *  and save in datastore
     *  
     * @param int limit
     * @return boolean
     */
    public boolean processReports(int limit){
    	List<DataCollectorReport> reportList=null;
		IReportDAO reportDAO=new ReportDAO();
		try {
			log.info("Going to fetch files to process: limit"+limit);
			reportList=reportDAO.loadReportsToProcess(limit);			
			if (reportList != null && reportList.size()>0) {
				log.info("loaded reports to process : reportList size:"+reportList.size());
				for (DataCollectorReport collectorObj : reportList) {

					String startDate = collectorObj.getStartDate();
					String endDate = collectorObj.getEndDate();
					String rawFileName = collectorObj.getReportName();
					String timestamp = rawFileName.substring(0,rawFileName.indexOf("_"));					
					String procFileName = rawFileName.replace("_raw", "_proc");					
					String dirName =collectorObj.getDirName();
					String reportPathOnStorage=null;
					String dataSource=collectorObj.getDataSource();
					String reportType=collectorObj.getReportType();
					log.info("File to be processed: dataSource:"+dataSource+" and reportType:"+reportType);
					if(dataSource !=null){
						if(dataSource.equalsIgnoreCase("DFP")){
							if(reportType.equalsIgnoreCase(LinMobileConstants.REPORT_TYPE_DFP_CORE_PERFORMANCE)){
								reportPathOnStorage=generateDFPReport(rawFileName,procFileName, 
										dirName, timestamp);
							}else if(reportType.equalsIgnoreCase(LinMobileConstants.REPORT_TYPE_DFP_PERFORMANCE_BY_LOCATION)){
								reportPathOnStorage=generateDFPReportByLocation(rawFileName,procFileName
										, dirName, timestamp);
							}else if(reportType.equalsIgnoreCase(LinMobileConstants.REPORT_TYPE_DFP_SELL_THROUGH)){
								reportPathOnStorage=generateSellThroughReport(rawFileName, procFileName,
										dirName,LinMobileConstants.DFP_NETWORK_CODE,LinMobileVariables.APPLICATION_BUCKET_NAME);
							}else{
								log.warning("Invalid reportType :reportType:"+reportType);
							}
						}else if(dataSource.equalsIgnoreCase("Undertone")){
							reportPathOnStorage=generateUndertoneReport(rawFileName,procFileName, dirName,
									LinMobileVariables.APPLICATION_BUCKET_NAME);
						}else if(dataSource.equalsIgnoreCase("Google AdExchange") 
								|| dataSource.equalsIgnoreCase("Nexage")
								|| dataSource.equalsIgnoreCase("Mojiva")){
							reportPathOnStorage=generateDFPReport(rawFileName,procFileName, 
									dirName, timestamp);
						}else if(dataSource.equalsIgnoreCase("LSN")){
							reportPathOnStorage=generateLSNReport(rawFileName,procFileName, dirName);
						}else if(dataSource.equalsIgnoreCase("Celtra")){
							reportPathOnStorage=generateCeltraReport(rawFileName,procFileName, dirName);
						}else if(dataSource.equalsIgnoreCase("XAd")){
							reportPathOnStorage=generateXAdReport(rawFileName,procFileName, dirName);
						}else if(dataSource.equalsIgnoreCase("Tribune")){
							reportPathOnStorage=generateTribuneReport(rawFileName,procFileName, dirName);
						}else{
							log.warning("Invalid dataSource :dataSource:"+dataSource);
						}
						
					}else{
						log.warning("No data source found for this report: so not able to identify it: dataSource:"+dataSource+" and reportType:"+reportType);
					}
					
					String updatedOn = DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
					collectorObj.setUpdatedOn(updatedOn);					
					
					if(reportPathOnStorage !=null){
							log.info("Report processed and saved at cloud storage :reportPathOnStorage:" + reportPathOnStorage);
							collectorObj.setDownloadStatus("1");
							collectorObj.setProcessedStatus("1");
							collectorObj.setStatus("1");
							
							reportDAO.saveObject(collectorObj);
						    log.info("DataCollectorReport updated in datastore..ReportId: "+collectorObj.getReportId());
						      
							DataProcessorReport processReport = new DataProcessorReport();
							processReport.setReportName(procFileName);
							processReport.setReportId(collectorObj.getReportId());
							processReport.setId(collectorObj.getId());
							processReport.setReportName(procFileName);
							processReport.setUpdatedOn(updatedOn);
							processReport.setProcessedStatus("1");
							processReport.setUploadBQStatus("0");
							processReport.setStatus("1");
							processReport.setStartDate(startDate);
							processReport.setEndDate(endDate);
							processReport.setReportPath(reportPathOnStorage);
							processReport.setDirName(dirName);
							processReport.setReportType(reportType);
							processReport.setDataSource(dataSource);							
							
							reportDAO.saveObject(processReport);
							log.info("DataProcessReport updated in datastore..ReportId: "+collectorObj.getReportId());
							
				  }else{
						log.warning("Report failed to process: reportPathOnStorage:"+reportPathOnStorage);
						collectorObj.setDownloadStatus("1");
						collectorObj.setProcessedStatus("0");
						collectorObj.setStatus("2");
						collectorObj.setErrorLog("Report failed to process: reportPathOnStorage:"+reportPathOnStorage);
						reportDAO.saveObject(collectorObj);
					    log.info("DataCollectorReport updated in datastore..ReportId: "+collectorObj.getReportId());
					    
					    String subject= "Data processing cron job falied";  
						String message="Please check the log, cron job failed to process report.\nreportPathOnStorage:"+reportPathOnStorage;
						LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS,subject, message);					
				  }
			    		
			      
			    } // end of for eachloop
		    }else{
				log.info("No report found to be processed");
		    }
		    return true;
	    } catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return false;
		}	
    }
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * It will find the processed file path from datastore and upload it on BigQuery
     * @param int limit
     * @return boolean
     */
    public boolean uploadReportsOnBigQuery(int limit){
    	String message="";
		String subject="";
    	List<DataProcessorReport> reportList=null;
		IReportDAO reportDAO=new ReportDAO();
		try {
			reportList=reportDAO.loadReportsToUploadBQ(limit);			
			if (reportList != null && reportList.size()>0) {
				log.info("loaded reports to upload : reportList size:"+reportList.size());
				for (DataProcessorReport processorObj : reportList) {

					String startDate = processorObj.getStartDate();
					String endDate = processorObj.getEndDate();
					String procFileName = processorObj.getReportName();						
					String dirName =processorObj.getDirName();
					String reportPathOnStorage=processorObj.getReportPath();
					String dataSource=processorObj.getDataSource();
					String reportType=processorObj.getReportType();
					
					String uploadedResponse=null;
					if(dataSource !=null){
						if(dataSource.equalsIgnoreCase("DFP")){
							if(reportType.equalsIgnoreCase(LinMobileConstants.REPORT_TYPE_DFP_CORE_PERFORMANCE)){
								uploadedResponse=uploadDataOnBigQuery(reportPathOnStorage,
										LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA,
										LinMobileConstants.CORE_PERFORMANCE_TABLE_ID);
							}else if(reportType.equalsIgnoreCase(LinMobileConstants.REPORT_TYPE_DFP_PERFORMANCE_BY_LOCATION)){
								uploadedResponse=uploadDataOnBigQuery(reportPathOnStorage,
										LinMobileConstants.PERFORMANCE_BY_LOCATION_TABLE_SCHEMA,
										LinMobileConstants.PERFORMANCE_BY_LOCATION_TABLE_ID);
							}else if(reportType.equalsIgnoreCase(LinMobileConstants.REPORT_TYPE_DFP_SELL_THROUGH)){
								uploadedResponse=uploadDataOnBigQuery(reportPathOnStorage,
										LinMobileConstants.SELL_THROUGH_TABLE_SCHEMA,
										LinMobileConstants.SELL_THROUGH_TABLE_ID);
							}else{
								log.warning("Invalid dataSource :reportType:"+reportType);
							}
						}else if(dataSource.equalsIgnoreCase("Google AdExchange") 
								|| dataSource.equalsIgnoreCase("Nexage")
								|| dataSource.equalsIgnoreCase("Mojiva")
								|| dataSource.equalsIgnoreCase("Undertone")
								|| dataSource.equalsIgnoreCase("LSN") 
								|| dataSource.equalsIgnoreCase("Celtra")
								|| dataSource.equalsIgnoreCase("XAd")
								|| dataSource.equalsIgnoreCase("Tribune")){
							
							uploadedResponse=uploadDataOnBigQuery(reportPathOnStorage,
									LinMobileConstants.CORE_PERFORMANCE_TABLE_SCHEMA,
									LinMobileConstants.CORE_PERFORMANCE_TABLE_ID);
						}else{
							log.warning("Invalid dataSource :dataSource:"+dataSource);
						}							
						
					}else{
						log.warning("No data source found for this report: so not able to identify it: dataSource:"+dataSource+" and reportType:"+reportType);
					}
					
					String report=dataSource+"-"+reportType;
					String updatedOn = DateUtil.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss");
					processorObj.setUpdatedOn(updatedOn);					
					
					DataUploaderReport uploadedReportObj = new DataUploaderReport();
					uploadedReportObj.setUpdatedOn(updatedOn);
					
					if(uploadedResponse !=null && uploadedResponse.equals("Success")){
							log.info("Report uploaded on BigQuery :uploadedResponse:" + uploadedResponse);
							processorObj.setUploadBQStatus("1");
							processorObj.setProcessedStatus("1");
							processorObj.setStatus("1");
						    
						    uploadedReportObj.setReportName(procFileName);
							uploadedReportObj.setReportId(processorObj.getReportId());
							uploadedReportObj.setId(processorObj.getId());
							uploadedReportObj.setReportName(procFileName);
							uploadedReportObj.setUpdatedOn(updatedOn);
							uploadedReportObj.setUploadBQStatus("1");
							uploadedReportObj.setStatus("1");
							uploadedReportObj.setStartDate(startDate);
							uploadedReportObj.setEndDate(endDate);
							uploadedReportObj.setReportPath(reportPathOnStorage);
							uploadedReportObj.setDirName(dirName);
							uploadedReportObj.setReportType(reportType);
							uploadedReportObj.setDataSource(dataSource);
							
							reportDAO.saveObject(uploadedReportObj);
							log.info("DataProcessorReport updated in datastore..ReportId: "+processorObj.getReportId());
							
							subject="Data uploaded successfully on BigQuery for :"+report;
							message=report+" data uploaded successfully on BigQuery.";
							
                   }else{
						log.warning("Report failed to upload on BigQuery: uploadedResponse:"+uploadedResponse);
						processorObj.setUploadBQStatus("0");
						processorObj.setProcessedStatus("1");
						processorObj.setStatus("2");
						processorObj.setErrorLog("Report failed to upload on BigQuery: uploadedResponse:"+uploadedResponse);
						
						subject="Failed to upload data on BigQuery for :"+report;
						message=report+" data failed to upload on BigQuery. \n Please see BigQueryResponse:"+uploadedResponse;
				   }
					
				    reportDAO.saveObject(processorObj);
				    log.info("DataProcessorReport updated in datastore..ReportId: "+processorObj.getReportId());  
			      
			    } // end of for eachloop
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, subject, message);
		    }else{
				log.info("No report found to be upload on BigQuery");
		    }
		    return true;
	    } catch (DataServiceException e) {
			log.severe("DataServiceException :"+e.getMessage());
			
			return false;
		}	
    }
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * It will upload csv from cloud storage to bigquery
     * @param String responseURL, String schemaFile,String tableId
     * @return String bigQueryResponse
     */
	private String uploadDataOnBigQuery(String responseURL, String schemaFile,
			String tableId) {

		String bigQueryResponse = null;

		try {
			log.info("Before saving data in bigquery");
			bigQueryResponse = BigQueryUtil.saveData(responseURL, schemaFile,
					tableId);
			log.info("bigQueryResponse:" + bigQueryResponse);

		} catch (IOException e) {
			log.severe("IOException:" + e.getMessage());
			
		} catch (GeneralSecurityException e) {
			log.severe("Exception in saving data in bigquery::"+ e.getMessage());
			
		}

		return bigQueryResponse;
	}
    
	/*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * It will load cron startDate and cron endDate
     * @param String 
     * @return TrackCronJobReport 
     */
	public TrackCronJobReport getCronJobData(String reportType){
		TrackCronJobReport obj=null;
		log.info("getCronJobdata: reportType:"+reportType);
		IReportDAO reportDAO=new ReportDAO();
		try {
			List<TrackCronJobReport> resultList=reportDAO.loadCronJobData(reportType);
			if(resultList!=null && resultList.size()>0){
				log.info("TrackCronJobReport: resultList"+resultList.size());
				obj=resultList.get(0);
			}else{
				log.info("TrackCronJobReport: resultList :null");
			}
			
		} catch (DataServiceException e) {
			log.severe("getCronJobdata: DataServiceException:"+e.getClientMessage());
			
			
		}
		return obj;
	}
	
	/*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * It will save initial cron data in datastore
     * @param TrackCronJobReport
     * @return boolean
     */
	public boolean saveCronJobData(TrackCronJobReport obj){
		log.info("saveCronJobData: reportType:"+obj.getReportType());
		IReportDAO reportDAO=new ReportDAO();
		try {
			reportDAO.saveObject(obj);
			return true;
		} catch (DataServiceException e) {
			log.severe("saveCronJobData: DataServiceException:"+e.getClientMessage());
			
			return false;
		}
	}
	
	
	 /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate LSN report
     *  
     * @param String rawFileName,String procFileName,String dirName
     * @return String reportPath
     */
    public String generateLSNReport(String rawFileName,String procFileName,String dirName){
    	String reportPath=null;
    	log.info("Going to process raw file: "+rawFileName);
    	//List<CorePerformanceReportObj> reportList=LSNReportUtil.createLSNCorePerformanceCSVReport(filePath);
    	List<CorePerformanceReportObj> reportList=CloudStorageUtil.createLSNCSVReportFromCloudStorage(rawFileName, dirName);
    	if(reportList!=null && reportList.size()>0){
    		log.info("reportList:"+reportList.size());
    		StringBuffer csvDate=ReportUtil.createCorePerformanceCSVReport(reportList);
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate Celtra report
     *  
     * @param String rawFileName,String procFileName,String dirName
     * @return String reportPath
     */
    public String generateCeltraReport(String rawFileName,String procFileName,String dirName){
    	String reportPath=null;
    	log.info("Going to process raw file: "+rawFileName+" : file dirName : "+dirName);    	
    	List<CorePerformanceReportObj> reportList=null; //CloudStorageUtil.createCeltraCSVReportFromCloudStorage(rawFileName, dirName);
    /*	if(reportList!=null && reportList.size()>0){
    		log.info("reportList:"+reportList.size());
    		StringBuffer csvDate=ReportUtil.createCorePerformanceCSVReport(reportList);
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}*/
    	reportPath="These is no file processor for celtra. Please create a file processor first.";
    	log.warning("These is no file processor for celtra. Please create a file processor first.");
    	return reportPath;
    }
    
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate XAd report
     *  
     * @param String rawFileName,String procFileName,String dirName
     * @return String reportPath
     */
    public String generateXAdReport(String rawFileName,String procFileName,String dirName){
    	String reportPath=null;
    	log.info("Going to process raw file: "+rawFileName);    	
    	List<CorePerformanceReportObj> reportList=null;
    	//CloudStorageUtil.createXAdCSVReportFromCloudStorage(rawFileName, dirName);
    	/*if(reportList!=null && reportList.size()>0){
    		log.info("reportList:"+reportList.size());
    		StringBuffer csvDate=ReportUtil.createCorePerformanceCSVReport(reportList);
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}*/
    	reportPath="These is no file processor for celtra. Please create a file processor first.";
    	log.warning(reportPath);
    	return reportPath;
    }
    
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate Tribune report
     *  
     * @param String rawFileName,String procFileName,String dirName
     * @return String reportPath
     */
    public String generateTribuneReport(String rawFileName,String procFileName,String dirName){
    	String reportPath=null;
    	log.info("Going to process raw file: "+rawFileName);    	
    	List<CorePerformanceReportObj> reportList=CloudStorageUtil.createTribuneCSVReportFromCloudStorage(rawFileName, dirName);
    	if(reportList!=null && reportList.size()>0){
    		log.info("reportList:"+reportList.size());
    		StringBuffer csvDate=ReportUtil.createCorePerformanceCSVReport(reportList);
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate RichMedia trafficking report for LinOne-Golfsmith
     *  
     * @param String rawFileName,String procFileName,String dirName,String dataSource
     * @return String reportPath
     */
    public String generateRichMediaTraffickingReport(String rawFileName,String procFileName,String dirName,String dataSource){
    	String reportPath=null;
    	List<RichMediaCommonReportObj> reportList=null;
    	//List<RichMediaReportTraffickingObj> reportList=CloudStorageUtil.createRichMediaTraffickingCSVReportFromCloudStorage(rawFileName, dirName,dataSource);
    	log.info("Going to process raw file: "+rawFileName+" dataSource:"+dataSource);
    	if(dataSource !=null && dataSource.equalsIgnoreCase(LinMobileConstants.DFP_DATA_SOURCE)){
    		reportList=CloudStorageUtil.createRichMediaLinOneCSVReportFromCloudStorage(rawFileName, dirName);
    	}else if(dataSource !=null && dataSource.equalsIgnoreCase(LinMobileConstants.CELTRA_CHANNEL_NAME)){
    		reportList=CloudStorageUtil.createRichMediaCeltraTraffickingCSVReportFromCloudStorage(rawFileName, dirName);
    	}else if(dataSource !=null && dataSource.equalsIgnoreCase(LinMobileConstants.XAD_CHANNEL_NAME)){
    		reportList=CloudStorageUtil.createRichMediaXAdTraffickingCSVReportFromCloudStorage(rawFileName, dirName);
    	}else{
    		log.warning("Not a valid rich media dataSource:"+dataSource);
    	} 
    	
    	if(reportList!=null && reportList.size()>0){
    		log.info("reportList:"+reportList.size());
    		//StringBuffer csvDate=ReportUtil.createRichMediaTraffickingCSVReport(reportList);
    		StringBuffer csvDate=ReportUtil.createGolfsmithCommonRichMediaCSVReport(reportList);    		
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    
       
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate RichMedia custom event report
     *  
     * @param String rawFileName,String procFileName,
    		String dirName,String dataSource,String networkCode,String publisherId,String publisherName,String bucketName
     * @return String reportPath
     */
    public String generateRichMediaCustomEventReport(String rawFileName,String procFileName,
    		String dirName,String dataSource,String networkCode,String publisherId,String publisherName,String bucketName){
    	String reportPath=null;
    	List<RichMediaCustomEventReportObj> reportList=null;
    	log.info("Going to process raw file: "+rawFileName+" dataSource:"+dataSource); 
    	if(dataSource!=null && dataSource.equals(LinMobileConstants.DFP_DATA_SOURCE)){
    		reportList=RichMediaProcessReportUtil.processRichMediaCustomEventRawCSVFromCloudStorage(
    				rawFileName, dirName,dataSource,networkCode,publisherId,publisherName,bucketName);
    	}else if(dataSource!=null && dataSource.equals(LinMobileConstants.CELTRA_CHANNEL_NAME)){
    		reportList=RichMediaProcessReportUtil.createRichMediaCeltraCustomEventCSVReportFromCloudStorage(
    				rawFileName, dirName,dataSource,bucketName);
    	}else if(dataSource!=null && dataSource.equals(LinMobileConstants.XAD_CHANNEL_NAME)){
    		reportList=RichMediaProcessReportUtil.createRichMediaCustomEventXAdCSVReportFromCloudStorage(
    				rawFileName, dirName,bucketName);
    	}else{
    		log.warning("Not a valid custom event dataSource:"+dataSource);
    	} 
    	
    	
    	if(reportList!=null && reportList.size()>0){
    		log.info("reportList:"+reportList.size());
    		StringBuffer csvDate=ReportUtil.createRichMediaCustomEventCSVReport(reportList);
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName,bucketName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    /*
     * @author Naresh Pokhriyal(naresh.pokhriyal@mediaagility.com)
     * 
     * It will generate RichMedia video campaign report
     *  
     * @param String rawFileName,String procFileName,
    		String dirName,String dataSource,String networkCode,String publisherId,String publisherName,String bucketName
     * @return String reportPath
     */
    public String generateRichMediaVideoCampaignReport(String rawFileName,String procFileName,
    		String dirName,String dataSource,String networkCode,String publisherId,String publisherName,String bucketName){
    	String reportPath=null;
    	List<RichMediaCommonReportObj> reportList=null;
    	log.info("Going to process raw file: "+rawFileName+" dataSource:"+dataSource); 
    	if(dataSource!=null && dataSource.equals(LinMobileConstants.DFP_DATA_SOURCE)){
    		reportList=RichMediaProcessReportUtil.processRichMediaVideoCampaignRawCSVFromCloudStorage(
    				rawFileName, dirName,dataSource,networkCode,publisherId,publisherName,bucketName);
    	}else{
    		log.warning("Not a valid custom event dataSource:"+dataSource);
    	} 
    	
    	
    	if(reportList!=null && reportList.size()>0){
    		log.info("reportList:"+reportList.size());
    		StringBuffer csvDate=ReportUtil.createRichMediaVideoCampaignCSVReport(reportList);
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName,bucketName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate RichMedia custom event report for LinOne , Golfsmith
     *  
     * @param String rawFileName,String procFileName,String dirName,String dataSource
     * @return String reportPath
     */
    public String generateLinOneRichMediaCustomEventReport(String rawFileName,String procFileName
    		,String dirName,String dataSource){
    	String reportPath=null;
    	List<RichMediaCustomEventReportObj> reportList=null;
    	log.info("Going to process raw file: "+rawFileName+" dataSource:"+dataSource); 
    	if(dataSource!=null && dataSource.equals(LinMobileConstants.DFP_DATA_SOURCE)){
    		reportList=CloudStorageUtil.readRichMediaLinOneCustomEventRawCSVFromCloudStorage(
    				rawFileName, dirName,dataSource);
    	}else{
    		log.warning("Not a valid custom event dataSource:"+dataSource);
    	} 
    	
    	
    	if(reportList!=null && reportList.size()>0){
    		log.info("reportList:"+reportList.size());
    		StringBuffer csvDate=ReportUtil.createRichMediaCustomEventCSVReport(reportList);
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    /*
     * @author Youdhveer Panwar
     * It will load all orderIds for given dfp network code
     * @see com.lin.web.service.IReportService#getAllOrderIds(java.lang.String)
     * @param String networkCode
     * @return Long[] orderIds
     * 
     */
    public List<Long> getAllOrderIds(String networkCode){    	
    	IReportDAO reportDAO=new ReportDAO();
    	List<Long> orderIdList=MemcacheUtil.getOrderIdsFromCache(networkCode);
    	if(orderIdList==null || orderIdList.size()==0){
    		try {
    			orderIdList=new ArrayList<Long>();
    			
				List<DfpOrderIdsObj> orderList=reportDAO.loadAllOrderIds(networkCode);
				for(DfpOrderIdsObj obj:orderList){
					if(!orderIdList.contains(obj.getOrderId())){
						orderIdList.add(obj.getOrderId());
					}					
				}				
				if(orderIdList.size()>0){
					MemcacheUtil.setOrderIdsInCache(orderIdList, networkCode);
				}
				log.info("returning orderIds:"+orderIdList.size());
			} catch (DataServiceException e) {
				log.severe("DataServiceException : getAllOrderIds: :"+e.getMessage());
				
			}
    	}
    	return orderIdList;
    }
    
    /*
     * Process and upload dfp target report
     * 
     */
    public String generateDFPTargetReport(String startDate,String endDate,String rawFileName,
			String dirName,String networkCode,String publisherId,String publisherName,String bucketName){
		String reportURL=null;
		
		log.info("startDate:"+startDate+" | endDate:"+endDate+" | networkCode:"+networkCode);		
	    
		List<DFPTargetReportObj>  reportList=new ArrayList<DFPTargetReportObj>();
		
		reportList=CloudStorageUtil.readDFPTargetDFPRawCSVFromCloudStorage(rawFileName, dirName,networkCode,
				publisherId,publisherName,bucketName);
		log.info("Total objects for CSV report::"+reportList.size());
		if(reportList.size()>0){
			StringBuffer strBuffer=ReportUtil.createDFPTargetCSVReport(reportList);	
			try {			
				String procFileName=rawFileName.replace("raw", "proc");
				reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), procFileName, dirName,bucketName);			
			} catch (IOException e) {
				log.severe("uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			}		
			log.info("reportURL:"+reportURL);  
		}		
       
        return reportURL;
	}
    
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate generateDFPTargetReport report from cloud storage
     *  
     * @param String rawFileName,String proccessFileName,String dirName,String networkCode
     *        String publisherId,String publisherName,String bucketName
     * @return List<String> fileList
     */
	public List<String> generateDFPTargetReport(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String publisherId,String publisherName,String bucketName){
		List<String> fileList=new ArrayList<String>();
        String reportURL=null;		
		List<DFPTargetReportObj> reportList=new ArrayList<DFPTargetReportObj>();		
		if(networkCode !=null ){			
			reportList=CloudStorageUtil.readDFPTargetDFPRawCSVFromCloudStorage(rawFileName, dirName,
					networkCode,publisherId,publisherName,bucketName);
		}else{
			log.warning("No data available for this network code:"+networkCode);
		}
		
		int total=reportList.size();
		log.info("Total objects for CSV reports: reportList size:"+total);
		log.info("Going to create complete csv ....");
		StringBuffer strBuffer=ReportUtil.createDFPTargetCSVReport(reportList);
		try {
			if(total>0){
				reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), proccessFileName, dirName,bucketName);
				if(reportURL !=null){
					fileList.add(reportURL);
				}				
			}else{
				log.severe("Failed to processed file, check raw file. failed proccessFileName:"+proccessFileName);
			}
			
		} catch (IOException e) {
			log.severe(":uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());			
		}  
		
        return fileList;
	}
	
    public String generateNewDFPReport(String startDate,String endDate,String rawFileName,String dirName,String timestamp){
		String reportURL=null;
		
		log.info("generateDFPReport: startDate:"+startDate+" & endDate:"+endDate);		
	    
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		List<CorePerformanceReportObj>  reportList2=new ArrayList<CorePerformanceReportObj>();
		
		Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		
		allDataMap=CloudStorageUtil.readNewCorePerformanceDFPRawCSVFromCloudStorage(rawFileName, dirName);
		log.info("Total objects for CSV reports: allDataMap:"+allDataMap.size());
		reportList=allDataMap.get("CorrectData");
		reportList2=allDataMap.get("InCorrectData");
		log.info("Total objects for CSV reports: CorrectData: reportList :"+reportList.size());
		log.info("Total objects for CSV reports: InCorrectData: reportList2 :"+reportList2.size());
		//Collections.sort(reportList, RootReportComparator);
		
		StringBuffer strBuffer=ReportUtil.createCorePerformanceCSVReport(reportList);		
		//log.info("CSV Report:"+strBuffer.toString());		
		try {			
			String procFileName=rawFileName.replace("raw", "proc");
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), procFileName, dirName);
			StringBuffer strBuffer2=new StringBuffer();
			if(reportList2 !=null && reportList2.size()>0){
				strBuffer2=ReportUtil.createCorePerformanceCSVReport(reportList2);
				String rejectFileName=rawFileName.replace("raw", "reject");
				String inCorrectDataReportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer2.toString(), rejectFileName, dirName);
				log.info("inCorrectDataReportURL:"+inCorrectDataReportURL);
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, "InCorrect data found.", "Hi, Incorrect data found while processing DFP Core performance table, please see csv file at cloud storage : "+inCorrectDataReportURL);
			}
		} catch (IOException e) {
			log.severe("generateDFPReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}		
		log.info("generateDFPReport: reportURL:"+reportURL);  
       
        return reportURL;
	}
    
	public Comparator<RootReportObj> RootReportComparator = new Comparator<RootReportObj>() {
		public int compare(RootReportObj obj, RootReportObj anotherObj) {
			
			long date1 = StringUtil.getLongValue(DateUtil.getFormatedDate(obj.getDate(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
			long date2 = StringUtil.getLongValue(DateUtil.getFormatedDate(anotherObj.getDate(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
			if (date1 <= date2) {  // sort by ascending order
				return -1;
			} else {
				return 1;
			}
		}
	};

	public Comparator<CorePerformanceReportObj> CorePerformanceComparator = new Comparator<CorePerformanceReportObj>() {
		public int compare(CorePerformanceReportObj obj, CorePerformanceReportObj anotherObj) {
			
			long date1 = StringUtil.getLongValue(DateUtil.getFormatedDate(obj.getDate(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
			long date2 = StringUtil.getLongValue(DateUtil.getFormatedDate(anotherObj.getDate(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
			if (date1 <= date2) {  // sort by ascending order
				return -1;
			} else {
				return 1;
			}
		}
	};
	
	
	/*
	 * generateFinilisedDFPReport 
	 * @param String startDate,String endDate,String rawFileName,
			String dirName,String networkCode,String publisherId, String publisherName
		@return String reportURL
	 */
	public String generateCorePerformanceReport(String rawFileName,
			String dirName,String networkCode,String publisherId, String publisherName,String bucketName){
		String reportURL=null;
		
		log.info("Process raw file : rawFileName :"+rawFileName);    
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		List<CorePerformanceReportObj>  reportList2=new ArrayList<CorePerformanceReportObj>();
		
		Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		
		// Read csv and form a processed reportObject
		allDataMap=CloudStroageFileProcessUtil.readDFPRawCSVFromCloudStorage(rawFileName, dirName,networkCode,publisherId,
				publisherName,bucketName);
		
		log.info("Total objects for CSV reports: allDataMap:"+allDataMap.size());
		reportList=allDataMap.get("CorrectData");
		reportList2=allDataMap.get("InCorrectData");
		log.info("Total objects for CSV reports: CorrectData: reportList :"+reportList.size());
		log.info("Total objects for CSV reports: InCorrectData: reportList2 :"+reportList2.size());
		
		// Iterate through all correct objects in list and form csv from processed objects. For being loaded into BigQuery.
		StringBuffer strBuffer=ReportUtil.createCorePerformanceFinaliseCSVReport(reportList);		
		
		try {			
			String procFileName=rawFileName.replace("raw", "proc");
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), procFileName, dirName,bucketName);
			StringBuffer strBuffer2=new StringBuffer();
			if(reportList2 !=null && reportList2.size()>0){
				strBuffer2=ReportUtil.createCorePerformanceFinaliseCSVReport(reportList2);
				String rejectFileName=rawFileName.replace("raw", "reject");
				String inCorrectDataReportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer2.toString(), rejectFileName, 
						dirName,bucketName);
				log.info("inCorrectDataReportURL:"+inCorrectDataReportURL);
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS, "InCorrect data found.", "Hi, Incorrect data found while processing DFP Core performance table, please see csv file at cloud storage : "+inCorrectDataReportURL);
			}
		} catch (IOException e) {
			log.severe("generateDFPReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}		
		log.info("generateDFPReport: reportURL:"+reportURL);  
       
        return reportURL;
	}
	
	
	  /*
     * @author Shubham Goel
     * It will load all Account ID for given companyId
     */
	
    public List<String> getAllAccountByCompanyId(String companyId){
    	List<String> accountIdList = new ArrayList<String>();
    	List<AccountsEntity> accountsObjList =new ArrayList<AccountsEntity>(0);
    	try{
    		accountsObjList = MemcacheUtil.getAllAccountsObjList(companyId);
        	if(accountsObjList!=null && accountsObjList.size()>0){
        		for (AccountsEntity accountsObj : accountsObjList) {
					if(accountsObj!=null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountDfpId()!=null && !accountsObj.getAccountDfpId().equals("")){
						accountIdList.add(accountsObj.getAccountDfpId());
					}
				}
        	}
    	}catch(Exception e){
    		log.severe("Exception in getAllAccountId of ReportService "+e.getMessage());
    		
    	}
		return accountIdList;
    	
    }
    
    /*
     * @author Naresh Pokhriyal
     * It will load all Account ID for given companyName
     */
    @Override
    public List<String> getAllAccountIdByCompanyName(String companyName) {
    	IUserDetailsDAO userDetailsDAO = new UserDetailsDAO();
    	List<String> accountIdList = new ArrayList<String>();
    	List<AccountsEntity> accountsObjList =new ArrayList<AccountsEntity>(0);
    	try{
    		if(companyName!= null) {
    			CompanyObj companyObj = userDetailsDAO.getCompanyByName(companyName, MemcacheUtil.getAllCompanyList());
    			if(companyObj == null) {
    				log.info("Compny does not exists for name : "+companyName);
    			}
    			accountsObjList = MemcacheUtil.getAllAccountsObjList(companyObj.getId()+"");
            	if(accountsObjList!=null && accountsObjList.size()>0){
            		for (AccountsEntity accountsObj : accountsObjList) {
    					if(accountsObj!=null && accountsObj.getStatus().equals(LinMobileConstants.STATUS_ARRAY[0]) && accountsObj.getAccountDfpId()!=null && !accountsObj.getAccountDfpId().equals("")){
    						accountIdList.add(accountsObj.getAccountDfpId());
    					}
    				}
            	}
    		}
    	}catch(Exception e){
    		log.severe("Exception in getAllAccountId of ReportService "+e.getMessage());
    		
    	}
		return accountIdList;
    	
    }
    
    /*
     * This method save any report entity in datastore
     * @see com.lin.web.service.IReportService#saveReportInDatastore(java.lang.Object)
     * @author Youdhveer
     */
    public boolean saveReportInDatastore(Object reportObject){
    	IReportDAO reportDAO=new ReportDAO();
    	try {
			reportDAO.saveObject(reportObject);
			return true;
		} catch (DataServiceException e) {
			log.severe("DataServiceException:"+e.getMessage());
			
			return false;
		}
    }
    
  
    /*
     * @author Youdhveer Panwar
     * (non-Javadoc)
     * @see com.lin.web.service.IReportService#uploadProcessFileAtBQ(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public String uploadProcessFileAtBQ(String tableId,String reportsResponse, String startDate,String endDate,
		String timestamp,String processedFilePath,String dataSource,int finalizeOrNonFinalize, 
		String publisherId,String publisherName, String serviceAccountEmail,String servicePrivateKey, 
		String projectId,String nonFinaliseTableId){
    	
    	log.info("Going to save this data on bigquery: reportsResponse:"+reportsResponse);
		
		String schemaFile=LinMobileConstants.CORE_PERFORMANCE_FINILIZED_TABLE_SCHEMA;
		String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
	
		if(reportsResponse !=null){
			List<String> sourceFileList=new ArrayList<String>();
	    	sourceFileList.add(reportsResponse);
	    	
			if(finalizeOrNonFinalize==0){
				reportsResponse=BigQueryUtil.uploadDataOnBigQuery(sourceFileList,true,schemaFile, tableId,
	    				projectId,dataSetId,serviceAccountEmail,servicePrivateKey,publisherId);
			}else{
				/*boolean doesTableExist=BigQueryUtil.doesTableExist(serviceAccountEmail, servicePrivateKey, projectId,
  					   dataSetId, tableId);
				if(doesTableExist){
					
				}*/
				reportsResponse=BigQueryUtil.uploadDataOnBigQuery(sourceFileList,false,schemaFile, tableId,
	    				projectId,dataSetId,serviceAccountEmail,servicePrivateKey,publisherId);
			}
    		
    		
    		if(reportsResponse!=null && reportsResponse.contains("Success")){	
    		   String id=publisherId+":"+tableId;
    		   FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, finalizeOrNonFinalize, 
    				   startDate, endDate, timestamp,Integer.parseInt(publisherId),dataSource, 
    				   processedFilePath, projectId,dataSetId,0);
    		   
    		   boolean saveReportStatus=saveReportInDatastore(reportObject);
    		   log.info("Report looged in datastore:"+saveReportStatus);
    		   if(saveReportStatus && finalizeOrNonFinalize==1){    
    			   // After adding non-finalise table to finalise, now delete it from BigQuery 
    			   // and update in datastore with deleteStatus flag --1
    			   if(dataSource !=null && dataSource.indexOf(" ")>=0){
    				   dataSource=dataSource.replaceAll(" ", "");
    			   }
    			   String readyToMergeTableId="CorePerformance_"+dataSource+"_"+startDate.replaceAll("-", "_");
    			   if(nonFinaliseTableId !=null ){
    				   readyToMergeTableId=nonFinaliseTableId;
    			   }
    			   
    			   BigQueryUtil.deleteTable(serviceAccountEmail, servicePrivateKey, projectId,
    					   dataSetId, readyToMergeTableId);
    			   
    			   IReportDAO reportDAO=new ReportDAO();
    			   try {
					reportDAO.deleteFinaliseOrNonFinaliseTable(readyToMergeTableId,publisherId);
					
					//Update non-finalise object as merged non-finalise object 
					id=publisherId+":"+readyToMergeTableId;
					FinalisedTableDetailsObj mergeReportObject=new FinalisedTableDetailsObj(id, readyToMergeTableId, 0, 
		    				   startDate, endDate, timestamp,Integer.parseInt(publisherId),dataSource, 
		    				   processedFilePath, projectId,dataSetId,1);
					saveReportInDatastore(mergeReportObject);
					log.info("Update merge status : "+mergeReportObject.toString());
					
				  } catch (DataServiceException e) {
					log.severe("Failed to delete row from datastore:"+readyToMergeTableId+" : err msg:"+e.getMessage());					
				  }        		   
    		   }
    		}
	   }else{
			String message="Please check the log, CorePerformance report failed to generate for "+publisherName+" , finalizeOrNonFinalize:"+finalizeOrNonFinalize;
			String subject="CorePerformance report failed to generate for "+publisherName;        			
			LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, 
					LinMobileConstants.TO_EMAIL_ADDRESS, 
					LinMobileConstants.CC_EMAIL_ADDRESS,
					subject, message);
	   }
	   return reportsResponse;
    }    
    
    
    public FinalisedTableDetailsObj loadFinaliseNonFinaliseObject(String tableId,String publisherId){
    	log.info("Going to load object with tableId:"+tableId+" for publisherId:"+publisherId);
    	IReportDAO reportDAO=new ReportDAO();
    	try {
			FinalisedTableDetailsObj obj=reportDAO.loadFinaliseOrNonFinaliseTable(tableId,publisherId);
			return obj;
		} catch (DataServiceException e) {
			log.severe("Failed to load row from datastore:"+tableId+" : err msg:"+e.getMessage());	
			return null;
			
		}
    }
    
    /*
     *  This method generate processed file for nexage finalised/non-finalised table
     *  
     */
    public String generateFinalisedNexageReport(String startDate,String endDate,String dimensions,
    		String dirName,String rawFileName,String bucketName){
		String reportURL=null;
		String subject="";
		String message="";
		log.info(" startDate:"+startDate+" & endDate:"+endDate+" & dimensions:"+dimensions);
		
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
				
		String reportsResponse=NexageReportUtil.getReportsBySite(startDate,endDate);
		if(reportsResponse !=null && (!reportsResponse.contains("IOException"))){
			JSONObject nexageSiteDetails = (JSONObject) JSONSerializer.toJSON(reportsResponse);			
			dimensions="day";	
	        JSONArray jsonArray=nexageSiteDetails.getJSONArray("detail");
	        if(jsonArray.size()==0){
	        	log.info("reportsResponse :"+reportsResponse);
	        	log.warning("No sites loaded, our API failed to get data from nexage, please restart cron job.");
	        	subject="Nexage cron job failed to fecth data - no sites found";
	        	message="Hi, Please check your application log, nexage cron failed for startDate:"
					+startDate+" and endDate:"+endDate+"\n"+"No sites found...";
	        	LinMobileUtil.sendMailOnGAE(
	        			LinMobileVariables.SENDER_EMAIL_ADDRESS, 
	    				LinMobileConstants.TO_EMAIL_ADDRESS,
	    				LinMobileConstants.CC_EMAIL_ADDRESS, 
	    				subject,message);
	        }else{
	        	String procFileName=rawFileName.replace("_raw", "_proc");	        	
	        	for(int i=0;i<jsonArray.size();i++){
	    			JSONObject obj=jsonArray.getJSONObject(i);
	    			String site=obj.getString("site");
	    			String siteId=obj.getString("siteId");
	    			String reportsResponseBySiteId=NexageReportUtil.getReportsBySiteId(startDate,endDate,dimensions,siteId);
	    			if(reportsResponseBySiteId !=null){
	    				List<CorePerformanceReportObj> reportListById=
	    						NexageReportUtil.createNexageCorePerformanceReportData(reportsResponseBySiteId,siteId,site);
		    			log.info("reportListById:"+reportListById.size());
		    			reportList.addAll(reportListById);
	    			}else{
	    				log.warning("Failed to get report for siteId:"+siteId);
	    			}	    			
	    		}
	    		log.info("Nexage reportList: "+reportList.size());
	    		Collections.sort(reportList, CorePerformanceComparator);
	    		
	    		StringBuffer strBuffer=ReportUtil.createCorePerformanceFinaliseCSVReport(reportList);
	    		//log.info("Nexage CSV Report:"+strBuffer.toString());
	    		
	    		try {
	    			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), procFileName, dirName,bucketName);
	    		} catch (IOException e) {
	    			log.severe("uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
	    			
	    		}		
	    		log.info("reportURL:"+reportURL);    		
	        }		
		}else{			
        	
			if(reportsResponse !=null && (reportsResponse.contains("IOException"))){
				subject="Nexage cron job failed to fecth data - IOException";
	        	message="Hi, Please check your application log, nexage cron failed for startDate:"
					+startDate+" and endDate:"+endDate+"\n"+reportsResponse;
			}else{
				subject="Nexage cron job failed to fecth data - no sites found";
	        	message="Hi, Please check your application log, nexage cron failed for startDate:"
					+startDate+" and endDate:"+endDate+"\n"+"No sites found...";
			}
			log.warning("Cron job failed, send email alert...reportsResponse:"+reportsResponse);
			
			LinMobileUtil.sendMailOnGAE(
				LinMobileVariables.SENDER_EMAIL_ADDRESS, 
				LinMobileConstants.TO_EMAIL_ADDRESS,
				LinMobileConstants.CC_EMAIL_ADDRESS, 
				subject,message);
			
		}
		
        return reportURL;
	}
    
    public String generateFinalisedNexageReportNewAPI(String startDate,String endDate,String dimensions,
    		String dirName,String rawFileName,String bucketName){
		String reportURL=null;
		
		log.info(" startDate:"+startDate+" & endDate:"+endDate+" & dimensions:"+dimensions);
		
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		List<String> siteIdList = NexageReportUtil.getIdList(startDate, endDate, "sellerrevenue", "site");
		List<String> sourceIdList = NexageReportUtil.getIdList(startDate, endDate, "sellerrevenue", "source");
				

		for(String site : siteIdList)
		{
			String[] siteIdSiteName = site.split(":");
			String siteId = siteIdSiteName[0];
			String siteName = siteIdSiteName[1];
			
			for(String source : sourceIdList)
			{
				String[] sourceIdSourceName = source.split(":");
				String sourceId = sourceIdSourceName[0];
				String sourceName = sourceIdSourceName[1];
				
				String reportsResponse=NexageReportUtil.getNexageReportsByDimId(startDate,endDate,"sellerrevenue","site",siteId,"source",sourceId);
				
				if(reportsResponse !=null && (!reportsResponse.contains("IOException"))){
					List<CorePerformanceReportObj> reportListById=NexageReportUtil.createNexageCorePerformanceReportDataNewAPI(reportsResponse,siteId,siteName,sourceId,sourceName);
	    			log.info("reportListById:"+reportListById.size());
	    			reportList.addAll(reportListById);
			    }
				else
				{
			    	log.warning("Failed to get report for dimension :"+dimensions);
			    }
			}
		}
		
        return reportURL;
	}
    
    
   /*
    * generateFinaliseMojivaReportByDay
    * @see com.lin.web.service.IReportService#generateFinaliseMojivaReportByDay(java.lang.String, java.lang.String, java.lang.String)
    */
	public String generateFinaliseMojivaReportByDay(String date, String fileName,String dirName,String bucketName){
		
		String reportURL=null;
		String rawResponse="";
		
		MojiveReportWebServiceResponse mojivaResponseObj=MojivaReportUtil.getAllMojivaSites();
		if(mojivaResponseObj !=null){
			List<MojivaReportDTO> resultList=mojivaResponseObj.getSiteList();
			
			List<String> siteIdList=new ArrayList<String>();
			for(MojivaReportDTO obj:resultList){
				siteIdList.add(obj.getId());
			}
			resultList=new ArrayList<MojivaReportDTO>();
			log.info("Mojiva SiteIds :"+siteIdList);
			
			for(String siteId:siteIdList){				
				MojiveReportWebServiceResponse response=MojivaReportUtil.getMojivaSiteReportByDate(date, siteId);
				if(response ==null ){
					log.warning("Report failed to get response...for siteId:"+siteId);
				}else{
					rawResponse=rawResponse+"\n"+response;
					List<MojivaReportDTO> siteWiseList=response.getSiteList();						
					if(siteWiseList.size() > 0){
						log.info("Add in list:  siteId:"+siteId+" : siteWiseList :"+siteWiseList.size()+" : date:"+date);
						resultList.addAll(siteWiseList);
					}else{
						log.warning("Don't get any siteWiseList : for siteId:"+siteId+" : siteWiseList: "+siteWiseList.size()+" date:"+date);
					}
				}				
			}
			log.info("Total DTO objects for mojiva :"+resultList.size());
			List<CorePerformanceReportObj> rootDataList=MojivaReportUtil.createMojivaCorePerformanceReportData(resultList, date);
			log.info("rootDataList for process :"+rootDataList.size());
			if(rootDataList !=null && rootDataList.size()>0){
				StringBuffer strBuffer=ReportUtil.createCorePerformanceFinaliseCSVReport(rootDataList);
				//log.info("Mojiva CSV report:"+strBuffer.toString());				
						
				try {					
					reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName,bucketName);
				} catch (IOException e) {
					log.severe("generateMojivaReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
					
				}		
				log.info("generateMojivaReport: reportURL:"+reportURL);
			}else{				
				reportURL=null;
				log.severe("No process data from Mojiva : Raw response :"+rawResponse);
			}
			
		}else{
			log.severe("Failed to get data from Mojiva...");
			reportURL="Failed to get data from Mojiva";			
		}		
		
		return reportURL;
	}
	
	/*
	 * generateFinaliseMojivaReport
	 * @see com.lin.web.service.IReportService#generateFinaliseMojivaReport(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String generateFinaliseMojivaReport(String startDate,String endDate, String fileName,String dirName,
			String bucketName){
		String reportURL=null;
		String rawResponse="";
		
		MojiveReportWebServiceResponse mojivaResponseObj=MojivaReportUtil.getAllMojivaSites();
		if(mojivaResponseObj!= null){
			List<MojivaReportDTO> resultList=mojivaResponseObj.getSiteList();
			
			List<String> siteIdList=new ArrayList<String>();
			for(MojivaReportDTO obj:resultList){
				siteIdList.add(obj.getId());
			}
			resultList=new ArrayList<MojivaReportDTO>();
			log.info("Mojiva SiteIds :"+siteIdList);
			
			int days=(int) DateUtil.getDifferneceBetweenTwoDates(startDate, endDate, "yyyy-MM-dd");
			days=days+1;
			for(int i=0;i<days;i++){
				log.info("fetching data daywise: day:"+i+" and startDate:"+startDate);
				List<MojivaReportDTO> dayWiseList=new ArrayList<MojivaReportDTO>();
				for(String siteId:siteIdList){
					MojiveReportWebServiceResponse response=MojivaReportUtil.getMojivaSiteReportByDate(startDate,siteId);
					if(response ==null ){
						log.warning("Report failed to get response...for siteId:"+siteId);
					}else{
						rawResponse=rawResponse+"\n"+response;
						List<MojivaReportDTO> siteWiseList=response.getSiteList();						
						if(siteWiseList.size() > 0){
							log.info("Add in list:  siteId:"+siteId+" : siteWiseList :"+siteWiseList.size()+" : startDate:"+startDate);
							dayWiseList.addAll(siteWiseList);
						}else{
							log.warning("Don't get any siteWiseList : for siteId:"+siteId+" : siteWiseList: "+siteWiseList.size()+" startDate:"+startDate);
						}
					}
				}
				Date dt=DateUtil.getFormatedDate(startDate,"yyyy-MM-dd");
				startDate=DateUtil.getModifiedDateStringByDays(dt, 1, "yyyy-MM-dd");				
				resultList.addAll(dayWiseList);				
			}
			
			log.info("Total DTO objects for mojiva :"+resultList.size());
			List<CorePerformanceReportObj> rootDataList=MojivaReportUtil.createMojivaCorePerformanceReportData(resultList, startDate);
			log.info("rootDataList for process :"+rootDataList.size());
			
			if(rootDataList.size()>0){
				StringBuffer strBuffer=ReportUtil.createCorePerformanceFinaliseCSVReport(rootDataList);			
				
				try {
					reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName,bucketName);
				} catch (IOException e) {
					log.severe("generateMojivaReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
					
				}		
				log.info("generateMojivaReport: reportURL:"+reportURL);
			}else{
				reportURL=null;
				log.severe("No process data from Mojiva : Raw response :"+rawResponse);
			}
			
		}else{
			log.severe("Failed to get data from Mojiva...");
			reportURL="Failed to get data from Mojiva";			
		}
		
		return reportURL;
	}
	
	public String generateFinalisedAdExchangeReport(String reportsResponse,String startDate,String fileName, 
			String dirName,String bucketName){
		String reportURL=null;
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
				
		try {
			if(reportsResponse!=null && reportsResponse.trim().length()>0){	
				reportList=AdExchangeReportUtil.createAdExCorePerformanceReportData(reportsResponse);
				if(reportList !=null && reportList.size()>0){
					log.info("Total objects for CSV reports: reportList:"+reportList.size());
					StringBuffer strBuffer=ReportUtil.createCorePerformanceFinaliseCSVReport(reportList);
					//log.info("AdEx CSV Report:"+strBuffer.toString());
					reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), fileName, dirName,bucketName);						
					log.info(" reportURL:"+reportURL);  
				}else{			
					log.info("No report generated as no records found:");
				}
			}else{
				log.warning("No report generated: some exception occured..");
			}
		}catch (IOException e){
			log.severe("uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		} catch (Exception e) {
			log.severe("Exception:"+e.getMessage());
			
		}		
		return reportURL;
	}
	
	 /*
	  * @author Youdhveer Panwar
	  * (non-Javadoc)
	  * @see com.lin.web.service.IReportService#uploadProcessFileAtBQ(com.lin.server.bean.FinalisedTableDetailsObj, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	  */
    public String uploadProcessFileAtBQ(FinalisedTableDetailsObj reportObject,int finalizeOrNonFinalize, 
		String reportsResponse, QueryDTO bqDTO,String readyToMergeTableId){
    	
    	log.info("Going to save this data on bigquery: reportsResponse:"+reportsResponse);
    	
    	String publisherId=reportObject.getPublisherId()+"";
		if(reportsResponse !=null){
			List<String> sourceFileList=new ArrayList<String>();
	    	sourceFileList.add(reportsResponse);
			if(finalizeOrNonFinalize==0){
				reportsResponse=BigQueryUtil.uploadDataOnBigQuery(sourceFileList,true,
						bqDTO.getBigQuerySchemaFileName(), reportObject.getTableId(),
						bqDTO.getBigQueryProjectId(), bqDTO.getBigQueryDataSetId(),
						bqDTO.getServiceAccountEmail(),bqDTO.getServicePrivateKey(),publisherId);
			}else{
				reportsResponse=BigQueryUtil.uploadDataOnBigQuery(sourceFileList,false,
						bqDTO.getBigQuerySchemaFileName(), reportObject.getTableId(),
						bqDTO.getBigQueryProjectId(),bqDTO.getBigQueryDataSetId(),
						bqDTO.getServiceAccountEmail(),bqDTO.getServicePrivateKey(),publisherId);
			}    		
    		
    		if(reportsResponse!=null && reportsResponse.contains("Success")){	
    		   boolean saveReportStatus=saveReportInDatastore(reportObject);
    		   log.info("Report looged in datastore:"+saveReportStatus);
    		   if(saveReportStatus && finalizeOrNonFinalize==1){    
    			   // After adding non-finalise table to finalise, now delete it from BigQuery 
    			   // and update in datastore with deleteStatus flag --1
    			   String dataSource=reportObject.getDataSource();
    			   if(dataSource !=null && dataSource.indexOf(" ")>=0){
    				   dataSource=dataSource.replaceAll(" ", "");
    			   }
    			   
    			   String nonFiniliseTableId=bqDTO.getBigQuerySchemaName()+"_"+reportObject.getDataSource()+"_"+reportObject.getStartDate().replaceAll("-", "_");
    			   if(readyToMergeTableId !=null){
      				   nonFiniliseTableId=readyToMergeTableId;
      			   }
      			   
    			   try {
    				    BigQueryUtil.deleteTable(bqDTO.getServiceAccountEmail(), bqDTO.getServicePrivateKey(),
        					   bqDTO.getBigQueryProjectId(),  bqDTO.getBigQueryDataSetId(), nonFiniliseTableId);
        			   
        			    IReportDAO reportDAO=new ReportDAO();
						reportDAO.deleteFinaliseOrNonFinaliseTable(nonFiniliseTableId,publisherId);					
						//Update non-finalise object as merged non-finalise object 
						String id=publisherId+":"+nonFiniliseTableId;
						reportObject.setId(id);
						reportObject.setTableId(nonFiniliseTableId);
						reportObject.setTableType(0);
						reportObject.setMergeStatus(1);					
						saveReportInDatastore(reportObject);
					
				  } catch (DataServiceException e) {
					 log.severe("Failed to delete row from datastore:"+nonFiniliseTableId+" : err msg:"+e.getMessage());					
				  } catch(Exception e){
					 log.severe("Exception :"+e.getMessage());
				  }
    		   }
    		}
	   }else{
			String message="Please check the log, "+bqDTO.getBigQuerySchemaName()+" report failed to generate for "+publisherId+" , finalizeOrNonFinalize:"+finalizeOrNonFinalize;
			String subject=bqDTO.getBigQuerySchemaName()+" report failed to generate for "+publisherId;        			
			LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, 
					LinMobileConstants.TO_EMAIL_ADDRESS, 
					LinMobileConstants.CC_EMAIL_ADDRESS,
					subject, message);
	   }
	   return reportsResponse;
    }    
    
    
    /*
	  * @author Youdhveer Panwar
	  * (non-Javadoc)
	  * @see com.lin.web.service.IReportService#uploadProcessFileAtBQ(com.lin.server.bean.FinalisedTableDetailsObj, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	  */
   public String uploadProcessFileAtBQ(FinalisedTableDetailsObj reportObject,int finalizeOrNonFinalize, 
		List<String> reportsResponseList, QueryDTO bqDTO){
   	String reportsResponse=null;
   	log.info("Going to save this data on bigquery: reportsResponse:"+reportsResponseList);
   	String publisherId=reportObject.getPublisherId()+"";
	boolean failedReport=false;
	
   	if(reportsResponseList !=null && reportsResponseList.size()>0){
		if(finalizeOrNonFinalize==0){
			reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponseList,true,
						bqDTO.getBigQuerySchemaFileName(), reportObject.getTableId(),
						bqDTO.getBigQueryProjectId(), bqDTO.getBigQueryDataSetId(),
						bqDTO.getServiceAccountEmail(),bqDTO.getServicePrivateKey(),publisherId);
		}else{
			reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponseList,false,
						bqDTO.getBigQuerySchemaFileName(), reportObject.getTableId(),
						bqDTO.getBigQueryProjectId(),bqDTO.getBigQueryDataSetId(),
						bqDTO.getServiceAccountEmail(),bqDTO.getServicePrivateKey(),publisherId);
		}    		
   		
   		if(reportsResponse!=null && reportsResponse.contains("Success")){	
   		   if(finalizeOrNonFinalize==1){
   			   reportObject.setMergeStatus(1);
   		   }
   		   boolean saveReportStatus=saveReportInDatastore(reportObject);
   		   log.info("Report looged in datastore:"+saveReportStatus);
   		}else{
   			failedReport=true;
   		}
	 }else{
		 failedReport=true;
			
	 }
   	 if(failedReport){
   		String message="Please check the log, "+bqDTO.getBigQuerySchemaName()+" report failed to generate for "+ 
   							publisherId+" , finalizeOrNonFinalize:"+finalizeOrNonFinalize+"\n reportsResponse : "+reportsResponse;
		String subject=bqDTO.getBigQuerySchemaName()+" report failed to generate for "+publisherId;        			
		LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, 
				LinMobileConstants.TO_EMAIL_ADDRESS, 
				LinMobileConstants.CC_EMAIL_ADDRESS,
				subject, message);
   	 }
	 return reportsResponse;
   }    
   
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate finalise undertone report
     *  
     * @param String rawFileName,String procFileName,String dirName
     * @return String reportPath
     */
    public String generateFinaliseUndertoneReport(String rawFileName,String procFileName,String dirName,String bucketName){
    	String reportPath=null;
    	log.info("Going to process raw file: "+rawFileName);
    	List<CorePerformanceReportObj> reportList=CloudStorageUtil.createUndertoneCSVReportFromCloudStorage(rawFileName,
    				dirName,bucketName);
    	log.info("reportList:"+reportList.size());
    	if(reportList!=null && reportList.size()>0){
    		StringBuffer csvDate=ReportUtil.createCorePerformanceFinaliseCSVReport(reportList);
    		//log.info("csvDate:"+csvDate.toString());
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    
    /*
	 * generateClientDemoCorePerformanceReport 
	 * @param String startDate,String endDate,String rawFileName,
			String dirName,String networkCode,String publisherId, String publisherName,
			String orderName,String advertiserName,String siteName,String lineItemName
		@return String reportURL
	 */
	public String generateClientDemoCorePerformanceReport(String startDate,String endDate,String rawFileName,
			String dirName,String networkCode,String publisherId, String publisherName,
			String orderName,String advertiserName,String siteName,String lineItemName){
		String reportURL=null;
		
		log.info("startDate:"+startDate+" & endDate:"+endDate);		
	    
		List<CorePerformanceReportObj>  reportList=new ArrayList<CorePerformanceReportObj>();
		List<CorePerformanceReportObj>  reportList2=new ArrayList<CorePerformanceReportObj>();
		
		Map<String,List<CorePerformanceReportObj>> allDataMap=new HashMap <String,List<CorePerformanceReportObj>>();
		
		allDataMap=CloudStroageFileProcessUtil.readClientDemoDFPRawCSVFromCloudStorage(rawFileName, dirName, networkCode, 
				publisherId, publisherName, orderName, advertiserName, siteName, lineItemName);
		
		log.info("Total objects for CSV reports: allDataMap:"+allDataMap.size());
		reportList=allDataMap.get("CorrectData");
		reportList2=allDataMap.get("InCorrectData");
		log.info("Total objects for CSV reports: CorrectData: reportList :"+reportList.size());
		log.info("Total objects for CSV reports: InCorrectData: reportList2 :"+reportList2.size());
		
		StringBuffer strBuffer=ReportUtil.createCorePerformanceFinaliseCSVReport(reportList);		
		
		try {			
			String procFileName=rawFileName.replace("raw", "proc");
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), procFileName, dirName);
			StringBuffer strBuffer2=new StringBuffer();
			if(reportList2 !=null && reportList2.size()>0){
				strBuffer2=ReportUtil.createCorePerformanceFinaliseCSVReport(reportList2);
				String rejectFileName=rawFileName.replace("raw", "reject");
				String inCorrectDataReportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer2.toString(), rejectFileName, dirName);
				log.info("inCorrectDataReportURL:"+inCorrectDataReportURL);
				LinMobileUtil.sendMailOnGAE(LinMobileVariables.SENDER_EMAIL_ADDRESS, LinMobileConstants.TO_EMAIL_ADDRESS,
						"InCorrect data found.", "Hi, Incorrect data found while processing DFP Core performance table, "
								+ "please see csv file at cloud storage : "+inCorrectDataReportURL);
			}
		} catch (IOException e) {
			log.severe("generateDFPReport :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}		
		log.info("generateDFPReport: reportURL:"+reportURL);  
       
        return reportURL;
	}
	
	/*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * generateClientDemoDFPReportByLocationWithSplit
     * It will generate PerformanceByLocation report from cloud stoarge and split it if file is too large
     *  
     * @param String rawFileName,String proccessFileName,String dirName,String networkCode,
     *        String orderName,String advertiserName,String siteName,String lineItemName
     * @return List<String> fileList
     */
	public List<String> generateClientDemoDFPReportByLocationWithSplit(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String publisherId,String publisherName,
			String orderName,String advertiserName,String siteName,String lineItemName){
		List<String> fileList=new ArrayList<String>();
        String reportURL=null;		
		List<LocationReportObj> reportList=new ArrayList<LocationReportObj>();
		List<LocationReportObj> subReportList=new ArrayList<LocationReportObj>();
		if(networkCode !=null ){			
			reportList=CloudStroageFileProcessUtil.readClientDemoDFPReportByLocationCSVFromCloudStorage(rawFileName,
					dirName, networkCode, publisherId, publisherName, orderName, advertiserName, siteName, lineItemName);
		}else{
			log.warning("No data available for this network code:"+networkCode);
		}
		
		int total=reportList.size();
		log.info("Total objects for CSV reports: reportList size:"+total);
		int count=total/10000;
		if(total >10000){
			log.info("Going to create multiple csv ....");
			int i=0;
			while(i<=count){
				int start=i*10000;
				int end=start+10000-1;
				if(i==count){
					start=i*10000;
					end=total-1;
				}
				log.info("Create sub list:start: "+start+" and end:"+end);
				subReportList=reportList.subList(start , end);
				StringBuffer strBuffer=ReportUtil.createLocationSchemaCSVReport(subReportList);
				try {			
					String splitFileName=proccessFileName.replace("_proc", "_proc_"+i);
					reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), splitFileName, dirName);
					if(reportURL !=null){
						fileList.add(reportURL);
					}
				} catch (IOException e) {
					log.severe("generateDFPReportByLocation :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
					
				}      
				i++;
			}
		}else{
			log.info("Going to create complete csv ....");
			StringBuffer strBuffer=ReportUtil.createLocationSchemaCSVReport(reportList);
			try {
				reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), proccessFileName, dirName);
				if(reportURL !=null){
					fileList.add(reportURL);
				}
				
			} catch (IOException e) {
				log.severe("generateDFPReportByLocation :uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
				
			}      
		}
        return fileList;
	}
	
	 /*
     * Process and upload dfp target report for CLIENT DEMO
     * 
     */
    public String generateClientDemoDFPTargetReport(String startDate,String endDate,String rawFileName,
			String dirName,String networkCode,String publisherId,String publisherName,
			String orderName,String advertiserName,String siteName,String lineItemName){
		String reportURL=null;
		
		log.info("startDate:"+startDate+" | endDate:"+endDate+" | networkCode:"+networkCode);		
	    
		List<DFPTargetReportObj>  reportList=new ArrayList<DFPTargetReportObj>();
		
		reportList=CloudStroageFileProcessUtil.readClientDemoDFPTargetRawCSV(rawFileName, dirName, networkCode,
				publisherId, publisherName, orderName, advertiserName, siteName, lineItemName);
		log.info("Total objects for CSV report::"+reportList.size());
		
		StringBuffer strBuffer=ReportUtil.createDFPTargetCSVReport(reportList);	
		try {			
			String procFileName=rawFileName.replace("raw", "proc");
			reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), procFileName, dirName);			
		} catch (IOException e) {
			log.severe("uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());
			
		}		
		log.info("reportURL:"+reportURL);  
       
        return reportURL;
	}
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * It will generate CLIENT DEMO RichMedia custom event report
     *  
     * @param String rawFileName,String procFileName,
    		String dirName,String dataSource,String networkCode,String publisherId,String publisherName,
    		String orderName,String advertiserName,String siteName,String lineItemName
     * @return String reportPath
     */
    public String generateClientDemoRichMediaCustomEventReport(String rawFileName,String procFileName,
    		String dirName,String dataSource,String networkCode,String publisherId,String publisherName,
    		String orderName,String advertiserName,String siteName,String lineItemName){
    	String reportPath=null;
    	
    	log.info("Going to process raw file: "+rawFileName+" dataSource:"+dataSource); 
    	List<RichMediaCustomEventReportObj> reportList=CloudStroageFileProcessUtil.
    			processClientDemoRichMediaCustomEventRawCSVFromCloudStorage(rawFileName, dirName, dataSource, networkCode, 
    					publisherId, publisherName, orderName, advertiserName, siteName, lineItemName);
    	
    	
    	if(reportList!=null && reportList.size()>0){
    		log.info("reportList:"+reportList.size());
    		StringBuffer csvDate=ReportUtil.createRichMediaCustomEventCSVReport(reportList);
    		try {
    			reportPath=ReportUtil.uploadReportOnCloudStorage(csvDate.toString(), procFileName, dirName);
			} catch (IOException e) {
				log.severe("IOException:"+e.getMessage());
				
			}
    	}else{
    		log.info("No report found or processed :");
    	}
    	return reportPath;
    }
    
    public boolean updateAllFinalisedTable(){
    	IReportDAO reportDAO=new ReportDAO();
    	try {
			List<FinalisedTableDetailsObj> dataList=reportDAO.loadAllNonFinaliseTables();
			for(FinalisedTableDetailsObj obj:dataList){
				if(obj!=null && obj.getTableType()==1){
					obj.setBigQueryDataSet(LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID);
					reportDAO.saveObject(obj);					
				}else{
					reportDAO.deleteObject(obj);
				}
			}
			log.info("All finalised table entities have been copied successfully.");
			return true;
		} catch (DataServiceException e) {
			log.severe("failed to load all FinalisedTableDetailsObj:"+e.getMessage());
			return false;
		}
    	
    }
    
    
    /*
     * @author Youdhveer Panwar(youdhveer.panwar@mediaagility.com)
     * 
     * 
     */
	public List<String> generateDFPReportByProductPerformance(String rawFileName,String proccessFileName,
			String dirName,String networkCode,String bucketName){
		List<String> fileList=new ArrayList<String>();
        String reportURL=null;		
		List<ProductPerformanceReportObj> reportList=new ArrayList<ProductPerformanceReportObj>();
		if(networkCode !=null ){			
			reportList=CloudStroageFileProcessUtil.readProductPerformanceCSVFromCloudStorage(rawFileName, dirName,
					networkCode,bucketName);
		}else{
			log.warning("No data available for this network code:"+networkCode);
		}
		
		int total=reportList.size();
		log.info("Total objects for CSV reports: reportList size:"+total);
		log.info("Going to create complete csv ....");
		StringBuffer strBuffer=ReportUtil.createProductPerformanceSchemaCSVReport(reportList);
		try {
			if(total>0){
				reportURL=ReportUtil.uploadReportOnCloudStorage(strBuffer.toString(), proccessFileName, dirName,bucketName);
				if(reportURL !=null){
					fileList.add(reportURL);
				}				
			}else{
				log.severe("Failed to processed file, check raw file. failed proccessFileName:"+proccessFileName);
			}
			
		} catch (IOException e) {
			log.severe("uploadReportOnCloudStorage:IOExcepotion: "+e.getMessage());			
		}  
		
        return fileList;
	}
	
	  /*
		  * @author Youdhveer Panwar
		  * (non-Javadoc)
		  * @see com.lin.web.service.IReportService#uploadProcessFileAtBQ(com.lin.server.bean.FinalisedTableDetailsObj, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
		  */
	 public String uploadProcessFileAtBQ(FinalisedTableDetailsObj reportObject,
			   String publisherId,List<String> reportsResponseList, QueryDTO bqDTO){
	   	String reportsResponse=null;
	   	log.info("Going to save this data on bigquery: reportsResponse:"+reportsResponseList);
		 
	   	if(reportsResponseList !=null && reportsResponseList.size()>0){
	   		
	   		reportsResponse=BigQueryUtil.uploadDataOnBigQuery(reportsResponseList,false,
					bqDTO.getBigQuerySchemaFileName(), reportObject.getTableId(),
					bqDTO.getBigQueryProjectId(), bqDTO.getBigQueryDataSetId(),
					bqDTO.getServiceAccountEmail(),bqDTO.getServicePrivateKey(),publisherId);	
	   		
	   }else{
		    log.warning("No process file to upload.");
			
	   }
	   return reportsResponse;
	}
	 @Override
	 public DFPTaskEntity getTaskEntryByName(String taskName){
			 throw new NullPointerException("No task was found with name ["+taskName+"] ");
	 }
	 
	 @Override
	 public DFPTaskEntity saveInProgressTask(String taskName, String networkCode, String startDate, String endDate, String taskGroupKey, String loadType, String orderId){
		 DFPTaskEntity entity = new DFPTaskEntity(taskName, networkCode, startDate, endDate, taskGroupKey,  
				 	DFPTaskEntity.STATUS_IN_PROGRESS, loadType, new Date(), new Date(), orderId);
		 IReportDAO dao = new ReportDAO();
		 try{
			 dao.saveDFPTaskEntity(entity);
			 return entity;
		 }catch(Exception e){
			 throw new NullPointerException("error saving in progress task ["+entity.toString()+"] ");
			 
		 }
	 }
	 
	 @Override
	 public void saveOrUpdateTask(DFPTaskEntity entity){
 		 IReportDAO dao = new ReportDAO();
		 try{
			 entity.setLastModifiedTime(new Date());
			 dao.saveDFPTaskEntity(entity);
		 }catch(Exception e){
			 throw new NullPointerException("Error while saving/updating task ["+entity.toString()+"] ");
		 }
	 }
	 
	 @Override
	 public DFPTaskEntity  getDFPTaskEntity (String entityId){
 		 IReportDAO dao = new ReportDAO();
		 try{
			 DFPTaskEntity entity =  dao.loadDFPTaskEntity(entityId);
			 if(entity == null){
				 log.severe("No task found with ID ["+entityId+"]");
			 }
			 return entity;
		 }catch(Exception e){
			 
			 throw new NullPointerException("Exception while fetching task from datastore. No task was found with id ["+entityId+"] ");
		 }
 	 }
	 
	 @Override
	 public List<DFPTaskEntity> getDFPTaskEntityByTaskKey(String dfpTaskKey){
		 log.info("going to fetch list of dfpTask for taskKey= "+dfpTaskKey);
		 IReportDAO dao = new ReportDAO();
		 try{
			 List<DFPTaskEntity> entityList =  dao.loadDFPTaskEntityByTaskKey(dfpTaskKey);
			 return entityList;
		 }catch(Exception e){
			 throw new NullPointerException("Exception while fetching task from datastore. No task was found with dfpTaskKey ["+dfpTaskKey+"] ");
		 }
	 }
	 
		 

}

