package com.lin.web.util;

import java.util.Comparator;

import com.lin.server.bean.CustomLineItemObj;

public class TopLoserComparator implements Comparator<CustomLineItemObj>{
	public int compare(CustomLineItemObj obj, CustomLineItemObj anotherObj) {
		long value1 = obj.getDeliveredImpressions();
		long value2= anotherObj.getDeliveredImpressions();
		
		if (value1 >= value2) {    //Descending order
			return -1;
		} else {
			return 1;
		}
	}
}

