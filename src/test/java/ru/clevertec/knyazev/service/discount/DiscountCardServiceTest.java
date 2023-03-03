package ru.clevertec.knyazev.service.discount;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.knyazev.dao.DiscountCardDAO;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.entity.DiscountCard;
import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;

@ExtendWith(MockitoExtension.class)
public class DiscountCardServiceTest {
	@Mock
	private DiscountCardDAO discountCardDAOMock;
	@InjectMocks
	private DiscountCardService discountCardService;

	private Set<DiscountCardDTO> discountCardsDTO;

	@BeforeEach
	public void setUp() {
		discountCardsDTO = new HashSet<>() {
			private static final long serialVersionUID = 1215443L;

			{
				add(new DiscountCardDTO("12F589654")); // Not exists
				add(new DiscountCardDTO("12F589655")); // 3 %
				add(new DiscountCardDTO("12F589656")); // 2 %
			}
		};
		discountCardService.setDiscountCardsDTO(discountCardsDTO);
	}

	@Test
	public void checkApplyDiscountShouldReturnTotalDiscountCardValue() {
		Set<DiscountCard> discountCards = new HashSet<>() {
			private static final long serialVersionUID = 5454512L;

			{
				add(DiscountCard.builder()
						.id(1L)
						.number("12F589655")
						.discountValue(3)
						.build());
				add(DiscountCard.builder()
						.id(2L)
						.number("12F589656")
						.discountValue(2)
						.build());
				add(DiscountCard.builder()
						.id(3L)
						.number("12F589657")
						.discountValue(1)
						.build());
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

		Product product1 = Product.builder()
				.id(1L)
				.description("On this product must be applying only discount that depends on its quantity")
				.isAuction(true)
				.build();
		Product product2 = Product.builder()
				.id(2L)
				.description("On this product must be applying discount card only")
				.isAuction(false)
				.build();

		List<Storage> discountCardProducts = new ArrayList<>() {
			private static final long serialVersionUID = 1748625L;
			{
				add(Storage.builder()
						.id(7L)
						.product(product2)
						.unit(Unit.pcs)
						.price(new BigDecimal(6.3))
						.quantity(new BigDecimal(8))
						.build());
				add(Storage.builder()
						.id(9L)
						.product(product2)
						.unit(Unit.pcs)
						.price(new BigDecimal(4.32))
						.quantity(new BigDecimal(6))
						.build());
			}
		};

		List<Storage> discountproductGroup = new ArrayList<>() {
			private static final long serialVersionUID = 185265L;

			{
				add(Storage.builder()
						.id(10L)
						.product(product1)
						.unit(Unit.unit)
						.price(new BigDecimal(5.21))
						.quantity(new BigDecimal(3))
						.build());
				add(Storage.builder()
						.id(12L)
						.product(product1)
						.unit(Unit.unit)
						.price(new BigDecimal(4.62))
						.quantity(new BigDecimal(2))
						.build());
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
		// 6.3 * 8 = 50.4; 4.32 * 6 = 25,92; (50.4 + 25.92) * 5 / 100 = 3.816
		BigDecimal expectedDiscountCardsValue = discountCardProducts.stream()
				.map(storage -> storage.getPrice().multiply(storage.getQuantity())).reduce((a, b) -> a.add(b))
				.orElse(new BigDecimal(0)).multiply(new BigDecimal(5)).divide(new BigDecimal(100));

		assertThat(totalDiscountCardsValue).isEqualByComparingTo(expectedDiscountCardsValue);

	}

	@Test
	public void checkApplyDiscountOnNullBoughtProductsShouldReturnZero() {
		Map<Long, List<Storage>> boughtProductsInStorages = null;

		BigDecimal expectedDiscountValue = new BigDecimal(0);
		BigDecimal actualDiscountValue = discountCardService.applyDiscount(boughtProductsInStorages);

		assertThat(actualDiscountValue).isEqualByComparingTo(expectedDiscountValue);
	}

	@Test
	public void  checkApplyDiscountOnEmptyBoughtProductsShouldReturnZero() {
		Map<Long, List<Storage>> boughtProductsInStorages = new HashMap<>();

		BigDecimal expectedDiscountValue = new BigDecimal(0);
		BigDecimal actualDiscountValue = discountCardService.applyDiscount(boughtProductsInStorages);

		assertThat(actualDiscountValue).isEqualByComparingTo(expectedDiscountValue);
	}
}