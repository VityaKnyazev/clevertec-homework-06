package ru.clevertec.knyazev.service.discount;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ru.clevertec.knyazev.dao.DiscountCardDAO;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.entity.DiscountCard;
import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;

public class DiscountCardServiceTests {
	private DiscountCardDAO discountCardDAOMock;

	private DiscountCardService discountCardService;

	private Set<DiscountCardDTO> discountCardsDTO;

	@BeforeEach
	public void setUp() {
		discountCardDAOMock = Mockito.mock(DiscountCardDAO.class);

		discountCardsDTO = new HashSet<>() {
			private static final long serialVersionUID = 1215443L;

			{
				add(new DiscountCardDTO("12F589654")); // Not exists
				add(new DiscountCardDTO("12F589655")); // 3 %
				add(new DiscountCardDTO("12F589656")); // 2 %
			}
		};

		discountCardService = new DiscountCardService(discountCardDAOMock);
		discountCardService.setDiscountCardsDTO(discountCardsDTO);
	}


	@Test
	public void whenApplyDiscount() {
		Set<DiscountCard> discountCards = new HashSet<>() {
			private static final long serialVersionUID = 5454512L;

			{
				add(new DiscountCard(1L, "12F589655", 3));
				add(new DiscountCard(1L, "12F589656", 2));
				add(new DiscountCard(1L, "12F589657", 1));
			}
		};

		Mockito.when(discountCardDAOMock.isDiscountCardExists(Mockito.argThat(str -> str.length() == 9)))
				.thenAnswer(invocation -> {
					String discountCardNumber = invocation.getArgument(0, String.class);
					for (DiscountCard discountCard : discountCards) {
						if (discountCard.getNumber().equals(discountCardNumber)) {
							return true;
						}
					}

					return false;
				});
		Mockito.when(discountCardDAOMock.getDiscountCardByNumber(Mockito.argThat(str -> str.length() == 9)))
				.then(invocation -> {
					String discountCardNumber = invocation.getArgument(0, String.class);
					for (DiscountCard discountCard : discountCards) {
						if (discountCard.getNumber().equals(discountCardNumber)) {
							return discountCard;
						}
					}

					return null;
				});

		Product product1 = new Product(1L,
				"On this product must be applying only discount that depends on its quantity", true);
		Product product2 = new Product(2L, "On this product must be applying discount card only", false);

		List<Storage> discountCardProducts = new ArrayList<>() {
			private static final long serialVersionUID = 1748625L;
			{
				add(new Storage(8L, product2, Unit.шт, new BigDecimal(6.3), new BigDecimal(8)));
				add(new Storage(11L, product2, Unit.шт, new BigDecimal(4.32), new BigDecimal(6)));
			}
		};

		List<Storage> discountproductGroup = new ArrayList<>() {
			private static final long serialVersionUID = 185265L;

			{
				add(new Storage(8L, product1, Unit.ед, new BigDecimal(5.21), new BigDecimal(1)));
				add(new Storage(11L, product1, Unit.ед, new BigDecimal(4.62), new BigDecimal(2)));
			}
		};

		Map<Long, List<Storage>> boughtProductsInStorages = new HashMap<>() {
			private static final long serialVersionUID = -1085315221959935342L;

			{
				put(product1.getId(), discountproductGroup);
				put(product2.getId(), discountCardProducts);
			}
		};
		
		BigDecimal totalDiscountCardsValue = discountCardService.applyDiscount(boughtProductsInStorages);
		//6.3 * 8 = 50.4; 4.32 * 6 = 25,92; (50.4 + 25.92) * 5 / 100 = 3.816
		BigDecimal expectedDiscountCardsValue = discountCardProducts.stream().map(storage -> storage.getPrice().multiply(storage.getQuantity())).reduce((a, b) -> a.add(b)).orElse(new BigDecimal(0)).multiply(new BigDecimal(5)).divide(new BigDecimal(100));
		
		assertTrue(totalDiscountCardsValue.compareTo(expectedDiscountCardsValue) == 0);

	}
	
	@Test
	public void whenApplyDiscountOnNullBoughtProducts() {
		final Map<Long, List<Storage>> boughtProductsInStorages = null;
		
		BigDecimal expectedDiscountValue = new BigDecimal(0);
		assertTrue(expectedDiscountValue.compareTo(discountCardService.applyDiscount(boughtProductsInStorages)) == 0);
	}
	
	@Test
	public void whenApplyDiscountOnEmptyBoughtProducts() {
		final Map<Long, List<Storage>> boughtProductsInStorages = new HashMap<>();
		
		BigDecimal expectedDiscountValue = new BigDecimal(0);
		assertTrue(expectedDiscountValue.compareTo(discountCardService.applyDiscount(boughtProductsInStorages)) == 0);
	}
}