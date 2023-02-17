package ru.clevertec.knyazev.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;
import ru.clevertec.knyazev.util.Settings;

public class StorageDAOJPATests {
	private EntityManager entityManagerMock;
	private Query queryMock;
	private StorageDAOJPA storageDAOJPA;

	@BeforeEach
	public void setUp() {
		entityManagerMock = Mockito.mock(EntityManager.class);
		queryMock = Mockito.mock(Query.class);
		storageDAOJPA = new StorageDAOJPA(entityManagerMock);
	}

	@Test
	public void whenIsStorageExistsByProductId() {
		final Number rowQuntity = 2L;

		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(rowQuntity);

		final Long productId = 3L;
		assertEquals(true, storageDAOJPA.isStorageExistsByProductId(productId));
	}

	@Test
	public void whenIsStorageNotExistsByProductId() {
		final Number rowQuntity = 0L;

		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(rowQuntity);

		final Long productId = 5L;
		assertEquals(false, storageDAOJPA.isStorageExistsByProductId(productId));
	}

	@Test
	public void whenGetProductQuantityInStorages() {
		final Number productQuntity = 5.5f;

		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(productQuntity);

		final Long productId = 8L;
		BigDecimal expectedQuantity = new BigDecimal(productQuntity.floatValue());
		expectedQuantity.setScale(Settings.QUANTITY_SCALE_VALUE, RoundingMode.HALF_UP);

		assertTrue(expectedQuantity.compareTo(storageDAOJPA.getProductQuantityInStorages(productId)) == 0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void whenGetProductGroupInStoragesById() {
		Product product = new Product(1L, "test product description", false);
		List<Storage> productsGroup = new ArrayList<>() {
			private static final long serialVersionUID = 7644547358945072008L;
			{
				add(new Storage(1L, product, Unit.кг, new BigDecimal(1.95), new BigDecimal(8)));
				add(new Storage(2L, product, Unit.кг, new BigDecimal(2.35), new BigDecimal(6.555)));
			}
		};
		
		TypedQuery<Storage> typedQueryMock = ((TypedQuery<Storage>) Mockito.mock(TypedQuery.class));
		
		Mockito.when(entityManagerMock.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.getResultList()).thenReturn(productsGroup);
		
		final Long productId = 1L;
		assertEquals(productsGroup.size(), storageDAOJPA.getProductGroupInStoragesById(productId).size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void whenGetProductGroupInStoragesByIdReturnsNull() {
		List<Storage> productsGroup = null;
		
		TypedQuery<Storage> typedQueryMock = ((TypedQuery<Storage>) Mockito.mock(TypedQuery.class));
		
		Mockito.when(entityManagerMock.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.getResultList()).thenReturn(productsGroup);
		
		final Long productId = 1L;
		assertEquals(0, storageDAOJPA.getProductGroupInStoragesById(productId).size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void whenGetProductGroupInStoragesByIdReturnsEmpty() {
		List<Storage> productsGroup = new ArrayList<>();
		
		TypedQuery<Storage> typedQueryMock = ((TypedQuery<Storage>) Mockito.mock(TypedQuery.class));
		
		Mockito.when(entityManagerMock.createQuery(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.getResultList()).thenReturn(productsGroup);
		
		final Long productId = 1L;
		assertEquals(0, storageDAOJPA.getProductGroupInStoragesById(productId).size());
	}
	
	@Test
	public void whenUpdateStorage() {
		Product product = new Product(1L, "test product description", false);
		Storage storage = (new Storage(1L, product, Unit.кг, new BigDecimal(1.95), new BigDecimal(8)));	
		
		Mockito.doNothing().when(entityManagerMock).persist(storage);
		
		assertEquals(storage, storageDAOJPA.updateStorage(storage));
	}
	
	@Test
	public void whenUpdateStorageOnNullId() {
		Product product = new Product(1L, "test product description", false);
		Storage storage = (new Storage(null, product, Unit.кг, new BigDecimal(1.95), new BigDecimal(8)));	
		
		assertNull(storageDAOJPA.updateStorage(storage));
	}
	
	@Test
	public void whenDeleteStorage() {
		final Long storageId = 5L;
		final int executedQuantity = 1;
		
		Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
		.thenReturn(queryMock);
		Mockito.when(queryMock.executeUpdate()).thenReturn(executedQuantity);
		
		assertDoesNotThrow(() -> storageDAOJPA.deleteStorage(storageId));
	}

}