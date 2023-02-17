package ru.clevertec.knyazev.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.aop.framework.AopProxyUtils;

import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.util.Settings;

public class DiscountServiceComposite {
	private Map<DiscountServiceType, DiscountService> discountServices;
	private static DiscountServiceComposite discountServiceComposite;

	private DiscountServiceComposite() {
		discountServices = new EnumMap<>(DiscountServiceType.class);
	}

	public void addDiscountService(DiscountService discountService) {
		String discountServiceName ="";
		
		if (discountService.getClass().getAnnotatedSuperclass().getType().getTypeName()
				.equals("java.lang.reflect.Proxy")) {
			DiscountService realDiscountService = (DiscountService) AopProxyUtils.getSingletonTarget(discountService);
			discountServiceName = realDiscountService.getClass().getSimpleName();
		} else {
			discountServiceName = discountService.getClass().getSimpleName();
		}				

		if (discountServiceName.equals(DiscountServiceType.DiscountCardService.name())) {
			discountServices.put(DiscountServiceType.DiscountCardService, discountService);
		} else if (discountServiceName.equals(DiscountServiceType.DiscountProductGroupService.name())) {
			discountServices.put(DiscountServiceType.DiscountProductGroupService, discountService);
		}
	}

	public BigDecimal getTotalCardsDiscount(Map<Long, List<Storage>> boughtProductsInStorages) {
		return getTotalDiscount(boughtProductsInStorages, DiscountServiceType.DiscountCardService);
	}

	public BigDecimal getTotalProductGroupsDiscount(Map<Long, List<Storage>> boughtProductsInStorages) {
		return getTotalDiscount(boughtProductsInStorages, DiscountServiceType.DiscountProductGroupService);
	}

	private BigDecimal getTotalDiscount(Map<Long, List<Storage>> boughtProductsInStorages,
			DiscountServiceType discountProductGroupsService) {
		BigDecimal totalDiscount = BigDecimal.ZERO;

		DiscountService discountService = discountServices.get(discountProductGroupsService);

		if (discountService != null) {
			totalDiscount = discountService.applyDiscount(boughtProductsInStorages);
			totalDiscount = totalDiscount.setScale(Settings.PRICE_SCALE_VALUE, RoundingMode.HALF_UP);
		}

		return totalDiscount;
	}

	public static DiscountServiceComposite getInstance() {
		if (discountServiceComposite == null) {
			discountServiceComposite = new DiscountServiceComposite();
		}

		return discountServiceComposite;
	}

	private static enum DiscountServiceType {
		DiscountCardService, DiscountProductGroupService
	}
}
