package com.vluee.codeflower.rest;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author SeanYe
 *
 */
public class GeneralResponseBuilder {

	private boolean appendExceptionSimpleMesssage = true;
	private boolean appendExceptionStackTrace = false;

	public GeneralRestResponse<Void> ok() {
		return new GeneralRestResponse<Void>(this.getSuccessCode(), null, null, null);
	}

	public <T> GeneralRestResponse<T> okWithEntity(T body) {
		return new GeneralRestResponse<T>(this.getSuccessCode(), null, null, body);
	}

	protected String getSuccessCode() {
		return "SA0000";
	}

	protected String getUnknownErrorCode() {
		return "SA9999";
	}

	public GeneralRestResponse<Void> codeResponse(String code) {
		if (StringUtils.isBlank(code)) {
			return new GeneralRestResponse<Void>(getUnknownErrorCode(), null, null, null);
		}
		return new GeneralRestResponse<Void>(code, null, null, null);
	}

	public GeneralRestResponse<Void> codeResponse(String string, Throwable anyException) {
		GeneralRestResponse<Void> codeResponse = this.codeResponse(string);
		if (appendExceptionSimpleMesssage) {
			String message = codeResponse.getMessage();
			StringBuilder builder = new StringBuilder((message == null ? "" : message) + " ->");
			builder.append(System.lineSeparator());
			builder.append(anyException.getMessage());
			codeResponse.setMessage(builder.toString());
		}
		return codeResponse;
	}
}
