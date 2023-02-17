package ru.clevertec.knyazev.data;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class StandardDataReaderTests {
	private StandardDataReader standardDataReader;

	@Test
	public void whenReadData() throws IOException {
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
			assertTrue(!gottenData.isEmpty());
			assertTrue(gottenData.size() == 1);
		});
		
		assertTrue(Arrays.equals(expectedData.keySet().iterator().next(), gottenData.keySet().iterator().next()));
		assertTrue(Arrays.equals(expectedData.values().iterator().next(), gottenData.values().iterator().next()));
	}
	
	@Test
	public void whenReadDataOnNull() {
		String[] args = null;

		standardDataReader = new StandardDataReader(args);
		
		assertThrows(IOException.class, () -> standardDataReader.readData());
	}
	
	@Test
	public void whenReadDataOnEmpty() {
		String[] args = {};

		standardDataReader = new StandardDataReader(args);
		
		assertThrows(IOException.class, () -> standardDataReader.readData());
	}

}
