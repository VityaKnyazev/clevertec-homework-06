package ru.clevertec.knyazev.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import ru.clevertec.knyazev.entity.Product;

@ExtendWith(MockitoExtension.class)
public class ProductDAOJPATest {
	@Mock
	private EntityManager entityManagerMock;
	@InjectMocks
	private ProductDAOJPA productDAOJPA;

	@Test
	public void checkFindByIdShouldReturnProduct() {
		Product expectedProduct = Product.builder()
				.id(1L)
				.description("test product description")
				.isAuction(true)
				.build();

		Mockito.when(entityManagerMock.find(Mockito.any(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(expectedProduct);

		Long inputId = 1L;
		Product actualProduct = productDAOJPA.getProductById(inputId);

		assertThat(actualProduct).isEqualTo(expectedProduct);
	}

	@Test
	public void checkFindByIdOnNullIdShouldReturnNull() {
		Long inputId = null;
		assertThat(productDAOJPA.getProductById(inputId)).isNull();
	}

	@Test
	public void checkFindByIdOnBadIdShouldReturnNull() {
		Long inputId = 0L;
		assertThat(productDAOJPA.getProductById(inputId)).isNull();
	}

	@Test
	public void checkSaveProductShouldReturnSavedProduct() {
		Mockito.doNothing().when(entityManagerMock).persist(Mockito.any(Product.class));

		Product savingProduct = Product.builder().description("test product description").isAuction(true).build();
		
		Product actualProduct = productDAOJPA.saveProduct(savingProduct);	
		
		Mockito.verify(entityManagerMock).persist(Mockito.any(Product.class));
		assertThat(actualProduct).isNotNull();
	}

	@Test
	public void checkSaveProductOnNotNullIdShouldReturnNull() {
		long savingId = 1L;
		Product savingProduct = Product.builder().id(savingId).description("test product description").isAuction(true).build();
		
		Product actualProduct = productDAOJPA.saveProduct(savingProduct);	
		
		Mockito.verify(entityManagerMock, never()).persist(Mockito.any(Product.class));
		assertThat(actualProduct).isNull();
	}
}