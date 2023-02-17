package ru.clevertec.knyazev.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ru.clevertec.knyazev.dao.StorageDAO;
import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class StorageServiceImplTests {
	private StorageDAO storageDAOMock;

	private StorageService storageService;

	@BeforeEach
	public void setUp() {
		storageDAOMock = Mockito.mock(StorageDAO.class);
		storageService = new StorageServiceImpl(storageDAOMock);
	}

	@Test
	public void whenBuyProductFromStorages() throws ServiceException {
		BigDecimal productQuantityInStorages = new BigDecimal(17.4, MathContext.DECIMAL32);

		Product product1 = new Product(1L, "Рассыпчатая мука", false);

		List<Storage> storages = new ArrayList<>() {
			private static final long serialVersionUID = -2370284320759078343L;

			{
				add(new Storage(2L, product1, Unit.кг, new BigDecimal(2.79), new BigDecimal(4, MathContext.DECIMAL32)));
				add(new Storage(3L, product1, Unit.кг, new BigDecimal(5.69), new BigDecimal(8, MathContext.DECIMAL32)));
				add(new Storage(8L, product1, Unit.кг, new BigDecimal(2.82),
						new BigDecimal(5.4, MathContext.DECIMAL32)));
			}
		};

		Mockito.when(storageDAOMock.isStorageExistsByProductId(Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(true);
		Mockito.when(storageDAOMock.getProductQuantityInStorages(Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(productQuantityInStorages);
		Mockito.when(storageDAOMock.getProductGroupInStoragesById(Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(storages);
		Mockito.doAnswer(invocation -> {
			Long storageId = invocation.getArgument(0, Long.class);
			Iterator<Storage> storagesIterator = storages.iterator();

			while (storagesIterator.hasNext()) {
				Storage storage = storagesIterator.next();

				if (storage.getId() == storageId) {
					storagesIterator.remove();
					break;
				}
			}
			return null;
		}).when(storageDAOMock).deleteStorage(Mockito.longThat(id -> (id != null) && (id > 0L)));
		Mockito.when(storageDAOMock.updateStorage(Mockito.any(Storage.class))).thenAnswer(invocation -> {
			Storage storageForUpdate = invocation.getArgument(0, Storage.class);
			for (int i = 0; i < storages.size(); i++) {
				if (storages.get(i).getId() == storageForUpdate.getId()) {
					storages.set(i, storageForUpdate);
				}
			}

			return storageForUpdate;
		});

		final Long productId = 1L;

		List<Storage> boughtStorages = storageService.buyProductFromStorages(productId, productQuantityInStorages);
		BigDecimal realProductQuntity = boughtStorages.stream().map(storage -> storage.getQuantity())
				.reduce((a, b) -> a.add(b)).get();

		assertAll(() -> {
			assertNotNull(boughtStorages);
			assertTrue(productQuantityInStorages.compareTo(realProductQuntity) == 0);
			assertTrue(storages.isEmpty());
		});
	}

	@Test
	public void whenBuySingleProductFromStorages() throws ServiceException {

		BigDecimal productQuantityInStorages = new BigDecimal(17.4, MathContext.DECIMAL32);

		Product product1 = new Product(1L, "Рассыпчатая мука", false);

		List<Storage> storages = new ArrayList<>() {
			private static final long serialVersionUID = -2370284320759078343L;

			{
				add(new Storage(2L, product1, Unit.кг, new BigDecimal(2.79), new BigDecimal(4, MathContext.DECIMAL32)));
				add(new Storage(3L, product1, Unit.кг, new BigDecimal(5.69), new BigDecimal(8, MathContext.DECIMAL32)));
				add(new Storage(8L, product1, Unit.кг, new BigDecimal(2.82),
						new BigDecimal(5.4, MathContext.DECIMAL32)));
			}
		};

		Mockito.when(storageDAOMock.isStorageExistsByProductId(Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(true);
		Mockito.when(storageDAOMock.getProductQuantityInStorages(Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(productQuantityInStorages);
		Mockito.when(storageDAOMock.getProductGroupInStoragesById(Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(storages);
		Mockito.when(storageDAOMock.updateStorage(Mockito.any(Storage.class))).thenAnswer(invocation -> {
			Storage storageForUpdate = invocation.getArgument(0, Storage.class);
			for (int i = 0; i < storages.size(); i++) {
				if (storages.get(i).getId() == storageForUpdate.getId()) {
					storages.set(i, storageForUpdate);
				}
			}

			return storageForUpdate;
		});

		final Long productId = 1L;
		final BigDecimal productQuantity = new BigDecimal(3.8, MathContext.DECIMAL32);

		List<Storage> boughtStorages = storageService.buyProductFromStorages(productId, productQuantity);
		BigDecimal stayInStorages = storages.stream().map(storage -> storage.getQuantity())
				.reduce((a, b) -> a.add(b)).get();

		assertAll(() -> {
			assertNotNull(boughtStorages);
			assertTrue(boughtStorages.get(0).getQuantity().add(stayInStorages).compareTo(productQuantityInStorages) == 0);
		});
	}
	
	@Test
	public void whenBProductFromStoragesAndProductNotExists() {
		Mockito.when(storageDAOMock.isStorageExistsByProductId(Mockito.longThat(id -> (id != null) && (id > 0L))))
		.thenReturn(false);
		
		assertThrows(ServiceException.class, () -> storageService.buyProductFromStorages(15L, new BigDecimal(12.0)));
	}
	
	@Test
	public void whenBProductFromStoragesAndLackOfProductQuantity() {
		BigDecimal productQuantityInStorages = new BigDecimal(5);
		
		Mockito.when(storageDAOMock.isStorageExistsByProductId(Mockito.longThat(id -> (id != null) && (id > 0L))))
		.thenReturn(true);
		Mockito.when(storageDAOMock.getProductQuantityInStorages(Mockito.longThat(id -> (id != null) && (id > 0L))))
		.thenReturn(productQuantityInStorages);
		
		BigDecimal wantToBuyProducts = new BigDecimal(6);
		assertThrows(ServiceException.class, () -> storageService.buyProductFromStorages(15L, wantToBuyProducts));
	}
	
	@Test
	public void whenGetBoughtProductsTotalPrice() {
		Product product1 = new Product(1L, "Рассыпчатая мука", false);

		List<Storage> storages = new ArrayList<>() {
			private static final long serialVersionUID = -2370284320759078343L;

			{
				add(new Storage(2L, product1, Unit.кг, new BigDecimal(2.79, MathContext.DECIMAL32), new BigDecimal(4, MathContext.DECIMAL32)));
				add(new Storage(3L, product1, Unit.кг, new BigDecimal(5.69, MathContext.DECIMAL32), new BigDecimal(8, MathContext.DECIMAL32)));
				add(new Storage(8L, product1, Unit.кг, new BigDecimal(2.82, MathContext.DECIMAL32),
						new BigDecimal(5.4, MathContext.DECIMAL32)));
			}
		};
		
		Map<Long, List<Storage>> productInStorages = new HashMap<>() {
			private static final long serialVersionUID = 1L;

		{
			put(product1.getId(), storages);
		}};
		
		BigDecimal result = storageService.getBoughtProductsTotalPrice(productInStorages);
		BigDecimal expectedResult = new BigDecimal(71.908, MathContext.DECIMAL32);
		
		assertTrue(expectedResult.compareTo(result) == 0);
	}

}
