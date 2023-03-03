package ru.clevertec.knyazev.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

public class ConverterValidatorDataReaderWriterTest {
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
	public void checkReadPurchasesShouldReturnPurchasesMap() throws IOException, ConverterException, ValidatorException {			
		Map<ProductDTO, BigDecimal> actualPurchases = converterValidatorDataReaderWriter.readPurchases();
	
		assertAll(() -> {
			assertThat(actualPurchases).isNotNull();
			assertThat(actualPurchases).hasSize(3);
		});		
	}
	
	@Test
	public void checkReadDiscountCardsShouldReturnDiscountCardsMap() throws IOException, ConverterException, ValidatorException {			
		 Set<DiscountCardDTO> actualDiscountCards = converterValidatorDataReaderWriter.readDiscountCards();
	
		assertAll(() -> {
			assertThat(actualDiscountCards).isNotNull();
			assertThat(actualDiscountCards).hasSize(1);
		});
		
	}
}
