package com.lin.web.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gdata.data.codesearch.File;
import com.lin.server.bean.AdvertiserReportObj;
import com.lin.server.bean.PublisherPropertiesObj;
import com.lin.web.action.MediaPlanAction;
import com.lin.web.dto.NewAdvertiserViewDTO;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;




public class ExcelReportGenerator {

	static final Logger log = Logger.getLogger(ExcelReportGenerator.class.getName());

	public byte[] generateReport(String templateName, String beanName , Object dataBean) throws Exception
	{

		//lookup the templates folder for the template
		String  path = "/com/lin/reporttemplates/" + templateName;

		InputStream is = null;
		try
		{
			is = 	getClass().getResourceAsStream(path);

			if (is == null) 
			{
				throw new Exception("Cannot find template");
			}

		}
		catch (Exception e)
		{
			System.out.println("Exception in loading template " + e);
			throw e;
		}
		XLSTransformer transformer = new XLSTransformer();
		Map excelBeans = new HashMap();
		excelBeans.put(beanName, dataBean);
		HSSFWorkbook output = null;;

		try {
			output = (HSSFWorkbook) transformer.transformXLS(is, excelBeans);
			//output = (HSSFWorkbook) transformer.transformMultipleSheetsList(is, arg1, arg2, arg3, arg4, arg5);

		} catch (ParsePropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}

		//output.

		byte[] xls = null ;

		try 
		{	
			//File outFile = new File("C:\temp\output.xls");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			output.write(baos);
			xls = baos.toByteArray();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xls;
	}


	public byte[] generateReport(String templateName, String beanName , Object dataBean, String sheetNameToReplace, String replacementName) throws Exception {
		//lookup the templates folder for the template
		String  path = "/com/lin/reporttemplates/" + templateName;
		InputStream is = null;
		try {
			is = 	getClass().getResourceAsStream(path);
			if (is == null) {
				throw new Exception("Cannot find template");
			}
		} catch (Exception e) {
			System.out.println("Exception in loading template " + e);
			throw e;
		}
		XLSTransformer transformer = new XLSTransformer();
		Map excelBeans = new HashMap();
		excelBeans.put(beanName, dataBean);
		HSSFWorkbook output = null;;

		try {
			if(sheetNameToReplace != null && sheetNameToReplace.trim().length() > 0 && replacementName != null && replacementName.trim().length() > 0) {
				transformer.setSpreadsheetToRename(sheetNameToReplace, sheetNameToReplace);
			}
			output = (HSSFWorkbook) transformer.transformXLS(is, excelBeans);
		} catch (ParsePropertyException e) {
			log.severe("ParsePropertyException in ExcelReportGenerator : "+e.getMessage());
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			log.severe("InvalidFormatException in ExcelReportGenerator : "+e.getMessage());
			e.printStackTrace();
		} catch(Exception e){
			log.severe("Exception in ExcelReportGenerator : "+e.getMessage());
			e.printStackTrace();
		}

		byte[] xls = null ;
		try {	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			output.write(baos);
			xls = baos.toByteArray();
		} catch (IOException e) {
			log.severe("IOException in ExcelReportGenerator : "+e.getMessage());
			e.printStackTrace();
		}
		return xls;
	}


	public byte[] advertiserReportGenerate(String templateName,Map map) throws Exception
	{  
		String  path = "/com/lin/reporttemplates/" + templateName;
		InputStream is = null;
		try
		{ 
			is = 	getClass().getResourceAsStream(path);
			if (is == null) 
			{
				throw new Exception("Cannot find template");
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception in loading template " + e);
			throw e;
		}
		XLSTransformer transformer = new XLSTransformer();
		Map excelBeans = new HashMap();
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry mapEntry = (Map.Entry) iterator.next();
			excelBeans.put(mapEntry.getKey(), mapEntry.getValue());
		}
		HSSFWorkbook output = null;;
		try {
			output = (HSSFWorkbook) transformer.transformXLS(is, excelBeans);
			// output =  (HSSFWorkbook) transformer.transformMultipleSheetsList(is, dataBean, sheetNames, "list", new HashMap(), 0);
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		byte[] xls = null ;
		try 
		{	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			output.write(baos);
			xls = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xls;
	}

	public byte[] dynamicSheetReport(String templateName,List<Object> dataBean, List<String> sheetNames, String beanName, int startPage) throws Exception
	{  
		String  path = "/com/lin/reporttemplates/" + templateName;
		InputStream is = null;
		try
		{ 
			is = 	getClass().getResourceAsStream(path);
			if (is == null) 
			{
				throw new Exception("Cannot find template");
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception in loading template " + e);
			throw e;
		}
		XLSTransformer transformer = new XLSTransformer();

		HSSFWorkbook output = null;;
		try {
			output =  (HSSFWorkbook) transformer.transformMultipleSheetsList(is, dataBean, sheetNames, beanName, new HashMap(), startPage);
		} catch (ParsePropertyException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		byte[] xls = null ;
		try 
		{	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			output.write(baos);
			xls = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xls;
	}


}











