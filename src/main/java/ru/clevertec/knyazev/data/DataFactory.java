package ru.clevertec.knyazev.data;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import ru.clevertec.knyazev.data.connection.DBConnectionImpl;
import ru.clevertec.knyazev.util.Settings;

public class DataFactory {
	private String[] args;

	public DataFactory(String[] args) throws IOException {
		if (args.length < 2) {
			throw new IOException(
					"Invalid arguments quantiy. Should be at least two parameters: input source and output source. Example: java -jar database console");
		}

		this.args = args;
	}

	public DataReaderWriterDecorator getDataReaderWriter() throws IOException {
		DataReader dataReader = getDataReader();
		DataWriter dataWriter = getDataWriter();

		DataReaderWriterDecorator dataReaderWriter = new ConverterValidatorDataReaderWriter(dataReader, dataWriter);

		return dataReaderWriter;
	}

	private DataReader getDataReader() throws IOException {
		InputSource inputSource = null;
		
		try {
			inputSource = InputSource.valueOf(args[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			inputSource = InputSource.UNDEFINED;
		}

		DataReader dataReader = switch (inputSource) {
		case CONSOLE -> new ConsoleDataReader();
		case DATABASE -> new DatabaseDataReader(DBConnectionImpl.getDBConnectionInstance());
		case STANDARD -> {
			if (args.length < 3) {
				throw new IOException(
						"Invalid arguments quantiy. For this case (standard) should be at least three parameters. Example: java -jar standard console 1-5");
			}
			String[] inArgs = Arrays.copyOfRange(args, 2, args.length);
			yield new StandardDataReader(inArgs);
		}
		case FILE -> {
			if (args.length < 4) {
				throw new IOException(
						"Invalid arguments quantiy. For this case (file) should be at least four parameters. Example: java -jar file console 1.txt 2.txt");
			}
			String[] inArgs = Arrays.copyOfRange(args, 2, args.length);
			yield new FileDataReader(inArgs);
		}
		default -> throw new IOException("Invalid input source value: " + args[0]);
		};

		return dataReader;
	}

	private DataWriter getDataWriter() throws IOException {
		final String outputFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss dd-MM-YYYY"))
				+ Settings.OUTPUT_FILE_NAME;

		OutputSource outputSource = null;
		
		try {
			outputSource = OutputSource.valueOf(args[1].toUpperCase());
		} catch (IllegalArgumentException e) {
			outputSource = OutputSource.UNDEFINED;
		}

		DataWriter dataWriter = switch (outputSource) {
		case CONSOLE -> new ConsoleDataWriter();
		case FILE -> new FileDataWriter(outputFileName);
		default -> throw new IOException("Invalid output source value: " + args[1]);
		};
		return dataWriter;
	}

	public static enum InputSource {
		STANDARD, CONSOLE, FILE, DATABASE, UNDEFINED
	}

	public static enum OutputSource {
		CONSOLE, FILE, UNDEFINED
	}

}
