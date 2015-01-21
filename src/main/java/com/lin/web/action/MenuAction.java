package com.lin.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.lin.web.dto.LeftMenuDTO;
import com.lin.web.service.ILinMobileBusinessService;
import com.lin.web.service.impl.BusinessServiceLocator;
import com.opensymphony.xwork2.Action;

public class MenuAction implements ServletRequestAware{
	
	private static final Logger log = Logger.getLogger(MenuAction.class.getName());
	private HttpServletRequest request;
	private List<LeftMenuDTO> leftMenuItemList ;

	
	public String execute(){	
		log.info("MenuAction action executes..");
		//ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		return Action.SUCCESS;
	}	
	
	
	public String getLeftMenuList(){
		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		leftMenuItemList = service.getLeftMenuList();

		return Action.SUCCESS;
	}
	
	
	
	
	public void insertMenuItems(){
		
		List<LeftMenuDTO> leftMenuItem = new ArrayList<LeftMenuDTO>();
		leftMenuItem.add(new LeftMenuDTO(1,"Publisher View",0,20,"publisher.lin",1,"icon-book"));
		leftMenuItem.add(new LeftMenuDTO(2,"Advertiser View",0,40,"advertiser.lin",1,"icon-tag"));
		
		leftMenuItem.add(new LeftMenuDTO(3,"Operation View",0,60,null,1,"icon-user"));
		leftMenuItem.add(new LeftMenuDTO(4,"Media Planning",3,65,"mediaplan.lin",2,"icon-calendar"));
		leftMenuItem.add(new LeftMenuDTO(5,"Trafficking",3,70,"trafficking.lin",2,"icon-tasks"));
		leftMenuItem.add(new LeftMenuDTO(6,"Diagnostic Tools",3,75,"diagnosticTool.lin",2,"icon-wrench"));
		
		leftMenuItem.add(new LeftMenuDTO(7,"Admin",0,80,null,1,"icon-user"));
		leftMenuItem.add(new LeftMenuDTO(8,"User",7,85,"mediaplan.lin",2,"icon-calendar"));
		leftMenuItem.add(new LeftMenuDTO(9,"Role",7,90,"trafficking.lin",2,"icon-tasks"));
		leftMenuItem.add(new LeftMenuDTO(10,"Team",7,95,"diagnosticTool.lin",2,"icon-wrench"));
		
		leftMenuItem.add(new LeftMenuDTO(11,"Profile",0,100,"profile.lin",1,"icon-user"));
		leftMenuItem.add(new LeftMenuDTO(12,"Settings",0,120,"settings.lin",1,"icon-refresh"));
		leftMenuItem.add(new LeftMenuDTO(13,"Help",0,140,"help.lin",1,"icon-plus"));

		ILinMobileBusinessService service = (ILinMobileBusinessService) BusinessServiceLocator.locate(ILinMobileBusinessService.class);
		service.insertLeftMenuDTO(leftMenuItem);
		
		System.out.println("HELLO INDIA 1");
	}
	
	public List<LeftMenuDTO> getLeftMenuItemList() {
		return leftMenuItemList;
	}

	public void setLeftMenuItemList(List<LeftMenuDTO> leftMenuItemList) {
		this.leftMenuItemList = leftMenuItemList;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}

}

