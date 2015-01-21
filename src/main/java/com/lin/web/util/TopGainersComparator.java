package com.lin.web.util;

import java.util.Comparator;

import com.lin.server.bean.CustomLineItemObj;

public class TopGainersComparator implements Comparator<CustomLineItemObj>{
	public int compare(CustomLineItemObj obj, CustomLineItemObj anotherObj) {
		double ctrChange1 = obj.getChangeLifeTime();
		double ctrChange2= anotherObj.getChangeLifeTime();
		
		if (ctrChange1 >= ctrChange2) {    //Descending order
			return -1;
		} else {
			return 1;
		}
	}
}

