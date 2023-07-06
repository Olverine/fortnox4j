package org.notima.api.fortnox;

import java.io.StringReader;

import javax.xml.bind.JAXB;

import org.notima.api.fortnox.entities3.ErrorInformation;

/**
 * 
 * An exception containing a Fortnox API error record.
 * 
 */
public class FortnoxException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5975880802908077519L;
	
	private org.notima.api.fortnox.entities3.Error error;
	private org.notima.api.fortnox.entities3.ErrorInformation errorInformation;
	
	public FortnoxException(String xmlStr) {
		
		StringReader reader = new StringReader(xmlStr);
		try {
			errorInformation = JAXB.unmarshal(reader, ErrorInformation.class);
		} catch (Exception ee) {
			errorInformation = new ErrorInformation();
			errorInformation.setMessage(xmlStr);
			errorInformation.setError("");
			errorInformation.setCode("");
		}
		
	}
	
	public FortnoxException(org.notima.api.fortnox.entities3.Error e) {
		error = e;
	}
	
	public FortnoxException(org.notima.api.fortnox.entities3.ErrorInformation e) {
		errorInformation = e; 
	}
	
	public void appendMessage(String additionalMessage) {
		if (additionalMessage!=null && additionalMessage.trim().length()>0) {
			if (error!=null) {
				if (error.getMessage()!=null && error.getMessage().trim().length()>0) {
					error.setMessage(error.getMessage() + " : " + additionalMessage);
				} else {
					error.setMessage(additionalMessage);
				}
			}
			if (errorInformation!=null) {
				errorInformation.appendMessage(additionalMessage);
			}
		}
	}
	
	public org.notima.api.fortnox.entities3.Error getError() {
		return error;
	}

	public void setError(org.notima.api.fortnox.entities3.Error error) {
		this.error = error;
	}

	public org.notima.api.fortnox.entities3.ErrorInformation getErrorInformation() {
		return errorInformation;
	}

	public void setErrorInformation(
			org.notima.api.fortnox.entities3.ErrorInformation errorInformation) {
		this.errorInformation = errorInformation;
	}

	public boolean isInvalidLoginException() {
		
		// There seems to be more than one code in Fortnox that implies invalid credentials
		if (errorInformation!=null && errorInformation.getCode()!=null) {
			String errCodeString = errorInformation.getCode().toString();
			return isInvalidLoginCode(errCodeString);
		}
		// TODO The below error-struct might be deprecated from Fortnox's side.
		if (error!=null && error.getCode()!=null) {
			String errCodeString = error.getCode().toString();
			return isInvalidLoginCode(errCodeString);
		}
		return false;
		
	}
	
	private boolean isInvalidLoginCode(String code) {
		if (code==null) return false;
		return (code.equals(FortnoxClient3.ERROR_INVALID_LOGIN) || 
				code.equals(FortnoxClient3.ERROR_INVALID_LOGIN_V2) ||
				code.equals(FortnoxClient3.ERROR_MISSING_CREDENTIALS));
	}
	
	public String toString() {
		if (error!=null)
			return(error.getCode() + " : " + error.getMessage());
		else
			return (errorInformation.getError() + " : " + errorInformation.getMessage() + " : " + errorInformation.getCode());
	}

	public String getMessage() {
		return this.toString();
	}
	
}
