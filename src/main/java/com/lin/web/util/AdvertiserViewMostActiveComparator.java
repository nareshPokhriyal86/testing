package com.lin.web.util;

import java.util.Comparator;

import com.lin.server.bean.CustomLineItemObj;

public class AdvertiserViewMostActiveComparator implements Comparator<CustomLineItemObj>{
	public int compare(CustomLineItemObj obj, CustomLineItemObj anotherObj) {
		double deliveryIndicator1 = obj.getDeliveryIndicator();
		double deliveryIndicator2= anotherObj.getDeliveryIndicator();
		
		if (deliveryIndicator1 >= deliveryIndicator2) {    //Descending order
			return -1;
		} else {
			return 1;
		}
	}
}
