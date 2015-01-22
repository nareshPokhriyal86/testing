package com.lin.web.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.jaxws.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.lin.dfp.api.IDFPReportService;
import com.lin.dfp.api.impl.DFPAuthenticationUtil;
import com.lin.dfp.api.impl.DFPReportService;
import com.lin.server.bean.FinalisedTableDetailsObj;
import com.lin.web.dto.AdServerCredentialsDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.dto.SessionObjectDTO;
import com.lin.web.gaebigquery.BigQueryUtil;
import com.lin.web.service.IProductService;
import com.lin.web.service.IReportService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.DateUtil;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.lin.web.util.LinMobileVariables;
import com.lin.web.util.ReportUtil;
import com.lin.web.util.TaskQueueUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
public class ProductPerformanceAction extends ActionSupport 
		implements SessionAware, ServletRequestAware {
	
	private static final Logger log = Logger.getLogger(ProductPerformanceAction.class.getName());
	
	private HttpServletRequest request;
	
	private Map session;
	private SessionObjectDTO sessionDTO;
	String schemaName=LinMobileConstants.BQ_PRODUCT_PERFORMANCE;
	private String response;
	
    public String execute(){
    	
    	String month=request.getParameter("month");
    	String today=DateUtil.getCurrentTimeStamp("dd");
    	log.info("productPerformance action starts..month: "+month+" and today day : "+today);
    	int day=Integer.parseInt(today);
    	
		boolean runTaskQueue=false;
		
    	if(month !=null){
    		boolean validMonth=DateUtil.isMonthFormatYYYYMM(month);
    		if(validMonth){
    			String [] aa=month.split("-");
    			int year=Integer.parseInt(aa[0]);
    			int monthNum=Integer.parseInt(aa[1]);
    			if(monthNum>12 || monthNum <=0){
    				validMonth=false;
    			}    			
    		}
    		if(!validMonth){
    			response="Inavlid month parameter, please provide in yyyy-MM format only.";
    			return Action.SUCCESS;
    		}else{
    			runTaskQueue=true;
    		}
    		
    	}else if(day >0 && (day == LinMobileConstants.CHANGE_WINDOW_SIZE+1)){
    		runTaskQueue=true;
    	}else{
    		log.info(" today : "+today);
    		response="This job will run on "+(LinMobileConstants.CHANGE_WINDOW_SIZE+1)+" day of month, today is :"+today;
    	}
    	
    	String projectId=LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
		String serviceAccountEmail=LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
		String servicePrivateKey=LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;		
		String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;		
		if(month==null){
			month=DateUtil.getStartDateOfMonthFromCurrent(-1, "yyyy-MM");
		}
		String tableId=schemaName+"_"+month.replaceAll("-", "_");
		
    	if(runTaskQueue){
    		log.info("fetch adUnits for task queue....");
    		IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
        	Map<String, List<String>> productAdUnitIdMap=productService.loadProductsAdUnitIdsMapByAdServerId();
        	boolean truncateTable=false;
        	if(productAdUnitIdMap !=null && productAdUnitIdMap.size()>0){
        		for(String networkCode:productAdUnitIdMap.keySet()){
        			List<String> adUnitIds=productAdUnitIdMap.get(networkCode);
        			log.info("adUnitIds : "+adUnitIds);
        			if(adUnitIds!=null && adUnitIds.size()>0){
        				if(!truncateTable){   
        					log.info("Truncate old table if exist : tableId : "+tableId);
        					BigQueryUtil.deleteTableFromBigQuery(serviceAccountEmail, servicePrivateKey, projectId,
        							dataSetId, tableId);
        					truncateTable=true;
        					/*boolean tableExist=BigQueryUtil.doesTableExist(serviceAccountEmail, servicePrivateKey, projectId, dataSetId, tableId);
        					if(tableExist){
        						truncateTable=BigQueryUtil.deleteTableFromBigQuery(serviceAccountEmail, servicePrivateKey, projectId,
            							dataSetId, tableId);
        					}    */    					
        				}
        				
        				for(String adUnitId:adUnitIds){
        					TaskQueueUtil.addProductAdUnitsPerformanceTaskInQueue("/loadProductAdUnitsPerformanceData.lin",
        							networkCode,adUnitId,month);
            				log.info("Added task in queue successfully.");
            				response="Added task in queue successfully. Total tasks : "+adUnitIds.size();
        				}
        				
        			}else{
        				log.info("No adUnis under product found for networkCode : "+networkCode);
        			}
        			
        		}
        	}else{
        		log.info("productAdUnitIdMap : null or 0");
        	}
    	}
    	
    	return Action.SUCCESS;
    }
	
    public String loadProductAdUnitsPerformanceData(){
    	String networkCode=request.getParameter("networkCode");
    	String month=request.getParameter("month");
    	String adUnitId=request.getParameter("adUnitId");
    	String startDate=null;
		String endDate=null;
		
    	if(month ==null){    		
    		month=DateUtil.getStartDateOfMonthFromCurrent(-1, "yyyy-MM");    	//Previous month	
    	}else{
    		boolean validMonth=DateUtil.isMonthFormatYYYYMM(month);
    		if(validMonth){
    			String [] aa=month.split("-");
    			int year=Integer.parseInt(aa[0]);
    			int monthNum=Integer.parseInt(aa[1]);
    			if(monthNum>12 || monthNum <=0){
    				validMonth=false;
    			}    			
    		}
    		
    		if(!validMonth){
    			response="Inavlid month parameter, please provide in yyyy-MM format only.";
    			return Action.SUCCESS;
    		}else{
    			startDate=month+"-01";
    			try {
					endDate=DateUtil.getEndDateOfMonth(startDate, "yyyy-MM-dd");
				} catch (ParseException e) {
					log.severe("Invalid endDate - "+endDate);
					response="Inavlid month parameter, please provide in yyyy-MM format only.";
	    			return Action.SUCCESS;
				}
    		}
    	}
    	
    	log.info("loadProductAdUnitsPerformanceData action starts via taskQueue : networkCode :"
    				+networkCode+", month:"+month+", adUnitId: "+adUnitId);
    	if(networkCode !=null && adUnitId !=null){
		
    		List<String> adUnitIds=new ArrayList<String>();
    		adUnitIds.add(adUnitId);
    		if(adUnitIds !=null && adUnitIds.size()>0){
    			IDFPReportService dfpReportService=new DFPReportService();
        		Map<String, AdServerCredentialsDTO> credentialsMap=DFPReportService.getNetWorkCredentials();
        		AdServerCredentialsDTO credentialDTO=credentialsMap.get(networkCode);
        		String email=credentialDTO.getAdServerUsername();
        		String password=credentialDTO.getAdServerPassword();
        		
        		
            	log.info(" now going to build dfpSession ...");
            	try {
					DfpSession dfpSession=DFPAuthenticationUtil.getDFPSession(networkCode);
					log.info(" getting DfpServices instance from properties...");
	        		DfpServices dfpServices = LinMobileProperties.getInstance().getDfpServices();    	        		
	        		
	        		String downloadReportUrl=dfpReportService.getDFPReportByAdUnitsWithCity(dfpServices, dfpSession,
	        				adUnitIds,startDate,endDate);
	        		response=processAndUploadData(downloadReportUrl, networkCode, month);
	        		
				} catch (ValidationException e) {
					log.severe(" Creating dfp session exception : "+e.getMessage());
					response=response+" : exception: "+e.getMessage();
					
				} catch (Exception e) {
					log.severe(" fetching report from dfp - exception : "+e.getMessage());
					response=response+" : exception: "+e.getMessage();
					
				}finally{
					log.info("DFP reports ends...response: "+response);
				}
    		}else{
    			log.info("No adUnits are available for this network in product entity");
    		}
        		
        	
        	
    	}else{
    		log.warning("Invalid networkCode : "+networkCode+", or adUnitId : "+adUnitId);
    	}
    	
    	log.info("Response : "+response);
    	return Action.SUCCESS;
    }
    
	private String processAndUploadData(String downloadUrl,String networkCode,String month) throws IOException{
		String response="";
		IDFPReportService dfpReportService=new DFPReportService();
		IReportService service = (IReportService) BusinessServiceLocator.locate(IReportService.class);
		
		List<String> csvDataList=dfpReportService.readCSVGZFileAndSplit(downloadUrl);		       
        
    	if(csvDataList !=null && csvDataList.size()>0){
		    
    		String projectId=LinMobileConstants.LIN_MOBILE_GOOGLE_API_PROJECT_ID;
			String serviceAccountEmail=LinMobileConstants.LIN_MOBILE_GOOGLE_API_SERVICE_ACCOUNT_EMAIL_ADDRESS;
			String servicePrivateKey=LinMobileConstants.LIN_MOBILE_GOOGLE_BQ_SERVICE_ACCOUNT_PRIVATE_KEY;
			String bucketName=LinMobileVariables.LIN_MOBILE_CLOUD_STORAGE_BUCKET;
			String publisherId=LinMobileConstants.LIN_MOBILE_PUBLISHER_ID;
			String dataSetId=LinMobileVariables.GOOGLE_BIGQUERY_DATASET_ID;
			
			String tableId=schemaName+"_"+month.replaceAll("-", "_");
			String schemaFile=LinMobileConstants.PRODUCT_PERFORMANCE_TABLE_SCHEMA;
			
			QueryDTO bigQueryDTO=new QueryDTO(serviceAccountEmail, servicePrivateKey, 
					projectId, schemaFile, tableId, dataSetId, schemaName);
			
			String timestamp=DateUtil.getCurrentTimeStamp("yyyyMMddHHmmss");
	    	//String month=DateUtil.getFormatedDate(startDate, "yyyy-MM-dd", "yyyy_MM");
    		String rawFileName=timestamp+"_"+schemaName+"_"+month+"_raw"+".csv";    		
    		String procFileName=rawFileName.replace("_raw", "_proc");
    	    String dirName=LinMobileConstants.REPORT_FOLDER+"/"+schemaName+"/dfp_"+networkCode+"/"+month;		    	    
    	    String dataSource=LinMobileConstants.DFP_DATA_SOURCE;
    	    String reportPathOnStorage="";
    	    
    	    String splitRawFileName="";
    	    List<String> processedFilePathList=new ArrayList<>();
    	    for(int i=0;i<csvDataList.size();i++){
    	    	String csvData=csvDataList.get(i);
    	    	if(csvDataList.size()> 1){
    	    		splitRawFileName=rawFileName.replace("_raw", "_raw_"+i);		    	    		
    	    	}else{
    	    		splitRawFileName=rawFileName;
    	    	}
    	    	procFileName=splitRawFileName.replace("_raw", "_proc");
    	    	
    	    	reportPathOnStorage=ReportUtil.uploadReportOnCloudStorage(csvData, splitRawFileName,dirName,bucketName);
	    		log.info(" raw report saved on cloud storage :"+reportPathOnStorage);	  
	    		List <String> tempProcessedFilePathList=service.generateDFPReportByProductPerformance(splitRawFileName,procFileName, 
	    				dirName,networkCode,bucketName);
	    		processedFilePathList.addAll(tempProcessedFilePathList);
    	    }
    		
    	    log.info("processedFilePathList:"+processedFilePathList.size());
    		
    		String processedFilePath="";
    		String bqResponse="";
    		String id=publisherId+":"+tableId;
    		for(int i=0;i<processedFilePathList.size();i++){		    				
    			response=processedFilePathList.get(i);
    			if(i==0){
    				processedFilePath=response;
    			}else{
    				processedFilePath=processedFilePath+","+response;
    			}			    			
    		}  
    		FinalisedTableDetailsObj reportObject=new FinalisedTableDetailsObj(id, tableId, 1, month, month, 
    				timestamp, Integer.parseInt(publisherId), dataSource, processedFilePath, 
    				projectId, dataSetId, 0);			    		
    		
    		bqResponse= service.uploadProcessFileAtBQ(reportObject, publisherId+"", processedFilePathList, bigQueryDTO);
    		
    		response=processedFilePathList+" and bigQuery response:: "+bqResponse;	
    	} 
    	return response;
	}
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.setRequest(request);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public Map<String, Object> getSession() {
		return session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	
	public SessionObjectDTO getSessionDTO() {
		return sessionDTO;
	}

	public void setSessionDTO(SessionObjectDTO sessionDTO) {
		this.sessionDTO = sessionDTO;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
		
}