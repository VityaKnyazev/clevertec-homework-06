package ru.clevertec.knyazev.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Shop;

public abstract class AbstractReceiptBuilder {
	String casherId;
	String shop;
	LocalDateTime dateTime;
	String purchases;
	String totalPrice;
	String discountCardsValue;
	String productGroupsDiscountValue;
	String totalDiscountPrice;

	AbstractReceiptBuilder() {
		dateTime = LocalDateTime.now();
	}

	public abstract AbstractReceiptBuilder setCasherIdWithDateTime(Long casherId);

	public abstract AbstractReceiptBuilder setShop(Shop shop);

	public abstract AbstractReceiptBuilder setPurchases(List<PurchaseDTO> purchasesDTO);
	
	public abstract AbstractReceiptBuilder setTotalPrice(BigDecimal totalPrice);

	public abstract AbstractReceiptBuilder setDiscountCardsValue(BigDecimal discountCardsValue);

	public abstract AbstractReceiptBuilder setProductGroupsDiscountValue(BigDecimal productGroupsDiscountValue);

	public abstract AbstractReceiptBuilder setTotalDiscountPrice(BigDecimal totalDiscountPrice);

	public Receipt build() {
		return new Receipt(casherId, shop, purchases, totalPrice, discountCardsValue, productGroupsDiscountValue,
				totalDiscountPrice);
	}

}