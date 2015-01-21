package com.lin.server.Exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.logging.Logger;






public class LinMobileBaseException extends Exception implements Serializable{
	private static final Logger log = Logger.getLogger(LinMobileBaseException.class.getName());
	private static final long serialVersionUID = 1L;
		/**
	    * The String showing the message for logging
	    */
    private String logMessage = null;
	   /**
	    * The String showing the message for client
	    */
    private String clientMessage = null;

	   /**
	    * An optional nested exception used to provide the ability to encapsulate
	    * a lower-level exception to provide more detailed context information
	    * concerning the exact cause of the exception.
	    */
	protected Throwable rootCause = null;

	   
	    /**
	     * Default Constructor
	     */
	    public LinMobileBaseException() {
	      super();
	      log.severe(this+toString());
	    }

	    public LinMobileBaseException(Throwable arg0) {
		      super();
		      log.severe(this+toString());
		    }


	    /**
	     * Constructor
	     * @param String exceptionContext -  a string for logging in log file
	     */
	         public LinMobileBaseException(String exceptionContext) {
	                 super(exceptionContext);
	                 this.logMessage=exceptionContext;
	                 this.clientMessage = exceptionContext;
	                 log.severe(this+toString());

	           }


	    /**
	     * @return String - an error string based on exception's values
	     */
	    public String toString() {

	        StringBuffer exceptionMessage = new StringBuffer("EXCEPTION: " +
	        this.getClass().getName());


	        // if an logged message exists, add it to the message
	        if (this.logMessage != null) {
	            exceptionMessage.append("; PARAMETER INFO: "
	                + this.logMessage);
	        }

	        return exceptionMessage.toString();
	    }


	   /**
	     * Print both the normal and rootCause stack traces.
	     * @param writer PrintWriter instance used for printing the error message
	     *  contents
	     */
	        public void printStackTrace(PrintWriter writer) {
	            super.printStackTrace(writer);
	            if(getRootCause() != null) {
	              getRootCause().printStackTrace(writer);
	            }
	            writer.flush();
	        }

	    /**
	     * Print both the normal and rootCause stack traces.
	     *
	     * @param outStream PrintStream instance used for printing the error message
	     *  contents
	     */
	        public void printStackTrace(PrintStream outStream) {
	            printStackTrace(new PrintWriter(outStream));
	        }

	    /**
	     *  Print both the normal and rootCause stack traces.
	     */
	        public void printStackTrace() {
	            printStackTrace(System.err);
	        }

	    /**
	     * Return the root cause exception.
	     *
	     * @return Throwable
	     */
	        public Throwable getRootCause() {
	            return rootCause;
	        }

	    /**
	     * Set a nested, encapsulated exception to provide more low-level
	     * detailed information to the client.
	     *
	     * @param previousException The root cause for the exception
	     */
	    public void setRootCause(Throwable previousException) {
	            rootCause = previousException;
	    }


	    /**
	     * Return the exception message for the client.
	     *
	     * @return String
	     */
	          public String getClientMessage(){
	                  return clientMessage;
	    }

	    /**
	     * Set exception message for the client
	     *
	     * @param clientMessage The String client exception message
	     */
	    public void setClientMessage(String clientMessage){
	                  this.clientMessage = clientMessage;
	    }


	    /**
	     * Return the logged reason for the exception occurs.
	     *
	     * @return String
	     */
	    public String getExceptionContext() {
	            return logMessage;
	    }
	}
