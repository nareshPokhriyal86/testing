package com.lin.web.util;

import java.util.Comparator;

import com.lin.server.bean.PublisherChannelObj;;

public class ActualPublisherComparatorDate implements Comparator<PublisherChannelObj> {
	
	public int compare(PublisherChannelObj obj, PublisherChannelObj anotherObj) {
		
		long date1 = StringUtil.getLongValue(DateUtil.getFormatedDate(obj.getDate(), "yyyy-MM-dd", "yyyyMMdd"));
		long date2 = StringUtil.getLongValue(DateUtil.getFormatedDate(anotherObj.getDate(), "yyyy-MM-dd", "yyyyMMdd"));
		if (date1 <= date2) {  // sort by ascending order
			return -1;
		} else {
			return 1;
		}
		
		
	}

}
