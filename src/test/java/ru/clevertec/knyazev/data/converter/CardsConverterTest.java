package ru.clevertec.knyazev.data.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;

public class CardsConverterTest {
	private Converter<Set<DiscountCardDTO>, String[]> cardsConverter;

	@BeforeEach
	public void setUp() {
		cardsConverter = new CardsConverter();
	}

	@ParameterizedTest
	@MethodSource("getCorrectCardDataForConverting")
	public void checkConvertOnCorrectCardDataShouldReturnDiscountCardsDTOSet(String data) throws ConverterException {
		String[] cardsData = data.split(" ");

		Set<DiscountCardDTO> discountCardsDTO = cardsConverter.convert(cardsData);

		assertThat(discountCardsDTO).hasSize(4);
	}

	
	@ParameterizedTest
	@MethodSource("getIncorrectCardDataForConverting")
	public void checkConvertOnIncorrectCardDataShouldReturnEmptyDiscountCardsDTOSet(String data) throws ConverterException {
		String[] cardsData = data.split(" ");

		assertThat(cardsConverter.convert(cardsData)).isEmpty();
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void checkConvertOnNullDataShouldReturnEmptyDiscountCardsDTOSet(String[] cardsData)
			throws ConverterException {
		assertThat(cardsConverter.convert(cardsData)).isEmpty();
	}

	@ParameterizedTest
	@MethodSource("getEmptyAndSpaceCardDataForConverting")
	public void checkConvertOnSpaceStringDataShouldReturnEmptyDiscountCardsDTOSet(String cardData)
			throws ConverterException {
		String[] cardsData = { cardData };

		assertThat(cardsConverter.convert(cardsData)).isEmpty();
	}

	private static final String[] getCorrectCardDataForConverting() {
		return new String[] { "card-123456789 card-123456798 card-123456897 card-123456798 card-123456799",
				"card-256845256 card-125426946 card-125426946 card-142345398 card-142345399",
				"card- card-f card-1 card-dsfs"};
	}

	private static final String[] getIncorrectCardDataForConverting() {
		return new String[] { "cord-123456798 123456897",
				 "cadr-123456798 cad-fsf455, cord-dsdsd55",
				"123456798card- cadr568971234", "cald-122555855 1234card-56897",
				"car-122555855 car-123456798 car-568971234", "car-122555855 car-d123456798 caad568971234",
				"122card-555855 crd-123456798 ard-568971234", "1-54" };
	}

	private static final String[] getEmptyAndSpaceCardDataForConverting() {
		return new String[] { "", " ", "           ", " \r\n    \r\n  " };
	}
}