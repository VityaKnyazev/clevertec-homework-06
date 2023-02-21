package ru.clevertec.knyazev.data.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.dto.ProductDTO;

public class PurchaseConverterTest {
	private Converter<Map<ProductDTO, BigDecimal>, String[]> purchaseConverter;

	@BeforeEach
	public void setUp() {
		purchaseConverter = new PurchaseConverter();
	}

	@ParameterizedTest
	@MethodSource("getCorrectProductDataForConverting")
	public void checkConvertShouldReturnProductDTOMap(String productData) throws ConverterException {
		String[] productsData = productData.split(" ");
		Map<ProductDTO, BigDecimal> products = purchaseConverter.convert(productsData);

		assertThat(products).hasSize(3);
	}

	@ParameterizedTest
	@MethodSource("getIncorrectProductDataForConverting")
	public void checkConvertShouldThrowConverterException(String productData) {
		String[] productsData = productData.split(" ");

		assertThatThrownBy(() -> purchaseConverter.convert(productsData)).isInstanceOf(ConverterException.class);
	}

	@ParameterizedTest
	@NullAndEmptySource
	public void checkConvertOnNullAndEmptyProductsDataShouldReturnEmptyProductDTOMap(String[] productsData)
			throws ConverterException {
		assertThat(purchaseConverter.convert(productsData)).isEmpty();
	}

	@Test
	public void checkConvertWhenProductsDataContainsEqualIdsShouldGroupAndReturnProductDTOMap()
			throws ConverterException {
		String[] productsData = { "1-8", "2-5", "1-5.115", "4-2.558", "2-11" };

		BigDecimal expectedProductWithId1Quantity = new BigDecimal(13.115).setScale(3, RoundingMode.HALF_UP);
		BigDecimal expectedProductWithId2Quantity = new BigDecimal(16).setScale(3, RoundingMode.HALF_UP);

		Map<ProductDTO, BigDecimal> products = purchaseConverter.convert(productsData);

		BigDecimal actualProductWithId1Quantity = null;

		BigDecimal actualproductWithId2Quantity = null;

		final Long productId1 = 1L;
		final Long productId2 = 2L;

		for (Map.Entry<ProductDTO, BigDecimal> productQuantity : products.entrySet()) {
			if (productQuantity.getKey().getId() == productId1) {
				actualProductWithId1Quantity = productQuantity.getValue().setScale(3, RoundingMode.HALF_UP);
			}

			if (productQuantity.getKey().getId() == productId2) {
				actualproductWithId2Quantity = productQuantity.getValue().setScale(3, RoundingMode.HALF_UP);
			}
		}

		assertThat(actualProductWithId1Quantity).isEqualByComparingTo(expectedProductWithId1Quantity);
		assertThat(actualproductWithId2Quantity).isEqualByComparingTo(expectedProductWithId2Quantity);
	}

	private static final String[] getCorrectProductDataForConverting() {
		return new String[] { "1-8 3-5 4-2.558", "1-10.25 4-10.1 8-5.001 8-10.121", "2-0.128 3-0.01 4-0.001",
				"2-0.128 3-0.01 4-0.001 3-0.258" };
	}

	private static final String[] getIncorrectProductDataForConverting() {
		return new String[] { "1214", " 12121 1121 ", " 1225-12445 g-5", "2-0.1g28 k3-0.01 4-d0.001 3.0.25d8",
				"2-12122 12 255 dfd   ", "3- 3-5 4-2.558", "3-f256 3-5 4-2.558", "fd35-8 3-5 4-2.558",
				"-8 3-5 4-2.558" };
	}

}
