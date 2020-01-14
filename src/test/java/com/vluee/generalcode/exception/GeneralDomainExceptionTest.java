package com.vluee.generalcode.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneralDomainExceptionTest {

	private static TestController controllerStub;
	private static GeneralDomainExceptionHandler handler;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		controllerStub = new TestController();
		handler = new GeneralDomainExceptionHandler();
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
	void testHandleGeneralDomainException() {
		try {
			controllerStub.callServiceWithGeneralDomainException();
		} catch (Exception e) {
			Map<String, String> extract = handler.extract(e);
			assertEquals(extract.get("returnCode"), "SA1000");
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
