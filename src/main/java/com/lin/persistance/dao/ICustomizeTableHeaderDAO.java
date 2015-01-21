package com.lin.persistance.dao;

import java.util.List;

import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdvertiserReportObj;


public interface ICustomizeTableHeaderDAO extends  IBaseDao{
	public List<AdvertiserReportObj> loadPerformingLineItemsForAdvertiser(int size,String lowerDate,String upperDate) throws DataServiceException;
}
