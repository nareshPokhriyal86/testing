package com.lin.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;







import com.opensymphony.xwork2.Action;

public class GraphAction {
	private Map map = new HashMap();
	
	
	 public String barGraphData() { 
		  
		    String title = "title";
	    	System.out.println("JSON example demo...");
	    	StringBuilder sbStr = new StringBuilder();
			List<Temp> list = new ArrayList<Temp>();
			list.add(new Temp("2004","1000","400"));
			list.add(new Temp("2005","1170","460"));
			list.add(new Temp("2006","660","1120"));
			list.add(new Temp("2007","1030","540"));

			sbStr.append("[['Year',   'Sales', 'Expenses']");
			for (Temp object : list) {
				sbStr.append(",[");
				sbStr.append("'"+object.getCity()+"',"+object.getPopulation()+","+object.getArea());
				sbStr.append("]");
			}
			sbStr.append("]");
			
			map.put("title", title);
			map.put("datastr", sbStr.toString());
	        return Action.SUCCESS;
	    }
	 
	 public String lineGraphData() { 
		 String title = "title";
	    	System.out.println("JSON example demo...");
	    	StringBuilder sbStr = new StringBuilder();
			List<Temp> list = new ArrayList<Temp>();
			list.add(new Temp("2004","1000","400"));
			list.add(new Temp("2005","1170","460"));
			list.add(new Temp("2006","660","1120"));
			list.add(new Temp("2007","1030","540"));

			sbStr.append("[['Year',   'Sales', 'Expenses']");
			for (Temp object : list) {
				sbStr.append(",[");
				sbStr.append("'"+object.getCity()+"',"+object.getPopulation()+","+object.getArea());
				sbStr.append("]");
			}
			sbStr.append("]");
			
			map.put("title", title);
			map.put("datastr", sbStr.toString());
		 return Action.SUCCESS;
	 }
	 public String pieGraphData() {
		    String title ="Pie Graph Title";
		    StringBuilder sbStr = new StringBuilder();
			
			
			List<Temp> list = new ArrayList<Temp>();
			list.add(new Temp("Work","11"));
			list.add(new Temp("Eat","2"));
			list.add(new Temp("Commute","2"));
			list.add(new Temp("Watch TV","7"));
			list.add(new Temp("Sleep","5"));

			sbStr.append("[['Task',   'Hours per Day']");
			for (Temp object : list) {
				sbStr.append(",[");
				sbStr.append("'"+object.getCity()+"',"+object.getPopulation());
				sbStr.append("]");
			}
			sbStr.append("]");
			
			map.put("title", title);
			map.put("datastr", sbStr.toString());
		 return Action.SUCCESS;
	 }
	 
	 public String channelPerformancePieGraphData() {
		    //String title ="Pie Graph Title";
		    StringBuilder sbStr = new StringBuilder();
			
			List<Temp> list = new ArrayList<Temp>();
			
			
			list.add(new Temp("Work","11"));
			list.add(new Temp("Eat","2"));
			list.add(new Temp("Commute","2"));
			list.add(new Temp("Watch TV","7"));
			list.add(new Temp("Sleep","5"));

			sbStr.append("[['Task',   'Hours per Day']");
			for (Temp object : list) {
				sbStr.append(",[");
				sbStr.append("'"+object.getCity()+"',"+object.getPopulation());
				sbStr.append("]");
			}
			sbStr.append("]");
			
			//map.put("title", title);
			map.put("datastr", sbStr.toString());
		 return Action.SUCCESS;
	 }
	 
	 public String geoGraphData() {  
		 StringBuilder sbStr = new StringBuilder();
		/* map.put("displayMode",displayMode);
		 map.put("region",region);
		 map.put("colorAxisStr",colorAxisStr);*/
			List<Temp> list = new ArrayList<Temp>();
			list.add(new Temp("Rome","2761477","1285.31"));
			list.add(new Temp("Milan","1324110","181.76"));
			list.add(new Temp("Naples","959574","117.27"));
			list.add(new Temp("Turin","907563","130.17"));
			list.add(new Temp("Palermo","655875","158.9"));
			list.add(new Temp("Genoa","607906","243.60"));
			sbStr.append("[['City',   'Population', 'Area']");
			for (Temp object : list) {
				sbStr.append(",[");
				sbStr.append("'"+object.getCity()+"',"+object.getPopulation()+","+object.getArea());
				sbStr.append("]");
			}
			sbStr.append("]");
			
			map.put("datastr", sbStr.toString());
		 return Action.SUCCESS;
	 }
	 
	 
	 public String areaGraphData() {  
		 StringBuilder sbStr = new StringBuilder();
		/* map.put("displayMode",displayMode);
		 map.put("region",region);
		 map.put("colorAxisStr",colorAxisStr);*/
			
			sbStr.append("[['Days', 'Mojiva' ,'Nexage','Google Ad Exchange'],['21',0.25,0.23,0.21],['22',0.24,0.22,0.19],['23',0.29,0.24,0.23],['24',0.28,0.26,0.27],['25',0.29,0.21,0.24],['26',0.28,0.26,0.21],['27',0.31,0.28,0.26]]");
			
			map.put("datastr", sbStr.toString());
			map.put("title", "eCPM ($)");
			map.put("hAxisTitle", "Date");
			
			
		 return Action.SUCCESS;
	 }
	 
	 
	 public Map getMap() {
	        return map;
	    }

	    public void setMap(Map map) {
	        this.map = map;
	    }
	 
}



class Temp{
	private String city;
	private String population;
	private String area;
	
	
	Temp(String city,String population,String area){
		this.city = city;
		this.population = population;
		this.area = area;
	}
	
	Temp(String city,String population){
		this.city = city;
		this.population = population;
		
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPopulation() {
		return population;
	}
	public void setPopulation(String population) {
		this.population = population;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
}
