package ru.clevertec.knyazev.data.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.data.exception.ValidatorException;

public class ProductQuantityValidatorTests {
	private Validator<BigDecimal> productQuantityValidator;
	
	@BeforeEach
	public void setUp() {
		productQuantityValidator = new ProductQuantityValidator();
	}
	
	@Test
	public void whenValidate() {
		BigDecimal quantity = new BigDecimal(12.568);
		
		assertDoesNotThrow(() -> productQuantityValidator.validate(quantity));
	}
	
	@Test
	public void whenValidateNull() {
		BigDecimal quantity = null;
		
		assertThrows(ValidatorException.class, () -> productQuantityValidator.validate(quantity));
	}
	
	@Test
	public void whenValidateZero() {
		BigDecimal quantity = new BigDecimal("0.000");
		
		assertThrows(ValidatorException.class, () -> productQuantityValidator.validate(quantity));
	}
	
	@Test
	public void whenValidateNegative() {
		BigDecimal quantity = new BigDecimal("-0.001");
		
		assertThrows(ValidatorException.class, () -> productQuantityValidator.validate(quantity));
	}
	
}
