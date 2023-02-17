package ru.clevertec.knyazev.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.util.Settings;

public class FileDataWriterTests {
	private Path outputFileDir;
	private final String outputFileName = "testOutput.txt";


	private FileDataWriter fileDataWriter;

	@BeforeEach
	public void setup() {
		outputFileDir = Paths.get(Settings.OUTPUT_FOLDER, outputFileName);
		fileDataWriter = new FileDataWriter(outputFileName);
	}
	
	@AfterEach
	public void postSetUp() throws IOException {
		if (Files.exists(outputFileDir)) Files.delete(outputFileDir);
	}
	
	@Test
	public void whenWriteData() throws IOException {
		String outputData = "string test data of receipt";
		
		fileDataWriter.writeData(outputData);
		
		assertEquals(outputData, Files.readString(outputFileDir));
	}
	
	@Test
	public void whenWriteEmptyData() throws IOException {
		String outputData = "";		
		
		assertThrows(IOException.class, () -> fileDataWriter.writeData(outputData));
	}
	
	@Test
	public void whenWriteNullData() throws IOException {
		String outputData = "";		
		
		assertThrows(IOException.class, () -> fileDataWriter.writeData(outputData));
	}
	
}
