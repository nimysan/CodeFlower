package com.vluee.codeflower.exception;

@FunctionalInterface
public interface CodeMessageConverter {
	public String getMessage(String code);
}
