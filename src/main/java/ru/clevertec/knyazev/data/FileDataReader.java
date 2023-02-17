package ru.clevertec.knyazev.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ru.clevertec.knyazev.util.Settings;

public class FileDataReader implements DataReader {
	private String[] fileNames = new String[2];

	public FileDataReader(String[] fileNames) throws IOException {
		if (fileNames.length != 2)
			throw new IOException("Error. Must be two files!");
		this.fileNames = fileNames;
	}

	@Override
	public Map<String[], String[]> readData() throws IOException {
		String purchasesFileName = Settings.INPUT_FOLDER + fileNames[0];
		String cardsFileName = Settings.INPUT_FOLDER + fileNames[1];

		String[] purchasesData = read(purchasesFileName);
		String[] cardsData = read(cardsFileName);

		return new HashMap<>() {
			private static final long serialVersionUID = -1473373359284415922L;
			{
				put(purchasesData, cardsData);
			}
		};
	}

	private final String[] read(String file) throws FileNotFoundException, IOException {
		String data = "";

		try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8); Scanner scanner = new Scanner(fileReader)) {
			while (scanner.hasNextLine()) {
				data += scanner.nextLine();
			}
		}

		if (!(file.substring(Settings.INPUT_FOLDER.length()).equals(fileNames[1]))) {
			if (data == null || data.length() == 0 || " ".equals(data)) {
				throw new IOException("Given purchase file=" + file + " contains an empty data!");
			}
		}
		data = data.replaceAll(System.lineSeparator(), " ").replaceAll("\\s+", " ");
		return data.split(" ");
	}

}
