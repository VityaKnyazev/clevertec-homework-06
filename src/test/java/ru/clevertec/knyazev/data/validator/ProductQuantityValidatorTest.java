package ru.clevertec.knyazev.data.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import ru.clevertec.knyazev.data.exception.ValidatorException;

public class ProductQuantityValidatorTest {
	private Validator<BigDecimal> productQuantityValidator;

	@BeforeEach
	public void setUp() {
		productQuantityValidator = new ProductQuantityValidator();
	}

	@ParameterizedTest
	@ValueSource(strings = { "12.568", "0.128", "0.001", "001.001" })
	public void checkValidateShouldDoesntThrowException(String productQuantity) {
		BigDecimal quantity = new BigDecimal(productQuantity);

		assertThatCode(() -> productQuantityValidator.validate(quantity)).doesNotThrowAnyException();
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "0.000", "0.0000000", "00.000", "0000.000", "-12.568", "-0.000" })
	public void checkValidateShouldThrowException(String productQuantity) {
		BigDecimal quantity;
		if (productQuantity == null) {
			quantity = null;
		} else {
			quantity = new BigDecimal(productQuantity);
		}

		assertThatThrownBy(() -> productQuantityValidator.validate(quantity)).isInstanceOf(ValidatorException.class);
	}

}
