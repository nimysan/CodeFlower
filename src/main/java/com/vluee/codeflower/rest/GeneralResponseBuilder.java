package com.vluee.codeflower.rest;

import java.io.PrintWriter;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author SeanYe
 *
 */
public class GeneralResponseBuilder {
	
	
	private boolean appendExceptionSimpleMesssage = true;

	/**
	 * 不建议设置为 true. Json输出后会转化为  \r\n 之类的字符串，并不适合于阅读。
	 */
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

	public GeneralRestResponse<Void> codeResponse(String code, Throwable anyException) {
		GeneralRestResponse<Void> codeResponse = this.codeResponse(code);
		if (appendExceptionSimpleMesssage) {
			String message = codeResponse.getMessage();
			StringBuilder builder = new StringBuilder((message == null ? "" : message) + " - [");
			builder.append(anyException.getMessage());
			builder.append("]");
			if (appendExceptionStackTrace) {
				anyException.printStackTrace(new PrintWriter(new StringBuilderWriter(builder)));
			}
			codeResponse.setMessage(builder.toString());
		}
		return codeResponse;
	}

	public void setAppendExceptionSimpleMesssage(boolean appendExceptionSimpleMesssage) {
		this.appendExceptionSimpleMesssage = appendExceptionSimpleMesssage;
	}

	public void setAppendExceptionStackTrace(boolean appendExceptionStackTrace) {
		this.appendExceptionStackTrace = appendExceptionStackTrace;
	}

}
