package com.vluee.codeflower.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

public class ReponseBehaiorTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReponseBehaiorTest.class);

	private static GeneralRestResponseBuilder builder = null;

	@BeforeAll
	public static void setup() {
		builder = new GeneralRestResponseBuilder();
	}

	@Test
	@DisplayName("case-success-no_entity")
	void testOkResponseWithoutEntity() {
		GeneralRestResponse<Void> rest = builder.ok();
		assertThat(rest.getCode()).isEqualTo(builder.getSuccessCode());
		assertThat(rest.getExceptionId()).isNull();
		assertThat(rest.getMessage()).isNull();
	}

	@Test
	@DisplayName("case-success-simple_entity")
	void testOkResponseWithEntity() {
		GeneralRestResponse<String> rest = builder.okWithEntity("t");
		assertThat(rest.getCode()).isEqualTo(builder.getSuccessCode());
		assertThat(rest.getExceptionId()).isNull();
		assertThat(rest.getMessage()).isNull();
		assertThat(rest.getBody()).isEqualTo("t");
	}

	@Test
	@DisplayName("case-success-complex_entity")
	void testOkResponseWithComplexEntity() {
		GeneralRestResponse<Map<String, String>> rest = builder.okWithEntity(ImmutableMap.of("a", "b"));
		LOGGER.info(JSON.toJSONString(rest));
		assertThat(rest.getCode()).isEqualTo(builder.getSuccessCode());
		assertThat(rest.getExceptionId()).isNull();
		assertThat(rest.getMessage()).isNull();
		assertThat(rest.getBody().get("a")).isEqualTo("b");
	}

	@Test
	@DisplayName("case-fail")
	void testFailCase() {
		GeneralRestResponse<Void> rest = builder.codeResponse("11111");
		LOGGER.info(JSON.toJSONString(rest));
		assertThat(rest.getCode()).isEqualTo("11111");
		assertThat(rest.getExceptionId()).isNull();
		assertThat(rest.getMessage()).isNotBlank();
	}

	@Test
	@DisplayName("case-fail-empty-code")
	void testFailCaseWithEmptyCode() {
		GeneralRestResponse<Void> rest = builder.codeResponse("");
		LOGGER.info(JSON.toJSONString(rest));
		assertThat(rest.getCode()).isEqualTo(builder.getUnknownErrorCode());
		assertThat(rest.getExceptionId()).isNull();
		assertThat(rest.getMessage()).isNotBlank();
	}

	@Test
	@DisplayName("case-normal-with-exception")
	void testFailWithException() {
		int b = 0;
		int a = 20;
		try {
			if (a / b > 0) {
			}
		} catch (Exception e) {
			builder.setAppendExceptionStackTrace(true);
			GeneralRestResponse<Void> rest = builder.failResponse(e);
			LOGGER.info(JSON.toJSONString(rest, true));
			assertThat(rest.getExceptionId()).isNotNull();
			builder.setAppendExceptionStackTrace(false);
		}
	}

	@Test
	void testFailOutputWithExceptionDetail() {
		int b = 0;
		int a = 20;
		try {
			if (a / b > 0) {
			}
		} catch (Exception e) {
			builder.setAppendExceptionStackTrace(true);
			GeneralRestResponse<Void> rest = builder.codeResponse("1234", e);
			LOGGER.info(JSON.toJSONString(rest, true));
			assertThat(rest.getExceptionId()).isNotNull();
			builder.setAppendExceptionStackTrace(false);
		}
	}

	@Test
	@DisplayName("case-fail-with-exception")
	void testFailCaseWithException() {
		GeneralRestResponse<Void> rest = builder.codeResponse("11111",
				new IllegalArgumentException("hey, test exception"));
		LOGGER.info(JSON.toJSONString(rest));
		assertThat(rest.getCode()).isEqualTo("11111");
		assertThat(rest.getExceptionId()).isNotBlank();
		assertThat(rest.getMessage()).isNotBlank();
	}
}
