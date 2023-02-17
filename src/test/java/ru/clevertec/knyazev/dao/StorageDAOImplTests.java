package ru.clevertec.knyazev.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;

public class StorageDAOImplTests {
	private StorageDAOImpl storageDAOImpl;
	
	@BeforeEach
	public void setUp() {
		storageDAOImpl = new StorageDAOImpl(new ProductDAOImpl());
	}
	
	@Test
	public void whenIsStorageExistsByProductId() {
		final Long productId = 2L;
		
		storageDAOImpl.isStorageExistsByProductId(productId);
		
		assertTrue(storageDAOImpl.isStorageExistsByProductId(productId));
	}
	
	@Test
	public void whenIsStorageExistsByNullProductId() {
		final Long productId = null;
		
		storageDAOImpl.isStorageExistsByProductId(productId);
		
		assertFalse(storageDAOImpl.isStorageExistsByProductId(productId));
	}
	
	@Test
	public void whenIsStorageExistsByBadProductId() {
		final Long productId = 0L;
		
		storageDAOImpl.isStorageExistsByProductId(productId);
		
		assertFalse(storageDAOImpl.isStorageExistsByProductId(productId));
	}
	
	@Test
	public void whenGetProductQuantityInStorages() {
		final Long productId = 2L;
		
		BigDecimal expectedProductQuantity = new BigDecimal(3);
		expectedProductQuantity = expectedProductQuantity.setScale(3, RoundingMode.HALF_UP);
		
		assertTrue(expectedProductQuantity.compareTo(storageDAOImpl.getProductQuantityInStorages(productId)) == 0);
	}
	
	@Test
	public void whenGetProductQuantityInStoragesByNullId() {
		final Long productId = null;
		
		BigDecimal expectedProductQuantity = new BigDecimal("0");
		expectedProductQuantity = expectedProductQuantity.setScale(3, RoundingMode.HALF_UP);
		
		assertTrue(expectedProductQuantity.compareTo(storageDAOImpl.getProductQuantityInStorages(productId)) == 0);
	}
	
	@Test
	public void whenGetProductQuantityInStoragesByBadId() {
		final Long productId = 0L;
		
		BigDecimal expectedProductQuantity = new BigDecimal("0");
		expectedProductQuantity = expectedProductQuantity.setScale(3, RoundingMode.HALF_UP);
		
		assertTrue(expectedProductQuantity.compareTo(storageDAOImpl.getProductQuantityInStorages(productId)) == 0);
	}
	
	@Test
	public void whenGetProductGroupInStoragesById() {
		final Long productId = 2L;
		
		List<Storage> actualGroup = storageDAOImpl.getProductGroupInStoragesById(productId);
		
		assertAll(() -> {
			assertNotNull(actualGroup);
			assertEquals(2, actualGroup.size());
		});
	}
	
	@Test
	public void whenGetProductGroupInStoragesByNullId() {
		final Long productId = null;
		
		List<Storage> actualGroup = storageDAOImpl.getProductGroupInStoragesById(productId);
		
		assertTrue(actualGroup.isEmpty());
	}
	
	@Test
	public void whenGetProductGroupInStoragesByBadId() {
		final Long productId = 0L;
		
		List<Storage> actualGroup = storageDAOImpl.getProductGroupInStoragesById(productId);
		
		assertTrue(actualGroup.isEmpty());
	}
	
	@Test
	public void whenUpdateStorage() {
		Long id = 4L;
		Storage storage = new Storage(id, new Product(13L, "Test product", false), Unit.кг, new BigDecimal(6.44), new BigDecimal(2.59));
		
		Storage updatedStorage = storageDAOImpl.updateStorage(storage);
		
		assertEquals(id, updatedStorage.getId());
	}
	
	@Test
	public void whenUpdateStorageOnNullStorageId() {
		Long id = null;
		Storage storage = new Storage(id, new Product(13L, "Test product", false), Unit.кг, new BigDecimal(6.44), new BigDecimal(2.59));
		
		Storage updatedStorage = storageDAOImpl.updateStorage(storage);
		
		assertNull(updatedStorage);
	}
	
	@Test
	public void whenDeleteStorage() {
		Long id = 4L;
		int storagesSizeBeforeDeleting = storageDAOImpl.getAll().size();

		storageDAOImpl.deleteStorage(id);
		
		List<Storage> storagesAfterDeleting = storageDAOImpl.getAll();
		
		assertAll(() -> {
			assertTrue(storagesSizeBeforeDeleting > storagesAfterDeleting.size());
			assertTrue(storagesAfterDeleting.stream().map(storage -> storage.getId()).noneMatch(sId -> sId == id));
		});
	}
}