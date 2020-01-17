package com.vluee.codeflower.rest;

import java.io.PrintWriter;

import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.vluee.codeflower.exception.ExceptionRepository;
import com.vluee.codeflower.exception.InMemExceptionRepository;

/**
 * 
 * @author SeanYe
 *
 */
public class GeneralRestResponseBuilder {

	private boolean appendExceptionSimpleMesssage = true;

	/**
	 * 不建议设置为 true. Json输出后会转化为 \r\n 之类的字符串，并不适合于阅读。
	 */
	private boolean appendExceptionStackTrace = false;

	/**
	 */
	private ExceptionRepository exceptionRepository = new InMemExceptionRepository();

	public GeneralRestResponseBuilder() {
		this(null);
	}

	public GeneralRestResponseBuilder(ExceptionRepository exceptionRepository) {
		if (exceptionRepository != null) {
			this.exceptionRepository = exceptionRepository;
		}
		Validate.notNull(this.exceptionRepository, "Exception Repository can't be null");
	}

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

	/**
	 * 无法给一个Exception定义Code的时候，直接使用这个方法。
	 * 
	 * @param e
	 * @return
	 */
	public GeneralRestResponse<Void> failResponse(Throwable e) {
		return codeResponse(getUnknownErrorCode(), e);
	}

	public GeneralRestResponse<Void> codeResponse(String code) {
		return codeResponse(StringUtils.isNotBlank(code) ? code : getUnknownErrorCode(), null);
	}

	public GeneralRestResponse<Void> codeResponse(String code, Throwable anyException) {
		GeneralRestResponse<Void> codeResponse = new GeneralRestResponse<Void>(code, null, null, null);
		if (anyException != null) {
			codeResponse.setExceptionId(exceptionRepository.store(anyException));
		}
		codeResponse.setMessage(getCodeDescription(code));
		if (anyException != null) {
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
		}
		return codeResponse;
	}

	private String getCodeDescription(String code) {
		return "TODO";
	}

	public void setAppendExceptionSimpleMesssage(boolean appendExceptionSimpleMesssage) {
		this.appendExceptionSimpleMesssage = appendExceptionSimpleMesssage;
	}

	public void setAppendExceptionStackTrace(boolean appendExceptionStackTrace) {
		this.appendExceptionStackTrace = appendExceptionStackTrace;
	}

}
