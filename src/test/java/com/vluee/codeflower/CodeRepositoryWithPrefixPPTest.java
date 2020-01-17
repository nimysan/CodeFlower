package com.vluee.codeflower;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for Code Repository With Prefix pp locale")
class CodeRepositoryWithPrefixPPTest {

	private static CodeMessageReposiroty repository;

	@BeforeAll
	public static void setup() {
		repository = CodeRepositoryBuilder.newBuilder().setPrefix("PP").build();
	}

	@Test
	void verifyGetCodeWithSuffix() {
		Optional<CodeMetadata> code = repository.getCode("PP0000");
		assertEquals(code.get().getCode(), "PP0000");
	}

	@Test
	void verifyGetCodeMessage() {
		String exampleCode = "0000";
		Optional<CodeMetadata> codeDescription = repository.getCode(exampleCode);
		assertTrue(codeDescription.isPresent());
		assertEquals(codeDescription.get().getCode(), "PP" + exampleCode);
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
		assertEquals("PP11111", code.get().getCode());
	}

}
