package ru.clevertec.knyazev.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import ru.clevertec.knyazev.util.Settings;

public class FileDataWriter implements DataWriter {
	private String filename;

	public FileDataWriter(String filename) {
		this.filename = filename;
	}

	@Override
	public void writeData(String data) throws IOException {
		String file = Settings.OUTPUT_FOLDER + filename;
		
		if (data == null || data.isEmpty()) {
			throw new IOException("Error on writing null or empty data");
		}
		
		File dataFile = new File(file);
		if (!dataFile.exists()) {
			dataFile.createNewFile();
		}
		
		try (FileWriter fileWriter = new FileWriter(file, Charset.forName("utf-8"))) {
			fileWriter.write(data);
		}
	}
}
