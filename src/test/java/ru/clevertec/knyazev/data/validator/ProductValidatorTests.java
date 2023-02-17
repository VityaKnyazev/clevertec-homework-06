package ru.clevertec.knyazev.data.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.ProductDTO;

public class ProductValidatorTests {
	private Validator<ProductDTO> productValidator;
	
	@BeforeEach
	public void setUp() {
		productValidator = new ProductValidator();
	}
	
	@Test
	public void whenValidate() {
		final Long id = 5L;
		ProductDTO productDTO = new ProductDTO(id);
		
		assertDoesNotThrow(() -> productValidator.validate(productDTO));
	}
	
	@Test
	public void whenValidateNullProduct() {
		ProductDTO productDTO = null;
		
		assertThrows(ValidatorException.class, () -> productValidator.validate(productDTO));
	}
	
	@Test
	public void whenValidateNullProductId() {
		final Long id = null;
		ProductDTO productDTO = new ProductDTO(id);
		
		assertThrows(ValidatorException.class, () -> productValidator.validate(productDTO));
	}
	
	@Test
	public void whenValidateBadProductId() {
		final Long id = 0L;
		ProductDTO productDTO = new ProductDTO(id);
		
		assertThrows(ValidatorException.class, () -> productValidator.validate(productDTO));
	}
}
