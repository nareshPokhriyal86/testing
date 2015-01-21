package com.lin.web.util;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import com.google.api.services.bigquery.model.QueryResponse;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.BooleanValue;
import com.google.visualization.datasource.datatable.value.DateTimeValue;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.NumberValue;
import com.google.visualization.datasource.datatable.value.TextValue;
import com.google.visualization.datasource.datatable.value.TimeOfDayValue;
import com.google.visualization.datasource.datatable.value.Value;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;

public class GoogleVisulizationUtil {

	static public DataTable buildColumns(QueryResponse response, List<String> columnIdsList) throws SQLException {
		  
		  
		 DataTable datatable = new DataTable();
		 //ResultSetMetaData metaData = rs.getMetaData();
		 TableSchema tabSchema = response.getSchema();
		 
		 List<TableFieldSchema> fields = tabSchema.getFields();
		 
		 for (TableFieldSchema schemaItem : fields){
		   ColumnDescription columnDescription =
		       new ColumnDescription(schemaItem.getName(),
		           sqlTypeToValueType(schemaItem.getType()),
		           schemaItem.getName());
		   datatable.addColumn(columnDescription);
		 }		 
		 return datatable;
	}
	
	 static public ValueType sqlTypeToValueType(String bqType) {
		  
		 ValueType valueType;
		 
		 BQDataType bqDataType = BQDataType.valueOf(bqType);
		 
		 switch (bqDataType) {
		  
		   case RECORD:
		     valueType = ValueType.TEXT;
		     break;
		   case BOOLEAN :
		      valueType =  ValueType.BOOLEAN;
		     break;
		   case STRING:
		     valueType =  ValueType.TEXT;
		     break;
		   case INTEGER:
		   case FLOAT:
		     valueType = ValueType.NUMBER;
		     break;
		   case NUMBER:
			     valueType = ValueType.NUMBER;
			     break;
		   case TIMESTAMP:
		     valueType = ValueType.DATETIME;
		     break;
		   default:
		     valueType = ValueType.TEXT;
		     break;
		 }
		 return valueType;
		}
	
	private enum BQDataType {

		  STRING, INTEGER, FLOAT, BOOLEAN,TIMESTAMP,RECORD,NUMBER
		  
		}
	
	static public DataTable buildRows(DataTable dataTable, QueryResponse results) throws SQLException {
		 
		  DataTable modTab = dataTable;		  
		  List<com.google.api.services.bigquery.model.TableRow> rows = results.getRows();
		  for ( com.google.api.services.bigquery.model.TableRow row : rows) {
		    TableRow tableRow = new TableRow();
		    int i =0;
		    for (com.google.api.services.bigquery.model.TableCell field : row.getF()){
		    	ColumnDescription colDesc = modTab.getColumnDescription(i);
		    	
		    	switch (colDesc.getType()){
		    	   case BOOLEAN:
//		    		   tableRow.addCell(new BooleanValue(true));
//		    	     value = BooleanValue.getInstance(rs.getBoolean(column));
		    	     break;
		    	   case NUMBER:
		    		   //Double d = new Double(field.getV().toString());
		    		   Double d = StringUtil.getDoubleValue(field.getV().toString(), 4);
		    		   tableRow.addCell(new NumberValue(d.doubleValue()));
		    	     break;
		    	   case DATE:
//		    	     Date date = rs.getDate(column);
		    	     break;
		    	   case TEXT:
		    		   tableRow.addCell(new TextValue(field.getV().toString()));
		    		   break;
		    	}
		    	i++;
		            //System.out.printf("%-50s", field.getV());
		    }

		    try {
		    	modTab.addRow(tableRow);
		    } catch (TypeMismatchException e) {
		    	System.out.println("Exception " + e);
		      // Should not happen. An SQLException would already have been thrown if there was such a
		      // problem.
		    }

		  }
		  
		 // System.out.println(" Rows" + modTab.getNumberOfRows()); 
		  return modTab;
   }
	
	public static TableCell buildTableCell(ResultSet rs, ValueType valueType,
			   int column) throws SQLException {
			 Value value = null;

			 // SQL indexes are 1- based.
			 column = column + 1;

			 switch (valueType) {
			   case BOOLEAN:
			     value = BooleanValue.getInstance(rs.getBoolean(column));
			     break;
			   case NUMBER:
			     value = new NumberValue(rs.getDouble(column));
			     break;
			   case DATE:
			     Date date = rs.getDate(column);
			     // If date is null it is handled later.
			     if (date != null) {
			       GregorianCalendar gc =
			           new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			       // Set the year, month and date in the gregorian calendar.
			       // Use the 'set' method with those parameters, and not the 'setTime'
			       // method with the date parameter, since the Date object contains the
			       // current time zone and it's impossible to change it to 'GMT'.
			       gc.set(date.getYear() + 1900, date.getMonth(), date.getDate());
			       value = new DateValue(gc);
			     }
			     break;
			   case DATETIME:
			     Timestamp timestamp = rs.getTimestamp(column);
			     // If timestamp is null it is handled later.
			     if (timestamp != null) {
			       GregorianCalendar gc =
			           new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			       // Set the year, month, date, hours, minutes and seconds in the
			       // gregorian calendar. Use the 'set' method with those parameters,
			       // and not the 'setTime' method with the timestamp parameter, since
			       // the Timestamp object contains the current time zone and it's
			       // impossible to change it to 'GMT'.
			       gc.set(timestamp.getYear() + 1900, timestamp.getMonth(),
			              timestamp.getDate(), timestamp.getHours(), timestamp.getMinutes(),
			              timestamp.getSeconds());
			       // Set the milliseconds explicitly, as they are not saved in the
			       // underlying date.
			       gc.set(Calendar.MILLISECOND, timestamp.getNanos() / 1000000);
			       value = new DateTimeValue(gc);
			     }
			     break;
			   case TIMEOFDAY:
			     Time time = rs.getTime(column);
			     // If time is null it is handled later.
			     if (time != null) {
			       GregorianCalendar gc =
			           new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			       // Set the hours, minutes and seconds of the time in the gregorian
			       // calendar. Set the year, month and date to be January 1 1970 like
			       // in the Time object.
			       // Use the 'set' method with those parameters,
			       // and not the 'setTime' method with the time parameter, since
			       // the Time object contains the current time zone and it's
			       // impossible to change it to 'GMT'.
			       gc.set(1970, Calendar.JANUARY, 1, time.getHours(), time.getMinutes(),
			              time.getSeconds());
			       // Set the milliseconds explicitly, otherwise the milliseconds from
			       // the time the gc was initialized are used.
			       gc.set(GregorianCalendar.MILLISECOND, 0);
			       value = new TimeOfDayValue(gc);
			     }
			     break;
			   default:
			     String colValue = rs.getString(column);
			     if (colValue == null) {
			       value = TextValue.getNullValue();
			     } else {
			       value = new TextValue(rs.getString(column));
			     }
			     break;
			 }
			 // Handle null values.
			 if (rs.wasNull()) {
			   return new TableCell(Value.getNullValueFromValueType(valueType));
			 } else {
			   return new TableCell(value);
			  
			 }
			}
	
	
	public static  DataTable buildCustomColumns(List<String> columnList,TableSchema tabSchema) throws SQLException {
		 DataTable datatable = new DataTable();
		 //ResultSetMetaData metaData = rs.getMetaData();
		 
		 List<TableFieldSchema> fields = tabSchema.getFields();
		 
		 for (TableFieldSchema schemaItem : fields){
			 if(columnList.contains(schemaItem.getName())){
				 ColumnDescription columnDescription = new ColumnDescription(schemaItem.getName(), 
						 sqlTypeToValueType(schemaItem.getType()), schemaItem.getName());
				 datatable.addColumn(columnDescription);
			 }
		 }		 
		 return datatable;
	}
	
	// Added By Anup 
	static public DataTable buildCustomColumns(QueryResponse response, List<String> columnList) throws SQLException {
		 DataTable datatable = new DataTable();
		 //ResultSetMetaData metaData = rs.getMetaData();
		 TableSchema tabSchema = response.getSchema();
		 
		 List<TableFieldSchema> fields = tabSchema.getFields();
		 
		 for (TableFieldSchema schemaItem : fields){
			 if(columnList.contains(schemaItem.getName())){
				 ColumnDescription columnDescription = new ColumnDescription(schemaItem.getName(), sqlTypeToValueType(schemaItem.getType()), schemaItem.getName());
				 datatable.addColumn(columnDescription);
			 }
		 }		 
		 return datatable;
	}
	
	static public DataTable buildCustomRows(DataTable dataTable, QueryResponse results,int skipCol) throws SQLException {
		 
		  DataTable modTab = dataTable;		  
		  List<com.google.api.services.bigquery.model.TableRow> rows = results.getRows();
		  for ( com.google.api.services.bigquery.model.TableRow row : rows) {
		    TableRow tableRow = new TableRow();
		    int i =0;
		    int j = 0;
		    for (com.google.api.services.bigquery.model.TableCell field : row.getF()){
		    	if(j != skipCol){
			    	ColumnDescription colDesc = modTab.getColumnDescription(i);
			    	
			    	switch (colDesc.getType()){
			    	   case BOOLEAN:
			    	     break;
			    	   case NUMBER:
			    		   Double d = StringUtil.getDoubleValue(field.getV().toString(), 4);
			    		   tableRow.addCell(new NumberValue(d.doubleValue()));
			    	     break;
			    	   case DATE:
			    	     break;
			    	   case TEXT:
			    		   tableRow.addCell(new TextValue(field.getV().toString()));
			    		   break;
			    	}
			    	i++;
		    	}
		    	j++;
		    }

		    try {
		    	modTab.addRow(tableRow);
		    } catch (TypeMismatchException e) {
		    	System.out.println("Exception " + e);
		      // Should not happen. An SQLException would already have been thrown if there was such a
		      // problem.
		    }

		  }
		  
		 // System.out.println(" Rows" + modTab.getNumberOfRows()); 
		  return modTab;
 }
	
	@SuppressWarnings("incomplete-switch")
	/*
	 * @author Youdhveer Panwar
	 * RichMedia Donut chart -- Create rows
	 */
	public static DataTable buildCustomRows(DataTable dataTable, QueryResponse results,List<Integer> skipCol,String customEvent) throws SQLException {
		 
		  DataTable modTab = dataTable;		  
		  List<com.google.api.services.bigquery.model.TableRow> rows = results.getRows();
		  for ( com.google.api.services.bigquery.model.TableRow row : rows) {
		    TableRow tableRow = new TableRow();
		    int i =0;
		    int j = 0;
		    String customEventBQ=null;
		    for (com.google.api.services.bigquery.model.TableCell field : row.getF()){
		    	if(j==0){
		    		customEventBQ=field.getV().toString();
		    	}
		    	if(!skipCol.contains(j)){
		    		if(customEventBQ !=null && customEventBQ.equalsIgnoreCase(customEvent)){
		    			ColumnDescription colDesc = modTab.getColumnDescription(i);
				    	
				    	switch (colDesc.getType()){
				    	   case BOOLEAN:
				    	     break;
				    	   case NUMBER:
				    		   Double d = StringUtil.getDoubleValue(field.getV().toString(), 4);
				    		   tableRow.addCell(new NumberValue(d.doubleValue()));
				    	     break;
				    	   case DATE:
				    	     break;
				    	   case TEXT:
				    		   tableRow.addCell(new TextValue(field.getV().toString()));
				    		   break;
				    	}
		    		}else{
		    			//System.out.println("customEventBQ : "+customEventBQ+" and customEvent:"+customEvent);
		    			tableRow=null;
		    		}
			    	
			    	i++;
		    	}
		    	j++;
		    }

		    try {
		    	if(tableRow !=null){
		    		modTab.addRow(tableRow);
		    	}
		    } catch (TypeMismatchException e) {
		    	System.out.println("Exception " + e);
		      
		    }

		  }		
		  return modTab;
   }
	
}
