package com.lin.web.util;

import java.util.Comparator;

import com.lin.server.bean.PublisherChannelObj;

public class ActualPublisherComparatorChannelName implements Comparator<PublisherChannelObj> {

public int compare(PublisherChannelObj obj, PublisherChannelObj anotherObj) {
		
		long channel1 = StringUtil.getLongValue(obj.getChannelName());
		long channel2 = StringUtil.getLongValue(anotherObj.getChannelName());
		if (channel1 <= channel2) {  // sort by ascending order
			return -1;
		} else {
			return 1;
		}
		
		
	}
}
