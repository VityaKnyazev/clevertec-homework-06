package ru.clevertec.knyazev.data.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;

public class DiscountCardNumberValidatorTests {
	private Validator<DiscountCardDTO> discountCardNumberValidator;
	
	@BeforeEach
	public void setUp() {
		discountCardNumberValidator = new DiscountCardNumberValidator();
	}
	
	@Test
	public void whenValidate() {
		final String cardNumber = "1234f6789";
		final DiscountCardDTO discountCardDTO = new DiscountCardDTO(cardNumber);
		
		assertDoesNotThrow(() -> discountCardNumberValidator.validate(discountCardDTO));
	}
	
	@Test
	public void whenValidateNull() {
		final String cardNumber = null;
		final DiscountCardDTO discountCardDTO = new DiscountCardDTO(cardNumber);
		
		assertThrows(ValidatorException.class, () -> discountCardNumberValidator.validate(discountCardDTO));
	}
	@Test
	public void whenValidateEmpty() {
		final String cardNumber = "";
		final DiscountCardDTO discountCardDTO = new DiscountCardDTO(cardNumber);
		
		assertThrows(ValidatorException.class, () -> discountCardNumberValidator.validate(discountCardDTO));
	}
	
	@Test
	public void whenValidateWrongLength() {
		final String cardNumber = "123";
		final DiscountCardDTO discountCardDTO = new DiscountCardDTO(cardNumber);
		
		assertThrows(ValidatorException.class, () -> discountCardNumberValidator.validate(discountCardDTO));
	}
}