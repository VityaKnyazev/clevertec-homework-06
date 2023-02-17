package ru.clevertec.knyazev.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.entity.DiscountCard;

public class DiscountCardDAOImplTests {
	private DiscountCardDAO discountCardDAOImpl;
	
	@BeforeEach
	public void setUp() {
		discountCardDAOImpl = new DiscountCardDAOImpl();
	}
	
	@Test
	public void whenGetDiscountCardNumber() {
		String number = "15fd20181";
		
		DiscountCard discountCard = discountCardDAOImpl.getDiscountCardByNumber(number);
		
		assertAll(() -> {
			assertNotNull(discountCard);
			assertNotNull(discountCard.getId());
		});
		
		assertEquals(number, discountCard.getNumber());
	}
	
	@Test
	public void whenGetDiscountCardNumberByNullNumber() {
		String number = null;
		
		DiscountCard discountCard = discountCardDAOImpl.getDiscountCardByNumber(number);
		
		assertNull(discountCard);
	}
	
	@Test
	public void whenGetDiscountCardNumberByBadNumber() {
		String number = "1235fg44";
		
		DiscountCard discountCard = discountCardDAOImpl.getDiscountCardByNumber(number);
		
		assertNull(discountCard);
	}
	
	@Test
	public void whenSaveDiscountCard() {
		Long id = null;
		
		DiscountCard discountCard = new DiscountCard(id, "15fd20193", 10);
		
		DiscountCard savedDiscountCard = discountCardDAOImpl.saveDiscountCard(discountCard);
		
		assertAll(() -> {
			assertNotNull(savedDiscountCard);
			assertNotNull(savedDiscountCard.getId());
		});
	}
	
	@Test
	public void whenIsDiscountCardExists() {
		String number = "15fd20188";
		
		assertTrue(discountCardDAOImpl.isDiscountCardExists(number));
	}
	
	@Test
	public void whenIsDiscountCardExistsOnNullCardNumber() {
		String number = null;
		
		assertFalse(discountCardDAOImpl.isDiscountCardExists(number));
	}
	
	@Test
	public void whenIsDiscountCardExistsOnBadCardNumber() {
		String number = "12345678f";
		
		assertFalse(discountCardDAOImpl.isDiscountCardExists(number));
	}
}
