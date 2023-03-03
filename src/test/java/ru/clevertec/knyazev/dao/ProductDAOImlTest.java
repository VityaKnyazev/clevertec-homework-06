package ru.clevertec.knyazev.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.knyazev.entity.Product;

@ExtendWith(MockitoExtension.class)
public class ProductDAOImlTest {
	private ProductDAO productDAOImpl;

	@BeforeEach
	public void setUp() {
		productDAOImpl = new ProductDAOImpl();
	}

	@Test
	public void checkGetProductByIdShouldReturnProduct() {
		Long inputProductId = 2L;
		Product actualProduct = productDAOImpl.getProductById(inputProductId);

		Long expectedProductId = 2L;

		assertAll(() -> {
			assertThat(actualProduct).isNotNull();
			assertThat(actualProduct.getId()).isSameAs(expectedProductId);
		});
	}

	@Test
	public void checkGetProductByIdOnNullIdShouldReturnNull() {
		Long inputProductId = null;
		Product actualProduct = productDAOImpl.getProductById(inputProductId);

		assertThat(actualProduct).isNull();
	}

	@Test
	public void checkGetProductByIdOnBadIdShouldReturnNull() {
		Long inputProductId = 0L;
		Product actualProduct = productDAOImpl.getProductById(inputProductId);

		assertThat(actualProduct).isNull();
	}

	@Test
	public void checkSaveProductShouldReturnSavedProduct() {
		Long savingProductId = null;
		Product savingProduct = new Product(savingProductId, "Елка праздничная НОВОГОДНЯЯ", true);

		Product actualProduct = productDAOImpl.saveProduct(savingProduct);

		assertAll(() -> {
			assertThat(actualProduct).isNotNull();
			assertThat(actualProduct.getId()).isGreaterThan(0L);
		});
	}
}