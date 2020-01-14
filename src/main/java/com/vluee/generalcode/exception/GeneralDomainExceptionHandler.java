package com.vluee.generalcode.exception;

import static com.vluee.generalcode.GeneralCodeConstants.RETURN_CODE_LABEL;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vluee.generalcode.CodeRepository;
import com.vluee.generalcode.GeneralCodeConstants;

/**
 * 
 * @author SeanYe
 *
 */
public class GeneralDomainExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GeneralDomainExceptionHandler.class);

	private ExceptionRepository exceptionRepository;

	private CodeRepository codeRepository;

	public GeneralDomainExceptionHandler(CodeRepository codeRepository, ExceptionRepository exceptionRepository) {
		this.codeRepository = codeRepository;
		this.exceptionRepository = exceptionRepository;
		Validate.notNull(codeRepository, "CodeRepository can't be null");
		Validate.notNull(exceptionRepository, "ExceptionRepository need to be initialized");
	}

	/**
	 * 提取异常消息信息。
	 * 
	 * @param exception
	 * @return
	 */
	public Map<String, String> extract(Exception exception) {
		String traceId = exceptionRepository.record(exception);
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Exception [%s]", traceId == null ? "" : traceId), exception);
		}
		Map<String, String> result = new HashMap<String, String>(2);
		// result.put(traceId, ExceptionUtils.get) 将Exception异常存储起来， 方便后期调用
		result.put(GeneralCodeConstants.RETURN_EXCEPTION_LABEL, traceId);
		if (exception instanceof GeneralDomainException) {
			GeneralDomainException gde = (GeneralDomainException) exception;
			String code = gde.getCode();
			result.put(RETURN_CODE_LABEL, codeRepository.getCode(code).get().getCode());
			result.put(GeneralCodeConstants.RETURN_MESSAGE_LABEL, gde.getMessage());
		} else {
			result.put(RETURN_CODE_LABEL,
					GeneralCodeConstants.DEFAULT_PREFIX + GeneralCodeConstants.UNDEFINED_CODE_REPLACEMENT);
			result.put(GeneralCodeConstants.RETURN_MESSAGE_LABEL, exception.getMessage());
		}
		return result;
	}

	public String getExceptionDetail(String traceId) {
		if (StringUtils.isBlank(traceId)) {
			return null;
		}
		return exceptionRepository.getException(traceId);
	}

}
