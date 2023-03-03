package ru.clevertec.knyazev.data.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;

public class DiscountCardNumberValidatorTest {
	private Validator<DiscountCardDTO> discountCardNumberValidator;
	
	@BeforeEach
	public void setUp() {
		discountCardNumberValidator = new DiscountCardNumberValidator();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1234f6789", "fff555fff", "1234f6787", "ggggggggg"})
	public void checkValidateShouldDoesntThrowValidatorException(String cardNumber) {
		DiscountCardDTO discountCardDTO = new DiscountCardDTO(cardNumber);
		
		assertThatCode(() -> discountCardNumberValidator.validate(discountCardDTO)).doesNotThrowAnyException();
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"f", "fff555ffff", "1234f67879", " ", "", "123", "\r\n", "         ", "\r\n    \r\n "})
	public void checkValidateShouldThrowValidatorException(String cardNumber) {
		DiscountCardDTO discountCardDTO = new DiscountCardDTO(cardNumber);
		
		assertThatThrownBy(() -> discountCardNumberValidator.validate(discountCardDTO)).isInstanceOf(ValidatorException.class);
	}
}