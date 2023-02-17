package ru.clevertec.knyazev.data;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.util.Settings;

public class FileDataReaderTests {
	private Path purchasefileDir;
	private Path cardsfileDir;
	private String purchasefileName = "testPurchase.txt";
	private String cardsFileName = "testCards.txt";

	private FileDataReader fileDataReader;

	@BeforeEach
	public void setup() throws IOException {
		purchasefileDir = Paths.get(Settings.INPUT_FOLDER, purchasefileName);
		cardsfileDir = Paths.get(Settings.INPUT_FOLDER, cardsFileName);

		String[] fileNames = { purchasefileName, cardsFileName };
		fileDataReader = new FileDataReader(fileNames);
	}
	
	@AfterEach
	public void postSetUp() throws IOException {
		if (Files.exists(purchasefileDir)) Files.delete(purchasefileDir);
		if (Files.exists(cardsfileDir)) Files.delete(cardsfileDir);
	}

	@Test
	public void whenReadData() throws IOException {
		String inputPurchaseData = "1-3 4-5 8-12.3 1-15.6";
		String inputCardsData = "card-156954 card-126554187";
		
		Files.writeString(purchasefileDir, inputPurchaseData);
		Files.writeString(cardsfileDir, inputCardsData);
		
		Map<String[], String[]> data = fileDataReader.readData();
		
		assertAll(() -> {
			assertNotNull(data);
			assertEquals(1, data.size());
		});
		
		assertTrue(Arrays.equals(inputPurchaseData.split(" "), data.keySet().iterator().next()));
		assertTrue(Arrays.equals(inputCardsData.split(" "), data.values().iterator().next()));
	}
	
	@Test
	public void whenReadDataNotOnTwoFiles() {
		assertThrows(IOException.class, () -> new FileDataReader(new String[] {"first", "second", "third"}));
	}
	
	@Test
	public void whenReadDataOnEmptyFiles() {
		assertThrows(IOException.class, () -> new FileDataReader(new String[] {}));
	}
	
	@Test
	public void whenReadDataOnEmptyPurchaseData() throws IOException {
		String inputPurchaseData = "";
		String inputCardsData = "card-156954 card-126554187";
		
		Files.writeString(purchasefileDir, inputPurchaseData);
		Files.writeString(cardsfileDir, inputCardsData);
		
		assertThrows(IOException.class, () -> fileDataReader.readData());
	}
	
	@Test
	public void whenReadDataOnSpacePurchaseData() throws IOException {
		String inputPurchaseData = " ";
		String inputCardsData = "card-156954 card-126554187";
		
		Files.writeString(purchasefileDir, inputPurchaseData);
		Files.writeString(cardsfileDir, inputCardsData);
		
		assertThrows(IOException.class, () -> fileDataReader.readData());
	}
	
}
