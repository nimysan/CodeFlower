package com.vluee.codeflower.exception;

import static com.vluee.codeflower.GeneralCodeConstants.RETURN_CODE_LABEL;
import static com.vluee.codeflower.GeneralCodeConstants.RETURN_EXCEPTION_LABEL;
import static com.vluee.codeflower.GeneralCodeConstants.RETURN_MESSAGE_LABEL;
import static com.vluee.codeflower.GeneralCodeConstants.UNDEFINED_CODE_REPLACEMENT;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.vluee.codeflower.CodeMessageReposiroty;
import com.vluee.codeflower.GeneralCodeConstants;

/**
 * 
 * @author SeanYe
 *
 */
public class GeneralDomainExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GeneralDomainExceptionHandler.class);

	private ExceptionRepository exceptionRepository;

	private CodeMessageReposiroty codeRepository;

	private String prefix = GeneralCodeConstants.DEFAULT_PREFIX;

	public GeneralDomainExceptionHandler(CodeMessageReposiroty codeRepository,
			ExceptionRepository exceptionRepository) {
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
	public Map<String, String> handle(Exception exception) {
		if (exception == null) {
			return ImmutableMap.of();
		}
		String traceId = exceptionRepository.store(exception);
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Exception [%s]", traceId == null ? "" : traceId), exception);
		}
		Map<String, String> result = new HashMap<String, String>(2);
		// result.put(traceId, ExceptionUtils.get) 将Exception异常存储起来， 方便后期调用
		result.put(RETURN_EXCEPTION_LABEL, traceId);
		if (exception instanceof GeneralDomainException) {
			GeneralDomainException gde = (GeneralDomainException) exception;
			String code = gde.getCode();
			result.put(RETURN_CODE_LABEL, decorateWithPrefix(codeRepository.getCode(code).get().getCode()));
			result.put(RETURN_MESSAGE_LABEL, gde.getMessage());
		} else {
			result.put(RETURN_CODE_LABEL, decorateWithPrefix(UNDEFINED_CODE_REPLACEMENT));
			result.put(RETURN_MESSAGE_LABEL, exception.getMessage());
		}
		return result;
	}

	private String decorateWithPrefix(String code) {
		if (code.startsWith(this.prefix)) {
			return code;
		}
		return code + this.prefix;
	}

	public String getExceptionDetail(String traceId) {
		if (StringUtils.isBlank(traceId)) {
			return null;
		}
		return exceptionRepository.getException(traceId);
	}

}
