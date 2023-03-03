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
		String inputSource = "standard";
		String outputSource = "file";

		args = new String[] { inputSource, outputSource };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatThrownBy(() -> dataFactory.getDataReaderWriter()).isInstanceOf(IOException.class);
		});
	}

	@Test
	public void checkGetDataReaderWriterAndFileReadConsoleWriteShouldDoesntThrowAnyException() {
		String inputSource = "file";
		String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1.txt", "2.txt" };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatCode(() -> dataFactory.getDataReaderWriter()).doesNotThrowAnyException();
		});
	}

	@Test
	public void checkGetDataReaderWriterAndFileReadConsoleWriteShouldThrowIOException() {
		String inputSource = "file";
		String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1.txt" };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatThrownBy(() -> dataFactory.getDataReaderWriter()).isInstanceOf(IOException.class);
		});
	}

	@Test
	public void checkGetDataReaderWriterOnBadArgsShouldThrowIOException() {
		String inputSource = "file";

		args = new String[] { inputSource };

		assertThatThrownBy(() -> dataFactory = new DataFactory(args)).isInstanceOf(IOException.class);
	}

	@Test
	public void checkGetDataReaderWriterOnBadInputSourceShouldThrowIOException() {
		String inputSource = "filter";
		String outputSource = "console";

		args = new String[] { inputSource, outputSource, "1-15.55" };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatThrownBy(() -> dataFactory.getDataReaderWriter()).isInstanceOf(IOException.class);
		});
	}

	@Test
	public void checkGetDataReaderWriterOnBadOutputSourceShouldThrowIOException() {
		String inputSource = "console";
		String outputSource = "vasya";

		args = new String[] { inputSource, outputSource, "1-15.55" };

		assertAll(() -> {
			assertThatCode(() -> dataFactory = new DataFactory(args)).doesNotThrowAnyException();
			assertThatThrownBy(() -> dataFactory.getDataReaderWriter()).isInstanceOf(IOException.class);
		});
	}
}
