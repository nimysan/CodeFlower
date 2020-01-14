package com.vluee.generalcode.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.net.ConnectException;
import java.util.Map;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.vluee.generalcode.DefaultCodeRepository;
import com.vluee.generalcode.GeneralCodeConstants;

class GeneralDomainExceptionTest {

	private static TestController controllerStub;
	private static GeneralDomainExceptionHandler handler;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		controllerStub = new TestController();
		handler = new GeneralDomainExceptionHandler(new DefaultCodeRepository(), new InMemExceptionRepository());
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
	void testGetCodeMessage() {
		try {
			controllerStub.callServiceWithException();
		} catch (Exception e) {
			Map<String, String> extract = handler.extract(e);
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
			Map<String, String> extract = handler.extract(e);
			assertEquals(extract.get("returnCode"), "SA1000");
		}
	}

	@Test
	@DisplayName("Test exception record and trace")
	void testHandleGivenGeneralDomainException() {
		String originExceptionMessage = "Failed to connect email service: 10.23.42.34";
		try {
			GeneralDomainException sendEmailNetworkError = new GeneralDomainException("SA3000",
					new ConnectException(originExceptionMessage));
			controllerStub.callServiceWithGivenGeneralDomainException(sendEmailNetworkError);
		} catch (Exception e) {
			Map<String, String> extract = handler.extract(e);
			assertEquals(extract.get("returnCode"), "SA3000");
			String exceptionId = extract.get(GeneralCodeConstants.RETURN_EXCEPTION_LABEL);
			assertNotNull(exceptionId);
			assertTrue(handler.getExceptionDetail(exceptionId).contains(originExceptionMessage));
			// assertEquals("外部系统调用错误",
			// extract.get(GeneralCodeConstants.RETURN_MESSAGE_LABEL));
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
