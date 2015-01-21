package com.lin.web.service;

import java.util.List;

import com.lin.server.bean.CustomizeTableHeaderObj;
import com.lin.web.dto.AdvertiserPerformerDTO;

public interface ICustomizeTableHeaderService extends IBusinessService  {

	public List<CustomizeTableHeaderObj> advertiserPerformer(List<CustomizeTableHeaderObj> headerList, AdvertiserPerformerDTO perfoemerDTO);
}
