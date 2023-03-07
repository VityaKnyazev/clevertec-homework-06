package ru.clevertec.knyazev.util;

import java.math.BigDecimal;

public final class Settings {
	//For DiscountCardService.class
	public static final int MAX_DISCOUNT_CARD_VAUE = 30;
	//For DiscountProductGroupService	
	public static final BigDecimal PRODUCT_QUANTITY_FOR_DISCOUNT = new BigDecimal(5);
	public static final int DISCOUNT_VALUE_PERCENT_FOR_PRODUCT_GROUP = 10;
	//Scale for BigDecimal
	public static final int PRICE_SCALE_VALUE = 2;
	public static final  int QUANTITY_SCALE_VALUE = 3;
	//File
	public static final String OUTPUT_FOLDER = "src/main/resources/ru/clevertec/knyazev/";
	public static final String OUTPUT_FILE_NAME = "check.txt";
	public static final String INPUT_FOLDER = "src/main/resources/ru/clevertec/knyazev/";
	//Cache algorithm settings
	public static final String CACHE_FILE_SETTINGS = "application.yaml";
	public static final String CACHE_OBJECT = "cache";
	public static final String CACHE_ALGORITHM = "algorithm";
	public static final String CACHE_MAX_SIZE = "size";
	
	private Settings() {}
}