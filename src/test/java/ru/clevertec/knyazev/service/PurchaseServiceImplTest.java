package ru.clevertec.knyazev.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.entity.Casher;
import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.view.AbstractReceiptBuilder;
import ru.clevertec.knyazev.view.Receipt;
import ru.clevertec.knyazev.view.ReceiptBuilderImpl;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceImplTest {
	@Mock
	private StorageService storageServiceMock;
	@Mock
	private CasherService casherServiceMock;
	@Mock
	private ShopService shopServiceMock;

	private PurchaseService purchaseService;

	@BeforeEach
	public void setUp() {
		AbstractReceiptBuilder receiptBuilder = new ReceiptBuilderImpl();
		purchaseService = new PurchaseServiceImpl(storageServiceMock, casherServiceMock, shopServiceMock,
				receiptBuilder);
	}

	@Test
	public void checkBuyPurchasesShouldReturnReceipt() throws ServiceException {
		Long inputProductId = 1L;
		Product product = Product.builder()
				.id(inputProductId)
				.description("Рассыпчатая мука")
				.isAuction(false)
				.build();

		List<Storage> storages = new ArrayList<>() {
			private static final long serialVersionUID = -2370284320759078343L;

			{
				add(Storage.builder()
						.id(4L)
						.product(product)
						.unit(Unit.kg)
						.price(new BigDecimal(2.79))
						.quantity(new BigDecimal(4))
						.build());
				add(Storage.builder()
						.id(5L)
						.product(product)
						.unit(Unit.kg)
						.price(new BigDecimal(5.69))
						.quantity(new BigDecimal(125.658))
						.build());
				add(Storage.builder()
						.id(6L)
						.product(product)
						.unit(Unit.kg)
						.price(new BigDecimal(2.82))
						.quantity(new BigDecimal(16))
						.build());
			}
		};

		BigDecimal totalPrice = storages.stream().map(storage -> storage.getQuantity().multiply(storage.getPrice()))
				.reduce((a, b) -> a.add(b)).orElse(new BigDecimal(0));

		Address address = Address
				.builder()
				.id(1L)
				.postalCode("212658")
				.country("Belarus")
				.city("Minsk")
				.street("Malaya str")
				.buildingNumber("12")
				.build();
		Shop shop = Shop.builder()
				.id(1L)
				.name("TestShop")
				.address(address)
				.phone("+375 29 689 23 56")
				.build();

		Casher casher = Casher.builder()
				.id(1L)
				.build();

		Mockito.when(storageServiceMock.buyProductFromStorages(Mockito.longThat(l -> (l != null) && (l > 0L)),
				Mockito.any(BigDecimal.class))).thenReturn(storages);
		Mockito.when(casherServiceMock.showCasherId()).thenReturn(casher.getId());
		Mockito.when(shopServiceMock.showShop()).thenReturn(shop);
		Mockito.when(storageServiceMock.getBoughtProductsTotalPrice(Mockito.anyMap())).thenReturn(totalPrice);

		Map<ProductDTO, BigDecimal> purchase = new HashMap<>() {
			private static final long serialVersionUID = 1L;

			{
				put(new ProductDTO(inputProductId), new BigDecimal(125.38)); // < 145.658
			}
		};

		Receipt receipt = purchaseService.buyPurchases(purchase);

		assertAll(() -> {
			assertThat(receipt).isNotNull();
			assertThat(receipt.getCasherIdWithDateTime()).isNotNull();
			assertThat(receipt.getShop()).isNotNull();
		});
	}

	@Test
	public void checkBuyPurchasesOnNullProductDTOShouldThrowServiceException() {
		Map<ProductDTO, BigDecimal> productsDTO = null;

		assertThatThrownBy(() -> purchaseService.buyPurchases(productsDTO)).isInstanceOf(ServiceException.class)
				.hasMessage("Error when buying purchases. Products are null or empty!");
	}

	@Test
	public void checkBuyPurchasesOnEmptyProductDTOShouldThrowServiceException() {
		Map<ProductDTO, BigDecimal> productsDTO = new HashMap<>();

		assertThatThrownBy(() -> purchaseService.buyPurchases(productsDTO)).isInstanceOf(ServiceException.class).hasMessage("Error when buying purchases. Products are null or empty!");
	}
	
	@Test
	public void checkBuyPurchasesWhenBoughtProductsIsEmptyShouldThrowServiceException() throws ServiceException {
		List<Storage> storages = new ArrayList<>();

		Mockito.when(storageServiceMock.buyProductFromStorages(Mockito.longThat(l -> (l != null) && (l > 0L)),
				Mockito.any(BigDecimal.class))).thenReturn(storages);
		
		Map<ProductDTO, BigDecimal> purchase = new HashMap<>() {
			private static final long serialVersionUID = 1L;

			{
				put(new ProductDTO(1L), new BigDecimal(125.38));
			}
		};

		assertThatThrownBy(() -> purchaseService.buyPurchases(purchase)).isInstanceOf(ServiceException.class).hasMessage("Nothing to buy on current products ids and count!");
	}

}
