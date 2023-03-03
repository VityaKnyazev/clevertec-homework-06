package ru.clevertec.knyazev.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.knyazev.dao.StorageDAO;
import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;
import ru.clevertec.knyazev.service.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
public class StorageServiceImplTest {
	@Mock
	private StorageDAO storageDAOMock;
	@InjectMocks
	private StorageServiceImpl storageService;

	@DisplayName("checkBuyProductFromStorages")
	@Nested
	class BuyProductFromStorages {
		@Test
		public void checkBuyProductFromStoragesShouldReturnBoughtStoragesList() throws ServiceException {
			BigDecimal productQuantityInStorages = new BigDecimal(17.4, MathContext.DECIMAL32);

			Product product1 = Product.builder()
					.id(1L)
					.description("Рассыпчатая мука")
					.isAuction(false)
					.build();

			List<Storage> storages = new ArrayList<>() {
				private static final long serialVersionUID = -2370284320759078343L;

				{
					add(Storage.builder()
							.id(2L)
							.product(product1)
							.unit(Unit.kg)
							.price(new BigDecimal(2.79, MathContext.DECIMAL32))
							.quantity(new BigDecimal(4, MathContext.DECIMAL32))
							.build());
					add(Storage.builder()
							.id(3L)
							.product(product1)
							.unit(Unit.kg)
							.price(new BigDecimal(5.69, MathContext.DECIMAL32))
							.quantity(new BigDecimal(8, MathContext.DECIMAL32))
							.build());
					add(Storage.builder()
							.id(8L)
							.product(product1)
							.unit(Unit.kg)
							.price(new BigDecimal(2.82, MathContext.DECIMAL32))
							.quantity(new BigDecimal(5.4, MathContext.DECIMAL32))
							.build());
				}
			};

			Mockito.when(storageDAOMock.isStorageExistsByProductId(Mockito.longThat(id -> (id != null) && (id > 0L))))
					.thenReturn(true);
			Mockito.when(storageDAOMock.getProductQuantityInStorages(Mockito.longThat(id -> (id != null) && (id > 0L))))
					.thenReturn(productQuantityInStorages);
			Mockito.when(
					storageDAOMock.getProductGroupInStoragesById(Mockito.longThat(id -> (id != null) && (id > 0L))))
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

			Long productId = 1L;

			List<Storage> boughtStorages = storageService.buyProductFromStorages(productId, productQuantityInStorages);
			BigDecimal realProductQuntity = boughtStorages.stream().map(storage -> storage.getQuantity())
					.reduce((a, b) -> a.add(b)).get();

			assertAll(() -> {
				assertThat(boughtStorages).isNotNull();
				assertThat(realProductQuntity).isEqualByComparingTo(productQuantityInStorages);
				assertThat(storages).isEmpty();
			});
		}

		@Test
		public void checkBuyProductFromStoragesShouldReturnSingleStorageList() throws ServiceException {

			BigDecimal productQuantityInStorages = new BigDecimal(17.4, MathContext.DECIMAL32);

			Product product1 = Product.builder()
					.id(1L)
					.description("Рассыпчатая мука")
					.isAuction(false)
					.build();

			List<Storage> storages = new ArrayList<>() {
				private static final long serialVersionUID = -137028432569851243L;

				{
					add(Storage.builder()
							.id(2L)
							.product(product1)
							.unit(Unit.kg)
							.price(new BigDecimal(2.79, MathContext.DECIMAL32))
							.quantity(new BigDecimal(4, MathContext.DECIMAL32))
							.build());
					add(Storage.builder()
							.id(3L)
							.product(product1)
							.unit(Unit.kg)
							.price(new BigDecimal(5.69, MathContext.DECIMAL32))
							.quantity(new BigDecimal(8, MathContext.DECIMAL32))
							.build());
					add(Storage.builder()
							.id(8L)
							.product(product1)
							.unit(Unit.kg)
							.price(new BigDecimal(2.82, MathContext.DECIMAL32))
							.quantity(new BigDecimal(5.4, MathContext.DECIMAL32))
							.build());
				}
			};

			Mockito.when(storageDAOMock.isStorageExistsByProductId(Mockito.longThat(id -> (id != null) && (id > 0L))))
					.thenReturn(true);
			Mockito.when(storageDAOMock.getProductQuantityInStorages(Mockito.longThat(id -> (id != null) && (id > 0L))))
					.thenReturn(productQuantityInStorages);
			Mockito.when(
					storageDAOMock.getProductGroupInStoragesById(Mockito.longThat(id -> (id != null) && (id > 0L))))
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

			Long productId = 1L;
			BigDecimal productQuantity = new BigDecimal(3.8, MathContext.DECIMAL32);

			List<Storage> boughtStorages = storageService.buyProductFromStorages(productId, productQuantity);
			BigDecimal stayInStorages = storages.stream().map(storage -> storage.getQuantity())
					.reduce((a, b) -> a.add(b)).get();

			assertAll(() -> {
				assertThat(boughtStorages).isNotNull();
				assertThat(boughtStorages.get(0).getQuantity().add(stayInStorages))
						.isEqualByComparingTo(productQuantityInStorages);
			});
		}

		@Test
		public void checkBuyProductFromStoragesOnNonExistingProductShouldThrowServiceException() {
			Mockito.when(storageDAOMock.isStorageExistsByProductId(Mockito.longThat(id -> (id != null) && (id > 0L))))
					.thenReturn(false);

			assertThatThrownBy(() -> storageService.buyProductFromStorages(15L, new BigDecimal(12.0)))
					.isInstanceOf(ServiceException.class)
					.hasMessage("Error. All or some products that passed are not exists or not added in storage!");
		}

		@Test
		public void checkBuyProductFromStoragesOnLackOfProductQuantityShouldThrowServiceException() {
			BigDecimal productQuantityInStorages = new BigDecimal(5);

			Mockito.when(storageDAOMock.isStorageExistsByProductId(Mockito.longThat(id -> (id != null) && (id > 0L))))
					.thenReturn(true);
			Mockito.when(storageDAOMock.getProductQuantityInStorages(Mockito.longThat(id -> (id != null) && (id > 0L))))
					.thenReturn(productQuantityInStorages);

			BigDecimal wantToBuyProducts = new BigDecimal(6);
			assertThatThrownBy(() -> storageService.buyProductFromStorages(15L, wantToBuyProducts))
					.isInstanceOf(ServiceException.class).hasMessageContaining(
							"Error. Given product quantity is exceeding that storages contain. Error for product with id=");
		}
	}

	@Test
	public void checkGetBoughtProductsTotalPriceShouldReturnTotalPrice() {
		Product product1 = Product.builder()
				.id(1L)
				.description("Рассыпчатая мука")
				.isAuction(false)
				.build();

		List<Storage> storages = new ArrayList<>() {
			private static final long serialVersionUID = -137028432569851243L;

			{
				add(Storage.builder()
						.id(2L)
						.product(product1)
						.unit(Unit.kg)
						.price(new BigDecimal(2.79, MathContext.DECIMAL32))
						.quantity(new BigDecimal(4, MathContext.DECIMAL32))
						.build());
				add(Storage.builder()
						.id(3L)
						.product(product1)
						.unit(Unit.kg)
						.price(new BigDecimal(5.69, MathContext.DECIMAL32))
						.quantity(new BigDecimal(8, MathContext.DECIMAL32))
						.build());
				add(Storage.builder()
						.id(8L)
						.product(product1)
						.unit(Unit.kg)
						.price(new BigDecimal(2.82, MathContext.DECIMAL32))
						.quantity(new BigDecimal(5.4, MathContext.DECIMAL32))
						.build());
			}
		};

		Map<Long, List<Storage>> productInStorages = new HashMap<>() {
			private static final long serialVersionUID = 1L;

			{
				put(product1.getId(), storages);
			}
		};

		BigDecimal expectedResult = new BigDecimal(71.908, MathContext.DECIMAL32);
		BigDecimal result = storageService.getBoughtProductsTotalPrice(productInStorages);
		
		assertThat(result).isEqualByComparingTo(expectedResult);
	}

}
