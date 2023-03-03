package ru.clevertec.knyazev.data.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

		assertThat(discountCardsDTO).hasSize(3);
	}

	// TODO fix card- input after here -> 23 String cardNumber =
	// cardsData[i].substring(CARD_PREFIX.length())
	@ParameterizedTest
	@MethodSource("getIncorrectCardDataForConverting")
	public void checkConvertOnIncorrectCardDataShouldThrowConverterException(String data) throws ConverterException {
		String[] cardsData = data.split(" ");

		assertThatThrownBy(() -> cardsConverter.convert(cardsData)).isInstanceOf(ConverterException.class);
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void checkConvertOnOnNullDataShouldReturnEmptyDiscountCardsDTOSet(String[] cardsData)
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
		return new String[] { "card-123456789 card-123456798 card-123456897 card-123456798",
				"card-256845256 card-125426946 card-125426946 card-142345398" };
	}

	private static final String[] getIncorrectCardDataForConverting() {
		return new String[] { "card-123456789 card-123456798 123456897",
				// "card- card-123456798 card-568971234",
				"card-122555855 123456798card- card-568971234", "card-122555855 card-123456798 1234card-56897",
				"car-122555855 card-123456798 card-568971234", "card-122555855 car-d123456798 card-568971234",
				"122card-555855 card-123456798 card-568971234", "1-54" };
	}

	private static final String[] getEmptyAndSpaceCardDataForConverting() {
		return new String[] { "", " " };
	}
}