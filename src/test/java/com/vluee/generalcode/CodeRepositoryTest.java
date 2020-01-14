package com.vluee.generalcode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for Code Repository default behaivor")
class CodeRepositoryTest {

	private CodeRepository repository = new DefaultCodeRepository();

	@Test
	void verifyGetCodeWithSuffix() {
		Optional<CodeMetadata> code = repository.getCode("SA0000");
		assertEquals(code.get().getCode(), "SA0000");
	}

	@Test
	void verifyGetCodeMessage() {
		String exampleCode = "SA0000";
		Optional<CodeMetadata> codeDescription = repository.getCode(exampleCode);
		assertTrue(codeDescription.isPresent());
		assertEquals(codeDescription.get().getCode(), exampleCode);
	}

	@Test
	void verifyGetAllCodes() {
		Collection<CodeMetadata> allCodes = repository.listAllCodes();
		assertNotNull(allCodes);
		assertTrue(allCodes.size() > 0);
	}

	@Test
	void verifyGetNotExistCode() {
		Optional<CodeMetadata> code = repository.getCode("NOT_EXIST");
		assertTrue(code.isPresent());
		assertEquals("SA11111", code.get().getCode());
	}

	@Test
	void verifyCodeMessageZHCN() {
		Locale locale = Locale.ENGLISH;
		Optional<CodeMetadata> code = repository.getCode("NOT_EXIST");
		assertEquals("未定义的错误码", repository.getLocaleMessage(code.get(), locale));
	}

	@Test
	void verifyCodeMessage_enUS() {
		Optional<CodeMetadata> code = repository.getCode("NOT_EXIST");
		assertEquals("Undefined Code", repository.getLocaleMessage(code.get(), Locale.ENGLISH));
	}

	@Test
	void verifyCodeWithoutMessageSetup_enUS() {
		Optional<CodeMetadata> code = repository.getCode("SA1000");
		assertEquals(code.get().getSimpleName(), repository.getLocaleMessage(code.get(), Locale.ENGLISH));
	}

}
