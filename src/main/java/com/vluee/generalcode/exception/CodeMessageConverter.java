package com.vluee.generalcode.exception;

@FunctionalInterface
public interface CodeMessageConverter {
	public String getMessage(String code);
}
