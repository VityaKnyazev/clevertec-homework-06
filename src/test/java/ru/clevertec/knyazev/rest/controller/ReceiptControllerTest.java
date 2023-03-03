package ru.clevertec.knyazev.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.entity.util.Unit;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.discount.DiscountCardService;
import ru.clevertec.knyazev.service.discount.DiscountProductGroupService;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.view.Receipt;
import ru.clevertec.knyazev.view.ReceiptBuilderImpl;

public class ReceiptControllerTest {
	private PurchaseService purchaseServiceMock;
	private DiscountCardService discountCardServiceMock;
	private DiscountProductGroupService discountProductGroupServiceMock;
	private ReceiptController receiptController;
	private Receipt receipt;

	private final static String RECEIPT_REQUEST = "/receipt";

	private MockMvc mockMVC;

	@BeforeEach
	public void setUp() {
		discountProductGroupServiceMock = Mockito.mock(DiscountProductGroupService.class);
		purchaseServiceMock = Mockito.mock(PurchaseService.class);
		discountCardServiceMock = Mockito.mock(DiscountCardService.class);
		receiptController = new ReceiptController(purchaseServiceMock, discountCardServiceMock, discountProductGroupServiceMock);
		mockMVC = MockMvcBuilders.standaloneSetup(receiptController).defaultRequest(MockMvcRequestBuilders.get("/"))
				.build();

		receipt = new ReceiptBuilderImpl()
				.setCasherIdWithDateTime(1L)
				.setShop(Shop.builder()
						.id(1L)
						.name("TestShop")
						.address(Address.builder()
								.id(1L)
								.postalCode("120589")
								.country("Russia")
								.city("MOSCOW")
								.street("Chapaeva, str")
								.buildingNumber("128")
								.build())
						.phone("+7xx 85 698 55")
						.build())
				.setPurchases(new ArrayList<>() {
							  private static final long serialVersionUID = 7534980376943621763L;

							  {
								  	add(new PurchaseDTO(new BigDecimal(3), Unit.pcs, "Кофеинка темно-серая", new BigDecimal(2)));
							  }})
				.setDiscountCardsValue(BigDecimal.ZERO)
				.setProductGroupsDiscountValue(BigDecimal.ZERO)
				.setTotalPrice(new BigDecimal(6))
				.setTotalDiscountPrice(new BigDecimal(6))
				.build();
	}

	@Test
	public void checkGetReceiptShouldReturnReceiptOkStatus() throws Exception {
		String purchaseParamName = "purchase";
		String purchaseParamValue = "1-3";

		String discountCardParamName = "card";
		String discountCardParamValue = "card-123456789";

		Mockito.when(purchaseServiceMock.buyPurchases(Mockito.anyMap())).thenReturn(receipt);

		MvcResult result = mockMVC.perform(MockMvcRequestBuilders.get(RECEIPT_REQUEST)
				.param(purchaseParamName, purchaseParamValue).param(discountCardParamName, discountCardParamValue))
				.andReturn();

		int expectedStatus = 200;
		String expectedContentType = "text/plain;charset=utf-8";

		assertThat(result.getResponse().getContentType()).isEqualTo(expectedContentType);
		assertThat(result.getResponse().getStatus()).isEqualTo(expectedStatus);
	}

	@Test
	public void checkGetReceiptWithoutPurchaseParamShouldReturn400Status() throws Exception {

		MvcResult result = mockMVC.perform(MockMvcRequestBuilders.get(RECEIPT_REQUEST)).andReturn();

		int expectedStatus = 400;

		assertThat(result.getResponse().getStatus()).isEqualTo(expectedStatus);
	}

	@Test
	public void checkGetReceiptOnEmptyPurchaseShouldReturn400Status() throws Exception {
		String purchaseParamName = "purchase";
		String purchaseParamValue = "";

		Mockito.when(purchaseServiceMock.buyPurchases(Mockito.anyMap())).thenReturn(receipt);

		MvcResult result = mockMVC
				.perform(MockMvcRequestBuilders.get(RECEIPT_REQUEST).param(purchaseParamName, purchaseParamValue))
				.andReturn();

		int expectedStatus = 400;

		assertThat(result.getResponse().getStatus()).isEqualTo(expectedStatus);
	}
	
	@Test
	public void checkGetReceiptWhenThrowServiceExceptionShouldReturn400Status() throws Exception {
		String purchaseParamName = "purchase";
		String purchaseParamValue = "1-3";

		String discountCardParamName = "card";
		String discountCardParamValue = "card-123456789";

		Mockito.when(purchaseServiceMock.buyPurchases(Mockito.anyMap())).thenThrow(ServiceException.class);

		MvcResult result = mockMVC.perform(MockMvcRequestBuilders.get(RECEIPT_REQUEST)
				.param(purchaseParamName, purchaseParamValue).param(discountCardParamName, discountCardParamValue))
				.andReturn();

		int expectedStatus = 400;

		assertThat(result.getResponse().getStatus()).isEqualTo(expectedStatus);
	}
}