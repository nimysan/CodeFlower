package com.vluee.generalcode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("返回码仓库默认行为测试")
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
		Optional<CodeMetadata> code = repository.getCode("NOT_EXIST");
		assertEquals("未定义的错误码", repository.getLocaleMessage(code.get(), "zh_CN"));
	}

	@Test
	void verifyCodeMessage_enUS() {
		Optional<CodeMetadata> code = repository.getCode("NOT_EXIST");
		assertEquals("Undefined Code", repository.getLocaleMessage(code.get(), "en_US"));
	}

}
