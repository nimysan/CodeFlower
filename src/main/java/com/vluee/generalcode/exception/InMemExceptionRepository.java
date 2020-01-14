package com.vluee.generalcode.exception;

import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 
 * @author SeanYe
 *
 */
public final class InMemExceptionRepository implements ExceptionRepository {

	private final Cache<String, String[]> exceptionCache = CacheBuilder.newBuilder().maximumSize(1000).build();

	@Override
	public String record(Exception exception) {

		if (exception == null) {
			return "";
		}

		String id = this.generateId();
		exceptionCache.put(id, ExceptionUtils.getStackFrames(exception));
		return id;
	}

	// TODO how to handle it if we want record the occur time?
	@Override
	public String getException(String id) {
		String[] ifPresent = exceptionCache.getIfPresent(id);
		if (ifPresent != null && ifPresent.length > 0) {
			return String.join(System.lineSeparator(), ifPresent);
		}
		return "Exception information does not exist or expired";
	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}

}
