package ru.clevertec.knyazev.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import ru.clevertec.knyazev.entity.Address;

@ExtendWith(MockitoExtension.class)
public class AddressDAOJPATest {
	@Mock
	private EntityManager entityManagerMock;
	@InjectMocks
	private AddressDAOJPA addressDAOJPA;

	@Test
	public void checkFindByIdShouldReturnAddress() {
		final Address expectedAddress = Address.builder().id(2L).postalCode("2512665").country("Belarus").city("Minsk")
				.street("Puskin prt.").buildingNumber("11").build();

		Mockito.when(entityManagerMock.find(Mockito.any(), Mockito.longThat(id -> (id != null) && (id > 0L))))
				.thenReturn(expectedAddress);

		final Long inputId = 2L;
		Address actualSavedAddress = addressDAOJPA.findById(inputId);

		assertThat(actualSavedAddress).isEqualTo(expectedAddress);
	}

	@Test
	public void checkFindByIdOnNullIdShouldReturnNull() {
		final Long inputId = null;
		assertThat(addressDAOJPA.findById(inputId)).isNull();
	}

	@Test
	public void checkFindByIdOnBadIdShouldReturnNull() {
		final Long inputId = 0L;
		assertThat(addressDAOJPA.findById(inputId)).isNull();
	}

}