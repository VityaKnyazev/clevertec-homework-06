package ru.clevertec.knyazev.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class StandardDataReaderTest {
	private StandardDataReader standardDataReader;

	@Test
	public void checkReadDataShouldReturnDataMap() throws IOException {
		String[] args = { "5-8", "4-7", "9-6", "card-256987452", "card-256254698", "6-12.58" };

		standardDataReader = new StandardDataReader(args);

		Map<String[], String[]> gottenData = standardDataReader.readData();

		Map<String[], String[]> expectedData = new HashMap<>() {
			private static final long serialVersionUID = 591029272646909566L;
			String[] purchases = { "5-8", "4-7", "9-6", "6-12.58" };
			String[] cards = { "card-256987452", "card-256254698" };
			{
				put(purchases, cards);
			}
		};

		assertAll(() -> {
			assertThat(gottenData).isNotEmpty();
			assertThat(gottenData).hasSize(1);
		});

		assertThat(Arrays.equals(expectedData.keySet().iterator().next(), gottenData.keySet().iterator().next()))
				.isTrue();
		assertThat(Arrays.equals(expectedData.values().iterator().next(), gottenData.values().iterator().next()))
				.isTrue();
	}

	@Test
	public void checkReadDataOnNullArgsShouldThrowIOException() {
		String[] args = null;

		standardDataReader = new StandardDataReader(args);

		assertThatThrownBy(() -> standardDataReader.readData()).isInstanceOf(IOException.class);
	}

	@Test
	public void checkReadDataOnEmptyArgsShouldThrowIOException() {
		String[] args = {};

		standardDataReader = new StandardDataReader(args);

		assertThatThrownBy(() -> standardDataReader.readData()).isInstanceOf(IOException.class);
	}

}
