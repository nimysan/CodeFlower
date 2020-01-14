package com.vluee.generalcode.exception;

import static com.vluee.generalcode.GeneralCodeConstants.RETURN_CODE_LABEL;

import java.util.HashMap;
import java.util.Map;

import com.vluee.generalcode.GeneralCodeConstants;

public class GeneralDomainExceptionHandler {

	public Map<String, String> extract(Exception exception) {
		Map<String, String> result = new HashMap<String, String>(2);
		if (exception instanceof GeneralDomainException) {
			GeneralDomainException gde = (GeneralDomainException) exception;
			String code = gde.getCode();
			result.put(RETURN_CODE_LABEL, code);
			result.put(GeneralCodeConstants.RETURN_MESSAGE_LABEL, gde.getMessage());
		} else {
			result.put(RETURN_CODE_LABEL,
					GeneralCodeConstants.DEFAULT_PREFIX + GeneralCodeConstants.UNDEFINED_CODE_REPLACEMENT);
			result.put(GeneralCodeConstants.RETURN_MESSAGE_LABEL, exception.getMessage());
		}
		return result;
	}

}
