package ru.clevertec.knyazev.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.entity.DiscountCard;

public class DiscountCardDAOImplTest {
	private DiscountCardDAO discountCardDAOImpl;

	@BeforeEach
	public void setUp() {
		discountCardDAOImpl = new DiscountCardDAOImpl();
	}

	@Test
	public void checkGetDiscountCardByNumberShouldReturnDiscountCard() {
		String inputNumber = "15fd20181";
		Long expectedId = 1L;

		DiscountCard actualDiscountCard = discountCardDAOImpl.getDiscountCardByNumber(inputNumber);

		assertAll(() -> {
			assertThat(actualDiscountCard).isNotNull();
			assertThat(actualDiscountCard.getId()).isEqualTo(expectedId);
		});
	}

	@Test
	public void checkGetDiscountCardByNumberOnNullNumberShouldReturnNull() {
		String inputNumber = null;

		DiscountCard actualDiscountCard = discountCardDAOImpl.getDiscountCardByNumber(inputNumber);

		assertThat(actualDiscountCard).isNull();
	}

	@Test
	public void checkGetDiscountCardByNumberOnBadNumberShouldReturnNull() {
		String inputNumber = "1235fg44";

		DiscountCard actualDiscountCard = discountCardDAOImpl.getDiscountCardByNumber(inputNumber);

		assertThat(actualDiscountCard).isNull();
	}

	@Test
	public void checkSaveDiscountCardShouldReturnSavedDiscountCard() {
		Long savingId = null;

		DiscountCard savingDiscountCard = DiscountCard.builder().id(savingId).number("15fd20193").discountValue(10)
				.build();

		DiscountCard savedDiscountCard = discountCardDAOImpl.saveDiscountCard(savingDiscountCard);

		assertAll(() -> {
			assertThat(savedDiscountCard).isNotNull();
			assertThat(savedDiscountCard.getId()).isGreaterThan(0L);
		});
	}

	@Test
	public void checkIsDiscountCardExistsShouldReturnTrue() {
		String inputNumber = "15fd20188";

		assertThat(discountCardDAOImpl.isDiscountCardExists(inputNumber)).isTrue();
	}

	@Test
	public void checkIsDiscountCardExistsOnNullCardNumberShouldReturnFalse() {
		String inputNumber = null;

		assertThat(discountCardDAOImpl.isDiscountCardExists(inputNumber)).isFalse();
	}

	@Test
	public void checkIsDiscountCardExistsOnBadCardNumberShouldReturnFalse() {
		String inputNumber = "12345678f";

		assertThat(discountCardDAOImpl.isDiscountCardExists(inputNumber)).isFalse();
	}
}
