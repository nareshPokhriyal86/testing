package com.lin.persistance.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableCell;
import com.google.api.services.bigquery.model.TableRow;
import com.lin.persistance.dao.IMapEngineDAO;
import com.lin.web.dto.MapEngineDTO;
import com.lin.web.dto.QueryDTO;
import com.lin.web.gaebigquery.BigQueryUtil;

public class MapEngineDAO implements IMapEngineDAO{
	
	private static final Logger log = Logger.getLogger(MapEngineDAO.class.getName());
	
	/*
	 * @author Shubham Goel 
	 * Featch product data for all the products from BigQuery group by stateId.
	 */
	 @Override
	 public List<MapEngineDTO> getProductDataGMEByStateId(QueryDTO queryDTO){
		 log.info(" Inside getProductDataGMEByStateId of MapEngineDAO....");
		 QueryResponse queryResponse = null;
		 MapEngineDTO dtoObj = new MapEngineDTO();
		 List<MapEngineDTO> MapEngineDTOList = new ArrayList<MapEngineDTO>();
		 StringBuilder query = new StringBuilder();
		 try{
			 query.append(" SELECT RegionId, Region, sum( Impressions) as Impresssions , sum( Clicks) as Clicks, ")
				.append(" from "+queryDTO.getQueryData())
				.append(" group each by RegionId,Region ");
			 
			 log.info("getProductDataGMEByStateId MapEngineDAO :: Query  :"+query);
				
				queryDTO.setQueryData(query.toString());
				
				int j=0;
				do{
		             try {
						queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
					} catch (Exception e) {
						log.severe("Query Exception = " + e.getMessage());
						
					}
					j++;
				}while(!queryResponse.getJobComplete() && j<=3);
				
				 if (queryResponse!=null && queryResponse.getRows() != null) {
					 List<TableRow> rowList = queryResponse.getRows();
				     
					 for (TableRow row : rowList){
				    	  List<TableCell> cellList = row.getF();
				    	  
				    	  dtoObj= new MapEngineDTO(
				    				 new Long(cellList.get(0).getV().toString()),
				    			     (cellList.get(1).getV().toString()),
				    				 new Long(cellList.get(2).getV().toString()),
				    				 new Long (cellList.get(3).getV().toString())
				    				);
				    	  
				    	  MapEngineDTOList.add(dtoObj);
					 }
		 }
		 }catch(Exception e){
			 log.info("Exception in getProductDataGMEByStateId of MapEngineDAO : "+e.getMessage());
				
			}
		return MapEngineDTOList;
		
	}
	 
	 /*
		 * @author Shubham Goel 
		 * Featch product data for all the products from BigQuery group by stateId.
		 */
		 @Override
		 public List<MapEngineDTO> getProductDataGMEByCityId(QueryDTO queryDTO){
			 log.info(" Inside getProductDataGMEByCityId of MapEngineDAO....");
			 QueryResponse queryResponse = null;
			 MapEngineDTO dtoObj = new MapEngineDTO();
			 List<MapEngineDTO> MapEngineDTOList = new ArrayList<MapEngineDTO>();
			 StringBuilder query = new StringBuilder();
			 try{
				 query.append(" SELECT  sum( Impressions) as Impresssions , sum( Clicks) as Clicks, CityId, City ")
					.append(" from "+queryDTO.getQueryData())
					.append(" group each by RegionId,Region ");
				 
				 log.info("getProductDataGMEByCityId MapEngineDAO :: Query  :"+query);
					
					queryDTO.setQueryData(query.toString());
					
					int j=0;
					do{
			             try {
							queryResponse = BigQueryUtil.getBigQueryData(queryDTO);
						} catch (Exception e) {
							log.severe("Query Exception = " + e.getMessage());
							
						}
						j++;
					}while(!queryResponse.getJobComplete() && j<=3);
					
					 if (queryResponse!=null && queryResponse.getRows() != null) {
						 List<TableRow> rowList = queryResponse.getRows();
					     
						 for (TableRow row : rowList){
					    	  List<TableCell> cellList = row.getF();
					    	  
					    	  dtoObj= new MapEngineDTO(
					    				 new Long(cellList.get(0).getV().toString()),
					    				 new Long (cellList.get(1).getV().toString()),
					    				 new Long(cellList.get(2).getV().toString()),
					    				 	     (cellList.get(3).getV().toString())
					    				);
					    	  
					    	  MapEngineDTOList.add(dtoObj);
						 }
			 }
			 }catch(Exception e){
				 log.info("Exception in getProductDataGMEByCityId of MapEngineDAO : "+e.getMessage());
					
				}
			return MapEngineDTOList;
			
		}

}
