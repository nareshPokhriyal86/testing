package com.lin.web.action;

import java.io.IOException;
import java.security.GeneralSecurityException;
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
import com.lin.server.bean.AdUnitHierarchy;
import com.lin.web.service.IProductService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.lin.web.util.LinMobileConstants;
import com.lin.web.util.LinMobileProperties;
import com.opensymphony.xwork2.Action;

public class ExaminerDFPAction implements ServletRequestAware,SessionAware{

	
	private static final Logger log = Logger.getLogger(ExaminerDFPAction.class.getName());
	
	private String reportsResponse;	
	private HttpServletRequest request;
	private Map session;
	
	private DfpSession dfpSession;
    private DfpServices dfpServices;
	
	public String execute(){		
		log.info("ExaminerDFP action executes..");			
	    return Action.SUCCESS;
	}	
	
	public String dailyUpdateAdUnits(){
		log.info("dailyUpdateAdUnits......");
		
		try {				
			 // Use this code only after adding service account at DFP  
			 dfpSession=DFPAuthenticationUtil.getDFPSession(LinMobileConstants.EXAMINER_DFP_NETWORK_CODE,
						LinMobileConstants.EXAMINER_DFP_APPLICATION_NAME);
			
			
			dfpServices = LinMobileProperties.getInstance().getDfpServices();
			
			IDFPReportService dfpReport=new DFPReportService();
			List<AdUnitHierarchy> adUnitHierarchyList= dfpReport.getRecentlyUpdatedAdUnitsHierarchy(dfpServices, dfpSession);
			if(adUnitHierarchyList !=null){
				IProductService productService = (IProductService) BusinessServiceLocator.locate(IProductService.class);
				productService.saveAdUnitHierarchy(adUnitHierarchyList);
			}			
			
		}  catch (ValidationException e) {
			log.severe("ValidationException :"+e.getMessage());
		} catch (GeneralSecurityException e) {
			log.severe("GeneralSecurityException :"+e.getMessage());
		} catch (IOException e) {
			log.severe("IOException :"+e.getMessage());
		} catch (Exception e) {
		   log.severe("ApiException_Exception :"+e.getMessage());			
		}
		
		
		return Action.SUCCESS;
	}
	
	@Override
	public void setSession(Map session) {
		this.session=session;
		
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	public void setReportsResponse(String reportsResponse) {
		this.reportsResponse = reportsResponse;
	}


	public String getReportsResponse() {
		return reportsResponse;
	}



}


