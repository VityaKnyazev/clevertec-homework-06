package ru.clevertec.knyazev.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.persistence.EntityManager;
import ru.clevertec.knyazev.entity.Product;

public class ProductDAOJPATests {
	private EntityManager entityManagerMock;
	private ProductDAOJPA productDAOJPA;
	
	@BeforeEach
	public void setUp() {
		entityManagerMock = Mockito.mock(EntityManager.class);
		productDAOJPA = new ProductDAOJPA(entityManagerMock);
	}
	
	@Test
	public void whenFindById() {
		final Product product = new Product(1L, "test product description", true);
		
		Mockito.when(entityManagerMock.find(Mockito.any(), Mockito.longThat(id -> (id != null) && (id > 0L)))).thenReturn(product);
		
		final Long id = 1L;		
		Product savedProduct = productDAOJPA.getProductById(id);
		
		assertNotNull(savedProduct);
	}
	
	@Test
	public void whenFindByNullId() {		
		final Long id = null;		
		assertNull(productDAOJPA.getProductById(id));
	}
	
	@Test
	public void whenFindByBadId() {		
		final Long id = 0L;		
		assertNull(productDAOJPA.getProductById(id));
	}
	
	@Test
	public void whenSaveProduct() {
		final Product product = new Product(null, "test product description", true);
		
		Mockito.doNothing().when(entityManagerMock).persist(Mockito.any(Product.class));
		
		assertNotNull(productDAOJPA.saveProduct(product));
	}
	
	@Test
	public void whenSaveProductOnNotNullId() {
		final long productId = 1L;
		final Product product = new Product(productId, "test product description", true);
		
		Mockito.doNothing().when(entityManagerMock).persist(Mockito.any(Product.class));
		
		assertNull(productDAOJPA.saveProduct(product));
	}
}