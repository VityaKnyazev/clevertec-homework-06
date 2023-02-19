package ru.clevertec.knyazev.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;

public class StorageDAOImplTest {
	private StorageDAOImpl storageDAOImpl;

	@BeforeEach
	public void setUp() {
		storageDAOImpl = new StorageDAOImpl(new ProductDAOImpl());
	}

	@Test
	public void checkIsStorageExistsByProductIdShouldReturnTrue() {
		final Long inputProductId = 2L;

		boolean actualResult = storageDAOImpl.isStorageExistsByProductId(inputProductId);

		assertThat(actualResult).isTrue();
	}

	@Test
	public void checkIsStorageExistsByProductIdOnNullProductIdShouldReturnFalse() {
		final Long inputProductId = null;

		boolean actualResult = storageDAOImpl.isStorageExistsByProductId(inputProductId);

		assertThat(actualResult).isFalse();
	}

	@Test
	public void checkIsStorageExistsByProductIdOnBadProductIdShouldReturnFalse() {
		final Long inputProductId = 0L;

		boolean actualResult = storageDAOImpl.isStorageExistsByProductId(inputProductId);

		assertThat(actualResult).isFalse();
	}

	@Test
	public void checkGetProductQuantityInStoragesShouldReturnProductQuantity() {
		final Long inputProductId = 2L;

		BigDecimal expectedProductQuantity = new BigDecimal(3);
		expectedProductQuantity = expectedProductQuantity.setScale(3, RoundingMode.HALF_UP);

		BigDecimal actualProductQuantity = storageDAOImpl.getProductQuantityInStorages(inputProductId);
		assertThat(actualProductQuantity).isEqualByComparingTo(expectedProductQuantity);
	}

	@Test
	public void checkGetProductQuantityInStoragesOnNullIdShouldReturnZero() {
		final Long inputProductId = null;

		BigDecimal expectedProductQuantity = new BigDecimal("0");
		expectedProductQuantity = expectedProductQuantity.setScale(3, RoundingMode.HALF_UP);

		BigDecimal actualProductQuantity = storageDAOImpl.getProductQuantityInStorages(inputProductId);

		assertThat(actualProductQuantity).isEqualByComparingTo(expectedProductQuantity);
	}

	@Test
	public void checkGetProductQuantityInStoragesOnBadIdShoulReturnZero() {
		final Long inputProductId = 0L;

		BigDecimal expectedProductQuantity = new BigDecimal("0");
		expectedProductQuantity = expectedProductQuantity.setScale(3, RoundingMode.HALF_UP);

		BigDecimal actualProductQuantity = storageDAOImpl.getProductQuantityInStorages(inputProductId);

		assertThat(actualProductQuantity).isEqualByComparingTo(expectedProductQuantity);
	}

	@Test
	public void checkGetProductGroupInStoragesByIdShouldReturnStorageList() {
		final Long inputProductId = 2L;
		final int expectedGroupSize = 2;

		List<Storage> actualGroup = storageDAOImpl.getProductGroupInStoragesById(inputProductId);

		assertAll(() -> {
			assertThat(actualGroup).isNotNull();
			assertThat(actualGroup).size().isEqualTo(expectedGroupSize);
		});
	}

	@Test
	public void checkGetProductGroupInStoragesByIdOnNullIdShouldReturnEmptyList() {
		final Long inputProductId = null;

		List<Storage> actualGroup = storageDAOImpl.getProductGroupInStoragesById(inputProductId);

		assertThat(actualGroup).isEmpty();
	}

	@Test
	public void checkGetProductGroupInStoragesByIdOnBadIdShouldReturnEmptyList() {
		final Long inputProductId = 0L;

		List<Storage> actualGroup = storageDAOImpl.getProductGroupInStoragesById(inputProductId);

		assertThat(actualGroup).isEmpty();
	}

	@Test
	public void checkUpdateStorageShouldReturnUpdatedStorage() {
		Long inputId = 4L;
		Storage updatingStorage = Storage.builder().id(inputId)
				.product(Product.builder().id(13L).description("Test product").isAuction(false).build()).unit(Unit.кг)
				.price(new BigDecimal(6.44)).quantity(new BigDecimal(2.596)).build();

		Storage updatedStorage = storageDAOImpl.updateStorage(updatingStorage);

		assertThat(updatedStorage).isEqualTo(updatingStorage);
	}

	@Test
	public void checkUpdateStorageOnNullStorageIdShouldReturnNull() {
		Long inputId = null;
		Storage updatingStorage = Storage.builder().id(inputId)
				.product(Product.builder().id(13L).description("Test product").isAuction(false).build()).unit(Unit.кг)
				.price(new BigDecimal(6.44)).quantity(new BigDecimal(2.596)).build();

		Storage updatedStorage = storageDAOImpl.updateStorage(updatingStorage);

		assertThat(updatedStorage).isNull();
	}

	@Test
	public void checkDeleteStorage() {
		Long id = 4L;
		int storagesSizeBeforeDeleting = storageDAOImpl.getAll().size();

		storageDAOImpl.deleteStorage(id);

		List<Storage> storagesAfterDeleting = storageDAOImpl.getAll();
		
		assertAll(() -> {
			assertThat(storagesAfterDeleting).size().isLessThan(storagesSizeBeforeDeleting);
			assertThat(storagesAfterDeleting.stream().map(storage -> storage.getId()).noneMatch(sId -> sId == id)).isTrue();
		});
	}
}