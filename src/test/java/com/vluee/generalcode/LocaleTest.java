package com.vluee.generalcode;

import java.util.Locale;

import org.junit.jupiter.api.Test;

public class LocaleTest {

	@Test
	void testDefaultLocale() {
		Locale default1 = Locale.getDefault();
		System.out.println(default1);

		Locale english = Locale.ENGLISH;
		System.out.println(english);
	}
}
