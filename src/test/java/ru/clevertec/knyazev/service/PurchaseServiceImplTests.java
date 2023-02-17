package ru.clevertec.knyazev.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

public class PurchaseServiceImplTests {
	private StorageService storageServiceMock;
	private CasherService casherServiceMock;
	private ShopService shopServiceMock;

	private PurchaseService purchaseService;

	@BeforeEach
	public void setUp() {
		storageServiceMock = Mockito.mock(StorageService.class);
		casherServiceMock = Mockito.mock(CasherService.class);
		shopServiceMock = Mockito.mock(ShopService.class);

		AbstractReceiptBuilder receiptBuilder = new ReceiptBuilderImpl();
		purchaseService = new PurchaseServiceImpl(storageServiceMock, casherServiceMock, shopServiceMock, receiptBuilder);
	}

	@Test
	public void whenBuyPurchases() throws ServiceException {
		final Long productId = 1L;
		Product product1 = new Product(productId, "Рассыпчатая мука", false);

		List<Storage> storages = new ArrayList<>() {
			private static final long serialVersionUID = -2370284320759078343L;

			{
				add(new Storage(4L, product1, Unit.кг, new BigDecimal(2.79), new BigDecimal(4)));
				add(new Storage(5L, product1, Unit.кг, new BigDecimal(5.69), new BigDecimal(125.658)));
				add(new Storage(6L, product1, Unit.кг, new BigDecimal(2.82), new BigDecimal(16)));
			}
		};

		BigDecimal totalPrice = storages.stream().map(storage -> storage.getQuantity().multiply(storage.getPrice()))
				.reduce((a, b) -> a.add(b)).orElse(new BigDecimal(0));

		Address address = new Address(1L, "212658", "Belarus", "Minsk", "Malaya str", "12");
		Shop shop = new Shop(1L, "TestShop", address, "+375 29 689 23 56");

		Casher casher = new Casher(1L);

		Mockito.when(storageServiceMock.buyProductFromStorages(Mockito.longThat(l -> (l != null) && (l > 0L)),
				Mockito.any(BigDecimal.class))).thenReturn(storages);
		Mockito.when(casherServiceMock.showCasherId()).thenReturn(casher.getId());
		Mockito.when(shopServiceMock.showShop()).thenReturn(shop);
		Mockito.when(storageServiceMock.getBoughtProductsTotalPrice(Mockito.anyMap())).thenReturn(totalPrice);

		Map<ProductDTO, BigDecimal> purchase = new HashMap<>() {
			private static final long serialVersionUID = 1L;

			{
				put(new ProductDTO(productId), new BigDecimal(125.38)); // < 145.658
			}
		};

		Receipt receipt = purchaseService.buyPurchases(purchase);
		
		assertAll(() -> {
			assertNotNull(receipt);
			assertNotNull(receipt.getCasherIdWithDateTime());
			assertNotNull(receipt.getShop());
		});
	}
	
	@Test
	public void whenGetPurchasesOnNullProductDTO() {
		Map<ProductDTO, BigDecimal> productsDTO = null;
		
		assertThrows(ServiceException.class, () -> purchaseService.buyPurchases(productsDTO), "ServiceException expected when null productDTO was given.");
	}
	
	@Test
	public void whenGetPurchasesOnEmptyProductDTO() {
		Map<ProductDTO, BigDecimal> productsDTO = new HashMap<>();
				
		assertThrows(ServiceException.class, () -> purchaseService.buyPurchases(productsDTO), "ServiceException expected when null productDTO was given.");
	}
	
}
