package com.vluee.codeflower.exception;

/**
 * 
 * 保存异常并分配异常traceId以后续跟踪查询
 * 
 * @author SeanYe
 *
 */
public interface ExceptionRepository {

	/**
	 * 记录异常
	 * 
	 * @param exception
	 * @return
	 */
	public String store(Throwable exception);

	/**
	 * 根据traceId获取异常详情
	 * 
	 * @param id
	 * @return
	 */
	String getException(String id);

}
