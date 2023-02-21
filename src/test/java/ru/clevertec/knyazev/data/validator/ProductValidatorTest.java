package ru.clevertec.knyazev.data.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.ProductDTO;

public class ProductValidatorTest {
	private Validator<ProductDTO> productValidator;
	
	@BeforeEach
	public void setUp() {
		productValidator = new ProductValidator();
	}
	
	@ParameterizedTest
	@ValueSource(longs = {5L, 1L, 120L})
	public void checkValidateShouldDoesntThrowException(Long id) {
		ProductDTO productDTO = new ProductDTO(id);
		
		assertThatCode(() -> productValidator.validate(productDTO)).doesNotThrowAnyException();
	}
	
	@ParameterizedTest
	@NullSource
	@ValueSource(longs = {0L, -1L -235L})
	public void whenValidateNullProduct(Long id) {
		ProductDTO productDTO = new ProductDTO(id);
		
		assertThatThrownBy(() -> productValidator.validate(productDTO)).isInstanceOf(ValidatorException.class);
	}
}