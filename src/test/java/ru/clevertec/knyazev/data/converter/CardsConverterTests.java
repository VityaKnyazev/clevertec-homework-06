package ru.clevertec.knyazev.data.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;

public class CardsConverterTests {
	private Converter<Set<DiscountCardDTO>, String[]> cardsConverter;
	
	@BeforeEach
	public void setUp() {
		cardsConverter = new CardsConverter();
	}
	
	@Test
	public void whenConvert() throws ConverterException {
		String[] cardsData = {"card-123456789", "card-123456798", "card-123456897"};
		
		Set<DiscountCardDTO> discountCardsDTO = cardsConverter.convert(cardsData);
		
		assertAll(() -> {
			assertTrue(!discountCardsDTO.isEmpty());
			assertTrue(discountCardsDTO.size() == 3);
		});
	}
	
	@Test
	public void whenConvertAndCardsDataDoesntContainPrefix() throws ConverterException {
		String[] cardsData = {"card-123456789", "card-123456798", "123456897"};
		
		assertThrows(ConverterException.class, () -> cardsConverter.convert(cardsData));
	}
	
	@Test
	public void whenConvertAndCardsDataDoesntStartWithPrefix() throws ConverterException {
		String[] cardsData = {"card-", "123456798card-", "1234card-56897"};
		
		assertThrows(ConverterException.class, () -> cardsConverter.convert(cardsData));
	}
	
	@Test
	public void whenConvertOnNullData() throws ConverterException {
		String[] cardsData = null;
		
		assertTrue(cardsConverter.convert(cardsData).isEmpty());
	}
	
	@Test
	public void whenConvertOnEmptyData() throws ConverterException {
		String[] cardsData = {};
		
		assertTrue(cardsConverter.convert(cardsData).isEmpty());
	}
	
	@Test
	public void whenConvertOnEmptyStringData() throws ConverterException {
		String[] cardsData = {""};
		
		assertTrue(cardsConverter.convert(cardsData).isEmpty());
	}
	
	@Test
	public void whenConvertOnSpaceStringData() throws ConverterException {
		String[] cardsData = {" "};
		
		assertTrue(cardsConverter.convert(cardsData).isEmpty());
	}
	
	@Test
	public void whenConvertOnBadData() throws ConverterException {
		String[] cardsData = {"1-54"};
		
		assertThrows(ConverterException.class, () -> cardsConverter.convert(cardsData));
	}
	
}