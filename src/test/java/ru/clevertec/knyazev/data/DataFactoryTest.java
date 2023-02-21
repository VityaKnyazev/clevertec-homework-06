package ru.clevertec.knyazev.data;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DataFactoryTest {
	private DataFactory dataFactory;
	private String[] args;

	@ParameterizedTest
	@CsvSource(value = { "standard:console", "standard:file", "console:console" }, delimiter = ':')
	public void checkGetDataReaderWriterWithVariousInputOutputShouldDoesntThrowAnyException(String inputSource,
			String outputSource) {

		args = new String[] { inputSource, outputSource, "1-5" };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatCode(() -> dataFactory.getDataReaderWriter()).doesNotThrowAnyException();
		});

	}

	@Test
	public void checkGetDataReaderWriterAndStandardReadBadArgumentsShouldThrowIOException() {
		final String inputSource = "standard";
		final String outputSource = "file";

		args = new String[] { inputSource, outputSource };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatThrownBy(() -> dataFactory.getDataReaderWriter()).isInstanceOf(IOException.class);
		});
	}

	@Test
	public void checkGetDataReaderWriterAndFileReadConsoleWriteShouldDoesntThrowAnyException() {
		final String inputSource = "file";
		final String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1.txt", "2.txt" };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatCode(() -> dataFactory.getDataReaderWriter()).doesNotThrowAnyException();
		});
	}

	@Test
	public void checkGetDataReaderWriterAndFileReadConsoleWriteShouldThrowIOException() {
		final String inputSource = "file";
		final String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1.txt" };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatThrownBy(() -> dataFactory.getDataReaderWriter()).isInstanceOf(IOException.class);
		});
	}

	@Test
	public void checkGetDataReaderWriterOnBadArgsShouldThrowIOException() {
		final String inputSource = "file";

		args = new String[] { inputSource };

		assertThatThrownBy(() -> dataFactory = new DataFactory(args)).isInstanceOf(IOException.class);
	}

	@Test
	public void checkGetDataReaderWriterOnBadInputSourceShouldThrowIOException() {
		final String inputSource = "filter";
		final String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1-15.55" };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatThrownBy(() -> dataFactory.getDataReaderWriter()).isInstanceOf(IOException.class);
		});
	}

	@Test
	public void checkGetDataReaderWriterOnBadOutputSourceShouldThrowIOException() {
		final String inputSource = "console";
		final String outputSource = "vasya";

		args = new String[] { inputSource, outputSource, "1-15.55" };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatThrownBy(() -> dataFactory.getDataReaderWriter()).isInstanceOf(IOException.class);
		});
	}
}
