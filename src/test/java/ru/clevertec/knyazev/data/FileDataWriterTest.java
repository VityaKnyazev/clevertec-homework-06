package ru.clevertec.knyazev.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.util.Settings;

public class FileDataWriterTest {
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
	public void checkWriteDataShouldCreateFileWithData() throws IOException {
		String outputData = "string test data of receipt";
		
		fileDataWriter.writeData(outputData);
		
		assertThat(Files.readString(outputFileDir)).isEqualTo(outputData);
	}
	
	@Test
	public void checkWriteDataOnEmptyDataShouldThrowIOException() {
		String outputData = "";		
		
		assertThatThrownBy(() -> fileDataWriter.writeData(outputData)).isInstanceOf(IOException.class);
	}
	
	@Test
	public void checkWriteDataOnNullDataShouldThrowIOException() {
		String outputData = null;		
		
		assertThatThrownBy(() -> fileDataWriter.writeData(outputData)).isInstanceOf(IOException.class);
	}
	
}
