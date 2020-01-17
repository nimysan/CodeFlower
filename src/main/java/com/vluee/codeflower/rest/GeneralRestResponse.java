package com.vluee.codeflower.rest;

public class GeneralRestResponse<T> {
	private String code;
	private String message;
	private String exceptionId;
	private T body;

	public GeneralRestResponse(String code, String message, String exceptionId, T body) {
		super();
		this.code = code;
		this.message = message;
		this.exceptionId = exceptionId;
		this.body = body;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

}
