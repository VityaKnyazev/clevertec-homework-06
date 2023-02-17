package ru.clevertec.knyazev.service.discount;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class DiscountProductGroupServiceTests {
	private DiscountService discountProductGroupServise;
	
	@BeforeEach
	public void setUp() {
		discountProductGroupServise = new DiscountProductGroupService();
	}
	
	@Test
	public void whenApplyDiscount() {
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
				add(new Storage(8L, product1, Unit.ед, new BigDecimal(5.21), new BigDecimal(3)));
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
		
		BigDecimal actualDiscount = discountProductGroupServise.applyDiscount(boughtProductsInStorages);
		//5.21 * 3 = 15.63; 4.62 * 2 = 9.24; (15.63 + 9.24) * 10 / 100 = 2.487
		BigDecimal expectedDiscount = discountproductGroup.stream().map(storage -> storage.getPrice().multiply(storage.getQuantity())).reduce((a, b) -> a.add(b)).orElse(new BigDecimal(0)).multiply(new BigDecimal(Settings.DISCOUNT_VALUE_PERCENT_FOR_PRODUCT_GROUP)).divide(new BigDecimal(100));	
				
		assertTrue(expectedDiscount.compareTo(actualDiscount) == 0);
	}
	
	@Test
	public void whenApplyDiscountOnNullBoughtProducts() {
		final Map<Long, List<Storage>> boughtProductsInStorages = null;
		
		BigDecimal expectedDiscountValue = new BigDecimal(0);
		assertTrue(expectedDiscountValue.compareTo(discountProductGroupServise.applyDiscount(boughtProductsInStorages)) == 0);
	}
	
	@Test
	public void whenApplyDiscountOnEmptyBoughtProducts() {
		final Map<Long, List<Storage>> boughtProductsInStorages = new HashMap<>();
		
		BigDecimal expectedDiscountValue = new BigDecimal(0);
		assertTrue(expectedDiscountValue.compareTo(discountProductGroupServise.applyDiscount(boughtProductsInStorages)) == 0);
	}

}
