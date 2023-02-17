package ru.clevertec.knyazev.data;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class DataFactoryTests {
	private DataFactory dataFactory;
	private String[] args;

	@Test
	public void whenGetDataReaderWriterAndStandardReadConsoleWrite() {
		final String inputSource = "standard";
		final String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1-5" };

		assertAll(() -> {
			assertDoesNotThrow(() -> dataFactory = new DataFactory(args));
			assertDoesNotThrow(() -> dataFactory.getDataReaderWriter());
		});
	}
	
	@Test
	public void whenGetDataReaderWriterAndStandardReadFileWrite() {
		final String inputSource = "standard";
		final String outputSource = "file";

		args = new String[] { inputSource, outputSource, "1-5" };

		assertAll(() -> {
			assertDoesNotThrow(() -> dataFactory = new DataFactory(args));
			assertDoesNotThrow(() -> dataFactory.getDataReaderWriter());
		});
	}
	
	@Test
	public void whenGetDataReaderWriterAndStandardReadBadArguments() {
		final String inputSource = "standard";
		final String outputSource = "file";

		args = new String[] { inputSource, outputSource };

		assertAll(() -> {
			assertDoesNotThrow(() -> dataFactory = new DataFactory(args));
			assertThrows(IOException.class, () -> dataFactory.getDataReaderWriter());
		});
	}

	@Test
	public void whenGetDataReaderWriterAndConsoleReadConsoleWrite() {
		final String inputSource = "console";
		final String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1-5" };

		assertAll(() -> {
			assertDoesNotThrow(() -> dataFactory = new DataFactory(args));
			assertDoesNotThrow(() -> dataFactory.getDataReaderWriter());
		});
	}

	@Test
	public void whenGetDataReaderWriterAndFileReadConsoleWrite() {
		final String inputSource = "file";
		final String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1.txt", "2.txt" };

		assertAll(() -> {
			assertDoesNotThrow(() -> dataFactory = new DataFactory(args));
			assertDoesNotThrow(() -> dataFactory.getDataReaderWriter());
		});
	}
	
	@Test
	public void whenGetDataReaderWriterAndFileReadExceptionConsoleWrite() {
		final String inputSource = "file";
		final String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1.txt" };

		assertAll(() -> {
			assertDoesNotThrow(() -> dataFactory = new DataFactory(args));
			assertThrows(IOException.class, () -> dataFactory.getDataReaderWriter());
		});
	}
	
	@Test
	public void whenGetDataReaderWriterAndBadArgs() {
		final String inputSource = "file";

		args = new String[] { inputSource };

		assertAll(() -> {
			assertThrows(IOException.class, () -> dataFactory = new DataFactory(args));
		});
	}
	
	@Test
	public void whenGetDataReaderWriterAndBadInput() {
		final String inputSource = "filter";
		final String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1-15.55" };

		assertAll(() -> {
			assertDoesNotThrow(() -> dataFactory = new DataFactory(args));
			assertThrows(IOException.class, () -> dataFactory.getDataReaderWriter());
		});
	}
	
	@Test
	public void whenGetDataReaderWriterAndBadOutput() {
		final String inputSource = "console";
		final String outputSource = "vasya";

		args = new String[] { inputSource, outputSource, "1-15.55" };

		assertAll(() -> {
			assertDoesNotThrow(() -> dataFactory = new DataFactory(args));
			assertThrows(IOException.class, () -> dataFactory.getDataReaderWriter());
		});
	}	
}
