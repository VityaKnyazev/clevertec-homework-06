package ru.clevertec.knyazev.data.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.dto.ProductDTO;

public class PurchaseConverterTests {
	private Converter<Map<ProductDTO, BigDecimal>, String[]> purchaseConverter;
	
	@BeforeEach
	public void setUp() {
		purchaseConverter = new PurchaseConverter();
	}
	
	@Test
	public void whenConvert() throws ConverterException {
		String[] productsData = {"1-8", "3-5", "4-2.558"};
		
		Map<ProductDTO, BigDecimal> products = purchaseConverter.convert(productsData);
		
		assertAll(() -> {
			assertTrue(!products.isEmpty());
			assertTrue(products.size() == 3);
		});
	}
	
	@Test
	public void whenConvertOnNullData() throws ConverterException {
		String[] productsData = null;
		
		assertTrue(purchaseConverter.convert(productsData).isEmpty());
	}
	
	@Test
	public void whenConvertOnEmptyData() throws ConverterException {
		String[] productsData = {};
		
		assertTrue(purchaseConverter.convert(productsData).isEmpty());
	}
	
	@Test
	public void whenConvertOnBadId() {
		String[] productsData = {"-8", "3-5", "4-2.558"};
		
		assertThrows(ConverterException.class, () -> purchaseConverter.convert(productsData));
	}
	
	@Test
	public void whenConvertOnBadIdText() {
		String[] productsData = {"fd35-8", "3-5", "4-2.558"};
		
		assertThrows(ConverterException.class, () -> purchaseConverter.convert(productsData));
	}
	
	@Test
	public void whenConvertOnBadQuantity() {
		String[] productsData = {"3-", "3-5", "4-2.558"};
		
		assertThrows(ConverterException.class, () -> purchaseConverter.convert(productsData));
	}
	
	@Test
	public void whenConvertOnBadQuantityText() {
		String[] productsData = {"3-f256", "3-5", "4-2.558"};
		
		assertThrows(ConverterException.class, () -> purchaseConverter.convert(productsData));
	}
	
	@Test
	public void whenConvertAndProductDataContainsEqualIds() throws ConverterException {
		String[] productsData = {"1-8", "2-5", "1-5.115", "4-2.558", "2-11"};
		
		Map<ProductDTO, BigDecimal> products = purchaseConverter.convert(productsData);
		
		BigDecimal productWithId1Quantity = null;
		
		BigDecimal productWithId2Quantity = null;
		
		final Long productId1 = 1L;
		final Long productId2 = 2L;
		
		for (Map.Entry<ProductDTO, BigDecimal> productQuantity : products.entrySet()) {
			if (productQuantity.getKey().getId() == productId1) {
				productWithId1Quantity = productQuantity.getValue();
				productWithId1Quantity = productWithId1Quantity.setScale(3, RoundingMode.HALF_UP);
			}
			
			if (productQuantity.getKey().getId() == productId2) {
				productWithId2Quantity = productQuantity.getValue();
				productWithId2Quantity = productWithId2Quantity.setScale(3, RoundingMode.HALF_UP);
			}
		}
		
		BigDecimal productWithId1QuantityExpected = new BigDecimal(13.115);
		productWithId1QuantityExpected = productWithId1QuantityExpected.setScale(3, RoundingMode.HALF_UP);
		
		BigDecimal productWithId2QuantityExpected = new BigDecimal(16);
		productWithId2QuantityExpected = productWithId2QuantityExpected.setScale(3, RoundingMode.HALF_UP);
		
		assertAll(() -> {
			assertNotNull(products);
			assertTrue(products.size() == 3);
		});
		
		assertTrue(productWithId1QuantityExpected.compareTo(productWithId1Quantity) == 0);
		assertTrue(productWithId2QuantityExpected.compareTo(productWithId2Quantity) == 0);		
	}
	
}
