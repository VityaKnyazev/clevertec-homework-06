package ru.clevertec.knyazev.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import ru.clevertec.knyazev.entity.DiscountCard;

@ExtendWith(MockitoExtension.class)
public class DiscountCardDAOJPATest {
	@Mock
	private EntityManager entityManagerMock;
	@Mock
	private Query queryMock;
	@InjectMocks
	private DiscountCardDAOJPA discountCardDAOJPA;

	@Test
	public void checkGetDiscountCardByNumberShouldReturnDiscountCard() {
		final String inputDiscountCardNumber = "123456789";
		DiscountCard expectedDiscountCard = DiscountCard.builder().id(2L).number(inputDiscountCardNumber)
				.discountValue(4).build();

		Mockito.when(entityManagerMock.createQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(expectedDiscountCard);

		DiscountCard actualDiscountCard = discountCardDAOJPA.getDiscountCardByNumber(inputDiscountCardNumber);

		assertThat(actualDiscountCard).isEqualTo(expectedDiscountCard);
	}

	@Test
	public void checkGetDiscountCardByNumberOnNullNumberShouldReturnNull() {
		final String inputDiscountCardNumber = null;

		assertThat(discountCardDAOJPA.getDiscountCardByNumber(inputDiscountCardNumber)).isNull();
	}

	@Test
	public void checkGetDiscountCardByNumberOnBadNumberShouldReturnNull() {
		final String inputDiscountCardNumber = "";

		assertThat(discountCardDAOJPA.getDiscountCardByNumber(inputDiscountCardNumber)).isNull();
	}

	@Test
	public void checkSaveDiscountCardShouldReturnDiscountCard() {
		final Long savingDiscountCardId = null;
		DiscountCard savingDiscountCard = DiscountCard.builder().id(savingDiscountCardId).number("123456321")
				.discountValue(4).build();

		Mockito.doNothing().when(entityManagerMock).persist(Mockito.any(DiscountCard.class));

		DiscountCard actualDiscountCard = discountCardDAOJPA.saveDiscountCard(savingDiscountCard);

		Mockito.verify(entityManagerMock, Mockito.times(1)).persist(Mockito.any(DiscountCard.class));
		assertThat(actualDiscountCard).isEqualTo(savingDiscountCard);
	}

	@Test
	public void checkSaveDiscountCardOnNotNullIdShouldReturnNull() {
		final Long savingDiscountCardId = 1L;
		DiscountCard savingDiscountCard = DiscountCard.builder().id(savingDiscountCardId).number("123456321")
				.discountValue(4).build();

		Mockito.verify(entityManagerMock, never()).persist(Mockito.any(DiscountCard.class));
		assertThat(discountCardDAOJPA.saveDiscountCard(savingDiscountCard)).isNull();
	}

	@Test
	public void checkIsDiscountCardExistsShouldReturnTrue() {
		int resultQuantity = 1;

		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(resultQuantity);

		final String inputDiscountCardNumber = "123456789";
		
		boolean actualIsDiscountCardExists = discountCardDAOJPA.isDiscountCardExists(inputDiscountCardNumber);
		
		assertThat(actualIsDiscountCardExists).isTrue();
	}

	@Test
	public void checkIsDiscountCardExistsShouldReturnFalse() {
		int resultQuantity = 0;

		Mockito.when(entityManagerMock.createNativeQuery(Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.setParameter(Mockito.anyInt(), Mockito.anyString())).thenReturn(queryMock);
		Mockito.when(queryMock.getSingleResult()).thenReturn(resultQuantity);

		final String inputDiscountCardNumber = "323456789";
		
		boolean actualIsDiscountCardExists = discountCardDAOJPA.isDiscountCardExists(inputDiscountCardNumber);
		
		assertThat(actualIsDiscountCardExists).isFalse();
	}

}