package com.lin.web.util;

import java.util.Comparator;

import com.lin.server.bean.AdvertiserReportObj;



public class AdvertiserViewPerformerComparator implements Comparator<AdvertiserReportObj>{
	public int compare(AdvertiserReportObj reportObj, AdvertiserReportObj anotherReportObj) {
		double ctr1 = reportObj.getAdServerCTR();
		double ctr2= anotherReportObj.getAdServerCTR();
		
		if (ctr1 >= ctr2) {    //Descending order
			return -1;
		} else {
			return 1;
		}
	}
}
