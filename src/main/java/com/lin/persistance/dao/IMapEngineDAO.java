package com.lin.persistance.dao;

import java.util.List;

import com.lin.web.dto.MapEngineDTO;
import com.lin.web.dto.QueryDTO;

public interface IMapEngineDAO {

	List<MapEngineDTO> getProductDataGMEByStateId(QueryDTO queryDTO);

	List<MapEngineDTO> getProductDataGMEByCityId(QueryDTO queryDTO);

}
