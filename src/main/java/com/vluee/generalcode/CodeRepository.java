package com.vluee.generalcode;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * 
 * @author SeanYe
 *
 */
public interface CodeRepository {

	public static final String DEFAULT_PREFIX = "SA";
	public static final String UNDEFINED_CODE_REPLACEMENT = "11111";

	public static final Charset UTF8 = Charset.forName("utf8");

	public Optional<CodeMetadata> getCode(String string);

	public void setLocale(Locale locale);

	public Locale getEffectLocale();

	public Collection<CodeMetadata> listAllCodes();

	public String getLocaleMessage(CodeMetadata code, Locale givenLocale);

	public String getLocaleMessage(CodeMetadata code);
}
