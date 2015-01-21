package com.lin.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.lin.web.dto.PublisherViewDTO;
import com.lin.web.service.ILinMobileBusinessService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.opensymphony.xwork2.Action;

public class PaginationDataAction implements ServletRequestAware{
	
	private static final Logger log = Logger.getLogger(PaginationDataAction.class.getName());
	private HttpServletRequest request;
	private List<PublisherViewDTO> publisherDTOList ;
	private int total;

	
	public String execute(){		
		log.info("PaginationDataAction action executes..");
		//ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		
		
		return Action.SUCCESS;
	}	
	
	public String publisherRelocationData(){		
		
		publisherDTOList = new ArrayList<PublisherViewDTO>();
		publisherDTOList.add(new PublisherViewDTO("1","Nexage","0.25","92.85","1,456,789","1,024", "363.36", "1.00","1.50"));
		publisherDTOList.add(new PublisherViewDTO("2","Mojiva","0.22","92.21","1,256,655","9,021", "323.32", "1.01","1.49"));
		publisherDTOList.add(new PublisherViewDTO("3","Google Ad Exchange","0.21","92.35","1,156,121","8,023 ", "313.35", "1.05","1.45"));
		
		return Action.SUCCESS;
	}	

	public String loadPublisherSellThroughData(){		
		log.info("..getPublisherSellThroughData.............");
		String pageNo=request.getParameter("pageNo");
		log.info("page No = "+request.getParameter("pageNo"));
		publisherDTOList = new ArrayList<PublisherViewDTO>();
		
		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		if(pageNo !=null){
			publisherDTOList = service.getPublisherViewDTO(Integer.parseInt(pageNo));
		}
		
		return Action.SUCCESS;
	}	
	
	public void insertPublisherView(){
		
		List<PublisherViewDTO> publisherList = new ArrayList<PublisherViewDTO>();
		publisherList.add(new PublisherViewDTO("1","Nexage","0.25","92.85","1,456,789","1,024", "363.36", "1.00","1.50"));
		publisherList.add(new PublisherViewDTO("2","Mojiva","0.22","92.21","1,256,655","9,021", "323.32", "1.01","1.49"));
		publisherList.add(new PublisherViewDTO("3","Google Ad Exchange","0.21","92.35","1,156,121","8,023 ", "313.35", "1.05","1.45"));
		publisherList.add(new PublisherViewDTO("1","Nexage","0.25","92.85","1,456,789","1,024", "363.36", "1.00","1.50"));
		publisherList.add(new PublisherViewDTO("2","Mojiva","0.22","92.21","1,256,655","9,021", "323.32", "1.01","1.49"));
		publisherList.add(new PublisherViewDTO("3","Google Ad Exchange","0.21","92.35","1,156,121","8,023 ", "313.35", "1.05","1.45"));
		publisherList.add(new PublisherViewDTO("1","Nexage","0.25","92.85","1,456,789","1,024", "363.36", "1.00","1.50"));
		publisherList.add(new PublisherViewDTO("2","Mojiva","0.22","92.21","1,256,655","9,021", "323.32", "1.01","1.49"));
		publisherList.add(new PublisherViewDTO("3","Google Ad Exchange","0.21","92.35","1,156,121","8,023 ", "313.35", "1.05","1.45"));
		publisherList.add(new PublisherViewDTO("1","Nexage","0.25","92.85","1,456,789","1,024", "363.36", "1.00","1.50"));
		publisherList.add(new PublisherViewDTO("2","Mojiva","0.22","92.21","1,256,655","9,021", "323.32", "1.01","1.49"));
		publisherList.add(new PublisherViewDTO("3","Google Ad Exchange","0.21","92.35","1,156,121","8,023 ", "313.35", "1.05","1.45"));
		publisherList.add(new PublisherViewDTO("1","Nexage","0.25","92.85","1,456,789","1,024", "363.36", "1.00","1.50"));
		publisherList.add(new PublisherViewDTO("2","Mojiva","0.22","92.21","1,256,655","9,021", "323.32", "1.01","1.49"));
		publisherList.add(new PublisherViewDTO("3","Google Ad Exchange","0.21","92.35","1,156,121","8,023 ", "313.35", "1.05","1.45"));
		publisherList.add(new PublisherViewDTO("1","Nexage","0.25","92.85","1,456,789","1,024", "363.36", "1.00","1.50"));
		publisherList.add(new PublisherViewDTO("2","Mojiva","0.22","92.21","1,256,655","9,021", "323.32", "1.01","1.49"));
		publisherList.add(new PublisherViewDTO("3","Google Ad Exchange","0.21","92.35","1,156,121","8,023 ", "313.35", "1.05","1.45"));
		publisherList.add(new PublisherViewDTO("1","Nexage","0.25","92.85","1,456,789","1,024", "363.36", "1.00","1.50"));
		publisherList.add(new PublisherViewDTO("2","Mojiva","0.22","92.21","1,256,655","9,021", "323.32", "1.01","1.49"));
		publisherList.add(new PublisherViewDTO("3","Google Ad Exchange","0.21","92.35","1,156,121","8,023 ", "313.35", "1.05","1.45"));
		publisherList.add(new PublisherViewDTO("1","Nexage","0.25","92.85","1,456,789","1,024", "363.36", "1.00","1.50"));
		publisherList.add(new PublisherViewDTO("2","Mojiva","0.22","92.21","1,256,655","9,021", "323.32", "1.01","1.49"));
		publisherList.add(new PublisherViewDTO("3","Google Ad Exchange","0.21","92.35","1,156,121","8,023 ", "313.35", "1.05","1.45"));

		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		service.insertPublisherViewDTO(publisherList);
		System.out.println("HELLO INDIA 1");
	
}



public String loadPublisherTotal() {
	publisherDTOList = new ArrayList<PublisherViewDTO>();
	log.info("publisherTotal.........................................");
	ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
	publisherDTOList = service.getPublisherViewDTO();
	total = publisherDTOList.size();
	log.info("got size......"+total);
	return Action.SUCCESS;
}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}

	public List<PublisherViewDTO> getPublisherDTOList() {
		return publisherDTOList;
	}

	public void setPublisherDTOList(List<PublisherViewDTO> publisherDTOList) {
		this.publisherDTOList = publisherDTOList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	
	

}

