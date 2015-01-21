package com.lin.web.util;

import java.util.Comparator;

import com.lin.server.bean.AdvertiserReportObj;



public class AdvertiserViewNonPerformerComparator implements Comparator<AdvertiserReportObj>{
	public int compare(AdvertiserReportObj reportObj, AdvertiserReportObj anotherReportObj) {
		double deliveryIndicator1 = reportObj.getDeliveryIndicator();
		double deliveryIndicator2= anotherReportObj.getDeliveryIndicator();
		
		if (deliveryIndicator1 >= deliveryIndicator2) {    //Descending order
			return -1;
		} else {
			return 1;
		}
	}
}
