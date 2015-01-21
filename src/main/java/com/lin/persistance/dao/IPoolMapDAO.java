package com.lin.persistance.dao;

import java.util.List;

import com.lin.server.Exception.DataServiceException;
import com.lin.server.bean.AdUnitDataObj;
import com.lin.server.bean.ForcastInventoryObj;


public interface IPoolMapDAO extends IBaseDao{

	public void saveObject(Object obj) throws DataServiceException;
	public List<AdUnitDataObj> loadAllAdUnitIds(String publisherId) throws DataServiceException;
	public List<ForcastInventoryObj> loadAllocateInventry(String startDate, String endDate, String codeListstr, List<String> codeList);
}
