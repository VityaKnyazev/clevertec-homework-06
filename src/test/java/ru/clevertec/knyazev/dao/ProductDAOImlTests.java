package ru.clevertec.knyazev.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.entity.Product;

public class ProductDAOImlTests {
	private ProductDAO productDAOImpl;
	
	@BeforeEach
	public void setUp() {
		productDAOImpl = new ProductDAOImpl();
	}
	
	@Test
	public void whenGetProductById() {
		Long productId = 2L;
		Product product = productDAOImpl.getProductById(productId);
		
		assertAll(() -> {
			assertNotNull(product);
			assertEquals(productId, product.getId());
		});
	}
	
	@Test
	public void whenGetProductByNullId() {
		Long productId = null;		
		Product product = productDAOImpl.getProductById(productId);
		
		assertNull(product);
	}
	
	@Test
	public void whenGetProductByBadId() {
		Long productId = 0L;		
		Product product = productDAOImpl.getProductById(productId);
		
		assertNull(product);
	}
	
	@Test
	public void whenSaveProduct() {
		Long productId = null;	
		Product product = new Product(productId, "Елка праздничная НОВОГОДНЯЯ", true);
		Product savedProduct = productDAOImpl.saveProduct(product);
		
		assertNotNull(savedProduct.getId());
	}
}