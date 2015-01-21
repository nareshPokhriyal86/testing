package com.lin.server.Exception;

public class DataServiceException extends LinMobileBaseException {

	private static final long serialVersionUID = 2774636125636672199L;
	/**
	 * Default Constructor
	 */
	@SuppressWarnings("unused")
	private DataServiceException() {
		super();
	}
	/**
	 * @param arg0
	 */
	public DataServiceException(String arg0) {
		super(arg0);
	}
	
	public DataServiceException(Throwable arg0) {
		super(arg0);
	}

}
