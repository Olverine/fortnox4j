package org.notima.api.fortnox.entities3;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ErrorInformation")
public class ErrorInformation {
	
	private String error;
	private String message;
	private String code;
	
	
	@XmlElement(name="Error")
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	@XmlElement(name="Message")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@XmlElement(name="Code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String toString() {
		return (getError() + " : " + getMessage() + " : " + getCode());
	}
	
}
