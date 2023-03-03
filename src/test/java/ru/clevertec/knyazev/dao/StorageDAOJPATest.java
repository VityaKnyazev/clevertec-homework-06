package ru.clevertec.knyazev.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;
import ru.clevertec.knyazev.util.Settings;

@ExtendWith(MockitoExtension.class)
public class StorageDAOJPATest {
	@Mock
	private EntityManager entityManagerMock;
	@Mock
	private Query queryMock;
	@InjectMocks
	private StorageDAOJPA storageDAOJPA;

	@Test
	public void checkIsStorageExistsByProductIdShouldReturnTrue() {
		Long inputProductId = 3L;
		Number expectedRowQuntity = 2L;

		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(expectedRowQuntity);

		boolean actualResult = storageDAOJPA.isStorageExistsByProductId(inputProductId);

		assertThat(actualResult).isTrue();
	}

	@Test
	public void checkIsStorageExistsByProductIdShouldReturnFalse() {
		Long inputProductId = 5L;
		Number expectedRowQuntity = 0L;

		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(expectedRowQuntity);

		boolean actualResult = storageDAOJPA.isStorageExistsByProductId(inputProductId);

		assertThat(actualResult).isFalse();
	}

	@Test
	public void checkGetProductQuantityInStoragesShouldReturnProductQuantity() {
		Long inputProductId = 8L;
		Number expectedProductQuntity = 5.584f;
		BigDecimal expectedProductQuntityResult = new BigDecimal(expectedProductQuntity.floatValue())
				.setScale(Settings.QUANTITY_SCALE_VALUE, RoundingMode.HALF_UP);

		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(expectedProductQuntity);

		BigDecimal actualProductQuantity = storageDAOJPA.getProductQuantityInStorages(inputProductId);

		assertThat(actualProductQuantity).isEqualByComparingTo(expectedProductQuntityResult);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void checkGetProductGroupInStoragesByIdShouldReturnStorageList() {
		long inputProductId = 1L;

		Product expectedProduct = Product.builder()
				.id(inputProductId)
				.description("test product description")
				.isAuction(false)
				.build();
		List<Storage> expectedProductsGroup = new ArrayList<>() {
			private static final long serialVersionUID = 7644547358945072008L;
			{
				add(Storage.builder()
						.id(1L)
						.product(expectedProduct)
						.unit(Unit.kg)
						.price(new BigDecimal(1.95))
						.quantity(new BigDecimal(8))
						.build());
				add(Storage.builder()
						.id(2L)
						.product(expectedProduct)
						.unit(Unit.kg)
						.price(new BigDecimal(2.35))
						.quantity(new BigDecimal(6.555))
						.build());
			}
		};

		TypedQuery<Storage> typedQueryMock = ((TypedQuery<Storage>) Mockito.mock(TypedQuery.class));

		Mockito.when(entityManagerMock.createQuery(Mockito.anyString(), Mockito.any(Class.class)))
				.thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.getResultList()).thenReturn(expectedProductsGroup);

		List<Storage> actualProductGroup = storageDAOJPA.getProductGroupInStoragesById(inputProductId);

		assertAll(() -> {
			assertThat(actualProductGroup).isNotEmpty();
			assertThat(actualProductGroup).size().isEqualTo(expectedProductsGroup.size());
		});
	}

	@SuppressWarnings("unchecked")
	@Test
	public void checkGetProductGroupInStoragesByIdShouldReturnEmptyStorageList() {
		Long inputProductId = 62L;
		List<Storage> expectedProductGroup = null;

		TypedQuery<Storage> typedQueryMock = ((TypedQuery<Storage>) Mockito.mock(TypedQuery.class));

		Mockito.when(entityManagerMock.createQuery(Mockito.anyString(), Mockito.any(Class.class)))
				.thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.getResultList()).thenReturn(expectedProductGroup);

		List<Storage> actualProductGroup = storageDAOJPA.getProductGroupInStoragesById(inputProductId);

		assertThat(actualProductGroup).isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void checkGetProductGroupInStoragesByIdShouldReturnEmptyStorageList2() {
		final Long inputProductId = 62L;
		List<Storage> expectedProductGroup = new ArrayList<>();

		TypedQuery<Storage> typedQueryMock = ((TypedQuery<Storage>) Mockito.mock(TypedQuery.class));

		Mockito.when(entityManagerMock.createQuery(Mockito.anyString(), Mockito.any(Class.class)))
				.thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(typedQueryMock);
		Mockito.when(typedQueryMock.getResultList()).thenReturn(expectedProductGroup);

		List<Storage> actualProductGroup = storageDAOJPA.getProductGroupInStoragesById(inputProductId);

		assertThat(actualProductGroup).isEmpty();
	}

	@Test
	public void checkUpdateStorageShouldReturnStorage() {
		Product product = Product.builder()
				.id(1L)
				.description("test product description")
				.isAuction(false)
				.build();
		Storage updatingStorage = Storage.builder()
				.id(1L)
				.product(product)
				.unit(Unit.kg)
				.price(new BigDecimal(1.95))
				.quantity(new BigDecimal(8))
				.build();

		Mockito.doNothing().when(entityManagerMock).persist(Mockito.any(Storage.class));

		Storage actualStorage = storageDAOJPA.updateStorage(updatingStorage);

		ArgumentCaptor<Storage> storageCaptor = ArgumentCaptor.forClass(Storage.class);
		Mockito.verify(entityManagerMock).persist(storageCaptor.capture());

		assertThat(actualStorage).isEqualTo(storageCaptor.getValue());
	}

	@Test
	public void checkUpdateStorageOnNullIdShouldReturnNull() {
		Product product = Product.builder()
				.id(1L)
				.description("test product description")
				.isAuction(false)
				.build();
		
		Long updatingStorageId = null;
		Storage updatingStorage = Storage.builder()
				.id(updatingStorageId)
				.product(product)
				.unit(Unit.kg)
				.price(new BigDecimal(1.95))
				.quantity(new BigDecimal(8))
				.build();

		Storage actualStorage = storageDAOJPA.updateStorage(updatingStorage);

		assertThat(actualStorage).isNull();
	}

	@Test
	public void whenDeleteStorage() {
		Long deletingStorageId = 5L;
		int executedQuantity = 1;

		Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(queryMock);
		Mockito.when(queryMock.executeUpdate()).thenReturn(executedQuantity);

		storageDAOJPA.deleteStorage(deletingStorageId);

		Mockito.verify(queryMock).executeUpdate();
	}

}