package ru.clevertec.knyazev.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import ru.clevertec.knyazev.entity.DiscountCard;

public class DiscountCardDAOJPATests {
	private EntityManager entityManagerMock;
	private Query queryMock;

	private DiscountCardDAOJPA discountCardDAOJPA;

	@BeforeEach
	public void setUp() {
		entityManagerMock = Mockito.mock(EntityManager.class);
		queryMock = Mockito.mock(Query.class);
		discountCardDAOJPA = new DiscountCardDAOJPA(entityManagerMock);
	}

	@Test
	public void whenGetDiscountCardByNumber() {
		final String discountCardNumber = "123456789";
		DiscountCard discountCard = new DiscountCard(2L, discountCardNumber, 4);

		Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(discountCard);

		assertEquals(discountCard, discountCardDAOJPA.getDiscountCardByNumber(discountCardNumber));
	}
	
	@Test
	public void whenGetDiscountCardByNullNumber() {
		final String discountCardNumber = null;

		assertNull(discountCardDAOJPA.getDiscountCardByNumber(discountCardNumber));
	}
	
	@Test
	public void whenGetDiscountCardByBadNumber() {
		final String discountCardNumber = "";

		assertNull(discountCardDAOJPA.getDiscountCardByNumber(discountCardNumber));
	}
	
	@Test
	public void whenSaveDiscountCard() {
		final Long discountCardId = null;
		DiscountCard discountCard = new DiscountCard(discountCardId, "123456321", 4);
		
		Mockito.doNothing().when(entityManagerMock).persist(discountCard);
		
		assertNotNull(discountCardDAOJPA.saveDiscountCard(discountCard));
	}
	
	@Test
	public void whenSaveDiscountCardOnNotNullId() {
		final Long discountCardId = 1L;
		DiscountCard discountCard = new DiscountCard(discountCardId, "123456321", 4);
		
		assertNull(discountCardDAOJPA.saveDiscountCard(discountCard));
	}
	
	@Test
	public void whenIsDiscountCardExists() {
		int resultQuantity = 2;
		
		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(resultQuantity);
		
		final String discountCardNumber = "123456789";
		
		assertEquals(true, discountCardDAOJPA.isDiscountCardExists(discountCardNumber));
	}	
	
	@Test
	public void whenIsDiscountCardNotExists() {
		int resultQuantity = 0;
		
		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(resultQuantity);
		
		final String discountCardNumber = "323456789";
		
		assertEquals(false, discountCardDAOJPA.isDiscountCardExists(discountCardNumber));
	}
	
}