package com.vluee.codeflower.exception;

/**
 * 标识一个领域异常
 * 
 * @author SeanYe
 *
 */
public class GeneralDomainException extends RuntimeException {

	private static final long serialVersionUID = -6619809515630729874L;

	private String code;

	public GeneralDomainException(String code, Exception e) {
		super(e);
		this.code = code;
	}
	
	public GeneralDomainException(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
