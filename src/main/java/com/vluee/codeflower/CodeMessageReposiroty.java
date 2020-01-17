package com.vluee.codeflower;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * 
 * @author SeanYe
 *
 */
public interface CodeMessageReposiroty {


	public Optional<CodeMetadata> getCode(String string);

	public void setLocale(Locale locale);

	public Locale getEffectLocale();

	public Collection<CodeMetadata> listAllCodes();

	public String getLocaleMessage(CodeMetadata code, Locale givenLocale);

	public String getLocaleMessage(CodeMetadata code);
}
