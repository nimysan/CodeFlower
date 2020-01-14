package com.vluee.generalcode.exception;

public interface ExceptionRepository {

	public String record(Exception exception);

	String getException(String id);

}
