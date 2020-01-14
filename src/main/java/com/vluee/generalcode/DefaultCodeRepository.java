package com.vluee.generalcode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 
 * @author SeanYe
 *
 */
public class DefaultCodeRepository implements CodeRepository {

	private static Logger log = LoggerFactory.getLogger(DefaultCodeRepository.class);

	private Map<String, CodeMetadata> codes = new HashMap<>(24);
	MultiKeyMap<String, String> localeBasedMessages = new MultiKeyMap<>();

	private String prefix;
	private Locale effectLocale = Locale.getDefault();

	public DefaultCodeRepository() {
		log.info("Start initialize code reposiroty");
		this.prefix = CodeRepository.DEFAULT_PREFIX;
		try {
			InputStream codeInputStream = DefaultCodeRepository.class.getClassLoader()
					.getResourceAsStream("vluee_code_default.json");
			Validate.notNull(codeInputStream,
					"Code metadata initialize failed. Can't find file: vluee_code_default.json files under classpath.");
			@SuppressWarnings("unchecked")
			Map<String, String> codeList = JSON.parseObject(String.join("", IOUtils.readLines(codeInputStream, UTF8)),
					Map.class);
			Arrays.asList(Locale.getAvailableLocales()).stream().forEach(locale -> {
				try {
					String currentLocaleName = locale.toString();
					if (!StringUtils.isBlank(currentLocaleName)) {
						InputStream resourceAsStream = DefaultCodeRepository.class.getClassLoader()
								.getResourceAsStream("code_description_" + currentLocaleName + ".txt");
						if (resourceAsStream != null) {
							IOUtils.readLines(resourceAsStream, UTF8).forEach(t -> {
								String[] segments = t.split("=");
								if (segments.length == 2) {
									localeBasedMessages.put(this.prefix + segments[0], currentLocaleName, segments[1]);
								}
							});
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			for (String key : codeList.keySet()) {
				CodeMetadata cd = new CodeMetadata(key, codeList.get(key));
				decorateCode(cd);
				codes.put(cd.getCode(), cd);
				log.info(cd.toString());
			}
		} catch (IOException e) {
			throw new RuntimeException("Code Metadata initialize failed, please check and make sure things are ok.");
		}
		Validate.notNull(effectLocale, "Effect Locale can't be null. ");
	}

	public String getUnkonwErrorCode() {
		return this.prefix + UNDEFINED_CODE_REPLACEMENT;
	}

	protected void decorateCode(CodeMetadata codeDescription) {
		if (codeDescription == null) {
			return;
		}

		String code = codeDescription.getCode();
		if (code.startsWith(this.prefix)) {
			return;
		} else {
			code = this.prefix + code;
			codeDescription.setCode(code);
		}
	}

	public Optional<CodeMetadata> getCode(String code) {
		CodeMetadata value = codes.get(code);
		if (value == null) {
			value = codes.get(this.getUnkonwErrorCode());
		}
		return Optional.of(value);
	}

	public synchronized void setLocale(Locale locale) {
		Validate.notNull(locale, "Locale can't be null");
		this.effectLocale = locale;
	}

	public Locale getEffectLocale() {
		return this.effectLocale;
	}

	public Collection<CodeMetadata> listAllCodes() {
		return Collections.unmodifiableCollection(codes.values());
	}

	private String getLocaleMessage(CodeMetadata code, String localeName) {
		if (code == null) {
			return "";
		}
		String localeMessage = localeBasedMessages.get(code.getCode(), localeName);
		return StringUtils.isBlank(localeMessage) ? code.getSimpleName() : localeMessage;
	}

	@Override
	public String getLocaleMessage(CodeMetadata code, Locale givenLocale) {
		return getLocaleMessage(code, givenLocale.toString());
	}

	@Override
	public String getLocaleMessage(CodeMetadata code) {
		return getLocaleMessage(code, this.getEffectLocale().toString());
	}

}
