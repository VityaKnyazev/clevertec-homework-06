package ru.clevertec.knyazev.data;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;

public class ConverterValidatorDataReaderWriterTests {
	private ConverterValidatorDataReaderWriter converterValidatorDataReaderWriter;
	
	@BeforeEach
	public void setUp() {
		String purchaseData = "1-8 2-5 1-1.99 3-6";
		String cardsData = "card-256895489 card-235";
		DataReader dataReader = new StandardDataReader(new String[]{purchaseData, cardsData});
		DataWriter dataWriter = new ConsoleDataWriter();
		converterValidatorDataReaderWriter = new ConverterValidatorDataReaderWriter(dataReader, dataWriter);
	}
		
	@Test
	public void whenReadPurchases() throws IOException, ConverterException, ValidatorException {			
		Map<ProductDTO, BigDecimal> actualPurchases = converterValidatorDataReaderWriter.readPurchases();
	
		assertAll(() -> {
			assertNotNull(actualPurchases);
			assertEquals(3, actualPurchases.size());
		});
		
	}
	
	@Test
	public void whenReadDiscountCards() throws IOException, ConverterException, ValidatorException {			
		 Set<DiscountCardDTO> actualDiscountCards = converterValidatorDataReaderWriter.readDiscountCards();
	
		assertAll(() -> {
			assertNotNull(actualDiscountCards);
			assertEquals(1, actualDiscountCards.size());
		});
		
	}
}
