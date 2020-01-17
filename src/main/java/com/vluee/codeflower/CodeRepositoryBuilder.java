package com.vluee.codeflower;

import java.util.Locale;

import org.apache.commons.lang3.Validate;

/**
 * @author SeanYe
 *
 */
public class CodeRepositoryBuilder {

	private Locale locale = Locale.getDefault();
	private String prefix = GeneralCodeConstants.DEFAULT_PREFIX;

	private CodeRepositoryBuilder() {

	}

	public static final CodeRepositoryBuilder newBuilder() {
		return new CodeRepositoryBuilder();
	}

	public CodeRepositoryBuilder setLocale(Locale locale) {
		Validate.notNull(locale, "Locale can't not be null");
		this.locale = locale;
		return this;
	}

	public CodeRepositoryBuilder setPrefix(String prefix) {
		Validate.notBlank(prefix, "Prefix can't be null");
		Validate.isTrue(prefix.length() <= 4, "Code Prefix max size should be 4");
		this.prefix = prefix;
		return this;
	}

	public CodeMessageReposiroty build() {
		DefaultCodeRepository codeRepository = new DefaultCodeRepository(this.locale, this.prefix);
		return codeRepository;
	}

}
