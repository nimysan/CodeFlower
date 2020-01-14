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


	public Optional<CodeMetadata> getCode(String string);

	public void setLocale(Locale locale);

	public Locale getEffectLocale();

	public Collection<CodeMetadata> listAllCodes();

	public String getLocaleMessage(CodeMetadata code, Locale givenLocale);

	public String getLocaleMessage(CodeMetadata code);
}
