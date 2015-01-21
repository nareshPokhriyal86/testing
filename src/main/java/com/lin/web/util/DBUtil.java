package com.lin.web.util;

public class DBUtil {
	public static final String AND_PREFIX = "AND";
	public static final String OR_PREFIX = "OR";
	public static final String EQUAL_OPERATOR = "=";
	public static final String LIKE_OPERATOR = "LIKE";
	public static final String IN_OPERATOR = "IN";
	public static final String BRACKET_OPEN = "(";
	public static final String BRACKET_CLOSE= ")";

	 /**
     * Appends the AND Clause to a QueryString
     * @param StringBuffer - query
     */
	public static final void addAndClause(StringBuffer queryStr){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		queryStr.append(StringUtil.WHITESPACE);
		queryStr.append(AND_PREFIX);
		queryStr.append(StringUtil.WHITESPACE);
	}

	/**
     * Appends the Opening Barcket Clause to a QueryString
     * @param StringBuffer - query
     */
	public static final void addBracketOpenClause(StringBuffer queryStr){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		queryStr.append(StringUtil.WHITESPACE);
		queryStr.append(BRACKET_OPEN);
		queryStr.append(StringUtil.WHITESPACE);
	}
	
	public static final void addBracketCloseClause(StringBuffer queryStr){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		queryStr.append(StringUtil.WHITESPACE);
		queryStr.append(BRACKET_CLOSE);
		queryStr.append(StringUtil.WHITESPACE);
	}
	
	public static final void addWhereClause(StringBuffer queryStr, String prefix, String property, String value, String operator, boolean isDBNumeric){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		if(StringUtil.isBlank(value)){
			return;
		}
		queryStr.append(StringUtil.WHITESPACE);
		queryStr.append(StringUtil.getBlankIfNull(prefix));
		queryStr.append(StringUtil.WHITESPACE);
		queryStr.append(property);
		queryStr.append(StringUtil.WHITESPACE);
		queryStr.append(operator);
		if (isDBNumeric){
			queryStr.append(value);
		}else if( IN_OPERATOR.equalsIgnoreCase(operator)){
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_OPEN);
			queryStr.append(value);
			queryStr.append(BRACKET_CLOSE);
			queryStr.append(StringUtil.WHITESPACE);
		}else{
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append("'");
			if (LIKE_OPERATOR.equalsIgnoreCase(operator)){
				queryStr.append("%");
			}
			queryStr.append(value);
			if (LIKE_OPERATOR.equalsIgnoreCase(operator)){
				queryStr.append("%");
			}
			queryStr.append("'");
			
		}
		queryStr.append(StringUtil.WHITESPACE);
	}
	
	public static final void addPropertyWhereClause(StringBuffer queryStr, String prefix, String property1, String property2){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		
		queryStr.append(StringUtil.WHITESPACE);
		queryStr.append(StringUtil.getBlankIfNull(prefix));
		queryStr.append(StringUtil.WHITESPACE);
		queryStr.append(property1);
		queryStr.append(EQUAL_OPERATOR);
		queryStr.append(property2);
		queryStr.append(StringUtil.WHITESPACE);
	}
	
	public static final StringBuffer addDay(StringBuffer queryStr,String day){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		if(day !=null && day.trim().length()>0){
			queryStr.append(BRACKET_OPEN);
			queryStr.append("day:");
			queryStr.append(day);
			queryStr.append(BRACKET_CLOSE);
			
		}
		return queryStr;
	}
	
	public static final StringBuffer addMonth(StringBuffer queryStr,String month){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		if(month !=null && month.trim().length()>0){
			queryStr.append(BRACKET_OPEN);
			queryStr.append("month:");
			queryStr.append(month);
			queryStr.append(BRACKET_CLOSE);
			
		}
		return queryStr;
	}
	
	public static final StringBuffer addYear(StringBuffer queryStr,String year){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		if(year !=null && year.trim().length()>0){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append("year:");
			queryStr.append(year);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
			
		}
		return queryStr;
	}
	
	public static final StringBuffer addAuthor(StringBuffer queryStr,String author){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		if(author !=null && author.trim().length()>0){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append("author:");
			queryStr.append(author);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
			
		}
		return queryStr;
	}
	
	public static final StringBuffer addSearchTag(StringBuffer queryStr,String searchTag){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		if(searchTag !=null && searchTag.trim().length()>0){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append("zipToken:");
			queryStr.append(searchTag);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(OR_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append("token:");
			queryStr.append(searchTag);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
		}
		return queryStr;
	}
	
	
	public static final StringBuffer createSearchQuery(String searchTag,String author,String year,String month,String day){
		StringBuffer queryStr=new StringBuffer();
		boolean includeDate=false;
		boolean includeTag=false;
		boolean includeAuthor=false;
		if((year !=null && year.trim().length()>0) 
				|| (month !=null && month.trim().length()>0) 
				|| (day !=null && day.trim().length()>0)){
			includeDate=true;
		}
		if(searchTag !=null && searchTag.trim().length()>0){	
			includeTag=true;
		}
		if(author !=null && author.trim().length()>0){
			includeAuthor=true;
		}
		
		if(includeTag && includeAuthor && includeDate){			
			queryStr=addSearchTag(queryStr, searchTag);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addAuthor(queryStr,author);	
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);			
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addDate(queryStr,year,month,day);
			
		}else if(includeTag && includeAuthor){
			queryStr=addSearchTag(queryStr, searchTag);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addAuthor(queryStr,author);			
		}
		else if(includeTag && includeDate){
			queryStr=addSearchTag(queryStr, searchTag);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);			
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addDate(queryStr,year,month,day);
			queryStr.append(StringUtil.WHITESPACE);
		}
		else if(includeAuthor && includeDate){
            queryStr=addAuthor(queryStr,author);
            queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);			
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addDate(queryStr,year,month,day);
		}
		else if(includeTag){
			queryStr=addSearchTag(queryStr, searchTag);
		}
		else if(includeAuthor){
			queryStr=addAuthor(queryStr,author);
		}
		else if(includeDate){
			queryStr=addDate(queryStr,year,month,day);
		}		
		
		return queryStr;
	}
	
	
	public static final StringBuffer createSearchQuery(String searchTag){
		StringBuffer queryStr=new StringBuffer();		
		queryStr=addSearchTag(queryStr, searchTag);		
		return queryStr;
	}
	
	public static final StringBuffer createAdUnitSearchQuery(String searchTag,String adServerId){
		StringBuffer queryStr=new StringBuffer();		
	
		if(searchTag !=null && searchTag.trim().length()>0){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append("canonicalPathTokens:");
			queryStr.append(searchTag);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(OR_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append("adUnitTokens:");
			queryStr.append(searchTag);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append("adServerId:");
			queryStr.append(adServerId);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
			
		}
		
		return queryStr;
	}
	public static final StringBuffer addDate(StringBuffer queryStr,String year,String month,String day){
		if (queryStr == null){
			throw new IllegalArgumentException("queryString cannot be null");
		}
		boolean includeYear=false;
		boolean includeMonth=false;
		boolean includeDay=false;
		
    	
		if(year !=null && year.trim().length()>0){
			includeYear=true;
		}
		
		if(month !=null && month.trim().length()>0){
			includeMonth=true;
			if(month.trim().length()==1){
	    		month="0"+month.trim();
	    	}
		}
		if(day !=null && day.trim().length()>0){
			includeDay=true;
			if(day.trim().length()==1){
	    		day="0"+day.trim();
	    	}
		}
		
		if(includeYear && includeMonth && includeDay){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addYear(queryStr,year);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addMonth(queryStr,month);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addDay(queryStr,day);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
		}
		else if(includeYear && includeMonth){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addYear(queryStr,year);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addMonth(queryStr,month);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
		}
		else if(includeYear && includeDay){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addYear(queryStr,year);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addDay(queryStr,day);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
		}
		else if(includeMonth && includeDay){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addMonth(queryStr,month);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(AND_PREFIX);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addDay(queryStr,day);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
		}
		else if(includeYear){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addYear(queryStr,year);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
		}
		else if(includeMonth){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addMonth(queryStr,month);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
		}
		else if(includeDay){
			queryStr.append(BRACKET_OPEN);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr=addDay(queryStr,day);
			queryStr.append(StringUtil.WHITESPACE);
			queryStr.append(BRACKET_CLOSE);
		}
		return queryStr;
	}
	
}
