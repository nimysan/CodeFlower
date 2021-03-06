package com.vluee.codeflower.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.ConnectException;
import java.util.Map;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vluee.codeflower.CodeRepositoryBuilder;
import com.vluee.codeflower.GeneralCodeConstants;

class GeneralDomainExceptionTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralDomainExceptionTest.class);

	private static TestController controllerStub;
	private static GeneralDomainExceptionHandler handler;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		controllerStub = new TestController();
		handler = new GeneralDomainExceptionHandler(CodeRepositoryBuilder.newBuilder().build(),
				new InMemExceptionRepository());
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testHandleExceptionNull() {
		Map<String, String> handle = handler.handle(null);
		assertEquals(0, handle.size());
		
		InMemExceptionRepository inMem = new InMemExceptionRepository();
		assertNull(inMem.getException(null));
	}

	@Test
	void testHandlerNormalException() {
		try {
			controllerStub.callServiceWithException();
		} catch (Exception e) {
			Map<String, String> extract = handler.handle(e);
			assertEquals(extract.get(GeneralCodeConstants.RETURN_CODE_LABEL), "SA11111");
		}
	}

	@Test
	void testGetReturnInformationWithCode() {
		try {
			controllerStub.callServiceWithException();
		} catch (Exception e) {
			Map<String, String> extract = handler.handle(e);
			assertEquals(extract.get("returnCode"), "SA11111");
		}
	}
	

	@Test
	void testEmptyExceptionIdShouldGetNull() {
		assertNull(handler.getExceptionDetail(null));
		assertNull(handler.getExceptionDetail(" "));
		assertNull(handler.getExceptionDetail("    "));
	}

	void testExceptionDoesNotExistForHandler() {
		assertEquals(GeneralCodeConstants.NO_EXCEPTION_TIP, handler.getExceptionDetail("not_exists"));
	}

	@Test
	void testHandleGeneralDomainException() {
		try {
			controllerStub.callServiceWithGeneralDomainException();
		} catch (Exception e) {
			Map<String, String> extract = handler.handle(e);
			assertEquals(extract.get(GeneralCodeConstants.RETURN_CODE_LABEL), "SA1000");
		}
	}

	@Test
	@DisplayName("Test exception record and trace")
	void testHandleGivenGeneralDomainException() {
		String originExceptionMessage = "Failed to connect email service: 10.23.42.34.（模拟的跟真的一样）";
		try {
			GeneralDomainException sendEmailNetworkError = new GeneralDomainException("SA3000",
					new ConnectException(originExceptionMessage));
			controllerStub.callServiceWithGivenGeneralDomainException(sendEmailNetworkError);
		} catch (Exception e) {
			Map<String, String> extract = handler.handle(e);
			LOGGER.debug("Display:" + extract);
			assertEquals(extract.get("returnCode"), "SA3000");
			String exceptionId = extract.get(GeneralCodeConstants.RETURN_EXCEPTION_LABEL);
			assertNotNull(exceptionId);
			assertTrue(handler.getExceptionDetail(exceptionId).contains(originExceptionMessage));
		}
	}

	static class TestController {

		private TestService testService = new TestService();

		public void callServiceWithException() throws Exception {
			testService.serviceWithException();
		}

		public void callServiceWithGeneralDomainException() {
			testService.serviceWithDomainException();
		}

		/**
		 * 仅仅为了方便测试
		 * 
		 * @param gde
		 */
		public void callServiceWithGivenGeneralDomainException(GeneralDomainException gde) {
			throw gde;
		}

	}

	static class TestService {
		public void serviceWithException() throws UnsupportedAudioFileException {
			throw new UnsupportedAudioFileException("AudioFileException");
		}

		public void serviceWithDomainException() {
			throw new GeneralDomainException("SA1000");
		}
	}

}
