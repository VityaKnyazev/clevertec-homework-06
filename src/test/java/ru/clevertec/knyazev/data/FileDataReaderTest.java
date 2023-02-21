package ru.clevertec.knyazev.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.util.Settings;

public class FileDataReaderTest {
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
	public void checkReadDataShouldReturnMapWithProductAndCardData() throws IOException {
		String expectedInputPurchaseData = "1-3 4-5 8-12.3 1-15.6";
		String expectedInputCardsData = "card-156954 card-126554187";
		
		Files.writeString(purchasefileDir, expectedInputPurchaseData);
		Files.writeString(cardsfileDir, expectedInputCardsData);
		
		Map<String[], String[]> data = fileDataReader.readData();
		
		assertAll(() -> {
			assertThat(data).isNotNull();
			assertThat(data).hasSize(1);
		});
		
		assertThat(data.keySet().iterator().next()).isEqualTo(expectedInputPurchaseData.split(" "));
		assertThat(data.values().iterator().next()).isEqualTo(expectedInputCardsData.split(" "));
	}
	
	@Test
	public void checkReadDataNotOnTwoFilesShouldThrowIOException() {
		assertThatThrownBy(() -> new FileDataReader(new String[] {"first", "second", "third"})).isInstanceOf(IOException.class);
	}
	
	@Test
	public void checkReadDataOnEmptyFilesShouldThrowIOException() {
		assertThatThrownBy(() -> new FileDataReader(new String[] {})).isInstanceOf(IOException.class);
	}
	
	@Test
	public void checkReadDataOnEmptyPurchaseDataShouldThrowIOException() throws IOException {
		String inputPurchaseData = "";
		String inputCardsData = "card-156954 card-126554187";
		
		Files.writeString(purchasefileDir, inputPurchaseData);
		Files.writeString(cardsfileDir, inputCardsData);
		
		assertThatThrownBy(() -> fileDataReader.readData()).isInstanceOf(IOException.class);
	}
	
	@Test
	public void checkReadDataOnSpacePurchaseDataShouldThrowIOException() throws IOException {
		String inputPurchaseData = " ";
		String inputCardsData = "card-156954 card-126554187";
		
		Files.writeString(purchasefileDir, inputPurchaseData);
		Files.writeString(cardsfileDir, inputCardsData);
		
		assertThatThrownBy(() -> fileDataReader.readData()).isInstanceOf(IOException.class);
	}
	
}
