package ru.clevertec.knyazev.service.discount;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.entity.Product;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.entity.util.Unit;
import ru.clevertec.knyazev.util.Settings;

public class DiscountProductGroupServiceTest {
	private DiscountService discountProductGroupServise;

	@BeforeEach
	public void setUp() {
		discountProductGroupServise = new DiscountProductGroupService();
	}

	@Test
	public void checkApplyDiscountShouldReturnTotalProductGroupDiscount() {
		Product product1 = new Product(1L,
				"On this product must be applying only discount that depends on its quantity", true);
		Product product2 = new Product(2L, "On this product must be applying discount card only", false);

		List<Storage> discountCardProducts = new ArrayList<>() {
			private static final long serialVersionUID = 1748625L;
			{
				add(Storage.builder().id(7L).product(product2).unit(Unit.pcs).price(new BigDecimal(6.3))
						.quantity(new BigDecimal(8)).build());
				add(Storage.builder().id(9L).product(product2).unit(Unit.pcs).price(new BigDecimal(4.32))
						.quantity(new BigDecimal(6)).build());
			}
		};

		List<Storage> discountproductGroup = new ArrayList<>() {
			private static final long serialVersionUID = 185265L;

			{
				add(Storage.builder().id(10L).product(product1).unit(Unit.unit).price(new BigDecimal(5.21))
						.quantity(new BigDecimal(3)).build());
				add(Storage.builder().id(12L).product(product1).unit(Unit.unit).price(new BigDecimal(4.62))
						.quantity(new BigDecimal(2)).build());
			}
		};

		Map<Long, List<Storage>> boughtProductsInStorages = new HashMap<>() {
			private static final long serialVersionUID = -1085315221959935342L;

			{
				put(product1.getId(), discountproductGroup);
				put(product2.getId(), discountCardProducts);
			}
		};

		BigDecimal actualDiscount = discountProductGroupServise.applyDiscount(boughtProductsInStorages);
		// 5.21 * 3 = 15.63; 4.62 * 2 = 9.24; (15.63 + 9.24) * 10 / 100 = 2.487
		BigDecimal expectedDiscount = discountproductGroup.stream()
				.map(storage -> storage.getPrice().multiply(storage.getQuantity())).reduce((a, b) -> a.add(b))
				.orElse(new BigDecimal(0)).multiply(new BigDecimal(Settings.DISCOUNT_VALUE_PERCENT_FOR_PRODUCT_GROUP))
				.divide(new BigDecimal(100));

		assertThat(actualDiscount).isEqualByComparingTo(expectedDiscount);
	}

	@Test
	public void checkApplyDiscountOnNullBoughtProductsShouldReturnZero() {
		Map<Long, List<Storage>> boughtProductsInStorages = null;

		BigDecimal expectedDiscountValue = new BigDecimal(0);
		BigDecimal actualDiscountValue = discountProductGroupServise.applyDiscount(boughtProductsInStorages);

		assertThat(actualDiscountValue).isEqualByComparingTo(expectedDiscountValue);
	}

	@Test
	public void  checkApplyDiscountOnEmptyBoughtProductsShouldReturnZero() {
		Map<Long, List<Storage>> boughtProductsInStorages = new HashMap<>();

		BigDecimal expectedDiscountValue = new BigDecimal(0);
		BigDecimal actualDiscountValue = discountProductGroupServise.applyDiscount(boughtProductsInStorages);

		assertThat(actualDiscountValue).isEqualByComparingTo(expectedDiscountValue);
	}

}
